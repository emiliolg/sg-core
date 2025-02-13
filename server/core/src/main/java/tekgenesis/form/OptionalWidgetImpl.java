
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Suppliers;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;

/**
 * Implements an {@link OptionalWidget optional widget}.
 */
class OptionalWidgetImpl<W extends WidgetInstance<?>> implements OptionalWidget<W> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Supplier<W> creator;

    @NotNull private Option<W> value;

    //~ Constructors .................................................................................................................................

    OptionalWidgetImpl(@NotNull final W instance) {
        value   = some(instance);
        creator = Suppliers.empty();
    }

    OptionalWidgetImpl(@NotNull final Supplier<W> creator) {
        value        = empty();
        this.creator = creator;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public W create() {
        value.ifEmpty(this::initialize);
        return get();
    }

    @NotNull @Override public <U> Option<U> flatMap(@NotNull Function<? super W, Option<U>> f) {
        return value.flatMap(f);
    }

    @NotNull @Override public W get() {
        return value.get();
    }

    @Override public OptionalWidget<W> ifEmpty(@NotNull Runnable runnable) {
        value.ifEmpty(runnable);
        return this;
    }

    @Override public OptionalWidget<W> ifPresent(@NotNull Consumer<? super W> consumer) {
        value.ifPresent(consumer);
        return this;
    }

    @NotNull @Override public <U> Option<U> map(@NotNull Function<? super W, ? extends U> f) {
        return value.map(f);
    }

    @Override public boolean isPresent() {
        return value.isPresent();
    }

    @Override public boolean isEmpty() {
        return value.isEmpty();
    }

    private void initialize() {
        value = some(creator.get());
    }
}  // end class OptionalWidgetImpl
