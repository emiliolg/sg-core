
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.ui;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.graph.base.Graph;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.DeleteProvider;
import com.intellij.openapi.graph.builder.components.BasicGraphPresentationModel;
import com.intellij.openapi.graph.builder.util.GraphViewUtil;
import com.intellij.openapi.graph.view.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.graph.graphUltimate.actions.ActionFactory;
import tekgenesis.lang.mm.graph.graphUltimate.model.*;
import tekgenesis.lang.mm.psi.PsiUtils;

/**
 * Model for Entity-Relation Graph.
 */
public class MMPresentationModel extends BasicGraphPresentationModel<MMGraphNode<?>, Edge<MMGraphNode<?>>> {

    //~ Instance Fields ..............................................................................................................................

    private final MMGraphContext graphContext;

    // private BasicGraphNodeRenderer<MMGraphNode<?>, Edge<MMGraphNode<?>>> renderer = null;
    private MMNodeRenderer renderer = null;

    //~ Constructors .................................................................................................................................

    /** Model for Entity-Relation Graph. */
    public MMPresentationModel(Graph graph) {
        super(graph);
        setShowEdgeLabels(true);
        final Graph2D g = (Graph2D) graph;
        GraphContextManager.addContext(g, new MMGraphContext());
        graphContext = GraphContextManager.getGraphContext(g);
        g.addGraph2DSelectionListener(new ShowSourceSelectionListener());
    }

    //~ Methods ......................................................................................................................................

    public void customizeSettings(Graph2DView view, EditMode editMode) {
        editMode.allowBendCreation(false);
        editMode.allowEdgeCreation(false);
        editMode.allowNodeCreation(false);

        editMode.allowResizeNodes(true);
        editMode.allowMoveSelection(true);
        view.setFitContentOnResize(true);
    }

    public boolean editNode(MMGraphNode<?> node) {  // called on node double click
        return true;
    }

    public DefaultActionGroup getCommonActionGroup() {
        return ActionFactory.createContextMenuActions(getGraphBuilder());
    }

    public DeleteProvider<MMGraphNode<?>, Edge<MMGraphNode<?>>> getDeleteProvider() {
        return new MyDeleteProvider();
    }

    @NotNull public EdgeRealizer getEdgeRealizer(Edge<MMGraphNode<?>> edge) {
        final EdgeRealizer   edgeRealizer = super.getEdgeRealizer(edge);
        final MMGraphNode<?> from         = edge.getFrom();
        final MMGraphNode<?> to           = edge.getTo();
        if (!graphContext.isFormsShowing() && !graphContext.isGeneratedSourcesShowing())
            edgeRealizer.setVisible(!(from instanceof FormNode || to instanceof FormNode || from instanceof JavaNode ||
                    to instanceof JavaNode));
        else if (!graphContext.isFormsShowing()) edgeRealizer.setVisible(!(from instanceof FormNode || to instanceof FormNode));
        else if (!graphContext.isGeneratedSourcesShowing()) edgeRealizer.setVisible(!(from instanceof JavaNode || to instanceof JavaNode));

        return edgeRealizer;
    }

    @NotNull public NodeRealizer getNodeRealizer(MMGraphNode<?> node) {
        if (renderer == null) renderer = new MMNodeRenderer(getGraphBuilder(), null);
        final NodeRealizer nodeRealizer = GraphViewUtil.createNodeRealizer("JTextField", renderer);
        final MMNodePanel  mmNodePanel  = renderer.getMMNodePanelForNode(node);
        ((GenericNodeRealizer) nodeRealizer).setUserData(mmNodePanel);
        nodeRealizer.setSize(mmNodePanel.getPreferredSize().getWidth(), mmNodePanel.getPreferredSize().getHeight());

        nodeRealizer.setVisible(
            !(node instanceof FormNode && !graphContext.isFormsShowing()) &&
            !(node instanceof JavaNode && !graphContext.isGeneratedSourcesShowing()));
        return nodeRealizer;
    }

    // public NodeCellEditor getCustomNodeCellEditor(MMGraphNode<?> node){
    // return new MMNodeCellEditor(node);
    // }

