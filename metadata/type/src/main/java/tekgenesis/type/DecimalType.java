
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.math.BigDecimal;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.ToStringBuilder;

import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static java.sql.Types.DECIMAL;

import static tekgenesis.common.Predefined.createToStringBuilder;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.decimalType;

/**
 * A {@link Type} that represents a Decimal number.
 */
public class DecimalType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    private final int decimals;
    private final int precision;

    //~ Constructors .................................................................................................................................

    /** default constructor.* */
    public DecimalType() {
        decimals  = 0;
        precision = 0;
    }

    DecimalType(final int p, final int d) {
        precision = p;
        decimals  = d;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type applyParameters(Seq<String> strings) {
        final Iterator<String> it = strings.iterator();
        final int              p1 = it.hasNext() ? parseAsInt(it.next(), DEFAULT_PRECISION) : DEFAULT_PRECISION;
        final int              p2 = it.hasNext() ? parseAsInt(it.next(), 0) : 0;
        return decimalType(p1, p2);
    }

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        return that.isNumber() ? this : anyType();
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof DecimalType) {
            final DecimalType that = (DecimalType) obj;
            return that.precision == precision && that.decimals == decimals;
        }
        return false;
    }

    @Override public int hashCode() {
        return precision * 101 + decimals;
    }

    @Override public String toString() {
        final String name = super.toString();
        if (this == INSTANCE) return name;
        final ToStringBuilder b = createToStringBuilder(name).add(precision);
        if (decimals > 0) b.add(decimals);
        return b.build();
    }

    @Override public BigDecimal valueOf(String str) {
        return (isEmpty(str) ? BigDecimal.ZERO : new BigDecimal(str.trim())).setScale(decimals, ROUND_UNNECESSARY);
    }

    /** Return the number of decimal digits. */
    public int getDecimals() {
        return decimals;
    }

    @Override public BigDecimal getDefaultValue() {
        return BigDecimal.ZERO.setScale(decimals, ROUND_UNNECESSARY);
    }

    @NotNull @Override public Kind getKind() {
        return Kind.DECIMAL;
    }

    @Override public Option<Integer> getLength() {
        return Option.some(precision);
    }

    @Override public int getParametersCount() {
        return 2;
    }

    /** Returns the maximum number of digits of the number. */
    public int getPrecision() {
        return precision;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return "decimal(" + precision + "," + decimals + ")";
    }

    @Override public int getSqlType() {
        return DECIMAL;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1191657010366265299L;

    public static final int DEFAULT_PRECISION = 16;

    public static final DecimalType INSTANCE = new DecimalType(DEFAULT_PRECISION, 0);
}  // end class DecimalType
