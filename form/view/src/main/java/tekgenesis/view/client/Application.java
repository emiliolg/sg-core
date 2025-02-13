
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.view.client.menu.MenuBox;
import tekgenesis.view.client.service.BaseAsyncCallback;
import tekgenesis.view.client.ui.FormUI;
import tekgenesis.view.client.ui.WidgetMessages;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.ModalOkCancelAlert.OkCancelCallback;
import tekgenesis.view.client.ui.base.Popover;
import tekgenesis.view.client.ui.modal.Modal;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalSubscription;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.BootResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.response.ResponseError;
import tekgenesis.view.shared.service.FormService;

import static com.google.gwt.dom.client.Style.Visibility.HIDDEN;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.InstanceReference.createInstanceReference;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.menu.MenuBox.getMenuBoxes;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.getActiveElement;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelector;
import static tekgenesis.view.client.ui.base.ModalOkCancelAlert.show;

/**
 * Client Application.
 */
public class Application implements FormBoxListener {

    //~ Instance Fields ..............................................................................................................................

    private boolean authenticated = true;

    private final List<ApplicationBootListener> bootListeners;

    private final Map<Element, FormBox> boxes;
    private Popover                     currentPopover;
    private WidgetMessages              currentTooltip;
    private Frame                       downloadPrint        = null;
    private final FeedbackModal         feedbackModal;
    private final FuturesModal          futuresModal;
    private final Stack<HistoryItem>    history;
    private KeepAlive                   keepAlive            = null;
    private boolean                     mailValidatorEnabled;
    private String                      mapServerUrl;

    private final List<Element>       menuElements;
    private final ApplicationMessages messages;
    private final Modal               modal;
    private String                    resourceServerUrl;

    private final Map<String, FormUI> subformViews;
    private String                    userid                 = "";

    //~ Constructors .................................................................................................................................

    private Application() {
        boxes             = new HashMap<>();
        history           = new Stack<>();
        mapServerUrl      = "";
        resourceServerUrl = "";
        messages          = new ApplicationMessages();
        feedbackModal     = new FeedbackModal();
        futuresModal      = new FuturesModal();
        bootListeners     = new ArrayList<>();
        menuElements      = new ArrayList<>();
        modal             = new Modal();
        currentPopover    = null;
        currentTooltip    = null;
        subformViews      = new HashMap<>();

        // Server application booting
        boot();

        // Root input handler registration
        RootInputHandler.getInstance().registerHandlers();
    }

    //~ Methods ......................................................................................................................................

    /** Add Boot listener. */
    public void addBootListener(@NotNull ApplicationBootListener listener) {
        bootListeners.add(listener);
    }

    /** Attach FormBox. */
    public void attach(@NotNull final FormBox box) {
        if (box instanceof HistoryFormBox) {
            for (final MenuBox menuBox : getMenuBoxes()) {
                if (menuBox != null) box.addListener(menuBox);
            }
        }
        box.addListener(this);
        boxes.put(box.getBoxElement(), box);
    }

    /** Boots (or reboots) application. */
    public void boot() {
        FormService.App.getInstance().boot(new BaseAsyncCallback<Response<BootResponse>>() {
                @Override public void onSuccess(Response<BootResponse> result) {
                    final BootResponse response = result.getResponse();

                    if (response.hasError()) {
                        first(boxes.values())  //
                        .ifPresent(box -> messages().error(FormViewMessages.MSGS.errorReachingServer(response.getErrorMessage()), box));
                        return;
                    }
                    keepAlive = new KeepAlive(response.getTimeout());
                    keepAlive.updateLastKnownServerConnection();

                    final Option<Form> feedback = response.getFeedback();
                    resourceServerUrl    = response.getResourceServerUrl();
                    mapServerUrl         = response.getUnigisServerUrl();
                    userid               = response.getUserId();
                    mailValidatorEnabled = response.isMailAddressEnabled();

                    feedback.ifPresent(f -> {
                        feedbackModal.initialize(f);
                        messages.allowReporting();
                    });
                    authenticated = true;
                    for (final ApplicationBootListener listener : bootListeners)
                        listener.onBoot(response);

                    if (response.isProductionMode()) toProductionMode();
                }
            });
    }  // end method boot

