
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.View;

import static java.util.Collections.singletonList;

import static tekgenesis.common.core.Strings.split;

/**
 * A field reference node, supporting multiple references.
 */
public class PsiFieldReference extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiFieldReference(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Nullable public PsiElement getIdentifier() {
        final PsiElement[] elements = getChildrenAsPsiElements(MMElementType.IDENTIFIER, PsiElement.ARRAY_FACTORY);
        return elements.length != 0 ? elements[elements.length - 1] : null;
    }

    @Override public FieldReference getReference() {
        final FieldReference[] references = getReferences();
        return references.length > 0 ? references[references.length - 1] : null;
    }

    @NotNull @Override public FieldReference[] getReferences() {  //
        return PsiUtils.findParentModel(this).map(model -> {
                final List<FieldReference> result;
                // Fix views parser not to parse field_ref for entity attribute binding
                if (isViewsHack(model)) result = createViewsReferences((PsiView) model);
                else result = createReferences(model.getFullName());
                return result.toArray(new FieldReference[result.size()]);
            }).orElse(FieldReference.EMPTY_ARRAY);
    }

    private List<FieldReference> createReferences(String root) {
        final List<FieldReference> result = new ArrayList<>();

        FieldReference parent = null;
        int            offset = 0;

        for (final String field : split(getText(), '.')) {
            final TextRange      range     = TextRange.create(offset, offset + field.length());
            final FieldReference reference;

            if (offset == 0) reference = new FieldReference(root, field, range, this);
            else reference = new FieldReference(parent, field, range, this);

            parent = reference;
            result.add(reference);
            offset += field.length() + 1;
        }
        return result;
    }

    private List<FieldReference> createViewsReferences(PsiView psiView) {
        for (final View view : psiView.getModel()) {
            for (final DbObject entity : view.getBaseEntity())
                return singletonList(new FieldReference(entity.getFullName(), getText(), getEntireTextRange(), this));
        }
        return Colls.emptyList();
    }

    private boolean isViewsHack(PsiMetaModel<?> model) {
        return model instanceof PsiView && getParent() instanceof PsiEntityField;
    }
}  // end class PsiFieldReference
