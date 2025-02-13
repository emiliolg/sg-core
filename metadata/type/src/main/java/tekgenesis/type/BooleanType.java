
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

import static java.sql.Types.*;

/**
 * A {@link Type} that represents a Boolean.
 */
public class BooleanType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private BooleanType() {}

    //~ Methods ......................................................................................................................................

    @Override public Boolean valueOf(String str) {
        return Boolean.valueOf(str);
    }

    @Override public Boolean getDefaultValue() {
        return false;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.BOOLEAN;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return Constants.BOOLEAN;
    }

    @Override public int getSqlType() {
        return BOOLEAN;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2077113471249086324L;

    static final BooleanType INSTANCE = new BooleanType();
}
