
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.errors;

import java.util.List;

import com.intellij.ide.errorTreeView.NewErrorTreeViewPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.MessageCategory;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;

/**
 * Tree view for Displaying COmpiler Errors in plugin.
 */
public class MMErrorTreeView extends NewErrorTreeViewPanel {

    //~ Constructors .................................................................................................................................

    /** View for displaying compiler errors in plugin. */
    public MMErrorTreeView(Project project, String helpId) {
        super(project, helpId);
    }

    //~ Methods ......................................................................................................................................

    /** adds error messages. */
    public void addErrorMessages(List<Tuple<MetaModelAST, BuilderError>> errors) {
        for (final Tuple<MetaModelAST, BuilderError> error : errors) {
            final BuilderError problem = error.second();
            final MetaModelAST node    = error.first();

            if (!node.isEmpty()) {
                final PsiElement element = node instanceof PsiElement ? (PsiElement) node : null;

                if (node instanceof Navigatable)
                    addMessage(MessageCategory.ERROR,
                        new String[] { problem.getMessage() },
                        null,
                        (Navigatable) node,
                        null,
                        null,
                        getVirtualFileFromPsiElement(element));
                else addMessage(MessageCategory.ERROR, new String[] { problem.getMessage() }, getVirtualFileFromPsiElement(element), -1, -1, null);
            }
        }
    }

    @Nullable private VirtualFile getVirtualFileFromPsiElement(@Nullable PsiElement element) {
        if (element != null) {
            final PsiFile file = element.getContainingFile();
            return file != null ? file.getVirtualFile() : null;
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4417125291083473660L;
}  // end class MMErrorTreeView
