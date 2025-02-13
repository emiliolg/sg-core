package tekgenesis.sales.cart;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.sales.cart.CartForm.ItemsRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.sales.basic.Product;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CartForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CartFormBase
    extends FormInstance<Cart>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Cart cart = Cart.create();
        copyTo(cart);
        createOrUpdateItems(cart);
        cart.insert();
        setId(cart.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Cart cart = find();
        copyTo(cart);
        createOrUpdateItems(cart);
        cart.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Cart find() {
        final Cart value = Cart.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Cart populate() {
        final Cart cart = find();

        setUser(cart.getUser());

        getItems().populate(cart.getItems(), ItemsRow::populate);

        return cart;
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

    /** Copies field values to given cart instance. */
    public void copyTo(@NotNull Cart cart) { cart.setUser(getUser()); }

    /** Updates external references to items. */
    public void createOrUpdateItems(@NotNull Cart cart) {
        cart.getItems().merge(getItems(), (cartItem, row) -> row.copyTo(cartItem));
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public CartForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (CartForm) this;
    }

    /** Returns the value of the display(user). */
    @NotNull public String getUser() { return f.get(Field.USER, String.class); }

    /** Sets the value of the display(user). */
    @NotNull public CartForm setUser(@NotNull String user) {
        f.set(Field.USER, user);
        return (CartForm) this;
    }

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the text_field(subtotal). */
    @Nullable public BigDecimal getSubtotal() { return f.get(Field.SUBTOTAL, BigDecimal.class); }

    /** Invoked when button($B4) is clicked */
    @NotNull public abstract Action checkout();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CartForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CartForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CartForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CartForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(product) value changes */
        @NotNull public abstract Action updateUnitPrice();

        /** Returns the value of the suggest_box(product). */
        @NotNull public Product getProduct() {
            return Predefined.ensureNotNull(Product.find(getProductKey()), "'product' not found");
        }

        /** Returns the key value of the suggest_box(product). */
        @NotNull public String getProductKey() { return f.get(Field.PRODUCT, String.class); }

        /** Sets the value of the suggest_box(product). */
        @NotNull public ItemsRow setProduct(@NotNull Product product) {
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

        /** Populate field values with given cartItem instance. */
        public void populate(@NotNull CartItem cartItem) {
            setProduct(cartItem.getProduct())
            	.setQuantity(cartItem.getQuantity());
        }

        /** Copies field values to given cartItem instance. */
        public void copyTo(@NotNull CartItem cartItem) {
            cartItem.setProduct(getProduct())
            	.setQuantity(getQuantity());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        USER("user"),
        ITEMS("items"),
        PRODUCT("product"),
        UNIT("unit"),
        QUANTITY("quantity"),
        TOTAL("total"),
        $H2("$H2"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
        SUBTOTAL("subtotal"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
