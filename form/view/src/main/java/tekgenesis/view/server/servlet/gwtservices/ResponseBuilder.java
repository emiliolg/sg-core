
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.util.*;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.core.Builder;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.exception.ApplicationException;
import tekgenesis.common.invoker.exception.InvokerApplicationException;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.*;
import tekgenesis.inbox.Inbox;
import tekgenesis.inbox.WorkItems;
import tekgenesis.metadata.form.InvokeData;
import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.view.server.servlet.DownloadServlet;
import tekgenesis.view.shared.response.*;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.service.Parameters.mapToQueryString;
import static tekgenesis.form.FormsImpl.createUserFormInstance;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;
import static tekgenesis.form.exprs.ServerExpressions.bindListener;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedForm;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedWidgetDef;
import static tekgenesis.metadata.form.model.FormConstants.DEFAULT_HEIGHT;
import static tekgenesis.metadata.form.model.FormConstants.DEFAULT_WIDTH;
import static tekgenesis.metadata.form.widget.WidgetType.SUBFORM;
import static tekgenesis.metadata.form.widget.WidgetType.WIDGET;
import static tekgenesis.view.server.servlet.gwtservices.FormMethod.Scope.CREATE_OR_UPDATE;
import static tekgenesis.view.shared.FormHandling.initMetadata;
import static tekgenesis.view.shared.response.RedirectFormResponse.IMPLICIT_CALLBACK;

class ResponseBuilder implements Builder<FormModelResponse> {

    //~ Instance Fields ..............................................................................................................................

    private Handling  handling;
    private FormModel main;

    @NotNull private final FormMethod      method;
    @Nullable private String               parameters;
    @Nullable private String               pk;
    @Nullable private RedirectFormResponse redirect;
    private boolean                        redirectToInbox;
    @Nullable private SourceWidget         source       = null;
    private String                         submitAction;

    //~ Constructors .................................................................................................................................

    ResponseBuilder(@NotNull FormMethod method) {
        handling        = null;
        main            = null;
        redirect        = null;
        pk              = null;
        parameters      = null;
        submitAction    = null;
        redirectToInbox = false;
        this.method     = method;
    }

    //~ Methods ......................................................................................................................................

    @Override public FormModelResponse build() {
        // Execute form class method.
        ActionImpl action;
        try {
            action = cast(method.apply(main));
        }
        catch (final ApplicationException e) {
            action = (ActionImpl) ActionsImpl.getInstance().getError(e);
        }
        catch (final InvokerApplicationException e) {
            action = (ActionImpl) ActionsImpl.getInstance().getError(e);
        }

        final FormModelResponse response;

        if (action.isError()) response = errorResponse(action);
        // If execution returned an error, return and sync.
        else {
            switch (handling) {
            case LOAD:  // If its a loading invocation, load.
                response = toResponse(some(main));
                setPrimaryKeyAndParameters(response.getLoad());
                break;
            case SYNC:  // If its a syncing invocation (Change, Click, etc.), sync.
                response = toResponse(some(main));
                setSourceWidget(response);
                break;
            default:    // If its a termination one (Save, Cancel, Delete), check for any piggybacked redirection.
                response = toResponse(handleRedirect());
                closeWorkItem(action);
                break;
            }

            // Resolve response redirection.
            navigation(action, response);
        }

        // Append client side function execution if defined
        appendClientInvocation(response, action);

        // Append server side asynchronous invocation if defined
        appendAsynchronousInvocation(response, action);

        // Attach subforms to response.
        subforms(response);

        // Append stream download location if defined
        appendDownload(response, action);

        return response;
    }  // end method build

    /** Load request. */
    ResponseBuilder load(@NotNull FormModel load) {
        main     = load;
        handling = Handling.LOAD;
        return this;
    }

    /**
     * Redirected request. Callback parameter indicates whether this response should execute
     * callbacks (Create, Update) or not (Cancel, Delete)
     */
    ResponseBuilder redirect(@Nullable RedirectFormResponse request) {
        redirect = request;
        handling = Handling.TERMINATION;
        return this;
    }

    /** Sync request. */
    ResponseBuilder sync(@NotNull SyncFormResponse request, Form form, @Nullable SourceWidget s) {
        main     = request.getModel(form);
        handling = Handling.SYNC;
        source   = s;
        initMetadata(main, createLocalizedRetriever());
        // initSubforms(form, main); todo pcolunga review
        return this;
    }

    ResponseBuilder withParameters(@Nullable String ps) {
        parameters = ps;
        return this;
    }

