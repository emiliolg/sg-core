
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Exception to throw when a Field is not found.
 */
public class FieldNotFoundException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a NoDescribeByFieldException for an entity. */
    public FieldNotFoundException(String entity, String name) {
        super("Field not found or illegal: '" + name + "' in type: '" + entity + "'", name);
    }

    /** Creates a searchable field not found in entity exception. */
    private FieldNotFoundException(String entity, String fieldName, String modelName) {
        super("Searchable field '" + fieldName + "' not found in entity '" + entity + "'", modelName);
    }

    //~ Methods ......................................................................................................................................

    /** Searchable field not found in entity. */
    @NotNull public static FieldNotFoundException searchableFieldNotFound(String entity, String fieldName) {
        return new FieldNotFoundException(entity, fieldName, fieldName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4714024816463220557L;
}
