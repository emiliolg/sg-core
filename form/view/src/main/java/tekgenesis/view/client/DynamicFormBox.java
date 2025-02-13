
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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Stack;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormAction;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.event.LazyFetchSyncEvent;
import tekgenesis.view.client.event.LoadFormEvent;
import tekgenesis.view.client.event.OnAbstractSyncEvent;
import tekgenesis.view.client.event.OnBlurSyncEvent;
import tekgenesis.view.client.event.OnClickSyncEvent;
import tekgenesis.view.client.event.OnNewLocationSyncEvent;
import tekgenesis.view.client.event.SubformEvent;
import tekgenesis.view.client.event.WidgetEvent;
import tekgenesis.view.client.ui.*;
import tekgenesis.view.client.ui.base.DetailPopup;
import tekgenesis.view.client.ui.base.ModalOkCancelAlert.OkCancelCallback;
import tekgenesis.view.client.ui.base.SwipePopup;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;
import tekgenesis.view.shared.response.DetailResponse;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.LoadFormResponse;
import tekgenesis.view.shared.response.RedirectFormResponse;
import tekgenesis.view.shared.response.SwipeFetchResponse;
import tekgenesis.view.shared.response.SwipeResponse;
import tekgenesis.view.shared.response.SyncFormResponse;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.model.ChangeListener.Registration;
import static tekgenesis.metadata.form.widget.CustomMsgType.SUCCESS;
import static tekgenesis.view.client.BoxConfiguration.DEFAULT;
import static tekgenesis.view.client.ClientUiModelContext.getRetriever;
import static tekgenesis.view.client.exprs.ExpressionEvaluatorFactory.ValidationResult;
import static tekgenesis.view.shared.FormHandling.init;
import static tekgenesis.view.shared.FormHandling.initMetadata;
import static tekgenesis.view.shared.FormHandling.updateCache;

/**
 * Form box that is dynamic (a.k.a. Navigable Box). Allows redirection and manages stack.
 */
public class DynamicFormBox extends FormBox {

    //~ Instance Fields ..............................................................................................................................

    private final List<BodyOnlyFormBox> children;
    private Option<DetailPopup>         detail;
    private Option<SwipePopup>          swipe;

    //~ Constructors .................................................................................................................................

    /** Creates Dynamic Form Box. */
    public DynamicFormBox() {
        this(Stack.createStack());
    }

    /** Creates Dynamic Form Box. */
    public DynamicFormBox(Stack<CallbackStackItem> callbacks) {
        super(callbacks);
        children = new ArrayList<>();
        swipe    = Option.empty();
        detail   = Option.empty();
    }

    //~ Methods ......................................................................................................................................

    @Override public void onLoadForm(@NotNull LoadFormEvent e) {
        load(e.getFqn(), e.getPk());
    }

    @Override public void showSubform(@NotNull SubformEvent e) {
        final AnchoredSubformUI subform      = e.getSubform();
        final Model             model        = getContextualModel(subform);
        final Widget            subformModel = subform.getModel();
        final FormModel         result       = model.getSubform(subformModel);
        if (result != null) {
            final String anchor = subformModel.getOnAnchor();
            if (isEmpty(anchor)) model.backup(subformModel);
            new LoadSubformCallback(subform, model, subformModel).showSubform(result);
        }
        else service.loadForm(e.getSubformFqn(), null, null, new LoadSubformCallback(subform, model, subformModel));
    }

    /** Tells if Detail popup is showing or not. */
    public boolean isDetailShowing() {
        return isPopupShowing(detail);
    }

    /** Tells if Swipe popup is showing or not. */
    public boolean isSwipeShowing() {
        return isPopupShowing(swipe);
    }