    ResponseBuilder withPk(@Nullable String key) {
        pk = key;
        return this;
    }

    /** Append work item reference and contextual action. */
    ResponseBuilder withSubmitAction(@NotNull final String action) {
        submitAction = action;
        return this;
    }

    private void appendAsynchronousInvocation(FormModelResponse response, Action action) {
        final ActionImpl implementation = cast(action);
        for (final Tuple<String, Integer> invocation : implementation.getAsynchronousInvocation())
            response.method(invocation);
    }

    private void appendClientInvocation(FormModelResponse response, Action action) {
        final ActionImpl implementation = cast(action);
        for (final InvokeData invoke : implementation.getInvokeData())
            response.invoke(invoke);
    }

    private void appendDownload(FormModelResponse response, Action action) {
        final ActionImpl implementation = cast(action);
        for (final DownloadImpl download : implementation.getDownload()) {
            final String location = DownloadServlet.persist(main, download);
            response.download(new DownloadResponse(location, download.isPrint()));
        }
    }

    private void closeWorkItem(@NotNull final ActionImpl action) {
        if (action.shouldCloseWorkItem() && main.hasWorkItem()) {
            if (submitAction != null) WorkItems.close(main.getWorkItem(), submitAction);
            redirectToInbox = true;
        }
    }

    private UiModelRetriever createLocalizedRetriever() {
        final UiModelRetriever retriever = getRetriever();

        return new UiModelRetriever() {
            @NotNull @Override public Option<UiModel> getUiModel(@NotNull QName key) {
                return retriever.getUiModel(key);
            }
            @NotNull @Override public Form getForm(@NotNull QName key) {
                return getLocalizedForm(key.getFullName());
            }
        };
    }

    private DetailResponse detailAction(ActionImpl action) {
        final DetailImpl<?>             detail   = cast(action);
        final BaseReflectedFormInstance instance = BaseReflectedFormInstance.wrapInstanceHandler(detail.getForm());
        final DetailResponse            response = new DetailResponse(instance.getModel(), instance.getForm());

        if (detail.isFullscreen()) response.setFullscreen(detail.isFullscreen());
        else if (detail.isDimensionDefined()) response.setDimension(detail.getWidth(), detail.getHeight());
        else response.setDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        response.setMarginTop(detail.getMarginTop() != 0 ? detail.getMarginTop() : 100);

        response.setMessage(detail.getMsg().orElse(""));

        response.setUiReferences(resolveUiModelReferences(instance.getForm()));

        return response;
    }

