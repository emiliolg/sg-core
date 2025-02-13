
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
import tekgenesis.common.core.DateTime;

import static java.sql.Types.TIMESTAMP;

import static tekgenesis.common.Predefined.createToStringBuilder;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.dateTimeType;

/**
 * A {@link Type} that represents an Instant in time. The Type receives an integer representing the
 * precision as the number of decimal fractional seconds. (i.e. Precision 6 means: microseconds
 * precision) If not precision is specified then it is assumed that the type represents a 'Date'
 * only (i.e. Precision in days)
 */
public class DateTimeType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    private final int precision;

    //~ Constructors .................................................................................................................................

    /** Default constructor.* */
    DateTimeType() {
        precision = 0;
    }

    DateTimeType(int precision) {
        this.precision = precision;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type applyParameters(Seq<String> strings) {
        return dateTimeType(parseAsInt(strings.getFirst().orElse("0"), 0));
    }

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        return that.isTime() ? this : anyType();
    }

    @Override public boolean equals(Object obj) {
        return obj instanceof DateTimeType && precision == ((DateTimeType) obj).precision;
    }

    @Override public int hashCode() {
        return precision * 31;
    }

    @Override public String toString() {
        final String name = super.toString();
        return precision == 0 ? name : createToStringBuilder(name).add(precision).build();
    }

    @Override public DateTime valueOf(String str) {
        return DateTime.fromString(str);
    }

    @Override public boolean isTime() {
        return true;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.DATE_TIME;
    }

    @Override public int getParametersCount() {
        return 1;
    }

    /**
     * Returns the precision as the number of decimal fractional seconds. (i.e. Precision 6 means:
     * microseconds precision)
     */
    public int getPrecision() {
        return precision;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return "datetime(" + precision + ")";
    }

    @Override public int getSqlType() {
        return TIMESTAMP;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6678399116994511573L;

    static final DateTimeType DATE_TIME_INSTANCE = new DateTimeType(0);
}  // end class DateTimeType
