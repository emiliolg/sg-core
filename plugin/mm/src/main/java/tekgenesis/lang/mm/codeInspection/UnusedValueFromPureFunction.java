
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.collections.ImmutableList.fromArray;

/**
 * Check if methods with @Pure annotation's value is being used.
 */
public class UnusedValueFromPureFunction extends LocalInspectionTool {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override public void visitExpressionStatement(PsiExpressionStatement statement) {
                final PsiExpression expression = statement.getExpression();
                if (expression instanceof PsiMethodCallExpression) {
                    final PsiMethod method = ((PsiMethodCallExpression) expression).resolveMethod();
                    if (method != null) {
                        final ImmutableList<PsiMethod> deepest = fromArray(method.findDeepestSuperMethods());

                        if (deepest.isEmpty()) checkAnnotation(statement, method, holder);
                        else deepest.forEach(m -> checkAnnotation(statement, m, holder));
                    }
                }
                super.visitExpressionStatement(statement);
            }
        };
    }

    @Nullable @Override public String getStaticDescription() {
        return "A Pure function:" +
               "<ul>" +
               "\n<li>always evaluates the same result value given the same arguments</li>" +
               "\n<li>does not cause any semantically observable side effect or output after the evaluation of the result</li>" +
               "</ul>" +
               "\n<strong>Therefore, pure functions return value must be assigned or evaluated after invocation.</strong>";
    }

    private void checkAnnotation(PsiExpressionStatement statement, PsiMethod m, ProblemsHolder holder) {
        final PsiAnnotation annotation = m.getModifierList().findAnnotation(Pure.class.getName());
        if (annotation != null) holder.registerProblem(statement, "Unassigned return value from pure method.");
    }
}
