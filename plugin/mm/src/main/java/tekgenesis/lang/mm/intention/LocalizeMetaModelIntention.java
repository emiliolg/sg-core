
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.siyeh.ipp.base.PsiElementPredicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.actions.LocalizeMetaModelAction;
import tekgenesis.lang.mm.i18n.PluginMessages;

import static com.intellij.openapi.command.CommandProcessor.getInstance;

import static tekgenesis.lang.mm.MMElementType.ENUM;
import static tekgenesis.lang.mm.MMElementType.FORM;
import static tekgenesis.lang.mm.MMElementType.LINK;
import static tekgenesis.lang.mm.MMElementType.MENU;
import static tekgenesis.lang.mm.MMElementType.WIDGET_DEF;
import static tekgenesis.lang.mm.actions.LocalizeMetaModelAction.LOCALIZE_META_MODEL;

/**
 * Intention to localize a form, menu or enum.
 */
public class LocalizeMetaModelIntention extends Intention {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public LocalizeMetaModelIntention() {
        setText(PluginMessages.MSGS.localizeMetaModelAnnotation());
    }

    //~ Methods ......................................................................................................................................

    @Override protected void processIntention(@NotNull PsiElement element) {}

    @Override protected void processIntention(Editor editor, @NotNull PsiElement element) {
        final Project project = element.getProject();
        getInstance().executeCommand(project, new LocalizeMetaModelAction(project, editor), LOCALIZE_META_MODEL, null);
    }

    @NotNull @Override PsiElementPredicate getElementPredicate() {
        return LOCALIZE_PREDICATE;
    }

    //~ Static Fields ................................................................................................................................

    public static final PsiElementPredicate LOCALIZE_PREDICATE = new MetaModelNamePredicate(FORM, WIDGET_DEF, MENU, ENUM, LINK);
}
