
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.MetaModelKind;

import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.field.FieldOptionType.*;
import static tekgenesis.type.MetaModelKind.*;

/**
 * Options for a Field.
 */
public enum FieldOption implements HasFieldOption {

    //~ Enum constants ...............................................................................................................................

    ID(STRING_T, DB_AND_UI, false, false), LABEL(STRING_T, DB_AND_UI, false),
    //J-
    ABSTRACT(BOOLEAN_T, ENTITY_ONLY),
    ACTIONS(LABELED_IDS_T, CASE_ONLY),
    AFFIX(STRING_T, UI_MODELS),
    AGGREGATE(AGGREGATE_T, UI_MODELS, false),
    ALIGN(ENUM_T, UI_MODELS), // mask currency "$"  ==>>  10,10 > $ 10
    AXIS(UNSIGNED_T, UI_MODELS, true),
    BINDING(IDENTIFIER_T, UI_MODELS, false),
    PLACEHOLDER_BINDING(BOOLEAN_T, UI_MODELS, false),
    BODY(TYPE_T, HANDLER_ONLY),
    BOUNDED_TO_DEPRECABLE(BOOLEAN_T, UI_MODELS, false, false),
    BUTTON_TYPE(ENUM_T, UI_MODELS, false),
    CAMERA(BOOLEAN_T, UI_MODELS),
    CHART_TYPE(ENUM_T, UI_MODELS, false),
    CHECK(CHECK_T),
    CHECK_TYPE(ENUM_T, UI_MODELS, false),
    CLASS_METHOD(STRING_T, HANDLER_ONLY, false, false),
    SUMMARY(STRING_T, HANDLER_ONLY),
    KEY(STRING_T, HANDLER_ONLY),
    COL(UNSIGNED_T, UI_MODELS),
    COLLAPSIBLE(BOOLEAN_T, UI_MODELS),
    COLUMN(IDENTIFIERS_T, ENTITY_ONLY),
    COLUMN_STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    CONFIRM(STRING_EXPR_T, UI_MODELS),
    CONTENT_STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    CUSTOM_MASK(STRING_EXPRS_T, DB_AND_UI, true, false), // mask for formatting, ex: AAA-###
    CUSTOM_VARIANT(STRING_T, UI_MODELS),
    DATE_TYPE(ENUM_T, UI_MODELS, false),
    DEFAULT(VALUE_EXPR_T),
    DEPENDS_ON(IDENTIFIER_T, UI_MODELS),
    DEPENDS_ON_EXPR(BOOLEAN_EXPR_T, UI_MODELS, false),
    DISABLE(BOOLEAN_EXPR_T, UI_MODELS),
    DISPLAY(STRING_EXPR_T, UI_MODELS),
    DRAGGABLE(BOOLEAN_T, UI_MODELS),
    DYNAMIC_FORM(BOOLEAN_T, UI_MODELS, false),
    DISABLE_METADATA_CACHE(BOOLEAN_T, UI_MODELS, false),
    EXTENDED_FORM(STRING_T, UI_MODELS, false),
    ORIGINAL_FORM_FIELD(BOOLEAN_T, UI_MODELS, false),
    EXPAND(BOOLEAN_T, UI_MODELS),
    EXPORT_TYPE(ENUM_T, UI_MODELS, true, false),
    FEEDBACK(BOOLEAN_T, UI_MODELS),
    FILE_TYPE(ENUM_T, UI_MODELS, false),
    FILTER(ASSIGNMENT_EXPRS, UI_MODELS, true, false),
    FILTERABLE(BOOLEAN_T, UI_MODELS),
    FILTERING(IDENTIFIER_T, UI_MODELS),
    FLOW(BOOLEAN_T, UI_MODELS),
    FORM(METAMODEL_REFERENCE_T, MENU_AND_CASE_ONLY),
    FROM(VALUE_EXPR_T, UI_MODELS),
    FULL_TEXT(BOOLEAN_T, UI_MODELS),
    HANDLER(METAMODEL_REFERENCE_T, UI_MODELS),
    HIDE(BOOLEAN_EXPR_T, UI_MODELS),
    HIDE_COLUMN(BOOLEAN_EXPR_T, UI_MODELS),
    HINT(STRING_EXPR_T, UI_MODELS),
    HOVER(BOOLEAN_T, UI_MODELS),
    ICON(ENUM_T, UI_MODELS, true, false),
    ICON_EXPR(STRING_EXPR_T, UI_MODELS, true, false),
    ICON_SELECTED(ENUM_T, UI_MODELS, true, false),
    INLINE(BOOLEAN_T, UI_MODELS),
    INLINE_STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    INTERNAL(BOOLEAN_T, HANDLER_ONLY, false),
    IS(VALUE_EXPR_T, UI_MODELS, true, true),
    LABEL_COL(UNSIGNED_T, UI_MODELS),
    LABEL_EXPRESSION(STRING_EXPR_T, UI_MODELS),
    LARGE_VARIANT(STRING_T, UI_MODELS),
    LAZY(BOOLEAN_T, UI_MODELS),
    LAZY_FETCH(BOOLEAN_T, UI_MODELS),
    LENGTH(UNSIGNED_T, UI_MODELS, false),
    FIELD_DOCUMENTATION(STRING_T, ENTITY_AND_VIEW_ONLY),
    LINK(STRING_EXPR_T, UI_MODELS, true, false),
    LINK_FORM(METAMODEL_REFERENCE_T, UI_MODELS),
    LINK_PK(STRING_EXPR_T, UI_MODELS, true, false),
    MAIL_VALIDATION_TYPE(ENUM_T, UI_MODELS, false),
    MAP_TYPE(ENUM_T, UI_MODELS, false),
    MASK(ENUM_T, UI_MODELS), // mask currency "$"  ==>>  10,10 > $ 10
    MENU_ELEMENT_REF(METAMODEL_REFERENCE_T, MENU_ONLY),
    METERING(UNSIGNED_EXPR_T, UI_MODELS),
    METHOD(ENUM_T, HANDLER_ONLY),
    MIDNIGHT_AS_24(BOOLEAN_T, UI_MODELS),
    RESET_TIME(STRING_T, UI_MODELS),
    ROLE_APPLICATION_REF(METAMODEL_REFERENCE_T, ROLE_ONLY),
    ROLE_PERMISSION(STRING_T, ROLE_ONLY),
    MULTIPLE(BOOLEAN_T, UI_MODELS),
    @SuppressWarnings("SpellCheckingInspection")NOPASTE(BOOLEAN_T, UI_MODELS),
    NO_LABEL(BOOLEAN_T, UI_MODELS),
    OFFSET_COL(UNSIGNED_T, UI_MODELS),
    ON(IDENTIFIER_T, UI_MODELS),
    ON_BLUR(IDENTIFIER_T, UI_MODELS),
    ON_CANCEL(IDENTIFIER_T, UI_MODELS),
    ON_CHANGE(IDENTIFIER_T, UI_MODELS),
    ON_NEW_LOCATION(IDENTIFIER_T, UI_MODELS),
    ON_CHANGE_FN(METHOD_REF_T, UI_MODELS, false),
    ON_CLICK(IDENTIFIER_T, UI_MODELS),
    ON_CLICK_FN(METHOD_REF_T, UI_MODELS, false),
    ON_DISPLAY(IDENTIFIER_T, UI_MODELS),
    ON_LAZY_FETCH(IDENTIFIER_T, UI_MODELS, false),
    ON_INIT(IDENTIFIER_T, TASK_ONLY),
    ON_LOAD(IDENTIFIER_T, UI_MODELS),
    ON_NEW(IDENTIFIER_T, UI_MODELS),
    ON_NEW_FORM(METAMODEL_REFERENCE_T, UI_MODELS),
    ON_ROUTE(IDENTIFIER_T, UI_MODELS, false),
    ON_SCHEDULE(IDENTIFIER_T, UI_MODELS),
    ON_SCHEDULE_INTERVAL(UNSIGNED_T, UI_MODELS, false),
    ON_SELECTION(METHOD_T, UI_MODELS),
    ON_SELECTION_EXPR(BOOLEAN_EXPR_T, UI_MODELS, false),
    ON_STOP(IDENTIFIER_T, TASK_ONLY),
    ON_SUGGEST(METHOD_T, UI_MODELS),
    ON_SUGGEST_EXPR(GENERIC_EXPR_T, UI_MODELS, false),
    ON_SUGGEST_SYNC(IDENTIFIER_T, UI_MODELS),
    ON_UI_CHANGE(IDENTIFIER_T, UI_MODELS),
    OPTIONAL(BOOLEAN_EXPR_T),
    PARAMETERS(FIELDS_T, HANDLER_ONLY),
    ABSTRACT_INVOCATION(BOOLEAN_T, UI_MODELS),
    PLACEHOLDER(STRING_EXPR_T, UI_MODELS),
    POPOVER_TYPE(ENUM_T, UI_MODELS, false),
    PROCESS(IDENTIFIER_T, CASE_ONLY),
    PROJECT(STRING_T, UI_MODELS),
    PROTECTED(BOOLEAN_T, ENTITY_ONLY),
    RATING_TYPE(ENUM_T, UI_MODELS, false),
    READ_ONLY(BOOLEAN_T, ENTITY_AND_VIEW_ONLY),
    REQUIRED(BOOLEAN_T, UI_MODELS),
    RESET(BOOLEAN_T, UI_MODELS),
    ROWS(UNSIGNED_T, UI_MODELS, false),
    ROW_STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    ROW_INLINE_STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    SAVES_ENTITY(BOOLEAN_T, UI_MODELS, false, false),
    SCROLLABLE(BOOLEAN_T, UI_MODELS),
    SECRET(BOOLEAN_T, UI_MODELS),
    SECURE_METHOD(ENUM_T, HANDLER_ONLY, false),
    SERIAL(IDENTIFIER_T, ENTITY_ONLY),
    SHORTCUT(STRING_T, UI_MODELS, true, false),
    SIGNED,
    UNSIGNED(BOOLEAN_T, UI_MODELS),
    SKIP_TAB(BOOLEAN_T, UI_MODELS),
    ORDINAL(UNSIGNED_T, UI_MODELS, false),
    SLOT_CONFIGURATION(UNSIGNED_T, UI_MODELS, false),
    SLOT_FIELD(UNSIGNED_T, UI_MODELS, false),
    SLOT_GLOBAL_OPTIONS(UNSIGNED_T, UI_MODELS, false),
    SLOT_OPTIONS(UNSIGNED_T, UI_MODELS, false),
    SLOT_SUBFORM(UNSIGNED_T, UI_MODELS, false),
    SLOT_WIDGET(UNSIGNED_T, UI_MODELS, false),
    SORTABLE(BOOLEAN_T, UI_MODELS),
    SPLIT(BOOLEAN_T, UI_MODELS),
    START_WITH(UNSIGNED_T, ENTITY_ONLY),
    STEP(UNSIGNED_T, UI_MODELS),
    QUERY_MODE(ENUM_T),
    STYLE(STRING_EXPR_T, UI_MODELS, true, false),
    SUBFORM_ID(METAMODEL_REFERENCE_T, UI_MODELS, false),
    WIDGET_DEF(METAMODEL_REFERENCE_T, UI_MODELS, false),
    FOOTER_TYPE(ENUM_T, UI_MODELS, false),
    CHANGE_DELAY(UNSIGNED_T, UI_MODELS),
    CHANGE_THRESHOLD(UNSIGNED_T, UI_MODELS),
    SYNCHRONOUS(BOOLEAN_T, UI_MODELS),
    BUTTON_BOUND_ID(IDENTIFIER_T, UI_MODELS, false),
    TAB_TYPE(ENUM_T, UI_MODELS, false),
    TARGET_BLANK(BOOLEAN_T, UI_MODELS),
    TASK(IDENTIFIER_T, TASK_ONLY),
    TEXT_LENGTH(UNSIGNED_EXPR_T, UI_MODELS, true, false),
    THUMB_VARIANT(STRING_T, UI_MODELS),
    TIME_OF_DAY(BOOLEAN_T, ENTITY_ONLY, false),
    TO(VALUE_EXPR_T, UI_MODELS),
    TOGGLE_BUTTON_TYPE(ENUM_T, UI_MODELS, false),
    TOOLTIP(STRING_EXPR_T, UI_MODELS),
    TOP_LABEL(BOOLEAN_T, UI_MODELS),
    UNIQUE(BOOLEAN_T, UI_MODELS),
    UNRESTRICTED(BOOLEAN_T, HANDLER_AND_FORM_ONLY, true),
    WIDTH(UNSIGNED_T, UI_MODELS),
    ANALYZED(BOOLEAN_T, SEARCHABLE_ONLY),
    FILTER_ONLY(BOOLEAN_T, SEARCHABLE_ONLY),
    RESULT_ONLY(BOOLEAN_T, SEARCHABLE_ONLY),
    PREFIX(BOOLEAN_T, SEARCHABLE_ONLY),
    FUZZY(UNSIGNED_T, SEARCHABLE_ONLY),
    BOOST(UNSIGNED_T, SEARCHABLE_ONLY),
    SLOP(UNSIGNED_T, SEARCHABLE_ONLY),
    WITH_IMAGE(BOOLEAN_T, UI_MODELS),
    WITH_TIMEZONE(BOOLEAN_T, UI_MODELS);
    //J+

