
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.Binder;
import tekgenesis.code.BoundRef;
import tekgenesis.code.SymbolNotFoundException;
import tekgenesis.common.core.DateTimeBase;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * A simple RefBinder and RefTypeSolver backed by a Map. Useful for representing simple environments
 */
public class SimpleReferenceSolver extends Binder.Default implements RefTypeSolver {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, ValueType> references = new HashMap<>();

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ValueType bindRef(@NotNull String name, boolean col) {
        final ValueType result = references.get(name);
        if (result == null) throw new SymbolNotFoundException(name);
        return result;
    }

    /** Simple implementation of {@link RefTypeSolver}. */
    @NotNull @Override public Type doResolve(@NotNull String referenceName, boolean isColumn) {
        return bindRef(referenceName, false).getType();
    }

    /** Adds an Object to the Environment. */
    @NotNull public SimpleReferenceSolver put(@NotNull String name, @NotNull final Object o) {
        references.put(name, new ValueType(o));
        return this;
    }

    /** Adds an Object to the Environment, With an specified Type. */
    @NotNull public SimpleReferenceSolver put(@NotNull String name, @NotNull final Object o, Type type) {
        references.put(name, new ValueType(o, type));
        return this;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A Holder for a value with its type.
     */
    public static class ValueType implements BoundRef<Object> {
        private final Type   type;
        private final Object value;

        /** Creates a ValueTypeRetriever from an object, inferring the type. */
        private ValueType(Object value) {
            this(value, Types.typeOf(value));
        }
        /** Creates a ValueTypeRetriever specifying value and type. */
        private ValueType(Object value, Type type) {
            this.value = value instanceof DateTimeBase ? ((DateTimeBase<?>) value).toMilliseconds() : value;
            this.type  = type;
        }

        @Override public Object apply(Object context) {
            return value;
        }

        /** Get the Type of the value. */
        protected Type getType() {
            return type;
        }
    }
}  // end class SimpleReferenceSolver
