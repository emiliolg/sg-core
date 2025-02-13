
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
 * Any element allowing typing.
 */
public interface Typed {

    //~ Methods ......................................................................................................................................

    /** Return element's Type. */
    @NotNull default Type getFinalType() {
        return getType().getFinalType();
    }

    /** Return element's Type. */
    @NotNull Type getType();

    /** Set element's type. */
    void setType(@NotNull Type type);
}
