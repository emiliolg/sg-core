
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.dependency.PrecedenceData;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;

import static java.util.Collections.emptyMap;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.common.core.Constants.WIDGET;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.field.FieldOption.SLOT_CONFIGURATION;
import static tekgenesis.field.FieldOption.SLOT_GLOBAL_OPTIONS;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;
import static tekgenesis.metadata.form.widget.WidgetTypes.supports;

/**
 * Base graphical model for classes {@link Form} and {@link WidgetDef}.
 */
@SuppressWarnings("FieldMayBeFinal")  // GWT !
public abstract class UiModel implements Iterable<Widget>, ModelContainer, MetaModel {

    //~ Instance Fields ..............................................................................................................................

    @NotNull QName binding;

    int     configureDimension;
    int     fieldDimension;
    boolean generated;
    QName   modelKey;

    @NotNull final EnumSet<Modifier>    modifiers;
    int                                 multipleDimension;
    FieldOptions                        options;
    int                                 optionsDimension;
    @NotNull transient List<ModelField> parameters;

    PrecedenceData      precedence;
    String              sourceName;
    int                 subformDimension;
    int                 widgetDefDimension;
    Widget[]            widgetsArray;
    Map<String, Widget> widgetsMap;  // todo: add the ordinal to the widget and remove this

    @NotNull private ImmutableList<Widget> children;
    private Widget[]                       subformSlotArray;
    private Widget[]                       widgetDefSlotArray;
    private Widget[]                       widgetsConfigurationArray;
    private Widget[]                       widgetsFieldArray;
    private Widget[]                       widgetsOptionArray;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    UiModel() {
        this(QName.EMPTY,
            "",
            QName.EMPTY,
            emptyList(),
            FieldOptions.EMPTY,
            emptyList(),
            null,
            0,
            0,
            0,
            0,
            0,
            0,
            false,
            EnumSet.noneOf(Modifier.class));
    }

    /** Copy constructor. */
    UiModel(UiModel base, ImmutableList<Widget> children, FieldOptions options) {
        this(base.modelKey,
            base.sourceName,
            base.binding,
            children,
            options,
            base.parameters,
            base.precedence,
            base.multipleDimension,
            base.fieldDimension,
            base.subformDimension,
            base.widgetDefDimension,
            base.optionsDimension,
            base.configureDimension,
            false,
            base.modifiers);
    }

    /** Expanded constructor. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    UiModel(@NotNull QName modelKey, @NotNull String sourceName, @NotNull QName binding, @NotNull ImmutableList<Widget> children,
            @NotNull FieldOptions options, @NotNull List<ModelField> parameters, @Nullable PrecedenceData precedence, int multipleDimension,
            int fieldDimension, int subformDimension, int widgetDefDimension, int optionsDimension, int configureDimension, boolean generated,
            @NotNull EnumSet<Modifier> modifiers) {
        this.modelKey           = modelKey;
        this.sourceName         = sourceName;
        this.binding            = binding;
        this.children           = children;
        this.options            = options;
        this.parameters         = parameters;
        this.multipleDimension  = multipleDimension;
        this.fieldDimension     = fieldDimension;
        this.subformDimension   = subformDimension;
        this.widgetDefDimension = widgetDefDimension;
        this.optionsDimension   = optionsDimension;
        this.configureDimension = configureDimension;
        this.generated          = generated;
        this.modifiers          = modifiers;

        final boolean empty = children.isEmpty();

        widgetsMap                = empty ? emptyMap() : createWidgetsMap();
        widgetsArray              = empty ? EMPTY_WIDGET_ARRAY : widgetsMap.values().toArray(new Widget[widgetsMap.size()]);
        widgetsFieldArray         = empty ? EMPTY_WIDGET_ARRAY : createFieldsArray(this, fieldDimension);
        widgetsOptionArray        = empty ? EMPTY_WIDGET_ARRAY : createOptionsArray(this, optionsDimension);
        subformSlotArray          = empty ? EMPTY_WIDGET_ARRAY : createSubFormsArray(this, subformDimension);
        widgetDefSlotArray        = empty ? EMPTY_WIDGET_ARRAY : createWidgetDefsArray(this, widgetDefDimension);
        widgetsConfigurationArray = empty ? EMPTY_WIDGET_ARRAY : createConfigurationsArray(this, configureDimension);

        this.precedence = precedence;
    }

    //~ Methods ......................................................................................................................................

    /** Return true if form direct children contains the widget. */
    public boolean containsChildren(final Widget widget) {
        return children.contains(widget);
    }

    /** Return true if form contains an element with given name. */
    public boolean containsElement(String name) {
        return widgetsMap.containsKey(name);
    }

