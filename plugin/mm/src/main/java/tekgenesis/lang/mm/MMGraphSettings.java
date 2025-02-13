
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import tekgenesis.lang.mm.graph.graphUltimate.model.MMGraphNode;
import tekgenesis.type.ModelType;

/**
 * Plugin Settings for the Project.
 */
public class MMGraphSettings {

    //~ Instance Fields ..............................................................................................................................

    private boolean                          isHorizontalOrientation = true;
    private MMGraphNode<? extends ModelType> lastNodeSelected        = null;

    //~ Methods ......................................................................................................................................

    /** Sets Orientation for the current MMgraph. */
    public void setHorizontalOrientation(boolean horizontalOrientation) {
        isHorizontalOrientation = horizontalOrientation;
    }
    /** Gets the last node selected by user. */
    // todo cambiar de lugar
    public MMGraphNode<? extends ModelType> getLastNodeSelected() {
        return lastNodeSelected;
    }

    /** Sets the last node selected by user. */
    public void setLastNodeSelected(MMGraphNode<? extends ModelType> graphNode) {
        lastNodeSelected = graphNode;
    }

    /** Returns true if layout for the current MMgraph is Horizontal. */
    public boolean isHorizontalOrientation() {
        return isHorizontalOrientation;
    }
}