    /** Get ToolTip for Node. */
    public String getNodeTooltip(@Nullable MMGraphNode<?> node) {
        if (node != null) return node.getFullName();
        return "";
    }

    // private String addTooltip(String source, @NotNull MMGraphNode node) { return node.getName(); }

    private EntityRelationDataModel getModel() {
        return (EntityRelationDataModel) getGraphBuilder().getGraphDataModel();
    }

    //~ Inner Classes ................................................................................................................................

    private class MyDeleteProvider extends DeleteProvider<MMGraphNode<?>, Edge<MMGraphNode<?>>> {
        public boolean canDeleteEdge(@NotNull Edge<MMGraphNode<?>> edge) {
            return false;
        }

        public boolean canDeleteNode(@NotNull MMGraphNode<?> node) {
            return true;
        }

        public boolean deleteEdge(@NotNull Edge<MMGraphNode<?>> edge) {
            return false;
        }

        public boolean deleteNode(@NotNull MMGraphNode<?> node) {
            final EntityRelationDataModel model = getModel();
            model.remove(node);
            model.remove(model.getEdges(node));
            return true;
        }
    }

    private class ShowSourceSelectionListener implements Graph2DSelectionListener {
        public void onGraph2DSelectionEvent(Graph2DSelectionEvent e) {
            if (e.isNodeSelection()) {
                final Node           node      = (Node) e.getSubject();
                final MMGraphNode<?> graphNode = getGraphBuilder().getNodeObject(node);

                assert graphNode != null;
                GraphContextManager.getGraphContext(e.getGraph2D()).setLastNodeSelected(graphNode);
                PsiUtils.scrollTo(graphNode.getPsi());
            }
        }
    }

    // private class MMNodeCellRendererPainter implements NodeCellRendererPainter{
    //
    // private DataMap userDataMap;
    // private NodeCellRenderer ncr;
    //
    // MMNodeCellRendererPainter(NodeCellRenderer ncr, DataMap userDataMap) {
    // this.ncr = ncr;
    // this.userDataMap = userDataMap;
    // }
    //
    //
    //
    // @Override
    // public NodeCellRenderer getNodeCellRenderer() {
    // return ncr;
    //
    // }
    //
    // @Override
    // public DataProvider getDataProvider() {
    // return userDataMap;
    // }
    //
    // @Override
    // public void paint(NodeRealizer nodeRealizer, Graphics2D graphics2D) {
    // nodeRealizer.repaint();
    // nodeRealizer.paint(graphics2D);
    // }
    //
    // @Override
    // public void paintSloppy(NodeRealizer nodeRealizer, Graphics2D graphics2D) {
    // nodeRealizer.repaint();   //nose si es necesario
    // nodeRealizer.paintSloppy(graphics2D);
    // }
    // }
    // private void registerViewModes(Graph2DView view) {
    // final NodeCellEditor simpleNodeCellEditor = new MMNodeCellEditor();
    // // instantiate an appropriate editor for the complex renderer
    // //final NodeCellEditor complexNodeCellEditor = new SwingRendererDemo.ComplexNodeCellEditor();
    //
    // // create a data provider that dynamically switches between the different NodeCellEditor instances
    // final DataProvider nodeCellEditorProvider = new DataProviderAdapter() {
    // public Object get(Object dataHolder) {
    // final NodeRealizer realizer = g.getRealizer((Node) dataHolder);
    // if (realizer instanceof GenericNodeRealizer){
    // if ("JTextField".equals(((GenericNodeRealizer) realizer).getConfiguration())){
    // return simpleNodeCellEditor;
    // }
    // }
    // return null;
    // }
    //
    // @Override
    // public int getInt(Object o) {
    // return 0;
    // }
    //
    // @Override
    // public double getDouble(Object o) {
    // return 0;
    // }
    //
    // @Override
    // public boolean getBool(Object o) {
    // return false;
    // }
    //
    // @Override
    // public boolean defined(Object o) {
    // return false;
    // }
    // };
    //
    // final EditMode editMode = new EditMode();
    // // create the CellEditorMode and give it the multiplexing NodeCellEditor provider,
    // // as well as tell it where to find the user data
    // final CellEditorMode cellEditorMode = new CellEditorMode(nodeCellEditorProvider, NodeCellRendererPainter.USER_DATA_MAP);
    //
    // // register it with the EditMode
    // editMode.setEditNodeMode(cellEditorMode);
    // // Disable generic node label assignment in the view since it would spoil the
    // // effect of the node cell editors/renderers.
    // editMode.assignNodeLabel(false);
    //
    // view.addViewMode(editMode);
    // }

