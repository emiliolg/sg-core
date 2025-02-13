
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

/**
 * Class to solves GWT serialization issues.
 */
@SuppressWarnings("GwtInconsistentSerializableClass")  // We have a TypeSupplier_CustomFieldSerializer
public class TypeSupplier implements Supplier<Type>, Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final Type type;

    //~ Constructors .................................................................................................................................

    /** Create a serializable ref to the type. */
    public TypeSupplier(@NotNull Type type) {
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type get() {
        return type;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3151608054587670886L;
}
