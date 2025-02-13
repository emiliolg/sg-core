
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.surround;

import java.util.Arrays;
import java.util.StringTokenizer;

import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Strings;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.lang.mm.surround.MMFormFieldSurroundDescriptor.EMPTY_ARRAY;

/**
 * Form field surrounder.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MMFormFieldSurrounder implements Surrounder {

    //~ Instance Fields ..............................................................................................................................

    private final WidgetType group;

    //~ Constructors .................................................................................................................................

    MMFormFieldSurrounder(WidgetType group) {
        this.group = group;
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public TextRange surroundElements(@NotNull Project project, @NotNull final Editor editor, @NotNull PsiElement[] elements)
        throws IncorrectOperationException
    {
        final PsiElement firstElement = elements[0];
        final PsiElement lastElement  = elements[1];

        final int offset     = firstElement.getTextOffset();
        final int textLength = lastElement.getTextOffset() - offset + lastElement.getTextLength();

        final int tabs = editor.offsetToLogicalPosition(offset).column / editor.getSettings().getTabSize(project);

        final Document document = editor.getDocument();

        ApplicationManager.getApplication().runWriteAction(() -> {
            final String oldText = addTabs(document.getText(new TextRange(offset, offset + textLength)), tabs);
            document.replaceString(offset, offset + textLength, group.getId() + " {" + oldText + "};");
        });

        final int braces = offset + group.getId().length() + 1;

        return new TextRange(braces, braces);
    }  // end method surroundElements

    @Override public boolean isApplicable(@NotNull PsiElement[] elements) {
        return !Arrays.equals(elements, EMPTY_ARRAY);
    }

    @Override public String getTemplateDescription() {
        return group.getId() + " {...}";
    }

    protected String addTabs(String oldText, int tabsInitialOffset) {
        final StringTokenizer tokenizer = new StringTokenizer(oldText);
        String                newText   = '\n' + Strings.nChars('\t', tabsInitialOffset + 1);

        while (tokenizer.hasMoreElements()) {
            String text = tokenizer.nextToken("\n");
            text    =  text + "\n\t";
            newText += text;
        }

        newText += Strings.nChars('\t', tabsInitialOffset - 1);

        return newText;
    }
}  // end class MMFormFieldSurrounder
