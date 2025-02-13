
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.EnumSet;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MetaModelReferenceKind;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiImport;
import tekgenesis.lang.mm.psi.PsiMetaModelCodeReferenceElement;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.lang.mm.completion.LookupElements.*;
import static tekgenesis.lang.mm.psi.MetaModelReferences.getReferenceKind;
import static tekgenesis.lang.mm.psi.PsiUtils.*;
import static tekgenesis.type.MetaModelKind.*;

/**
 * Reference completion provider.
 */
class ReferenceCompletionProvider extends CompletionProvider<CompletionParameters> {

    //~ Methods ......................................................................................................................................

    @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                            @NotNull CompletionResultSet result) {
        final PsiElement element = parameters.getOriginalPosition();
        if (element != null) {
            final PsiMetaModelCodeReferenceElement reference = getReference(parameters);
            final MetaModelReferenceKind           kind      = getReferenceKind(reference);
            switch (kind) {
            case PACKAGE_NAME_KIND:
                packageCompletions(reference, result);
                break;
            case CLASS_NAME_KIND:
            case CLASS_FQ_NAME_KIND:
                packageCompletions(reference, result);
                classCompletions(reference, result);
                break;
            }
        }
    }

    private void addPackagesLookupElements(ModelRepository repository, String qualification, CompletionResultSet result) {
        result.addAllElements(lookupDomains(repository, qualification));
    }

    private void classCompletions(PsiMetaModelCodeReferenceElement reference, CompletionResultSet result) {
        final Module                 module        = getModule(reference);
        final Option<String>         qualification = getQualificationString(reference);
        final EnumSet<MetaModelKind> scope         = resolveScope(reference);

        final Project      project  = reference.getProject();
        Seq<LookupElement> elements = lookupMetaModels(project, module, getModelRepository(module), scope, qualification).append(
                lookupMetaModels(project, module, getLibRepository(module), scope, qualification));

        if (qualification.isEmpty() && (scope == SCOPE_TYPE || scope == SCOPE_STRUCT)) elements = elements.append(lookupElementsForBasicTypes());

        result.addAllElements(elements);
    }

    private void packageCompletions(PsiMetaModelCodeReferenceElement reference, CompletionResultSet result) {
        for (final String qualification : getQualificationString(reference)) {
            final Module module = getModule(reference);
            addPackagesLookupElements(getModelRepository(module), qualification, result);
            addPackagesLookupElements(getLibRepository(module), qualification, result);
        }
    }

    private EnumSet<MetaModelKind> resolveScope(PsiMetaModelCodeReferenceElement reference) {
        final PsiElement   parent = reference.getParent();
        final IElementType t      = parent.getNode().getElementType();

        if (parent instanceof PsiDomain || !(t instanceof MMElementType)) return SCOPE_EMPTY;

        if (parent instanceof PsiImport) return SCOPE_IMPORT;

        final MMElementType type = (MMElementType) t;
        switch (type.getTokenType()) {
        case LISTING:
        case ENTITY_REF:
            return ENTITY_ONLY;
        case DATAOBJECT_REF:
            return SCOPE_TABLE;
        case FORM_REF:
            return FORM_ONLY;
        case WIDGET_DEF_REF:
            return WIDGET_ONLY;
        case STRUCT_REF:
            return resolveStructRefScope(parent);
        case TASK_REF:
            return TASK_ONLY;
        case ENUM_REF:
            return ENUM_ONLY;
        case TYPE_REF:
        default:
            return SCOPE_TYPE;
        }
    }

    private EnumSet<MetaModelKind> resolveStructRefScope(@NotNull PsiElement parent) {
        return parent.getParent().getNode().getElementType() == MMElementType.TYPE ? SCOPE_STRUCT : TYPE_ONLY;
    }

    @NotNull private Option<String> getQualificationString(PsiMetaModelCodeReferenceElement reference) {
        if (reference.isQualified()) {
            final PsiElement qualifier = reference.getQualifier();
            if (qualifier != null) return some(qualifier.getText());
        }
        return Option.empty();
    }

    private PsiMetaModelCodeReferenceElement getReference(@NotNull CompletionParameters parameters) {
        return (PsiMetaModelCodeReferenceElement) parameters.getPosition().getParent();
    }
}  // end class ReferenceCompletionProvider
