
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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenameJavaClassProcessor;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.HashSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.codeInspection.MMTranslationInspection;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.MMIdentifier;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.type.MetaModel;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.lang.mm.FileUtils.saveFiles;
import static tekgenesis.lang.mm.psi.PsiUtils.*;

/**
 * Any meta model rename processor.
 */
class RenamePsiMetaModelProcessor extends RenamePsiElementProcessor {

    //~ Methods ......................................................................................................................................

    @Override public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof PsiMetaModel || (element instanceof MMIdentifier &&
                                                   element.getParent().getParent() instanceof PsiMetaModel);
    }

    @Override public void renameElement(final PsiElement element, String newName, UsageInfo[] usages, RefactoringElementListener listener)
        throws IncorrectOperationException
    {
        for (final PsiMetaModel<?> mm : findParentModel(element)) {
            final Option<? extends MetaModel> model = mm.getModel();
            if (model.isPresent()) {
                renameJavaClass(newName, model.get().getFullName(), element.getProject(), listener);
                renamePropertiesFile(newName, model.get(), element.getContainingFile(), listener);
            }
        }

        super.renameElement(element, newName, usages, listener);
    }

    @Nullable @Override public Runnable getPostRenameCallback(final PsiElement element, String newName,
                                                              final RefactoringElementListener elementListener) {
        return FileUtils::saveAndSynchronizeFiles;
    }

    private void renameFile(@NotNull String newName, @NotNull MetaModel metaModel, @NotNull RefactoringElementListener listener, MMFile mmFile,
                            VirtualFile child) {
        final PsiFile psiFile = PsiManager.getInstance(mmFile.getProject()).findFile(child);
        if (psiFile != null) {
            final RenamePsiElementProcessor psiElementProcessor = RenamePsiFileProcessor.forElement(psiFile);
            psiElementProcessor.renameElement(psiFile, child.getName().replace(metaModel.getName(), newName), EMPTY_USAGES, listener);
        }
    }

    /** Rename java user and base classes. */
    private void renameJavaClass(@NotNull String newName, @NotNull String fqn, @NotNull Project project,
                                 @NotNull RefactoringElementListener listener) {
        final PsiClass userClass = getPsiClassForFqnNullable(project, fqn);
        final PsiClass baseClass = getPsiClassForFqnNullable(project, fqn + BASE);

        final Set<PsiFile> affectedFiles = new HashSet<>();
        if (baseClass != null) affectedFiles.add(baseClass.getContainingFile());

        renameClass(newName + BASE, baseClass, listener, affectedFiles);
        renameClass(newName, userClass, listener, affectedFiles);

        saveFiles(affectedFiles);
    }

    /** Rename properties file. */
    private void renamePropertiesFile(@NotNull String newName, @NotNull MetaModel metaModel, @NotNull PsiFile file,
                                      @NotNull RefactoringElementListener listener) {
        final MMFile    mmFile = cast(file);
        final PsiDomain domain = mmFile.getDomain();
        if (domain == null || domain.getDomainName() == null) return;

        final String propertyFolderPath = mmFile.getPath().split("mm/")[0] + MMTranslationInspection.RESOURCES +
                                          domain.getDomainName().replaceAll("\\.", "/");

        final VirtualFile vf = FileUtils.findVirtualFile(propertyFolderPath);
        if (vf == null) return;

        fromArray(vf.getChildren()).filter(child -> child.getPath().matches(propertyFolderPath + "/" + metaModel.getName() + "([\\.|_](\\w+))+"))
            .forEach(child -> renameFile(newName, metaModel, listener, mmFile, child));
    }

    //~ Methods ......................................................................................................................................

    /** Rename given class with given newName. */
    static void renameClass(String newName, @Nullable PsiClass affectedClass, RefactoringElementListener listener, Set<PsiFile> affectedFiles) {
        if (affectedClass != null) {
            final RenamePsiElementProcessor baseRenameProcessor = RenameJavaClassProcessor.forElement(affectedClass);
            baseRenameProcessor.renameElement(affectedClass,
                newName,
                convertReferencesToUsageInfo(baseRenameProcessor.findReferences(affectedClass), affectedFiles),
                listener);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final UsageInfo[] EMPTY_USAGES = new UsageInfo[0];
}  // end class RenamePsiMetaModelProcessor
