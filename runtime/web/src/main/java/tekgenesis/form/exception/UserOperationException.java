
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.exception;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;

/**
 * UserOperationException is the superclass of those exceptions that can be thrown during the
 * operation SuiGeneris forms. They must be internationalized as they will be shown to the end user.
 */
public abstract class UserOperationException extends RuntimeException {

    //~ Instance Fields ..............................................................................................................................

    private final Object[]          args;
    private final Enumeration<?, ?> message;

    //~ Constructors .................................................................................................................................

    protected UserOperationException(@NotNull Enumeration<?, ?> message, Object... args) {
        this.message = message;
        this.args    = args;
    }

    //~ Methods ......................................................................................................................................

    /** Returns default label. */
    @NotNull public final String getMessage() {
        return message.label(args);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1415949784591369695L;
}
