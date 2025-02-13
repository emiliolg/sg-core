
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.List;
import java.util.function.Predicate;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.intellij.CommonCompositeElement;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.errors.MMBuilderErrorListener;
import tekgenesis.lang.mm.injection.MetaModelLanguageInjector;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.mmcompiler.builder.QContext;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.lang.mm.psi.PsiUtils.findMMFileIncludingNonProjectItems;

/**
 * Common PsiElement for composite nodes.
 */
public class MMCommonComposite extends CommonCompositeElement<MetaModelAST, MMElementType, MMToken>
    implements PsiNamedElement, MetaModelAST, MetaModelPsiElement
{

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    MMCommonComposite(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        // Avoid updating model repository for injected code
        if (isNotInjected()) updateModelRepository(holder);
    }

    /** Gets ancestor of type T if available, or none. */
    @NotNull public <T extends PsiElement> Option<T> getAncestor(@NotNull final Class<T> ancestor) {
        return getAncestor(ancestor::isInstance).castTo(ancestor);
    }

    /** Get Meta Model containing file. */
    @Override public MMFile getContainingFile() {
        return cast(super.getContainingFile());
    }

    @NotNull @Override public MetaModelAST getEffectiveNode() {
        if (getType().isWrapper()) {
            final PsiElement child = getFirstChild();
            if (child instanceof MetaModelAST) return cast(child);
        }
        return this;
    }

    /** Gets empty node. */
    public MMLeafElement getEmptyNode() {
        return MMLeafElement.EMPTY;
    }

    /** Get full qualification name. */
    public String getFullName() {
        return qualify(getDomain(), getName());
    }

    /** Get element label node. */
    public PsiElement getIdentifier() {
        final PsiElement labeledId = findPsiChildByType(MMElementType.LABELED_ID);
        return labeledId != null ? labeledId.getFirstChild() : this;
    }

    @NotNull @Override public String getName() {
        final PsiElement identifier = getIdentifier();
        return identifier != null ? identifier.getText() : "";
    }

    @Override public PsiElement setName(@NonNls @NotNull String name)
        throws IncorrectOperationException
    {
        final PsiElement identifier  = getIdentifier();
        final ASTNode    childByType = findChildByType(MMElementType.LABELED_ID);
        if (identifier != null && childByType != null)

            childByType.replaceChild(identifier.getNode(), MetaModelElementFactory.createIdentifier(getProject(), name).getNode());
        return this;
    }

    @NotNull @Override public PsiElement getNavigationElement() {
        final PsiElement identifier = getIdentifier();
        return identifier == null ? this : identifier;
    }
    @Override public ItemPresentation getPresentation() {
        return new MMCommonCompositePresentation(this);
    }

    /** Return file qualification context. */
    @NotNull public QContext getQContext() {
        return getContainingFile().getQContext();
    }

    /** Resolve PsiMetaModel which matches given name (if any!). */
    @NotNull Option<PsiMetaModel<?>> resolveMetaModel(@NotNull final String fqn) {
        return getPsiFromMetaModel(getModelRepository().getModel(createQName(fqn)));
    }

    /** Resolve PsiMetaModel which matches given name (if any!). */
    @NotNull Option<PsiMetaModel<?>> resolveMetaModel(@NotNull final String domain, @NotNull final String name) {
        return getPsiFromMetaModel(getModelRepository().getModel(domain, name));
    }

    /** gets all Identifiers for a MMCommonComposite(useful for finding usages). */
    void getAllIdentifiers(List<MMIdentifier> identifiers) {
        for (final PsiElement element : getChildren()) {
            if (element instanceof MMCommonComposite) {
                final MMCommonComposite mmCommonComposite = (MMCommonComposite) element;
                final ASTNode           childByType       = mmCommonComposite.findChildByType(MMElementType.IDENTIFIER);
                if (childByType instanceof MMIdentifier) {
                    final MMIdentifier identifier = (MMIdentifier) childByType;
                    identifiers.add(identifier);
                }
                if (element.getChildren().length != 0) mmCommonComposite.getAllIdentifiers(identifiers);
            }
        }
    }

    /** Gets ancestor of type T if available, or none. */
    @NotNull Option<PsiElement> getAncestor(Predicate<PsiElement> p) {
        PsiElement parent = getParent();
        while (parent != null) {
            if (p.test(parent)) return some(parent);
            else parent = parent.getParent();
        }
        return empty();
    }

    boolean isNotInjected() {
        return MetaModelLanguageInjector.isNotInjected(getContainingFile());
    }

    /** Return LibRepository for current module. */
    @NotNull ModelRepository getLibRepository() {
        final Option<Module> module = getModule();
        return module.isPresent() ? PsiUtils.getLibRepository(module.get()) : new ModelRepository();
    }

    private void updateModelRepository(@NotNull AnnotationHolder holder) {
        final ModelRepository repository = holder.getCurrentAnnotationSession().getUserData(MODEL_KEY);

        if (repository == null) {
            final ModelRepository updated = updateRepository(holder);
            holder.getCurrentAnnotationSession().putUserData(MODEL_KEY, updated);
        }
    }

    @NotNull private ModelRepository updateRepository(AnnotationHolder holder) {
        final Option<Module> module     = getModule();
        ModelRepository      repository = new ModelRepository();
        if (module.isPresent()) {
            final MMModuleComponent component = module.get().getComponent(MMModuleComponent.class);
            repository = component.getRepository();
            final VirtualFile moduleFile = module.get().getModuleFile();
            if (moduleFile != null && moduleFile.getParent() != null) {
                final BuilderFromAST b = new BuilderFromAST(repository, new MMBuilderErrorListener(holder));
                b.build(getContainingFile().getPath(), getContainingFile().getFirstRoot());
            }
        }
        return repository;
    }

    private Option<PsiMetaModel<?>> getPsiFromMetaModel(@NotNull final Option<MetaModel> option) {
        if (option.isEmpty()) return empty();
        return getModule().map(module -> findPsiMetaModel(option.get(), module, getProject()));
    }

    //~ Methods ......................................................................................................................................

    /** Find PsiMetaModel including non project items. */
    @Nullable public static PsiMetaModel<?> findPsiMetaModel(@NotNull MetaModel mm, @NotNull Module module, @NotNull Project project) {
        return findMMFileIncludingNonProjectItems(project, module, mm.getSourceName()).flatMap(f -> f.getMetaModel(mm.getName())).getOrNull();
    }

    //~ Static Fields ................................................................................................................................

    private static final Key<ModelRepository> MODEL_KEY = Key.create("model.repo");
}  // end class MMCommonComposite
