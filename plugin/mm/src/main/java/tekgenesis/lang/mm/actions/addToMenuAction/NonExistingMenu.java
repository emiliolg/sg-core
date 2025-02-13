
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.addToMenuAction;

import java.awt.*;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.gui.InputTextDialog;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiUtils;

/**
 * Represents a not existing menu option with the name to be displayed only.
 */
public class NonExistingMenu extends MenuOption {

    //~ Constructors .................................................................................................................................

    /** Constructs a non existing menu. */
    public NonExistingMenu(String menuName) {
        super(menuName);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onChosen(PsiElement clickedElement, Project project, Editor editor) {
        final InputTextDialog inputTextDialog = new NewMenuInputDialog(project, editor, clickedElement, "New Menu", "Enter new Menu name");
        EventQueue.invokeLater(inputTextDialog::show);
    }

    //~ Inner Classes ................................................................................................................................

    class NewMenuInputDialog extends InputTextDialog {
        private final PsiElement clickedElement;
        private final Editor     editor;
        private final Project    project;

        protected NewMenuInputDialog(@Nullable Project project, Editor editor, PsiElement clickedElement, String title, String myLabel) {
            super(project, title, myLabel);
            this.editor         = editor;
            this.clickedElement = clickedElement;
            this.project        = project;
        }

        @Override protected void doOKAction() {
            final PsiMetaModel<?> clickedModel = (PsiMetaModel<?>) clickedElement;
            final int             offset       = clickedModel.isInner() ? PsiUtils.findParentModel(clickedModel).get().getTextRange().getEndOffset()
                                                                        : clickedModel.getTextRange().getEndOffset();
            final Document        document     = editor.getDocument();

            final String item = clickedModel.getName() + ";";

            final String text = "\n" +
                                "\n" +
                                "menu " + getText() + " {\n" +
                                "\t" + item + "\n" +
                                "}";

            WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(offset, text));
            super.doOKAction();
        }
    }
}  // end class NonExistingMenu
