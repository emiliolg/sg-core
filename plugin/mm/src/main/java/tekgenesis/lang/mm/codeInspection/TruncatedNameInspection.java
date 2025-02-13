
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import java.util.ArrayList;

import com.intellij.codeInspection.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.MMIdentifier;
import tekgenesis.lang.mm.psi.PsiEntity;
import tekgenesis.lang.mm.psi.PsiEntityField;
import tekgenesis.type.Names;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.MAX_DB_ID_LENGTH;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.field.FieldOption.COLUMN;

/**
 * Inspection for when the Table or column name will be truncated.
 */
public class TruncatedNameInspection extends LocalInspectionTool {

    //~ Methods ......................................................................................................................................

    @Override public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        final ArrayList<ProblemDescriptor> problemDescriptors = new ArrayList<>();
        final MMFile                       mmFile             = cast(file);
        for (final PsiEntity entity : mmFile.getEntities()) {
            checkEntity(manager, isOnTheFly, problemDescriptors, entity);

            for (final PsiEntityField entityField : entity.getFields())
                checkEntityField(manager, isOnTheFly, problemDescriptors, entityField, entity);
        }
        return problemDescriptors.toArray(new ProblemDescriptor[problemDescriptors.size()]);
    }

    private void checkEntity(@NotNull InspectionManager manager, boolean isOnTheFly, ArrayList<ProblemDescriptor> problemDescriptors,
                             PsiEntity entity) {
        if (Names.longTableName(entity.getName()).length() > MAX_DB_ID_LENGTH)
            problemDescriptors.add(
                manager.createProblemDescriptor(entity.getIdentifier(),
                    "Table name exceeds " + MAX_DB_ID_LENGTH + " characters for this entity," +
                    "\n will be truncated to " + Names.tableName(entity.getName()),
                    isOnTheFly,
                    LocalQuickFix.EMPTY_ARRAY,
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING));
    }

    private void checkEntityField(@NotNull InspectionManager manager, boolean isOnTheFly, ArrayList<ProblemDescriptor> problemDescriptors,
                                  PsiEntityField entityField, PsiEntity entity) {
        if (Names.longTableName(entityField.getName()).length() > MAX_DB_ID_LENGTH) {
            if (fieldHasOption(entityField, COLUMN)) return;

            problemDescriptors.add(
                manager.createProblemDescriptor(entityField.getIdentifier(),
                    "Field name exceeds " + MAX_DB_ID_LENGTH + " characters,\n" +
                    " will be truncated to " + Names.tableName(entityField.getName() + " in Table"),
                    new TruncatedFix(entity, entityField),
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    isOnTheFly));
        }
    }

    private String checkLastCharSep(String str, char sep) {
        final int lastCharIndex = str.length() - 1;
        if (str.charAt(lastCharIndex) == sep) return checkLastCharSep(str.substring(0, lastCharIndex - 1), sep);
        else return str;
    }

    private boolean fieldHasOption(PsiEntityField entityField, FieldOption option) {
        final ASTNode[] fieldOptions = entityField.getChildren(TokenSet.create(MMElementType.OPTION));
        for (final ASTNode fieldOption : fieldOptions) {
            final ASTNode id = fieldOption.findChildByType(MMElementType.IDENTIFIER);
            if (id instanceof MMIdentifier && option.name().equalsIgnoreCase(((MMIdentifier) id).getName())) return true;
        }
        return false;
    }

    private String getTruncatedName(final String column, boolean withSeq) {
        final char   sep    = '_';
        final String suffix = withSeq ? sep + "seq" : "";
        return checkLastCharSep(truncate(column, MAX_DB_ID_LENGTH - suffix.length()), sep) + suffix;
    }

    //~ Inner Classes ................................................................................................................................

    private class TruncatedFix extends AbstractFix<PsiEntity, PsiEntityField> {
        private TruncatedFix(final PsiEntity entity, final PsiEntityField entityField) {
            super("Set Default Column Name", entity, entityField);
        }

        @Override public void doApplyFix(Project project, PsiEntity entity, PsiEntityField e) {
            final boolean withSeq = isPrimaryKeyAutoGenerated(entity);

            final PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
            final Document           document        = documentManager.getDocument(e.getContainingFile());
            final ASTNode            fieldType       = e.findChildByType(MMElementType.TYPE);

            if (document != null && fieldType != null) {
                final String text = String.format(", %s %s",
                        COLUMN.name().toLowerCase(),
                        getTruncatedName(fromCamelCase(e.getIdentifier().getText()).toLowerCase(), withSeq));

                documentManager.doPostponedOperationsAndUnblockDocument(document);
                if (!e.getText().contains(";")) document.insertString(e.getTextOffset() + e.getTextLength(), ";");
                document.insertString(fieldType.getStartOffset() + fieldType.getTextLength(), text);
                documentManager.commitDocument(document);
            }
        }

        private boolean isPrimaryKeyAutoGenerated(PsiEntity entity) {
            return entity.findChildByType(MMElementType.PRIMARY_KEY) == null;
        }
    }
}  // end class TruncatedNameInspection
