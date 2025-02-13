
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

import tekgenesis.common.core.Enumeration;

import static tekgenesis.form.Download.DownloadWriter;

/**
 * A form navigation action.
 */
public interface Action {

    //~ Methods ......................................................................................................................................

    /**
     * Adds a server side function invocation to the action. Will be executed after given delay in
     * seconds.
     */
    @NotNull Action withAsynchronousInvocation(@NotNull final String method, final int delay);

    /** Allows to customize the action message. */
    @NotNull CustomMessage withCustomMessage(@NotNull final String message);

    /** Allows to customize the action message. */
    @NotNull CustomMessage withCustomMessage(@NotNull final Enumeration<?, ?> message);

    /** Allows to customize the action message. */
    @NotNull CustomMessage withCustomMessage(@NotNull final Enumeration<?, ?> message, Object... args);

    /** Adds stream download behavior to the action given a writer implementation. */
    @NotNull Download withDownload(@NotNull final Class<? extends DownloadWriter> writer);

    /** Adds a client side function invocation to the action. */
    @NotNull Invoke withInvocation(@NotNull final String function);

    /** Specifies work item reference manual handling. */
    @NotNull Action withManualHandling();

    /** Adds a message to the action. */
    @NotNull Action withMessage(@NotNull final String message);

    /** Adds a message to the action. */
    @NotNull Action withMessage(@NotNull final Enumeration<?, ?> message);

    /** Adds a message to the action. */
    @NotNull Action withMessage(@NotNull final Enumeration<?, ?> message, Object... args);

    /** Adds the option to refresh de page in the message. */
    @NotNull Action withReloadMessage(@NotNull String message);

    /** Adds a summary of errors message to the action. */
    @NotNull Action withSummary();

    /** Returns true if this action is an error one. */
    boolean isError();
}  // end interface Action