    /** Check if parameter assignation is contained. */
    public boolean containsParameter(ModelField field) {
        return parameters.contains(field);
    }

    @Override public boolean equals(Object obj) {
        return obj instanceof UiModel && modelKey.equals(((UiModel) obj).modelKey);
    }

    @Override public int hashCode() {
        return modelKey.hashCode();
    }

    @Override public boolean hasModifier(Modifier mod) {
        return false;
    }

    /** Queries the given permission name over the permissions. */
    public abstract boolean hasPermission(final String permissionName);

    @Override public Iterator<Widget> iterator() {
        return children.iterator();
    }

    /** Resolves all references to binding fields. */
    public void solve(Function<ModelField, ModelField> solver) {
        parameters = map(parameters, solver).toList();
    }

    @Override public String toString() {
        return extractName(getClass().getSimpleName()) + "(" + modelKey + ")";
    }

    /** Returns graphical model binding. */
    @NotNull public QName getBinding() {
        return binding;
    }

    @NotNull @Override public Seq<Widget> getChildren() {
        return children;
    }

    @Override public int getConfigureDimension() {
        return configureDimension;
    }

    /**
     * Returns true if the form can be cached once loaded. Both in the server repository and on the
     * clients browser.
     */
    public boolean isCached() {
        return true;
    }

    /** Returns true if the uiModel has been auto-generated. */
    public boolean isGenerated() {
        return generated;
    }

    /** Return all {@link UiModel} elements (my children and the children of my children). */
    @NotNull public Collection<Widget> getDescendants() {
        return widgetsMap.values();
    }

    @NotNull @Override public String getDomain() {
        return modelKey.getQualification();
    }

    /** Return the element with given name. */
    @NotNull public Widget getElement(String name) {
        final Widget value = widgetsMap.get(name);
        if (value == null) throw new NullPointerException(WIDGET + name + NOT_FOUND + " in form " + getFullName());
        return value;
    }

    @Override public int getFieldDimension() {
        return fieldDimension;
    }

    @NotNull @Override public String getFullName() {
        return modelKey.getFullName();
    }

    /** Return the full name of the user class. */
    public String getImplementationClassFullName() {
        return getDomain() + "." + getImplementationClassName();
    }

    /** Return the name of the user class. */
    public String getImplementationClassName() {
        return capitalizeFirst(getName());
    }

    @NotNull @Override public QName getKey() {
        return modelKey;
    }

    /** Get the Form label. */
    @NotNull public String getLabel() {
        return notEmpty(getRawLabel(), toWords(fromCamelCase(getName())));
    }

    /** Return the number of tables in the graphical model. */
    public int getMultipleDimension() {
        return multipleDimension;
    }

    @NotNull @Override public String getName() {
        return modelKey.getName();
    }

    @Override public int getOptionsDimension() {
        return optionsDimension;
    }

    /** Get a parameter assignation if it is there, else returns none. */
    public Option<ModelField> getParameter(String name) {
        for (final ModelField parameter : parameters) {
            if (parameter.getName().equals(name)) return option(parameter);
        }
        return Option.empty();
    }

    /** Returns parameter ids. */
    @NotNull public Seq<ModelField> getParameters() {
        return immutable(parameters);
    }

    /** Return the precedence Information for the form elements. */
    @NotNull public PrecedenceData getPrecedenceData() {
        return precedence;
    }

    /** Set the precedence Information for the form elements. */
    public void setPrecedenceData(@NotNull final PrecedenceData precedence) {
        this.precedence = precedence;
    }

    /** Get the explicitly set label. May be empty */
    @NotNull public String getRawLabel() {
        return options.getString(FieldOption.LABEL);
    }

    @Override public Seq<MetaModel> getReferences() {
        // TODO Implement it !!
        return emptyIterable();
    }

    @NotNull @Override public String getSchema() {
        return "";
    }

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    /** Return subform matching specified slot. */
    @NotNull public Widget getSubformBySlot(int slot) {
        return subformSlotArray[slot];
    }

    @Override public int getSubformDimension() {
        return subformDimension;
    }

