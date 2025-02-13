
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
import tekgenesis.common.core.Option;

import static java.sql.Types.CLOB;
import static java.sql.Types.VARCHAR;

import static tekgenesis.common.Predefined.hashCodeAll;
import static tekgenesis.common.core.Constants.DEFAULT_STRING_LENGTH;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.type.Types.stringType;

/**
 * A {@link Type} that represents a String.
 */
public class StringType extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    private boolean intern;

    private final int length;

    //~ Constructors .................................................................................................................................

    private StringType() {
        length = DEFAULT_STRING_LENGTH;
    }

    StringType(final int l) {
        length = l;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type applyParameters(Seq<String> strings) {
        return stringType(parseAsInt(strings.getFirst().orElse("0"), 0));
    }

    @Override public boolean equals(Object obj) {
        return obj instanceof StringType && length == ((StringType) obj).length;
    }

    @Override public int hashCode() {
        return hashCodeAll(length);
    }

    /** Sets the string type as intern. */
    public StringType intern() {
        intern = true;
        return this;
    }

    @Override public String toString() {
        final StringBuilder b = new StringBuilder(super.toString());

        if (length != DEFAULT_STRING_LENGTH) b.append('(').append(length).append(')');

        return b.toString();
    }

    /** Returns true if I must use database CLOB type. */
    public boolean useClob() {
        return length > VAR_CHAR_MAX;
    }

    @Override public String valueOf(String str) {
        return str;
    }

    @Override public String getDefaultValue() {
        return "";
    }

    @NotNull @Override public Kind getKind() {
        return Kind.STRING;
    }

    /** Returns the maximum length of the String. */
    @Override public Option<Integer> getLength() {
        return some(length);
    }

    /** Returns true if the string should be internalized in runtime to optimize memory. */
    public boolean isIntern() {
        return intern;
    }

    @Override public int getParametersCount() {
        return 1;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return useClob() ? "clob" : "nvarchar(" + getLength().orElse(DEFAULT_STRING_LENGTH) + ")";
    }

    @Override public int getSqlType() {
        return useClob() ? CLOB : VARCHAR;
    }

    //~ Static Fields ................................................................................................................................

    public static final int VAR_CHAR_MAX = 2000;

    private static final long serialVersionUID = -878051609570082517L;

    public static final int        UNDEFINED = -1;
    public static final StringType INSTANCE  = new StringType();
}  // end class StringType
