
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
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.lang.mm.psi.PsiDatabaseObject;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.SearchField;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.psi.PsiUtils.findParentModel;
import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;
import static tekgenesis.mmcompiler.ast.MMToken.SEARCHABLE;

/**
 * Action to expand a default Searchable.
 */
public class DefaultSearchableExpansionAction<T extends DbObject> extends MMActionTree implements Runnable, UndoableAction {

    //~ Instance Fields ..............................................................................................................................

    private Option<T> dbObject = null;

    private Document document = null;
    private String   newText  = "";
    private int      offset;
    private String   oldText  = "";

    //~ Constructors .................................................................................................................................

    /** Action to expand a default Searchable in a DbObject. */
    public DefaultSearchableExpansionAction() {
        final Presentation presentation = getTemplatePresentation();
        presentation.setDescription(DEFAULT_SEARCHABLE_EXPANSION);
        presentation.setText(DEFAULT_SEARCHABLE_EXPANSION);
        dbObject = Option.empty();
    }
    /** Action to expand a default searchable in a DbObject. */
    public DefaultSearchableExpansionAction(Editor editor, Project project) {
        this.editor  = editor;
        this.project = project;

        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent event) {
        editor  = getEditor(event.getDataContext());
        project = event.getProject();
        CommandProcessor.getInstance().executeCommand(event.getProject(), this, SEARCHABLE_EXPANSION, null);
    }

    @Override public void redo()
        throws UnexpectedUndoException {}

    @Override public void run() {
        if (editor != null) {
            final PsiFile psiFileInEditor = getPsiFileInEditor(editor, project);

            if (psiFileInEditor != null) {
                final String generated = generateSearchable(psiFileInEditor.getName());
                if (generated.isEmpty()) return;

                document = editor.getDocument();
                final PsiDatabaseObject<T> caretPsiDbObject = getCaretPsiDbObject(editor, file);
                assert caretPsiDbObject != null : "db object shouldn't be null";
                final TreeElement searchable = caretPsiDbObject.getHighLevelOption(SEARCHABLE);
                assert searchable != null : "searchable shouldn't be null";

                offset = searchable.getStartOffset();

                newText = addTabs(generated);

                oldText = document.getText(new TextRange(offset, offset + searchable.getTextLength()));

                ApplicationManager.getApplication().runWriteAction(() ->
                        editor.getDocument()
                              .replaceString(offset, offset + searchable.getTextLength(), "searchable by { " + newText + "}"));
            }
        }
    }

    @Override public void undo()
        throws UnexpectedUndoException
    {
        ApplicationManager.getApplication().runWriteAction(() -> document.replaceString(offset, offset + newText.length(), oldText));
    }

    @Override public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override public boolean isGlobal() {
        return false;
    }

    @Override protected boolean hasValidContext() {
        return false;
    }

    @Override protected void setupFromEditor(Project project, Editor editor) {
        file = getPsiFileInEditor(editor, project);

        if (file != null) {
            final PsiDatabaseObject<T> element = getCaretPsiDbObject(editor, file);
            dbObject = element != null ? element.getModel() : Option.empty();
        }
    }

    @Override protected void setupFromTree(DataContext context) {}

    private String addTabs(String oldString) {
        final int    tabsInitialOffset = editor.offsetToLogicalPosition(offset).column / editor.getSettings().getTabSize(project);
        final String tabs              = '\n' + Strings.nChars('\t', tabsInitialOffset + 1);

        final StringTokenizer tokenizer = new StringTokenizer(oldString);
        String                newString = "";

        while (tokenizer.hasMoreElements()) {
            String text = tokenizer.nextToken("\n").trim();
            text      =  tabs + text;
            newString += text;
        }

        newString += "\n" + Strings.nChars('\t', tabsInitialOffset);

        return newString;
    }

    @NotNull private String generateSearchable(String name) {
        return dbObject.map(dbObj -> {
                if (dbObj.isSearchable() && !dbObj.searchByFields().isEmpty())
                    return dbObj.searchByFields().map(SearchField::getField).mkString("", ";\n", ";\n");
                return "";
            }).orElse("");
    }

    @Nullable private PsiDatabaseObject<T> getCaretPsiDbObject(@NotNull final Editor e, @NotNull final PsiFile psiFile) {
        offset = e.getCaretModel().getOffset();
        final PsiElement element = psiFile.findElementAt(offset);
        return element != null ? getContainingPsiDbOject(element).getOrNull() : null;
    }

    @NotNull private Option<PsiDatabaseObject<T>> getContainingPsiDbOject(@NotNull final PsiElement element) {
        return cast(findParentModel(element));
    }

    //~ Static Fields ................................................................................................................................

    public static final String SEARCHABLE_EXPANSION = "SearchableExpansion";

    private static final String DEFAULT_SEARCHABLE_EXPANSION = "Default Searchable expansion";
}  // end class DefaultSearchableExpansionAction
// end class DefaultFormExpansionAction
