
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.Aggregate;
import tekgenesis.aggregate.AggregateList;
import tekgenesis.check.Check;
import tekgenesis.check.CheckType;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableList.Builder;
import tekgenesis.common.core.Option;
import tekgenesis.common.media.Mime;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.MetaModelReference;
import tekgenesis.field.ModelField;
import tekgenesis.field.Signed;
import tekgenesis.index.QueryMode;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;
import tekgenesis.type.TypeSupplier;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.metadata.form.model.FormConstants.AUTO_ID_PREFIX;
import static tekgenesis.metadata.form.widget.WidgetTypes.getElementTypeForWidget;

/**
 * The base class for widget Metadata.
 */
@SuppressWarnings({ "FieldMayBeFinal", "ClassWithTooManyMethods", "OverlyComplexClass" })
public class Widget implements Serializable, Iterable<Widget>, ModelField, Signed {

    //~ Instance Fields ..............................................................................................................................

    private boolean belongsToPrimaryKey;

    @NotNull private ImmutableList<Widget> children;

    @NotNull private ElemType elemType;

    @NotNull private TypeSupplier    modelType;
    @Nullable private MultipleWidget multiple;

    @NotNull private FieldOptions   options;
    @NotNull private transient Type originalType;
    @NotNull private WidgetType     widgetType;

    //~ Constructors .................................................................................................................................

    /** Gwt. */
    Widget() {
        this(WidgetType.LABEL);
    }

    Widget(@NotNull WidgetType widgetType) {
        this.widgetType = widgetType;
        multiple        = null;
        elemType        = ElemType.SCALAR;
        options         = FieldOptions.EMPTY;
        originalType    = Types.nullType();
        modelType       = new TypeSupplier(originalType);
        children        = emptyList();
    }