    //~ Instance Fields ..............................................................................................................................

    /** True if the options is a keyword in the 'mm' definition language. */

    private final boolean keyword;

    /** True if the options is localizable. */
    private final boolean localizable;

    /** The Meta models the Options is applicable to. */
    private final EnumSet<MetaModelKind> metaModels;

    /** The Options type. */
    private final FieldOptionType type;

    //~ Constructors .................................................................................................................................

    FieldOption() {
        this(BOOLEAN_T, MetaModelKind.DB_AND_UI);
    }

    @SuppressWarnings("WeakerAccess")  // inspections bug
    FieldOption(FieldOptionType type) {
        this(type, MetaModelKind.DB_AND_UI);
    }

    @SuppressWarnings("WeakerAccess")  // inspections bug
    FieldOption(FieldOptionType type, EnumSet<MetaModelKind> metaModels) {
        this(type, metaModels, true);
    }

    @SuppressWarnings("WeakerAccess")  // inspections bug
    FieldOption(FieldOptionType type, EnumSet<MetaModelKind> metaModels, boolean keyword) {
        this(type, metaModels, keyword, type.isLocalizable());
    }

    @SuppressWarnings("WeakerAccess")  // inspections bug
    FieldOption(FieldOptionType type, EnumSet<MetaModelKind> metaModels, boolean keyword, boolean localizable) {
        this.type        = type;
        this.metaModels  = metaModels;
        this.keyword     = keyword;
        this.localizable = localizable;
    }

