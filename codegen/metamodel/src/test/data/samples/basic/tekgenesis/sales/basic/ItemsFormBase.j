package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.sales.basic.ItemsForm.ItemsRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ItemsForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ItemsFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.basic.ItemsForm");
    }

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

    /** Returns the value of the internal(count). */
    public int getCount() { return f.get(Field.COUNT, Integer.class); }

    /** Sets the value of the internal(count). */
    @NotNull public ItemsForm setCount(int count) {
        f.set(Field.COUNT, count);
        return (ItemsForm) this;
    }

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the text_field(subtotal). */
    @Nullable public BigDecimal getSubtotal() { return f.get(Field.SUBTOTAL, BigDecimal.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ItemsForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ItemsForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ItemsForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ItemsForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(product). */
        @NotNull public String getProduct() { return f.get(Field.PRODUCT, String.class); }

        /** Sets the value of the text_field(product). */
        @NotNull public ItemsRow setProduct(@NotNull String product) {
            f.set(Field.PRODUCT, product);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(unit). */
        @NotNull public BigDecimal getUnit() { return f.get(Field.UNIT, BigDecimal.class); }

        /** Sets the value of the text_field(unit). */
        @NotNull public ItemsRow setUnit(@NotNull BigDecimal unit) {
            f.set(Field.UNIT, Decimals.scaleAndCheck("unit", unit, false, 10, 2));
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(quantity). */
        public int getQuantity() { return f.get(Field.QUANTITY, Integer.class); }

        /** Sets the value of the text_field(quantity). */
        @NotNull public ItemsRow setQuantity(int quantity) {
            f.set(Field.QUANTITY, quantity);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(total). */
        @NotNull public BigDecimal getTotal() { return f.get(Field.TOTAL, BigDecimal.class); }

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
        COUNT("count"),
        ITEMS("items"),
        PRODUCT("product"),
        UNIT("unit"),
        QUANTITY("quantity"),
        TOTAL("total"),
        SUBTOTAL("subtotal");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
