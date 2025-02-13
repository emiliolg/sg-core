
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.media.Mime;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.HasFieldOption;
import tekgenesis.index.QueryMode;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.DEFAULT_STRING_LENGTH;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.metadata.form.widget.Widget.ElemType;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetType.DISPLAY;
import static tekgenesis.metadata.form.widget.WidgetType.LABEL;
import static tekgenesis.type.Kind.*;
import static tekgenesis.type.Types.*;

/**
 * Utility class to factor common methods of {@link WidgetType}. This should not be needed on the
 * Client Side so it is factor here....
 */
@SuppressWarnings({ "NonJREEmulationClassesInClientCode", "ClassWithTooManyMethods" })
public class WidgetTypes {

    //~ Constructors .................................................................................................................................

    private WidgetTypes() {}

    //~ Methods ......................................................................................................................................

    /** Return either the FieldOption it-self or an ExtendedFieldOption). */
    @NotNull public static HasFieldOption asHasFieldOption(@NotNull FieldOption option) {
        return notNull(extendedOptions.get(option), option);
    }

    /** returns true if field option is compatible with already assigned field options. */
    @Nullable public static FieldOption compatible(FieldOption option, FieldOptions options) {
        if (option != CHECK && option != AGGREGATE && options.containsKey(option)) return option;
        final EnumSet<FieldOption> incompatibleOptions = incompatible.get(option);
        if (incompatibleOptions == null) return null;
        for (final FieldOption fieldOption : options) {
            if (incompatibleOptions.contains(fieldOption)) return fieldOption;
        }
        return null;
    }

    /** Returns a WidgetType based on a \n String or <code>null</code> if it doesn't exists. */
    @Nullable public static WidgetType fromId(String nm) {
        return idToEnum.get(nm);
    }

    /** Returns the default Widget for the type. */
    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public static WidgetType fromType(Type type, boolean multiple, boolean synthesized)
        throws IllegalArgumentException
    {
        if (synthesized) return DISPLAY;
        switch (type.getKind()) {
        case DECIMAL:
        case REAL:
        case INT:
            return TEXT_FIELD;
        case STRING:
            final int len = type.getLength().orElse(0);
            return len <= DEFAULT_STRING_LENGTH ? TEXT_FIELD : TEXT_AREA;
        case DATE_TIME:
            return DATE_TIME_BOX;
        case DATE:
            return DATE_BOX;
        case ENUM:
            return multiple ? TAGS_COMBO_BOX : COMBO_BOX;
        case BOOLEAN:
            return CHECK_BOX;
        case REFERENCE:
            return type.isEnum() ? multiple ? TAGS_COMBO_BOX : COMBO_BOX : multiple ? WidgetType.TABLE : SUGGEST_BOX;
        case RESOURCE:
            return UPLOAD;
        case ARRAY:
            return ((ArrayType) type).getElementType().isEnum() && multiple ? TAGS_COMBO_BOX : null;
        default:
            return null;
        }
    }

    /** returns true if the Widget has an argument with the specified option type. */
    public static boolean hasArgument(WidgetType widgetType, FieldOption option) {
        final List<HasFieldOption> args = arguments.get(widgetType);
        if (args != null) {
            for (final HasFieldOption arg : args) {
                if (arg.getFieldOption() == option) return true;
            }
        }
        return false;
    }

    /** Returns true if the Widget has options. */
    public static boolean hasOptions(WidgetType widgetType) {
        return OPTIONS_WIDGETS.contains(widgetType);
    }

    /** Returns true if the Widget has a value in the form model. */
    public static boolean hasValue(WidgetType widgetType) {
        return VALUED_WIDGETS.contains(widgetType);
    }

    /** Returns other required field options for the passed field option. */
    @NotNull public static Seq<FieldOption> requires(FieldOption option) {
        final List<FieldOption> r = required.get(option);
        return r == null ? Colls.emptyIterable() : Colls.seq(r);
    }

    /** Returns true if the Specified type supports the specified option. */
    public static boolean supports(WidgetType type, FieldOption option) {
        final EnumSet<WidgetType> widgetTypes = supports.get(option);
        return widgetTypes != null && widgetTypes.contains(type);
    }

    /** Returns true if the widget supports the specified type. */
    public static boolean supports(WidgetType wt, Type type) {
        final TypeSupports t = typeForWidget.get(wt);
        return t != null && t.supports(type);
    }
    /** Returns true if widget supports list. */
    public static boolean supportsList(WidgetType widgetType) {
        return isGroup(widgetType) || isMultiple(widgetType) || widgetType == WidgetType.FORM;
    }

