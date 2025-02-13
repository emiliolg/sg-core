
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

/**
 * Form navigation action.
 */
public interface Navigate<T extends FormInstance<?>> extends Action {

    //~ Methods ......................................................................................................................................

    /** Add form navigation callback with a MappingCallback class. */
    @NotNull <V extends FormInstance<?>> Navigate<T> callback(final Class<? extends MappingCallback<T, V>> clazz);

    /** Add form navigation callback bind to a Widget. */
    @NotNull Navigate<T> callback(@NotNull Enum<?> field);

    /** Specify form box id to navigate in that target. */
    @NotNull Navigate<T> dialog();

    /** Specify callback flow leave. */
    @NotNull Navigate<T> leave();

    /** Leave a form with confirmation dialog. */
    @NotNull Navigate<T> leaveWithConfirmation();

    /** Specify form box id to navigate in that target. */
    @NotNull Navigate<T> targetFormBox(@NotNull final String formBoxId);

    /** Leave a form with confirmation dialog. */
    @NotNull Navigate<T> withParameters(final FormParameters<T> parameters);
}
