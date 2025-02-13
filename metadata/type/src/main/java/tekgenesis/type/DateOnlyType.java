
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

import tekgenesis.common.core.DateOnly;

import static java.sql.Types.DATE;

import static tekgenesis.type.Types.anyType;

/**
 * A {@link Type} that represents a Date.
 */
public class DateOnlyType extends AbstractType {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        return that.isTime() ? that : anyType();
    }

    @Override public DateOnly valueOf(String str) {
        return DateOnly.fromString(str);
    }

    @Override public boolean isTime() {
        return true;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.DATE;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return "date";
    }

    @Override public int getSqlType() {
        return DATE;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3265979306842598588L;

    static final DateOnlyType DATE_ONLY_INSTANCE = new DateOnlyType();
}
