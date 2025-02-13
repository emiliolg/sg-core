package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.FilterableTable.ItemsRow;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FilterableTable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FilterableTableBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FilterableTable");
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

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(option). */
    public void setOptionOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTION, items); }

    /** Sets the options of the combo_box(option) with the given KeyMap. */
    public void setOptionOptions(@NotNull KeyMap items) { f.opts(Field.OPTION, items); }

    /** Invoked when button(add10) is clicked */
    @NotNull public abstract Action add10();

    /** Invoked when button(remove10) is clicked */
    @NotNull public abstract Action remove10();

    /** Invoked when button(nextPage) is clicked */
    @NotNull public abstract Action nextPage();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FilterableTable> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FilterableTable> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FilterableTable> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FilterableTable.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(number). */
        public int getNumber() { return f.get(Field.NUMBER, Integer.class); }

        /** Sets the value of the text_field(number). */
        @NotNull public ItemsRow setNumber(int number) {
            f.set(Field.NUMBER, number);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(label). */
        @NotNull public String getLabel() { return f.get(Field.LABEL, String.class); }

        /** Sets the value of the text_field(label). */
        @NotNull public ItemsRow setLabel(@NotNull String label) {
            f.set(Field.LABEL, label);
            return (ItemsRow) this;
        }

        /** Returns the value of the date_box(date). */
        @NotNull public DateOnly getDate() {
            return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
        }

        /** Sets the value of the date_box(date). */
        @NotNull public ItemsRow setDate(@NotNull DateOnly date) {
            f.set(Field.DATE, date);
            return (ItemsRow) this;
        }

        /** Returns the value of the suggest_box(entity). */
        @NotNull public SimpleEntity getEntity() {
            return Predefined.ensureNotNull(SimpleEntity.find(getEntityKey()), "'entity' not found");
        }

        /** Returns the key value of the suggest_box(entity). */
        @NotNull public String getEntityKey() { return f.get(Field.ENTITY, String.class); }

        /** Sets the value of the suggest_box(entity). */
        @NotNull public ItemsRow setEntity(@NotNull SimpleEntity entity) {
            f.set(Field.ENTITY, entity);
            return (ItemsRow) this;
        }

        /** Returns the value of the combo_box(option). */
        @NotNull public Options getOption() { return Options.valueOf(f.get(Field.OPTION, String.class)); }

        /** Sets the value of the combo_box(option). */
        @NotNull public ItemsRow setOption(@NotNull Options option) {
            f.set(Field.OPTION, option);
            return (ItemsRow) this;
        }

        /** Sets the options of the combo_box(option). */
        public void setOptionOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTION, items); }

        /** Sets the options of the combo_box(option) with the given KeyMap. */
        public void setOptionOptions(@NotNull KeyMap items) { f.opts(Field.OPTION, items); }

        /** Returns the value of the check_box(valid). */
        public boolean isValid() { return f.get(Field.VALID, Boolean.class); }

        /** Sets the value of the check_box(valid). */
        @NotNull public ItemsRow setValid(boolean valid) {
            f.set(Field.VALID, valid);
            return (ItemsRow) this;
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
        @NotNull public final FormTable<ItemsRow> table() { return getItems(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ItemsRow) this);
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
        ITEMS("items"),
        NUMBER("number"),
        LABEL("label"),
        DATE("date"),
        ENTITY("entity"),
        OPTION("option"),
        VALID("valid"),
        $B3("$B3"),
        $B4("$B4"),
        ADD10("add10"),
        REMOVE10("remove10"),
        NEXT_PAGE("nextPage");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
