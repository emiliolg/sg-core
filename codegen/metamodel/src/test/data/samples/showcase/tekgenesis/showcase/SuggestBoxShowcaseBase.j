package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.util.ArrayList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.Suggestion;
import tekgenesis.showcase.SuggestBoxShowcase.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SuggestBoxShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SuggestBoxShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SuggestBoxShowcase");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

    /** Associates a new message to the specified field and returns it for any further configuration. */
    @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

    /** Returns true if the field value is not null. */
    public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

    /** Resets the given fields. */
    protected void reset(@NotNull Field... fields) { f.reset(fields); }

    /** Focuses given field. */
    protected void focus(@NotNull Field field) { f.focus(field); }

    /** Returns the label of the given field. */
    @NotNull protected String label(@NotNull Field field) { return f.label(field); }

    /** Returns a typed configuration for a given field. */
    @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    /** Returns the value of the internal(today). */
    @NotNull public DateOnly getToday() {
        return DateOnly.fromMilliseconds(f.get(Field.TODAY, Long.class));
    }

    /** Sets the value of the internal(today). */
    @NotNull public SuggestBoxShowcase setToday(@NotNull DateOnly today) {
        f.set(Field.TODAY, today);
        return (SuggestBoxShowcase) this;
    }

    /** Returns the value of the suggest_box(options). */
    @NotNull public Options getOptions() { return Options.valueOf(f.get(Field.OPTIONS, String.class)); }

    /** Sets the value of the suggest_box(options). */
    @NotNull public SuggestBoxShowcase setOptions(@NotNull Options options) {
        f.set(Field.OPTIONS, options);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when suggest_box(entity) value changes */
    @NotNull public abstract Action entityChanged();

    /** Returns the value of the suggest_box(entity). */
    @Nullable public SimpleEntity getEntity() {
        final String key = getEntityKey();
        return key == null ? null : SimpleEntity.find(key);
    }

    /** Returns the key value of the suggest_box(entity). */
    @Nullable public String getEntityKey() { return f.get(Field.ENTITY, String.class); }

    /** Sets the value of the suggest_box(entity). */
    @NotNull public SuggestBoxShowcase setEntity(@Nullable SimpleEntity entity) {
        f.set(Field.ENTITY, entity);
        return (SuggestBoxShowcase) this;
    }

    /** Returns the value of the suggest_box(entityWithSuggest). */
    @Nullable public SimpleEntity getEntityWithSuggest() {
        final String key = getEntityWithSuggestKey();
        return key == null ? null : SimpleEntity.find(key);
    }

    /** Returns the key value of the suggest_box(entityWithSuggest). */
    @Nullable public String getEntityWithSuggestKey() { return f.get(Field.ENTITY_WITH_SUGGEST, String.class); }

    /** Sets the value of the suggest_box(entityWithSuggest). */
    @NotNull public SuggestBoxShowcase setEntityWithSuggest(@Nullable SimpleEntity entityWithSuggest) {
        f.set(Field.ENTITY_WITH_SUGGEST, entityWithSuggest);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when suggest_box(integer) value changes */
    @NotNull public abstract Action integerChanged();

    /** Returns the value of the suggest_box(integer). */
    @Nullable public Integer getInteger() { return f.get(Field.INTEGER, Integer.class); }

    /** Sets the value of the suggest_box(integer). */
    @NotNull public SuggestBoxShowcase setInteger(@Nullable Integer integer) {
        f.set(Field.INTEGER, integer);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when suggest_box(strings) value ui changes */
    @NotNull public abstract Action changed();

    /** Returns the value of the suggest_box(strings). */
    @Nullable public String getStrings() { return f.get(Field.STRINGS, String.class); }

    /** Sets the value of the suggest_box(strings). */
    @NotNull public SuggestBoxShowcase setStrings(@Nullable String strings) {
        f.set(Field.STRINGS, strings);
        return (SuggestBoxShowcase) this;
    }

    /** Sets the value of the suggest_box(strings). */
    @NotNull public SuggestBoxShowcase setStrings(@NotNull Suggestion strings) {
        f.set(Field.STRINGS, strings);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when the user type something on suggest_box(stringsSync) to create suggest list */
    @NotNull public abstract Iterable<Suggestion> suggestSync(@Nullable String query);

    /** Invoked when suggest_box(stringsSync) value changes */
    @NotNull public abstract Action stringsChanged();

    /** Returns the value of the suggest_box(stringsSync). */
    @Nullable public String getStringsSync() { return f.get(Field.STRINGS_SYNC, String.class); }

    /** Sets the value of the suggest_box(stringsSync). */
    @NotNull public SuggestBoxShowcase setStringsSync(@Nullable String stringsSync) {
        f.set(Field.STRINGS_SYNC, stringsSync);
        return (SuggestBoxShowcase) this;
    }

    /** Sets the value of the suggest_box(stringsSync). */
    @NotNull public SuggestBoxShowcase setStringsSync(@NotNull Suggestion stringsSync) {
        f.set(Field.STRINGS_SYNC, stringsSync);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when the user type something on suggest_box(entitySync) to create suggest list */
    @NotNull public abstract Iterable<SimpleEntity> entitySuggestSync(@Nullable String query);

    /** Returns the value of the suggest_box(entitySync). */
    @Nullable public SimpleEntity getEntitySync() {
        final String key = getEntitySyncKey();
        return key == null ? null : SimpleEntity.find(key);
    }

    /** Returns the key value of the suggest_box(entitySync). */
    @Nullable public String getEntitySyncKey() { return f.get(Field.ENTITY_SYNC, String.class); }

    /** Sets the value of the suggest_box(entitySync). */
    @NotNull public SuggestBoxShowcase setEntitySync(@Nullable SimpleEntity entitySync) {
        f.set(Field.ENTITY_SYNC, entitySync);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when button(resetStrings) is clicked */
    @NotNull public abstract Action resetStrings();

    /** Returns the value of the tags_suggest_box(optionTags). */
    @NotNull public Seq<Options> getOptionTags() { return f.getArray(Field.OPTION_TAGS, Options.class); }

    /** Sets the value of the tags_suggest_box(optionTags). */
    @NotNull public SuggestBoxShowcase setOptionTags(@NotNull Iterable<Options> optionTags) {
        f.setArray(Field.OPTION_TAGS, optionTags);
        return (SuggestBoxShowcase) this;
    }

    /** Returns the value of the tags_suggest_box(entityTags). */
    @NotNull public Seq<SimpleEntity> getEntityTags() { return f.getArray(Field.ENTITY_TAGS, SimpleEntity.class); }

    /** Sets the value of the tags_suggest_box(entityTags). */
    @NotNull public SuggestBoxShowcase setEntityTags(@NotNull Iterable<SimpleEntity> entityTags) {
        f.setArray(Field.ENTITY_TAGS, entityTags);
        return (SuggestBoxShowcase) this;
    }

    /** Returns the value of the tags_suggest_box(integerTags). */
    @NotNull public Seq<Integer> getIntegerTags() { return f.getArray(Field.INTEGER_TAGS, Integer.class); }

    /** Sets the value of the tags_suggest_box(integerTags). */
    @NotNull public SuggestBoxShowcase setIntegerTags(@NotNull Iterable<Integer> integerTags) {
        f.setArray(Field.INTEGER_TAGS, integerTags);
        return (SuggestBoxShowcase) this;
    }

    /** Returns the value of the tags_suggest_box(stringTags). */
    @NotNull public Seq<String> getStringTags() { return f.getArray(Field.STRING_TAGS, String.class); }

    /** Sets the value of the tags_suggest_box(stringTags). */
    @NotNull public SuggestBoxShowcase setStringTags(@NotNull Iterable<String> stringTags) {
        f.setArray(Field.STRING_TAGS, stringTags);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when tags_suggest_box(stringsTagsSync) value changes */
    @NotNull public abstract Action stringsTagsSyncChanged();

    /** Returns the value of the tags_suggest_box(stringsTagsSync). */
    @NotNull public Seq<String> getStringsTagsSync() { return f.getArray(Field.STRINGS_TAGS_SYNC, String.class); }

    /** Sets the value of the tags_suggest_box(stringsTagsSync). */
    @NotNull public SuggestBoxShowcase setStringsTagsSync(@NotNull Iterable<String> stringsTagsSync) {
        f.setArray(Field.STRINGS_TAGS_SYNC, stringsTagsSync);
        return (SuggestBoxShowcase) this;
    }

    /** Invoked when tags_suggest_box(entityTagsSync) value changes */
    @NotNull public abstract Action entityTagsSyncChanged();

    /** Returns the value of the tags_suggest_box(entityTagsSync). */
    @NotNull public Seq<SimpleEntity> getEntityTagsSync() {
        return f.getArray(Field.ENTITY_TAGS_SYNC, SimpleEntity.class);
    }

    /** Sets the value of the tags_suggest_box(entityTagsSync). */
    @NotNull public SuggestBoxShowcase setEntityTagsSync(@NotNull Iterable<SimpleEntity> entityTagsSync) {
        f.setArray(Field.ENTITY_TAGS_SYNC, entityTagsSync);
        return (SuggestBoxShowcase) this;
    }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Invoked when button(click) is clicked */
    @NotNull public abstract Action click();

    /** Invoked when button(clear) is clicked */
    @NotNull public abstract Action clear();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SuggestBoxShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SuggestBoxShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SuggestBoxShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SuggestBoxShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(columnStrings) value ui changes */
        @NotNull public abstract Action changed();

        /** Returns the value of the suggest_box(columnStrings). */
        @Nullable public String getColumnStrings() { return f.get(Field.COLUMN_STRINGS, String.class); }

        /** Sets the value of the suggest_box(columnStrings). */
        @NotNull public TableRow setColumnStrings(@Nullable String columnStrings) {
            f.set(Field.COLUMN_STRINGS, columnStrings);
            return (TableRow) this;
        }

        /** Sets the value of the suggest_box(columnStrings). */
        @NotNull public TableRow setColumnStrings(@NotNull Suggestion columnStrings) {
            f.set(Field.COLUMN_STRINGS, columnStrings);
            return (TableRow) this;
        }

        /** Invoked when the user type something on suggest_box(columnStringsSync) to create suggest list */
        @NotNull public abstract Iterable<Suggestion> tableSuggestSync(@Nullable String query);

        /** Invoked when suggest_box(columnStringsSync) value changes */
        @NotNull public abstract Action stringsChanged();

        /** Returns the value of the suggest_box(columnStringsSync). */
        @Nullable public String getColumnStringsSync() { return f.get(Field.COLUMN_STRINGS_SYNC, String.class); }

        /** Sets the value of the suggest_box(columnStringsSync). */
        @NotNull public TableRow setColumnStringsSync(@Nullable String columnStringsSync) {
            f.set(Field.COLUMN_STRINGS_SYNC, columnStringsSync);
            return (TableRow) this;
        }

        /** Sets the value of the suggest_box(columnStringsSync). */
        @NotNull public TableRow setColumnStringsSync(@NotNull Suggestion columnStringsSync) {
            f.set(Field.COLUMN_STRINGS_SYNC, columnStringsSync);
            return (TableRow) this;
        }

        /** Invoked when the user type something on suggest_box(columnEntitySync) to create suggest list */
        @NotNull public abstract Iterable<SimpleEntity> entitySuggestSync(@Nullable String query);

        /** Returns the value of the suggest_box(columnEntitySync). */
        @Nullable public SimpleEntity getColumnEntitySync() {
            final String key = getColumnEntitySyncKey();
            return key == null ? null : SimpleEntity.find(key);
        }

        /** Returns the key value of the suggest_box(columnEntitySync). */
        @Nullable public String getColumnEntitySyncKey() { return f.get(Field.COLUMN_ENTITY_SYNC, String.class); }

        /** Sets the value of the suggest_box(columnEntitySync). */
        @NotNull public TableRow setColumnEntitySync(@Nullable SimpleEntity columnEntitySync) {
            f.set(Field.COLUMN_ENTITY_SYNC, columnEntitySync);
            return (TableRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<TableRow> table() { return getTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((TableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        TODAY("today"),
        OPTIONS("options"),
        ENTITY("entity"),
        ENTITY_WITH_SUGGEST("entityWithSuggest"),
        INTEGER("integer"),
        STRINGS("strings"),
        STRINGS_SYNC("stringsSync"),
        ENTITY_SYNC("entitySync"),
        RESET_STRINGS("resetStrings"),
        WITH_TAGS("withTags"),
        OPTION_TAGS("optionTags"),
        ENTITY_TAGS("entityTags"),
        INTEGER_TAGS("integerTags"),
        STRING_TAGS("stringTags"),
        STRINGS_TAGS_SYNC("stringsTagsSync"),
        ENTITY_TAGS_SYNC("entityTagsSync"),
        TABLE("table"),
        COLUMN_STRINGS("columnStrings"),
        COLUMN_STRINGS_SYNC("columnStringsSync"),
        COLUMN_ENTITY_SYNC("columnEntitySync"),
        CLICK("click"),
        CLEAR("clear");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
