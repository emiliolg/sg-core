
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import java.io.File;
import java.util.ArrayList;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiEnum;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiWidgetDef;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static tekgenesis.common.Predefined.cast;

/**
 * Abstract Translation inspection for MetaModel's.
 */
public abstract class MMTranslationInspection extends LocalInspectionTool {

    //~ Methods ......................................................................................................................................

    @Override public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        final MMFile    mmFile = cast(file);
        final PsiDomain domain = mmFile.getDomain();
        if (domain == null || domain.getDomainName() == null) return null;

        final String propertyFolderPath = mmFile.getPath().split("mm/")[0] + RESOURCES + domain.getDomainName().replaceAll("\\.", "/");

        final File                         propertyFolder     = new File(propertyFolderPath);
        final ArrayList<ProblemDescriptor> problemDescriptors = new ArrayList<>();
        if (propertyFolder.exists() && propertyFolder.isDirectory()) {
            for (final MetaModelAST metaModelASTs : mmFile.getFirstRoot())
                if (metaModelASTs instanceof PsiEnum || metaModelASTs instanceof PsiForm || metaModelASTs instanceof PsiWidgetDef) {
                    final MMCommonComposite commonComposite = cast(metaModelASTs);
                    checkChildren(propertyFolderPath, commonComposite, mmFile, problemDescriptors, isOnTheFly, manager);
                }
        }

        return problemDescriptors.toArray(new ProblemDescriptor[problemDescriptors.size()]);
    }

    protected abstract void checkChildren(String propertyFolderPath, MMCommonComposite commonComposite, MMFile mmFile,
                                          ArrayList<ProblemDescriptor> problemDescriptors, boolean isOnTheFly, @NotNull InspectionManager manager);

    //~ Static Fields ................................................................................................................................

    public static final String RESOURCES = "resources/";
}  // end class MMTranslationInspection
