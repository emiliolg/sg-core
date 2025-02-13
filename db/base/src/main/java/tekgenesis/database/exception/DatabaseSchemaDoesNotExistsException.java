
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

/**
 * Exceptions of DatabaseHandling.
 */
public class DatabaseSchemaDoesNotExistsException extends DatabaseException {

    //~ Instance Fields ..............................................................................................................................

    private final String schema;

    //~ Constructors .................................................................................................................................

    /** Create the exception. */
    public DatabaseSchemaDoesNotExistsException(Throwable cause, String schema) {
        super(cause, true);
        this.schema = schema;
    }

    //~ Methods ......................................................................................................................................

    /** The schema. */
    public String getSchema() {
        return schema;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1617878670107143016L;
}
