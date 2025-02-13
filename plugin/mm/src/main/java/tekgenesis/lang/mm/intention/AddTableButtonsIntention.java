
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.actions.AddButtonsToStaticTableAction;

import static tekgenesis.lang.mm.actions.AddButtonsToStaticTableAction.TABLE_ADD_BUTTONS;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Intention to generate add and remove buttons for a static table.
 */
public class AddTableButtonsIntention implements IntentionAction {

    //~ Methods ......................................................................................................................................

    @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
        throws IncorrectOperationException
    {
        CommandProcessor.getInstance().executeCommand(project, new AddButtonsToStaticTableAction(editor, project), TABLE_ADD_BUTTONS, null);
    }

    @Override public boolean startInWriteAction() {
        return false;
    }

    @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return new AddButtonsToStaticTableAction(editor, project).hasValidContext();
    }

    @NotNull @Override public String getFamilyName() {
        return getText();
    }

    @NotNull @Override public String getText() {
        return MSGS.buttonsForTableAnnotation();
    }
}