    @Override protected void hideSubform(@NotNull SubformEvent e) {
        getAnchor(e.getSubform().getModel().getOnAnchor())  //
        .ifPresent(AnchorUI::detach);

        findChildBox(e.getSubform())  //
        .ifPresent(child -> {
            child.unRegister();
            final Option<AnchoredSubformUI> subform = child.getSubform().map(s -> (AnchoredSubformUI) s);
            subform.ifPresent(FieldWidgetUI::clearMessages);
            final ValidationResult errors = child.getCurrent().validateOnly(Option.empty());

            first(errors)                             //
            .flatMap(err -> err.getMsg().getFirst())  //
            .ifPresent(err -> subform.ifPresent(s -> s.addMessage(err)));

            children.remove(child);
        });
    }

    protected void onSync(final FormModelResponse response) {
        invoke(response.getInvoke());
        if (response.hasDownload()) download(response.getDownload());

        if (isNotEmpty(response.getUrl())) {
            urlRedirection(response.getUrl(), response.isInlineRedirect());
            return;
        }

        final FormController current = getCurrent();
        final Form           form    = current.getView().getUiModel();

        final SyncFormResponse sync = response.getSync();

        /* Special case of 'empty' redirect sync on download. Should never happen. */
        if (sync == null) throw unreachable();

        if (sync.getSource() != null) service.removeProcessing(sync.getSource());

        sync.ensureMetamodelsInitialization(form, getRetriever());

        final RedirectFormResponse redirect = response.getRedirect();

        if (redirect == null || redirect.hasTargetFormBox()) {
            // apply sync on current form box
            boxModelUpdated(response);
            LazyFetchPopulator.getRequestStatus().setRequest(true);

            final SwipeResponse s = response.getSwipe();
            if (s != null) handleSwipe(s);

            final DetailResponse d = response.getDetail();
            if (d != null) handleDetail(d);

            notifyOnUpdate();
        }

        if (redirect != null) {
            // Hide all messages.
            Application.messages().hide(this);

            if (redirect.hasTargetFormBox())
            // redirect specified form box
            BoxHandler.redirect(redirect.getTargetFormBox(), redirect);
            else {
                if (needsConfirmation(response, redirect, this::onSync)) return;

                // Push future callback
                if (redirect.hasCallback()) {
                    current.beforeModelUnload();

                    sync.syncModel(form, current.getModel()).ifPresent(model ->
                            callbacks.push(new CallbackStackItem(form, model, redirect.getCallback(), current.getPk(), current.getParameters())));
                }

                if (redirect.isLeave()) callbacks.clear();

                if (redirect.isDialog()) dialogRedirect(current, redirect);
                else {
                    Application.getInstance().hideActiveModal(true);
                    redirect(redirect);
                    asynchronousInvoke(response);
                }
            }
        }
    }  // end method onSync

    protected void onTermination(FormModelResponse response) {
        invoke(response.getInvoke());
        if (response.hasDownload()) download(response.getDownload());

        if (response.isError()) {
            boxModelUpdated(response);
            notifyOnUpdate();
            return;
        }

        Application.getInstance().hideActiveModal(true);

        if (isNotEmpty(response.getUrl())) {
            urlRedirection(response.getUrl(), response.isInlineRedirect());
            return;
        }

        final RedirectFormResponse redirect = response.getRedirect();

        final SyncFormResponse sync = response.getSync();

        if (sync != null) {
            final CallbackStackItem peek = callbacks.peek();
            sync.ensureMetamodelsInitialization(peek.getForm(), getRetriever());
            peek.sync(sync);
        }

        if (redirect != null) {
            // If we are on termination and redirection is provided by Sui Generis, we should start a new lifecycle.
            if (redirect.isProvided()) startLifeCycle();

            redirect(redirect);
            asynchronousInvoke(response);
        }
        else {
            if (sync != null) {
                final CallbackStackItem last      = callbacks.pop();
                final FormModel         lastModel = last.getModel();
                setCurrent(lastModel, last.getPk(), last.getParameters());
                getCurrent().afterModelSync();
            }
            else home();
        }
    }

