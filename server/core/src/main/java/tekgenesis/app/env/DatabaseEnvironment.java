
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.env;

import java.io.*;
import java.net.InetAddress;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch.qos.logback.classic.Level;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.PropertiesEnvironment;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Reflection;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.type.Lob;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.database.Databases.openDefault;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * DatabaseEnvironment.
 */
public class DatabaseEnvironment extends PropertiesEnvironment {

    //~ Instance Fields ..............................................................................................................................

    private final ObjectMapper objectMapper = JsonMapping.json();

    //~ Constructors .................................................................................................................................

    /**  */
    public DatabaseEnvironment() {
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        final SimpleModule module = new SimpleModule("dbEnv", new Version(1, 0, 0, null, null, null));

        module.addDeserializer(Level.class, new JsonDeserializer<Level>() {
                @Override public Level deserialize(JsonParser p, DeserializationContext context)
                    throws IOException
                {
                    p.nextToken();
                    p.nextToken();
                    final Level level = Level.toLevel(p.getIntValue());
                    // Move token to next object
                    p.nextToken();
                    p.nextToken();
                    return level;
                }
            });
        objectMapper.registerModule(module);
    }

    //~ Methods ......................................................................................................................................

    /** Store variable in DB. */
    public <T> void put(@NotNull String scope, @NotNull String nm, @NotNull T value) {
        super.put(scope, nm, value);

        try {
            final String valueAsString = objectMapper.writeValueAsString(value);
            runInTransaction(() -> mergeProperties(openDefault(), scope, nm, value.getClass(), valueAsString));
        }
        catch (final JsonProcessingException e) {
            logger.warning(format("Unable to store property %s", scope), e);
        }
    }

    /**  */
    public void refresh() {
        super.refresh();

        runInTransaction(db ->
                openDefault().sqlStatement("select * from QName(SG,ENV_PROPERTY) where PROP_NODE= '%s'", getNode())
                             .forEach(this::putProperty, false));
    }

    private void mergeProperties(final Database db, @NotNull final String scope, @NotNull final String nm, final Class<?> clazz, final String value) {
        final Lob reader = value == null ? null : Lob.createClob(new StringReader(value), value.length());

        final String name          = nm.isEmpty() ? Utils.name(clazz) : nm;
        final String propScope     = notEmpty(scope, " ");
        final String canonicalName = clazz.getCanonicalName();
        final String node          = getNode();

        if (db.getDatabaseType() != DatabaseType.POSTGRES && db.getDatabaseType().supportsMerge())
            db.sqlStatement(
                    "merge into QName(SG,ENV_PROPERTY) E" +
                    " using Values(V, ? NODE, ? NAME,? SCOPE, ? VAL,? CLAZZ) on (V.NAME = PROP_NAME and V.SCOPE=PROP_SCOPE and V.NODE=PROP_NODE) " +
                    "when matched then update set PROP_VALUE = V.VAL ,PROP_CLASS=CLAZZ " +
                    "when not matched then  insert (PROP_NODE,PROP_NAME,PROP_SCOPE,PROP_VALUE,PROP_CLASS) values (V.NODE,V.NAME,V.SCOPE, V.VAL,V.CLAZZ)")
                .onArgs(node, name, propScope, reader, canonicalName)
                .execute();
        else {
            final String first = db.sqlStatement("select PROP_NAME from QName(SG,ENV_PROPERTY) where PROP_NAME=? and PROP_SCOPE=? and PROP_NODE=?")
                                 .onArgs(name, propScope, node)
                                 .get(String.class);

            if (first != null)
                db.sqlStatement("update QName(SG,ENV_PROPERTY) set PROP_VALUE=?,PROP_CLASS=? where PROP_NAME=? and PROP_SCOPE=? and PROP_NODE=?")
                    .onArgs(reader, canonicalName, name, propScope, node)
                    .execute();
            else
                db.sqlStatement("insert into QName(SG,ENV_PROPERTY) (PROP_NODE,PROP_NAME,PROP_SCOPE,PROP_VALUE,PROP_CLASS) values (?,?,?,?,?)")
                    .onArgs(node, name, propScope, reader, canonicalName)
                    .execute();
        }
    }  // end method mergeProperties

    private StepResult<Void> putProperty(final ResultSet rs)
        throws SQLException
    {
        final String propScope = rs.getString(3);
        final String propName  = rs.getString(2);
        final String className = rs.getString(5);
        Reader       reader    = null;
        if (openDefault().getDatabaseType().supportsLobs()) {
            final Clob clob = rs.getClob(4);
            if (clob != null) reader = clob.getCharacterStream();
        }
        else {
            final InputStream in = rs.getBinaryStream(4);
            if (in != null) reader = new InputStreamReader(in);
        }

        try {
            super.put(propName.trim(), propScope.trim(), objectMapper.readValue(reader, Reflection.findClass(className)));
        }
        catch (final Exception e) {
            logger.warning(format("Unable to recover stored property %s,scope %s, class %s", propName, propScope, className), e);
        }
        return StepResult.next();
    }

    private String getNode() {
        final ClusterManager<Address> singleton       = cast(Context.getSingleton(ClusterManager.class));
        final InetAddress             physicalAddress = singleton.getPhysicalAddress(singleton.getMember());
        return physicalAddress == null ? "N/A" : physicalAddress.getHostAddress();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(DatabaseEnvironment.class);
}  // end class DatabaseEnvironment
