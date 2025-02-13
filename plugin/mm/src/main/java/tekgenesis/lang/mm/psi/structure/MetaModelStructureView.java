
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.structure.MetaModelTreeElement.MetaModelFieldTreeElement;

class MetaModelStructureView extends TextEditorBasedStructureViewModel implements StructureViewModel.ElementInfoProvider {

    //~ Constructors .................................................................................................................................

    MetaModelStructureView(MMFile psiFile) {
        super(psiFile);
    }

    //~ Methods ......................................................................................................................................

    @Override public Object getCurrentEditorElement() {
        return getPsiFile().findElementAt(getEditor().getCaretModel().getOffset());
    }

    @Override public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof MetaModelFieldTreeElement;
    }

    @NotNull @Override public StructureViewTreeElement getRoot() {
        return new MetaModelFileTreeElement((MMFile) getPsiFile());
    }

    @Override public boolean isAlwaysShowsPlus(StructureViewTreeElement structureViewTreeElement) {
        return false;
    }

    //~ Methods ......................................................................................................................................

    static String cutPostfix(@NotNull String s, @NotNull String postfix) {
        if (s.endsWith(postfix)) return s.substring(0, s.length() - postfix.length());
        else return s;
    }

    //~ Static Fields ................................................................................................................................

    static final String SEMICOLON = ";";
}
