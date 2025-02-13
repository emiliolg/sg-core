
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.mmcompiler.builder.QContext;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.lang.mm.completion.Imports.addMetaModelImport;
import static tekgenesis.lang.mm.psi.MetaModelReferences.*;
import static tekgenesis.lang.mm.psi.PsiUtils.findPsiMetaModel;

/**
 * Reference to a MetaModel.
 */
public class MetaModelReference extends AbstractReference {

    //~ Constructors .................................................................................................................................

    MetaModelReference(@NotNull PsiMetaModelCodeReferenceElement underlying) {
        super(underlying);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiMetaModelCodeReferenceElement bindToElement(@NotNull PsiElement element)
        throws IncorrectOperationException
    {
        if (element instanceof PsiMetaModel) return bindToMetaModel((PsiMetaModel<?>) element);
        throw cannotBindError(element);
    }

    @NotNull @Override public Option<PsiMetaModel<?>> resolveInner() {
        final PsiMetaModelCodeReferenceElement e    = getElement();
        final QContext                         c    = e.getQContext();
        final MetaModelReferenceKind           kind = getReferenceKind(e);

        switch (kind) {
        case CLASS_FQ_NAME_KIND:
            return isReferenceName(e) ? resolveMetaModel(e, c, false) : resolvePackage(e);
        case CLASS_NAME_KIND:
            return isReferenceName(e) ? resolveMetaModel(e, c, true) : resolvePackage(e);
        case PACKAGE_NAME_KIND:
            return resolvePackage(e);
        }

        return Option.empty();
    }

    @Override public boolean isReferenceTo(PsiElement e) {
        final PsiElement element = e instanceof MMIdentifier ? e.getParent().getParent() : e;
        return element instanceof PsiMetaModel && super.isReferenceTo(element);
    }

    @NotNull @Override public Object[] getVariants() {
        return EMPTY_ARRAY;
    }

    private PsiMetaModelCodeReferenceElement bindToMetaModel(PsiMetaModel<?> model) {
        final PsiMetaModelCodeReferenceElement underlying = getElement();
        addMetaModelImport(underlying.getContainingFile(), model);
        return underlying;
    }

    private IncorrectOperationException cannotBindError(@NotNull PsiElement element) {
        return new IncorrectOperationException("Cannot bind to " + element);
    }

    private Option<PsiMetaModel<?>> resolveMetaModel(@NotNull PsiMetaModelCodeReferenceElement e, @NotNull QContext c, boolean usingContext) {
        final String text = getReferenceText(e);
        if (Strings.isBlank(text)) return Option.empty();
        final QName fqn = usingContext ? c.resolve(text) : createQName(text);

        final Module module = PsiUtils.getModule(e);

        return module == null ? Option.empty() : findPsiMetaModel(getProject(), module, e.getModelRepository(), fqn);
    }

    private Option<PsiMetaModel<?>> resolvePackage(PsiMetaModelCodeReferenceElement e) {
        return Option.empty();  // todo implement mm package navigation
    }
}  // end class MetaModelReference
