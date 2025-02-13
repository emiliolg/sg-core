
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.ApplicationAudit;
import tekgenesis.authorization.OrgUnit;
import tekgenesis.authorization.User;
import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.exception.ApplicationException;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.*;
import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.form.extension.FormExtensionRegistry;
import tekgenesis.form.feedback.FeedbackEvent;
import tekgenesis.form.feedback.FeedbackEventImpl;
import tekgenesis.form.feedback.FeedbackReporter;
import tekgenesis.form.properties.FormProps;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.link.Link;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.menu.MenuItem;
import tekgenesis.metadata.menu.MenuLocalizer;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModelKind;
import tekgenesis.view.client.service.Feedback;
import tekgenesis.view.shared.logging.LoggingUtils;
import tekgenesis.view.shared.response.BootResponse;
import tekgenesis.view.shared.response.FavoriteResponse;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.MailValidationResponse;
import tekgenesis.view.shared.response.MailValidationResponse.MailValidationError;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.OrgUnitsResponse;
import tekgenesis.view.shared.response.SwipeFetchResponse;
import tekgenesis.view.shared.response.SyncFormResponse;
import tekgenesis.view.shared.utils.Favorite;

import static java.lang.String.format;

import static tekgenesis.authorization.g.FavoriteBase.listWhere;
import static tekgenesis.authorization.g.FavoriteTable.FAVORITE;
import static tekgenesis.authorization.shiro.AuthorizationUtils.*;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.NEWRELIC_AGENT_TRANSACTION_NAME;
import static tekgenesis.common.core.Constants.RESOURCE_SERVLET_PATH;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.service.Parameters.queryStringToMap;
import static tekgenesis.form.ActionsImpl.getInstance;
import static tekgenesis.form.ReflectedFormInstance.createFormInstance;
import static tekgenesis.form.exprs.ServerExpressions.*;
import static tekgenesis.form.feedback.FeedbackEvent.FeedbackType;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.metadata.link.Links.getFqn;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;
import static tekgenesis.type.MetaModelKind.FORM;
import static tekgenesis.type.MetaModelKind.LINK;
import static tekgenesis.type.permission.PredefinedPermission.READ;
import static tekgenesis.view.client.service.Feedback.TicketType;
import static tekgenesis.view.server.servlet.gwtservices.BootService.createFeedbackForm;
import static tekgenesis.view.server.servlet.gwtservices.FormMethod.DEFAULT;
import static tekgenesis.view.server.servlet.gwtservices.FormMethod.Scope.CREATE_OR_UPDATE;
import static tekgenesis.view.server.servlet.gwtservices.FormMethod.Scope.DELETE;
import static tekgenesis.view.server.servlet.gwtservices.MailAddressValidator.doesDomainExist;
import static tekgenesis.view.server.servlet.gwtservices.MailAddressValidator.mailAddressExist;

/**
 * Implementation of the Servlet to handle Forms.
 */
public class FormServiceServlet extends FormServiceServletProxy {

    //~ Methods ......................................................................................................................................

