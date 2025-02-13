
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.List;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.actions.DefaultSearchableExpansionAction;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassForFqnNullable;
import static tekgenesis.mmcompiler.ast.MMToken.SEARCHABLE;

/**
 * Database Object model class.
 */
public abstract class PsiDatabaseObject<T extends DbObject> extends PsiMetaModel<T> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiDatabaseObject(@NotNull MMElementType t, @NotNull MetaModelKind kind, @NotNull Class<T> metamodelClass) {
        super(t, kind, metamodelClass);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        annotateSearchable(holder);
    }

    @Nullable @Override public PsiEntityField getFieldNullable(String name) {
        return getPsiModelField(name, getFields());
    }

    @NotNull @Override public PsiEntityField[] getFields() {
        return getPsiModelFields(MMElementType.LIST, MMElementType.ENTITY_FIELD, PsiEntityField[]::new, EMPTY_ENTITY_MODEL_FIELDS);
    }

    @NotNull @Override public List<PsiClass> getLineMarkerTargets() {
        final ImmutableList.Builder<PsiClass> targets = ImmutableList.<PsiClass>builder().addAll(super.getLineMarkerTargets());

        addPsiClassWithSuffix(targets, getFullName() + MMCodeGenConstants.SEARCHER_SUFFIX);
        addPsiClassWithSuffix(targets, qualify(getDomain() + ".g", getName()) + MMCodeGenConstants.TABLE_CLASS_NAME);

        return targets.build();
    }

    private void addPsiClassWithSuffix(ImmutableList.Builder<PsiClass> targets, String fqn) {
        final PsiClass psiClass = getPsiClassForFqnNullable(getProject(), fqn);
        if (psiClass != null) targets.add(psiClass);
    }

    private void annotateSearchable(AnnotationHolder holder) {
        for (final PsiElement child : getChildren()) {
            if (child instanceof MMCommonComposite && ((MMCommonComposite) child).getType() == SEARCHABLE)
                checkSearchableIntention(holder, (MMCommonComposite) child);
        }
    }

    private void checkSearchableIntention(AnnotationHolder holder, MMCommonComposite child) {
        if (child.children().isEmpty()) {
            final Annotation annotation = holder.createWeakWarningAnnotation(child.getTextRange(), EXPAND_DEFAULT_SEARCHABLE);
            annotation.registerFix(new PsiDatabaseObject.ExpandSearchableAction<>(this, child));
        }
    }

    //~ Static Fields ................................................................................................................................

    static final PsiEntityField[] EMPTY_ENTITY_MODEL_FIELDS = {};
    private static final String   EXPAND_DEFAULT_SEARCHABLE = "Expand Default Searchable";

    //~ Inner Classes ................................................................................................................................

    private static class ExpandSearchableAction<T extends DbObject> implements IntentionAction {
        private final PsiDatabaseObject<T> dbObject;
        private final MMCommonComposite    searchable;

        ExpandSearchableAction(PsiDatabaseObject<T> dbObject, MMCommonComposite child) {
            this.dbObject = dbObject;
            searchable    = child;
        }

        @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException
        {
            final DefaultSearchableExpansionAction<T> undoableDefaultSearchableExpansion = new DefaultSearchableExpansionAction<>(editor, project);
            CommandProcessor.getInstance()
                .executeCommand(project, undoableDefaultSearchableExpansion, DefaultSearchableExpansionAction.SEARCHABLE_EXPANSION, null);
        }

        @Override public boolean startInWriteAction() {
            return true;
        }

        @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return true;
        }

        @NotNull @Override public String getFamilyName() {
            return getText();
        }

        @NotNull @Override public String getText() {
            return "Expand searchable";
        }
    }
}  // end class PsiDatabaseObject
