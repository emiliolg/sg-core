
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.env.Environment;
import tekgenesis.common.util.Diff;
import tekgenesis.common.util.Files;
import tekgenesis.common.util.VersionString;
import tekgenesis.database.type.Lob;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.util.VersionString.VERSION_ONE;
import static tekgenesis.common.util.VersionString.versionFrom;
import static tekgenesis.database.exception.SchemaVersionException.*;

/**
 * Class to manage the creation or upgrade of the database.
 */
@SuppressWarnings("WeakerAccess")
public class SchemaDefinition {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final SchemaVersion current;

    @NotNull private final Database db;
    private final boolean           doNotUpgrade;

    private final boolean     enforceVersion;
    private final Environment env;
    private boolean           ignoreErrors;

    @NotNull private final String schema;

    @Nullable private TreeSet<VersionString> versions;

    //~ Constructors .................................................................................................................................

    /** Creates a new Schema Definition over the given schema. */
    public SchemaDefinition(@NotNull Database db, @NotNull String schema, Environment env) {
        this.db        = db;
        this.schema    = schema.toUpperCase();
        enforceVersion = db.getConfiguration().enforceVersion;
        doNotUpgrade   = !db.getConfiguration().autoUpgrade;
        current        = schemaVersionCurrent(VERSION_ONE, null);
        versions       = null;
        ignoreErrors   = false;
        this.env       = env;
    }

    //~ Methods ......................................................................................................................................

    /** Check for create or update. */
    public ChangeLevel checkVersion() {
        final MetaDataEntry dbVersion = MetaDataEntry.dbVersion(db, schema, false);

        // Check if up to date
        if (current.isCurrent(dbVersion)) return ChangeLevel.NONE;

        final VersionString lv = lastVersion();

        final SchemaVersion lastVersion = lv == null ? null : schemaVersion(lv);

        if (dbVersion == null) {
            schemaStructureCreate(lastVersion);
            return ChangeLevel.MINOR;
        }

        if (lastVersion == null) throw currentAndCreatedMismatch(schema);

        return schemaStructureUpdate(dbVersion, lastVersion);
    }  // end method checkVersion

    /**
     * Create an schema with the current version, Use the specified resource path to search for the
     * sql files.
     */
    public void createSchema(List<File> resourcePath) {
        createSchema(schemaVersionCurrent(VERSION_ONE, resourcePath));
    }

    /** Create an schema with the specified version. */
    public void createSchema(VersionString version) {
        createSchema(schemaVersion(version));
    }

    /** Returns first schema version. */
    @NotNull public VersionString firstVersion() {
        final TreeSet<VersionString> vs = findVersions();
        if (vs.isEmpty()) {
            if (enforceVersion) throw versionFileMissing(schema);
            return VERSION_ONE;
        }
        return vs.first();
    }

    /** Ignore errors while executing Sql files. */
    public SchemaDefinition ignoreErrors() {
        ignoreErrors = true;
        return this;
    }

    /** Return last version. */
    @Nullable public VersionString lastVersion() {
        final TreeSet<VersionString> vs = findVersions();
        if (vs.isEmpty()) {
            if (enforceVersion) throw versionFileMissing(schema);
            return null;
        }
        return vs.last();
    }

    private void checkSqlFiles(final VersionString version) {
        final MetaDataEntry metadata = MetaDataEntry.dbVersion(db, schema, true);
        if (metadata == null) throw sqlFilesDoNotMatch(schema, version);

        if (!equal(current.mainSha(), metadata.getSha())) checkSqlFiles(version, current.mainLob(), metadata.getSchemaSql());

        if (!equal(current.overlaySha(), metadata.getOverlaySha())) checkSqlFiles(version, current.overlayLob(), metadata.getOverlaySql());
    }

    private void checkSqlFiles(final VersionString version, @Nullable final Lob newSqlFile, @Nullable final Lob currentSqlFile) {
        final ImmutableList<String>             newSql = Files.readLines(newSqlFile == null ? null : newSqlFile.getReader());
        final ImmutableList<String>             dbSql  = Files.readLines(currentSqlFile == null ? null : currentSqlFile.getReader());
        final ImmutableList<Diff.Delta<String>> deltas = Diff.ignoreAllSpace().diff(newSql, dbSql);
        if (deltas.size() > 1) throw sqlFilesDoNotMatch(schema, version, Diff.makeString(deltas));
    }

    private void createSchema(SchemaVersion schemaVersion) {
        final TransactionManager tm = db.getTransactionManager();
        tm.runInTransaction(t -> db.getDatabaseType().createSchema(db, schema, env.get(schema, SchemaProps.class).tableTablespace));
        tm.runInTransaction(t -> schemaVersion.executeSql(ignoreErrors));
        schemaVersion.updateMetadataEntry(true);
    }

