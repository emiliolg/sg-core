
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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.util.ArrayFactory;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.documentationProvider.Documentable;
import tekgenesis.lang.mm.documentationProvider.MMDocumentationProvider;
import tekgenesis.lang.mm.gutter.GutterManager;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;

/**
 * Abstract Class for Psi MetaModel.
 */
public abstract class PsiMetaModel<T extends MetaModel> extends PsiMetaModelMember implements Documentable {

    //~ Instance Fields ..............................................................................................................................

    private final MetaModelKind kind;
    private final Class<T>      metaModelClass;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiMetaModel(@NotNull final MMElementType t, @NotNull final MetaModelKind kind, @NotNull final Class<T> metaModelClass) {
        super(t);
        this.kind           = kind;
        this.metaModelClass = metaModelClass;
    }

    //~ Methods ......................................................................................................................................

    public void delete()
        throws IncorrectOperationException
    {
        final ASTNode parentNode = getParent().getNode();
        assert parentNode != null;

        final ASTNode node = getNode();
        final ASTNode prev = node.getTreePrev();
        final ASTNode next = node.getTreeNext();
        parentNode.removeChild(node);
        if ((prev == null || prev.getElementType() == TokenType.WHITE_SPACE) && next != null && next.getElementType() == TokenType.WHITE_SPACE)
            parentNode.removeChild(next);

        for (final MMIdentifier identifier : findUsages(parentNode.getText()))
            identifier.delete();
    }

    /** finds usages for a field name. */
    public List<MMIdentifier> findUsages(String name) {
        final List<MMIdentifier> identifiersModel = new ArrayList<>();
        getAllIdentifiers(identifiersModel);

        final List<MMIdentifier> usages = new ArrayList<>();
        for (final MMIdentifier identifier : identifiersModel) {
            if (name.equals(identifier.getText())) usages.add(identifier);
        }
        return usages;
    }

    /** Checks if the model has a linked form with the given name. */
    public boolean hasFormWithName(final String formName) {
        final ModelRepository modelRepository = getModelRepository();
        final Option<Form>    formOption      = modelRepository.getModel(formName, Form.class);
        return formOption.isPresent();
    }

    /** Checks if the model has a linked widget with the given name. */
    public boolean hasWidgetWithName(final String widgetName) {
        return getModelRepository().getModel(widgetName, WidgetDef.class).isPresent();
    }
    /** Gets Description to be shown in Documentation. */
    public String getDocumentation() {
        final Option<MMComment> comment = findComment();
        if (comment.isPresent()) return comment.get().getUnCommentedText();
        return getDefaultDocumentation();
    }

    /** To make safe delete available for this kind of Meta Model. */
    public boolean isSafeDeleteAvailable() {
        return true;
    }
    /** Get Meta Model field. */

    @NotNull public Option<PsiModelField> getField(String fieldName) {
        return Option.ofNullable(getFieldNullable(fieldName));
    }

    /** Get Meta Model field. */
    @Nullable public abstract PsiModelField getFieldNullable(String fieldName);

    /** Get Meta Model fields. */
    @NotNull public abstract PsiModelField[] getFields();

    /** Get meta model full qualification name. */
    @NotNull public QName getFqn() {
        return QName.createQName(getDomain(), getName());
    }

    /** Return metamodel high level option node. For test purposes. */
    @Nullable public MMCommonComposite getHighLevelOption(MMToken token) {
        final ASTNode result = findChildByType(MMElementType.forToken(token));
        return (MMCommonComposite) result;
    }

    /** gets Line Marker Info for Model. */
    @NotNull public List<PsiClass> getLineMarkerTargets() {
        final PsiClass clazz = GutterManager.getPsiClass(this);
        if (clazz == null) return ImmutableList.empty();

        final ASTNode           doc  = findChildByType(MMElementType.DOCUMENTATION);
        final MMCommonComposite node = doc != null ? (MMCommonComposite) findChildByType(MMElementType.MODEL) : this;
        if (node == null) return ImmutableList.empty();

        return ImmutableList.of(clazz);
    }

    /** Return MetaModel Kind. */
    public final MetaModelKind getMetaModelKind() {
        return kind;
    }

    /** Returns the repository Meta Model. */
    public final Option<T> getModel() {
        return getModelRepository().getModel(getDomain(), getName(), metaModelClass);
    }

    /** Returns the repository Meta Model. */
    @Nullable public final T getModelOrNull() {
        return getModel().getOrNull();
    }

    /** Return true if model is inner. */
    public boolean isInner() {
        return PsiUtils.findParentField(this).isPresent();
    }

    /** To make safe delete available on this kind of Meta Model. */
    public boolean isValidElement() {
        return true;
    }

    boolean hasDefaultForm() {
        return hasFormWithName(getFullName() + "Form");
    }

    @Nullable <F extends PsiModelField> F getPsiModelField(String name, F[] fields) {
        for (final F field : fields)
            if (name.equals(field.getName())) return field;
        return null;
    }

    <F extends PsiModelField> F[] getPsiModelFields(MMElementType listType, MMElementType fieldType, ArrayFactory<F> factory, F[] emptyModelFields) {
        final MMCommonComposite list = (MMCommonComposite) findPsiChildByType(listType);
        if (list != null) return list.getChildrenAsPsiElements(fieldType, factory);
        return emptyModelFields;
    }

    private Option<MMComment> findComment() {
        PsiElement prevSibling = getPrevSibling();
        while (prevSibling != null && !(prevSibling instanceof MMCommonComposite)) {
            if (prevSibling instanceof MMComment) return Option.some((MMComment) prevSibling);
            else prevSibling = prevSibling.getPrevSibling();
        }
        return Option.empty();
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String getDefaultDocumentation() {
        final Option<? extends MetaModel> model = getModel();
        if (model.isPresent()) {
            final MetaModel     metaModel          = model.get();
            final StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("Domain : ")
                .append(metaModel.getDomain())
                .append("\n Name: ")
                .append(metaModel.getName())
                .append("\n Kind: ")
                .append(metaModel.getMetaModelKind());
            return descriptionBuilder.toString();
        }
        else return MMDocumentationProvider.NO_DOCUMENTATION_FOUND;
    }

    //~ Static Fields ................................................................................................................................

    static final PsiModelField[] EMPTY_MODEL_FIELDS = {};
}  // end class PsiMetaModel
