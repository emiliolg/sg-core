
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import org.apache.shiro.session.Session;

import tekgenesis.cluster.ClusterProps;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notEmpty;

/**
 * Dynamo DB session DAO.
 */
public class DynamoSessionDAO extends ExternalSessionDAO {

    //~ Instance Fields ..............................................................................................................................

    private final AmazonDynamoDBAsync dynamo;
    private final String              tableName;

    //~ Constructors .................................................................................................................................

    /** Constructor with cluster props. */
    public DynamoSessionDAO(int sessionTimeout, SSOProps props, ClusterProps clusterProps) {
        super(sessionTimeout);

        if (TEST.equals(clusterProps.awsAccessKey)) dynamo = new DynamoMemoryDB();
        else {
            if (!clusterProps.awsBucket.isEmpty()) {
                final BasicAWSCredentials          credentials = new BasicAWSCredentials(clusterProps.awsAccessKey, clusterProps.awsSecretKey);
                final AWSStaticCredentialsProvider provider    = new AWSStaticCredentialsProvider(credentials);
                dynamo = AmazonDynamoDBAsyncClientBuilder.standard().withCredentials(provider).build();
            }
            else dynamo = AmazonDynamoDBAsyncClientBuilder.defaultClient();
        }

        tableName = "sessions-" + props.dynamoSuffix;

        DynamoUtils.createTable(dynamo,
            tableName,
            new KeySchemaElement().withAttributeName(SESSION_ID_KEY).withKeyType(KeyType.HASH),
            Colls.listOf(new AttributeDefinition().withAttributeName(SESSION_ID_KEY).withAttributeType(ScalarAttributeType.S)).toList(),
            props.dynamoReadCapacity,
            props.dynamoWriteCapacity);
    }

    //~ Methods ......................................................................................................................................

    /** Delete session. */
    protected void deleteSession(String sessionId) {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(SESSION_ID_KEY, new AttributeValue(sessionId));

        final DeleteItemRequest request = new DeleteItemRequest(tableName, key);
        DynamoUtils.addClientMarker(request);

        try {
            dynamo.deleteItemAsync(request);
        }
        catch (final Exception e) {
            logger.warning("Unable to delete session " + sessionId, e);
        }
    }

    @Override
    @SuppressWarnings("MagicNumber")
    protected SessionData loadSessionData(Serializable sessionId) {
        final String                      id   = sessionId.toString();
        final Map<String, AttributeValue> item = loadItemBySessionId(id);
        if (item == null || !item.containsKey(SESSION_ID_KEY) || !item.containsKey(SESSION_DATA_ATTRIBUTE)) return null;

        final String createdAt = item.get(CREATED_AT_ATTRIBUTE).getN();

        final String host = item.get(HOST_ATTRIBUTE).getS();

        final ByteBuffer byteBuffer = item.get(SESSION_DATA_ATTRIBUTE).getB();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(byteBuffer.array());

        Object            readObject;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            readObject        = objectInputStream.readObject();
        }
        catch (final IOException | ClassNotFoundException e) {
            logger.error(e);
            return null;
        }
        finally {
            try {
                if (objectInputStream != null) objectInputStream.close();
            }
            catch (final Exception ignored) {}
        }

        final Map<String, Object> cast = cast(readObject);
        return new SessionData(createdAt, host, cast);
    }

    /** Store session. */
    @Override protected void storeSession(Session session, SessionData data)
        throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream    objectOutputStream    = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data.getAttributes());
        objectOutputStream.close();

        final byte[] byteArray = byteArrayOutputStream.toByteArray();

        final Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(SESSION_ID_KEY, new AttributeValue(session.getId().toString()));
        final ByteBuffer b = ByteBuffer.wrap(byteArray);
        attributes.put(SESSION_DATA_ATTRIBUTE, new AttributeValue().withB(b));
        attributes.put(CREATED_AT_ATTRIBUTE, new AttributeValue().withN(data.getCreatedAt()));
        attributes.put(LAST_UPDATED_AT_ATTRIBUTE, new AttributeValue().withN(Long.toString(session.getLastAccessTime().getTime())));
        attributes.put(TIMEOUT_ATTRIBUTE, new AttributeValue().withN(Long.toString(session.getTimeout())));
        attributes.put(HOST_ATTRIBUTE, new AttributeValue().withS(notEmpty(session.getHost(), "NA")));

        try {
            final PutItemRequest request = new PutItemRequest(tableName, attributes);
            DynamoUtils.addClientMarker(request);
            final Future<PutItemResult> future = dynamo.putItemAsync(request);
            future.get(1, TimeUnit.SECONDS);
        }
        catch (final Exception e) {
            logger.error(e);
        }
    }

    /** Load session. */
    private Map<String, AttributeValue> loadItemBySessionId(String sessionId) {
        final Map<String, AttributeValue> map = new HashMap<>();
        map.put(SESSION_ID_KEY, new AttributeValue(sessionId));
        final GetItemRequest request = new GetItemRequest(tableName, map);
        DynamoUtils.addClientMarker(request);

        try {
            final Future<GetItemResult> result = dynamo.getItemAsync(request);
            return result.get(2, TimeUnit.SECONDS).getItem();
        }
        catch (final Exception e) {
            logger.warning("Unable to load session " + sessionId, e);
        }

        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(DynamoSessionDAO.class);

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SESSION_ID_KEY = "sessionId";

    public static final String SESSION_DATA_ATTRIBUTE    = "sessionData";
    public static final String LAST_UPDATED_AT_ATTRIBUTE = "lastUpdatedAt";
    public static final String CREATED_AT_ATTRIBUTE      = "createdAt";
    public static final String HOST_ATTRIBUTE            = "host";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String TIMEOUT_ATTRIBUTE = "timeout";

    public static final String TEST           = "__test__";
    public static final int    THIRTY_MINUTES = 30;
    public static final int    SIXTY_MINUTES  = 30;
}  // end class DynamoSessionDAO
