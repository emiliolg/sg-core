
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

import static java.lang.String.format;

/**
 * Exception thrown when a searchable field is abstract but not read_only.
 */
public class IllegalDatabaseSearchableFieldType extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String modelName;

    //~ Constructors .................................................................................................................................

    /** Exception constructor with abstract searchable field name. */
    private IllegalDatabaseSearchableFieldType(String fieldName, String type, String modelName) {
        super(format("Searchable field '%s' with type '%s' is not allowed in database searchable entities.", fieldName, type));
        this.modelName = modelName;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.createError(getMessage(), modelName);
    }

    //~ Methods ......................................................................................................................................

    /** Entity fields are not allowed in database searchable clauses. */
    @NotNull public static IllegalDatabaseSearchableFieldType illegalEntity(String fieldName, String type, String modelName) {
        return new IllegalDatabaseSearchableFieldType(fieldName, type, modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3659873371415112716L;
}
