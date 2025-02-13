
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.check.Check;
import tekgenesis.check.CheckType;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.expr.ExpressionAST;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.exception.*;
import tekgenesis.metadata.form.ref.UiModelReferenceMapper;
import tekgenesis.type.*;
import tekgenesis.type.Kind;

import static tekgenesis.check.CheckType.ENTITY;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.common.core.Constants.EXPORT_METHOD;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.expr.ExpressionFactory.eq;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.field.MetaModelReference.referenceMetaModel;
import static tekgenesis.metadata.form.SourceWidget.buttonTypeSupportsTableDiscovery;
import static tekgenesis.metadata.form.model.FormConstants.AUTO_ID_PREFIX;
import static tekgenesis.metadata.form.widget.Aggregators.createExpression;
import static tekgenesis.metadata.form.widget.ButtonType.*;
import static tekgenesis.metadata.form.widget.ExportType.CSV;
import static tekgenesis.metadata.form.widget.ToggleButtonType.CUSTOM;
import static tekgenesis.metadata.form.widget.ToggleButtonType.DEPRECATE;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.*;
import static tekgenesis.type.Kind.*;
import static tekgenesis.type.Types.*;

/**
 * The base builder for widgets (including {@link Form} and {@link WidgetDef}).
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "UnusedReturnValue", "WeakerAccess", "OverlyComplexClass" })
public class WidgetBuilder extends FieldBuilder<WidgetBuilder> implements Iterable<WidgetBuilder> {

    //~ Instance Fields ..............................................................................................................................

    // Slots counters
    int configureDimension;
    int fieldDimension;
    int subformDimension;
    int widgetDefDimension;

    private boolean belongsToPrimaryKey = false;

    @NotNull private Option<TypeField>          binding;
    @NotNull private final List<WidgetBuilder>  childrenBuilders;
    private int                                 localOptionsDimension;
    @Nullable private FormFieldRef.Dynamic      ref  = null;
    @NotNull private Type                       type;

    @NotNull private final WidgetType widgetType;

    //~ Constructors .................................................................................................................................

    WidgetBuilder(@NotNull final WidgetType widgetType) {
        this(widgetType, new FieldOptions());
    }

    WidgetBuilder(@NotNull final WidgetType widgetType, @NotNull final FieldOptions options) {
        super(options);
        this.widgetType  = widgetType;
        childrenBuilders = new ArrayList<>();
        binding          = Option.empty();
        type             = getTypeFor(widgetType);
        if (type == anyType()) type = stringType();
    }

    //~ Methods ......................................................................................................................................

    /** Consolidate fieldOptions and Add a widget to the builder. */
    public WidgetBuilder addWidget(@NotNull final WidgetBuilder builder)
        throws BuilderException
    {
        return addConsolidatedWidget(builder.consolidate());
    }

    /** Consolidate fieldOptions and the widget to the builder in the desired index. */
    public WidgetBuilder addWidget(int index, @NotNull final WidgetBuilder builder)
        throws BuilderException
    {
        childrenBuilders.add(index, builder.consolidate());
        return this;
    }

    /** Add an aggregate function to all columns. */
    public WidgetBuilder aggregate(@NotNull AggregateFn fn)
        throws BuilderException
    {
        return aggregate(fn, o -> true);
    }

    /** Add an aggregate function to specific column. */
    public WidgetBuilder aggregate(@NotNull AggregateFn fn, @NotNull String column)
        throws BuilderException
    {
        checkColumnExists(column);
        return aggregate(fn, o -> equal(o, column));
    }

    /** Sets the button type. */
    public WidgetBuilder buttonType(@NotNull final ButtonType buttonType)
        throws BuilderException
    {
        return with(BUTTON_TYPE, buttonType);
    }

    /** Add children to the Widget. */
    public WidgetBuilder children(@NotNull WidgetBuilder... bs)
        throws BuilderException
    {
        for (final WidgetBuilder builder : bs)
            addWidget(builder);
        return this;
    }

    /** Set widget content style class. */
    public WidgetBuilder contentStyleClass(@NotNull final String styleClass)
        throws BuilderException
    {
        return contentStyleClass(str(styleClass));
    }

    /** Set widget content style class. */
    public WidgetBuilder contentStyleClass(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(CONTENT_STYLE, expr.createExpression());
    }

    /** Sets widget default value. */
    public WidgetBuilder defaultValue(@NotNull final String value)
        throws BuilderException
    {
        return with(DEFAULT, str(value).createExpression());
    }

    /** Sets widget with default value expression. */
    public WidgetBuilder defaultValue(@NotNull ExpressionAST expr)
        throws BuilderException
    {
        return with(DEFAULT, expr.createExpression());
    }

    /** Sets widget default value. */
    public WidgetBuilder defaultValue(@NotNull final Boolean value)
        throws BuilderException
    {
        return with(DEFAULT, value ? Expression.TRUE : Expression.FALSE);
    }

    /** Sets widget auto calculate 'is' expression. */
    public WidgetBuilder dependsOn(@NotNull final String fieldId)
        throws BuilderException
    {
        return with(DEPENDS_ON, fieldId);
    }

    /** Disable the widget permanently. */
    public WidgetBuilder disable()
        throws BuilderException
    {
        return with(DISABLE, Expression.TRUE);
    }

    /** Add a enable expression to the widget. */
    public WidgetBuilder disable(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(DISABLE, expr.createExpression());
    }

    /** Sets widget auto calculate 'is' expression. */
    public WidgetBuilder display(@NotNull final ExpressionAST expression)
        throws BuilderException
    {
        return with(FieldOption.DISPLAY, expression.createExpression());
    }

    /** Sets the number of displayed items for a list box. */
    public WidgetBuilder displayedItems(final int rows)
        throws BuilderException
    {
        return with(ROWS, rows);
    }

    /** Sets the number of displayed lines for a text area. */
    public WidgetBuilder displayedLines(final int rows)
        throws BuilderException
    {
        return with(ROWS, rows);
    }

    /** Sets the number of displayed rows for a table. */
    public WidgetBuilder displayedRows(final int rows)
        throws BuilderException
    {
        return with(ROWS, rows);
    }

    /** Sets the accepted file types. */
    public WidgetBuilder fileType(final String acceptedFile)
        throws BuilderException
    {
        return with(FILE_TYPE, acceptedFile);
    }

    /** Sets filtering relation. */
    public WidgetBuilder filtering(@NotNull final String multiple)
        throws BuilderException
    {
        return with(FILTERING, multiple);
    }

    /** Add a visible expression to the widget. */
    public WidgetBuilder hide(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(HIDE, expr.createExpression());
    }

    /** Add a visible expression to the widget. */
    public WidgetBuilder hideColumn(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(HIDE_COLUMN, expr.createExpression());
    }

    /** Sets subform inline flag. */
    public WidgetBuilder inline()
        throws BuilderException
    {
        return with(INLINE, Expression.TRUE);
    }

    /** Sets widget auto calculate 'is' expression. */
    public WidgetBuilder is(@NotNull final ExpressionAST expression)
        throws BuilderException
    {
        return with(IS, expression.createExpression());
    }

    @NotNull @Override public Iterator<WidgetBuilder> iterator() {
        return childrenBuilders.iterator();
    }

    /** Add a label expression to the widget. */
    public WidgetBuilder labelExpression(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(LABEL_EXPRESSION, expr.createExpression());
    }

    /** Sets the length of the widget. */
    public WidgetBuilder length(final int length)
        throws BuilderException
    {
        return with(LENGTH, length);
    }

    /** Sets the link url. */
    public WidgetBuilder link(@NotNull final String link)
        throws BuilderException
    {
        return link(str(link));
    }

    /** Sets the link url expression. */
    public WidgetBuilder link(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(LINK, expr.createExpression());
    }

    /** Sets the link form. */
    public WidgetBuilder linkForm(@NotNull final String link)
        throws BuilderException
    {
        return with(LINK_FORM, referenceMetaModel("", link));
    }

    /** Sets the link pk, used with linkForm. */
    public WidgetBuilder linkPk(@NotNull final String pk)
        throws BuilderException
    {
        return linkPk(str(pk));
    }

    /** Sets the link pk expression, used with linkForm. */
    public WidgetBuilder linkPk(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(LINK_PK, expr.createExpression());
    }

    /** Returns a form field ref to . */
    public FormFieldRef makeRef() {
        if (ref == null) ref = new FormFieldRef.Dynamic(getName(), -1);
        return ref;
    }

    /** Sets the widget mask. Used to display the value. */
    public WidgetBuilder mask(@NotNull final String mask)
        throws BuilderException
    {
        return mask(str(mask));
    }

    /** Sets the widget mask. Used to display the value. */
    public WidgetBuilder mask(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(CUSTOM_MASK, expr.createExpression());
    }

    /** Sets the widget message type. */
    public WidgetBuilder msg(@NotNull final CheckType t)
        throws BuilderException
    {
        return with(CHECK_TYPE, t);
    }

    /** Sets if a list box has multiple selection/options. */
    public WidgetBuilder multiple()
        throws BuilderException
    {
        return with(MULTIPLE);
    }

    /** Sets subform widget placeholder. */
    public WidgetBuilder on(@NotNull final String anchor)
        throws BuilderException
    {
        return with(ON, anchor);
    }

    /** Sets the widget on change method name, to be called on the server when the value changes. */
    public WidgetBuilder onChange(@NotNull final String methodName)
        throws BuilderException
    {
        return with(ON_CHANGE, methodName);
    }

    /** Sets the widget on change method name, to be called on the server when the value changes. */
    public WidgetBuilder onChange(@NotNull final Function<?, ?> method)
        throws BuilderException
    {
        return with(ON_CHANGE_FN, method);
    }

    /** Sets the widget on change method name, to be called on the server when the value changes. */
    public WidgetBuilder onClick(@NotNull final Function<?, ?> method)
        throws BuilderException
    {
        return with(ON_CLICK_FN, method);
    }

    /**
     * Sets the button on click method name, to be called on the server when the button is clicked.
     */
    public WidgetBuilder onClick(@NotNull final String methodName)
        throws BuilderException
    {
        return with(ON_CLICK, methodName);
    }

    /** make the value of the widget not required. */
    public WidgetBuilder optional()
        throws BuilderException
    {
        return with(OPTIONAL, Expression.TRUE);
    }

    /** make the value of the widget not required. */
    public WidgetBuilder optional(final ExpressionAST expr)
        throws BuilderException
    {
        return with(OPTIONAL, expr.createExpression());
    }

    /** Removes a children from the builder. */
    public void removeWidget(@NotNull final WidgetBuilder builder) {
        childrenBuilders.remove(builder);
    }

    /** Set table row style. */
    public WidgetBuilder rowStyle(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(ROW_INLINE_STYLE, expr.createExpression());
    }

    /** Set table row style class. */
    public WidgetBuilder rowStyleClass(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(ROW_STYLE, expr.createExpression());
    }

    /** Set widget style class. */
    public WidgetBuilder styleClass(@NotNull final String styleClass)
        throws BuilderException
    {
        return styleClass(str(styleClass));
    }

    /** Set widget style class. */
    public WidgetBuilder styleClass(@NotNull final ExpressionAST expr)
        throws BuilderException
    {
        return with(STYLE, expr.createExpression());
    }

    /** Sets the subform reference. */
    public WidgetBuilder subForm(final String fqn)
        throws BuilderException
    {
        return with(SUBFORM_ID, referenceMetaModel(fqn));
    }

    /** Sets the table reference. */
    public WidgetBuilder table(final String tableName)
        throws BuilderException
    {
        return with(BUTTON_BOUND_ID, tableName);
    }

    /** Set link to target blank. */
    public WidgetBuilder targetBlank()
        throws BuilderException
    {
        return with(TARGET_BLANK, Expression.TRUE);
    }

    /** Sets the toggle button type. */
    public WidgetBuilder toggleButtonType(@NotNull final ToggleButtonType buttonType)
        throws BuilderException
    {
        return with(TOGGLE_BUTTON_TYPE, buttonType);
    }

    @Override public String toString() {
        return getOptions().getString(ID) + " : " + widgetType;
    }

    /** Set a table column as unique. */
    public WidgetBuilder unique()
        throws BuilderException
    {
        return with(UNIQUE);
    }

    /** Set widget col. */
    public WidgetBuilder widgetCol(int col)
        throws BuilderException
    {
        return with(COL, col);
    }

    /** Sets the widget definition reference. */
    public WidgetBuilder widgetDef(final String fqn)
        throws BuilderException
    {
        return with(WIDGET_DEF, referenceMetaModel(fqn));
    }

    /** Defines widget binding. */
    public WidgetBuilder withBinding(@NotNull TypeField attr)
        throws BuilderException
    {
        binding = some(attr);
        with(BINDING, attr.getFullName());
        return this;
    }

    /** Defines widget binding as placeholder. */
    public WidgetBuilder withPlaceholderBinding()
        throws BuilderException
    {
        with(PLACEHOLDER_BINDING);
        return this;
    }

    /** Sets the widget model type. */
    public WidgetBuilder withType(@NotNull final Type t)
        throws BuilderException
    {
        if (supports(widgetType, t.getFinalType())) {
            type = t.getFinalType();

            adjustSigned(t);

            return this;
        }
        throw getTypeFor(widgetType) == nullType() ? new InvalidTypeForWidgetException(widgetType) : new InvalidTypeForWidgetException(widgetType, t);
    }

    /** Return Final Type. */
    @NotNull public Type getFinalType() {
        return type.getFinalType();
    }

    @Override public String getName() {
        return super.getName();
    }

    /** Return WidgetType. */
    @NotNull public WidgetType getWidgetType() {
        return widgetType;
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException
    {
        if (!supports(widgetType, option) && !hasArgument(widgetType, option)) throw new UnsupportedOptionException(option, widgetType);

        final FieldOption incompatibleField = compatible(option, getOptions());
        if (incompatibleField != null) {
            if (incompatibleField == option) throw new IncompatibleOptionsException(option, widgetType, getFinalType());
            else throw new IncompatibleOptionsException(option, incompatibleField, widgetType);
        }

        // check if we have any of the required fields
        final Seq<FieldOption> requires = requires(option);
        if (!requires.isEmpty() && !requires.exists(ro -> ro != null && hasOption(ro)))
            throw new RequiredOptionsException(widgetType, option, Colls.seq(requires));

        // Signed and unsigned options are only for Numbers
        if ((option == SIGNED || option == UNSIGNED) && !getFinalType().isNumber()) throw new UnsupportedOptionException(option, widgetType);
    }

    /** Add a widget to the builder. */
    WidgetBuilder addConsolidatedWidget(@NotNull final WidgetBuilder builder) {
        childrenBuilders.add(builder);
        return this;
    }

    @NotNull ImmutableList<Widget> buildChildren(@NotNull final UiModelBuilder<?, ?> builder, @NotNull final WidgetBuilder modelParent)
        throws BuilderException
    {
        final ImmutableList.Builder<Widget> widgets = builder(childrenBuilders.size());
        final WidgetBuilder                 model   = isGroup(widgetType) ? modelParent : this;

        for (final WidgetBuilder b : childrenBuilders) {
            // add default id
            if (!b.hasOption(ID)) b.with(ID, AUTO_ID_PREFIX + b.widgetType.name().substring(0, 1) + builder.autoIdCount++);

            model.incrementSlotCounters(b, builder);

            final ImmutableList<Widget> grandChildren = b.buildChildren(builder, model);

            widgets.add(b.createWidget(grandChildren));
        }
        return widgets.build();
    }

    // Todo unify with consolidate ???
    @SuppressWarnings({ "OverlyComplexMethod", "OverlyLongMethod", "IfStatementWithTooManyBranches", "OverlyCoupledMethod" })
    WidgetBuilder consolidateExpressions(final UiModelReferenceMapper m, UiModel model)
        throws BuilderException
    {
        if (binding.isPresent() && binding.get() instanceof Attribute) {
            final Attribute attribute = (Attribute) binding.get();

            if (supports(widgetType, DEFAULT) && !hasOption(DEFAULT)) {
                final Expression defaultValue = attribute.getDefaultValue();
                if (!defaultValue.isNull()) with(DEFAULT, defaultValue.reMapReferences(m));
            }

            final String entityFqn = attribute.getDbObject().getFullName();
            for (final Check check : attribute.getCheck()) {
                // if attribute and form are bounded to different entities... (for example: tables).
                if (!entityFqn.equals(model.getBinding().getFullName())) m.usingTableEntity(some(entityFqn));

                check(check.getExpr().reMapReferences(m), check.getMsg());
            }
        }

        final boolean isForm     = model instanceof Form;
        final boolean deprecable = isForm && ((Form) model).isDeprecableBoundModel();

        if (widgetType == SEARCH_BOX) {
            if (!hasOption(WIDTH)) with(EXPAND);
            if (!(isForm && ((Form) model).isSearchable()) && !hasOption(ON_SUGGEST)) throw new EntityNotSearchableException(SEARCH_BOX, getName());
            if (deprecable) with(BOUNDED_TO_DEPRECABLE);
        }
        else if (widgetType == MESSAGE) {
            final CheckType checkType       = getOptions().getEnum(CHECK_TYPE, CheckType.class, CheckType.INFO);
            final boolean   isNotBoundModel = model.getBinding().isEmpty();
            if (isNotBoundModel && checkType == ENTITY) throw new UnsupportedOptionException(ENTITY, getName());
        }
        else if (widgetType == TOGGLE_BUTTON && getOptions().getEnum(TOGGLE_BUTTON_TYPE, ToggleButtonType.class, CUSTOM) == DEPRECATE && !deprecable)
            throw new InvalidDeprecateOptionException(model.getFullName());
        else if (widgetType == BUTTON) {
            final ButtonType buttonType = getOptions().getEnum(BUTTON_TYPE, ButtonType.class, ButtonType.CUSTOM);

            if (!model.getBinding().isEmpty() && buttonType == SAVE) with(SAVES_ENTITY);

            final String buttonBoundId = getOptions().getString(BUTTON_BOUND_ID);
            if (buttonTypeSupportsTableDiscovery(buttonType)) {
                if (isNotEmpty(buttonBoundId)) {
                    if (forAll(model.getDescendants(),
                            widget -> widget != null && (widget.getWidgetType() != TABLE || !widget.getName().equals(buttonBoundId))))
                        throw new TableButtonWithWrongTableDefinedException(getName(), buttonBoundId);
                }
                else
                {
                    final int dimensions = model.getMultipleDimension();
                    if (dimensions == 0) throw new TableButtonWithNoTableInScopeException(getName(), buttonType);

                    final Seq<String> tables = filter(model.getDescendants(), WidgetBuilder::isTableWidget).map(Widget::getName);
                    if (dimensions != 1) throw new TableButtonWithAmbiguousTableException(getName(), buttonType, tables);

                    getOptions().put(BUTTON_BOUND_ID, tables.getFirst().get());
                }
            }
            else {
                if (isNotEmpty(buttonBoundId)) {
                    if (buttonType != VALIDATE) throw new ButtonTypeDoesNotSupportArgumentException(getName(), buttonType, buttonBoundId);
                    if (forAll(model.getDescendants(),
                            widget -> widget != null && (!widget.getWidgetType().isGroup() || !widget.getName().equals(buttonBoundId))))
                        throw new ValidateButtonWithWrongGroupDefinedException(getName(), buttonBoundId);
                }
                if (buttonType == SAVE) {
                    if (isNotEmpty(getOptions().getString(ON_CLICK))) throw new ButtonSaveWithOnClickException(getName());
                }
            }

            if (buttonType == EXPORT && isEmpty(getOptions().getString(ON_CLICK))) {
                final String exportType = getOptions().getEnum(EXPORT_TYPE, ExportType.class, CSV).getName();
                with(ON_CLICK, EXPORT_METHOD + exportType + capitalizeFirst(getOptions().getString(BUTTON_BOUND_ID)));
            }
        }
        else if (widgetType == ANCHOR) {
            final Option<MultipleWidget> multiple = getMultipleFromFieldName(model);
            if (multiple.isPresent()) throw new WidgetInMultipleException("Anchor", multiple.get().getName(), getName());
        }
        else if (widgetType == SUBFORM) {
            if (!isForm) throw new SubformInWidgetDefinition(getName());
            if (hasOption(INLINE)) {
                final Option<MultipleWidget> multiple = getMultipleFromFieldName(model);
                if (multiple.isPresent() && isTableWidget(multiple.get()))
                    throw new WidgetInMultipleException("Inline Subform", multiple.get().getName(), getName());
            }
        }

        if (hasOption(DEPENDS_ON)) dependsOnExpr(getOptions().getString(DEPENDS_ON));

        return this;
    }  // end method consolidateExpressions

    /** Sets the widget ordinal. */
    WidgetBuilder ordinal(final int ordinal)
        throws BuilderException
    {
        if (ref != null) ref.solve(getName(), ordinal);
        return with(ORDINAL, ordinal);
    }

    private void addMaskToDecimalsOrIntegers()
        throws BuilderException
    {
        final Kind k = type.getKind();
        if ((k == DECIMAL || k == INT) && !hasOption(MASK) && supports(widgetType, MASK)) with(MASK, PredefinedMask.DECIMAL);
    }

    private void adjustLength(Type finalType)
        throws BuilderException
    {
        if (WidgetTypes.hasArgument(widgetType, LENGTH) && !hasOption(LENGTH)) {
            // if (hasOption(CUSTOM_MASK)) length(getOptions().getString(CUSTOM_MASK).length());
            // else {
            for (int length : finalType.getLength()) {
                if (finalType.isNumber()) {
                    length += (length / 3);  // for all, adding space for thousands separator.

                    if (hasOption(SIGNED)) length += 1;  // adding space for minus sign.

                    if (finalType.getKind() == REAL) length += 1;  // adding space for decimal separator.
                    else if (finalType.getKind() == DECIMAL && ((DecimalType) finalType).getDecimals() > 0)
                        length += 1;                               // adding space for decimal separator.
                }

                length(length);
            }
            // }
        }
    }  // end method adjustLength

    private void adjustSigned(@NotNull Type t)
        throws BuilderException
    {
        if (t instanceof SimpleType && ((SimpleType) t).getOptions().hasOption(SIGNED) && !hasOption(SIGNED) && !hasOption(UNSIGNED)) with(SIGNED);
    }

    /** Add aggregate function to all columns satisfying given condition. */
    private WidgetBuilder aggregate(@NotNull AggregateFn fn, @NotNull Predicate<String> condition)
        throws BuilderException
    {
        for (final WidgetBuilder child : childrenBuilders) {
            final String column = child.getName();
            if (isNotEmpty(column) && condition.test(column) && child.getFinalType().isNumber()) aggregate(createExpression(fn, column), fn, column);
        }
        return this;
    }

    private void checkColumnExists(String column)
        throws BuilderException
    {
        if (!exists(childrenBuilders, w -> w != null && w.getName().equals(column))) throw new InvalidReferenceException(column);
    }

    @SuppressWarnings({ "OverlyComplexMethod", "OverlyLongMethod" })
    private WidgetBuilder consolidate()
        throws BuilderException
    {
        if (widgetType == SUGGEST_BOX) {
            if (type instanceof Entity) {
                final Entity entity = (Entity) type;
                if (entity.isDeprecable()) with(BOUNDED_TO_DEPRECABLE);

                if (!entity.isSearchable() && !hasOption(ON_SUGGEST) && !hasOption(ON_SUGGEST_SYNC))
                    throw new NoSearchableForSuggestBox(entity.getFullName(), getName());
            }
            else if (hasOption(FILTER) && !(type instanceof DbObject)) throw new FilterNotAllowedInNonEntitySuggestBox(type, getName());
        }
        if (binding.isPresent()) {
            final TypeField attribute = binding.get();

            if (!hasOption(ID)) with(ID, attribute.getName());
            if (hasNoLabel()) {
                String label = attribute.getLabel();
                if (label.isEmpty()) label = toWords(fromCamelCase(attribute.getName()));
                with(FieldOption.LABEL, label);
            }

            if (!hasOption(OPTIONAL) && !hasOption(REQUIRED) && supports(widgetType, OPTIONAL)) with(OPTIONAL, attribute.getOptional());

            if (attribute instanceof Attribute && ((Attribute) attribute).getDbObject().isPrimaryKey((Attribute) attribute))
                belongsToPrimaryKey = true;

            if (attribute.getFinalType().isDatabaseObject()) {
                final DbObject modelType = (DbObject) attribute.getFinalType();
                withType(new EntityReference(modelType.getDomain(), modelType.getName()));
            }
            else {
                final Type elementType = Types.elementType(attribute.getFinalType());
                if (elementType != null && elementType.isType()) withType(elementType);
                else if (!attribute.getFinalType().isUndefined()) withType(attribute.getType());
            }

            if (attribute.getOptions().hasOption(SIGNED) && !hasOption(SIGNED) && !hasOption(UNSIGNED)) with(SIGNED);
        }
        else {
            if (hasNoLabel() && supports(widgetType, NO_LABEL) && !hasOption(NO_LABEL)) with(NO_LABEL);

            final Type t = getFinalType();
            if (t instanceof MetaModel && t.getKind() == REFERENCE) {
                final MetaModel modelType = (MetaModel) t;
                withType(new EntityReference(modelType.getDomain(), modelType.getName()));
            }
        }

        if (widgetType == TABLE && hasOption(LAZY_FETCH)) with(ON_LAZY_FETCH, "lazyLoad" + capitalizeFirst(getName()));

        if (widgetType == WIDGET && !hasOption(ID)) throw new WidgetDefMustHaveId(getName());

        if (WidgetTypes.hasValue(widgetType) && !type.isUndefined()) adjustLength(getFinalType());

        if (hasValue(widgetType) && !type.isUndefined()) adjustLength(getFinalType());

        if (isArrayValued(widgetType)) with(MULTIPLE);

        if (hasOption(IS)) disable();  // will be changed by the is expression

        if (widgetType == DYNAMIC) withType(Types.anyType());  // Override dynamic type to any.

        if (hasOption(FILTERING)) {
            for (final WidgetBuilder child : childrenBuilders) {
                if (child.getWidgetType() == DYNAMIC) child.with(FILTERING, getOptions().getString(FILTERING));
            }
        }

        if (isGroup(widgetType) && hasOption(DISABLE)) disableChildren();

        addDefaultArguments(WidgetTypes.getArguments(widgetType));

        addMaskToDecimalsOrIntegers();

        if (type.isNumber() && hasOption(CUSTOM_MASK)) throw new InvalidOptionForType(widgetType, type, CUSTOM_MASK);

        return this;
    }  // end method consolidate

    @SuppressWarnings({ "OverlyComplexMethod", "OverlyCoupledMethod", "OverlyLongMethod" })  // ok for Builder
    private Widget createWidget(ImmutableList<Widget> children) {
        if (isMultiple(widgetType)) {
            if (widgetType == WidgetType.TABLE) for (final Widget child : children)
                child.getOptions().put(NO_LABEL, true);
            return new MultipleWidget(widgetType,
                type,
                getOptions(),
                children,
                fieldDimension,
                subformDimension,
                widgetDefDimension,
                localOptionsDimension,
                configureDimension);
        }
        final int size = children.size();

        int remaining  = GRID_SIZE;
        int noColChild = 0;
        for (final Widget child : children) {
            final int i = child.getOptions().getInt(COL);
            remaining -= i;
            if (i == 0 && child.getWidgetType() != WidgetType.INTERNAL) noColChild++;
        }

        // if (widgetType == INPUT_GROUP) {
        // final int label = hasOption(LABEL_COL) ? getOptions().getInt(LABEL_COL) : 2;
        // remaining -= label;
        // }
        final int colCandidate = noColChild != 0 ? remaining > 0 ? remaining / noColChild : 0 : 0;
        final int defaultCol   = size != 0 && (widgetType == HORIZONTAL || widgetType == HEADER || widgetType == INPUT_GROUP) ? colCandidate : 0;

        for (final Widget child : children) {
            // && !isButtonOrLabel(child.getWidgetType())
            if (!child.getOptions().containsKey(COL) && !isButtonOrLabel(child.getWidgetType()) && !hasOption(FLOW))
                child.getOptions().put(COL, defaultCol);
            if (widgetType == INPUT_GROUP && !child.hasInnerLabel()) child.getOptions().put(NO_LABEL, true);
            if (hasOption(LABEL_COL) && widgetType != INPUT_GROUP)
                if (hasNoLabelOption(child)) child.getOptions().put(LABEL_COL, getOptions().getInt(LABEL_COL));
            if (hasOption(TOP_LABEL) && widgetType != INPUT_GROUP)
                if (hasNoLabelOption(child)) child.getOptions().put(TOP_LABEL, true);
        }

        return new Widget(widgetType, type, getOptions(), children, belongsToPrimaryKey);
    }  // end method createWidget

    private void dependsOnExpr(String field)
        throws BuilderException
    {
        with(DEPENDS_ON_EXPR, eq(ref(field), nullValue()).createExpression());
    }

    private void disableChildren() {
        for (final WidgetBuilder childBuilder : childrenBuilders) {
            if (!childBuilder.hasOption(DISABLE)) {
                childBuilder.getOptions().put(DISABLE, getOptions().getExpression(DISABLE));
                childBuilder.disableChildren();
            }
            else if (isGroup(childBuilder.widgetType)) childBuilder.disableChildren();
        }
    }

    private boolean hasNoLabel() {
        return !hasOption(FieldOption.LABEL) && !hasOption(LABEL_EXPRESSION);
    }

    private boolean hasNoLabelOption(Widget child) {
        return !child.getOptions().containsKey(TOP_LABEL) && !child.getOptions().containsKey(LABEL_COL);
    }

    private void incrementSlotCounters(@NotNull final WidgetBuilder widget, UiModelBuilder<?, ?> builder) {
        try {
            final int slot;

            final WidgetType wt = widget.widgetType;
            if (isMultiple(wt)) slot = builder.multipleDimension++;
            else if (wt == SUBFORM) {
                widget.with(SLOT_SUBFORM, subformDimension++);
                slot = fieldDimension++;
            }
            else if (wt == WIDGET) {
                widget.with(SLOT_WIDGET, widgetDefDimension++);
                slot = fieldDimension++;
            }
            else slot = fieldDimension++;

            if (supports(wt, SLOT_FIELD)) widget.with(SLOT_FIELD, slot);
            if (supports(wt, SLOT_GLOBAL_OPTIONS)) {
                widget.with(SLOT_GLOBAL_OPTIONS, builder.globalOptionsDimension++);
                if (isMultiple(widgetType)) widget.with(SLOT_OPTIONS, localOptionsDimension++);
            }
            if (supports(wt, SLOT_CONFIGURATION)) widget.with(SLOT_CONFIGURATION, configureDimension++);
        }
        catch (final BuilderException e) {
            throw new IllegalStateException("Unreachable build error", e);
        }
    }

    @NotNull private Option<MultipleWidget> getMultipleFromFieldName(UiModel form) {
        return form.getElement(getName()).getMultiple();
    }

    //~ Methods ......................................................................................................................................

    /** Returns default widget. */
    public static WidgetBuilder defaultWidget() {
        return widgetBuilder(TEXT_FIELD);
    }

    /** Return a {@link WidgetBuilder} for list widget. */
    public static WidgetBuilder listWidgetBuilder(Type type)
        throws BuilderException
    {
        final WidgetBuilder result = widgetBuilder(type, true, false);

        final String name = ((DbObject) type).getName();
        result.with(ON_LOAD, getOnLoadMethodName(name));
        result.with(ON_CHANGE, getOnChangeMethodName(name));
        result.with(SORTABLE);

        return result;
    }

    /** Return a {@link WidgetBuilder} for the given {@link WidgetType}. */
    public static WidgetBuilder widgetBuilder(WidgetType widgetType) {
        return new WidgetBuilder(widgetType);
    }

    /** Create a Widget Builder for a specific Type. */
    public static WidgetBuilder widgetBuilder(Type type, boolean multiple, boolean synthesized)
        throws BuilderException
    {
        final WidgetType wt = fromType(type.getFinalType(), multiple, synthesized);
        if (wt == null) throw new InvalidTypeForWidgetException(type);

        return widgetBuilder(wt).withType(type);
    }
    private static String getOnChangeMethodName(String name) {
        return "save" + name;
    }
    private static String getOnLoadMethodName(String name) {
        return "load" + pluralize(name);
    }

    private static boolean isTableWidget(final Widget widget) {
        return widget != null && widget.getWidgetType() == TABLE;
    }

    //~ Static Fields ................................................................................................................................

    public static final int GRID_SIZE = 12;
}  // end class WidgetBuilder
