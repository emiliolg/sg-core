
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.content.Content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiEntity;
import tekgenesis.repository.ModelRepository;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.lang.mm.graph.GraphUtils.isUltimate;
import static tekgenesis.lang.mm.graph.graphUltimate.utils.UltimateGraphUtils.createSelectedGraph;
import static tekgenesis.lang.mm.psi.PsiUtils.getModelRepository;

/**
 * Creates MMGraph for a certain entity.
 */
public class SelectedMMGraphAction extends MMActionTree implements Runnable {

    //~ Instance Fields ..............................................................................................................................

    private Content content = null;

    private ModelRepository modelRepository = null;
    private PsiEntity       selectedEntity  = null;

    //~ Constructors .................................................................................................................................

    /** Action to show an MM graph of a given entity. */
    public SelectedMMGraphAction() {}

    /** Action to show an MM graph of a given entity. */
    public SelectedMMGraphAction(Editor editor, Project project, PsiFile file) {
        this.file    = file;
        this.editor  = editor;
        this.project = project;
        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    public void actionPerformed(@NotNull AnActionEvent e) {
        if (file != null && modelRepository != null && selectedEntity != null) content = createSelectedGraph(selectedEntity, file, e.getProject());
    }

    /**
     * Dispose graph content. Used for testing, where you can't control when to hide and dispose
     * window
     */
    public void disposeContent() {
        if (content != null) Disposer.dispose(content);
    }

    @Override public boolean hasValidContext() {
        return selectedEntity != null && !selectedEntity.isEmpty() && modelRepository != null && isUltimate();
    }

    @Override public void run() {
        if (file != null && modelRepository != null && selectedEntity != null) content = createSelectedGraph(selectedEntity, file, project);
    }
    /** setup Method used in Test. */
    public void setupFromTest(MMFile mmFile, PsiEntity entity) {
        selectedEntity  = entity;
        file            = mmFile;
        modelRepository = mmFile.getModelRepository();
    }
    public void update(@NotNull AnActionEvent event) {
        final String title = isUltimate() ? "MMGraph For Entity" : "MMGraph For Entity(Only In Ultimate)";
        event.getPresentation().setText(title);
        super.update(event);
    }
    @Override protected void setupFromEditor(Project p, Editor e) {
        file = getPsiFileInEditor(e, p);
        if (file != null) {
            setSelectedEntity(e, file);
            if (selectedEntity != null) modelRepository = getModelRepository(selectedEntity).get();
        }
    }

    @Override protected void setupFromTree(DataContext context) {
        final Navigatable data = PlatformDataKeys.NAVIGATABLE.getData(context);

        if (data instanceof StructureViewTreeElement) {
            final Object value = ((StructureViewTreeElement) data).getValue();

            if (value instanceof PsiElement) {
                final PsiElement psiElement = (PsiElement) value;
                setSelectedEntity(psiElement);
                if (selectedEntity != null) modelRepository = getModelRepository(selectedEntity).get();
            }
        }
    }

    private void setSelectedEntity(@Nullable PsiElement element) {
        PsiElement parent = element != null ? element.getParent() : null;
        selectedEntity = null;
        while (parent != null) {
            if (parent instanceof PsiEntity) {
                selectedEntity = (PsiEntity) parent;
                break;
            }
            parent = parent.getParent();
        }
    }
    private void setSelectedEntity(@NotNull final Editor editor1, @NotNull final PsiFile psiFile) {
        final int        offset  = editor1.getCaretModel().getOffset();
        final PsiElement element = psiFile.findElementAt(offset);
        setSelectedEntity(element);
    }

    //~ Static Fields ................................................................................................................................

    public static final String MM_GRAPH_ENTITY = "MMGraphEntity";
}  // end class SelectedMMGraphAction
