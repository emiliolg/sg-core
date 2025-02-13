
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.util.Collection;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

import tekgenesis.common.logging.Logger;

/**
 * Utilities for working with Amazon DynamoDB for session management.
 */
public class DynamoUtils {

    //~ Constructors .................................................................................................................................

    private DynamoUtils() {}

    //~ Methods ......................................................................................................................................

    /** Add client marker. */
    public static void addClientMarker(AmazonWebServiceRequest request) {
        request.getRequestClientOptions().appendUserAgent("DynamoSessionManager/1.0");
    }

    /** Create table. */
    public static void createTable(AmazonDynamoDB dynamo, String tableName, KeySchemaElement key, Collection<AttributeDefinition> attributes,
                                   long readCapacityUnits, long writeCapacityUnits) {
        if (!doesTableExist(dynamo, tableName)) {
            try {
                final CreateTableRequest request = new CreateTableRequest().withTableName(tableName);
                addClientMarker(request);

                request.withKeySchema(key);

                request.withAttributeDefinitions(attributes);

                request.setProvisionedThroughput(
                    new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits).withWriteCapacityUnits(writeCapacityUnits));

                dynamo.createTable(request);

                waitForTableToBecomeActive(dynamo, tableName);
            }
            catch (final AmazonClientException e) {
                logger.error(e);
                throw e;
            }
        }
    }

    /** Check table exists. */
    public static boolean doesTableExist(AmazonDynamoDB dynamo, String tableName) {
        try {
            final DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
            addClientMarker(request);

            final TableDescription table = dynamo.describeTable(request).getTable();
            return table != null;
        }
        catch (final AmazonServiceException ase) {
            if (ase.getErrorCode().equalsIgnoreCase(RESOURCE_NOT_FOUND_EXCEPTION)) return false;
            else throw ase;
        }
    }

    /** Wait for table to be ready. */
    public static void waitForTableToBecomeActive(AmazonDynamoDB dynamo, String tableName) {
        final long startTime = System.currentTimeMillis();
        @SuppressWarnings("MagicNumber")
        final long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime) {
            try {
                final DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                addClientMarker(request);

                final TableDescription tableDescription = dynamo.describeTable(request).getTable();
                if (tableDescription == null)  // noinspection ContinueStatement
                    continue;

                final String tableStatus = tableDescription.getTableStatus();
                if (tableStatus.equals(TableStatus.ACTIVE.toString())) return;
            }
            catch (final AmazonServiceException ase) {
                if (!ase.getErrorCode().equalsIgnoreCase(RESOURCE_NOT_FOUND_EXCEPTION)) throw ase;
            }

            try {
                Thread.sleep(1000 * 5);
            }
            catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AmazonClientException("Interrupted while waiting for table '" + tableName + "' to become active.", e);
            }
        }

        throw new AmazonClientException(TABLE + tableName + "' never became active");
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(DynamoUtils.class);

    public static final String RESOURCE_NOT_FOUND_EXCEPTION = "ResourceNotFoundException";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String TABLE = "Table '";
}  // end class DynamoUtils