    //~ Methods ......................................................................................................................................

    /** Decorate the option with an Enum Class. */
    public <T extends Enum<T>> ExtendedFieldOption withClass(Class<T> c) {
        return new ExtendedFieldOption(this, c, null);
    }

    /** Decorate the option with a default value. */
    public ExtendedFieldOption withDefault(Object o) {
        return new ExtendedFieldOption(this, null, o);
    }

    /** Returns true if the options is a keyword in the 'mm' definition language. */
    @SuppressWarnings("WeakerAccess")
    public boolean isKeyword() {
        return keyword;
    }

    @NotNull @Override public Object getDefaultValue() {
        return type.getDefault();
    }

    // public String getDocumentation(Locale locale){ return  WIDGET_DOC.getString(name(),locale);}

    /** Returns true if the option is applicable to the specified MetaModelKind. */
    public boolean isApplicable(MetaModelKind k) {
        return metaModels.contains(k);
    }

    /** Returns true if the option is localizable. */
    public boolean isLocalizable() {
        return localizable;
    }

    @Nullable @Override public <T extends Enum<T>> Class<T> getEnumClass() {
        return null;
    }

    @NotNull @Override public FieldOption getFieldOption() {
        return this;
    }

    /** The I18n Key. */
    public String getI18nKey(String id) {
        return getI18nKey(id, 0);
    }
    /** The I18n Key. */
    public String getI18nKey(String id, int n) {
        final StringBuilder result = new StringBuilder(id);
        if (this != LABEL) result.append('.').append(name().toLowerCase());
        if (n > 0) result.append('.').append(n);
        return result.toString();
    }

