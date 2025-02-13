
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Stack;
import tekgenesis.common.core.UUID;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.InvokeData;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.ToggleButtonType;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.event.*;
import tekgenesis.view.client.service.ClientFormService;
import tekgenesis.view.client.service.ClientSocketService;
import tekgenesis.view.client.service.DefaultCallback;
import tekgenesis.view.client.ui.ButtonUI;
import tekgenesis.view.client.ui.FormUI;
import tekgenesis.view.client.ui.HasLinkUI;
import tekgenesis.view.client.ui.ToggleButtonUI;
import tekgenesis.view.client.ui.WidgetUI;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.DownloadResponse;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.LoadFormResponse;
import tekgenesis.view.shared.response.RedirectFormResponse;
import tekgenesis.view.shared.response.ResponseError;
import tekgenesis.view.shared.response.SyncFormResponse;

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;
import static com.google.gwt.core.client.ScriptInjector.fromString;
import static com.google.gwt.core.client.ScriptInjector.fromUrl;
import static com.google.gwt.user.client.Cookies.setCookie;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.CLASS;
import static tekgenesis.common.core.Constants.SCRIPT;
import static tekgenesis.common.core.Constants.SUI_SERVICE_CACHE;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.model.FormConstants.FORM_LIFECYCLE_COOKIE;
import static tekgenesis.metadata.form.widget.ButtonType.*;
import static tekgenesis.metadata.form.widget.ToggleButtonType.DEPRECATE;
import static tekgenesis.metadata.form.widget.WidgetType.BUTTON;
import static tekgenesis.type.permission.PredefinedPermission.CREATE;
import static tekgenesis.type.permission.PredefinedPermission.HANDLE_DEPRECATED;
import static tekgenesis.type.permission.PredefinedPermission.UPDATE;
import static tekgenesis.view.client.Application.getInstance;
import static tekgenesis.view.client.ClientUiModelContext.getRetriever;
import static tekgenesis.view.shared.FormHandling.clearTransientCache;
import static tekgenesis.view.shared.FormHandling.init;
import static tekgenesis.view.shared.FormHandling.initMetadata;
import static tekgenesis.view.shared.FormHandling.updateCache;

