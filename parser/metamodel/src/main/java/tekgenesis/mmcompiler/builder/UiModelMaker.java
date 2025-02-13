
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.check.CheckType;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.HasFieldOption;
import tekgenesis.field.MetaModelReference;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.FieldAlreadyBindException;
import tekgenesis.metadata.exception.UnsupportedOptionException;
import tekgenesis.metadata.form.widget.UiModelBuilder;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.*;
import tekgenesis.type.exception.IntLengthException;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.decode;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.metadata.exception.BuilderErrors.*;
import static tekgenesis.metadata.form.DefaultUiModelGenerator.defaultUiModel;
import static tekgenesis.metadata.form.widget.WidgetBuilder.defaultWidget;
import static tekgenesis.metadata.form.widget.WidgetBuilder.widgetBuilder;
import static tekgenesis.metadata.form.widget.WidgetType.SEARCH_BOX;
import static tekgenesis.metadata.form.widget.WidgetTypes.getRelated;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.ASTMetaModelReference.unresolvedMetaModel;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.firstAtom;
import static tekgenesis.type.MetaModelKind.MODEL_TYPE;
import static tekgenesis.type.Types.*;

/**
 * Maker for graphical definitions (such as forms and components).
 */
abstract class UiModelMaker<M extends MetaModel, B extends UiModelBuilder<M, B>> extends Maker {

    //~ Constructors .................................................................................................................................