    @Override public FormModelResponse handleOnRowChangeSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse response) {
        final String formFullName = response.getSync().getFqn();
        LoggingUtils.logOnRowChange(logger, listOf(source), formFullName, false);
        setTransactionName(formFullName, "rowChangeSync", source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnChanges(model, listOf(source));
                    }
                }, response).build();
    }

    @Override public FormModelResponse handleOnRowSelectedSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnClick(logger, source, fqn, false);
        setTransactionName(fqn, "rowSelectedSync", source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnSelected(model, listOf(source));
                    }
                }, response).build();
    }

    @Override protected FormModelResponse handleCancelForm(@NotNull final SourceWidget source, @NotNull final FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logCancel(logger, fqn, false);
        setTransactionName(fqn, "cancelForm", source.toString());

        final FormMethod cancel = new FormMethod(FormMethod.Scope.CANCEL) {
                @Override public Action apply(FormModel model) {
                    return execCancel(model, source);
                }
            };

        return builderTerminate(cancel, response).build();
    }

    @Override protected void handleFeedback(@NotNull final Feedback feedback) {
        setTransactionName(feedback.getApplication(), "handleFeedback");

        final FeedbackEvent event = new FeedbackEventImpl(feedback.getSummary(),
                feedback.getDescription(),
                resolveType(feedback.getType()),
                feedback.getUrl(),
                feedback.getApplication(),
                ApplicationContextImpl.getInstance().getUser(),
                DateTime.current(),
                feedback.getHistory(),
                feedback.getError());

        for (final FeedbackReporter reporter : getEnabledReporters())
            reporter.reportFeedback(event);
    }

    @Override void handleAddFavorite(@NotNull String formLink, int newIdx) {
        setTransactionName(formLink, "addFavorite");

        runInTransaction(() -> new tekgenesis.authorization.Favorite().setLink(formLink).setUser(getCurrentUser()).setIndex(newIdx).persist());
    }

    @Override BootResponse handleBoot() {
        final BootResponse result = new BootResponse();

        try {
            if (!getEnabledReporters().isEmpty()) result.withFeedbackForm(createFeedbackForm());
            bootUser(result);
            bootProperties(result);
        }
        catch (final Exception e) {
            logger.error(e);

            result.setError(true);
            result.setErrorMessage(notEmpty(e.getMessage(), ""));
        }

        return result;
    }

    @Override FormModelResponse handleDeleteForm(@NotNull final SourceWidget source, @NotNull final FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logDelete(logger, fqn, false);
        setTransactionName(fqn, MMCodeGenConstants.DELETE_METHOD, source.toString());

        final FormMethod delete = new FormMethod(DELETE) {
                @Override public Action apply(FormModel model) {
                    return execDelete(model, source);
                }
            };

        return builderTerminate(delete, response).build();
    }

    @Override FormModelResponse handleDeprecateForm(final boolean deprecate, final FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logDeprecate(logger, deprecate, fqn, false);
        setTransactionName(fqn, MMCodeGenConstants.DEPRECATE_METHOD);

        final FormMethod method = new FormMethod() {
                @Override public Action apply(FormModel model) {
                    return execDeprecate(model, NONE, deprecate);
                }
            };

        return builderSync(method, response).build();
    }

    @Override SwipeFetchResponse handleFetchSwipeModels(@NotNull final FormModelResponse response, @NotNull final List<Integer> fetchIndexes,
                                                        @NotNull final String loaderClassName) {
        LoggingUtils.logFetch(logger, fetchIndexes, loaderClassName, false);
        setTransactionName(response.getSync().getFqn(), "fetchSwipe");

        final Form            form        = getLocalizedForm(response.getSync());
        final FormInstance<?> base        = ReflectedFormInstance.create(response.getSync().getModel(form)).getInstance();
        final SwipeLoader<?>  swipeLoader = Reflection.construct(loaderClassName, base);

        final HashMap<Integer, FormModel> models = new HashMap<>();
        for (final Integer i : fetchIndexes) {
            final FormInstance<?>           load           = swipeLoader.load(i);
            final BaseReflectedFormInstance loadedInstance = BaseReflectedFormInstance.wrapInstanceHandler(load);
            models.put(i, loadedInstance.getModel());
        }

        return new SwipeFetchResponse(models);
    }

    @Override FavoriteResponse handleGetFavorites() {
        final ModelRepository repository = Context.getSingleton(ModelRepository.class);

        final Seq<Favorite> map = getCurrentUser().getFavorites().map(f -> {
                for (final Form form : repository.getModel(getFqn(f.getLink()), Form.class)) {
                    final String label = localizer(form).localize().getLabel();
                    return new Favorite(label, f.getLink());
                }
                return new Favorite(f.getLink(), f.getLink());
            });

        return new FavoriteResponse(map.toList());
    }

    @Override MenuResponse handleGetMenu(@Nullable final String menuFqn) {
        LoggingUtils.logGetMenu(logger, menuFqn, false);

        final ModelRepository repository = Context.getSingleton(ModelRepository.class);
        final Seq<Menu>       menuSeq    = repository.getModels().filter(Menu.class);
        final Seq<Menu>       menus      = menuSeq.filter(menu -> menuFqn == null || menu != null && menu.getKey().getFullName().equals(menuFqn));

        Seq<Form> forms = null;

        if (menus.isEmpty()) forms = repository.getModels().filter(Form.class).filter(FormServiceServlet::hasReadPermission).map(FormUtils::localize);

        return MenuResponse.create(menus, forms, menuSeq, FormServiceServlet::readPermissionMenu, FormServiceServlet::localizeMenu);
    }

    @Override OrgUnitsResponse handleGetOrgUnits() {
        final OrganizationalUnit currentOrgUnit = AuthorizationUtils.getCurrentOrgUnit();
        final OrgUnitsResponse   response       = new OrgUnitsResponse(currentOrgUnit.getName());
        for (final OrgUnit userOrgUnit : getUserOrgUnits())
            response.addOrgUnit(userOrgUnit.getName(), userOrgUnit.getDescription());
        return response;
    }

    @Override FormModelResponse handleLazyFetchSync(final SourceWidget source, int offset, int limit, FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logLazyFetch(logger, source, fqn, false);
        setTransactionName(fqn, "lazyFetchSync", source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execLazyFetch(model, offset, limit, source);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleLoadFormInstance(String formId, String pk, String parameters) {
        LoggingUtils.logLoad(logger, formId, pk, parameters, false);
        setTransactionName(formId, "load", parameters);

        if (isAuthenticated()) snapshot(getCurrentUser(), formId);

        final Form form = FormExtensionRegistry.getLocalizedForm(formId, pk, parameters);
        if (form.isExternal()) return resolveExternalNavigation(form);

        String p = parameters;  // p is for decoded parameters
        try {
            if (parameters != null) p = URLDecoder.decode(parameters, UTF8);
        }
        catch (final UnsupportedEncodingException e) {
            logger.error(e);
        }

        final BaseReflectedFormInstance instance;

        try {
            instance = createFormInstance(form, pk, queryStringToMap(p));
        }
        catch (final ApplicationException e) {
            return new FormModelResponse().error(e.getMessage(), new ArrayList<>(), false);
        }

        return builderLoad(DEFAULT, instance.getModel()).withPk(pk).withParameters(parameters).build();
    }

    @Override FormModelResponse handleLoadSync(@NotNull final SourceWidget source, @Nullable final String pk,
                                               @NotNull final FormModelResponse response) {
        LoggingUtils.logOnClick(logger, source, pk, false);
        setTransactionName(response.getSync().getFqn(), "loadSync", pk);

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel origin) {
                        return execFunction(origin,
                            source,
                            (instance, option) -> {
                                final IndexedWidget indexed = option.get();
                                final Widget        widget  = indexed.widget();

                                final String fqn      = widget.getLinkForm().getFullName();
                                final Form   redirect = FormExtensionRegistry.getLocalizedForm(fqn, pk, null);

                                return createAutoRedirect(widget, origin, redirect, pk);
                            });
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnAbstractSync(final SourceWidget delegate, final SourceWidget source, FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logAbstractMethod(logger, delegate, source, fqn, false);
        setTransactionName(fqn, "onAbstractSync", delegate.toString() + " <- " + source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnAbstract(model, delegate, source);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnBlurSync(final SourceWidget source, FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnBlur(logger, source, fqn, false);
        setTransactionName(fqn, "onBlurSync", source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnBlur(model, source);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnChangeSync(final List<SourceWidget> sources, FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnChange(logger, sources, fqn, false);
        setTransactionName(fqn, "onChangeSync");

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnChanges(model, sources);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnClickSync(final SourceWidget source, FormModelResponse response, final Option<ExecutionFeedback> feedback) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnClick(logger, source, fqn, false);
        setTransactionName(fqn, "onClickSync", source.toString());

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return execOnClick(model, source, feedback);
                    }
                }, response, source).build();
    }

    @Override FormModelResponse handleOnDisplaySync(@NotNull FormModelResponse response) {
        setTransactionName(response.getSync().getFqn(), "onDisplaySync");

        return builderSync(new FormMethod() {
                    @Override public Action apply(@NotNull FormModel model) {
                        return execOnDisplay(model);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnMethodInvocation(@NotNull final String method, @NotNull final FormModelResponse response) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnMethodInvocation(logger, method, fqn, false);
        setTransactionName(fqn, "onMethod", method);
        return builderSync(new FormMethod() {
                    @Override public Action apply(@NotNull FormModel model) {
                        return execOnMethodInvocation(method, model);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnNewLocaBlurSync(SourceWidget source, FormModelResponse response, double lat, double lng) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logOnNewLocation(logger, source, fqn, false);

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel model) {
                        return ServerExpressions.execOnNewLocation(model, source, lat, lng);
                    }
                }, response).build();
    }

    @Override FormModelResponse handleOnNewSync(final SourceWidget source, final String text, FormModelResponse response) {
        final String fqn1 = response.getSync().getFqn();
        LoggingUtils.logOnClick(logger, source, fqn1, false);
        setTransactionName(fqn1, "onNewSync");

        return builderSync(new FormMethod() {
                    @Override public Action apply(FormModel origin) {
                        return execFunction(origin,
                            source,
                            (instance, option) -> {
                                final IndexedWidget indexed = option.get();
                                final Widget        widget  = indexed.widget();

                                if (widget.getOnNewForm().isNotEmpty()) {
                                    final String fqn      = widget.getOnNewForm().getFullName();
                                    final Form   redirect = FormExtensionRegistry.getLocalizedForm(fqn);
                                    return createAutoRedirect(widget, origin, redirect, null);
                                }
                                else      // noinspection ConstantConditions
                                    return invokeUserMethod(instance, indexed.item(), widget, widget.getOnNewMethodName(), text);
                            });
                    }
                }, response).build();
    }

    @Override FormModelResponse handlePostForm(@NotNull final SourceWidget source, @NotNull FormModelResponse response,
                                               @NotNull final String action) {
        final String fqn = response.getSync().getFqn();
        LoggingUtils.logSubmit(logger, fqn, action, false);
        setTransactionName(fqn, "post", action);

        final FormMethod persist = new FormMethod(CREATE_OR_UPDATE) {
                @Override public Action apply(FormModel model) {
                    return execCreateOrUpdate(model, source);
                }
            };

        return builderTerminate(persist, response).withSubmitAction(action).build();
    }  // end method handlePostForm

    @Override void handleRemoveFavorite(int idx) {
        final User user = getCurrentUser();

        runInTransaction(() -> {
            setTransactionName(user.getName(), "removeFavorite", String.valueOf(idx));
            user.getFavorites()                               //
            .getFirst(f -> f != null && f.getIndex() == idx)  //
            .ifPresent(tekgenesis.authorization.Favorite::delete);

            listWhere(FAVORITE.USER_ID.eq(user.getId()).and(FAVORITE.INDEX.ge(idx)))  //
            .forEach(favorite -> {
                if (favorite.getIndex() == idx) favorite.delete();
                else if (favorite.getId() > idx) favorite.setIndex(favorite.getIndex() - 1).persist();
            });
        });
    }

    @Override void handleSetOrgUnit(@NotNull String ouName) {
        setTransactionName(ouName, "setOu");

        for (final OrgUnit orgUnit : invokeInTransaction(() -> option(OrgUnit.find(ouName))))
            AuthorizationUtils.setCurrentOrgUnit(orgUnit);
    }

    @Override void handleSwapFavorites(int idxA, int idxB) {
        final User user = getCurrentUser();
        setTransactionName(user.getName(), "swapFavorite", idxA + "-" + idxB);
        runInTransaction(() ->
                listWhere(FAVORITE.USER_ID.eq(user.getId()).and(FAVORITE.INDEX.eq(idxA).or(FAVORITE.INDEX.eq(idxB))))  //
                .forEach(favorite -> {
                    if (favorite.getIndex() == idxA) favorite.setIndex(idxB).persist();
                    else favorite.setIndex(idxA).persist();
                }));
    }

    /**
     * Checks domain existence, and may check address existence according to checkAddress boolean
     * param.
     */
    @Override MailValidationResponse handleValidateEmail(String mail, boolean checkAddress) {
        setTransactionName("validateEmail", String.valueOf(checkAddress));
        try {
            return new MailValidationResponse(
                doesDomainExist(mail) ? !checkAddress || mailAddressExist(mail) ? MailValidationError.NONE : MailValidationError.ADDRESS
                                      : MailValidationError.DOMAIN);
        }
        catch (final ConnectException ignore) {
            return new MailValidationResponse(MailValidationError.CONNECTION);
        }
    }

    private void bootProperties(BootResponse response) {
        final Environment env = Context.getEnvironment();

        response.withUnigisServerUrl(env.get(FormProps.class).mapUnigisServer)
            .withResourceServerUrl(notEmpty(env.get(ApplicationProps.class).resourceUrl, RESOURCE_SERVLET_PATH))
            .withMailValidatorEnabled(env.get(FormProps.class).mailValidatorEnabled);

        // @todo(imanzano) if(env.isProduction()) response.productionMode();
    }

    private void bootUser(BootResponse response) {
        String username  = "";
        String userId    = "";
        String userImage = "";
        int    timeout   = -1;

        if (Context.getContext().hasBinding(Session.class) && getSession().isAuthenticated()) {
            final String id   = getSession().getPrincipal().getId();
            final User   user = ensureNotNull(invokeInTransaction(() -> User.find(id)), format("Sui Generis user expected with id: %s", id));
            username  = user.getName();
            userId    = user.getId();
            userImage = user.getImage();
            timeout   = getSession().getTimeout();
        }

        response.forUser(username, userId, userImage).withSessionTimeout(timeout);
    }

    private Action createAutoRedirect(@NotNull final Widget widget, FormModel origin, @NotNull final Form redirect, @Nullable final String pk) {
        final Class<FormInstance<?>>    formClass = ReflectedFormInstance.getClass(redirect);
        final Navigate<FormInstance<?>> navigate  = isEmpty(pk) ? getInstance().navigate(formClass) : getInstance().navigate(formClass, pk);

        // Assert redirect Form matches field type.
        if (redirect.getBinding().getFullName().equals(widget.getType().getImplementationClassName())) {
            final Integer ordinal = origin.metadata().getOrdinal(widget);
            if (ordinal != null) ((NavigateImpl<?>) navigate).callback(ordinal);
        }

        return navigate;
    }

    private FormModelResponse resolveExternalNavigation(Form form) {
        final ExternalNavigateImpl<?> action   = Reflection.invokeStatic(form.getFullName(), "navigate");
        final FormModelResponse       response = new FormModelResponse();
        if (action != null) response.url(action.getUrl());
        return response;
    }

    private FeedbackType resolveType(@NotNull final TicketType type) {
        final FeedbackType result;
        switch (type) {
        case EXCEPTION:
            result = FeedbackType.EXCEPTION;
            break;
        case ERROR:
            result = FeedbackType.ERROR;
            break;
        case SUGGESTION:
        default:
            result = FeedbackType.SUGGESTION;
            break;
        }
        return result;
    }

    private void snapshot(User user, String application) {
        runInTransaction(() -> {
            ApplicationAudit audit  = ApplicationAudit.find(application, user.getId());
            final boolean    exists = audit != null;

            final DateTime lastEvent = DateTime.current();

            if (!exists) audit = ApplicationAudit.create(application, user.getId());
            else {
                final DateTime lastAudit     = audit.getLastEvent();
                final boolean  differentYear = lastAudit.getYear() != lastEvent.getYear();

                final org.joda.time.DateTime jodaLastAudit = new org.joda.time.DateTime(lastAudit.toMilliseconds());
                final org.joda.time.DateTime jodaLastEvent = new org.joda.time.DateTime(lastEvent.toMilliseconds());

                if (differentYear || lastEvent.daysFrom(lastAudit) > 0) {
                    audit.setYesterdayEvents(audit.getDayEvents());
                    audit.setDayEvents(0);
                }
                if (differentYear || jodaLastAudit.getWeekOfWeekyear() != jodaLastEvent.getWeekOfWeekyear()) {
                    audit.setLastWeekEvents(audit.getWeekEvents());
                    audit.setWeekEvents(0);
                }
                if (differentYear || lastAudit.getMonth() != lastEvent.getMonth()) {
                    audit.setLastMonthEvents(audit.getMonthEvents());
                    audit.setMonthEvents(0);
                }
            }

            audit.setDayEvents(audit.getDayEvents() + 1);
            audit.setWeekEvents(audit.getWeekEvents() + 1);
            audit.setMonthEvents(audit.getMonthEvents() + 1);

            audit.setLastEvent(lastEvent);

            if (exists) audit.update();
            else audit.insert();
        });
    }  // end method snapshot

    private void setTransactionName(String formFullName, String method) {
        setTransactionName(formFullName, method, "");
    }

    private void setTransactionName(String formFullName, String method, @Nullable String element) {
        final HttpServletRequest request = getThreadLocalRequest();
        if (request != null)
            request.setAttribute(NEWRELIC_AGENT_TRANSACTION_NAME, formFullName + "/" + method + (isNotEmpty(element) ? "/" + element : ""));
    }

    //~ Methods ......................................................................................................................................

    /** Builder for sync. */
    static ResponseBuilder builderSync(@NotNull FormMethod method, @NotNull FormModelResponse response) {
        return builderSync(method, response, null);
    }

    static ResponseBuilder builderSync(@NotNull FormMethod method, @NotNull FormModelResponse response, @Nullable SourceWidget widget) {
        final Form form = getLocalizedForm(response.getSync());
        if (!(form.isUnrestricted() || isAuthenticated())) throw new UnauthenticatedException();
        return builderPlain(method).sync(response.getSync(), form, widget);
    }

    @NotNull static Form getLocalizedForm(final SyncFormResponse sync) {
        return FormExtensionRegistry.getLocalizedForm(sync.getFqn(), sync.getPk(), sync.getParameters());
    }

    /** Builder for load. */
    private static ResponseBuilder builderLoad(@NotNull FormMethod method, @NotNull FormModel load) {
        return builderPlain(method).load(load);
    }

    /** Plain builder. */
    private static ResponseBuilder builderPlain(@NotNull FormMethod method) {
        return new ResponseBuilder(method);
    }

    /** Builder for terminations and piggybacked redirection. */
    private static ResponseBuilder builderTerminate(@NotNull FormMethod method, @NotNull FormModelResponse response) {
        return builderSync(method, response).redirect(response.getRedirect());
    }

    private static boolean hasReadPermission(final Form form) {
        return isPermitted(form, READ);
    }

    private static boolean hasReadPermission(Link link) {
        return hasPermission(createQName(link.getDomain(), link.getName()), READ);
    }

    private static Menu localizeMenu(final Menu menu) {
        return new MenuLocalizer(menu, Context.getContext().getLocale()).localize();
    }

    @NotNull private static Menu readPermissionMenu(final Menu menu) {
        final Seq<MenuItem>  items        = menu.getChildren();
        final List<MenuItem> allowedItems = new ArrayList<>();

        for (final MenuItem item : items) {
            final MetaModelKind   targetType = item.getTargetType();
            boolean               isAllowed  = true;
            final ModelRepository repository = Context.getSingleton(ModelRepository.class);
            if (targetType == FORM) {
                final Option<Form> f = repository.getModel(createQName(item.getTarget()), Form.class);
                isAllowed = hasReadPermission(f.get());
            }
            if (targetType == LINK) {
                final Link link = (Link) item.getModel();
                isAllowed = hasReadPermission(link);

                if (isAllowed && link.hasForm()) {
                    final Option<Form> f = repository.getModel(createQName(item.getTarget()), Form.class);
                    isAllowed = hasReadPermission(f.get());
                }
            }

            if (isAllowed) allowedItems.add(item);
        }

        return new Menu(menu, allowedItems);
    }

    @NotNull private static Seq<FeedbackReporter> getEnabledReporters() {
        return Colls.seq(ServiceLoader.load(FeedbackReporter.class)).filter(FeedbackReporter::isEnabled);
    }

    //~ Static Fields ................................................................................................................................

    private static final long   serialVersionUID = 2380163346830091984L;
    private static final Logger logger           = getLogger(FormServiceServlet.class);
}  // end class FormServiceServlet
