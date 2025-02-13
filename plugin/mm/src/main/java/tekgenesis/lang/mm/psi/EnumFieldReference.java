
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.some;

/**
 * Reference of an enum field inside an enum in a MM file.
 */
public class EnumFieldReference extends AbstractReference {

    //~ Instance Fields ..............................................................................................................................

    private final Option<PsiEnum> psiEnum;

    //~ Constructors .................................................................................................................................

    EnumFieldReference(@NotNull String text, @NotNull TextRange range, @NotNull ElementWithReferences underlying) {
        super(text, range, underlying);
        psiEnum = getEnum(underlying);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<PsiEnumField> resolveInner() {
        return psiEnum.map(e -> e.getFieldNullable(getElement().getText()));
    }

    @NotNull @Override public Object[] getVariants() {
        if (psiEnum.isPresent()) return psiEnum.get().getFields();
        return EMPTY_ARRAY;
    }

    @Nullable private Option<PsiEnum> getEnum(PsiElement element) {
        final PsiElement parent = element.getParent();
        if (parent instanceof PsiEnum) return some((PsiEnum) parent);
        else if (parent instanceof PsiFile || parent == null) return Option.empty();
        else return getEnum(parent);
    }
}