    UiModelMaker(MetaModelAST node, BuilderFromAST builder, String sourceName, QContext context) {
        super(node, builder, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected final B createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final Type binding = ofNullable(retrieveType(rootNode, this::asModelType)).orElse(nullType());
        return createBuilder(fqn, label, binding, modifiers);
    }

    void buildWidgets(WidgetBuilder parent, Option<Type> scope, MetaModelAST n, FieldsChecker check) {
        final WidgetType type = parent.getWidgetType();

        if (n.getType() == LIST && (type == WidgetType.TABLE || type == WidgetType.CHART || type == WidgetType.FORM || type == WidgetType.WIDGET)) {
            if (n.children(WIDGET_FIELD).isEmpty()) error(n, emptyList(type.toString()));
        }
        else if (!(WidgetTypes.supportsList(type))) error(n, listNotSupported(type.toString()));

        for (final MetaModelAST widget : n.children(WIDGET_FIELD)) {
            try {
                final WidgetBuilder widgetBuilder = buildWidget(scope, widget, check);
                parent.addWidget(widgetBuilder);
            }
            catch (final BuilderException e) {
                error(widget, e);
            }
            catch (final IntLengthException e) {
                error(widget, createError(e.getMessage(), ""));
            }
        }
    }

    abstract B createBuilder(QName fqn, String label, Type binding, EnumSet<Modifier> modifiers);

    void generateDefaultModelUi(@NotNull final B builder) {
        if (builder.getBinding().isPresent()) {
            try {
                defaultUiModel(repository, builder);
            }
            catch (final BuilderException e) {
                error(rootNode, e);
            }
        }
    }

    private <T extends Enum<T>> void addEnumOption(final WidgetBuilder builder, final HasFieldOption hasFieldOption, final MetaModelAST arg,
                                                   final FieldOption opt)
        throws BuilderException
    {
        final Class<T> enumClass = hasFieldOption.getEnumClass();
        builder.withEnum(opt, ensureNotNull(enumClass), arg.getEffectiveNode().getText());
    }

    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod" })
    private void addOption(WidgetBuilder builder, HasFieldOption hasFieldOption, MetaModelAST arg, @Nullable final Type type)
        throws BuilderException
    {
        final FieldOption opt = hasFieldOption.getFieldOption();
        switch (opt.getType()) {
        case CHECK_T:
            for (final MetaModelAST check : arg.children())
                makeCheckElement(builder, check);
            break;
        case STRING_T:
            builder.with(opt, Strings.decode(arg.getText()));
            break;
        case IDENTIFIER_T:
            builder.with(opt, arg.getEffectiveNode().getText());
            break;
        case METAMODEL_REFERENCE_T:
            builder.with(opt, createMetaModelReference(arg));
            break;
        case BOOLEAN_T:
            builder.with(opt);
            break;
        case BOOLEAN_EXPR_T:
            builder.with(opt, getAsExpression(arg, Expression.TRUE));
            break;
        case STRING_EXPR_T:
            builder.with(opt, getAsExpression(arg, stringType()));
            break;
        case STRING_EXPRS_T:
            builder.with(opt, getAsExpression(arg, arg.getType() == LIST ? arrayType(stringType()) : stringType()));
            break;
        case ASSIGNMENT_EXPRS:
            builder.with(opt, getAsExpression(arg, arrayType(anyType()), type));
            break;
        case UNSIGNED_EXPR_T:
            builder.with(opt, getAsExpression(arg, intType()));
            break;
        case STRING_ARRAY_EXPR_T:
            builder.with(opt, getAsExpression(arg, arrayType(stringType())));
            break;
        case VALUE_EXPR_T:
            builder.with(opt, getAsExpression(arg, notNull(type, stringType())));
            break;
        case METHOD_T:
            builder.with(opt, arg.getChild(0).getText());
            final FieldOption expression = getRelated(opt);
            if (expression != null && !arg.getChild(1).isEmpty()) addOption(builder, expression, arg.getChild(1), type);
            break;
        case GENERIC_EXPR_T:
            builder.with(opt, getAsExpression(arg, Types.anyType()));
            break;
        case VALUE_ARRAY_EXPR_T:
            builder.with(opt, getAsExpression(arg, arrayType(notNull(type, stringType()))));
            break;
        case ENUM_T:
            addEnumOption(builder, hasFieldOption, arg, opt);
            break;
        case UNSIGNED_T:
            builder.with(opt, parseAsInt(arg.getText(), 0));
            break;
        default:
            error(arg, new UnsupportedOptionException(opt, builder.getWidgetType()));
        }
    }  // end method addOption

    @NotNull private Type asModelType(MetaModelAST node) {
        final MetaModelAST typeRefNode = node.getChild(0);
        final String       fqn         = retrieveReferenceQualifiedId(typeRefNode);
        final Option<Type> model       = repository.getModel(context.extractQualification(fqn), context.extractName(fqn)).filter(m ->
                    MODEL_TYPE.contains(m.getMetaModelKind())).castTo(Type.class);
        model.ifEmpty(() -> error(typeRefNode, unresolvedReference(fqn)));
        return model.orElse(nullType());
    }

    private void buildAggregateColumns(WidgetBuilder table, MetaModelAST aggregate, AggregateFn fn) {
        boolean global = true;

        for (final MetaModelAST reference : aggregate.children(FIELD_REF)) {
            final String column = reference.getChild(0).getText();
            try {
                global = false;
                table.aggregate(fn, column);
            }
            catch (final BuilderException e) {
                error(reference, e);
            }
        }

        if (global) {
            try {
                table.aggregate(fn);
            }
            catch (final BuilderException e) {
                error(aggregate, e);
            }
        }
    }

    private void buildTableAggregates(WidgetBuilder parent, MetaModelAST n) {
        if (n.children(AGGREGATE).isEmpty()) error(n, emptyAggregates(parent.getWidgetType().toString()));

        for (final MetaModelAST aggregate : n.children(AGGREGATE)) {
            final Tuple<MetaModelAST, String> idLabel = retrieveLabeledId(aggregate);
            final String                      id      = idLabel.first().getText();

            try {
                final AggregateFn fn = AggregateFn.valueOf(id.toUpperCase());
                buildAggregateColumns(parent, aggregate, fn);
            }
            catch (final IllegalArgumentException e) {
                error(aggregate, undefinedAggregate(id));
            }
        }
    }

    private WidgetBuilder buildWidget(final Option<Type> scope, MetaModelAST widgetNode, FieldsChecker check) {
        final WidgetProcessRef  handler = new WidgetProcessRef(scope);
        final Type              type    = retrieveType(widgetNode, handler);
        final Option<TypeField> binding = handler.getField();

        final WidgetBuilder builder = retrieveWidgetType(widgetNode, type, binding);

        final Tuple<MetaModelAST, String> idLabel = retrieveLabeledId(widgetNode);
        final String                      id      = idLabel.first().getText();
        builder.id(id).label(idLabel.second());

        try {
            check.duplicateFieldName(id);

            if (binding.isPresent()) {
                final TypeField typeField = binding.get();
                builder.withBinding(typeField);
                check.duplicateBinding(id, typeField);
            }
            else if (handler.isPlaceholder()) builder.withPlaceholderBinding();

            if (type != null && !type.isNull()) builder.withType(type);
        }
        catch (final BuilderException e) {
            error(widgetNode, e);
        }

        final WidgetType   widgetType = builder.getWidgetType();
        final Option<Type> nested     = isMultiple(widgetType) ? ofNullable(type).map(Type::getFinalType) : scope;

        final Type resultingType = widgetType == SEARCH_BOX && scope.isPresent() ? scope.get() : builder.getFinalType();

        for (final MetaModelAST n : widgetNode) {
            if (n.hasType(LIST)) buildWidgets(builder, nested, n, check);
            else if (n.hasType(AGGREGATE)) buildTableAggregates(builder, n);
            else if (n.hasType(OPTION)) {
                final FieldOption opt = retrieveOption(n);
                if (opt != null) {
                    try {
                        final HasFieldOption option = WidgetTypes.asHasFieldOption(opt);
                        addOption(builder, option, n.getChild(1).getEffectiveNode(), resultingType);
                        // todo how to validate unique ?
                        // if (parent.getFeature(TYPE) != TABLE) error(n,
                        // illegalUniqueAttribute(String.valueOf(builder.getFeature(ID))));
                    }
                    catch (final BuilderException e) {
                        error(n, e);
                    }
                }
            }
        }
        checkForErrors(widgetNode, resultingType, widgetType);

        return builder;
    }  // end method buildWidget

    private void checkForErrors(MetaModelAST widgetNode, Type type, WidgetType widgetType) {
        if (type == nullType() && WidgetTypes.hasValue(widgetType)) error(widgetNode, undefinedMetaModelType(widgetNode.getText()));
        if (widgetType == WidgetType.TABLE || widgetType == WidgetType.CHART) {
            if (!widgetNode.children(MMToken.LIST).getFirst().isPresent())
                error(widgetNode.children(MMToken.WIDGET_TYPE).getFirst().orElse(widgetNode), emptyList(widgetType.toString()));
        }
    }

    private MetaModelReference createMetaModelReference(MetaModelAST arg) {
        return unresolvedMetaModel(this, arg, context, retrieveReferenceQualifiedId(arg));
    }

    private void makeCheckElement(WidgetBuilder builder, MetaModelAST check)
        throws BuilderException
    {
        final Expression   expr      = getAsExpression(check.getChild(0), Types.booleanType());
        final boolean      inline    = check.getChild(1).getChild(0).getText().equals(MMToken.INLINE.getText());
        final MetaModelAST ct        = check.getChild(2).getChild(0);
        final CheckType    checkType = ct.isEmpty() ? CheckType.ERROR : CheckType.valueOf(ct.getText().toUpperCase());
        final String       msg       = decode(check.getChild(3).getText());
        builder.check(expr, inline, checkType, msg);
    }

    private void retrieveArguments(MetaModelAST arguments, WidgetBuilder builder, @Nullable final Type type) {
        // todo (Agustin) check for required arguments (argument with no default value)
        if (arguments.hasType(LIST) && !arguments.children().isEmpty()) {
            final WidgetType widgetType = builder.getWidgetType();

            int n = 0;
            for (final MetaModelAST arg : arguments) {
                final HasFieldOption option = WidgetTypes.getArgument(widgetType, n++);
                if (option == null) {
                    final String text = arg.getEffectiveNode().getText();
                    error(arg, createIllegalArgument(widgetType.getId(), text, n));
                }
                else {
                    try {
                        addOption(builder, option, arg.getEffectiveNode(), type);
                    }
                    catch (final BuilderException e) {
                        error(arg, e);
                    }
                }
            }
        }
    }  // end method retrieveArguments

    private WidgetBuilder retrieveWidgetType(@NotNull MetaModelAST widgetNode, @Nullable Type type, @NotNull Option<TypeField> binding) {
        for (final MetaModelAST n : widgetNode) {
            switch (n.getType()) {
            case WIDGET_TYPE:
                final WidgetType widgetType = WidgetTypes.fromId(n.getChild(0).getText());
                if (widgetType == null) break;

                final WidgetBuilder builder = widgetBuilder(widgetType);
                retrieveArguments(n.getChild(1), builder, type);
                return builder;
            case LIST:
                if (type != null) break;
                return widgetBuilder(WidgetType.HORIZONTAL);
            default:
                break;
            }
        }

        if (type != null) {
            try {
                final boolean multiple    = binding.map(TypeField::isMultiple).orElse(false);
                final boolean synthesized = binding.map(TypeField::isSynthesized).orElse(false);

                return type.isUndefined() ? defaultWidget() : widgetBuilder(type, multiple, synthesized);
            }
            catch (final Exception e) {
                // Fall through
            }
        }
        error(widgetNode, createIllegalWidgetTypeError(firstAtom(widgetNode).getText()));
        return defaultWidget();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Holds a context of fields ids and bindings to check for duplicates.
     */
    class FieldsChecker {
        private final Set<TypeField> binds;
        private final Set<String>    fields;
        private final String         form;

        FieldsChecker(String form) {
            this.form = form;
            fields    = new TreeSet<>();
            binds     = new HashSet<>();
        }

        private void duplicateBinding(String field, TypeField bind)
            throws BuilderException
        {
            if (!binds.add(bind)) throw new FieldAlreadyBindException(field, form, bind);
        }

        private void duplicateFieldName(String field)
            throws BuilderException
        {
            if (isNotEmpty(field) && !fields.add(field)) throw DuplicateFieldException.onForm(field, form);
        }
    }

    private class WidgetProcessRef implements Function<MetaModelAST, Type> {
        private Option<TypeField>       field;
        private boolean                 placeholder;
        private final Option<ModelType> scope;

        WidgetProcessRef(Option<Type> scope) {
            this.scope  = scope.castTo(ModelType.class);
            field       = empty();
            placeholder = false;
        }

        @Override public Type apply(MetaModelAST ast) {
            final MetaModelAST typeNode = ast.getChild(0);
            final String       target   = retrieveReferenceQualifiedId(typeNode);

            return getType(typeNode, target).orElseGet(() -> {
                error(typeNode, cannotResolveSymbol(target));
                return nullType();
            });
        }

        @NotNull Type checkFieldResolved(MetaModelAST typeNode, TypeField a) {
            final Type type = a.getFinalType();
            if (type.isUndefined()) {
                error(typeNode, unresolvedFieldType(a.getName()));
                return nullType();
            }
            return notNull(elementType(type), type);
        }

        @NotNull Option<Type> getType(MetaModelAST typeNode, String target) {
            return resolveFieldBinding(typeNode, target)  // Field binding
                   .or(resolveTypeBinding(target))       // Type binding
                   .or(resolvePlaceholderBinding(target));  // Placeholder binding
        }

        @NotNull private Option<Type> resolveFieldBinding(MetaModelAST typeNode, String target) {
            return scope.flatMap(m -> {
                    field = m.getField(target).castTo(TypeField.class);
                    return field;
                }).map(a -> checkFieldResolved(typeNode, a));
        }

        @NotNull private Supplier<Option<Type>> resolvePlaceholderBinding(String target) {
            return () ->
                   scope.filter(m -> "_".equals(target)).map(m -> {
                    placeholder = true;
                    return m;
                });
        }

        @NotNull private Supplier<Option<Type>> resolveTypeBinding(String target) {
            return () -> {
                       final QName fqn = createQName(context.extractQualification(target), context.extractName(target));
                       return repository.getModel(fqn, ModelType.class).castTo(Type.class);
                   };
        }

        private Option<TypeField> getField() {
            return field;
        }

        private boolean isPlaceholder() {
            return placeholder;
        }
    }  // end class WidgetProcessRef
}  // end class UiModelMaker
