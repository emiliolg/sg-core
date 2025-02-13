
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.notEmpty;

/**
 * Phase Status It is only applies when the Task is transactional.
 */
public final class Status implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final String  msg;
    private final boolean mustContinue;

    private final short state;

    //~ Constructors .................................................................................................................................

    private Status(short state, boolean mustContinue, @NotNull String msg) {
        this.state        = state;
        this.msg          = msg;
        this.mustContinue = mustContinue;
    }

    //~ Methods ......................................................................................................................................

    /** @return  true if the execution must continue */
    public boolean mustContinue() {
        return mustContinue;
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public String toString() {
        switch (state) {
        case SUCCESS:
            return "Ok";
        case ERROR:
            return "Error: " + msg;
        case IGNORE:
            return "Ignore: " + msg;
        default:
            throw new IllegalStateException(format("invalid state %d", state));
        }
    }

    /** @return  true if the execution has been ignored */
    public boolean isIgnored() {
        return state == IGNORE;
    }

    /** @return  message */
    @NotNull public String getMessage() {
        return msg;
    }

    /** @return  true if the execution has been successful */
    public boolean isSuccess() {
        return state == SUCCESS;
    }

    //~ Methods ......................................................................................................................................

    /** @return  Status abort. The task execution will abort. */
    public static Status abort(@NotNull String msg) {
        return new Status(ERROR, false, msg);
    }

    /** Abort with an Exception. */
    public static Status abort(Exception e) {
        return abort(e.getMessage());
    }

    /** @return  Done Status. The task execution will stop in case of a processor task */
    public static Status done() {
        return new Status(SUCCESS, false, "");
    }

    /**
     * @return  Status error. The execution will continue in case of a lifecycle or processor task.
     */
    public static Status error(@NotNull String msg) {
        return new Status(ERROR, true, msg);
    }

    /**
     * @return  Ignore Status. The execution will ignore the current item and continue in case of a
     *          processor task
     */
    public static Status ignore(@Nullable String msg) {
        return new Status(IGNORE, true, notEmpty(msg, ""));
    }

    /** @return  Ok Status. The execution will continue in case of a processor task */
    public static Status ok() {
        return new Status(SUCCESS, true, "");
    }

    /**
     * @return  Undo Status. The execution will ignore the current item and stop in case of a
     *          processor task
     */
    public static Status undo(@Nullable String msg) {
        return new Status(IGNORE, false, notEmpty(msg, ""));
    }

    //~ Static Fields ................................................................................................................................

    private static final short SUCCESS = 0;
    private static final short ERROR   = 1;
    private static final short IGNORE  = 2;

    private static final long serialVersionUID = -1967466539519933845L;
}  // end class PhaseStatus
