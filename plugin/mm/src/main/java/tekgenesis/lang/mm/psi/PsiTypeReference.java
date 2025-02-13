
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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.equal;

/**
 * An element representing a reference to a Type.
 */
public class PsiTypeReference extends ElementWithReferences implements PsiMetaModelCodeReference {

    //~ Constructors .................................................................................................................................

    /** Creates a type reference. */
    PsiTypeReference(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public PsiElement bindToElement(@NotNull PsiElement element)
        throws IncorrectOperationException
    {
        return null;
    }

    @Override public PsiElement handleElementRename(String name) {
        replaceChild(getLastChildNode(), MetaModelElementFactory.createIdentifier(getProject(), name).getNode());
        return this;
    }

    @Override public PsiElement resolve() {
        return resolve(false);
    }

    @Nullable @Override public PsiElement resolve(boolean incompleteCode) {  //
        return getAncestor(PsiUiModel.class).flatMap(m -> resolveUiModelWidgetBinding()).getOrNull();
    }

    @NotNull @Override public String getCanonicalText() {
        return getText();
    }

    @Override public boolean isQualified() {
        return getQualifier() != null;
    }

    @Override public PsiTypeReference getElement() {
        return this;
    }

    @Override public boolean isReferenceTo(PsiElement e) {
        boolean result = false;

        final PsiElement element = e instanceof MMIdentifier ? e.getParent().getParent() : e;

        if (element instanceof PsiModelField) result = resolveUiModelWidgetBinding().map(b -> equal(element, b)).orElse(false);
        else if (element instanceof PsiMetaModel) result = resolveMetaModel(getDomain(), getText()).map(m -> equal(element, m)).orElse(false);

        return result;
    }

    @Nullable @Override public PsiElement getQualifier() {
        return null;
    }

    @Override public TextRange getRangeInElement() {
        return getEntireTextRange();
    }

    @Override public PsiReference getReference() {
        return this;
    }

    @Nullable @Override public String getReferenceName() {
        return null;
    }

    @Override public boolean isSoft() {
        return false;
    }

    @NotNull @Override public Object[] getVariants() {
        return TreeElement.EMPTY_ARRAY;
    }

    @Nullable private PsiMetaModel<?> resolveJar() {
        final ModelRepository   libRep      = getLibRepository();
        final Option<MetaModel> modelOption = libRep.getModel(QName.createQName(getText()));
        if (modelOption.isPresent()) {
            final MetaModel     model        = modelOption.get();
            final String        pathName     = model.getSourceName().substring(5);
            final String        entityName   = getName().split("\\.")[getName().split("\\.").length - 1];
            final VirtualFile[] libraryRoots = getModule().getOrFail("No Module found").getComponent(MMModuleComponent.class).getLibraryRoots();
            final PsiFile       psiFile      = PsiUtils.findChildInRoots(getProject(), libraryRoots, pathName);
            if (psiFile != null) return ((MMFile) psiFile).getMetaModel(entityName).orElse(null);
        }
        return null;
    }  // end method resolveJar

    @Nullable private Option<PsiElement> resolveUiModelWidgetBinding() {
        return getAncestor(PsiWidget.class).map(PsiWidget::getBinding);
    }
}  // end class PsiTypeReference
