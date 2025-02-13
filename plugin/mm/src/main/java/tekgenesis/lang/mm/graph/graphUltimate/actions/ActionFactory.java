
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.actions;

import javax.swing.*;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.*;
import com.intellij.openapi.graph.builder.actions.export.ExportToFileAction;
import com.intellij.openapi.graph.builder.actions.layout.*;
import com.intellij.openapi.graph.builder.actions.printing.PrintGraphAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintPreviewAction;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.settings.GraphSettingsProvider;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.MMProjectComponent;
import tekgenesis.lang.mm.graph.graphUltimate.model.Edge;
import tekgenesis.lang.mm.graph.graphUltimate.model.MMGraphNode;
import tekgenesis.lang.mm.graph.graphUltimate.ui.GraphContextManager;
import tekgenesis.lang.mm.graph.graphUltimate.ui.MMGraphContext;
import tekgenesis.lang.mm.graph.graphUltimate.utils.UltimateGraphUtils;

/**
 * MMUltimateGraph Actions.
 */
public class ActionFactory {

    //~ Constructors .................................................................................................................................

    private ActionFactory() {}

    //~ Methods ......................................................................................................................................

    /** Default Actions. */
    public static DefaultActionGroup createActions(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
        return createActions(builder, false);
    }

    /** Default ContextMenu Actions. */
    public static DefaultActionGroup createContextMenuActions(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
        return createActions(builder, true);
    }

    private static DefaultActionGroup createActions(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder, boolean contextMenu) {
        final DefaultActionGroup actions = new DefaultActionGroup();

        final Graph2D graph = builder.getGraph();

        actions.addSeparator();
        actions.add(new ChangeLayoutOrientationAction(builder));
        actions.add(new ShowAllChildrenAction(graph, builder));
        actions.add(new ShowFormsAction(graph, builder));
        actions.add(new ShowGeneratedSourcesAction(graph, builder));
        if (contextMenu) actions.add(new ShowChildrenAction(graph, builder));
        actions.addSeparator();
        actions.add(new LayoutGraphGroup(graph));
        actions.addSeparator();
        // actions.add(new DeleteSelectionAction());

        actions.add(new ShowHideGridAction(graph));
        actions.add(new SnapToGridAction(graph));
        actions.addSeparator();

        actions.add(new ZoomInAction(graph));
        actions.add(new ZoomOutAction(graph));
        actions.add(new ActualZoomAction(graph));
        actions.addSeparator();

        actions.add(new ExportToFileAction(graph));
        actions.add(new PrintGraphAction(graph));
        actions.add(new PrintPreviewAction(graph));

        return actions;
    }

    //~ Inner Classes ................................................................................................................................

    private abstract static class AbstractShowChildrenAction extends AbstractGraphToggleAction {
        GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder;
        private boolean                                    childrenShowing = false;
        private final Graph2D                              graph;

        /** Abstract Action for showing children in Graph. */
        AbstractShowChildrenAction(Graph2D graph, GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder, Icon icon) {
            super(graph, icon);
            this.builder = builder;
            this.graph   = graph;
        }

        public void toggleChildrenShowing() {
            childrenShowing = !childrenShowing;
        }

        public void setChildrenShowing(boolean childrenShowing) {
            this.childrenShowing = childrenShowing;
        }

        public boolean isChildrenShowing() {
            return childrenShowing;
        }

        @Override protected boolean isSelected(Graph2D graph2D, Project project, AnActionEvent anActionEvent) {
            return isChildrenShowing();
        }

        @Override protected abstract void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent);
        @Override protected abstract String getText(@NotNull Graph2D graph2D);

