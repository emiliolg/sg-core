
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.utils;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.GraphBuilderFactory;
import com.intellij.openapi.graph.layout.LayoutOrientation;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicLayouter;
import com.intellij.openapi.graph.settings.GraphSettingsProvider;
import com.intellij.openapi.graph.view.Drawable;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMProjectComponent;
import tekgenesis.lang.mm.graph.graphUltimate.actions.ActionFactory;
import tekgenesis.lang.mm.graph.graphUltimate.model.*;
import tekgenesis.lang.mm.graph.graphUltimate.ui.GraphContextManager;
import tekgenesis.lang.mm.graph.graphUltimate.ui.MMPresentationModel;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiEntity;

import static tekgenesis.lang.mm.psi.PsiUtils.getModuleOrFail;

/**
 * Utils for Graphs.
 */
public class UltimateGraphUtils {

    //~ Constructors .................................................................................................................................

    private UltimateGraphUtils() {}

    //~ Methods ......................................................................................................................................

    /** Builds Module Graph and displays it in window. */
    public static Content createModuleGraphAndShow(MMFile file, AnActionEvent e) {
        final Graph2D graph = GraphManager.getGraphManager().createGraph2D();

        final MMPresentationModel presentation = new MMPresentationModel(graph);

        final EntityRelationDataModel model = new EntityRelationDataModel(getModuleOrFail(file.getNavigationElement()));
        final Graph2DView             view  = GraphManager.getGraphManager().createGraph2DView();
        return createGraphAndShow(getModuleOrFail(file).getName(), e.getProject(), graph, presentation, model, view);
    }
    /** Builds Selected Graph and displays it in window. */
    public static Content createSelectedGraph(PsiEntity selectedEntity, PsiFile file, @Nullable Project project) {
        final Graph2D                graph        = GraphManager.getGraphManager().createGraph2D();
        final MMPresentationModel    presentation = new MMPresentationModel(graph);
        final MMGraphSimpleDataModel model        = new MMGraphSimpleDataModel(selectedEntity,
                (MMFile) file,
                getModuleOrFail(file.getNavigationElement()));
        final Graph2DView            view         = GraphManager.getGraphManager().createGraph2DView();

        GraphContextManager.getGraphContext(graph).setFormsShowing(true);

        return createGraphAndShow(selectedEntity.getName(), project, graph, presentation, model, view);
    }

    /** Applies Default Hierarchical Layout. */
    public static void layout(Graph2D graph, Project project) {
        final Layouter layouter = GraphSettingsProvider.getInstance(project).getSettings(graph).getCurrentLayouter();
        layout(graph, layouter);
    }

    /** Applies Layout and updates view. */
    public static void layout(Graph2D graph, Layouter layouter) {
        layouter.doLayout(graph);
        graph.updateViews();
    }

    /** Sets layoutOrientation for window. */
    public static void setLayoutOrientation(Layouter layouter, boolean horizontal) {
        if (layouter instanceof HierarchicLayouter)
            ((HierarchicLayouter) layouter).setLayoutOrientation(horizontal ? LayoutOrientation.LEFT_TO_RIGHT : LayoutOrientation.TOP_TO_BOTTOM);
    }

    /** Build Graph and displays it in window. */
    private static Content createGraphAndShow(String title, @Nullable Project project, Graph2D graph, MMPresentationModel presentation,
                                              MMGraphDataModel model, final Graph2DView view) {
        assert project != null;
        final GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder = GraphBuilderFactory.getInstance(project)
                                                                           .createGraphBuilder(graph, view, model, presentation);

        builder.getView().addBackgroundDrawable(new TekLogoAndBackGround(view));
        builder.initialize();
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createToolbarPanel(builder), BorderLayout.NORTH);
        final JComponent jComponent = builder.getView().getJComponent();
        panel.add(jComponent, BorderLayout.CENTER);
        final Content    content = ContentFactory.SERVICE.getInstance().createContent(panel, title, true);
        final ToolWindow window  = ToolWindowManager.getInstance(project).getToolWindow(MMProjectComponent.MM_GRAPH);
        window.getContentManager().addContent(content);
        window.setAvailable(true, null);
        Disposer.register(content, builder);
        window.getContentManager().setSelectedContent(content);
        window.show(null);
        return content;
    }

    /** creates Default toolbarPanel. */
    private static JComponent createToolbarPanel(GraphBuilder<MMGraphNode<?>, Edge<MMGraphNode<?>>> builder) {
        final DefaultActionGroup actions       = ActionFactory.createActions(builder);
        final ActionToolbar      actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actions,


                true);
        final JComponent         component     = actionToolbar.getComponent();
        component.setBackground(SystemColor.window);
        return component;
    }

    //~ Static Fields ................................................................................................................................

    public static final String TEKGENESIS_LANG_MM_LOGO_TEK_GENESIS_MINI_PNG = "/tekgenesis/lang/mm/logo-TekGenesis-mini.png";
    private static final Image TEKGEN_ICON                                  = Toolkit.getDefaultToolkit()
                                                                              .getImage(
            UltimateGraphUtils.class.getResource(TEKGENESIS_LANG_MM_LOGO_TEK_GENESIS_MINI_PNG));

    //~ Inner Classes ................................................................................................................................

    private static class TekLogoAndBackGround implements Drawable {
        private final Graph2DView view;

        private TekLogoAndBackGround(Graph2DView view) {
            this.view = view;
        }

        @Override public void paint(Graphics2D graphics2D) {
            graphics2D.setColor(SystemColor.window);
            final Rectangle bounds = getBounds();
            graphics2D.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            final int w = (int) (TEKGEN_ICON.getWidth(null) / view.getZoom());
            final int h = (int) (TEKGEN_ICON.getHeight(null) / view.getZoom());
            final int x = bounds.x + bounds.width - (int) (TEKGEN_ICON.getWidth(null) / view.getZoom());
            final int y = bounds.y + bounds.height - (int) (TEKGEN_ICON.getHeight(null) / view.getZoom());
            graphics2D.drawImage(TEKGEN_ICON, x, y, w, h, null);
        }
        @Override public Rectangle getBounds() {
            return view.getVisibleRect();
        }
    }
}  // end class UltimateGraphUtils
