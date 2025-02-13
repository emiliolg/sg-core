
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
import java.util.List;
import java.util.function.Function;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiModelField;
import tekgenesis.lang.mm.psi.structure.MetaModelTreeElement.EntityFieldTreeElement;
import tekgenesis.lang.mm.psi.structure.MetaModelTreeElement.HandlerFieldTreeElement;
import tekgenesis.lang.mm.psi.structure.MetaModelTreeElement.MetaModelFieldTreeElement;
import tekgenesis.lang.mm.psi.structure.MetaModelTreeElement.WidgetTreeElement;

import static tekgenesis.common.collections.ImmutableList.fromArray;

class MetaModelFileTreeElement extends PsiTreeElementBase<MMFile> {

    //~ Constructors .................................................................................................................................

    MetaModelFileTreeElement(MMFile psiFile) {
        super(psiFile);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Collection<StructureViewTreeElement> getChildrenBase() {
        final List<StructureViewTreeElement> children = new ArrayList<>();
        final MMFile                         element  = getElement();
        if (element != null) {
            addMetaModels(children, element.getEntities(), EntityFieldTreeElement::new);
            addMetaModels(children, element.getEnums(), MetaModelFieldTreeElement::new);
            addMetaModels(children, element.getForms(), WidgetTreeElement::new);
            addMetaModels(children, element.getComponents(), WidgetTreeElement::new);
            addMetaModels(children, element.getViews(), EntityFieldTreeElement::new);
            addMetaModels(children, element.getMenus(), MetaModelFieldTreeElement::new);
            addMetaModels(children, element.getRoles(), MetaModelFieldTreeElement::new);
            addMetaModels(children, element.getTasks(), MetaModelFieldTreeElement::new);
            addMetaModels(children, element.getHandlers(), HandlerFieldTreeElement::new);
            addMetaModels(children, element.getCases(), HandlerFieldTreeElement::new);
            addMetaModels(children, element.getTypes(), EntityFieldTreeElement::new);
        }

        return children;
    }

    @Override public String getPresentableText() {
        final MMFile element = getElement();
        return element != null ? element.toString() : Constants.INVALID;
    }

    private void addMetaModels(@NotNull final List<StructureViewTreeElement> children, @NotNull final PsiMetaModel<?>[] models,
                               @NotNull final Function<PsiModelField, StructureViewTreeElement> generator) {  //
        fromArray(models).map(m -> new MetaModelTreeElement(m, generator)).forEach(children::add);
    }
}  // end class MetaModelFileTreeElement
