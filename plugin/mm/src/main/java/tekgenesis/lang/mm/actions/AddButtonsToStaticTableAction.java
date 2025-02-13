
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.util.StringTokenizer;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.lang.mm.psi.PsiWidgetType;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.lang.mm.psi.PsiUtils.findParentField;
import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;

/**
 * Action to generate buttons in selected static table.
 */
public class AddButtonsToStaticTableAction extends MMActionTree implements Runnable, UndoableAction {

    //~ Instance Fields ..............................................................................................................................

    private PsiElement   clickedElement = null;
    private String       newText        = "";
    private int          offset;
    private final String oldText        = "";

    //~ Constructors .................................................................................................................................

    /** Action to generate add and remove buttons in a selected static table. */
    public AddButtonsToStaticTableAction() {}

    /** Action to generate add and remove buttons in a selected static table. */
    public AddButtonsToStaticTableAction(Editor editor, Project project) {
        this.editor  = editor;
        this.project = project;
        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent event) {
        editor  = getEditor(event.getDataContext());
        project = event.getProject();
        CommandProcessor.getInstance().executeCommand(event.getProject(), this, TABLE_ADD_BUTTONS, null);
    }

    @Override public boolean hasValidContext() {
        return clickedElement instanceof PsiWidget && ((PsiWidget) clickedElement).isTable() && ((PsiWidget) clickedElement).shouldAppearIntention();
    }

    @Override public void redo()
        throws UnexpectedUndoException {}

    @Override public void run() {
        if (editor == null) return;

        final PsiWidget tableField = (PsiWidget) clickedElement;
        offset = tableField.getStartOffset() + tableField.getChars().length();

        final Seq<PsiWidget> modelWidgets = tableField.getUiModel().getWidgets();
        final Seq<PsiWidget> tableWidgets = modelWidgets.filter(formField -> formField != null && formField.isTable());

        final Seq<PsiWidgetType> tableButtons = tableButtonsFromPsiUiModelWidgets(modelWidgets);

        // if only one table is in the form, it can have not parametrized buttons
        if (tableWidgets.size() == 1) {
            if (tableField.hasButton(tableButtons, ADD_ROW) && tableField.hasButton(tableButtons, REMOVE_ROW)) return;
            else newText = buttonsForTable();
        }
        else {
            if (tableField.hasParametrizedButton(tableButtons, ADD_ROW, tableField.getName()) &&
                tableField.hasParametrizedButton(tableButtons, REMOVE_ROW, tableField.getName())) return;
            else newText = buttonsForTable(tableField.getName());
        }

        if (isNotEmpty(newText)) ApplicationManager.getApplication().runWriteAction(() -> editor.getDocument().insertString(offset, newText));
    }

    @Override public void undo()
        throws UnexpectedUndoException
    {
        //
        ApplicationManager.getApplication().runWriteAction(() -> editor.getDocument().replaceString(offset, offset + newText.length(), oldText));
    }

    @Nullable @Override public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override public boolean isGlobal() {
        return false;
    }

    @Override protected void setupFromEditor(Project p, Editor e) {
        file = getPsiFileInEditor(e, p);

        if (file != null) clickedElement = getCaretPsiElement(e, file);
    }

    @Override protected void setupFromTree(DataContext context) {
        final Navigatable data = PlatformDataKeys.NAVIGATABLE.getData(context);
        if (data instanceof PsiElement) {
            clickedElement = getContainingPsiElement((PsiElement) data).getOrNull();
            if (clickedElement != null) file = clickedElement.getContainingFile();
        }
    }

    private String addTabs(String textToTab, int tabsInitialOffset) {
        final StringTokenizer tokenizer  = new StringTokenizer(textToTab);
        String                tabbedText = "";
        while (tokenizer.hasMoreElements()) {
            final String text = tokenizer.nextToken("\n");
            tabbedText += "\n" + Strings.nChars('\t', tabsInitialOffset) + text;
        }
        return tabbedText;
    }

    @NotNull private String buttonsForTable() {
        final int tabs = editor.offsetToLogicalPosition(offset).column / editor.getSettings().getTabSize(project);
        return addTabs("horizontal {\n\tbutton(add_row);\n\tbutton(remove_row);\n};", tabs);
    }

    @NotNull private String buttonsForTable(String tableName) {
        final int tabs = editor.offsetToLogicalPosition(offset).column / editor.getSettings().getTabSize(project);
        return addTabs(String.format("horizontal {\n\tbutton(add_row, %s);\n\tbutton(remove_row, %s);\n};", tableName, tableName), tabs);
    }

    @NotNull private Seq<PsiWidgetType> tableButtonsFromPsiUiModelWidgets(Seq<PsiWidget> widgets) {
        return widgets.filter(PsiWidget::isAddOrRemoveButton).map(PsiWidget::findWidgetType);
    }

    @Nullable private PsiElement getCaretPsiElement(@NotNull final Editor e, @NotNull final PsiFile psiFile) {
        offset = e.getCaretModel().getOffset();
        final PsiElement element = psiFile.findElementAt(offset);
        return element != null ? getContainingPsiElement(element).getOrNull() : null;
    }

    @NotNull private Option<PsiElement> getContainingPsiElement(@NotNull final PsiElement element) {
        return findParentField(element).castTo(PsiElement.class);
    }

    //~ Static Fields ................................................................................................................................

    public static final String ADD_ROW    = "add_row";
    public static final String REMOVE_ROW = "remove_row";

    public static final String TABLE_ADD_BUTTONS = "AddButtonsToStaticTable";
}  // end class AddButtonsToStaticTableAction
