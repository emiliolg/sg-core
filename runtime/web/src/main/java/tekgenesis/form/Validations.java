
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;

/**
 * Result of a validation.
 */
public interface Validations {

    //~ Methods ......................................................................................................................................

    /** Get all errors. */
    @NotNull Seq<Validation> getAllMessages();

    /** Get errors for a specified field. */
    @NotNull Seq<Validation> getMessages(@NotNull final Enum<?> field);

    /** Returns true if the are no validations. */
    boolean isEmpty();

    //~ Enums ........................................................................................................................................

    /**
     * Validation Type.
     */
    enum ValidationType { ERROR, INFO, SUCCESS, WARNING }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Field validation.
     */
    interface Validation {
        /** Field Message. */
        String getMessage();
        /** Field ValidationType. */
        ValidationType getType();
    }
}
