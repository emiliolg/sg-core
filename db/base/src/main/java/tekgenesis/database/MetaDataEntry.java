
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.util.VersionString;
import tekgenesis.database.exception.DatabaseSchemaDoesNotExistsException;
import tekgenesis.database.type.Lob;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.util.VersionString.versionFrom;

/**
 * Metadata Table.
 */

public class MetaDataEntry {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final Lob overlaySql;
    @Nullable private final Lob schemaSql;

    @NotNull private final String        sha;
    @Nullable private final String       shaOverlay;
    @NotNull private final VersionString version;

    //~ Constructors .................................................................................................................................

    /** Create a MetadataTable Entry. */
    private MetaDataEntry(@NotNull VersionString version, @NotNull String sha, @Nullable String shaOverlay) {
        this(version, sha, shaOverlay, null, null);
    }

    /** Create a Full MetadataTable Entry. */
    MetaDataEntry(@NotNull VersionString version, @NotNull String sha, @Nullable String shaOverlay, @Nullable Lob schemaSql,
                  @Nullable Lob overlaySql) {
        this.shaOverlay = shaOverlay;
        this.version    = version;
        this.sha        = sha;
        this.schemaSql  = schemaSql;
        this.overlaySql = overlaySql;
    }

    //~ Methods ......................................................................................................................................

    /** Sha for Overlay Sql creation code /** Sha for Overlay Sql creation code *. */
    @Nullable public String getOverlaySha() {
        return shaOverlay;
    }

    /** The Sql for the Overlay. */
    @Nullable public Lob getOverlaySql() {
        return overlaySql;
    }

    /** The Sql for the Schema. */
    @Nullable public Lob getSchemaSql() {
        return schemaSql;
    }

    /** Sha for Sql creation code *. */
    @NotNull public String getSha() {
        return sha;
    }

    /** The version of the Entry. */
    @NotNull public VersionString getVersion() {
        return version;
    }

    void insert(Database db, String schema) {
        final TransactionManager tm = db.getTransactionManager();
        tm.runInTransaction(t ->
                db.sqlStatement("insert into TableName(%s, _METADATA) (VERSION, SHA, SHA_OVL, SCHEMA, OVERLAY) values ('%s', '%s', ?, ?, ?)",
                      schema.toUpperCase(),
                      version.toString(),
                      sha).onArgs(shaOverlay, schemaSql, overlaySql).executeDml());

        tm.getCurrentTransaction().ifPresent(Transaction::commit);
        tm.runInTransaction(t -> db.sqlStatement(UPDATE_TIME_STMT, schema.toUpperCase(), version.toString()).ignoreErrors(true).executeDml());
    }

    void update(Database db, String schema) {
        final TransactionManager tm = db.getTransactionManager();
        tm.runInTransaction(d ->
                db.sqlStatement(
                      " update TableName(%s, _METADATA)" +
                      "   set SHA     = '%s'," +
                      "       SHA_OVL = ?," +
                      "       SCHEMA  = ?," +
                      "       OVERLAY = ?" +
                      " where VERSION = '%s'",
                      schema.toUpperCase(),
                      sha,

                      version.toString()).onArgs(shaOverlay, schemaSql, overlaySql).executeDml());

        tm.runInTransaction(d -> db.sqlStatement(UPDATE_TIME_STMT, schema.toUpperCase(), version.toString()).ignoreErrors(true).executeDml());
    }

    //~ Methods ......................................................................................................................................

    /** Retrieve the last Entry from the Database. */
    @Nullable public static MetaDataEntry dbVersion(Database db, String schema, boolean loadLobs) {
        try {
            final String extraFields = loadLobs ? ",SCHEMA, OVERLAY" : "";
            return db.getTransactionManager().invokeInTransaction(t ->
                    db.asNotDry()
                      .sqlStatement("select VERSION, SHA, SHA_OVL %s from TableName(%s, _METADATA)", extraFields, schema.toUpperCase())
                      .ignoreErrors(true)                                                            //
                      .list(loadLobs ? MetaDataEntry::fullEntryMapper : MetaDataEntry::entryMapper)  //
                      .max(VERSION_COMPARATOR)                                                       //
                      .getOrNull());
        }
        catch (final DatabaseSchemaDoesNotExistsException exception) {
            return null;
        }
    }

    @NotNull private static MetaDataEntry entryMapper(final ResultSet rs)
        throws SQLException
    {
        return new MetaDataEntry(versionFrom(rs.getString(1)), rs.getString(2), rs.getString(3));
    }

    @NotNull private static MetaDataEntry fullEntryMapper(final ResultSet rs)
        throws SQLException
    {
        return new MetaDataEntry(versionFrom(rs.getString(1)),
            rs.getString(2),
            rs.getString(3),
            Lob.createClob(rs.getString(4)),
            Lob.createClob(rs.getString(5)));
    }

    //~ Static Fields ................................................................................................................................

    private static final String UPDATE_TIME_STMT = "update TableName(%s, _METADATA) set UPDATE_TIME = CurrentTime where VERSION = '%s'";

    private static final Comparator<MetaDataEntry> VERSION_COMPARATOR = (o1, o2) -> Predefined.compare(o1.getVersion(), o2.getVersion());
}  // end class MetaDataEntry
