
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.type.MetaModel;

/**
 * A {@link Change} that indicates that a {@link MetaModel} label has changed.
 */
public class LabelChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final String name;
    private final String newLabel;
    private final String oldLabel;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the {@link MetaModel}, the old label and the new one. */
    public LabelChanged(String name, String oldLabel, String newLabel) {
        this.name     = name;
        this.oldLabel = oldLabel;
        this.newLabel = newLabel;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return "Label for " + name + DiffConstants.HAS_CHANGED + oldLabel + "->" + newLabel;
    }
}
