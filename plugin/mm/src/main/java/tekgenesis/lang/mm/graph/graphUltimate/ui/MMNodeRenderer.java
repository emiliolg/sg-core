
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.ui;

import java.awt.*;

import javax.swing.*;

import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.renderer.BasicGraphNodeRenderer;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.graph.view.NodeCellRenderer;
import com.intellij.openapi.graph.view.NodeRealizer;
import com.intellij.openapi.util.ModificationTracker;

import tekgenesis.lang.mm.graph.graphUltimate.model.Edge;
import tekgenesis.lang.mm.graph.graphUltimate.model.MMGraphNode;

import static tekgenesis.common.Predefined.cast;

/**
 * Renderer for EntityNode.
 */
class MMNodeRenderer extends BasicGraphNodeRenderer<MMGraphNode<?>, Edge<MMGraphNode<?>>> implements NodeCellRenderer {

    //~ Constructors .................................................................................................................................

    /** Renderer for MMGraphNode. */
    public MMNodeRenderer(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder, ModificationTracker tracker) {
        super(builder, tracker);
        // getBuilder().addCustomUpdater(new MMGraphUpdater());
    }

    //~ Methods ......................................................................................................................................

    public MMNodePanel getMMNodePanelForNode(MMGraphNode<?> node) {
        return new MMNodePanel(node);
    }

    public JComponent getNodeCellRendererComponent(Graph2DView view, NodeRealizer nodeRealizer, Object userObject, boolean selected) {
        return cast(userObject);
    }

    protected Color getBackground(MMGraphNode<?> node) {
        return node.getBackgroundColor();
    }

    protected Icon getIcon(MMGraphNode<?> node) {
        return node.getIcon();
    }

    protected String getNodeName(MMGraphNode<?> node) {
        return node.getName();
    }

    // private class MMGraphUpdater extends CustomGraphUpdater {
    // @Override
    // public void update(Graph2D graph2D, Graph2DView graph2DView) {
    //
    // for (com.intellij.openapi.graph.base.Edge edge : graph2D.getEdgeArray()) {
    // graph2DView.
    //
    // }
    //
    // }
    // }
}  // end class MMNodeRenderer
