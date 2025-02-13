
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

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.metadata.form.model.Model;

/**
 * A message class.
 */
public class MessageImpl implements Message {

    //~ Instance Fields ..............................................................................................................................

    private boolean inline;

    private final Model  model;
    private final String msg;
    private final int    ordinal;
    private CheckType    type;

    //~ Constructors .................................................................................................................................

    /** Creates a not inline error message. */
    MessageImpl(@NotNull final Model model, int ordinal, String msg) {
        this(model, ordinal, msg, false, CheckType.ERROR);
    }

    private MessageImpl(@NotNull final Model model, int ordinal, String msg, boolean inline, CheckType type) {
        this.model   = model;
        this.ordinal = ordinal;
        this.msg     = msg;
        this.inline  = inline;
        this.type    = type;
        apply();
    }

    //~ Methods ......................................................................................................................................

    /** Mutates message to an info one. */
    @NotNull @Override public Message info() {
        type = CheckType.INFO;
        return apply();
    }

    /** Mutates message to an inline one. */
    @NotNull @Override public Message inline() {
        inline = true;
        return apply();
    }

    /** Mutates message to a success one. */
    @NotNull @Override public Message success() {
        type = CheckType.SUCCESS;
        return apply();
    }

    /** Mutates message to a warning one. */
    @NotNull @Override public Message warning() {
        type = CheckType.WARNING;
        return apply();
    }

    private Message apply() {
        model.setFieldMsg(ordinal, new CheckMsg(inline, type, msg));
        return this;
    }
}  // end class MessageImpl