    /** Return the Form elements of that type. */
    @NotNull public Seq<Widget> getUiModelElements(final WidgetType type) {
        return Colls.seq(widgetsMap.values()).filter(widget -> widget != null && widget.getWidgetType() == type);
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    /** Get a widget. */
    public Option<Widget> getWidget(String name) {
        return Option.option(widgetsMap.get(name));
    }

    /** Return the widget for given configuration slot. */
    @NotNull @Override public Widget getWidgetByConfigurationSlot(int slot) {
        return widgetsConfigurationArray[slot];
    }

    /** Return the widget for given field slot. */
    @NotNull @Override public Widget getWidgetByFieldSlot(int slot) {
        return widgetsFieldArray[slot];
    }

    /** Return the widget for given option slot. */
    @NotNull @Override public Widget getWidgetByOptionSlot(int slot) {
        return widgetsOptionArray[slot];
    }

    /** Return the widget for given enum ordinal. */
    @NotNull public Widget getWidgetByOrdinal(int ordinal) {
        return widgetsArray[ordinal];
    }

    /** Return widget definition matching specified slot. */
    @NotNull @Override public Widget getWidgetDefBySlot(int slot) {
        return widgetDefSlotArray[slot];
    }

    @Override public int getWidgetDefDimension() {
        return widgetDefDimension;
    }

    /**
     * Return the redirection from the original ordinal to the new ordinal in the extended model.
     */
    int mapOrdinal(final int ordinal) {
        return ordinal;
    }

    FieldOptions getOptions() {
        return options;
    }

    private Map<String, Widget> createWidgetsMap() {
        final Map<String, Widget> result = new LinkedHashMap<>();
        for (final Widget widget : Colls.deepSeq(this))
            result.put(widget.getName(), widget);
        return result;
    }

    //~ Methods ......................................................................................................................................

    static Widget[] createConfigurationsArray(Iterable<Widget> widgets, int dimension) {
        return createWidgetArray(widgets, dimension, UiModel::configurationOnly, Traverse.INCLUDE_MULTIPLES);
    }

    static Widget[] createFieldsArray(Iterable<Widget> widgets, int dimension) {
        return createWidgetArray(widgets, dimension, o -> true, Traverse.AVOID_MULTIPLES);
    }

    static Widget[] createOptionsArray(Iterable<Widget> widgets, int dimension) {
        return createWidgetArray(widgets, dimension, UiModel::optionOnly, Traverse.TRAVERSE_MULTIPLES);
    }

    static Widget[] createSubFormsArray(Iterable<Widget> widgets, int dimension) {
        return createWidgetArray(widgets, dimension, UiModel::subformOnly, Traverse.AVOID_MULTIPLES);
    }

    static Widget[] createWidgetDefsArray(Iterable<Widget> widgets, int dimension) {
        return createWidgetArray(widgets, dimension, UiModel::widgetDefOnly, Traverse.AVOID_MULTIPLES);
    }

    private static boolean configurationOnly(final Widget widget) {
        return widget != null && supports(widget.getWidgetType(), SLOT_CONFIGURATION);
    }

    private static Widget[] createWidgetArray(@NotNull Iterable<Widget> widgets, int dimension, @NotNull Predicate<Widget> filter,
                                              Traverse traverse) {
        final Widget[] array = new Widget[dimension];
        fillWidgetArray(widgets, array, 0, filter, traverse);
        return array;
    }

    private static int fillWidgetArray(@NotNull Iterable<Widget> widgets, @NotNull Widget[] array, int initial, @NotNull Predicate<Widget> filter,
                                       Traverse traverse) {
        int i = initial;
        for (final Widget widget : widgets) {
            if (traverse.include(widget)) {
                if (filter.test(widget) && array.length > i) array[i++] = widget;
                if (traverse.recursive(widget)) i = fillWidgetArray(widget, array, i, filter, traverse);
            }
        }
        return i;
    }

    private static boolean optionOnly(final Widget widget) {
        return widget != null && supports(widget.getWidgetType(), SLOT_GLOBAL_OPTIONS);
    }

    private static boolean subformOnly(final Widget widget) {
        return widget != null && widget.getWidgetType() == WidgetType.SUBFORM;
    }

    private static boolean widgetDefOnly(final Widget widget) {
        return widget != null && widget.getWidgetType() == WidgetType.WIDGET;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1944085042830072016L;

    private static final Widget[] EMPTY_WIDGET_ARRAY = new Widget[0];

    //~ Enums ........................................................................................................................................

    private enum Traverse {
        AVOID_MULTIPLES, INCLUDE_MULTIPLES, TRAVERSE_MULTIPLES;

        /**
         * Always include widgets, except if multiple-avoidance is specified and widget is multiple.
         */
        private boolean include(@NotNull final Widget widget) {
            return !(this == AVOID_MULTIPLES && isMultiple(widget.getWidgetType()));
        }

        /**
         * Always should be recursive, except if include (but not traverse) is specified for
         * multiples.
         */
        private boolean recursive(@NotNull final Widget widget) {
            return this != INCLUDE_MULTIPLES || !isMultiple(widget.getWidgetType());
        }
    }
}  // end class UiModel
