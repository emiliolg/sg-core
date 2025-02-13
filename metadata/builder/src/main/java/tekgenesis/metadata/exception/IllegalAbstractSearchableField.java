
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import static java.lang.String.format;

/**
 * Exception thrown when a searchable field is abstract but not read_only.
 */
public class IllegalAbstractSearchableField extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Exception constructor with abstract searchable field name. */
    public IllegalAbstractSearchableField(String fieldName) {
        super(format("Abstract searchable field '%s' must also be read_only.", fieldName), fieldName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3659873371415112716L;
}
