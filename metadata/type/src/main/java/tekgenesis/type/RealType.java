
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

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;

import static java.sql.Types.DOUBLE;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.type.Kind.*;
import static tekgenesis.type.Types.anyType;

/**
 * A {@link Type} that represents a Real (Floating Point) number.
 */
public class RealType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    private final int MAX_DOUBLE_LENGTH = String.valueOf(Double.MAX_VALUE).length();

    //~ Constructors .................................................................................................................................

    private RealType() {}

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        return that.getKind() == DECIMAL ? that : that.isNumber() ? this : anyType();
    }

    @Override public Double valueOf(String str) {
        return isEmpty(str) ? 0 : Double.parseDouble(str.trim());
    }

    @Override public Double getDefaultValue() {
        return 0.0;
    }

    @NotNull @Override public Kind getKind() {
        return REAL;
    }

    @Override public Option<Integer> getLength() {
        return Option.some(MAX_DOUBLE_LENGTH);
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return Constants.DOUBLE;
    }

    @Override public int getSqlType() {
        return DOUBLE;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7053479976556041429L;

    static final RealType INSTANCE = new RealType();
}  // end class RealType
