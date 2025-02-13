
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.model;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

/**
 * Defines a labelled value.
 */
public interface Labelled<T> {

    //~ Methods ......................................................................................................................................

    /** Return labelled label. */
    @NotNull String label();

    /** Return labelled value. */
    @NotNull T value();

    //~ Inner Classes ................................................................................................................................

    abstract class Default<T> implements Labelled<T> {
        final T value;

        Default(@NotNull T value) {
            this.value = value;
        }

        @NotNull @Override public abstract String label();

        @NotNull @Override public T value() {
            return value;
        }
    }

    abstract class LabelledMapper<T> implements Function<T, Labelled<T>> {
        @Override public Labelled<T> apply(T v) {
            return new Default<T>(v) {
                @NotNull @Override public String label() {
                    return LabelledMapper.this.label(value);
                }
            };
        }

        @NotNull abstract String label(@NotNull T value);
    }
}
