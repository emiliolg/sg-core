
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.CustomMsgType;
import tekgenesis.metadata.form.widget.Form;

import static tekgenesis.common.Predefined.notNull;

/**
 * Response with a FormModel.
 */
@SuppressWarnings({ "FieldMayBeFinal", "GwtInconsistentSerializableClass" })
abstract class FormResponse implements MessagedResponse, Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean autoClose;

    private String fqn;

    private FormModel     model;
    private String        msg;
    private CustomMsgType msgType;

    //~ Constructors .................................................................................................................................

    FormResponse() {
        model   = null;
        msg     = null;
        fqn     = null;
        msgType = null;
    }

    /** Constructs a FormResponse from the FormModel. */
    FormResponse(FormModel model) {
        this.model = model;
        msg        = null;
        fqn        = model.getFormFullName();
        autoClose  = true;
        msgType    = null;
    }

    //~ Methods ......................................................................................................................................

    public boolean accepts(FormModel other) {
        return model.accepts(other);
    }

    /** Sets this response to autoClose its message. */
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    /** Returns true if this response is set to autoClose its message. */
    public boolean isAutoClose() {
        return autoClose;
    }

    /** Returns the model's form full-qualified-name. */
    public String getFqn() {
        return fqn;
    }

    @Override public String getMessage() {
        return msg;
    }

    @Override public void setMessage(final String msg) {
        this.msg = msg;
    }

    /** Sets the style type of the response. */
    public void setMsgType(@Nullable CustomMsgType msgType) {
        this.msgType = msgType;
    }

    /** Returns the style type of the response. */
    @NotNull public CustomMsgType getMsgTypeOrElse(@NotNull CustomMsgType type) {
        return notNull(msgType, type);
    }

    public String getParameters() {
        return model.getParameters();
    }

    public String getPk() {
        return model.getPk();
    }

    /** Returns the Model. */
    FormModel getModel(final Form meta) {
        model.init(meta);
        return model;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8005633027330760439L;
}  // end class FormResponse
