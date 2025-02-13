
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.type.EnumType;

/**
 * A {@link Change} that indicates that an {@link EnumType} value label has changed.
 */
public class EnumLabelChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final EnumType anEnum;
    private final String   id;
    private final String   newLabel;
    private final String   oldLabel;

    //~ Constructors .................................................................................................................................

    /**
     * Constructor. Receives the {@link EnumType}, the value id , the old vlaue label and the new
     * one.
     */
    public EnumLabelChanged(EnumType anEnum, String id, String oldLabel, String newLabel) {
        this.anEnum   = anEnum;
        this.id       = id;
        this.oldLabel = oldLabel;
        this.newLabel = newLabel;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return "Label for enum id " + anEnum.getFullName() + "." + id + " changed: " + oldLabel + "->" + newLabel;
    }
}
