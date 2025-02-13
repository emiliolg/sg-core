
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
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.InvokeData;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.CustomMsgType;
import tekgenesis.view.shared.feedback.FeedbackEventData;

import static java.lang.Math.max;
import static java.util.Objects.isNull;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;

/**
 * Payload response to send a {@link FormModel}.
 */
@SuppressWarnings({ "GwtInconsistentSerializableClass", "FieldMayBeFinal", "UnusedReturnValue" })
public class FormModelResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private FeedbackEventData await;

    private DetailResponse detail;

    private DownloadResponse download;

    private String                error;
    private List<String>          errorDetails;
    private boolean               inlineRedirect;
    private InvokeData            invoke;
    private LoadFormResponse      load;
    private InvokeData            method;
    private RedirectFormResponse  redirect;
    private boolean               reload;
    private SwipeResponse         swipe;
    private SyncFormResponse      sync;
    private TerminateFormResponse terminate;
    private String                url;

    //~ Constructors .................................................................................................................................

    /** Creates a FormModelResponse. */
    @SuppressWarnings("UnusedDeclaration")  // GWT
    public FormModelResponse() {
        redirect     = null;
        load         = null;
        sync         = null;
        url          = null;
        download     = null;
        invoke       = null;
        error        = null;
        errorDetails = null;
        terminate    = null;
        swipe        = null;
        detail       = null;
        method       = null;
        await        = null;
        reload       = false;
    }

    //~ Methods ......................................................................................................................................

    /** Await response. */
    public FormModelResponse await(final FeedbackEventData data) {
        await = data;
        return this;
    }

    /** Detail response. */
    public FormModelResponse detail(final DetailResponse response) {
        detail = response;
        return this;
    }

    /** Download location. */
    public FormModelResponse download(final DownloadResponse response) {
        download = response;
        return this;
    }

    /** Error response. */
    public FormModelResponse error(@NotNull final String msg, List<String> errors, boolean reloadOption) {
        error        = msg;
        errorDetails = errors;
        reload       = reloadOption;
        return this;
    }

    /** Return true if has download. */
    public boolean hasDownload() {
        return download != null;
    }

    /** Url redirect. */
    public FormModelResponse inlineRedirect(final boolean inline) {
        inlineRedirect = inline;
        return this;
    }

    /** Client side function invocation. */
    public FormModelResponse invoke(final InvokeData invocation) {
        invoke = invocation;
        return this;
    }

    /** Load response. */
    public FormModelResponse load(final LoadFormResponse response) {
        load = response;
        return this;
    }

    /** Server side asynchronous method invocation. */
    public FormModelResponse method(final Tuple<String, Integer> invocation) {
        method = new InvokeData(invocation.first());
        method.setDelay(max(invocation.second(), 0));
        return this;
    }

    /** Redirect response. */
    public FormModelResponse redirect(@Nullable final RedirectFormResponse redirection) {
        redirect = redirection;
        return this;
    }

    /** Swipe response. */
    public FormModelResponse swipe(@Nullable final SwipeResponse response) {
        swipe = response;
        return this;
    }

    /** Sync response. */
    public FormModelResponse sync(final SyncFormResponse response) {
        sync = response;
        return this;
    }

    /** Terminate response. */
    public FormModelResponse terminate(final TerminateFormResponse response) {
        terminate = response;
        return this;
    }

    /** Url redirect. */
    public FormModelResponse url(final String redirection) {
        url = redirection;
        return this;
    }

    /** Return await execution data if any. */
    public FeedbackEventData getAwait() {
        return await;
    }

    /** Get custom message. */
    @NotNull public CustomMsgType getCustomMsgOrElse(CustomMsgType type) {
        CustomMsgType msgType = null;

        if (load != null) msgType = load.getMsgTypeOrElse(type);
        if (sync != null) msgType = sync.getMsgTypeOrElse(type);
        if (terminate != null) msgType = terminate.getMsgTypeOrElse(type);
        if (redirect != null) msgType = redirect.getMsgTypeOrElse(type);

        return notNull(msgType, type);
    }

    /** Return true if the reload option is enabled. */
    public boolean isReload() {
        return reload;
    }

    /** Return detail response if any. */
    public DetailResponse getDetail() {
        return detail;
    }

    /** Return download response. */
    public DownloadResponse getDownload() {
        return download;
    }

    public boolean isAutoClose() {
        Boolean result = null;

        if (sync != null && (isNotEmpty(sync.getMessage()) || isError())) result = sync.isAutoClose();
        if (isNull(result) && terminate != null && isNotEmpty(terminate.getMessage())) result = terminate.isAutoClose();
        if (isNull(result) && redirect != null && isNotEmpty(redirect.getMessage())) result = redirect.isAutoClose();

        return result != null ? result : false;
    }

    /** Return error message if any. */
    public String getError() {
        return error;
    }

    /** Get list of error details. */
    public List<String> getErrorDetails() {
        return errorDetails;
    }

    /** Return client side function invocation. */
    public InvokeData getInvoke() {
        return invoke;
    }

    /** Return load response if any. */
    public LoadFormResponse getLoad() {
        return load;
    }

    /** Get message. */
    @Nullable public String getMessage() {
        String message = null;

        if (load != null) message = load.getMessage();
        if (isEmpty(message) && sync != null) message = sync.getMessage();
        if (isEmpty(message) && terminate != null) message = terminate.getMessage();
        if (isEmpty(message) && redirect != null) message = redirect.getMessage();
        if (isEmpty(message) && swipe != null) message = swipe.getMessage();
        if (isEmpty(message) && detail != null) message = detail.getMessage();

        return message;
    }

    /** Return server side method invocation. */
    public InvokeData getMethod() {
        return method;
    }

    /** Return true if response is error. */
    public boolean isError() {
        return isNotEmpty(error);
    }

    /** Return redirect response if any. */
    public RedirectFormResponse getRedirect() {
        return redirect;
    }

    /** Return swipe response if any. */
    public SwipeResponse getSwipe() {
        return swipe;
    }

    /** Return sync response if any. */
    public SyncFormResponse getSync() {
        return sync;
    }

    /** Return if the url should be shown inline in the form box. */
    public boolean isInlineRedirect() {
        return inlineRedirect;
    }

    /** Return terminate response if any. */
    public TerminateFormResponse getTerminate() {
        return terminate;
    }

    /** Return non-application url. */
    public String getUrl() {
        return url;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4208070278535087366L;
}  // end class FormModelResponse