    protected void redirect(@NotNull final RedirectFormResponse redirect) {
        final FormModel model = redirect.getModel();
        updateCache(model.metadata(), redirect.getUiReferences());
        initMetadata(model, getRetriever());

        setCurrent(model, redirect.getPk(), redirect.getParameters());
        final String message = redirect.getMessage();
        if (!isEmpty(message)) Application.messages().custom(message, this, redirect.getMsgTypeOrElse(SUCCESS), redirect.isAutoClose());

        final List<FormAction> actions = redirect.getFormActions();
        if (isNotEmpty(actions)) getCurrent().getView().swapFooter(actions, new CustomActionHandler(getCurrent()));
    }

    /** Finds a subform Controller (exposed for testing). */
    FormController findSubformController(final SubformUI subform) {
        return findChildBox(subform).getOrFail("Subform not found.").getCurrent();
    }

    private void boxModelUpdated(FormModelResponse response) {
        final List<Registration> registrations = new ArrayList<>();

        for (final BodyOnlyFormBox child : children)
            registrations.add(child.getCurrent().registerSyncListener());

        getCurrent().modelUpdated(response.getSync());

        for (final Registration registration : registrations)
            registration.removeChangeListener();

        asynchronousInvoke(response);

        focusOnDisplay(getCurrent().getModel(), false);
    }

    private void dialogRedirect(final FormController current, RedirectFormResponse redirect) {
        final DynamicFormBox child = new DynamicFormBox(callbacks) {
                @Override protected void onTermination(FormModelResponse resp) {
                    final SyncFormResponse     s = resp.getSync();
                    final RedirectFormResponse r = resp.getRedirect();
                    if ((r == null || r.isProvided()) && (s == null || s.accepts(current.getModel()))) {
                        if (s == null) resp.redirect(null);
                        DynamicFormBox.this.onTermination(resp);
                        Application.getInstance().hideActiveModal(false);
                    }
                    else super.onTermination(resp);
                }
            };

        child.configure(new BoxConfiguration.Default());
        child.redirect(redirect);

        final ModalContent content = new ModalContent().setBodyOnly(true).setFormBox(child);
        content.setListener(new ModalListener() {
                @Override public void onHide(ModalButtonType buttonClicked) {
                    Application.getInstance().detach(child);
                }
                @Override public void onShow() {
                    Application.getInstance().attach(child);
                }
            });

        Application.modal(content);
    }

    private Option<BodyOnlyFormBox> findChildBox(final SubformUI subform) {
        // It could be better way of doing this?
        for (final BodyOnlyFormBox child : children)
            return child.getSubform().filter(s -> s.getId().equals(subform.getId())).map(s -> child);
        return Option.empty();
    }

    private void handleDetail(final DetailResponse response) {
        final Form form = response.getForm();
        updateCache(form, response.getUiReferences());

        final FormModel model = response.getModel();
        initMetadata(model, getRetriever());

        final DetailPopup  detailPopup = new DetailPopup(response);
        final SwipeFormBox box         = new SwipeFormBox(model, DEFAULT.notFocus(), this, detailPopup);

        detailPopup.show(box);
        detail = some(detailPopup);
        detailPopup.addCloseHandler(h -> detail = Option.empty());
    }

    private void handleSwipe(final SwipeResponse response) {
        final Form form = response.getForm();
        updateCache(form, response.getUiReferences());

        final SwipePopup swipePopup = new SwipePopup(response);
        swipePopup.setFetchSize(response.getFetchSize());
        swipePopup.setFetchFunction(value -> {
            final FormModelResponse syncResponse = createSyncResponse(getCurrent().getModel());
            service.fetchSwipeModels(syncResponse, value, response.getLoaderClassName(), new ServiceCallback<SwipeFetchResponse>() {
                    @Override public void onSuccess(final SwipeFetchResponse fetchResponse) {
                        handleSwipe(fetchResponse, form, swipePopup);
                        swipePopup.fetchFinish();
                    }
                });
        });

        handleSwipe(response, form, swipePopup);
        swipePopup.show(response.getStartIndex());
        swipe = some(swipePopup);
        swipePopup.addCloseHandler(h -> swipe = Option.empty());
    }

