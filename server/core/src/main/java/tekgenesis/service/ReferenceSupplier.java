
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

/**
 * Reference acting as {@link Supplier}.
 */
public class ReferenceSupplier<T> implements Supplier<T> {

    //~ Instance Fields ..............................................................................................................................

    private T value = null;

    //~ Constructors .................................................................................................................................

    /** Construct reference with no value. */
    public ReferenceSupplier() {
        value = null;
    }

    /** Construct reference with given value. */
    public ReferenceSupplier(@NotNull final T value) {
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public T get() {
        if (value == null) throw new IllegalStateException("Value for supplier not specified.");
        return value;
    }

    /** Set supplier value. */
    public void setValue(T value) {
        this.value = value;
    }
}
