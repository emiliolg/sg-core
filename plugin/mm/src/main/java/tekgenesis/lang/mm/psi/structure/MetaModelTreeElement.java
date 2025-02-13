
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.lang.ASTNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.*;

import static tekgenesis.common.collections.ImmutableList.empty;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.lang.mm.psi.structure.MetaModelStructureView.SEMICOLON;
import static tekgenesis.lang.mm.psi.structure.MetaModelStructureView.cutPostfix;

class MetaModelTreeElement extends PsiTreeElementBase<PsiMetaModel<?>> {

    //~ Instance Fields ..............................................................................................................................

    private final Function<PsiModelField, StructureViewTreeElement> generator;

    //~ Constructors .................................................................................................................................

    /** A MetaModel tree node for a given Intellij Idea node. */
    MetaModelTreeElement(PsiMetaModel<?> node, Function<PsiModelField, StructureViewTreeElement> generator) {
        super(node);
        this.generator = generator;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Collection<StructureViewTreeElement> getChildrenBase() {
        final PsiMetaModel<?> element = getElement();
        if (element != null) return fromArray(element.getFields()).map(generator).into(new ArrayList<>());
        return empty();
    }

    @Override public String getPresentableText() {
        final PsiMetaModel<?> element = getElement();
        return element != null ? element.getName() : Constants.INVALID;
    }

    //~ Inner Classes ................................................................................................................................

    static class EntityFieldTreeElement extends MetaModelFieldTreeElement<PsiEntityField> {
        EntityFieldTreeElement(PsiModelField field) {
            super((PsiEntityField) field);
        }

        @Override public String getPresentableText() {
            final PsiEntityField element = getElement();
            return element != null ? cutPostfix(element.getName(), SEMICOLON) + ":" + element.getTypeName() : Constants.INVALID;
        }
    }

    static class HandlerFieldTreeElement extends MetaModelFieldTreeElement<PsiHandlerField> {
        HandlerFieldTreeElement(PsiModelField field) {
            super((PsiHandlerField) field);
        }

        @Override public String getPresentableText() {
            final PsiHandlerField element = getElement();
            if (element == null) return Constants.INVALID;
            final ASTNode childByType = element.findChildByType(MMElementType.PATH);
            return childByType != null ? childByType.getText() : Constants.INVALID;
        }
    }

    static class MetaModelFieldTreeElement<F extends PsiModelField> extends PsiTreeElementBase<F> {
        MetaModelFieldTreeElement(F field) {
            super(field);
        }

        @NotNull @Override public Collection<StructureViewTreeElement> getChildrenBase() {
            return empty();
        }

        @Nullable @Override public String getPresentableText() {
            final F element = getElement();
            return element != null ? cutPostfix(element.getName(), SEMICOLON) : Constants.INVALID;
        }
    }

    static class WidgetTreeElement extends MetaModelFieldTreeElement<PsiWidget> {
        WidgetTreeElement(PsiModelField widget) {
            super((PsiWidget) widget);
        }

        @Override public String getPresentableText() {
            final PsiWidget element = getElement();
            return element != null ? element.getName() + ":" + element.getWidgetType() : Constants.INVALID;
        }
    }
}  // end class MetaModelTreeElement
