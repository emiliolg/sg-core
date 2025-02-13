
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

import static java.sql.Types.BLOB;

/**
 * A {@link Type} that represents the root of the Type hierarchy.
 */
public class AnyType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private AnyType() {}

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Kind getKind() {
        return Kind.ANY;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return "BLOB";
    }

    @Override public int getSqlType() {
        return BLOB;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7956670218618443645L;

    static final AnyType INSTANCE = new AnyType();
}
