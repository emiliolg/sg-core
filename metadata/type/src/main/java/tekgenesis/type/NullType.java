
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

/**
 * A {@link Type} that represents a null Type.
 */
public class NullType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private NullType() {}

    //~ Methods ......................................................................................................................................

    @Override public Object valueOf(String str) {
        return null;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.NULL;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4423709983473935587L;

    static final NullType INSTANCE = new NullType();
}
