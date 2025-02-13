
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.folding;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.codegen.CodeGeneratorConstants.TABLE;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;

/**
 * Folding Builder for Java Classes Generated.
 */
public class GeneratedJavaFoldingBuilder implements FoldingBuilder {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        if (astNode.getPsi() instanceof PsiJavaFile) {
            final PsiJavaFile javaFile = (PsiJavaFile) astNode.getPsi();
            for (final PsiClass psiClass : javaFile.getClasses()) {
                for (final PsiClass aClass : psiClass.getInnerClasses()) {
                    final PsiClass superClass = aClass.getSuperClass();
                    checkTableFolding(descriptors, aClass, superClass);
                    checkCacheDataFolding(descriptors, aClass, superClass);
                    checkRowFolding(descriptors, aClass, superClass);
                }
            }
        }
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Override public String getPlaceholderText(@NotNull ASTNode astNode) {
        final PsiElement psi = astNode.getPsi();
        if (psi instanceof PsiIdentifier) return psi.getText();
        return "{...}";
    }

    @Override public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return true;
    }

    private void addFoldingDescriptor(List<FoldingDescriptor> descriptors, PsiClass clazz) {
        final PsiIdentifier nameIdentifier = clazz.getNameIdentifier();
        if (nameIdentifier != null)
            descriptors.add(
                new FoldingDescriptor(nameIdentifier, new TextRange(nameIdentifier.getTextOffset(), clazz.getTextRange().getEndOffset())));
    }

    private void checkCacheDataFolding(List<FoldingDescriptor> descriptors, PsiClass clazz, @Nullable PsiClass superClass) {
        final String name  = clazz.getName();
        final String sName = superClass == null ? null : superClass.getName();
        if (name != null && sName != null && name.equals(DATA_CLASS_NAME) && sName.equals(OPEN_DATA_CLASS_NAME))
            addFoldingDescriptor(descriptors, clazz);
    }

    private void checkRowFolding(List<FoldingDescriptor> descriptors, PsiClass clazz, @Nullable PsiClass superClass) {
        final String name  = clazz.getName();
        final String sName = superClass == null ? null : superClass.getName();
        if (name != null && sName != null && name.endsWith(ROW_CLASS_SUFFIX) && sName.endsWith(ROW_BASE_CLASS_SUFFIX))
            addFoldingDescriptor(descriptors, clazz);
    }

    private void checkTableFolding(List<FoldingDescriptor> descriptors, PsiClass clazz, @Nullable PsiClass superClass) {
        final String name  = clazz.getName();
        final String sName = superClass == null ? null : superClass.getName();
        if (name != null && sName != null && name.equals(TABLE) && TABLE_BASE_CLASS_NAME.equals(superClass.getName()))
            addFoldingDescriptor(descriptors, clazz);
    }
}  // end class GeneratedJavaFoldingBuilder
