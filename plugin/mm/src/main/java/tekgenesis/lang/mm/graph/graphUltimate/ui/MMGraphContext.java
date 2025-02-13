
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.ui;

import tekgenesis.lang.mm.graph.graphUltimate.model.MMGraphNode;

/**
 * Context for a Certain MMGraph.
 */
public class MMGraphContext {

    //~ Instance Fields ..............................................................................................................................

    private boolean        childrenShowing         = false;
    private boolean        formsShowing            = false;
    private boolean        generatedSourcesShowing = false;
    private MMGraphNode<?> lastNodeSelected        = null;

    //~ Methods ......................................................................................................................................

    /** Toggles variable for if all children are showing in Graph. */
    public void toggleChildrenShowing() {
        childrenShowing = !childrenShowing;
    }
    /** Set variable for if all children are showing in Graph. */
    public void setChildrenShowing(boolean childrenShowing) {
        this.childrenShowing = childrenShowing;
    }
    /** Sets if Forms are showing. */
    public void setFormsShowing(boolean formsShowing) {
        this.formsShowing = formsShowing;
    }

    /** Variable for if all children are showing in Graph. */
    public boolean isChildrenShowing() {
        return childrenShowing;
    }
    /** Gets if Forms are showing. */
    public boolean isFormsShowing() {
        return formsShowing;
    }
    /** Gets if GeneratedSources are showing. */
    public boolean isGeneratedSourcesShowing() {
        return generatedSourcesShowing;
    }
    /** Sets if GeneratedSources are showing. */
    public void setGeneratedSourcesShowing(boolean generatedSourcesShowing) {
        this.generatedSourcesShowing = generatedSourcesShowing;
    }
    /** Gets the last node selected by user. */
    public MMGraphNode<?> getLastNodeSelected() {
        return lastNodeSelected;
    }

    /** Sets the last node selected by user. */
    public void setLastNodeSelected(MMGraphNode<?> graphNode) {
        lastNodeSelected = graphNode;
    }
}  // end class MMGraphContext
