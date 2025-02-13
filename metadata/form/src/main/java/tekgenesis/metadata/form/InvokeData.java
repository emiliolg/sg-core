
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Holds client side function invocation data.
 */
@SuppressWarnings({ "FieldMayBeFinal", "ConstantConditions" })
public class InvokeData implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String args;
    private int             delay;
    @NotNull private String function;
    @NotNull private String target;

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    protected InvokeData() {
        function = null;
        target   = null;
        args     = null;
        delay    = 0;
    }

    /** Invoke data to be used by action. */
    public InvokeData(@NotNull final String function) {
        this.function = function;
        target        = "";
        args          = "";
        delay         = 0;
    }

    //~ Methods ......................................................................................................................................

    /** Get invocation arguments. */
    @NotNull public String getArgs() {
        return args;
    }

    /** Set invocation arguments. */
    public void setArgs(@NotNull String args) {
        this.args = args;
    }

    /** Get invocation delay. */
    public int getDelay() {
        return delay;
    }

    /** Set invocation delay. */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /** Get invocation name. */
    @NotNull public String getFunction() {
        return function;
    }

    /** Get invocation target. */
    @NotNull public String getTarget() {
        return target;
    }

    /** Set invocation target. */
    public void setTarget(@NotNull final String target) {
        this.target = target;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8695914307405769176L;
}  // end class InvokeData