    /** The Id of the option. */
    @NotNull public String getId() {
        return name().toLowerCase();
    }

    /** Return the type of the option. */
    public FieldOptionType getType() {
        return type;
    }

    /** Serialize this FieldOption to a stream. */
    void serialize(StreamWriter w) {
        w.writeInt(ordinal());
    }

    //~ Methods ......................................................................................................................................

    /** Return all options that are applicable to forms. */
    public static Iterable<FieldOption> forms() {
        return filter(asList(VALUES), f -> f != null && f.isApplicable(MetaModelKind.FORM) && f.isKeyword());
    }

    /** Return the Modifier for a given id, or <code>null</code>. */
    @Nullable public static FieldOption fromId(String id) {
        return idToEnum.get(id);
    }

    /**
     * Return the Modifier for a given id, if it is applicable to the specified mm kind or <code>
     * null</code>.
     */
    @Nullable public static FieldOption fromId(String id, MetaModelKind k) {
        final FieldOption f = fromId(id);
        return f == null ? null : f.isApplicable(k) ? f : null;
    }

    /** Instantiate a FieldOption from a Stream. */
    public static FieldOption instantiate(StreamReader r) {
        return valueOf(r.readInt());
    }

    /** Return all options that are keywords. */
    public static Iterable<FieldOption> keywords() {
        return idToEnum.values();
    }

    /** Returns the FieldOption associated to the specified ordinal. */
    private static FieldOption valueOf(int ordinal) {
        return VALUES[ordinal];
    }

    //~ Static Fields ................................................................................................................................

    public static final EnumSet<FieldOption> NONE   = EnumSet.noneOf(FieldOption.class);
    private static final FieldOption[]       VALUES = values();

    private static final Map<String, FieldOption> idToEnum;

    static {
        idToEnum = new LinkedHashMap<>();
        for (final FieldOption option : VALUES)
            if (option.isKeyword()) idToEnum.put(option.getId(), option);
    }
}
