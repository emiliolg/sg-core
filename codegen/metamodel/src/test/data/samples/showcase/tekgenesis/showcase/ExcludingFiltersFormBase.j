package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.filter.Filter.CustomOptions;
import tekgenesis.form.filter.FilterFactory;
import tekgenesis.showcase.ExcludingFiltersForm.FiltersRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.ExcludingFiltersForm.ItemsRow;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.filter.Options;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.filter.Filter.StringFilter;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ExcludingFiltersForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ExcludingFiltersFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ExcludingFiltersForm");
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

    /** Returns a {@link FormTable<FiltersRow>} instance to handle Filters manipulation */
    @NotNull public final FormTable<FiltersRow> getFilters() { return table(Field.FILTERS, FiltersRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the dynamic(value) with the given KeyMap. */
    public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExcludingFiltersForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExcludingFiltersForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull public static final ItemsFilters ITEMS_ROW_FILTERS = new ItemsFilters();
    @NotNull private static FormListenerMap<ExcludingFiltersForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ExcludingFiltersForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class FiltersRowBase
        implements FormRowInstance<FiltersRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(title). */
        @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

        /** Sets the value of the display(title). */
        @NotNull public FiltersRow setTitle(@NotNull String title) {
            f.set(Field.TITLE, title);
            return (FiltersRow) this;
        }

        /** Returns the value of the dynamic(value). */
        @NotNull public Seq<Object> getValue() { return f.getArray(Field.VALUE, Object.class); }

        /** Sets the value of the dynamic(value). */
        @NotNull public FiltersRow setValue(@NotNull Iterable<Object> value) {
            f.setArray(Field.VALUE, value);
            return (FiltersRow) this;
        }

        /** Sets the options of the dynamic(value) with the given KeyMap. */
        public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<FiltersRow> table() { return getFilters(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((FiltersRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(label). */
        @NotNull public String getLabel() { return f.get(Field.LABEL, String.class); }

        /** Sets the value of the text_field(label). */
        @NotNull public ItemsRow setLabel(@NotNull String label) {
            f.set(Field.LABEL, label);
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

    public static class ItemsFilters
    {

        //~ Fields ...................................................................................................................

        @NotNull public final StringFilter label;
        @NotNull private final FilterFactory factory = Context.getSingleton(FilterFactory.class);

        //~ Constructors .............................................................................................................

        private ItemsFilters() { label = factory.strFilter(Field.LABEL, ""); }

        //~ Methods ..................................................................................................................

        /** Return a new filter options implementation. */
        @SuppressWarnings("unchecked") @NotNull public <F> CustomOptions<ItemsRow,F> createFilter(@NotNull final String title, @NotNull final Options<? super ItemsRow,F> options) { return factory.customFilter(title, options); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        MAIN("main"),
        FILTER_PANEL("filterPanel"),
        FILTERS("filters"),
        TITLE("title"),
        VALUE("value"),
        CARS_PANEL("carsPanel"),
        ITEMS("items"),
        LABEL("label");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