    private void handleSwipe(final SwipeFetchResponse s, final Form form, final SwipePopup swipePopup) {
        for (final Map.Entry<Integer, FormModel> entry : s.getModels(form).entrySet()) {
            final FormModel model = entry.getValue();
            initMetadata(model, getRetriever());

            final SwipeFormBox box = new SwipeFormBox(model, DEFAULT.notFocus(), this, swipePopup);
            swipePopup.put(entry.getKey(), box);
        }
    }

    private Option<AnchorUI> getAnchor(@Nullable String a) {
        return option(isNotEmpty(a) ? getCurrent().getView().finder().<AnchorUI>byName(a) : null);
    }

    private Model getContextualModel(SubformUI subform) {
        final FormModel        form           = getCurrent().getModel();
        final WidgetUI.Context subformContext = subform.getContext();

        return subformContext.getMultiple()  //
               .map(MultipleUI::getMultipleModel)
               .map(form::getMultiple)
               .flatMap(m -> subformContext.getItem().map(row -> (Model) m.getRow(row)))
               .orElse(form);
    }

    //~ Methods ......................................................................................................................................

    public static boolean needsConfirmation(final FormModelResponse response, RedirectFormResponse redirect, Consumer<FormModelResponse> sync) {
        if (redirect.needsConfirmation()) {
            response.getRedirect().withCallback(null);
            if (BoxHandler.isSomeoneDirty()) {
                Application.getInstance().headsUp(new OkCancelCallback() {
                        @Override public void cancel() {}
                        @Override public void ok() {
                            response.getRedirect().setConfirmation(false);
                            sync.accept(response);
                        }
                    });
                return true;
            }
        }
        return false;
    }

    private static boolean isPopupShowing(Option<? extends PopupPanel> swipe) {
        return swipe.map(PopupPanel::isShowing).orElse(false);
    }

    //~ Inner Classes ................................................................................................................................

    public static class BodyOnlyFormBox extends DynamicFormBox implements ChangeListener {
        final FormController ancestor;

        private final Model  container;
        private final Widget metadata;

        public BodyOnlyFormBox(Model container, Widget metadata, @NotNull FormController ancestor) {
            this.container = container;
            this.metadata  = metadata;
            this.ancestor  = ancestor;

            final FormModel subformModel = ensureNotNull(container.getSubform(metadata), "Subform model should never be null inside here");

            configure(new BoxConfiguration.Default().bodyOnly());
            setCurrent(subformModel, "");

            registerDetachHandler();
        }

        @Override public void onModelChange(@NotNull IndexedWidget widget) {
            getSubform().ifPresent(ancestor::subformChanged);
        }

        @Override public void onMultipleModelChange(@NotNull MultipleWidget multipleWidget, @NotNull MultipleChanges changes) {
            getSubform().ifPresent(ancestor::subformChanged);
        }

        @Override public void onSyncTriggered(@NotNull OnBlurSyncEvent e) {
            if (isNotAbstractInvocation(e)) super.onSyncTriggered(e);
        }

        @Override public void onSyncTriggered(@NotNull OnNewLocationSyncEvent e) {
            if (isNotAbstractInvocation(e)) super.onSyncTriggered(e);
        }

        @Override public void onSyncTriggered(@NotNull LazyFetchSyncEvent e) {
            if (isNotAbstractInvocation(e)) super.onSyncTriggered(e);
        }

        @Override public void onSyncTriggered(@NotNull OnClickSyncEvent e) {
            if (isNotAbstractInvocation(e)) super.onSyncTriggered(e);
        }

        @Override public void onWidgetDefinitionChange(@NotNull IndexedWidget widget) {}

        /** Register listener. */
        public void register() {
            getCurrent().getModel().addValueChangeListener(this);
        }

        /** Un Register listener. */
        public void unRegister() {
            getCurrent().getModel().removeValueChangeListener(this);
        }

        /** Get form controller from box. */
        public FormController getController() {
            return super.getCurrent();
        }

