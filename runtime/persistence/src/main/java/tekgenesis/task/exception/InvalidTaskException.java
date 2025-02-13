
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.exception;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

/**
 * Task Already running exception.
 */
public class InvalidTaskException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create exception. */
    public InvalidTaskException(@NotNull String taskName) {
        super(format("Task '%s' is invalid.", taskName));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3918043235045394383L;
}
