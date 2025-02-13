
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.MMLanguage;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.ast.MetaModelASTImpl;
import tekgenesis.mmcompiler.builder.QContext;
import tekgenesis.parser.Position;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.collections.Colls.seq;

/**
 * Psi Element for MetaModel Files.
 */
public class MMFile extends PsiFileBase {

    //~ Instance Fields ..............................................................................................................................

    private final CachedValue<QContext> context;

    //~ Constructors .................................................................................................................................

    /** Create EntityFile. */
    public MMFile(FileViewProvider viewProvider) {
        super(viewProvider, MMLanguage.INSTANCE);
        context = CachedValuesManager.getManager(myManager.getProject()).createCachedValue(new QContextCacheBuilder(this), false);
    }

    //~ Methods ......................................................................................................................................

    @Override public void accept(@NotNull PsiElementVisitor visitor) {
        visitor.visitFile(this);
    }

    /** replace text in MM file for the given offsets.* */
    public void replaceText(int startOffset, int endOffset, String replaceString) {
        final PrintWriter writer;
        try {
            writer = new PrintWriter(new File(getPath()));
        }
        catch (final FileNotFoundException e) {
            return;
        }
        final StringBuilder sb       = new StringBuilder();
        final String        fileText = getText();
        if (startOffset > 1) sb.append(fileText.substring(0, startOffset));
        sb.append(replaceString);
        sb.append(fileText.substring(endOffset + 1, fileText.length()));
        writer.write(sb.toString());
        writer.flush();
    }

    @Override public String toString() {
        return "File: " + getName();
    }

    /** Return any node with the given name. */
    @Nullable public MetaModelAST getAnyNode(String name) {
        final PsiMetaModel<?> element = getMetaModel(name).orElse(null);

        if (element != null) return element;

        MetaModelAST result;
        for (final PsiEntity entity : getEntities()) {
            result = entity.getFieldNullable(name);
            if (result != null) return result;
        }

        for (final PsiForm form : getForms()) {
            result = form.getFieldNullable(name);
            if (result != null) return result;
        }

        for (final PsiView view : getViews()) {
            result = view.getFieldNullable(name);
            if (result != null) return result;
        }

        return PsiUtils.findAnyNodeInFileByName(this, name);
    }  // end method getAnyNode

    /** Get all cases defined in the file. */
    public PsiCase[] getCases() {
        return findChildrenByClass(PsiCase.class);
    }

    /** Get all components defined in the file. */
    public PsiWidgetDef[] getComponents() {
        return findChildrenByClass(PsiWidgetDef.class);
    }

    /** Get package defined in the file. */
    @Nullable public PsiDomain getDomain() {
        final PsiDomain[] domains = findChildrenByClass(PsiDomain.class);
        assert domains.length <= 1 : "At most one domain must be defined";
        return domains.length > 0 ? domains[0] : null;
    }

    /** Return file domain text. */
    @NotNull public String getDomainText() {
        final PsiDomain domain = getDomain();
        return domain != null ? domain.getDomain() : "";
    }

    /** Get all entities defined in the file. */
    public PsiEntity[] getEntities() {
        return findChildrenByClass(PsiEntity.class);
    }

    /** Find an Entity by the name. */
    @Nullable public PsiEntity getEntity(@NotNull final String name) {
        return findEntity(name);
    }

    /** Find Enum by Name. */
    @Nullable public PsiEnum getEnum(@NotNull final String name) {
        return getMetaModel(PsiEnum.class, name);
    }

    /** Get all enums defined in the file. */
    public PsiEnum[] getEnums() {
        return findChildrenByClass(PsiEnum.class);
    }

    @NotNull public FileType getFileType() {
        return MMFileType.INSTANCE;
    }

    /** get first Root for the specified MMFile. */
    public MetaModelAST getFirstRoot() {
        final PsiFile[] roots = getPsiRoots();

        final List<MetaModelAST> nodes = new ArrayList<>();
        final FileASTNode        node  = roots[0].getNode();
        for (ASTNode n = node.getFirstChildNode(); n != null; n = n.getTreeNext()) {
            if (n instanceof MetaModelAST) {
                final MetaModelAST m = (MetaModelAST) n;
                if (!((MetaModelAST) n).getType().isIgnorable()) nodes.add(m);
            }
        }
        return new MetaModelASTImpl(MMToken.FILE, "", Position.OffsetPosition.ZERO, nodes);
    }

    /** Find an Form by the name. */
    @Nullable public PsiForm getForm(@NotNull final String name) {
        return getMetaModel(PsiForm.class, name);
    }

    /** Get all forms defined in the file. */
    public PsiForm[] getForms() {
        return findChildrenByClass(PsiForm.class);
    }

    /** Get all handlers defined in the file. */
    public PsiHandler[] getHandlers() {
        return findChildrenByClass(PsiHandler.class);
    }

    /** Find a Menu by the name. */
    @Nullable public PsiMenu getMenu(@NotNull final String name) {
        return getMetaModel(PsiMenu.class, name);
    }

