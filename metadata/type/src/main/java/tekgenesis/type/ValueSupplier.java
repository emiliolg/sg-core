
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.io.Serializable;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Class to solves GWT serialization issues.
 */
public class ValueSupplier<T> implements Supplier<T>, Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final T value;

    //~ Constructors .................................................................................................................................

    /** Default constructor.* */
    ValueSupplier() {
        value = null;
    }

    /** Create a serializable ref to the constant. */
    public ValueSupplier(@NotNull T value) {
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public T get() {
        return ensureNotNull(value);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2591292276839085910L;
}
