
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.addToMenuAction;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMenu;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.metadata.menu.Menu;

import static java.lang.String.format;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

/**
 * Represents an existing menu with name and source name only.
 */
public class ExistingMenu extends MenuOption {

    //~ Instance Fields ..............................................................................................................................

    private final String sourceName;

    //~ Constructors .................................................................................................................................

    private ExistingMenu(String menuName, String sourceName) {
        super(menuName);
        this.sourceName = sourceName;
    }

    //~ Methods ......................................................................................................................................

    @Override public void onChosen(PsiElement clickedElement, Project project, Editor editor) {
        final Option<MMFile>  mmFile       = PsiUtils.findMMFile(clickedElement.getProject(), sourceName);
        final PsiMetaModel<?> clickedModel = (PsiMetaModel<?>) clickedElement;
        if (mmFile.isEmpty()) return;

        final PsiMenu menu = mmFile.get().getMenu(name);
        if (menu == null) return;

        PsiUtils.scrollTo(menu);
        final Document document = PsiDocumentManager.getInstance(project).getDocument(mmFile.get());
        if (document == null) return;

        final int offset = menu.getTextRange().getEndOffset() - 1;

        runWriteCommandAction(project, () -> document.insertString(offset, format("\t%s;\n", clickedModel.getName())));
    }

    //~ Methods ......................................................................................................................................

    /** Constructs an existing menu. */
    @NotNull public static MenuOption createExisting(final Menu menu) {
        return new ExistingMenu(menu.getName(), menu.getSourceName());
    }
}  // end class ExistingMenu
