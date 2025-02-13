
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

import tekgenesis.metadata.form.widget.CustomMsgType;

import static tekgenesis.metadata.form.widget.CustomMsgType.*;

/**
 * A custom message class.
 */
public class CustomMessageImpl implements CustomMessage {

    //~ Instance Fields ..............................................................................................................................

    private boolean       autoClose;
    private CustomMsgType type;

    //~ Constructors .................................................................................................................................

    /** Creates a custom message from an action type. */
    CustomMessageImpl(ActionType type) {
        this.type = resolveType(type);
        autoClose = type != ActionType.ERROR;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public CustomMessage error() {
        type = ERROR;
        return this;
    }

    @NotNull @Override public CustomMessage notSticky() {
        autoClose = true;
        return this;
    }

    @NotNull @Override public CustomMessage sticky() {
        autoClose = false;
        return this;
    }

    @NotNull @Override public CustomMessage success() {
        type = SUCCESS;
        return this;
    }

    @NotNull @Override public CustomMessage warning() {
        type = WARNING;
        return this;
    }

    /** Return custom message styling type. */
    @NotNull public CustomMsgType getType() {
        return type;
    }

    /** Return true if the message must auto close. */
    boolean isAutoClose() {
        return autoClose;
    }

    private CustomMsgType resolveType(ActionType actionType) {
        return actionType == ActionType.ERROR ? ERROR : SUCCESS;
    }
}  // end class CustomMessageImpl