    /** Method used by menus to load the given fqn on the history form boxes. */
    public void clearStack() {
        for (final FormBox box : boxes.values()) {
            if (box.isBoundedByHistory()) box.clearStack();
        }
    }

    /** Close left menu. */
    public void closeLeftMenu() {
        final Element element = querySelector(RootPanel.get().getElement(), ".push-overlay-nav");
        if (element != null) element.removeClassName("in");
    }

    /** Detach FormBox. */
    public void detach(@NotNull final FormBox box) {
        box.removeListener(this);
        boxes.remove(box.getBoxElement());
    }

    /** Open feedback report panel after error. */
    public void feedback(@NotNull final ResponseError error) {
        showFeedback(error);
    }

    /** Shows heads up modal (confirmation). */
    public void headsUp(OkCancelCallback callback) {
        show(MSGS.headsUp(), MSGS.areYouSure(), CheckType.INFO, callback);
    }

    /** Hides non FormBox modal. */
    public void hideActiveModal(boolean onlyTransient) {
        hideActiveModal(onlyTransient, false);
    }

    /** Hides non FormBox modal indicating if a click outside the modal triggered the hide. */
    public void hideActiveModal(boolean onlyTransient, boolean byClick) {
        if (!onlyTransient || modal.isTransient()) modal.hide(byClick);
    }

    @Override public void onError(ResponseError error) {}

    @Override public void onLoad(String url) {
        history.push(new HistoryItem(url));
    }

    @Override public void onLoad(String fqn, String key, String parameters) {
        history.push(new HistoryItem(fqn, key));
    }

    @Override public void onUnload() {}

    @Override public void onUpdate() {}

    /** Register menu Element. */
    public void registerMenuElement(@NotNull final Element menu) {
        menuElements.add(menu);
    }

    /** Registers subform view. */
    public void registerSubformView(@NotNull final String subformPath, @NotNull final FormUI subformView) {
        subformViews.put(subformPath, subformView);
    }

    public void restartLifeCycles() {
        for (final FormBox formBox : boxes.values()) {
            if (formBox.isBoundedByHistory()) formBox.startLifeCycle();
        }
    }

    /** Updates last user interaction on the client. */
    public void updateLastInteraction() {
        if (keepAlive != null) keepAlive.updateLastInteraction();
    }

    /** Updates last known server connection. */
    public void updateLastKnownServerConnection() {
        if (keepAlive != null) keepAlive.updateLastKnownServerConnection();
    }

    /** Attempt to resolve an active Box, or fallback to the main or first one. */
    @NotNull public FormBox getActiveOrMain() {
        final Option<FormBox> active = findParentBox(getActiveElement());
        final Seq<FormBox>    values = seq(boxes.values());
        return active.orElse(
            values.filter(HISTORY_BOX).getFirst().orElse(values.getFirst().getOrFail("A main or at least one box should be defined!")));
    }

    /** Changes the authentication status of the application. */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /** Clear currentPopover. */
    public Popover getCurrentPopover() {
        return currentPopover;
    }

    /** Set currentPopover. */
    public void setCurrentPopover(@NotNull Popover popover) {
        currentPopover = popover;
    }

    /** Set currentTooltip. */
    public void setCurrentTooltip(@NotNull WidgetMessages tooltip) {
        currentTooltip = tooltip;
    }

    /** Is application authenticated? */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /** Is mail validator enabled. */
    public boolean isMailValidatorEnabled() {
        return mailValidatorEnabled;
    }

    /** Returns 'true' if the application is showing a modal. */
    public boolean isModalShowing() {
        return modal.isShowing();
    }

    /** Return application history. */
    public Seq<HistoryItem> getHistory() {
        return seq(history);
    }