    Widget(@NotNull final WidgetType widgetType, @NotNull Type type, @NotNull FieldOptions options, ImmutableList<Widget> children,
           boolean belongsToPrimaryKey) {
        this(widgetType);
        this.belongsToPrimaryKey = belongsToPrimaryKey;
        this.options             = options;
        elemType                 = getElementTypeForWidget(widgetType, options);
        this.children            = children.isEmpty() ? emptyList() : children;
        originalType             = type;
        modelType                = new TypeSupplier(resolveModelType(type.getFinalType()));
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if the attribute associated with the widget belongs to the primary key. */
    public boolean belongsToPrimaryKey() {
        return belongsToPrimaryKey;
    }

    /** Returns true if the widget has an axis defined. */
    public boolean hasAxis() {
        return options.hasOption(AXIS);
    }

    /**
     * Returns true if SuggestBox/TagsSuggestBox suggest's delay or TextField delay are available.
     */
    public boolean hasChangeDelay() {
        return options.hasOption(CHANGE_DELAY);
    }

    /** Returns true if the Widget has Children Widgets. */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /** returns true if the name of the widget is auto generated. */
    public boolean hasGeneratedName() {
        return getName().charAt(0) == AUTO_ID_PREFIX;
    }

    /** Returns true if widget has hover option. */
    public boolean hasHover() {
        return options.getBoolean(HOVER);
    }

    /** Returns true if this widget has icon. */
    public boolean hasIcon() {
        return options.hasOption(ICON) || options.hasOption(ICON_EXPR);
    }

    /** Returns true when this field is set 'with_image'. */
    public boolean hasImage() {
        return options.getBoolean(WITH_IMAGE);
    }

    /** Returns true if the widget label is its main component. */
    public boolean hasInnerLabel() {
        return widgetType == WidgetType.LABEL || widgetType == WidgetType.DISPLAY;
    }

    /** Returns true if this widget has label. */
    public boolean hasLabel() {
        return !getNoLabel() && (options.hasOption(LABEL) || options.hasOption(LABEL_EXPRESSION));
    }

    /** Returns true if has metering defined. */
    public boolean hasMetering() {
        return options.hasOption(METERING);
    }

    /** Returns true if the widget has an on_change method defined. */
    public boolean hasOnChangeMethod() {
        return isNotEmpty(getOnChangeMethodName()) || options.hasOption(ON_CHANGE_FN);
    }

    /** Returns true if the widget has an on_click method defined. */
    public boolean hasOnClickMethod() {
        return isNotEmpty(getOnClickMethodName()) || options.hasOption(ON_CLICK_FN);
    }

    /** Returns true when this field is set to 'with_timezone'. */
    public boolean hasTimezone() {
        return options.getBoolean(WITH_TIMEZONE);
    }

    /** returns true if the Widget has an associated value. */

    public boolean hasValue() {
        return WidgetTypes.hasValue(widgetType);
    }

    @Override public Iterator<Widget> iterator() {
        return children.iterator();
    }

    /** Serialize the Widget. */
    // public void serialize(StreamWriter w) {
    // w.writeInt(type.ordinal());
    // Kind.serializeType(w, modelType.get());
    // w.writeInt(elemType.ordinal());
    // options.serialize(w);
    // }

    @Override public String toString() {
        return widgetType + "(" + getName() + ")";
    }

    /** Returns true if time pickers should show 24:00. */
    public boolean isMidnightAs24() {
        return options.getBoolean(MIDNIGHT_AS_24);
    }

    /** Return the Widget affix. */
    public String getAffix() {
        return options.getString(AFFIX);
    }

    /** Get the Aggregate Expressions for the Widget. */
    @NotNull public AggregateList getAggregates() {
        return options.getAggregate(AGGREGATE);
    }

    /** Return the predefined mask. */
    public Alignment getAlignment() {
        return options.getEnum(ALIGN, Alignment.class, Alignment.LEFT);
    }

    /** Get the axis identifier of a chart's column widget. */
    public int getAxis() {
        return options.getInt(AXIS);
    }

    /** Returns true if widget should skip tab focus. */
    public boolean isSkipTab() {
        return options.getBoolean(SKIP_TAB);
    }

    /** Returns the entity field we are bound to. */
    @NotNull public String getBinding() {
        return options.getString(BINDING);
    }

    /** Get the associated Button Bound Id. */
    public String getButtonBoundId() {
        return options.getString(BUTTON_BOUND_ID);
    }

    /** Returns the button type. */
    public ButtonType getButtonType() {
        return options.getEnum(BUTTON_TYPE, ButtonType.class, ButtonType.CUSTOM);
    }

    /** Return upload camera support.. */
    public Boolean getCamera() {
        return options.getBoolean(CAMERA);
    }

    /** Returns SuggestBox/TagsSuggestBox suggest's delay or TextField delay in ms. */
    public int getChangeDelay() {
        return options.getInt(CHANGE_DELAY);
    }

    /**
     * Returns SuggestBox/TagsSuggestBox suggest's and TextField change's threshold in number of
     * chars.
     */
    public int getChangeThreshold() {
        return options.getInt(CHANGE_THRESHOLD);
    }

    /** Return chart type. */
    public ChartType getChartType() {
        return options.getEnum(CHART_TYPE, ChartType.class, ChartType.COLUMN);
    }

    /** Get the Check Expressions for the Widget. */
    @NotNull public Check.List getChecks() {
        return options.getCheck(CHECK);
    }

    /** Return the Children. */
    @NotNull public ImmutableList<? extends ModelField> getChildren() {
        return children;
    }

    /** Get the Col number. */
    public int getCol() {
        return options.getInt(COL);
    }

    /** Get the Expression to calculate a Row Style class. */
    @NotNull public Expression getColumnStyleClassExpression() {
        return options.getExpression(COLUMN_STYLE);
    }

    /** Returns the widget configuration slot. */
    public int getConfigurationSlot() {
        return options.getInt(SLOT_CONFIGURATION);
    }

    /** Get an Expression to calculate a Hint text. */
    @NotNull public Expression getConfirm() {
        return options.getExpression(CONFIRM);
    }

    /** Get the Expression to calculate content Style class. */
    @NotNull public Expression getContentStyleClassExpression() {
        return options.getExpression(CONTENT_STYLE);
    }

    /** Get the Expression to calculate the custom mask. */
    public Expression getCustomMaskExpression() {
        return options.getExpression(CUSTOM_MASK);
    }

    /** Return the Full variant. */
    public String getCustomVariant() {
        return options.getString(CUSTOM_VARIANT);
    }

    /** Return expected length. */
    public boolean isExpand() {
        return options.getBoolean(EXPAND);
    }

    /** Returns true if this widget was NOT created by the user. */
    public boolean isOriginalField() {
        return options.getBoolean(ORIGINAL_FORM_FIELD);
    }

    /** Returns true if expression is always required. */
    public boolean isRequired() {
        return getOptionalExpression() == Expression.FALSE;
    }

    /** Returns true when the number can be positive or negative. */
    @Override public boolean isSigned() {
        return options.getBoolean(SIGNED) && !options.getBoolean(UNSIGNED);
    }

    /** Return date type. */
    public DateType getDateType() {
        return options.getEnum(DATE_TYPE, DateType.class, DateType.SHORT_FORMAT);
    }

    /** Get the Expression for the default value. */
    @NotNull public Expression getDefaultValueExpression() {
        return options.getExpression(DEFAULT);
    }

    /** Gets the field dependency ref. */
    @NotNull public String getDependsOn() {
        return options.getString(DEPENDS_ON);
    }

    /** Gets the field dependency expression. */
    @NotNull public Expression getDependsOnExpression() {
        return options.getExpression(DEPENDS_ON_EXPR);
    }

    /** Get the Disable Expressions for the Widget. */
    @NotNull public Expression getDisableExpression() {
        return options.getExpression(DISABLE);
    }

    /** Get the Expression for the 'display' value on sub forms. */
    @NotNull public Expression getDisplayExpression() {
        return options.getExpression(DISPLAY);
    }

    /** Returns true if suggest box is bounded to a deprecable entity. */
    public boolean isBoundedToDeprecable() {
        return options.getBoolean(BOUNDED_TO_DEPRECABLE);
    }

    /** Get the Expression to calculate Group type. */
    public boolean isCollapsible() {
        return options.getBoolean(COLLAPSIBLE);
    }

    /** Returns true if map marker or multiple rows are draggable. */
    public boolean isDraggable() {
        return options.getBoolean(DRAGGABLE);
    }

    /** Returns true if the table is filterable. */
    public boolean isFilterable() {
        return options.getBoolean(FILTERABLE);
    }

    /** Get if the subform is to be displayed inline. */
    public boolean isInline() {
        return options.getBoolean(INLINE);
    }

    /** Returns true is it has multiple selection. */
    public boolean isMultiple() {
        return options.getBoolean(MULTIPLE);
    }

    /** Returns true if the field resets when not visible. */
    public boolean isResettable() {
        return options.getBoolean(RESET);
    }

    /** Returns true if section is scrollable. */
    public boolean isScrollable() {
        return options.getBoolean(SCROLLABLE);
    }

    /** Returns true if the table rows are sortable. */
    public boolean isSortable() {
        return options.getBoolean(SORTABLE);
    }

    /** Returns true if the field is unique. */
    public boolean isUnique() {
        return options.getBoolean(UNIQUE);
    }

    /** Returns the Effective Widget Type (WidgetType may be shadowed by Dynamics). */
    @NotNull public WidgetType getEffectiveWidgetType() {
        return widgetType;
    }

    /** Return the {@link ElemType} of the widget. */
    @NotNull public ElemType getElemType() {
        return elemType;
    }

    /** Gets export type. */
    @NotNull public ExportType getExportType() {
        return options.getEnum(EXPORT_TYPE, ExportType.class, ExportType.CSV);
    }

    /** all the widget Expressions. */
    @NotNull public ImmutableList<Expression> getExpressionList() {
        final Builder<Expression> result = builder();

        for (final FieldOption option : options) {
            if (option.getType().isExpression()) addExpression(result, options.getExpression(option));
        }

        for (final Check e : getChecks())
            addExpression(result, e.getExpr());

        for (final Aggregate a : getAggregates())
            addExpression(result, a.getExpr());

        return result.build();
    }

    /** Return true if widget has {@link WidgetType#WIDGET}. */
    public boolean isWidgetDef() {
        return getWidgetType() == WidgetType.WIDGET;
    }

    /** Returns the slot of the widget (used to store info in the data model). */
    public int getFieldSlot() {
        return options.getInt(SLOT_FIELD);
    }

    /** Returns the accepted file type. */
    public Mime getFileType() {
        return options.getEnum(FILE_TYPE, Mime.class, Mime.ALL);
    }

    /** Get the Suggest Box filtering expression. */
    @NotNull public Expression getFilterExpression() {
        return options.getExpression(FILTER);
    }

    /** Get filtering relation. */
    @NotNull public String getFiltering() {
        return options.getString(FILTERING);
    }

    /** Get the subform footer type. */
    @NotNull public FooterType getFooterType() {
        return options.getEnum(FOOTER_TYPE, FooterType.class, FooterType.OK_CANCEL);
    }

    /** Get the expression that determines the 'from' value. */
    @NotNull public Expression getFromExpression() {
        return options.getExpression(FROM);
    }

    /** Has placeholder binding. */
    public boolean isPlaceholderBinding() {
        return options.getBoolean(PLACEHOLDER_BINDING);
    }

    /** Global options slot. */
    public int getGlobalOptionsSlot() {
        return options.getInt(SLOT_GLOBAL_OPTIONS);
    }

    /** Returns true if the table must have lazy fetch rows on server. */
    public boolean isLazyFetch() {
        return options.getBoolean(LAZY_FETCH);
    }

    /** Get the expression that determines if the widget must be hidden. */
    @NotNull public Expression getHideColumnExpression() {
        return options.getExpression(HIDE_COLUMN);
    }

    /** Get the expression that determines if the widget must be hidden. */
    @NotNull public Expression getHideExpression() {
        return options.getExpression(HIDE);
    }

    /** Get an Expression to calculate a Hint text. */
    @NotNull public Expression getHint() {
        return options.getExpression(HINT);
    }

    /** Get an Expression to calculate Icon Type. */
    @NotNull public String getIconSelectedStyle() {
        return options.getEnum(ICON_SELECTED, IconType.class, IconType.NONE).getClassName();
    }

    /** Get an Expression to calculate Icon Type. */
    @NotNull public String getIconStyle() {
        return getIconStyleEnum().getClassName();
    }

    /** Get an Expression to calculate Icon Type. */
    @NotNull public IconType getIconStyleEnum() {
        return options.getEnum(ICON, IconType.class, IconType.NONE);
    }

    /** Get the Expression to calculate Style class. */
    @NotNull public Expression getIconStyleExpression() {
        return options.getExpression(ICON_EXPR);
    }

    /** Get the Expression to calculate inline style. */
    @NotNull public Expression getInlineStyleExpression() {
        return options.getExpression(INLINE_STYLE);
    }

    /** Get the InputHandler for the Widget. */
    public InputHandlerMetadata getInputHandler() {
        return InputHandlerMetadata.inheritFromType();
    }

    /** Get the Expression for the 'is' (automatic calculated) value. */
    @NotNull public Expression getIsExpression() {
        return options.getExpression(IS);
    }

    /** The 'feedback' option of the widget. */
    public boolean isFeedback() {
        return options.getBoolean(FEEDBACK);
    }

    /** Returns true if link is targeted blank (open in new tab). */
    public boolean isTargetBlank() {
        return options.getBoolean(TARGET_BLANK);
    }

    /** Returns true . */
    public boolean isTopLabel() {
        return options.getBoolean(TOP_LABEL);
    }

    /** Get the widget label. */
    @NotNull public String getLabel() {
        return options.getString(LABEL);
    }

    /** Get the Col number. */
    public int getLabelCol() {
        return options.getInt(LABEL_COL);
    }

    /** Get the widget label expression. */
    @NotNull public Expression getLabelExpression() {
        return options.getExpression(LABEL_EXPRESSION);
    }

    /** Return the Large variant. */
    public String getLargeVariant() {
        return options.getString(LARGE_VARIANT);
    }

    /** Get the name of the method to call on lazy fetch load. */
    @NotNull public String getLazyFetchMethodName() {
        return options.getString(ON_LAZY_FETCH);
    }

    /** the length of the field. */
    public int getLength() {
        return options.getInt(LENGTH);
    }

    /** Returns the href expression for LinkUI's href attribute. */
    @NotNull public Expression getLinkExpression() {
        return options.getExpression(LINK);
    }

    /** Get the name of the Form to invoke when the users clicks the link. */
    @NotNull public MetaModelReference getLinkForm() {
        return options.getMetaModelReference(LINK_FORM);
    }

    /** Get the pk of the Form to invoke when the users clicks the link. */
    @NotNull public Expression getLinkPkExpression() {
        return options.getExpression(LINK_PK);
    }

    /** Local options slot. Valid only for a multiple cell with Options */
    public int getLocalOptionsSlot() {
        return options.getInt(SLOT_OPTIONS);
    }

    /** Return true if widget has {@link WidgetType#SUBFORM}. */
    public boolean isSubform() {
        return getWidgetType() == WidgetType.SUBFORM;
    }

    /** Return mail validation type. */
    public MailValidationType getMailValidationType() {
        return options.getEnum(MAIL_VALIDATION_TYPE, MailValidationType.class, MailValidationType.ALL);
    }

    /** Return map type. */
    public MapType getMapType() {
        return options.getEnum(MAP_TYPE, MapType.class, MapType.OPENSTREET);
    }

    /** Return expected length. */
    public int getMaxWidth() {
        final int width = options.getInt(WIDTH);
        return width == 0 ? FormConstants.DEFAULT_LENGTH : width;
    }

    /** Gets metering value. */
    @NotNull public Expression getMetering() {
        return options.getExpression(METERING);
    }

    /** Returns the type of the msg. */
    public CheckType getMsgType() {
        return options.getEnum(CHECK_TYPE, CheckType.class, CheckType.INFO);
    }

    /** Get the multiple widget to which this widget belongs or none(). */
    @NotNull public Option<MultipleWidget> getMultiple() {
        return option(multiple);
    }

    /** Returns true if widget invocation is abstract and should be delegated to parent. */
    public boolean isAbstractInvocation() {
        return options.getBoolean(ABSTRACT_INVOCATION);
    }

    /** returns the name (id) of the widget. */
    @NotNull public String getName() {
        return options.getString(ID);
    }

    /** Get the Col number. */
    public boolean getNoLabel() {
        return options.getBoolean(NO_LABEL);
    }

    /** The 'noPaste' option of the widget. */
    public boolean getNoPaste() {
        return options.getBoolean(NOPASTE);
    }

    /** Get the Col number. */
    public int getOffsetCol() {
        return options.getInt(OFFSET_COL);
    }

    /** Get the form anchor where the subform will be placed. */
    @NotNull public String getOnAnchor() {
        return options.getString(ON);
    }

    /** Get the name of the method to call on blur. */
    @NotNull public String getOnBlurMethodName() {
        return options.getString(ON_BLUR);
    }

    /** Get the name of the method to call on change. */
    @NotNull public String getOnChangeMethodName() {
        return options.getString(ON_CHANGE);
    }

    /** Get the name of the method to call on change. */
    @Nullable
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public Function<?, ?> getOnChangeMethodRef() {
        return options.getMethodRef(ON_CHANGE_FN);
    }

    /** Get the name of the method to call on click. */
    @NotNull public String getOnClickMethodName() {
        return options.getString(ON_CLICK);
    }

    /** Get the name of the method to call on click. */
    @Nullable
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public Function<?, ?> getOnClickMethodRef() {
        return options.getMethodRef(ON_CLICK_FN);
    }

    /** Get the name of the method to call on load. */
    @NotNull public String getOnLoadMethodName() {
        return options.getString(ON_LOAD);
    }

    /** Get the name of the Form to invoke when the 'Create new' option is clicked. */
    @NotNull public MetaModelReference getOnNewForm() {
        return options.getMetaModelReference(ON_NEW_FORM);
    }

    /** Get the name of the method to call on new location. */
    @NotNull public String getOnNewLocationMethodName() {
        return options.getString(ON_NEW_LOCATION);
    }

    /** Get the name of the method to call when the 'Create new' option is clicked. */
    @NotNull public String getOnNewMethodName() {
        return options.getString(ON_NEW);
    }

    /** Get the expression for the on selection method. */
    @NotNull public Expression getOnSelectionExpression() {
        return options.getExpression(ON_SELECTION_EXPR);
    }

    /** Get the name of the method to call on a selection change. */
    @NotNull public String getOnSelectionMethodName() {
        return options.getMethod(ON_SELECTION);
    }

    /** Get the name of the method to call when building suggestions. */
    @NotNull public Expression getOnSuggestExpr() {
        return options.getExpression(ON_SUGGEST_EXPR);
    }

    /** Get the name of the method to call when building suggestions. */
    @NotNull public String getOnSuggestMethodName() {
        return options.getMethod(ON_SUGGEST);
    }

    /** Get the name of the method to call when building suggestions. */
    @NotNull public String getOnSuggestSyncMethodName() {
        return options.getString(ON_SUGGEST_SYNC);
    }

    /**
     * Get the name of the method to call on ui change (changes make on the ui by user interaction).
     */
    @NotNull public String getOnUiChangeMethodName() {
        return options.getString(ON_UI_CHANGE);
    }

    /** Get the expression that determines if the widget is optional. */
    @NotNull public Expression getOptionalExpression() {
        return options.getExpression(OPTIONAL);
    }

    /** Return all Widget Options. */
    @NotNull public FieldOptions getOptions() {
        return options;
    }

    /** Returns the global ordinal of the widget (used to get the widget by index from the Form). */
    public int getOrdinal() {
        return options.getInt(ORDINAL);
    }

    /** Get the the original type *The one specified when building the widget. */
    @NotNull public Type getOriginalType() {
        return originalType;
    }

    /** Get an Expression to calculate Placeholders. */
    @NotNull public Expression getPlaceholderExpression() {
        return options.getExpression(PLACEHOLDER);
    }

    /** Return popover type. */
    public PopoverType getPopoverType() {
        return options.getEnum(POPOVER_TYPE, PopoverType.class, PopoverType.RIGHT);
    }

    /** Return the predefined mask. */
    public PredefinedMask getPredefinedMask() {
        return options.getEnum(MASK, PredefinedMask.class, PredefinedMask.NONE);
    }

    /** Returns true if SuggestBox/TagsSuggestBox is strict. */
    public QueryMode getQueryMode() {
        return options.getEnum(QUERY_MODE, QueryMode.class, QueryMode.PREFIX);
    }

    /** Return rating style. */
    public RatingType getRatingType() {
        return options.getEnum(RATING_TYPE, RatingType.class, RatingType.STARS);
    }

    /** Returns time to reset when user changes date. */
    public String getResetTime() {
        return options.getString(RESET_TIME);
    }

    /** Get the Expression to calculate a Row Style class. */
    @NotNull public Expression getRowStyleClassExpression() {
        return options.getExpression(ROW_STYLE);
    }

    /** Get the Expression to calculate a Row Style class. */
    @NotNull public Expression getRowStyleExpression() {
        return options.getExpression(ROW_INLINE_STYLE);
    }

    /** The 'synchronous' option of the widget. */
    public boolean isSynchronous() {
        return options.getBoolean(SYNCHRONOUS);
    }

    /** Returns Widget keyboard shortcut. */
    public String getShortcut() {
        return options.getString(SHORTCUT);
    }

    /** Returns the step. */
    public int getStep() {
        return options.getInt(STEP);
    }

    /** Get the Expression to calculate Style class. */
    @NotNull public Expression getStyleClassExpression() {
        return options.getExpression(STYLE);
    }

    /** Subform reference. */
    public String getSubformFqn() {
        return options.getMetaModelReference(SUBFORM_ID).getFullName();
    }

    /** Get subform slot. */
    public int getSubformSlot() {
        return options.getInt(SLOT_SUBFORM);
    }

    /** Returns true if widget must show full text (not cover text). */
    public boolean isFullText() {
        return options.getBoolean(FULL_TEXT);
    }

    /** Returns true if this text field should hide chars as typing. */
    public boolean isSecret() {
        return options.getBoolean(SECRET);
    }

    /** Get if DropDown should be SPLIT. */
    public boolean isSplit() {
        return options.getBoolean(SPLIT) && isEmpty(getLabel());
    }

    /** Return expected length. */
    public int getTableColumnWidth() {
        return options.getInt(WIDTH);
    }

    /** Return tab type. */
    public TabType getTabType() {
        return options.getEnum(TAB_TYPE, TabType.class, TabType.TOP);
    }

    /** Get the Length Expressions for the Widget. */
    @NotNull public Expression getTextLengthExpression() {
        return options.getExpression(TEXT_LENGTH);
    }

    /** Return the Thumb variant. */
    public String getThumbVariant() {
        return options.getString(THUMB_VARIANT);
    }

    /** Get the expression that determines the 'to' value. */
    @NotNull public Expression getToExpression() {
        return options.getExpression(TO);
    }

    /** Returns the toggle button type. */
    public ToggleButtonType getToggleButtonType() {
        return options.getEnum(TOGGLE_BUTTON_TYPE, ToggleButtonType.class, ToggleButtonType.CUSTOM);
    }

    /** Get an Expression to calculate a Tooltip text. */
    @NotNull public Expression getTooltip() {
        return options.getExpression(TOOLTIP);
    }

    /** Get the {@link Type} for the Widget. */
    @NotNull public Type getType() {
        return modelType.get();
    }

    @Override public void setType(@NotNull Type type) {
        throw new IllegalStateException();
    }

    /** THe number of visible Rows of the Widget. */
    public int getVisibleRows() {
        return options.getInt(ROWS);
    }

    /** Get if InputGroup should be Flow layout. */
    public boolean isFlow() {
        return options.getBoolean(FLOW);
    }

    /** Widget definition reference. */
    public String getWidgetDefinitionFqn() {
        return options.getMetaModelReference(WIDGET_DEF).getFullName();
    }

    /** Get widget definition slot. */
    public int getWidgetDefSlot() {
        return options.getInt(SLOT_WIDGET);
    }

    /** Returns the Widget Type. */
    @NotNull public WidgetType getWidgetType() {
        return widgetType;
    }

    /** Is bounded to an Entity? */
    public boolean isEntity() {
        final Type type = getType();
        if (type.isArray()) return ((ArrayType) type).getElementType().isEntity();
        else return type.isEntity();
    }

    /** Returns true if the table must have lazy paging. */
    public boolean isLazy() {
        return options.getBoolean(LAZY);
    }

    /** Returns true if the button saves an entity. */
    public boolean isSavesEntity() {
        return options.getBoolean(SAVES_ENTITY);
    }

    Widget copy(FieldOptions opts, ImmutableList<Widget> widgets) {
        return new Widget(widgetType, modelType.get(), opts, widgets, belongsToPrimaryKey);
    }

    void setElemType(@NotNull ElemType elemType) {
        this.elemType = elemType;
    }

    /** Sets the multiple element holding this widget. */
    void setMultiple(@Nullable MultipleWidget multiple) {
        this.multiple = multiple;
    }

    private Type resolveModelType(@NotNull final Type type) {
        return elemType.isArray() && !type.isArray() ? Types.arrayType(type) : type;
    }

    //~ Methods ......................................................................................................................................

    private static void addExpression(Builder<Expression> expressionList, Expression expression) {
        if (!expression.isConstant()) expressionList.add(expression);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3062225171060554326L;

    //~ Enums ........................................................................................................................................

    /**
     * Model Element Type.
     */
    public enum ElemType {
        SCALAR, ARRAY, MULTIPLE, COLUMN, SUBFORM, WIDGET;

        /** Return true if is array. */
        public boolean isArray() {
            return this == ARRAY;
        }
    }
}  // end class Widget