    private Collection<VersionString> findDeltas(VersionString first, VersionString last) {
        final SortedSet<VersionString> deltas = VersionString.findVersions(DATABASE_DIR, DELTA_DIR, schemaFileName(schema))
                                                .subSet(first, false, last, true);
        if (deltas.isEmpty() || !deltas.last().equals(last)) throw missingDeltaFiles(schema);
        return deltas;
    }

    private TreeSet<VersionString> findVersions() {
        if (versions == null) versions = VersionString.findVersions(DATABASE_DIR, VERSION_DIR, schemaFileName(schema));
        return versions;
    }

    private void schemaStructureCreate(@Nullable SchemaVersion lv) {
        if (lv == null) createSchema(current);
        else if (lv.shaMatches(current)) createSchema(lv);
        else {
            if (enforceVersion) throw versionAndCurrentMismatch(schema, current.getVersion(), lv.getVersion());
            createSchema(schemaVersionCurrent(lv.getVersion().increment(true), null));
        }
    }  // end method schemaStructureCreate

    private ChangeLevel schemaStructureUpdate(@NotNull MetaDataEntry metadata, @NotNull SchemaVersion schemaVersion) {
        final VersionString dbVersion   = metadata.getVersion();
        final VersionString lastVersion = schemaVersion.getVersion();

        if (dbVersion.equals(lastVersion)) {
            checkSqlFiles(dbVersion);
            current.updateMetadataEntry(false);
            return ChangeLevel.NONE;
        }

        if (dbVersion.compareTo(lastVersion) >= 0) throw downgradingVersion(schema, dbVersion, lastVersion);

        if (!schemaVersion.shaMatches(current)) {
            if (enforceVersion) throw versionAndCurrentMismatch(schema, lastVersion, dbVersion);
            throw currentAndCreatedMismatch(schema);
        }

        if (doNotUpgrade) throw shouldUpgrade(schema, current.toString(), schemaVersion.toString());

        for (final VersionString delta : findDeltas(dbVersion, lastVersion)) {
            db.getTransactionManager().runInTransaction(t -> schemaVersionDelta(delta).executeSql(ignoreErrors));
            schemaVersion(delta).updateMetadataEntry(true);
        }
        return lastVersion.isMinor(dbVersion) ? ChangeLevel.MINOR : ChangeLevel.MAJOR;
    }

    private SchemaVersion schemaVersion(final VersionString lv) {
        return new SchemaVersion(lv, versionDir(new File(DATABASE_DIR), lv), db, schema, false, null);
    }

    private SchemaVersion schemaVersionCurrent(VersionString version, @Nullable List<File> resourcePath) {
        return new SchemaVersion(version, new File(DB_CURRENT), db, schema, false, resourcePath);
    }

    private SchemaVersion schemaVersionDelta(final VersionString delta) {
        return new SchemaVersion(delta, versionDirDelta(new File(DATABASE_DIR), delta), db, schema, true, null);
    }

    //~ Methods ......................................................................................................................................

    /** Given a 'current' SqlFile returns the list of versions for that file. */
    @NotNull public static TreeMap<VersionString, File> findVersions(final File sqlFile) {
        final File pf    = sqlFile.getParentFile();
        final File dbDir = pf == null ? null : pf.getParentFile();
        if (dbDir == null) throw new IllegalArgumentException(sqlFile.getPath());

        final String                       name         = sqlFile.getName();
        final int                          prefixLength = dbDir.getPath().length() + 2;
        final TreeMap<VersionString, File> result       = new TreeMap<>();

        for (final String file : Files.list(dbDir, ".*/v.*/" + VERSION_DIR + name)) {
            final String v   = file.substring(prefixLength);
            final int    end = v.indexOf('/');
            if (end != -1) {
                try {
                    final VersionString version = versionFrom(v.substring(0, end));
                    result.put(version, new File(file));
                }
                catch (final IllegalArgumentException ignore) {}
            }
        }
        return result;
    }

    /** Directory for specified version. */
    public static File versionDir(final File baseDir, final VersionString version) {
        return new File(new File(baseDir, "v" + version), VERSION_DIR);
    }

    /** Directory for specified delta version. */
    public static File versionDirDelta(final File baseDir, final VersionString version) {
        return new File(new File(baseDir, "v" + version), DELTA_DIR);
    }

    /** Returns the schema file name. */
    static String overlayFileName(final String schema) {
        return schema.toLowerCase() + "_ovl.sql";
    }

    /** Returns the schema file name. */
    static String schemaFileName(final String schema) {
        return schema.toLowerCase() + ".sql";
    }

    //~ Static Fields ................................................................................................................................

    public static final String VERSION_DIR = "version/";
    public static final String DELTA_DIR   = "delta/";

    private static final String DATABASE_DIR = "db/";
    public static final String  DB_CURRENT   = DATABASE_DIR + "current/";

    //~ Enums ........................................................................................................................................

    public enum ChangeLevel { NONE, MINOR, MAJOR }
}  // end class DatabaseSchemaDefinition
