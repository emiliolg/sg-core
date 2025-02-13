
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.rename;

import java.util.Set;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenameJavaMethodProcessor;
import com.intellij.refactoring.rename.RenameJavaVariableProcessor;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.HashSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Strings;
import tekgenesis.field.ModelField;
import tekgenesis.lang.mm.psi.MMIdentifier;
import tekgenesis.lang.mm.psi.PsiModelField;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.type.Scope;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.lang.mm.FileUtils.saveFiles;
import static tekgenesis.lang.mm.psi.PsiUtils.*;
import static tekgenesis.lang.mm.rename.RenamePsiMetaModelProcessor.renameClass;
import static tekgenesis.lang.mm.rename.ScopeInfo.createScopeInfoForMultiple;

/**
 * Any field rename processor.
 */
class RenamePsiFieldProcessor extends RenamePsiElementProcessor {

    //~ Methods ......................................................................................................................................

    @Override public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof PsiModelField || (element instanceof MMIdentifier &&
                                                    element.getParent().getParent() instanceof PsiModelField);
    }

    @Override public void renameElement(final PsiElement element, String newName, UsageInfo[] usages, RefactoringElementListener listener)
        throws IncorrectOperationException
    {
        for (final PsiModelField field : findParentField(element)) {
            final Set<PsiFile> affectedFiles = new HashSet<>();

            // Rename field java references
            renameFieldJavaReferences(element, newName, listener, field, affectedFiles);

            for (final PsiWidget widget : ImmutableList.fromArray(usages).map(RenamePsiFieldProcessor::toFormField)) {
                // Rename any form field binding java references if binding id is not overridden
                if (equal(widget.getBinding(), field) && !widget.isBindingIdOverridden())
                    renameFieldJavaReferences(element, newName, listener, widget, affectedFiles);
            }

            saveFiles(affectedFiles);
        }

        super.renameElement(element, newName, usages, listener);
    }

    private void renameFieldEnumConstant(ModelField field, String newName, ScopeInfo scopeInfo, Project project, RefactoringElementListener l,
                                         Set<PsiFile> affectedFiles) {
        final PsiClass fieldsEnumClass = getPsiClassForFqnNullable(project, qualify(scopeInfo.getRootBaseClassName(), FIELDS_ENUM));
        if (fieldsEnumClass != null) {
            final PsiField constant = fieldsEnumClass.findFieldByName(field.getName().toUpperCase(), true);
            if (constant != null) renameVariable(newName, l, affectedFiles, constant);
        }
    }

    private void renameFieldJavaReferences(PsiElement element, String newName, RefactoringElementListener listener, PsiModelField field,
                                           Set<PsiFile> affectedFiles) {
        for (final ModelField modelField : field.getModelField()) {
            final ScopeInfo scopeInfo = field.getScopeInfo();
            renameJavaReferences(modelField, newName, scopeInfo, element.getProject(), listener, affectedFiles);
        }
    }

    /** Rename java getter, setters, constants and classes for given field. */
    private void renameJavaReferences(@NotNull ModelField field, @NotNull String newName, @NotNull ScopeInfo scopeInfo, @NotNull Project project,
                                      RefactoringElementListener l, Set<PsiFile> affectedFiles) {
        final PsiClass userClass = getPsiClassForFqnNullable(project, scopeInfo.getScopedUserClassName());
        final PsiClass baseClass = getPsiClassForFqnNullable(project, scopeInfo.getScopedBaseClassName());

        final String type = field.getType().getImplementationClassName();

        final String oldSetter = Strings.setterName(field.getName());
        final String oldGetter = Strings.getterName(field.getName(), type);

        final String newSetter = Strings.setterName(newName);
        final String newGetter = Strings.getterName(newName, type);

        if (baseClass != null) {
            affectedFiles.add(baseClass.getContainingFile());
            // Rename fields enum constant!
            renameFieldEnumConstant(field, newName, scopeInfo, project, l, affectedFiles);
        }

        if (userClass != null) {
            // Rename all getter references!
            for (final PsiMethod getter : userClass.findMethodsByName(oldGetter, true))
                renameMethod(l, newGetter, affectedFiles, getter);

            // Rename all setter references!
            for (final PsiMethod setter : userClass.findMethodsByName(oldSetter, true))
                renameMethod(l, newSetter, affectedFiles, setter);
        }

        // Rename table inner classes as well!
        if (field instanceof Scope) renameMultipleInnerClass(field, newName, project, l, affectedFiles, scopeInfo.getRootUserClassName());
    }

    private void renameMethod(RefactoringElementListener l, String newMethod, Set<PsiFile> affectedFiles, PsiMethod method) {
        final RenamePsiElementProcessor rename = RenameJavaMethodProcessor.forElement(method);
        final UsageInfo[]               usages = convertReferencesToUsageInfo(rename.findReferences(method, true), affectedFiles);
        rename.renameElement(method, newMethod, usages, l);
    }

    private void renameMultipleInnerClass(ModelField field, String newName, Project project, RefactoringElementListener l, Set<PsiFile> affectedFiles,
                                          String rootUserClassName) {
        final ScopeInfo scopeInfo      = createScopeInfoForMultiple(createQName(rootUserClassName), field.getName());
        final String    tableUserFqn   = scopeInfo.getScopedUserClassName();
        final String    tableBaseFqn   = scopeInfo.getScopedBaseClassName();
        final String    tableNewName   = capitalizeFirst(newName + ROW_CLASS_SUFFIX);
        final PsiClass  tableBaseClass = getPsiClassForFqnNullable(project, tableBaseFqn);
        final PsiClass  tableUserClass = getPsiClassForFqnNullable(project, tableUserFqn);
        if (tableBaseClass != null) affectedFiles.add(tableBaseClass.getContainingFile());
        renameClass(tableNewName + BASE, tableBaseClass, l, affectedFiles);
        renameClass(tableNewName, tableUserClass, l, affectedFiles);
    }

    private void renameVariable(String newName, RefactoringElementListener l, Set<PsiFile> affectedFiles, PsiField constant) {
        final RenamePsiElementProcessor processor = RenameJavaVariableProcessor.forElement(constant);
        final UsageInfo[]               usages    = convertReferencesToUsageInfo(processor.findReferences(constant, true), affectedFiles);
        processor.renameElement(constant, newName.toUpperCase(), usages, l);
    }

    //~ Methods ......................................................................................................................................

    @Nullable private static PsiWidget toFormField(final UsageInfo value) {
        return findParentField(value.getElement()).castTo(PsiWidget.class).getOrNull();
    }
}  // end class RenamePsiFieldProcessor