    /** Return the widget's nth argument type or null. */
    @Nullable public static HasFieldOption getArgument(WidgetType option, int n) {
        final List<HasFieldOption> args = arguments.get(option);
        if (args == null || n >= args.size()) return null;
        return args.get(n);
    }
    /** Return the arguments for the widget type. */
    @NotNull public static List<HasFieldOption> getArguments(WidgetType option) {
        final List<HasFieldOption> args = arguments.get(option);
        return args == null ? Colls.emptyList() : args;
    }

    /** Returns true if the Widget has arrayed values. */
    public static boolean isArrayValued(WidgetType widgetType) {
        return ARRAY_WIDGETS.contains(widgetType);
    }

    /**
     * Returns the default init value of the widget. This is for widget that can't be null (ej:
     * false to boolean) or that have a default value (ej: images and internals of type String)
     */
    @Nullable public static Object getDefaultInitValue(Widget widget) {
        final Type type = widget.getType();
        return (type.isBoolean() && (widget.isRequired() || widget.getWidgetType() != DISPLAY)) ||
               (widget.isRequired() && DEFAULT_INIT_VALUE_WIDGETS.contains(widget.getWidgetType())) ? type.getDefaultValue() : null;
    }

    /** Returns true if the widget is a multiple type. */
    public static boolean isMultiple(WidgetType type) {
        return MULTIPLE_WIDGETS.contains(type);
    }

    /** Returns true if the Widget is a button widget. */
    public static boolean isButtonOrLabel(WidgetType widgetType) {
        return widgetType == LABEL || BUTTON_WIDGETS.contains(widgetType);
    }

    /** gets all the options available for the requested widget type. */
    public static Set<FieldOption> getOptionsForWidgetType(WidgetType widgetType) {
        return optionsSupported.get(widgetType);
    }

    /** Returns true if the widget is a group widget. */
    public static boolean isGroup(WidgetType type) {
        return GROUP_WIDGETS.contains(type);
    }

    /** Returns related option if defined. */
    @Nullable public static FieldOption getRelated(FieldOption option) {
        return related.get(option);
    }

    public static boolean isDateRangeWidget(@NotNull final WidgetType widgetType) {
        return RANGED_WIDGETS.contains(widgetType);
    }

    /** Get the Type handled by the widget. */
    public static Type getTypeFor(WidgetType widgetType) {
        final TypeSupports t = typeForWidget.get(widgetType);
        return t == null ? nullType() : t.getDefaultType();
    }

    /** Returns true if the Widget will be showing the entity describeBy. */
    public static boolean isShowingEntity(WidgetType widgetType) {
        return SHOWING_ENTITY_WIDGETS.contains(widgetType);
    }

    /** Returns the element type that corresponds to the widget type. */
    static ElemType getElementTypeForWidget(@NotNull final WidgetType type, @NotNull final FieldOptions options) {
        return type == SUBFORM ? ElemType.SUBFORM : type == WIDGET ? ElemType.WIDGET : options.hasOption(MULTIPLE) ? ElemType.ARRAY
                                                                                                                   : ElemType.SCALAR;
    }

    private static EnumSet<WidgetType> allAnd(EnumSet<WidgetType> base, WidgetType... other) {
        final EnumSet<WidgetType> copy = EnumSet.copyOf(base);
        copy.addAll(asList(other));
        return copy;
    }

    private static EnumSet<WidgetType> allBut(EnumSet<WidgetType> base, WidgetType... other) {
        return allBut(base, asList(other));
    }

    private static EnumSet<WidgetType> allBut(EnumSet<WidgetType> base, Collection<WidgetType> other) {
        final EnumSet<WidgetType> copy = EnumSet.copyOf(base);
        copy.removeAll(other);
        return copy;
    }

    private static void argumentFor(WidgetType type, HasFieldOption opt) {
        arguments.put(type, singletonList(opt));
    }
    private static void argumentsFor(WidgetType type, HasFieldOption... opt) {
        arguments.put(type, asList(opt));
    }

    private static <T extends Enum<T>> void extendOption(FieldOption field, Class<T> other) {
        extendedOptions.put(field, field.withClass(other));
    }
    private static void incompatible(FieldOption field, EnumSet<FieldOption> option) {
        incompatible.put(field, option);
    }
    private static void incompatibles(FieldOption field, FieldOption option) {
        incompatible(field, of(option));
        incompatible(option, of(field));
    }

    private static void relatedByOption(FieldOption field, FieldOption other) {
        related.put(field, other);
    }

