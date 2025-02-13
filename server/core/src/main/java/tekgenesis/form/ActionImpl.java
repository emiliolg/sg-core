
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
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.InvokeData;
import tekgenesis.metadata.form.widget.CustomMsgType;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.form.ActionType.ERROR;
import static tekgenesis.form.ActionType.STAY;
import static tekgenesis.form.Download.DownloadWriter;

/**
 * Immutable action implementation.
 */
public class ActionImpl implements Action {

    //~ Instance Fields ..............................................................................................................................

    private Option<CustomMessageImpl> customMsg;

    private Option<DownloadImpl> download;
    private boolean              errorSummary;

    private Option<InvokeImpl>             invoke;
    private Option<Tuple<String, Integer>> method;
    private Option<String>                 msg;
    private boolean                        reload;

    private final ActionType type;
    private boolean          workItemClose;

    //~ Constructors .................................................................................................................................

    ActionImpl(ActionType type) {
        this.type     = type;
        msg           = Option.empty();
        invoke        = Option.empty();
        download      = Option.empty();
        method        = Option.empty();
        workItemClose = true;
        errorSummary  = false;
        reload        = false;
        customMsg     = empty();
    }

    //~ Methods ......................................................................................................................................

    /** True if work item reference must be close. */
    public boolean shouldCloseWorkItem() {
        return workItemClose;
    }

    /**
     * Adds a server side asynchronous invocation to the action. Will be executed after given delay.
     */
    @NotNull @Override public Action withAsynchronousInvocation(@NotNull String m, int delay) {
        method = of(tuple(m, delay));
        return this;
    }

    @NotNull @Override public CustomMessage withCustomMessage(@NotNull final String message) {
        withMessage(message);
        return initCustomMsg();
    }

    @NotNull @Override public CustomMessage withCustomMessage(@NotNull final Enumeration<?, ?> message) {
        withMessage(message);
        return initCustomMsg();
    }

    @NotNull @Override public CustomMessage withCustomMessage(@NotNull final Enumeration<?, ?> message, Object... args) {
        withMessage(message, args);
        return initCustomMsg();
    }

    /** Adds stream download behavior to the action given a writer implementation. */
    @NotNull @Override public Download withDownload(@NotNull Class<? extends DownloadWriter> writer) {
        final DownloadImpl result = new DownloadImpl(writer);
        download = of(result);
        return result;
    }

    /** Append client side function invocation. */
    @NotNull public Invoke withInvocation(@NotNull final String function) {
        final InvokeImpl result = new InvokeImpl(function);
        invoke = of(result);
        return result;
    }

    /** Specifies work item reference manual handling. */
    @NotNull @Override public Action withManualHandling() {
        workItemClose = false;
        return this;
    }

    @NotNull @Override public Action withMessage(@NotNull final Enumeration<?, ?> message) {
        return withMessage(message.label());
    }

    /** Adds a message to the action. */
    @NotNull public Action withMessage(@NotNull final String message) {
        msg = of(message);
        return this;
    }

    @NotNull @Override public Action withMessage(@NotNull final Enumeration<?, ?> message, Object... args) {
        return withMessage(message.label(args));
    }

    @NotNull @Override public Action withReloadMessage(@NotNull String message) {
        reload = true;
        return withMessage(message);
    }

    @NotNull @Override public Action withSummary() {
        if (type == ERROR) errorSummary = true;
        return this;
    }

    /** Return asynchronous invocation data. */
    public Option<Tuple<String, Integer>> getAsynchronousInvocation() {
        return method;
    }

    /** Return custom message styling type. */
    public Option<CustomMsgType> getCustomMsgType() {
        return customMsg.map(CustomMessageImpl::getType);
    }

    /** True if Download. */
    public boolean isDownload() {
        return download.isPresent();
    }

    /** True if a reload was requested by the user. */
    public boolean isReload() {
        return reload;
    }

    /** Return download. */
    public Option<DownloadImpl> getDownload() {
        return download;
    }

    /** True if a message auto close was requested by the user. */
    public boolean isAutoClose() {
        if (isError()) return customMsg.isPresent() && customMsg.get().isAutoClose();
        return !customMsg.isPresent() || customMsg.get().isAutoClose();
    }

    /** Return client side function invocation. */
    @NotNull public Option<InvokeData> getInvokeData() {
        return invoke.map(InvokeImpl::generateInvokeData);
    }

    /** Get action message. */
    @NotNull public Option<String> getMsg() {
        return msg;
    }

    /** True if Error. */
    public boolean isError() {
        return ERROR == type;
    }

    /** Returns action type. */
    public ActionType getType() {
        return type;
    }

    /** True if Stay. */
    public boolean isStay() {
        return STAY == type;
    }

    /** True if a summary of errors was requested as message. */
    public boolean isWithSummary() {
        return errorSummary;
    }

    @NotNull private CustomMessage initCustomMsg() {
        final CustomMessageImpl customMessage = new CustomMessageImpl(type);
        customMsg = of(customMessage);
        return customMessage;
    }
}  // end class ActionImpl
