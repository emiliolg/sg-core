
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.descriptor;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewTypeLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Strings;
import tekgenesis.lang.mm.psi.PsiEntityField;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.lang.mm.MMPluginConstants.WIDGET;

/**
 * MetaModel Type description provider.
 */
class MMElementTypeDescriptionProvider implements ElementDescriptionProvider {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        return location instanceof UsageViewTypeLocation ? getElementTypeDescription(element) : null;
    }

    @Nullable private String getElementTypeDescription(@NotNull final PsiElement element) {
        String result = getFinalElementTypeDescription(element);

        if (result == null && element.getParent() != null) {
            /* If we didn't match any describable type, try with the enclosing one. */
            result = getFinalElementTypeDescription(element.getParent().getParent());
        }  //

        return result;
    }

    @Nullable private String getFinalElementTypeDescription(PsiElement element) {
        if (element instanceof PsiMetaModel) return getMetaModelKindDescription((PsiMetaModel<?>) element);
        if (element instanceof PsiWidget) return WIDGET;
        if (element instanceof PsiEntityField) return "Entity field";
        return null;
    }

    private String getMetaModelKindDescription(@NotNull final PsiMetaModel<?> element) {
        final MetaModelKind kind = element.getMetaModelKind();
        return Strings.toWords(kind.name());
    }
}