    private static void requiresAny(FieldOption field, FieldOption... requiredFields) {
        required.put(field, asList(requiredFields));
    }

    private static void supportsByOption(FieldOption field, WidgetType first, WidgetType... types) {
        supports.put(field, of(first, types));
    }

    private static void supportsByType(EnumSet<WidgetType> types, FieldOption... field) {
        for (final FieldOption fieldOption : field)
            supports.put(fieldOption, types);
    }
    private static void typeFor(WidgetType widget, Type defaultType, Kind... supportedKinds) {
        typeForWidget.put(widget, new TypeSupports(defaultType, supportedKinds));
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_TEXT_FIELD_LENGTH = 40;

    public static final EnumSet<WidgetType> ALL_WIDGETS = complementOf(of(WidgetType.NONE));

    private static final EnumSet<WidgetType> GROUP_WIDGETS = of(VERTICAL, HORIZONTAL, HEADER, FOOTER, TABS, DIALOG, DROPDOWN, POPOVER, INPUT_GROUP);

    private static final EnumSet<WidgetType> MULTIPLE_WIDGETS = of(WidgetType.TABLE, CHART, SECTION, MAP);

    private static final EnumSet<WidgetType> BUTTON_WIDGETS = of(BUTTON, TOGGLE_BUTTON, DROPDOWN);

    private static final EnumSet<WidgetType> OPTIONS_WIDGETS = of(COMBO_BOX,
            LIST_BOX,
            RADIO_GROUP,
            RATING,
            PICK_LIST,
            BREADCRUMB,
            TAGS_COMBO_BOX,
            CHECK_BOX_GROUP,
            TREE_VIEW,
            DYNAMIC);

    private static final EnumSet<WidgetType> ARRAY_WIDGETS = of(TAGS,
            TAGS_COMBO_BOX,
            CHECK_BOX_GROUP,
            SHOWCASE,
            GALLERY,
            TAGS_SUGGEST_BOX,
            RANGE,
            PICK_LIST);  // forced multiple values

    private static final EnumSet<WidgetType> NOT_ICONABLE_WIDGETS = of(DATE_PICKER,
            COMBO_DATE_BOX,
            UPLOAD,
            SHOWCASE,
            GALLERY,
            BREADCRUMB,
            CHECK_BOX_GROUP,
            CHECK_BOX,
            COLOR_PICKER,
            COMBO_BOX,
            IMAGE,
            LIST_BOX,
            PICK_LIST,
            PROGRESS,
            RICH_TEXT_AREA,
            RADIO_GROUP,
            RATING,
            TAGS_SUGGEST_BOX,
            TAGS,
            TEXT_AREA,
            WidgetType.INTERNAL,
            TIME_PICKER,
            TREE_VIEW,
            VIDEO);

    private static final EnumSet<WidgetType> NULLABLE_VALUE_WIDGETS = of(COMBO_BOX,
            PICK_LIST,
            TAGS_COMBO_BOX,
            DATE_BOX,
            COMBO_DATE_BOX,
            DATE_TIME_BOX,
            DATE_PICKER,
            DOUBLE_DATE_BOX,
            TIME_PICKER,
            LIST_BOX,
            SUGGEST_BOX,
            TAGS,
            UPLOAD,
            TEXT_AREA,
            TAGS_SUGGEST_BOX,
            TAGS,
            UPLOAD,
            TEXT_AREA,
            TAGS_SUGGEST_BOX,
            RICH_TEXT_AREA,
            BREADCRUMB,
            WidgetType.DISPLAY,
            PASSWORD_FIELD,
            MAIL_FIELD,
            TEXT_FIELD,
            WidgetType.INTERNAL,
            RADIO_GROUP,
            RATING,
            TREE_VIEW,
            CHECK_BOX_GROUP,
            DYNAMIC,
            IMAGE,
            VIDEO,
            COLOR_PICKER,
            SHOWCASE);

    // widgets that always have a default value (hence: they are required and can't be optional)
    private static final EnumSet<WidgetType> NOT_NULL_VALUE_WIDGETS = of(CHECK_BOX,
            TOGGLE_BUTTON,
            IFRAME,
            PROGRESS,
            TABS,
            MESSAGE,
            DIALOG,
            GALLERY,
            HORIZONTAL,
            VERTICAL,
            POPOVER);

    private static final EnumSet<WidgetType> MASK_WIDGETS = of(TEXT_FIELD, WidgetType.DISPLAY, COMBO_BOX, LIST_BOX, SUGGEST_BOX, RADIO_GROUP);

    // private static final EnumSet<WidgetType> HAS_INPUT_HANDLER = EnumSet.of(LIST_BOX, TEXT_FIELD, COMBO_BOX, RADIO_GROUP,
    // SUGGEST_BOX, DYNAMIC);

    private static final EnumSet<WidgetType> RANGED_WIDGETS = of(DATE_BOX, DATE_PICKER, DOUBLE_DATE_BOX, COMBO_DATE_BOX, DATE_TIME_BOX, TIME_PICKER);

    private static final EnumSet<WidgetType> SHOWING_ENTITY_WIDGETS = of(SUGGEST_BOX, WidgetType.DISPLAY, TAGS_SUGGEST_BOX);

    private static final EnumSet<WidgetType> DEFAULT_INIT_VALUE_WIDGETS = NOT_NULL_VALUE_WIDGETS.clone();
    private static final EnumSet<WidgetType> VALUED_WIDGETS             = NULLABLE_VALUE_WIDGETS.clone();

    private static final Map<String, WidgetType>                    idToEnum;
    private static final EnumMap<FieldOption, EnumSet<WidgetType>>  supports         = new EnumMap<>(FieldOption.class);
    private static final EnumMap<FieldOption, FieldOption>          related          = new EnumMap<>(FieldOption.class);
    private static final EnumMap<FieldOption, EnumSet<FieldOption>> incompatible     = new EnumMap<>(FieldOption.class);
    private static final EnumMap<FieldOption, List<FieldOption>>    required         = new EnumMap<>(FieldOption.class);
    private static final EnumMap<FieldOption, HasFieldOption>       extendedOptions  = new EnumMap<>(FieldOption.class);
    private static final EnumMap<WidgetType, List<HasFieldOption>>  arguments        = new EnumMap<>(WidgetType.class);
    private static final EnumMap<WidgetType, HashSet<FieldOption>>  optionsSupported = new EnumMap<>(WidgetType.class);
    private static final EnumMap<WidgetType, TypeSupports>          typeForWidget    = new EnumMap<>(WidgetType.class);

    static {
        DEFAULT_INIT_VALUE_WIDGETS.add(IMAGE);
        DEFAULT_INIT_VALUE_WIDGETS.add(WidgetType.INTERNAL);
        DEFAULT_INIT_VALUE_WIDGETS.add(WidgetType.DISPLAY);

        VALUED_WIDGETS.addAll(NOT_NULL_VALUE_WIDGETS);

        // TODO UNIQUE and HIDE_COLUMN should be for fields in a table
        supportsByType(ALL_WIDGETS, SLOT_FIELD, ORDINAL, ID, FieldOption.LABEL, SHORTCUT, FieldOption.LABEL_EXPRESSION);
        supportsByType(VALUED_WIDGETS, CHECK, IS, DEFAULT, RESET, UNIQUE, SKIP_TAB);
        supportsByType(allAnd(NULLABLE_VALUE_WIDGETS, SUBFORM, WIDGET), OPTIONAL, REQUIRED);
        supportsByType(allAnd(VALUED_WIDGETS, BUTTON, TOGGLE_BUTTON, DROPDOWN, LABEL, SUBFORM, TABLE), DISABLE, DEPENDS_ON, DEPENDS_ON_EXPR);
        supportsByType(allAnd(VALUED_WIDGETS, WidgetType.TABLE, CHART, MAP, SECTION, WIDGET),
            ON_CHANGE,
            ON_CHANGE_FN,
            ON_UI_CHANGE,
            PLACEHOLDER,
            ABSTRACT_INVOCATION);
        supportsByType(allAnd(allBut(VALUED_WIDGETS, MAIL_FIELD, RATING), WidgetType.TABLE, CHART, MAP, SECTION), ON_BLUR);
        supportsByType(allAnd(VALUED_WIDGETS, WidgetType.TABLE, CHART, MAP, SECTION, SUBFORM, WIDGET), BINDING);
        supportsByType(allBut(ALL_WIDGETS, WidgetType.INTERNAL, SUBFORM),
            HIDE_COLUMN,
            COLUMN_STYLE,
            STYLE,
            INLINE_STYLE,
            HINT,
            COL,
            OFFSET_COL,
            NO_LABEL,
            AFFIX);
        supportsByType(allBut(ALL_WIDGETS, WidgetType.INTERNAL), HIDE);
        supportsByType(allBut(ALL_WIDGETS, HORIZONTAL, VERTICAL, DIALOG, TABS, SUBFORM), CONTENT_STYLE);
        supportsByType(allBut(ALL_WIDGETS, WidgetType.INTERNAL, SUBFORM, BUTTON, TOGGLE_BUTTON, LABEL), LABEL_COL);
        supportsByType(allBut(ALL_WIDGETS, WidgetType.INTERNAL, SUBFORM, BUTTON, TOGGLE_BUTTON, LABEL), TOP_LABEL);
        supportsByType(allAnd(ARRAY_WIDGETS, IMAGE, WidgetType.DISPLAY, LIST_BOX, UPLOAD, DYNAMIC, WidgetType.INTERNAL), MULTIPLE);
        supportsByType(MASK_WIDGETS, CUSTOM_MASK, MASK);
        supportsByType(RANGED_WIDGETS, TO);
        supportsByType(
            of(MAP, CHART, DYNAMIC, UPLOAD, SUBFORM, DATE_BOX, DATE_PICKER, DATE_TIME_BOX, DOUBLE_DATE_BOX, RADIO_GROUP, RATING, WidgetType.INTERNAL),
            SLOT_CONFIGURATION);
        supportsByType(of(WidgetType.TABLE), SORTABLE, ON_SELECTION, ON_SELECTION_EXPR, AGGREGATE);
        supportsByType(of(WidgetType.TABLE, SECTION), ROW_STYLE);
        supportsByType(of(WidgetType.TABLE, SECTION), ROW_INLINE_STYLE);
        supportsByType(of(WidgetType.TABLE, SECTION, DYNAMIC), FILTERING);
        supportsByType(of(SUBFORM), SLOT_SUBFORM, ON, INLINE);
        supportsByType(of(WIDGET), SLOT_WIDGET, PLACEHOLDER_BINDING);
        supportsByType(of(BUTTON), EXPORT_TYPE);
        supportsByType(allAnd(VALUED_WIDGETS, BUTTON, TOGGLE_BUTTON, LABEL, DROPDOWN), TOOLTIP);
        supportsByType(allBut(ALL_WIDGETS, NOT_ICONABLE_WIDGETS), ICON);
        supportsByType(allBut(ALL_WIDGETS, NOT_ICONABLE_WIDGETS), ICON_EXPR);
        supportsByType(allAnd(RANGED_WIDGETS, PROGRESS), FROM);
        supportsByType(of(WidgetType.DISPLAY, LABEL, SUGGEST_BOX), LINK, LINK_FORM, LINK_PK, TARGET_BLANK);
        supportsByType(of(WidgetType.FORM),
            ON_CANCEL,
            ON_SCHEDULE,
            ON_SCHEDULE_INTERVAL,
            ON_ROUTE,
            DYNAMIC_FORM,
            EXTENDED_FORM,
            DISABLE_METADATA_CACHE,
            HANDLER,
            PROJECT);
        supportsByType(of(WidgetType.PASSWORD_FIELD), METERING);
        supportsByType(allAnd(VALUED_WIDGETS, HORIZONTAL, VERTICAL, INPUT_GROUP), WIDTH);

        final EnumSet<WidgetType> slotOptions = OPTIONS_WIDGETS.clone();
        slotOptions.addAll(SHOWING_ENTITY_WIDGETS);
        slotOptions.add(MAIL_FIELD);
        supportsByType(slotOptions, SLOT_OPTIONS, SLOT_GLOBAL_OPTIONS);

        supportsByOption(ON_NEW_LOCATION, MAP);
        supportsByOption(ICON_SELECTED, TOGGLE_BUTTON, POPOVER);
        supportsByOption(CONFIRM, BUTTON, LABEL, TOGGLE_BUTTON);
        supportsByOption(DRAGGABLE, MAP, WidgetType.TABLE, DISPLAY);
        supportsByOption(FILTERABLE, WidgetType.TABLE);
        supportsByOption(LAZY, WidgetType.TABLE);
        supportsByOption(LAZY_FETCH, WidgetType.TABLE);
        supportsByOption(ON_CLICK, BUTTON, WidgetType.DISPLAY, LABEL, WidgetType.TABLE, IMAGE, CHART);
        supportsByOption(ON_CLICK_FN, BUTTON, WidgetType.DISPLAY, LABEL, WidgetType.TABLE, IMAGE, CHART);
        supportsByOption(ABSTRACT_INVOCATION, BUTTON, WidgetType.DISPLAY, LABEL, WidgetType.TABLE, IMAGE, CHART);
        supportsByOption(FILTER, SUGGEST_BOX, SEARCH_BOX, TAGS_SUGGEST_BOX, COMBO_BOX, TAGS_COMBO_BOX);
        supportsByOption(EXPAND,
            SUGGEST_BOX,
            COMBO_BOX,
            COMBO_DATE_BOX,
            TAGS_SUGGEST_BOX,
            TAGS_COMBO_BOX,
            PASSWORD_FIELD,
            MAIL_FIELD,
            TEXT_FIELD,
            DATE_BOX,
            DATE_TIME_BOX,
            SEARCH_BOX,
            DOUBLE_DATE_BOX,
            DATE_PICKER,
            UPLOAD);
        supportsByOption(ALIGN,
            SUGGEST_BOX,
            COMBO_BOX,
            TAGS_SUGGEST_BOX,
            TAGS_COMBO_BOX,
            TAGS,
            DATE_BOX,
            DATE_TIME_BOX,
            TEXT_FIELD,
            MAIL_FIELD,
            DOUBLE_DATE_BOX);
        supportsByOption(ON_NEW, SUGGEST_BOX, SEARCH_BOX, TAGS_SUGGEST_BOX);
        supportsByOption(ON_NEW_FORM, SUGGEST_BOX);
        supportsByOption(TEXT_LENGTH, MESSAGE, WidgetType.DISPLAY);
        supportsByOption(FULL_TEXT, WidgetType.DISPLAY, CHECK_BOX_GROUP, SUGGEST_BOX, TAGS_SUGGEST_BOX, SEARCH_BOX, MESSAGE);

        supportsByOption(CHANGE_THRESHOLD, SUGGEST_BOX, TAGS_SUGGEST_BOX, SEARCH_BOX, TEXT_FIELD);
        supportsByOption(CHANGE_DELAY, SUGGEST_BOX, TAGS_SUGGEST_BOX, SEARCH_BOX, TEXT_FIELD);

        supportsByOption(QUERY_MODE, SUGGEST_BOX, TAGS_SUGGEST_BOX, SEARCH_BOX);
        supportsByOption(ON_SUGGEST_EXPR, SUGGEST_BOX, SEARCH_BOX, TAGS_SUGGEST_BOX, MAIL_FIELD);
        supportsByOption(ON_SUGGEST, SUGGEST_BOX, SEARCH_BOX, TAGS_SUGGEST_BOX, MAIL_FIELD);
        supportsByOption(ON_SUGGEST_SYNC, SUGGEST_BOX, TAGS_SUGGEST_BOX, MAIL_FIELD);
        supportsByOption(HOVER, POPOVER);
        supportsByOption(COLLAPSIBLE, VERTICAL, HORIZONTAL);
        supportsByOption(SCROLLABLE, SECTION, WidgetType.TABLE);
        supportsByOption(SPLIT, DROPDOWN);
        supportsByOption(FLOW, INPUT_GROUP);
        supportsByOption(FieldOption.DISPLAY, SUBFORM, DIALOG);
        supportsByOption(NOPASTE, TEXT_FIELD);
        supportsByOption(SECRET, TEXT_FIELD);
        supportsByOption(SYNCHRONOUS, LABEL, BUTTON, TOGGLE_BUTTON, DISPLAY);
        supportsByOption(FEEDBACK, BUTTON, TOGGLE_BUTTON);
        supportsByOption(CAMERA, UPLOAD);
        supportsByOption(ON_LOAD, WidgetType.FORM, WidgetType.TABLE);
        supportsByOption(ON_DISPLAY, WidgetType.FORM);
        supportsByOption(UNRESTRICTED, WidgetType.FORM);
        supportsByOption(THUMB_VARIANT, GALLERY, IMAGE, SHOWCASE);
        supportsByOption(LARGE_VARIANT, GALLERY, IMAGE, SHOWCASE);
        supportsByOption(CUSTOM_VARIANT, GALLERY, IMAGE, SHOWCASE);
        supportsByOption(MIDNIGHT_AS_24, DATE_TIME_BOX, TIME_PICKER);
        supportsByOption(RESET_TIME, DATE_TIME_BOX);
        supportsByOption(STEP, TIME_PICKER);
        supportsByOption(WITH_TIMEZONE, TIME_PICKER);
        supportsByOption(BOUNDED_TO_DEPRECABLE, SEARCH_BOX, SUGGEST_BOX);
        supportsByOption(WITH_IMAGE, DISPLAY);
        supportsByOption(SAVES_ENTITY, BUTTON);
        supportsByOption(ON_LAZY_FETCH, TABLE);
        supportsByOption(SIGNED, TEXT_FIELD, WidgetType.INTERNAL, WidgetType.DISPLAY);  // todo Strings & others doesn't
                                                                                        // support signed,
        supportsByOption(UNSIGNED, TEXT_FIELD, WidgetType.INTERNAL, WidgetType.DISPLAY);
        supportsByOption(AXIS, TEXT_FIELD, DISPLAY);
        // only Numbers.

        incompatibles(OPTIONAL, REQUIRED);  // todo check
        incompatibles(INLINE, OPTIONAL);    // todo check
        incompatibles(ICON, SPLIT);
        incompatibles(ICON, ICON_EXPR);
        incompatibles(ICON_SELECTED, ICON_EXPR);
        incompatibles(ICON_SELECTED, SPLIT);
        incompatibles(ON_NEW, ON_NEW_FORM);
        incompatibles(ON_CHANGE, ON_UI_CHANGE);
        incompatible(ON_BLUR, of(ON_UI_CHANGE, ON_CHANGE));
        incompatibles(LINK, LINK_FORM);
        incompatibles(LINK, LINK_PK);
        incompatibles(LABEL_COL, TOP_LABEL);
        incompatibles(ON, FOOTER_TYPE);
        incompatibles(SIGNED, UNSIGNED);
        incompatibles(LAZY, LAZY_FETCH);

        requiresAny(RESET, HIDE, DISABLE);
        requiresAny(ABSTRACT_INVOCATION, ON_CHANGE, ON_UI_CHANGE, ON_BLUR, ON_CLICK);
        requiresAny(OFFSET_COL, COL);
        requiresAny(LINK_PK, LINK_FORM);
        relatedByOption(ON_SUGGEST, ON_SUGGEST_EXPR);
        relatedByOption(ON_SELECTION, ON_SELECTION_EXPR);
        extendOption(MASK, PredefinedMask.class);
        extendOption(ALIGN, Alignment.class);
        extendOption(ICON, IconType.class);
        extendOption(QUERY_MODE, QueryMode.class);
        extendOption(ICON_SELECTED, IconType.class);
        extendOption(EXPORT_TYPE, ExportType.class);

        argumentFor(WidgetType.DISPLAY, LENGTH.withDefault(100));
        argumentFor(TEXT_FIELD, LENGTH.withDefault(10));
        argumentFor(SUGGEST_BOX, LENGTH.withDefault(10));
        argumentsFor(TEXT_AREA, ROWS.withDefault(3), LENGTH.withDefault(10));
        argumentFor(LIST_BOX, ROWS.withDefault(3));
        argumentFor(PICK_LIST, ROWS.withDefault(3));

        argumentFor(TABS, TAB_TYPE.withClass(TabType.class).withDefault(TabType.TOP));
        argumentFor(POPOVER, POPOVER_TYPE.withClass(PopoverType.class).withDefault(PopoverType.RIGHT));
        argumentFor(MAP, MAP_TYPE.withClass(MapType.class).withDefault(MapType.OPENSTREET));
        argumentFor(UPLOAD, FILE_TYPE.withClass(Mime.class).withDefault(Mime.ALL));
        argumentFor(DATE_PICKER, DATE_TYPE.withClass(DateType.class).withDefault(DateType.MEDIUM_FORMAT));
        argumentFor(DOUBLE_DATE_BOX, DATE_TYPE.withClass(DateType.class).withDefault(DateType.MEDIUM_FORMAT));
        argumentFor(DATE_BOX, DATE_TYPE.withClass(DateType.class).withDefault(DateType.MEDIUM_FORMAT));
        argumentFor(COMBO_DATE_BOX, DATE_TYPE.withClass(DateType.class).withDefault(DateType.MEDIUM_FORMAT));
        argumentFor(DATE_TIME_BOX, DATE_TYPE.withClass(DateType.class).withDefault(DateType.MEDIUM_FORMAT));
        argumentFor(CHART, CHART_TYPE.withClass(ChartType.class).withDefault(ChartType.COLUMN));
        argumentFor(RATING, RATING_TYPE.withClass(RatingType.class).withDefault(RatingType.STARS));
        argumentsFor(MAIL_FIELD,
            MAIL_VALIDATION_TYPE.withClass(MailValidationType.class).withDefault(MailValidationType.ALL),
            LENGTH.withDefault(10));
        argumentsFor(BUTTON, BUTTON_TYPE.withClass(ButtonType.class).withDefault(ButtonType.CUSTOM), BUTTON_BOUND_ID);
        argumentFor(TOGGLE_BUTTON, TOGGLE_BUTTON_TYPE.withClass(ToggleButtonType.class).withDefault(ToggleButtonType.CUSTOM));
        argumentFor(WidgetType.TABLE, ROWS);
        argumentFor(MESSAGE, CHECK_TYPE.withClass(CheckType.class).withDefault(CheckType.INFO));
        argumentsFor(SUBFORM, SUBFORM_ID, FOOTER_TYPE.withClass(FooterType.class).withDefault(FooterType.OK_CANCEL));
        argumentsFor(WIDGET, WIDGET_DEF);

        typeFor(CHECK_BOX, booleanType());
        typeFor(DATE_BOX, dateType());
        typeFor(COMBO_DATE_BOX, dateType());
        typeFor(DATE_PICKER, dateType());
        typeFor(DOUBLE_DATE_BOX, dateType());
        typeFor(DATE_TIME_BOX, dateTimeType());
        typeFor(TIME_PICKER, intType());
        typeFor(TOGGLE_BUTTON, booleanType());
        typeFor(MESSAGE, stringType());
        typeFor(TEXT_AREA, stringType());
        typeFor(RICH_TEXT_AREA, stringType());
        typeFor(PASSWORD_FIELD, stringType());
        typeFor(MAIL_FIELD, stringType());
        typeFor(TABS, intType());
        typeFor(HORIZONTAL, booleanType());
        typeFor(VERTICAL, booleanType());
        typeFor(DIALOG, booleanType());
        typeFor(POPOVER, booleanType());
        typeFor(MAP, realType());
        typeFor(PROGRESS, realType());
        typeFor(TAGS, stringType(), STRING, INT, REAL, DECIMAL);
        typeFor(DYNAMIC, anyType());
        typeFor(VIDEO, stringType());
        typeFor(UPLOAD, resourceType());
        typeFor(IFRAME, stringType());

        typeFor(BREADCRUMB, anyType());
        typeFor(TREE_VIEW, anyType());
        typeFor(TAGS_COMBO_BOX, anyType());
        typeFor(CHECK_BOX_GROUP, anyType());
        typeFor(PICK_LIST, anyType());
        typeFor(COMBO_BOX, anyType());
        typeFor(LIST_BOX, anyType());
        typeFor(RADIO_GROUP, anyType());
        typeFor(RATING, intType());
        typeFor(WidgetType.DISPLAY, anyType(), STRING, INT, REAL, DECIMAL, ARRAY, BOOLEAN, DATE, DATE_TIME, ENUM, REFERENCE);
        typeFor(WidgetType.INTERNAL, anyType());
        typeFor(SUGGEST_BOX, anyType());
        typeFor(TAGS_SUGGEST_BOX, anyType());
        typeFor(SUBFORM, anyType());
        typeFor(WIDGET, anyType());
        typeFor(COLOR_PICKER, stringType());

        typeFor(CHART, anyType(), REFERENCE);
        typeFor(WidgetType.TABLE, anyType(), REFERENCE, TYPE);
        typeFor(SECTION, anyType(), REFERENCE, TYPE);
        typeFor(MAP, anyType(), REFERENCE);
        typeFor(IMAGE, stringType(), STRING, RESOURCE, ENUM);
        typeFor(GALLERY, resourceType(), STRING, RESOURCE, ENUM);
        typeFor(SHOWCASE, resourceType(), STRING, RESOURCE, ENUM);
        typeFor(TEXT_FIELD, stringType(), STRING, INT, REAL, DECIMAL);
        typeFor(RANGE, realType(), INT, REAL, DECIMAL);
        typeFor(RANGE_VALUE, realType(), INT, REAL, DECIMAL);

        idToEnum = new HashMap<>();
        for (final WidgetType type : ALL_WIDGETS)
            idToEnum.put(type.getId(), type);

        for (final WidgetType widgetType : ALL_WIDGETS) {
            final HashSet<FieldOption> optionsForWidget = new HashSet<>();
            for (final FieldOption option : FieldOption.values()) {
                if (option.isKeyword() && supports(widgetType, option)) optionsForWidget.add(option);
            }
            optionsSupported.put(widgetType, optionsForWidget);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class TypeSupports {
        private final Type                defaultType;
        private final ImmutableList<Kind> supported;

        private TypeSupports(Type defaultType, Kind[] supported) {
            this.defaultType = defaultType;
            this.supported   = ImmutableList.fromArray(supported);
        }

        public boolean supports(Type t) {
            return !supported.isEmpty() ? supported.contains(t.getKind()) : defaultType == anyType() || t != nullType() && t.equivalent(defaultType);
                   // return defaultType == anyType() ||
                   // (t != nullType() && supported.isEmpty() ? t.equivalent(defaultType) : supported.contains(t.getKind()));
        }

        public Type getDefaultType() {
            return defaultType;
        }
    }
}  // end class WidgetTypes
