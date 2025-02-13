
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.siyeh.ipp.base.PsiElementPredicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.actions.AddToMenuAction;
import tekgenesis.lang.mm.i18n.PluginMessages;

import static tekgenesis.lang.mm.MMElementType.FORM;
import static tekgenesis.lang.mm.MMElementType.LINK;
import static tekgenesis.lang.mm.MMElementType.MENU;
import static tekgenesis.lang.mm.actions.AddToMenuAction.ADD_TO_MENU_ACTION;

/**
 * Intention to Add Form/Menu To Menu Action.
 */
public class AddToMenuIntention extends Intention {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public AddToMenuIntention() {
        setText(PluginMessages.MSGS.addToMenuAnnotation());
    }

    //~ Methods ......................................................................................................................................

    @Override protected void processIntention(@NotNull PsiElement element) {}

    @Override protected void processIntention(Editor editor, @NotNull PsiElement element) {
        final Project project = element.getProject();
        CommandProcessor.getInstance().executeCommand(project, new AddToMenuAction(project, editor), ADD_TO_MENU_ACTION, null);
    }

    @NotNull @Override PsiElementPredicate getElementPredicate() {
        return ADD_TO_MENU_PREDICATE;
    }

    //~ Static Fields ................................................................................................................................

    public static final PsiElementPredicate ADD_TO_MENU_PREDICATE = new MetaModelNamePredicate(FORM, MENU, LINK);
}
