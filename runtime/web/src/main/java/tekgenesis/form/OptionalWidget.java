
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.Predefined.notNull;

/**
 * Optional widget.
 */
public interface OptionalWidget<W extends WidgetInstance<?>> {

    //~ Methods ......................................................................................................................................

    /** Copy widget value to instance value. */
    default <T> void copyTo(@Nullable final T value, @NotNull final BiConsumer<W, T> copyTo, @NotNull final Consumer<T> setter) {
        ifEmpty(() -> setter.accept(null));

        /* If optional widget is defined, copy to instance (if not null). */
        ifPresent(w -> {
            if (value != null) copyTo.accept(get(), value);
        });
    }

    /** Copy widget value to instance value (if instance value is null, use value from supplier). */
    default <T> void copyTo(@Nullable final T value, @NotNull final BiConsumer<W, T> copyTo, @NotNull final Consumer<T> setter,
                            @NotNull final Supplier<T> supplier) {
        ifEmpty(() -> setter.accept(null));

        /* If optional widget is defined, copy to instance (if not null) or to a new one. */
        ifPresent(w -> {
            final T v = notNull(value, () -> {
                        final T create = supplier.get();
                        setter.accept(create);
                        return create;
                    });
            copyTo.accept(get(), v);
        });
    }

    /** Initialize widget definition if its not defined. */
    @NotNull W create();

    /**
     * Returns the widget's flatted mapped value as an option if its nonempty (The mapping function
     * returns an Option itself). Otherwise return an empty one.
     */
    @NotNull <U> Option<U> flatMap(@NotNull final Function<? super W, Option<U>> f);

    /**
     * Returns the widget definition, which must be defined.
     *
     * @throws  NoSuchElementException  if the instance is not defined
     */
    @NotNull W get();

    /** If widget is not defined, invoke the specified runnable, otherwise do nothing. */
    OptionalWidget<W> ifEmpty(@NotNull final Runnable runnable);

    /**
     * If widget is defined, invoke the specified consumer with the widget, otherwise do nothing.
     */
    OptionalWidget<W> ifPresent(@NotNull final Consumer<? super W> consumer);

    /**
     * Returns the widget's mapped value as an option if its nonempty, otherwise return an empty
     * one.
     */
    @NotNull <U> Option<U> map(@NotNull final Function<? super W, ? extends U> f);

    /** Populate widget value with instance value. */
    default <T> void populate(@Nullable final T value, @NotNull final BiConsumer<W, T> populate) {
        if (value != null) populate.accept(create(), value);
    }

    /** Returns {@code true} if widget is defined, otherwise {@code false}. */
    boolean isPresent();

    /** Returns {@code true} if widget is not defined, otherwise {@code false}. */
    boolean isEmpty();
}  // end interface OptionalWidget