    /** Return map server. */
    public String getMapServerUrl() {
        return mapServerUrl;
    }

    /** Get menu Elements. */
    public List<Element> getMenuElements() {
        return menuElements;
    }

    public Modal getModal() {
        return modal;
    }

    /** Returns the full path of the cdn image server url. */
    public String getResourceServerUrl() {
        return resourceServerUrl;
    }

    /** Returns subform view by path. */
    public FormUI getSubformView(@NotNull final String subformPath) {
        return subformViews.get(subformPath);
    }

    /** The last logged user id. */
    public String getUserid() {
        return userid;
    }

    /** Await dialog with feedback data. */
    void await(@NotNull FeedbackEventData data) {
        if (data.isStarted()) {
            final ModalSubscription subscription = showModal(futuresModal);
            futuresModal.setActive(subscription);
        }
        if (futuresModal.hasActiveSubscription()) futuresModal.update(data);
    }

    void downloadPrint(@NotNull final String location) {
        if (downloadPrint == null) {
            downloadPrint = HtmlWidgetFactory.frame(location);
            downloadPrint.addLoadHandler(event -> invokeIframePrint(IFrameElement.as(downloadPrint.getElement())));
            downloadPrint.getElement().getStyle().setVisibility(HIDDEN);
            RootPanel.get().add(downloadPrint);
        }
        else downloadPrint.setUrl(location);
    }

    Option<FormBox> findParentBox(final Element element) {
        return element == null ? Option.empty() : HtmlDomUtils.findParentBox(boxes, element);
    }

    private void showFeedback(@Nullable final ResponseError error) {
        final ModalSubscription subscription = showModal(feedbackModal.show(error));
        feedbackModal.setModalSubscription(subscription);
    }

    private ModalSubscription showModal(ModalContent content) {
        if (modal.isShowing()) {
            // messages.error("Modal is already showing", getActiveOrMain());
            Logger.getLogger(Application.class).debug("Modal is already showing");
            return null;
        }
        if (currentTooltip != null) currentTooltip.hide();
        return modal.show(content);
    }

    private void toProductionMode() {
        final Element body = RootPanel.getBodyElement();
        body.addClassName(PRODUCTION_CLASS);
        final Element html = Document.get().getElementsByTagName("html").getItem(0);
        html.addClassName(PRODUCTION_CLASS);
    }

    private Messages getMessages() {
        return messages;
    }

    //~ Methods ......................................................................................................................................

    /** Open feedback report panel. */
    public static void feedback() {
        getInstance().showFeedback(null);
    }

    /** Return application Messages service. */
    @NotNull public static Messages messages() {
        return getInstance().getMessages();
    }

    /** Displays a modal with the given Content.. */
    public static ModalSubscription modal(ModalContent content) {
        return getInstance().showModal(content);
    }

    /** Get application instance. */
    @NotNull public static Application getInstance() {
        return INSTANCE;
    }

    private static native void invokeIframePrint(IFrameElement frame)  /*-{ frame.contentWindow.print(); }-*/;

    //~ Static Fields ................................................................................................................................

    private static final String PRODUCTION_CLASS = "production";

    private static final Predicate<FormBox> HISTORY_BOX = formBox -> formBox instanceof HistoryFormBox;

    private static final Application INSTANCE = new Application();

    //~ Inner Classes ................................................................................................................................

    static class HistoryItem {
        final InstanceReference instance;

        final DateTime time;
        final String   url;

        private HistoryItem(@NotNull final String url) {
            this.url = url;
            instance = null;
            time     = DateTime.current();
        }

        private HistoryItem(@NotNull final String fqn, @Nullable final String key) {
            instance = createInstanceReference(createQName(fqn), notEmpty(key, ""));
            time     = DateTime.current();
            url      = null;
        }

        @Override public String toString() {
            return (isNotEmpty(url) ? url : instance) + " @{" + time + "}";
        }
    }
}  // end class Application
