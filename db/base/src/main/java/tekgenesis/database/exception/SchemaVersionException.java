
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

import tekgenesis.common.util.VersionString;

import static java.lang.String.format;

/**
 * Current schema different from last version.
 */
public class SchemaVersionException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    private SchemaVersionException(String msg, Object... args) {
        super(format(msg, args));
    }

    //~ Methods ......................................................................................................................................

    /** Schema version problem, current and last version are different. */
    public static SchemaVersionException currentAndCreatedMismatch(String schema) {
        return new SchemaVersionException("SQL definition for schema '%s' does not match Database one. Evolve or Drop database", schema);
    }

    /** New Sql version must be greater than current one. */
    public static SchemaVersionException downgradingVersion(String schema, VersionString dbVersion, VersionString sqlVersion) {
        return new SchemaVersionException("The SQL version (%s) for schema '%s' must be greater than the current one (%s).",
            sqlVersion,
            schema,
            dbVersion);
    }

    /** Missing delta files. */
    public static SchemaVersionException missingDeltaFiles(String schema) {
        return new SchemaVersionException("The database schema '%s' does not have deltas to update it.", schema);
    }

    /** Schema version problem, current and last version are different. */
    public static SchemaVersionException shouldUpgrade(String schema, String current, String last) {
        return new SchemaVersionException("Schema '%s' is not at current ('%s') but at ('%s') and database.autoUpgrade is false",
            schema,
            current,
            last);
    }

    /** Sql files do not match. */
    public static SchemaVersionException sqlFilesDoNotMatch(String schema, VersionString version) {
        return new SchemaVersionException("The SQL definition file for schema '%s' version '%s' do not match with the current one", schema, version);
    }

    /** Sql files do not match. */
    public static SchemaVersionException sqlFilesDoNotMatch(String schema, VersionString version, final String diff) {
        return new SchemaVersionException("Schema '%s' version '%s' have different Sql files in project and database\n%s", schema, version, diff);
    }

    /** Schema version problem, current and last version are different. */
    public static SchemaVersionException versionAndCurrentMismatch(String schema, VersionString current, VersionString last) {
        return new SchemaVersionException("SQL files for current and last version differ for schema '%s'.\nCurrent '%s'.\nLast '%s'.",
            schema,
            current,
            last);
    }

    /** Version file for 'schema' does not exist. */
    public static SchemaVersionException versionFileMissing(String schema) {
        return new SchemaVersionException("Not versioned SQL for schema '%s'", schema);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 955985923197440925L;
}  // end class SchemaVersionException