        Graph2D getGraph() {
            return graph;
        }
    }

    /**
     * Action in charge of toggling the Layout Orientation.
     */
    static class ChangeLayoutOrientationAction extends AbstractGraphToggleAction {
        /** Action in charge of toggling the Layout Orientation. */
        public ChangeLayoutOrientationAction(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
            super(builder.getGraph(), MMFileType.ENTITY_ICON);
        }

        protected boolean isSelected(Graph2D graph, Project project, AnActionEvent event) {
            return MMProjectComponent.getSettings(project).isHorizontalOrientation();
        }

        protected void setSelected(Graph2D graph, boolean state, Project project, AnActionEvent e) {
            MMProjectComponent.getSettings(project).setHorizontalOrientation(state);
            final Layouter layouter = GraphSettingsProvider.getInstance(project).getSettings(graph).getCurrentLayouter();
            UltimateGraphUtils.setLayoutOrientation(layouter, state);
            UltimateGraphUtils.layout(graph, layouter);
        }

        protected String getText(@NotNull Graph2D graph) {
            return "Change Layout Orientation";
        }
    }

    private static class LayoutGraphGroup extends DefaultActionGroup {
        private LayoutGraphGroup(Graph2D graph2D) {
            setPopup(true);
            setEnabledInModalContext(true);
            getTemplatePresentation().setText("Layouts");
            getTemplatePresentation().setIcon(MMFileType.LAYOUT);
            final CircularLayouterAction circularLayouterAction = new CircularLayouterAction(graph2D);
            circularLayouterAction.getTemplatePresentation().setText("Circular Layout");
            add(circularLayouterAction);
            final DirectedOrthogonalLayouterAction directedOrthogonalLayouterAction = new DirectedOrthogonalLayouterAction(graph2D);
            directedOrthogonalLayouterAction.getTemplatePresentation().setText("Directed Orthogonal Layout");
            add(directedOrthogonalLayouterAction);
            final OrthogonalLayouterAction orthogonalLayouterAction = new OrthogonalLayouterAction(graph2D);
            orthogonalLayouterAction.getTemplatePresentation().setText("Orthogonal Layout");
            add(orthogonalLayouterAction);
            final HVTreeLayouterAction hvTreeLayouterAction = new HVTreeLayouterAction(graph2D);
            hvTreeLayouterAction.getTemplatePresentation().setText("HVTree Layout");
            add(hvTreeLayouterAction);
            final OrganicLayouterAction organicAction = new OrganicLayouterAction(graph2D);
            organicAction.getTemplatePresentation().setText("Organic Layout");
            add(organicAction);
            final HierarchicGroupLayouterAction hierarchicGroupLayouterAction = new HierarchicGroupLayouterAction(graph2D);
            hierarchicGroupLayouterAction.getTemplatePresentation().setText("Hierarchic Layout");
            add(hierarchicGroupLayouterAction);
        }
    }

    private static class ShowAllChildrenAction extends AbstractShowChildrenAction {
        public ShowAllChildrenAction(Graph2D graph, GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
            super(graph, builder, MMFileType.ATTRIBUTE_ICON);
        }

        @Override public void update(AnActionEvent anActionEvent) {
            super.update(anActionEvent);
            setChildrenShowing(GraphContextManager.getGraphContext(getGraph()).isChildrenShowing());
        }

        @Override protected void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent) {
            for (final MMGraphNode<?> node : builder.getNodeObjects()) {
                if (isChildrenShowing()) node.setShowChildren(false);
                else node.setShowChildren(true);
            }
            toggleChildrenShowing();
            GraphContextManager.getGraphContext(getGraph()).setChildrenShowing(isChildrenShowing());
            builder.updateView();
        }

        @Override protected String getText(@NotNull Graph2D graph2D) {
            return isChildrenShowing() ? "Hide All Attributes" : "Show all Attributes";
        }
    }

    private static class ShowChildrenAction extends AbstractShowChildrenAction {
        public ShowChildrenAction(Graph2D graph, GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
            super(graph, builder, MMFileType.ATTRIBUTE_ICON);
        }

        @Override public void update(AnActionEvent anActionEvent) {
            super.update(anActionEvent);
            anActionEvent.getPresentation().setEnabled(!getGraph().isSelectionEmpty());
            if (GraphContextManager.getGraphContext(getGraph()).getLastNodeSelected() != null)
                setChildrenShowing(GraphContextManager.getGraphContext(getGraph()).getLastNodeSelected().isShowChildren());
        }

        @Override protected void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent) {
            final MMGraphNode<?> node = GraphContextManager.getGraphContext(graph2D).getLastNodeSelected();
            if (node == null) return;
            if (isChildrenShowing()) node.setShowChildren(false);
            else node.setShowChildren(true);
            builder.updateView();
        }

        @Override protected String getText(@NotNull Graph2D graph2D) {
            return isChildrenShowing() ? "Hide Attributes For Node" : "Show Attributes For Node";
        }
    }

    private static class ShowFormsAction extends AbstractGraphToggleAction {
        private final GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder;

        public ShowFormsAction(Graph2D graph, GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
            super(graph, MMFileType.FORM_ICON);
            this.builder = builder;
        }

        @Override protected boolean isSelected(Graph2D graph2D, Project project, AnActionEvent anActionEvent) {
            return GraphContextManager.getGraphContext(getGraph(anActionEvent)).isFormsShowing();
        }

        @Override protected void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent) {
            final MMGraphContext graphContext = GraphContextManager.getGraphContext(getGraph(anActionEvent));
            graphContext.setFormsShowing(!graphContext.isFormsShowing());
            // ((EntityRelationDataModel)builder.getGraphDataModel()).refreshEdges();
            // MMPresentationModel.i = 0;
            builder.updateView();
        }

        @Override protected String getText(@NotNull Graph2D graph2D) {
            return GraphContextManager.getGraphContext(graph2D).isFormsShowing() ? "Hide Forms " : "Show Forms";
        }
    }

    private static class ShowGeneratedSourcesAction extends AbstractGraphToggleAction {
        private final GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder;

        public ShowGeneratedSourcesAction(Graph2D graph, GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
            super(graph, PlatformIcons.CLASS_ICON);
            this.builder = builder;
        }

        @Override protected boolean isSelected(Graph2D graph2D, Project project, AnActionEvent anActionEvent) {
            return GraphContextManager.getGraphContext(getGraph(anActionEvent)).isGeneratedSourcesShowing();
        }

        @Override protected void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent) {
            final MMGraphContext graphContext = GraphContextManager.getGraphContext(getGraph(anActionEvent));
            graphContext.setGeneratedSourcesShowing(!graphContext.isGeneratedSourcesShowing());
            builder.updateView();
        }

        @Override protected String getText(@NotNull Graph2D graph2D) {
            return GraphContextManager.getGraphContext(graph2D).isGeneratedSourcesShowing() ? "Hide Generated Sources" : "Show Generated Sources";
        }
    }
}  // end class ActionFactory
