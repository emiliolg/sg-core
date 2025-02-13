
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

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.type.exception.IntLengthException;

import static java.sql.Types.BIGINT;
import static java.sql.Types.INTEGER;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.intType;

/**
 * A {@link Type} that represents an Integer.
 */
public class IntType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    private final int length;

    //~ Constructors .................................................................................................................................

    /** A maximum length Integer. */
    private IntType() {
        length = DEFAULT_INT_LENGTH;
    }

    /** An Integer with its length constrained. */
    IntType(final int length) {
        if (length > MAX_LONG_LENGTH) throw new IntLengthException("Int type can't have length " + length + " > " + MAX_LONG_LENGTH);
        this.length = length;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type applyParameters(Seq<String> strings) {
        return strings.getFirst().map(s -> intType(parseAsInt(s, MAX_INT_LENGTH))).orElse(INSTANCE);
    }

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        if (that instanceof IntType) return isLong() ? this : that;
        return that.isNumber() ? that : anyType();
    }

    @Override public boolean equivalent(final Type t) {
        return t instanceof IntType && isLong() == ((IntType) t).isLong();
    }

    @Override public String toString() {
        final StringBuilder b = new StringBuilder(super.toString());

        if (length != DEFAULT_INT_LENGTH) b.append('(').append(length).append(')');

        return b.toString();
    }

    @Override public Object valueOf(String str) {
        final String s = str.trim();
        if (isEmpty(s)) return getDefaultValue();
        // Do not inline as '?' because it will choose Long for both  https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
        if (isLong()) return Long.parseLong(s);
        return Integer.parseInt(s);
    }

    @NotNull @Override public Object getDefaultValue() {
        // Do not inline as '?' because it will choose Long for both  https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
        if (isLong()) return 0L;
        return 0;
    }

    /** Returns true if the IntType is represented as a Long in Java. */
    public boolean isLong() {
        return length > MAX_INT_LENGTH;
    }

    @Override public Class<?> getImplementationClass() {
        return isLong() ? Long.class : super.getImplementationClass();
    }

    @NotNull @Override public Kind getKind() {
        return Kind.INT;
    }

    @Override public Option<Integer> getLength() {
        return Option.some(length);
    }

    @Override public int getParametersCount() {
        return 1;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return isLong() ? Constants.BIGINT : Constants.INT;
    }

    @Override public int getSqlType() {
        return isLong() ? BIGINT : INTEGER;
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_INT_LENGTH     = String.valueOf(Integer.MAX_VALUE).length();
    public static final int DEFAULT_INT_LENGTH = MAX_INT_LENGTH - 1;
    public static final int MAX_LONG_LENGTH    = String.valueOf(Long.MAX_VALUE).length();

    private static final long serialVersionUID = 7769141490878343017L;

    static final IntType INSTANCE      = new IntType();
    static final IntType LONG_INSTANCE = new IntType(MAX_LONG_LENGTH);
}  // end class IntType