    /** Get all menus defined in the file. */
    public PsiMenu[] getMenus() {
        return findChildrenByClass(PsiMenu.class);
    }

    /** Return PsiMetaModel that matches given meta model name (if any). */
    public Option<PsiMetaModel<?>> getMetaModel(@NotNull final String name) {
        final PsiMetaModel<?> mm = getMetaModel(PsiMetaModel.class, name);
        return Option.ofNullable(mm);
    }

    /** Get all meta models in file. */
    @NotNull public Seq<PsiMetaModel<? extends MetaModel>> getMetaModels() {
        final List<PsiMetaModel<?>> result = new ArrayList<>();
        for (final PsiMetaModel<?> model : findChildrenByClass(PsiMetaModel.class)) {
            result.add(model);
            if (model instanceof PsiEntity) findInners(result, (PsiEntity) model);
        }
        return seq(result);
    }

    /** Return ModelRepository for current module. */
    @NotNull public ModelRepository getModelRepository() {
        return PsiUtils.getModelRepository(this).orElseThrow(IllegalStateException::new);
    }
    /** Get models by Type. */
    public <M extends PsiMetaModel<?>> M[] getModelsByType(Class<M> clazz) {
        return findChildrenByClass(clazz);
    }
    /** gets Path for the specified MMFile. */
    public String getPath() {
        return getViewProvider().getVirtualFile().getPath();
    }

    /** Returns the path to a file's source root. */
    @NotNull public String getPathToSourceRoot() {
        return PsiUtils.getPathToSourceRoot(this);
    }

    /** Return qualification context for file. */
    @NotNull public QContext getQContext() {
        return context.getValue();
    }

    /** Get all roles defined in the file. */
    public PsiRole[] getRoles() {
        return findChildrenByClass(PsiRole.class);
    }

    /** Get all tasks defined in the file. */
    public PsiTask[] getTasks() {
        return findChildrenByClass(PsiTask.class);
    }

    /** Find a Type by the name. For test purposes. */
    @Nullable public PsiType getType(@NotNull final String name) {
        return getMetaModel(PsiType.class, name);
    }

    /** Get all tasks defined in the file. */
    public PsiType[] getTypes() {
        return findChildrenByClass(PsiType.class);
    }

    /** Get all uiModels defined in the file. */
    public PsiUiModel<? extends UiModel>[] getUiModels() {
        return cast(findChildrenByClass(PsiUiModel.class));
    }

    /** Find an View by the name. */
    @Nullable public PsiView getView(String name) {
        return getMetaModel(PsiView.class, name);
    }

    /** Get all views defined in the file. */
    public PsiView[] getViews() {
        return findChildrenByClass(PsiView.class);
    }

    @Nullable private PsiEntity findEntity(@NotNull final String name) {
        PsiEntity result = null;
        for (final PsiEntity entity : getEntities()) {
            result = findEntity(name, entity);
            if (result != null) break;
        }
        return result;
    }

    @Nullable private PsiEntity findEntity(@NotNull final String name, @NotNull final PsiEntity entity) {
        PsiEntity result = null;
        if (name.equals(entity.getName())) result = entity;
        else {
            for (final PsiEntityField field : entity.getFields()) {
                for (final PsiEntity inner : field.asInnerEntity())
                    result = findEntity(name, inner);
                if (result != null) break;
            }
        }
        return result;
    }  // end method findEntity

    private void findInners(List<PsiMetaModel<?>> result, PsiEntity entity) {
        for (final PsiEntityField field : entity.getFields()) {
            for (final PsiEntity inner : field.asInnerEntity()) {
                findInners(result, inner);
                result.add(inner);
            }
        }
    }

    @Nullable private <T extends PsiMetaModel<?>> T getMetaModel(Class<T> clazz, String name) {
        T result = null;
        for (final T model : findChildrenByClass(clazz)) {
            // Check name on top level objects
            if (equal(name, model.getName())) result = model;
            else if (model instanceof PsiEntity)
            // Recursively only for inner entities!
            result = cast(findEntity(name, (PsiEntity) model));
            if (result != null) break;
        }
        return result;
    }

    //~ Inner Classes ................................................................................................................................

    private static class QContextCacheBuilder implements CachedValueProvider<QContext> {
        @NotNull private final MMFile file;

        QContextCacheBuilder(@NotNull MMFile file) {
            this.file = file;
        }

        @Override public Result<QContext> compute() {
            return Result.create(new QContext(computeDomain(), "", computeImports()), file);
        }

        @NotNull private String computeDomain() {
            final PsiDomain domain = file.getDomain();
            return domain != null ? domain.getDomain() : "";
        }

        @NotNull private List<String> computeImports() {
            final PsiImport[] imports = file.findChildrenByClass(PsiImport.class);
            return Stream.of(imports).map(PsiImport::getImportReferenceText).collect(Collectors.toList());
        }
    }
}  // end class MMFile
