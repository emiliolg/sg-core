package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.TableFilter.ProductsRow;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableFilter.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableFilterBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TableFilter");
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

    /** 
     * Invoked when tags(colors) value changes
     * Invoked when combo_box(lowest) value changes
     * Invoked when combo_box(highest) value changes
     * Invoked when tags(categories) value changes
     */
    @NotNull public abstract Action refresh();

    /** Returns the value of the tags(colors). */
    @NotNull public Seq<String> getColors() { return f.getArray(Field.COLORS, String.class); }

    /** Sets the value of the tags(colors). */
    @NotNull public TableFilter setColors(@NotNull Iterable<String> colors) {
        f.setArray(Field.COLORS, colors);
        return (TableFilter) this;
    }

    /** Returns the value of the combo_box(lowest). */
    @NotNull public String getLowest() { return f.get(Field.LOWEST, String.class); }

    /** Sets the value of the combo_box(lowest). */
    @NotNull public TableFilter setLowest(@NotNull String lowest) {
        f.set(Field.LOWEST, lowest);
        return (TableFilter) this;
    }

    /** Sets the options of the combo_box(lowest). */
    public void setLowestOptions(@NotNull Iterable<String> items) { f.opts(Field.LOWEST, items); }

    /** Sets the options of the combo_box(lowest) with the given KeyMap. */
    public void setLowestOptions(@NotNull KeyMap items) { f.opts(Field.LOWEST, items); }

    /** Returns the value of the combo_box(highest). */
    @NotNull public String getHighest() { return f.get(Field.HIGHEST, String.class); }

    /** Sets the value of the combo_box(highest). */
    @NotNull public TableFilter setHighest(@NotNull String highest) {
        f.set(Field.HIGHEST, highest);
        return (TableFilter) this;
    }

    /** Sets the options of the combo_box(highest). */
    public void setHighestOptions(@NotNull Iterable<String> items) { f.opts(Field.HIGHEST, items); }

    /** Sets the options of the combo_box(highest) with the given KeyMap. */
    public void setHighestOptions(@NotNull KeyMap items) { f.opts(Field.HIGHEST, items); }

    /** Returns the value of the tags(categories). */
    @NotNull public Seq<String> getCategories() { return f.getArray(Field.CATEGORIES, String.class); }

    /** Sets the value of the tags(categories). */
    @NotNull public TableFilter setCategories(@NotNull Iterable<String> categories) {
        f.setArray(Field.CATEGORIES, categories);
        return (TableFilter) this;
    }

    /** Returns a {@link FormTable<ProductsRow>} instance to handle Products manipulation */
    @NotNull public final FormTable<ProductsRow> getProducts() { return table(Field.PRODUCTS, ProductsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableFilter> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableFilter> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TableFilter> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TableFilter.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ProductsRowBase
        implements FormRowInstance<ProductsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public ProductsRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(price). */
        @NotNull public String getPrice() { return f.get(Field.PRICE, String.class); }

        /** Sets the value of the text_field(price). */
        @NotNull public ProductsRow setPrice(@NotNull String price) {
            f.set(Field.PRICE, price);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(category). */
        @NotNull public String getCategory() { return f.get(Field.CATEGORY, String.class); }

        /** Sets the value of the text_field(category). */
        @NotNull public ProductsRow setCategory(@NotNull String category) {
            f.set(Field.CATEGORY, category);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(color). */
        @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

        /** Sets the value of the text_field(color). */
        @NotNull public ProductsRow setColor(@NotNull String color) {
            f.set(Field.COLOR, color);
            return (ProductsRow) this;
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
        @NotNull public final FormTable<ProductsRow> table() { return getProducts(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ProductsRow) this);
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
        COLORS("colors"),
        $M3("$M3"),
        LOWEST("lowest"),
        HIGHEST("highest"),
        CATEGORIES("categories"),
        PRODUCTS("products"),
        NAME("name"),
        PRICE("price"),
        CATEGORY("category"),
        COLOR("color"),
        $H4("$H4"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
