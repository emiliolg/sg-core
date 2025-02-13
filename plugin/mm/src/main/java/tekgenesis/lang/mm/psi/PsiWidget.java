
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.Set;

import javax.swing.*;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.gutter.GutterManager;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.type.ModelType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.codegen.common.MMCodeGenConstants.ROW_CLASS_SUFFIX;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.*;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.lang.mm.MMElementType.WIDGET_TYPE;
import static tekgenesis.lang.mm.actions.AddButtonsToStaticTableAction.ADD_ROW;
import static tekgenesis.lang.mm.actions.AddButtonsToStaticTableAction.REMOVE_ROW;
import static tekgenesis.lang.mm.gutter.GutterManager.IMPLEMENTED_IN;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassForFqnNullable;
import static tekgenesis.metadata.form.widget.WidgetType.BUTTON;
import static tekgenesis.metadata.form.widget.WidgetType.TABLE;

/**
 * A Widget.
 */
@SuppressWarnings({ "UnnecessaryFullyQualifiedName", "DuplicateStringLiteralInspection" })
public class PsiWidget extends PsiModelField {

    //~ Constructors .................................................................................................................................

    /** Creates a widget. */
    public PsiWidget(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);

        final WidgetType type = getWidgetType();

        if (type != null && type.isMultiple()) {
            final PsiClass modelClass = resolveJavaClass();
            if (getMultiplePsiClass() != null || modelClass == null) return;

            getFormFieldIdentifier().ifPresent(id -> {
                // Annotate error...
                final Annotation annotation        = holder.createErrorAnnotation(id,
                        MSGS.missingTableAnnotation(getName(), getUiModel().getFullName()));
                final String     multipleClassName = getMultipleClassName(getNodePlainText(id));
                annotation.registerFix(CreateMissingInnerClassIntention.create(modelClass, multipleClassName, multipleClassName + BASE));
            });
        }
    }

    /** Find field widget type (if defined). */
    @Nullable public PsiWidgetType findWidgetType() {
        return (PsiWidgetType) findChildByType(WIDGET_TYPE);
    }

    /** returns if a table form field has buttons. */
    public boolean hasButton(Seq<PsiWidgetType> tableButtons, String argument) {
        return hasParametrizedButton(tableButtons, argument, null);
    }

    /** returns if a table form field has buttons parametrized with the table name. */
    public boolean hasParametrizedButton(Seq<PsiWidgetType> buttons, String argument, @Nullable String tableName) {
        if (!isTable()) throw new UnsupportedOperationException();

        for (final PsiWidgetType button : buttons) {
            final Set<String> arguments = button.getWidgetTypeArguments();
            if (arguments.contains(argument) && (Predefined.isEmpty(tableName) || arguments.contains(tableName))) return true;
        }
        return false;
    }

    /**
     * Return whether a table should let the user generate its buttons or not. Throws exception if
     * formField is not a table
     */
    public boolean shouldAppearIntention() {
        if (isTable()) {
            final Seq<PsiWidget> modelWidgets = getUiModel().getWidgets();
            final Seq<PsiWidget> tableWidgets = modelWidgets.filter(formField -> formField != null && formField.isTable());

            final Seq<PsiWidgetType> tableButtons = tableButtonsFromPsiUiModelWidgets(modelWidgets);

            return !(tableWidgets.size() == 1 && hasButton(tableButtons, ADD_ROW) && hasButton(tableButtons, REMOVE_ROW) ||
                     hasParametrizedButton(tableButtons, ADD_ROW, getName()) && hasParametrizedButton(tableButtons, REMOVE_ROW, getName()));
        }
        else throw new UnsupportedOperationException();
    }

    /** Return binding for form field (if any). */
    @Nullable public PsiModelField getBinding() {
        return getBindingFqn().flatMap(fqn -> resolveMetaModel(fqn.getQualification()))  //
               .flatMap(m -> getBoundMetaModel(getBindingFqn().get(), m))                //
               .orElseGet(this::resolvePlacerHolder);
    }

    /** Return form field binding attribute (if any). */
    public Option<TypeField> getBindingAttribute() {
        return getBindingFqn().flatMap(binding ->  //
                 getModelRepository().getModel(binding.getQualification(), ModelType.class).flatMap(modelType ->
                        modelType.getField(binding.getName())).castTo(TypeField.class));
    }

    /** Return binding fqn for form field (if any). */
    public Option<QName> getBindingFqn() {
        return getUiModel().getModel().flatMap(model -> getWidgetIdentifierText().filter(model::containsElement).flatMap(id -> getqName(model, id)));
    }

    /** Returns true if widget is table. */
    public boolean isTable() {
        return getWidgetType() == TABLE;
    }

    /** Get Type for form field. */
    public Type getFieldInfoType() {
        // Attempt to resolve explicit type
        Type result = getFieldInfoExplicitType();
        if (result.isNull()) {
            // If type is not explicit, attempt to resolve type from binding
            for (final TypeField field : getBindingAttribute())
                result = field.getFinalType();
        }
        return result;
    }

    /** Get node associated for given option (if any). For test purposes. */
    @Nullable public ASTNode getFieldOption(FieldOption option) {
        for (final ASTNode node : getChildren(TokenSet.create(MMElementType.OPTION))) {
            if (FieldOption.fromId(node.getFirstChildNode().getText()) == option) return node.getLastChildNode();
        }
        return null;
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.WIDGET_ICON;
    }

    /** Get table implements line marker. */
    @Nullable public LineMarkerInfo<MMCommonComposite> getMultipleLineMarker() {
        final PsiClass clazz = getMultiplePsiClass();

        final int startOffset = getStartOffset();
        return clazz != null
               ? new LineMarkerInfo<>(this,
            new TextRange(startOffset, startOffset),
            GutterManager.IMPLEMENTED_ICON,
            Pass.UPDATE_ALL,
            modelASTS -> IMPLEMENTED_IN + clazz.getQualifiedName(),
            (e, elt) -> PsiUtils.scrollTo(clazz),
            GutterIconRenderer.Alignment.LEFT)
               : null;
    }

    /** True if id is defined. */
    public boolean isBindingIdOverridden() {
        return getWidgetId().isPresent();
    }

    @NotNull @Override public String getName() {
        return getWidgetIdentifierText().orElse("");
    }

    @Override public PsiElement setName(@NonNls @NotNull String name)
        throws IncorrectOperationException
    {
        final PsiElement newChild = MetaModelElementFactory.createIdentifier(getProject(), name);
        for (final ASTNode replace : getWidgetId())
            replace.getTreeParent().replaceChild(replace, newChild.getNode());
        return this;
    }

    /** Get contextual ui model. */
    public PsiUiModel<UiModel> getUiModel() {
        return cast(getAncestor(PsiUiModel.class).getOrFail(MSGS.widgetNotOnUiContext()));
    }

    /** Returns ui model's widget (if it's identifiable). */
    public Option<Widget> getWidget() {
        return getWidgetIdentifierText().flatMap(name ->  //
                 getUiModel().getModel().filter(m -> m.containsElement(name)).map(m -> m.getElement(name)));
    }

    /** Get widget binding or type (if defined). */
    @NotNull public Option<ASTNode> getWidgetDataType() {
        Option<ASTNode> result = empty();
        final ASTNode   type   = findChildByType(MMElementType.TYPE);
        if (type != null) {
            result = option(type.findChildByType(MMElementType.TYPE_REF));
            if (result.isEmpty()) result = option(type.findChildByType(MMElementType.REFERENCE));
        }
        return result;
    }

    /** Get widget label id (if defined). */
    @NotNull public Option<ASTNode> getWidgetId() {
        Option<ASTNode> result = empty();
        final ASTNode   label  = findChildByType(MMElementType.LABELED_ID);
        if (label != null) result = option(label.findChildByType(MMElementType.IDENTIFIER));
        return result;
    }

    /** Get widget label (if defined). */
    @NotNull public Option<ASTNode> getWidgetLabel() {
        Option<ASTNode> result = empty();
        final ASTNode   label  = findChildByType(MMElementType.LABELED_ID);
        if (label != null) result = option(label.findChildByType(MMElementType.STRING_LITERAL));
        return result;
    }

    /** Get form field WidgetType. */
    @Nullable public WidgetType getWidgetType() {
        WidgetType result = null;

        // Attempt to check if widget is explicit
        final ASTNode widgetType = findWidgetType();
        if (widgetType != null) result = WidgetTypes.fromId(getNodePlainText(widgetType));

        if (result == null) {
            // Attempt to resolve widget if field is identifiable
            for (final Widget w : getWidget())
                result = w.getWidgetType();

            if (result == null) {
                // Expect explicit type defined
                final Type type = getFieldInfoExplicitType();
                if (!type.isNull()) result = WidgetTypes.fromType(type, false, false);
            }
        }
        return result;
    }

    @Nullable PsiClass getMultiplePsiClass() {
        final Option<String> multipleClassFqn = getMultipleWidgetClassFqn();
        return multipleClassFqn.isPresent() ? getPsiClassForFqnNullable(getProject(), multipleClassFqn.get()) : null;
    }

    @NotNull private Option<QName> getqName(UiModel model, String id) {
        final String binding = model.getElement(id).getBinding();
        if (Predefined.isEmpty(binding)) return empty();

        final QName qName = createQName(binding);
        if (qName.getQualification().isEmpty()) {
            final String q = model.getElement(id).getMultiple().map(Widget::getOriginalType).map(Object::toString).orElseGet(() ->
                        model.getBinding()
                             .getFullName());  // get the binding of the model or the table
            return of(createQName(q, binding));
        }
        return of(qName);
    }

    /** Resolves a Java Class for ui model holding widget. */
    @Nullable private PsiClass resolveJavaClass() {
        return PsiUtils.resolveJavaClass(getProject(), getUiModel().getFullName());
    }

    @Nullable private PsiEntityField resolvePlacerHolder() {
        return children(MMToken.TYPE_NODE).getFirst()                                                        //
               .flatMap(m -> m.children(MMToken.TYPE_REF).getFirst())                                        //
               .filter(n -> "_".equals(n.getText())).castTo(PsiTypeReference.class)                          //
               .flatMap(ref -> getParentFqn())                                                               //
               .flatMap(fqn -> resolveMetaModel(fqn.getQualification()).flatMap(mm -> getBoundMetaModel(fqn, mm)))  //
               .getOrNull();
    }

    @NotNull private Seq<PsiWidgetType> tableButtonsFromPsiUiModelWidgets(Seq<PsiWidget> widgets) {
        return widgets.filter(PsiWidget::isAddOrRemoveButton).map(PsiWidget::findWidgetType);
    }

    @NotNull private Option<PsiEntityField> getBoundMetaModel(QName binding, PsiMetaModel<?> model) {
        if (model instanceof PsiDatabaseObject) return ofNullable(((PsiDatabaseObject<?>) model).getFieldNullable(binding.getName()));
        if (model instanceof PsiType) return ofNullable(((PsiType) model).getFieldNullable(binding.getName()));
        return empty();
    }

    @NotNull private Type getFieldInfoExplicitType() {
        return Types.fromString(getWidgetDataType().map(PsiWidget::getNodePlainText).orElse(""));
    }

    /** Return and optional id if field is identifiable (id or binding defined!). */
    @NotNull private Option<ASTNode> getFormFieldIdentifier() {
        return getWidgetId().or(this::getWidgetDataType);
    }

    @NotNull private String getMultipleClassName(@NotNull final String id) {
        return capitalizeFirst(id + ROW_CLASS_SUFFIX);
    }

    private Option<String> getMultipleWidgetClassFqn() {
        return getWidgetId().isPresent() || getBindingAttribute().isPresent() ? getWidgetIdentifierText().map(value ->
                qualify(getUiModel().getFullName(), getMultipleClassName(value)))
                                                                              : empty();
    }

    private Option<QName> getParentFqn() {
        return getAncestor(psiElement -> PsiWidget.class.isInstance(psiElement) && ((PsiWidget) psiElement).getBindingFqn()
                                                                                                           .isPresent()).castTo(PsiWidget.class)
               .map(PsiWidget::getBindingFqn)
               .orElse(empty());
    }

    /** Return and optional id if widget is identifiable (id or binding defined!). */
    @NotNull private Option<String> getWidgetIdentifierText() {
        return getFormFieldIdentifier().map(PsiWidget::getNodePlainText);
    }

    //~ Methods ......................................................................................................................................

    /** Is form field a add or remove button? */
    public static boolean isAddOrRemoveButton(final PsiWidget widget) {
        if (widget != null && widget.getWidgetType() == BUTTON) {
            final PsiWidgetType type = widget.findWidgetType();
            if (type != null) {
                final Set<String> arguments = type.getWidgetTypeArguments();
                return arguments.contains(ADD_ROW) || arguments.contains(REMOVE_ROW);
            }
        }
        return false;
    }

    /**
     * Return node plain text without parameters. Eg: search_box(SomeForm) :: search_box ||
     * String(40) :: String.
     */
    private static String getNodePlainText(ASTNode node) {
        String    result     = node.getText();
        final int parameters = result.indexOf('(');
        if (parameters != -1) result = result.substring(0, parameters);
        return result;
    }
}  // end class PsiWidget
