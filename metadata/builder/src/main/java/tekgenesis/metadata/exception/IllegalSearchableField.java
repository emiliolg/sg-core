
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
public class IllegalSearchableField extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Exception constructor with abstract searchable field name. */
    private IllegalSearchableField(String fieldName) {
        super(format("Abstract searchable field '%s' is not allowed in database searchable entities.", fieldName), fieldName);
    }

    //~ Methods ......................................................................................................................................

    /** Abstract fields are not allowed in database searchable clauses. */
    @NotNull public static IllegalSearchableField illegalAbstract(String fieldName) {
        return new IllegalSearchableField(fieldName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3659873371415112716L;
}
