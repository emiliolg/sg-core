
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.exception.ApplicationException;
import tekgenesis.common.invoker.exception.InvokerApplicationException;
import tekgenesis.metadata.form.InstanceReference;

import static tekgenesis.form.ActionType.*;

/**
 * Utility methods to deal with Actions.
 */
public class ActionsImpl implements Actions {

    //~ Constructors .................................................................................................................................

    /** Default constructor for injection. */
    public ActionsImpl() {}

    //~ Methods ......................................................................................................................................

    @NotNull @Override public <T extends FormInstance<?>> Detail detail(@NotNull T form) {
        return DetailImpl.detail(form);
    }

    @NotNull @Override public <T extends FormInstance<?>> Detail detail(@NotNull Class<T> formClass, @NotNull String pk) {
        return DetailImpl.detail(FormsImpl.init(formClass, pk).first());
    }

    /** Navigate to external application. */
    @NotNull public <T> ExternalNavigate<T> external(@NotNull final String endpoint, @NotNull final String application, @Nullable final String key) {
        return ExternalNavigateImpl.navigate(endpoint, application, key);
    }

    /** Navigate to the given class form. */
    @NotNull @Override public <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final Class<T> formClass) {
        return NavigateImpl.navigate(formClass);
    }

    /** Navigate to the given instance form. */
    @NotNull @Override public <T extends FormInstance<?>> NavigateImpl<T> navigate(@NotNull final T form) {
        return NavigateImpl.navigate(form);
    }

    /** Navigate to the given class form and primary key. */
    @NotNull @Override public <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final Class<T> formClass, @NotNull final String pk) {
        return NavigateImpl.navigate(formClass, pk);
    }

    /** Navigate to the given class form, primary key, and work item reference. */
    @NotNull public <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final Class<T> formClass, @NotNull final String pk,
                                                                     @NotNull final InstanceReference reference) {
        return NavigateImpl.navigate(formClass, pk, reference);
    }

    /** Redirect to external url action. */
    @NotNull @Override public Redirect redirect(@NotNull final String url) {
        return RedirectImpl.redirect(url);
    }

    /** Swipe the rows of a given table, viewing the details of the form using the loader class. */
    @NotNull @Override public Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, FormTable<?> table) {
        return SwipeImpl.swipe(loaderClass, table);
    }

    /** Swipe a custom view, viewing the details of the form using the loader class. */
    @NotNull @Override public Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, int startIndex, int size) {
        return SwipeImpl.swipe(loaderClass, startIndex, size);
    }

    /** Default action. */
    @NotNull @Override public Action getDefault() {
        return create(OK);
    }

    /** Error action. */
    @NotNull @Override public Action getError() {
        return create(ERROR);
    }

    /** Error action containing {@link InvokerApplicationException exception message}. */
    @NotNull @Override public Action getError(@NotNull InvokerApplicationException exception) {
        return getError().withMessage(exception.getMsg());
    }

    /** Error action containing {@link ApplicationException exception message}. */
    @NotNull @Override public Action getError(@NotNull ApplicationException exception) {
        return getError().withMessage(exception.getMessage());
    }

    /** Stay action. */
    @NotNull @Override public Action getStay() {
        return create(STAY);
    }

    private ActionImpl create(@NotNull final ActionType t) {
        return new ActionImpl(t);
    }

    //~ Methods ......................................................................................................................................

    /** Return Actions instance to be set on forms. */
    public static Actions getInstance() {
        return Context.getSingleton(Actions.class);
    }
}  // end class ActionsImpl
