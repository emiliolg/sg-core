
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

import tekgenesis.common.exception.ApplicationException;
import tekgenesis.common.invoker.exception.InvokerApplicationException;

/**
 * Utility methods to deal with {@link Action actions}.
 */
public interface Actions {

    //~ Methods ......................................................................................................................................

    /** Load action with primary key. */
    @NotNull <T extends FormInstance<?>> Detail detail(@NotNull final T form);

    /** Load action with primary key. */
    @NotNull <T extends FormInstance<?>> Detail detail(@NotNull final Class<T> formClass, @NotNull final String pk);

    /** Navigate to given form class action. */
    @NotNull <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final Class<T> formClass);

    /** Navigate to given form model action. */
    @NotNull <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final T form);

    /** Navigate to given form class with primary key action. */
    @NotNull <T extends FormInstance<?>> Navigate<T> navigate(@NotNull final Class<T> formClass, @NotNull final String pk);

    /** Redirect to external url action. */
    @NotNull Redirect redirect(@NotNull final String url);

    /** Swipe the rows of a given table, viewing the details of the form using the loader class. */
    @NotNull Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, FormTable<?> table);

    /** Swipe a custom view, viewing the details of the form using the loader class. */
    @NotNull Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, int startIndex, int size);

    /** Default action. */
    @NotNull Action getDefault();

    /** Error action. */
    @NotNull Action getError();

    /** Error action containing {@link InvokerApplicationException exception message}. */
    @NotNull Action getError(@NotNull InvokerApplicationException exception);

    /** Error action containing {@link ApplicationException exception message}. */
    @NotNull Action getError(@NotNull ApplicationException exception);

    /** Stay action. */
    @NotNull Action getStay();
}  // end interface Actions
