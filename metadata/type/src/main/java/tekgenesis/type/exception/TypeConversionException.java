
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Exception to be used when converting incompatible types.
 */
@SuppressWarnings({ "NonFinalFieldOfException", "FieldMayBeFinal" })
public class TypeConversionException extends TypeException {

    //~ Instance Fields ..............................................................................................................................

    private Class<?> encountered;

    @Nullable private Class<?> expected;
    private String             value;

    //~ Constructors .................................................................................................................................

    /** constructor.* */
    TypeConversionException() {
        encountered = null;
        expected    = null;
        value       = null;
    }

    /** Build an exception for an invalid conversion. */
    public TypeConversionException(@NotNull String value, @Nullable Class<?> expected, @NotNull Class<?> encountered) {
        this.expected    = expected;
        this.encountered = encountered;
        this.value       = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return expected == null ? "null type"
                                : String.format("Cant convert value %s of type %s. Expecting %s.", value, encountered.getName(), expected.getName());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -872291577119557709L;
}
