
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.PsiMethodReference.MethodDescriptor;

import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.lang.mm.psi.PsiUtils.*;

/**
 * Reference to a java method.
 */
class MethodReference extends AbstractReference {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String           className;
    @NotNull private final MethodDescriptor descriptor;
    @NotNull private final String           methodName;

    //~ Constructors .................................................................................................................................

    protected MethodReference(@NotNull String className, @NotNull String methodName, @NotNull MethodDescriptor descriptor, @NotNull TextRange range,
                              @NotNull PsiMethodReference underlying) {
        super(className + "." + methodName, range, underlying);
        this.className  = className;
        this.methodName = methodName;
        this.descriptor = descriptor;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<PsiMethod> resolveInner() {
        return resolveJavaMethod(getPsiClassForFqnNullable(getProject(), className), methodName, descriptor);
    }

    @Override public PsiMethodReference getElement() {
        return (PsiMethodReference) super.getElement();
    }

    @Override public boolean isReferenceTo(PsiElement element) {
        return element instanceof PsiMethod && super.isReferenceTo(element);
    }

    @NotNull @Override public Object[] getVariants() {
        final PsiClass psiJavaClass = resolveJavaClass(getProject(), className);
        if (psiJavaClass != null) return filter(asList(psiJavaClass.getMethods()), descriptor).toList().toArray();
        return LookupElement.EMPTY_ARRAY;
    }
}