    private FormModelResponse errorResponse(@NotNull Action action) {
        final ActionImpl        error    = cast(action);
        final FormModelResponse response = toResponse(some(main));

        response.getSync().setMsgType(error.getCustomMsgType().getOrNull());
        response.getSync().setAutoClose(error.isAutoClose());
        setSourceWidget(response);

        final String       errorString  = notEmpty(error.getMsg().getOrNull(), MetadataFormMessages.MSGS.somethingWentWrong());
        final List<String> errorDetails = new ArrayList<>();

        if (error.isWithSummary()) {
            for (final Widget widget : main.metadata()) {
                final CheckMsg fieldMsg = main.getFieldMsg(widget);
                if (fieldMsg != null && fieldMsg.getType() == CheckType.ERROR) errorDetails.add(widget.getLabel() + " -> " + fieldMsg);
            }
        }

        return response.error(errorString, errorDetails, error.isReload());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void handleMappingCallback(@NotNull final String callbackClass, @NotNull final FormInstance<?> branch,
                                       @NotNull final FormInstance<?> master) {
        final MappingCallback callbackInstance = Reflection.construct(callbackClass);
        switch (method.getScope()) {
        case CREATE_OR_UPDATE:
            callbackInstance.onSave(branch, master);
            break;
        case DELETE:
            callbackInstance.onDelete(branch, master);
            break;
        case CANCEL:
            callbackInstance.onCancel(branch, master);
            break;
        default:
            throw unreachable();  // Mapping callback only on termination methods
        }
    }

    private Option<FormModel> handleRedirect() {
        if (redirect != null) {
            final FormModel model = redirect.getInitializedModel(getLocalizedForm(redirect.getFqn(), redirect.getPk(), redirect.getParameters()));

            if (redirect.hasCallback()) {
                final String cb = redirect.getCallback();

                if (!IMPLICIT_CALLBACK.equals(cb)) {
                    final BaseReflectedFormInstance branch = createUserFormInstance(main);
                    final BaseReflectedFormInstance master = createUserFormInstance(model);
                    bindListener(master);

                    if (!master.getForm().containsElement(cb)) handleMappingCallback(cb, branch.getInstance(), master.getInstance());
                    else if (method.getScope() == CREATE_OR_UPDATE)
                    // We can execute the find() only on create/update, not delete/cancel
                    master.setFormField(cb, branch.find());
                }
            }
            return some(model);
        }
        else return Option.empty();
    }

    private void initSubform(Widget widget, Model model) {
        final FormModel subform = model.getSubform(widget);
        if (subform != null) {
            final Form form = getLocalizedForm(widget.getSubformFqn(), subform.getPk(), subform.getParameters());
            subform.init(form);
            initSubforms(form, subform);  // Recursion for nested subforms
        }
    }

    private void initSubforms(Form form, FormModel model) {
        for (final Widget subform : form.getUiModelElements(SUBFORM)) {
            final Option<MultipleWidget> table = subform.getMultiple();
            if (table.isEmpty()) initSubform(subform, model);
            else {
                for (final RowModel row : model.getMultiple(table.get()))
                    initSubform(subform, row);
            }
        }
    }  // end method initSubforms

    private RedirectFormResponse navigateAction(Action action) {
        final NavigateImpl<?>           navigate = cast(action);
        final BaseReflectedFormInstance instance = BaseReflectedFormInstance.wrapInstanceHandler(navigate.resolveForm());
        final RedirectFormResponse      response = new RedirectFormResponse(instance.getModel(), instance.getForm(), navigate.getPK());

        response.setMessage(navigate.getMsg().orElse(""));
        response.setAutoClose(navigate.isAutoClose());
        response.setMsgType(navigate.getCustomMsgType().getOrNull());
        response.setFormActions(navigate.getActions());
        response.setTargetFormBox(navigate.getTargetFormBox());
        response.setDialog(navigate.isDialog());
        response.setConfirmation(navigate.needsConfirmation());
        response.setLeave(navigate.isTerminate());

        final FormParameters<?> ps = navigate.getParameters();
        if (ps != null) response.setParameters(mapToQueryString(ps.asMap()));

        if (navigate.hasCallback()) response.withCallback(navigate.getCallback(main.metadata()));
        else if (handling == Handling.SYNC && !navigate.isTerminate()) response.withImplicitCallback();

        return response;
    }

    private void navigation(@NotNull final ActionImpl action, FormModelResponse response) {
        switch (action.getType()) {
        case NAVIGATE:
            response.redirect(navigateAction(action));
            break;
        case REDIRECT:
            final RedirectImpl redirectUrl = (RedirectImpl) action;
            response.url(redirectUrl.getUrl());
            response.inlineRedirect(redirectUrl.isInline());
            break;
        case EXTERNAL:
            final ExternalNavigateImpl<?> external = cast(action);
            response.url(external.getUrl());
            break;
        case DETAIL:
            response.detail(detailAction(action));
            break;
        case SWIPE:
            response.swipe(swipeAction(action));
            break;
        case OK:
            terminateAction(response, action);
            break;
        case STAY:
            stayAction(response, action, false);
            break;
        case ERROR:
            break;
        }
    }

    private List<UiModel> resolveUiModelReferences(@NotNull final Form root) {
        return resolveUiModelReferences(new ArrayList<>(), root, new HashSet<>());
    }

    private List<UiModel> resolveUiModelReferences(List<UiModel> result, UiModel parent, Set<String> visited) {
        parent.getUiModelElements(SUBFORM).forEach(widget -> {
            final String fqn = widget.getSubformFqn();
            if (visited.add(fqn)) {
                final Form f = getLocalizedForm(fqn);
                result.add(f);
                resolveUiModelReferences(result, f, visited);
            }
        });

        parent.getUiModelElements(WIDGET).forEach(widget -> {
            final String fqn = widget.getWidgetDefinitionFqn();
            if (visited.add(fqn)) {
                final WidgetDef w = getLocalizedWidgetDef(fqn);
                result.add(w);
                resolveUiModelReferences(result, w, visited);
            }
        });

        return result;
    }

    private void stayAction(FormModelResponse response, ActionImpl action, boolean provided) {
        final Form                      syncForm    = main.metadata();
        final BaseReflectedFormInstance instance    = ReflectedFormInstance.createFormInstance(syncForm, null);
        final RedirectFormResponse      redirection = new RedirectFormResponse(instance.getModel(), syncForm);
        redirection.setMessage(action.getMsg().orElse(""));
        redirection.setAutoClose(action.isAutoClose());
        redirection.setMsgType(action.getCustomMsgType().getOrNull());
        if (provided) redirection.asProvided();
        response.redirect(redirection);
    }

    /** Append references to response. */
    private void subforms(@NotNull FormModelResponse response) {
        final RedirectFormResponse redirection = response.getRedirect();
        if (redirection != null) redirection.setUiReferences(resolveUiModelReferences(redirection.getForm()));

        final LoadFormResponse load = response.getLoad();
        if (load != null) load.setUiReferences(resolveUiModelReferences(load.getForm()));

        final SyncFormResponse sync = response.getSync();
        if (sync != null) {
            final Form      form  = FormServiceServlet.getLocalizedForm(sync);
            final FormModel model = sync.getModel(form);
            initSubforms(form, model);  // todo pcolunga review why?! remove sync initialization
        }
    }                                   // end method subforms

    @Nullable private SwipeResponse swipeAction(ActionImpl action) {
        final SwipeImpl swipe = cast(action);

        final Class<? extends SwipeLoader<?>> swipeLoader = swipe.getLoaderClass();
        final FormInstance<?>                 base        = createUserFormInstance(main).getInstance();
        final SwipeLoader<?>                  loader      = Reflection.construct(swipeLoader, base);
        final HashMap<Integer, FormModel>     models      = new HashMap<>();

        Form localized = null;
        for (final Integer i : swipe.getIndexesToLoad()) {
            final BaseReflectedFormInstance loadedInstance = BaseReflectedFormInstance.wrapInstanceHandler(loader.load(i));
            models.put(i, loadedInstance.getModel());
            if (localized == null) localized = loadedInstance.getForm();
        }

        if (localized != null) {
            final SwipeResponse response = new SwipeResponse(localized, swipeLoader.getName(), models, swipe.getIndexesMap());
            response.config(swipe.getStartIndex(), swipe.isLoop(), swipe.getFetchSize());

            if (swipe.isFullscreen()) response.setFullscreen(swipe.isFullscreen());
            else if (swipe.isDimensionDefined()) response.setDimension(swipe.getWidth(), swipe.getHeight());
            else response.setDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

            response.setMarginTop(swipe.getMarginTop() != 0 ? swipe.getMarginTop() : 100);

            response.setMessage(swipe.getMsg().orElse(""));

            response.setUiReferences(resolveUiModelReferences(localized));

            return response;
        }

        return null;
    }

    private void terminateAction(FormModelResponse response, ActionImpl action) {
        if (redirect != null || handling == Handling.SYNC) {
            final TerminateFormResponse terminate = new TerminateFormResponse();
            terminate.setMessage(action.getMsg().orElse(""));
            terminate.setAutoClose(action.isAutoClose());
            terminate.setMsgType(action.getCustomMsgType().getOrNull());
            response.terminate(terminate);
        }
        else if (handling != Handling.LOAD) {
            if (redirectToInbox) {
                // If terminates after work item navigation, return to Inbox
                final RedirectFormResponse inbox = navigateAction(ActionsImpl.getInstance().navigate(Inbox.class));
                response.redirect(inbox);
            }
            else stayAction(response, action, true);
        }
    }  // end method terminateAction

    private FormModelResponse toResponse(@NotNull Option<FormModel> model) {
        return model.map(handling.asResponse()).orElse(new FormModelResponse());  // Maybe mapOrElse() ?
    }

    private void setPrimaryKeyAndParameters(@NotNull LoadFormResponse response) {
        response.setPk(pk);
        response.setParameters(parameters);
    }

    private void setSourceWidget(FormModelResponse response) {
        if (source != null) response.getSync().setSourceWidget(source);
    }

    //~ Methods ......................................................................................................................................

    private static FormModelResponse asLoad(final FormModel model) {
        return new FormModelResponse().load(new LoadFormResponse(model, model.metadata()));
    }

    private static FormModelResponse asSync(final FormModel model) {
        return new FormModelResponse().sync(new SyncFormResponse(model));
    }

    //~ Enums ........................................................................................................................................

    /**
     * Method handling context.
     */
    private enum Handling {
        /** Load. */
        LOAD,
        /** OnClick, OnChange, OnNew, etc. */
        SYNC,
        /** Create, Update, Delete, Cancel. */
        TERMINATION;

        Function<FormModel, FormModelResponse> asResponse() {
            return this == LOAD ? ResponseBuilder::asLoad : ResponseBuilder::asSync;
        }
    }
}  // end class ResponseBuilder
