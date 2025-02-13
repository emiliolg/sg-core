
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
 * A {@link Type} that represents a void Type.
 */
public class VoidType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private VoidType() {}

    //~ Methods ......................................................................................................................................

    @Override public boolean isVoid() {
        return true;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.VOID;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4423709983473935587L;

    static final VoidType INSTANCE = new VoidType();
}