    // private void addConfigurations() {
    // factory = GenericNodeRealizer.Statics.getFactory();
    // // prepare a GenericNodeRealizer to use the NodeCellRenderer for rendering
    // final NodeCellRenderer simpleNodeCellRenderer = new MMNodeCellRenderer();
    // final Map map = factory.createDefaultConfigurationMap();
    // final MMNodeCellRendererPainter mmNodeCellRendererPainter = new MMNodeCellRendererPainter(simpleNodeCellRenderer,
    // NodeCellRendererPainter.USER_DATA_MAP);
    // ((Map)((GraphBase.unwrap(map,Map.class)))).put(GenericNodeRealizer.Painter.class, mmNodeCellRendererPainter);
    // //map.put(GenericNodeRealizer.UserDataHandler.class, new
    // SimpleUserDataHandler(SimpleUserDataHandler.REFERENCE_ON_FAILURE));
    // //map.put(GenericNodeRealizer.Painter.class, mmNodeCellRendererPainter);
    // // register the configuration using the given name
    // factory.addConfiguration("JTextField", map);
    //
    // // create another configuration based on the first one, this time use a more complex renderer
    // //map.put(GenericNodeRealizer.Painter.class, new NodeCellRendererPainter(new ComplexNodeCellRenderer(),
    // NodeCellRendererPainter.USER_DATA_MAP));
    // // register it
    // //factory.addConfiguration("JTable", map);
    //
    // }
    // private class MMNodeCellEditor extends AbstractCellEditor implements NodeCellEditor {
    //
    // private static final long serialVersionUID = -1761820793013412691L;
    // private final MMNodeRenderer ncr;
    //
    //
    //
    // public MMNodeCellEditor(MMGraphNode<?odeRenderer(node);
    //
    // // initialize
    //
    // // add editor hooks
    // //                   this.ncr.tf.addActionListener(new ActionListener()
    // //                         {
    // //                        public void actionPerformed(ActionEvent ae)
    // //                         {
    // //                               SimpleNodeCellEditor.this.fireEditingStopped();
    // //                            }
    // //                       });
    // //                  this.ncr.tf.addKeyListener(new KeyAdapter()
    // //                         {
    // //                        public void keyPressed(KeyEvent ke)
    // //                         {
    // //                              if (ke.getKeyCode() ==  KeyEvent.VK_ESCAPE)
    // //                                   {
    // //                                     SimpleNodeCellEditor.this.fireEditingCanceled();
    // //                                   }
    // //                            }
    // //                       });
    // }
    //
    // public JComponent getNodeCellEditorComponent(Graph2DView view, NodeRealizer context, Object value, boolean isSelected)
    // {
    // // get the renderer as editor
    // return ncr.getNodeCellRendererComponent(view, context, value, isSelected);
    // }
    //
    // public Object getCellEditorValue()
    // {
    // // get the value this editor represents
    // return ncr.getValue();
    // }
    // }

    // public static final class MMNodeCellRenderer extends JPanel implements NodeCellRenderer {
    // JTextField tf;
    //
    // /** Default constructor. */
    // public MMNodeCellRenderer() {
    // super(new BorderLayout());
    // // create a nice GUI
    // setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
    // BorderFactory.createEtchedBorder()));
    // add(new JLabel("Content"), BorderLayout.NORTH);
    // add(tf = new JTextField(), BorderLayout.CENTER);
    // }
    //
    // public JComponent getNodeCellRendererComponent(Graph2DView view, NodeRealizer nodeRealizer, Object userObject,
    // boolean selected) {
    // // initialize the text field
    // tf.setText(String.valueOf(userObject));
    // return this;
    // }
    //
    // /** Returns value. */
    // public Object getValue() {
    // // return the value of the text field
    // return tf.getText();
    // }
    //
    // private static final long serialVersionUID = -8346101780893997009L;
    // }

}  // end class MMPresentationModel
