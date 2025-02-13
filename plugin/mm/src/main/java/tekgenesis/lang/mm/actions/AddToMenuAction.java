
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListSeparator;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.actions.addToMenuAction.ExistingMenu;
import tekgenesis.lang.mm.actions.addToMenuAction.MenuOption;
import tekgenesis.lang.mm.actions.addToMenuAction.NonExistingMenu;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiLink;
import tekgenesis.lang.mm.psi.PsiMenu;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.repository.ModelRepository;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Add Form/Menu To Menu Action.
 */
public class AddToMenuAction extends MMActionTree implements Runnable {

    //~ Instance Fields ..............................................................................................................................

    private PsiElement      clickedElement = null;
    private PsiMetaModel<?> clickedModel   = null;
    private Seq<MenuOption> menuSeq        = Colls.emptyIterable();

    //~ Constructors .................................................................................................................................

    /** Action to add a form/menu to a selected menu. */
    public AddToMenuAction() {}

    /** Action to add a form/menu to a selected menu. */
    public AddToMenuAction(Project project, Editor editor) {
        this.project = project;
        this.editor  = editor;
        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        setupMenusToShow();
        CommandProcessor.getInstance().executeCommand(e.getProject(), this, ADD_TO_MENU_ACTION, null);
    }

    @Override public boolean hasValidContext() {
        return clickedElement != null &&
               (clickedElement instanceof PsiLink || clickedElement instanceof PsiMenu || clickedElement instanceof PsiForm || checkParents());
    }

    @Override public void run() {
        setupMenusToShow();
        final BaseListPopupStep<MenuOption> step = createPopUp();
        if (editor != null) JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(editor);
        else JBPopupFactory.getInstance().createListPopup(step).showInFocusCenter();
    }

    @Override protected void setupFromEditor(Project p, Editor e) {
        file = getPsiFileInEditor(e, p);
        final int offset1 = e.getCaretModel().getOffset();
        if (file != null) clickedElement = file.findElementAt(offset1);
    }

    @Override protected void setupFromTree(DataContext context) {
        final Navigatable data = PlatformDataKeys.NAVIGATABLE.getData(context);
        if (data instanceof PsiElement) {
            clickedElement = (PsiElement) data;
            file           = clickedElement.getContainingFile();
        }
    }

    private boolean checkParents() {
        clickedElement.getParent();
        while (clickedElement != null) {
            if (clickedElement instanceof PsiMenu || clickedElement instanceof PsiForm || clickedElement instanceof PsiLink) return true;
            else clickedElement = clickedElement.getParent();
        }
        return false;
    }

    private BaseListPopupStep<MenuOption> createPopUp() {
        final ImmutableList<MenuOption> menusList = menuSeq.toList();
        if (menuSeq.isEmpty()) return new BaseListPopupStep<>("No Menus Found", menusList);

        return new BaseListPopupStep<MenuOption>("Choose a Menu", menusList) {
            @NotNull @Override public String getTextFor(MenuOption menu) {
                return menu.getName();
            }

            @Override public PopupStep<?> onChosen(final MenuOption selectedValue, boolean finalChoice) {
                if (selectedValue == null) return FINAL_CHOICE;

                if (finalChoice) selectedValue.onChosen(clickedElement, project, editor);
                return FINAL_CHOICE;
            }

            @Nullable @Override public ListSeparator getSeparatorAbove(MenuOption value) {
                if (value instanceof NonExistingMenu) return new ListSeparator();
                else return null;
            }
        };
    }  // end method createPopUp

    private void setupMenusToShow() {
        checkParents();
        clickedModel = (PsiMetaModel<?>) clickedElement;
        final ModelRepository modelRepository = clickedModel.getModelRepository();
        final Seq<Menu>       models          = modelRepository.getModels(clickedModel.getDomain(), Menu.class);

        menuSeq = models.filter(menu -> menu != null && !(menu.getName().equals(clickedModel.getName())))
                  .map(ExistingMenu::createExisting)
                  .append(new NonExistingMenu(MSGS.createMenuAnnotation()));
    }

    //~ Static Fields ................................................................................................................................

    public static final String ADD_TO_MENU_ACTION = "AddToMenuAction";
}  // end class AddToMenuAction
