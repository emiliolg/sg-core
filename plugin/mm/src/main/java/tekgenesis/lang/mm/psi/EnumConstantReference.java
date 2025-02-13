
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

import static com.intellij.psi.search.GlobalSearchScope.allScope;

import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.QName.extractQualification;

/**
 * Psi Java enum constant reference.
 */
class EnumConstantReference extends AbstractReference {

    //~ Instance Fields ..............................................................................................................................

    private final String fqn;

    //~ Constructors .................................................................................................................................

    EnumConstantReference(@NotNull TextRange range, @NotNull ElementWithReferences source, @NotNull String fqn) {
        super(source.getText(), range, source);
        this.fqn = fqn;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<PsiField> resolveInner() {
        final PsiClass psiClass = JavaPsiFacade.getInstance(getProject()).findClass(extractQualification(fqn), allScope(getProject()));
        if (psiClass == null || !psiClass.isEnum()) return Option.empty();
        for (final PsiField field : psiClass.getFields()) {
            final String name = extractName(fqn);
            if (field instanceof PsiEnumConstant && field.getNameIdentifier().getText().equals(name)) return Option.of(field);
        }
        return Option.empty();
    }

    @NotNull @Override public Object[] getVariants() {
        return EMPTY_ARRAY;
    }
}
