
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

import tekgenesis.field.TypeField;

/**
 * Duplicate attribute exception.
 */
public class FieldAlreadyBindException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new form duplicate field exception. */
    public FieldAlreadyBindException(@NotNull String field, @NotNull String form, @NotNull TypeField bind) {
        super("Duplicate binding for attribute '" + bind + "' on field '" + field + "' on form " + form, form);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
