
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi.structure.projectStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.mmcompiler.ast.MetaModelAST;

@SuppressWarnings("rawtypes")
class MMProjectStructureViewProvider implements TreeStructureProvider {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent, @NotNull Collection<AbstractTreeNode> children,
                                                                  ViewSettings settings) {
        final List<AbstractTreeNode> result = new ArrayList<>();

        for (final AbstractTreeNode child : children) {
            final Object childValue = child.getValue();

            if (childValue instanceof MMFile) {
                final MMFile       file    = (MMFile) childValue;
                final MetaModelAST fileAst = file.getFirstRoot();

                if (fileAst.children().size() <= 2 && hasNameOfFile(file, fileAst.getChild(1)))
                    result.add(getTreeNode(file, fileAst.getChild(1), settings));
                else result.add(new MMFileTreeNode(file, settings));
            }
            else result.add(child);
        }
        return result;
    }

    @Override public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }

    //~ Methods ......................................................................................................................................

    private static boolean hasNameOfFile(MMFile file, MetaModelAST mm) {
        final VirtualFile virtualFile = file == null ? null : file.getVirtualFile();
        if (virtualFile == null) return true;
        else {
            final String fileName = virtualFile.getNameWithoutExtension();
            if (mm instanceof MMCommonComposite) {
                final String modelName = ((MMCommonComposite) mm).getName();
                return fileName.equals(modelName);
            }
            return false;
        }
    }

    private static AbstractPsiBasedNode<? extends MMCommonComposite> getTreeNode(MMFile father, MetaModelAST model, ViewSettings settings) {
        if (model instanceof PsiMetaModel) return new MetaModelTreeNode(father.getProject(), (PsiMetaModel) model, settings);
        return null;
    }

    //~ Inner Classes ................................................................................................................................

    private static class MetaModelTreeNode extends AbstractPsiBasedNode<PsiMetaModel<?>> {
        MetaModelTreeNode(Project project, PsiMetaModel<?> t, ViewSettings viewSettings) {
            super(project, t, viewSettings);
        }

        @Override protected PsiElement extractPsiFromValue() {
            return getValue();
        }

        @Override protected void updateImpl(PresentationData data) {
            data.setPresentableText(getValue().getName());
        }

        @Override protected Collection<AbstractTreeNode> getChildrenImpl() {
            return Colls.emptyList();
        }
    }

    private static class MMFileTreeNode extends PsiFileNode {
        MMFileTreeNode(MMFile mmFile, ViewSettings settings) {
            super(mmFile.getProject(), mmFile, settings);
        }

        @Override public Collection<AbstractTreeNode> getChildrenImpl() {
            final ViewSettings           settings = getSettings();
            final List<AbstractTreeNode> result   = new ArrayList<>();
            final MMFile                 father   = (MMFile) getValue();
            // if (!father.isScriptFile(true)) {
            for (final MetaModelAST model : father.getFirstRoot()) {
                final AbstractPsiBasedNode<? extends MMCommonComposite> child = getTreeNode(father, model, settings);
                if (child != null) result.add(child);
            }
            // }
            return result;
        }
        protected void updateImpl(PresentationData data) {
            super.updateImpl(data);
            data.setPresentableText(getValue().getName().split("\\." + Constants.META_MODEL_EXT)[0]);
        }
    }
}  // end class MMProjectStructureViewProvider
