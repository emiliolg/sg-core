
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.impl.source.resolve.ResolveCache.AbstractResolver;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.lang.mm.psi.MetaModelElementFactory.createIdentifier;

/**
 * Actual reference for any {@link ElementWithReferences}.
 */
abstract class AbstractReference implements PsiMetaModelCodeReference {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final PsiMetaModelCodeReferenceElement underlying;

    //~ Constructors .................................................................................................................................

    protected AbstractReference(@NotNull PsiMetaModelCodeReferenceElement underlying) {
        this.underlying = underlying;
    }

    protected AbstractReference(@NotNull String text, @NotNull TextRange range, @NotNull ElementWithReferences underlying) {
        this.underlying = underlying;
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiElement bindToElement(@NotNull PsiElement e)
        throws IncorrectOperationException
    {
        return null;
    }

    /** Overridden to allow reference caching based on underlying element! */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractReference that = (AbstractReference) o;
        return that.getElement().equals(getElement());
    }

    @Override public PsiElement handleElementRename(String name)
        throws IncorrectOperationException
    {
        final PsiMetaModelCodeReferenceElement element = getElement();

        final PsiElement previous = element.getReferenceNameElement();
        if (previous == null) throw new IncorrectOperationException();

        final PsiElement identifier = createIdentifier(element.getProject(), name);
        previous.replace(identifier);
        return element;
    }

    /** Overridden to allow reference caching based on underlying element! */
    @Override public int hashCode() {
        return getElement().hashCode();
    }

    @Nullable @Override public PsiElement resolve() {
        return resolve(false);
    }

    @Nullable @Override public final PsiElement resolve(boolean incompleteCode) {
        final PsiMetaModelCodeReferenceElement element = getElement();
        final MMFile                           file    = element.getContainingFile();
        final ResolveResult                    result  = ResolveCache.getInstance(file.getProject())
                                                         .resolveWithCaching(this, Resolver.INSTANCE, false, incompleteCode);
        return result != null ? result.getElement() : null;
    }

    @NotNull public abstract Option<? extends PsiElement> resolveInner();

    @NotNull @Override public String getCanonicalText() {
        final PsiElement target = resolve();
        if (target instanceof PsiMetaModel) return ((PsiMetaModel<?>) target).getFqn().getFullName();
        return notNull(getReferenceName());
    }

    @Override public PsiMetaModelCodeReferenceElement getElement() {
        return underlying;
    }

    @Override public boolean isReferenceTo(PsiElement element) {
        return getElement().getManager().areElementsEquivalent(resolve(), element);
    }

    @Override public TextRange getRangeInElement() {
        final Option<ASTNode> reference = getElement().getReferenceNameNode();

        if (reference.isEmpty()) return new TextRange(0, getElement().getTextLength());

        final TreeElement identifier = (TreeElement) reference.get();
        final int         offset     = identifier.getStartOffsetInParent();
        return new TextRange(offset, offset + identifier.getTextLength());
    }

    @Override public boolean isSoft() {
        return false;
    }

    @NotNull protected Project getProject() {
        return getElement().getProject();
    }

    //~ Static Fields ................................................................................................................................

    private static final ResolveResult EMPTY = new ResolveResult() {
            @Override public PsiElement getElement() {
                return null;
            }
            @Override public boolean isValidResult() {
                return false;
            }
        };

    //~ Inner Classes ................................................................................................................................

    private static final class Resolver implements AbstractResolver<PsiMetaModelCodeReference, ResolveResult> {
        @Override public ResolveResult resolve(@NotNull PsiMetaModelCodeReference referenceElement, boolean incompleteCode) {
            final AbstractReference reference = (AbstractReference) referenceElement;
            return reference.resolveInner().map(PsiElementResolveResult::new).castTo(ResolveResult.class).orElse(EMPTY);
        }

        private static final Resolver INSTANCE = new Resolver();
    }
}  // end class AbstractReference
