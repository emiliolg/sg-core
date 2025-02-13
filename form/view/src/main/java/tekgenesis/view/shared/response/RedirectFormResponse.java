
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormAction;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * FormResponse with also a callback function.
 */
@SuppressWarnings("FieldMayBeFinal")
public class RedirectFormResponse extends LoadFormResponse {

    //~ Instance Fields ..............................................................................................................................

    private List<FormAction> actions;
    private String           callback;
    private boolean          confirmation;
    private boolean          dialog;
    private boolean          leave;
    private boolean          provided;

    private String targetFormBox;

    //~ Constructors .................................................................................................................................

    RedirectFormResponse() {
        targetFormBox = null;
        callback      = null;
        actions       = null;
        leave         = false;
    }

    /** Constructs a RedirectFormResponse from the FormModel. */
    public RedirectFormResponse(FormModel model) {
        this(model, null, null);
    }

    /** Constructs a RedirectFormResponse from the Form and the FormModel. */
    public RedirectFormResponse(FormModel model, Form form) {
        this(model, form, null);
    }

    /** Constructs a RedirectFormResponse from the Form, the FormModel and it's primary key. */
    public RedirectFormResponse(@NotNull FormModel model, @Nullable Form form, @Nullable String pk) {
        super(model, form, pk);
        targetFormBox = null;
        callback      = null;
        actions       = null;
    }

    //~ Methods ......................................................................................................................................

    /** Redirection provided by application. Distinguishes between user or default redirection. */
    public void asProvided() {
        provided = true;
    }

    /** Has callback mapping. */
    public boolean hasCallback() {
        return isNotEmpty(callback);
    }

    /** Has target form box. */
    public boolean hasTargetFormBox() {
        return isNotEmpty(targetFormBox);
    }

    /** Get if redirect action needs confirmation to proceed or not. */
    public boolean needsConfirmation() {
        return confirmation;
    }

    /** After redirection is processed in server side, send a sync to client. */
    public SyncFormResponse toSync() {
        return new SyncFormResponse(getModel());
    }

    /** Set redirect callback. */
    public RedirectFormResponse withCallback(@Nullable String function) {
        callback = function;
        return this;
    }

    /** Set redirect implicit callback. */
    public void withImplicitCallback() {
        callback = IMPLICIT_CALLBACK;
    }

    /** Get redirect callback. */
    public String getCallback() {
        return callback;
    }

    /** Set if redirect action needs confirmation to proceed or not. */
    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    /** True if redirection is provided by application. */
    public boolean isProvided() {
        return provided;
    }

    /** Set dialog navigation. */
    public void setDialog(boolean dialog) {
        this.dialog = dialog;
    }

    /** Returns true if leave. */
    public boolean isLeave() {
        return leave;
    }

    /** Get form actions. */
    public List<FormAction> getFormActions() {
        return actions;
    }

    /** Set form actions. */
    public void setFormActions(@NotNull final List<FormAction> acts) {
        actions = acts.isEmpty() ? null : acts;
    }

    /** True if set to dialog navigation. */
    public boolean isDialog() {
        return dialog;
    }

    /** Returns the Model. */
    public FormModel getInitializedModel(final Form meta) {
        if (getForm() != null) throw new IllegalStateException("If I have a Form, and you have a Form, there is no need to serialize the form.");
        return super.getModel(meta);
    }

    /** Set true if it's leave navigation. */
    public void setLeave(boolean l) {
        leave = l;
    }

    /** Get targeted formBox for redirect. */
    public String getTargetFormBox() {
        return targetFormBox;
    }

    /** Sets the target formBox id. */
    public void setTargetFormBox(final String targetFormBoxId) {
        targetFormBox = targetFormBoxId;
    }

    //~ Static Fields ................................................................................................................................

    public static final String IMPLICIT_CALLBACK = "Implicit Callback";

    private static final long serialVersionUID = 7005633027600760439L;
}  // end class RedirectFormResponse