/**
 * FormBox.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "NonJREEmulationClassesInClientCode" })
public abstract class FormBox extends FormBus {

    //~ Instance Fields ..............................................................................................................................

    Stack<CallbackStackItem> callbacks;

    BoxConfiguration configuration;

    final FlowPanel         panel;
    final ClientFormService service;

    private FormController       current;
    private final FutureCallback future;

    private final SteppedTimer futures;

    private final List<FormBoxListener>   listeners;
    private final LoadFormCallback        load;
    private final ClientSocketService     socketService;
    private final SyncFormCallback        sync;
    private final TerminationFormCallback termination;

    //~ Constructors .................................................................................................................................

    FormBox() {
        this(Stack.createStack());
    }

    FormBox(@NotNull final Stack<CallbackStackItem> callbacks) {
        this.callbacks = callbacks;
        panel          = new FlowPanel();
        service        = new ClientFormService(this);
        socketService  = new ClientSocketService(this);
        listeners      = new LinkedList<>();

        termination = new TerminationFormCallback();
        sync        = new SyncFormCallback();
        load        = new LoadFormCallback();
        future      = new FutureCallback();

        futures = new SteppedTimer(service);

        configuration = null;
        current       = null;
    }

    //~ Methods ......................................................................................................................................

    /** Attach box to specified parent. */
    public void attach(HasWidgets parent) {
        parent.add(panel);
        getInstance().attach(this);

        if (isNotEmpty(configuration.getInitialFqn())) load(configuration.getInitialFqn(), configuration.getInitialPk());
    }

    /** Calls cancel action. */
    public void cancel() {
        if (current != null) current.cancel(NONE);
    }

    /** Detach box from parent. */
    public void detach() {
        if (current != null) current.beforeModelUnload();
        panel.removeFromParent();
        getInstance().detach(this);
    }

    /** Load form with pk. */
    public void load(@NotNull final String fqn, @Nullable final String pk) {
        load(fqn, pk, null);
    }

    /** Load form with pk and parameters. */
    public void load(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        clearStack();
        service.loadForm(fqn, pk, parameters, load);
    }

    @Override public void onCancel(CancelEvent e) {
        disableActionButtons();

        final FormModelResponse response = createStackResponse(e.getModel());
        service.cancelForm(e.getSourceWidget(), response, termination);
    }

    @Override public void onDelete(DeleteEvent e) {
        disableActionButtons();

        final FormModelResponse response = createStackResponse(e.getModel());
        service.deleteForm(e.getSourceWidget(), response, termination);
    }

    @Override public void onRowChange(OnRowChangeEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onRowChangeSync(e.getSourceWidget(), response, sync);
    }

    @Override public void onRowSelected(OnRowSelectedEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onRowSelectedSync(e.getSourceWidget(), response, sync);
    }

    @Override public void onSubmit(SubmitEvent e) {
        disableActionButtons();

        final FormModelResponse response = createStackResponse(e.getModel());
        service.postForm(e.getSourceWidget(), response, e.getAction(), termination);
    }

    @Override public void onSyncTriggered(@NotNull OnClickSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onClickSync(e.getSourceWidget(), e.isFeedback(), response, sync);
    }

    @Override public void onSyncTriggered(@NotNull OnBlurSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onBlurSync(e.getSourceWidget(), response, sync);
    }

    @Override public void onSyncTriggered(@NotNull OnNewLocationSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onNewLocationSync(e.getSourceWidget(), e.getLat(), e.getLng(), response, sync);
    }

    @Override public void onSyncTriggered(@NotNull LazyFetchSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.lazyFetchSync(e.getSourceWidget(), e.getOffset(), e.getLimit(), response, sync);
    }

    @Override public void onSyncTriggered(@NotNull OnAbstractSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onAbstractSync(e.getDelegateWidget(), e.getSourceWidget(), response, sync);
    }

    @Override public void onSyncTriggered(@NotNull OnScheduleSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        socketService.onScheduleSync(response, sync);
    }

    @Override public void onSyncTriggered(@NotNull OnChangeSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onChangeSync(e.getSourceWidgets(), response, sync);
    }

    @Override public void onSyncTriggered(OnSuggestNewSyncEvent e) {
        disableActionButtons();

        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onNewSync(e.getSourceWidget(), response, e.getText(), sync);
    }

    @Override public void onSyncTriggered(OnLoadFormSyncEvent e) {
        final FormModelResponse response = createSyncResponse(e.getModel());
        service.onLoadSync(e.getSourceWidget(), response, e.getPk(), sync);
    }

    /** Returns current form controller. */
    public FormController getCurrent() {
        return current;
    }

    /** Is this FormBox listening to history? */
    public boolean isBoundedByHistory() {
        return configuration != null && configuration.isBoundedByHistory();
    }

    protected void asynchronousInvoke(FormModelResponse response) {
        final InvokeData method = response.getMethod();
        if (method != null) getCurrent().scheduleMethodInvocation(method);
    }

    @Override protected void onDeprecate(DeprecateEvent e) {
        final FormModelResponse response = createSyncResponse(e.getModel());
        service.deprecateForm(e.isDeprecate(), response, sync);
    }

    @Override protected void onMethodInvocation(MethodInvocationSyncEvent e) {
        final FormModelResponse response = createSyncResponse(e.getModel());
        service.methodInvocation(e.getMethod(), response, sync);
    }

    /** Called on sync form response. */
    protected abstract void onSync(FormModelResponse response);

    /** Called on termination form response. */
    protected abstract void onTermination(FormModelResponse response);

    /** Called on termination form response. */
    protected abstract void redirect(@NotNull final RedirectFormResponse redirect);

    /** Register new interaction block. */
    protected void startLifeCycle() {
        setCookie(FORM_LIFECYCLE_COOKIE, UUID.generateUUIDString());
    }

    /** Register a new FormBoxListener. */
    void addListener(@NotNull final FormBoxListener listener) {
        LOGGER.info("Add BOX listener : " + listener);
        listeners.add(listener);
    }

    void applyConfiguration() {
        if (current != null) {
            if (configuration.isHeaderHidden()) current.getView().hideHeader();
            if (configuration.isFooterHidden()) current.getView().hideFooter();
        }
    }

    /** Cancels any on_schedule that might be running. */
    void cancelSchedule() {
        if (current != null) current.cancelScheduleIfAny();
    }

    void clearStack() {
        callbacks.clear();  // Empty the stack if navigating out-of-flow.
        Cookies.removeCookie(AWS_ELB_COOKIE);
        clearTransientCache();
    }

    void configure(@NotNull final BoxConfiguration c) {
        configuration = c;
    }

    FormModelResponse createSyncResponse(final FormModel formModel) {
        return new FormModelResponse().sync(new SyncFormResponse(formModel));
    }

    void download(@NotNull DownloadResponse response) {
        final String location = response.getLocation();
        if (location != null) {
            if (response.isPrint()) getInstance().downloadPrint(location);
            else Window.Location.assign(location);
        }
    }

    /** Grab widget focus upon box display. */
    void focusOnDisplay(FormModel model, boolean focusFirst) {
        if (configuration.isFocusOnLoad() && getCurrent() != null) {
            final FormUI view = getCurrent().getView();

            final SourceWidget modelFocus = model.getFocus();
            final SourceWidget lastFocus  = model.getLastFocus();
            final SourceWidget focus      = modelFocus != null ? modelFocus : lastFocus;

            if (focus == null) {
                if (focusFirst) view.focusFirst();
            }
            else if (!equal(modelFocus, lastFocus)) view.focus(focus);
        }
    }

    void home() {
        unloadCurrent();

        if (configuration.isHomeFormEnabled()) {
            final FormBoxHome home = FormBoxHome.create();
            panel.add(home);
            home.setFocus(true);
        }
    }

    void invoke(@Nullable InvokeData invocation) {
        if (invocation != null) invoke(invocation.getTarget(), invocation.getFunction(), invocation.getArgs());
    }

    void load(@NotNull final String url) {
        clearStack();

        service.loadHtml(url, html -> setCurrent(url, html));
    }

    /** Notify listeners when the current form is updated. */
    void notifyOnUpdate() {
        for (final FormBoxListener listener : copy())
            listener.onUpdate();
    }

    void onDisplay(@NotNull final FormModel model) {
        focusOnDisplay(model, true);

        final String onDisplayMethod = model.metadata().getOnDisplayMethodName();
        if (isNotEmpty(onDisplayMethod)) {
            disableActionButtons();

            final FormModelResponse response = createSyncResponse(model);
            service.onDisplaySync(response, sync);
        }
    }

    /** Remove previous FormBoxListener registration. */
    void removeListener(@NotNull final FormBoxListener listener) {
        LOGGER.info("Remove BOX listener : " + listener);
        listeners.remove(listener);
    }

    void unloadCurrent() {
        if (current != null) {
            current.beforeModelUnload();
            current = null;
            clear();
            notifyOnUnload();
        }
    }

    void urlRedirection(@NotNull String url, boolean inlineRedirect) {
        if (inlineRedirect) load(url);
        else Window.Location.replace(url);
    }

    Element getBoxElement() {
        return panel.getElement();
    }

    void setCurrent(@NotNull final String url, @NotNull final String html) {
        if (current != null) current.beforeModelUnload();
        clear();

        current = null;
        panel.getElement().setInnerHTML(html);
        evalScripts(panel.getElement().getElementsByTagName(SCRIPT));

        notifyOnLoad(url);
    }

    void setCurrent(@NotNull final FormModel model, @Nullable final String pk) {
        setCurrent(model, pk, null);
    }

    void setCurrent(@NotNull final FormModel model, @Nullable final String pk, @Nullable final String parameters) {
        if (current != null) current.beforeModelUnload();
        clear();

        current = new FormController(init(model, model.getFormFullName(), getRetriever()), this, pk, parameters);
        current.init();
        panel.add(current.getView());

        model.startUserInteraction();

        applyConfiguration();

        current.afterModelLoad();

        notifyOnLoad(model.getFormFullName(), pk, parameters);

        onDisplay(model);
    }

    /** For testing purposes only. */
    ClientFormService getService() {
        return service;
    }

    /** Clear form box. */
    private void clear() {
        panel.clear();
    }

    private List<FormBoxListener> copy() {
        return new ArrayList<>(listeners);
    }

    private FormModelResponse createStackResponse(final FormModel formModel) {
        final FormModelResponse response = createSyncResponse(formModel);

        if (!callbacks.isEmpty()) {
            final CallbackStackItem callback = callbacks.peek();
            response.redirect(new RedirectFormResponse(callback.getModel()).withCallback(callback.getCallback()));
        }

        return response;
    }

    private void disableActionButtons() {
        setButtonDisability(EnumSet.of(SAVE, DELETE, CANCEL), true);
        setToogleButtonDisability(DEPRECATE, true);
        setSynchronousDisability(true);
    }

    private void enableActionButtons() {
        final EnumSet<ButtonType> types = EnumSet.of(CANCEL, CUSTOM, ADD_ROW, REMOVE_ROW, EXPORT, VALIDATE);

        if (current != null) {
            if (current.getModel().hasPermission(CREATE.getName()) || current.getModel().hasPermission(UPDATE.getName())) types.add(SAVE);
            if (current.getModel().hasPermission(PredefinedPermission.DELETE.getName())) types.add(DELETE);
            if (current.getModel().hasPermission(HANDLE_DEPRECATED.getName())) setToogleButtonDisability(DEPRECATE, false);
        }

        setButtonDisability(types, false);
        setSynchronousDisability(false);
        setHasLinkDisability(false);
    }

    private boolean handleAwait(@NotNull final FeedbackEventData event) {
        getInstance().await(event);
        final boolean terminated = event.isTerminated();
        if (!terminated) futures.schedule(event.getUuid(), future);
        else futures.reset();
        return terminated;
    }

    /** Notify listeners when the current form fires an error. */
    private void notifyOnError(ResponseError error) {
        for (final FormBoxListener listener : copy())
            listener.onError(error);
    }

    /** Notify listeners when the current html is set. */
    private void notifyOnLoad(String url) {
        for (final FormBoxListener listener : copy())
            listener.onLoad(url);
    }

    /** Notify listeners when the current form is set. */
    private void notifyOnLoad(String fqn, @Nullable String pk, @Nullable String parameters) {
        for (final FormBoxListener listener : copy())
            listener.onLoad(fqn, pk, parameters);
    }

    /** Notify listeners when the current form is unloaded. */
    private void notifyOnUnload() {
        for (final FormBoxListener listener : copy())
            listener.onUnload();
    }

    /** Called on load form response. */
    private void onLoad(FormModelResponse result) {
        final LoadFormResponse response = result.getLoad();
        final FormModel        model    = response.getModel();
        updateCache(model.metadata(), response.getUiReferences());
        initMetadata(model, getRetriever());
        setCurrent(model, response.getPk(), response.getParameters());
    }

    private void setButtonDisability(EnumSet<ButtonType> buttonType, boolean disabled) {
        if (current == null) return;
        final List<ButtonUI> uis = current.getView().finder().allByType(listOf(BUTTON));
        for (final ButtonUI b : uis)
            if (buttonType.contains(b.getModel().getButtonType())) b.disableElement(disabled);
    }

    private void setHasLinkDisability(boolean disabled) {
        if (current == null) return;

        for (final WidgetUI l : current.getView().finder().allByPredicate(w -> w instanceof HasLinkUI))
            l.disableElement(disabled);
    }

    private void setSynchronousDisability(boolean disable) {
        if (current == null) return;
        for (final WidgetUI w : current.getView().finder().allByPredicate(w1 -> w1 != null && w1.getModel().isSynchronous()))
            w.disableElement(disable);
    }

    private void setToogleButtonDisability(ToggleButtonType toggleButtonType, boolean disabled) {
        if (current == null) return;
        for (final ToggleButtonUI b : current.getView().finder().byToggleButtonType(toggleButtonType))
            b.disableElement(disabled);
    }

    //~ Methods ......................................................................................................................................

    private static void evalScripts(final NodeList<Element> scripts) {
        for (int i = 0; i < scripts.getLength(); i++) {
            final Element item = scripts.getItem(i);
            if (SUI_SERVICE_CACHE.equals(item.getAttribute(CLASS))) fromString(item.getInnerHTML()).setWindow(TOP_WINDOW).inject();
        }

        for (int i = 0; i < scripts.getLength(); i++) {
            final Element item = scripts.getItem(i);
            if (item.hasAttribute("src")) {
                final String src = item.getAttribute("src");
                fromUrl(src).setCallback(new Callback<Void, Exception>() {
                            public void onFailure(Exception reason) {
                                Window.alert("Script load failed = " + src);
                            }
                            public void onSuccess(Void result) {
                                for (int i = 0; i < scripts.getLength(); i++) {
                                    final Element item = scripts.getItem(i);
                                    if (!item.hasAttribute("src") && !SUI_SERVICE_CACHE.equals(item.getAttribute(CLASS)))
                                        fromString(item.getInnerHTML()).setWindow(TOP_WINDOW).inject();
                                }
                            }
                        }).setWindow(TOP_WINDOW).inject();
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String AWS_ELB_COOKIE = "AWSELB";

    private static final Logger LOGGER = Logger.getLogger(FormBox.class);

    //~ Inner Classes ................................................................................................................................

    private class FutureCallback extends ServiceCallback<FeedbackEventData> {
        @Override public void onSuccess(final FeedbackEventData event) {
            if (handleAwait(event)) {
                enableActionButtons();
                final FormModelResponse response = event.getResponse();
                if (response != null && !event.isInterrupted()) onSync(response);
            }
        }
    }

    private class LoadFormCallback extends ServiceCallback<FormModelResponse> {
        @Override public void onSuccess(final FormModelResponse response) {
            if (isNotEmpty(response.getUrl())) {
                urlRedirection(response.getUrl(), response.isInlineRedirect());
                return;
            }

            if (!response.isError()) onLoad(response);
        }
    }

    protected abstract class ServiceCallback<T> implements DefaultCallback<T> {
        @Override public void onError(ResponseError error) {
            service.clearProcessing();
            enableActionButtons();
            notifyOnError(error);
        }

        @Override public void onException() {
            service.clearProcessing();
            enableActionButtons();
        }

        @Override public void onLoginClosed() {
            service.clearProcessing();
            enableActionButtons();
        }
    }

    private class SyncFormCallback extends ServiceCallback<FormModelResponse> {
        @Override public void onSuccess(final FormModelResponse response) {
            final FeedbackEventData await = response.getAwait();
            if (await == null) {
                enableActionButtons();
                onSync(response);
            }
            else handleAwait(await);
        }
    }

    private class TerminationFormCallback extends ServiceCallback<FormModelResponse> {
        @Override public void onSuccess(FormModelResponse response) {
            enableActionButtons();
            onTermination(response);
        }
    }

    //J-
    /** Invoke function. */
    public static native void invoke(@NotNull String target, @NotNull String f, @NotNull String arg)  /*-{
        var findTarget = function(t) {
            var result = $wnd;
            //noinspection FunctionWithInconsistentReturnsJS
            t.split('.').forEach(function(path) {
                if (typeof(result[path]) != "undefined") {
                    result = result[path]
                } else {
                    return undefined;
                }
            });
            return result;
        };

        var _t = findTarget(target);

        if (_t != undefined) {
            // If target is not set or its defined
            if(typeof(_t[f]) == "function") {
                // If function is defined
                _t[f].apply(_t, JSON.parse(arg)); // Scope and args
            } else {
                console.error("JS invoke: function " + (target.length == 0 ? "" : target + ".") + f + " not defined.");
            }
        }
        else console.error("JS invoke: target " + target + " not defined.");
    }-*/;
    //J+

}  // end class FormBox
