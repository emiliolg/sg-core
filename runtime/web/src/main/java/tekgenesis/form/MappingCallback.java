
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
 * Mapping callback for user code implementation.
 */
public interface MappingCallback<T extends FormInstance<?>, V extends FormInstance<?>> {

    //~ Methods ......................................................................................................................................

    /** Callback called on cancel to resolve mappings. Does nothing by default. */
    @SuppressWarnings("EmptyMethod")
    default void onCancel(@NotNull final T base, @NotNull final V out) {}

    /** Callback called on delete to resolve mappings. Does nothing by default. */
    @SuppressWarnings("EmptyMethod")
    default void onDelete(@NotNull final T base, @NotNull final V out) {}

    /** Callback called on save to resolve mappings. */
    void onSave(@NotNull final T base, @NotNull final V out);
}
