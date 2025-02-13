
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.createToStringBuilder;

/**
 * A {@link Type} that represents an Array of elements of a given {@link Type}.
 */
public class ArrayType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Type elementType;

    //~ Constructors .................................................................................................................................

    /** Default constructor.* */
    ArrayType() {
        elementType = Types.nullType();
    }

    private ArrayType(@NotNull final Type t) {
        elementType = t;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof ArrayType && ((ArrayType) obj).elementType.equals(elementType);
    }

    @Override public boolean equivalent(Type t) {
        return t instanceof ArrayType && ((ArrayType) t).elementType.equivalent(elementType);
    }

    @Override public int hashCode() {
        return elementType.hashCode() * 31;
    }

    @Override public String toString() {
        return createToStringBuilder(super.toString()).add(elementType).build();
    }

    /** Returns the array's element type. */
    @NotNull public Type getElementType() {
        return elementType;
    }

    @Override public String getImplementationClassName() {
        final Class<?> c = getKind().getImplementationClass();
        assert c != null;
        return c.getName() + "<" + elementType.getImplementationClassName() + ">";
    }

    @NotNull @Override public Kind getKind() {
        return Kind.ARRAY;
    }

    //~ Methods ......................................................................................................................................

    static ArrayType createArrayType(@NotNull final Type t) {
        switch (t.getKind()) {
        case INT:
            return INT_ARRAY_INSTANCE;
        case REAL:
            return REAL_ARRAY_INSTANCE;
        case DECIMAL:
        default:
            return new ArrayType(t);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3429310222616410510L;

    static final ArrayType INT_ARRAY_INSTANCE     = new ArrayType(Types.intType());
    static final ArrayType REAL_ARRAY_INSTANCE    = new ArrayType(Types.realType());
    static final ArrayType DECIMAL_ARRAY_INSTANCE = new ArrayType(Types.decimalType());
    static final ArrayType STRING_ARRAY_INSTANCE  = new ArrayType(Types.stringType());
}  // end class ArrayType
