
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
 * Duplicate field exception.
 */
public class DuplicateFieldException extends BuilderException {

    //~ Constructors .................................................................................................................................

    protected DuplicateFieldException(@NotNull String message, @NotNull String model) {
        super(message, model);
    }

    private DuplicateFieldException(@NotNull String field, @NotNull String to, @NotNull String model) {
        super(ADDING_DUPLICATE_FIELD + field + to + model, field);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a new case duplicate field exception. */
    public static DuplicateFieldException onCase(@NotNull String field, @NotNull String model) {
        return new DuplicateFieldException(field, TO_CASE, model);
    }

    /** Creates a new entity duplicate field exception. */
    public static DuplicateFieldException onEntity(@NotNull String field, @NotNull String entity) {
        return new DuplicateFieldException(field, TO_ENTITY, entity);
    }

    /** Creates a new form duplicate field exception. */
    public static DuplicateFieldException onForm(@NotNull String field, @NotNull String form) {
        return new DuplicateFieldException(field, TO_FORM, form);
    }

    /** Creates a new menu duplicate field exception. */
    public static DuplicateFieldException onMenu(@NotNull String field, @NotNull String form) {
        return new DuplicateFieldException(field, TO_MENU, form);
    }

    /** Creates a new searchable duplicate field exception. */
    public static DuplicateFieldException onSearchable(@NotNull String field) {
        return new DuplicateFieldException(field, TO_SEARCHABLE_OPTION, "");
    }

    /** Creates a new type duplicate field exception. */
    public static DuplicateFieldException onType(@NotNull String field, @NotNull String entity) {
        return new DuplicateFieldException(field, TO_TYPE, entity);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}  // end class DuplicateFieldException