        /** Get subform UI. */
        public Option<SubformUI> getSubform() {
            final Option<Integer>  item = Option.of(container).filter(m -> m instanceof RowModel).map(m -> ((RowModel) m).getRowIndex());
            final Option<WidgetUI> ui   = ancestor.getView().finder().find(metadata, item);
            return ui.map(w -> (SubformUI) w);
        }

        private void registerDetachHandler() {
            // Detach is attach with isAttached returning false... ugly GWT!
            panel.addAttachHandler(h -> {
                if (!h.isAttached()) cancelSchedule();
            });
        }

        /** Dispatch new event into ancestor if abstract invocation is performed. */
        private boolean isNotAbstractInvocation(@NotNull WidgetEvent<?> e) {
            final Option<SubformUI> subform = getSubform();
            if (e.isAbstractInvocation() && subform.isPresent()) {
                ancestor.getBus().fireEvent(new OnAbstractSyncEvent(ancestor.getModel(), subform.get().toSourceWidget(), e.getSourceWidget()));
                return false;
            }
            return true;
        }
    }  // end class BodyOnlyFormBox

    public static class CustomActionHandler {
        private final FormController controller;

        private CustomActionHandler(FormController controller) {
            this.controller = controller;
        }

        /** Returns a click handler for a given action. To prevent client modifying DOM data. */
        public ClickHandler handlerFor(@NotNull final FormAction action) {
            return event -> controller.submit(action.getAction(), NONE);
        }
    }

    private class LoadSubformCallback extends ServiceCallback<FormModelResponse> {
        private final Model             container;
        private final Widget            metadata;
        private final AnchoredSubformUI subform;

        private LoadSubformCallback(AnchoredSubformUI subform, Model container, Widget metadata) {
            this.subform   = subform;
            this.container = container;
            this.metadata  = metadata;
        }

        @Override public void onSuccess(final FormModelResponse result) {
            showSubform(getSubform(result.getLoad()));
        }

        private void anchor(@NotNull SubformBox box, @NotNull String anchor) {
            for (final AnchorUI a : getAnchor(anchor))
                a.attach(box);
        }

        private void showSubform(@NotNull final FormModel ancestor) {
            initMetadata(ancestor, getRetriever());

            final SubformBox box = new SubformBox(subform, container, metadata, getCurrent());

            children.add(box);

            final String anchor = subform.getModel().getOnAnchor();

            if (isNotEmpty(anchor)) anchor(box, anchor);
            else {
                final ModalContent content = new ModalContent();
                content.setTitle(box.getCurrent().getFormLabel());
                content.setFormBox(box);
                content.setFooterType(subform.getModel().getFooterType());
                content.setListener(new ModalListener() {
                        @Override public void onHide(ModalButtonType buttonClicked) {
                            if (buttonClicked == null) {
                                subform.toggle();
                                return;
                            }

                            box.onHide();
                            box.detach();
                            final FormController current = getCurrent();
                            if (current != null) {
                                switch (buttonClicked) {
                                case VALIDATE_OK:
                                case OK:
                                case CLOSE:
                                    onDisplay(current.getModel());
                                    break;
                                case CANCEL:
                                    getContextualModel(subform).restore(subform.getModel());
                                    current.subformChanged(subform);
                                    break;
                                }
                            }
                        }

                        @Override public void onShow() {
                            box.onShow();
                        }
                    });

                Application.modal(content);
            }

            box.getCurrent().validateWithoutInit(Option.empty());

            notifyOnUpdate();
        }  // end method showSubform

        private FormModel getSubform(@NotNull final LoadFormResponse response) {
            final Model model = getContextualModel(subform);

            FormModel result = model.getSubform(subform.getModel());

            if (result == null) {
                result = response.getModel();
                model.setSubform(subform.getModel(), result);
                init(result, response.getFqn(), getRetriever());
            }

            return result;
        }
    }  // end class LoadSubformCallback
}  // end class DynamicFormBox
