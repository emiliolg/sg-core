package tekgenesis.sales.cart;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.sales.basic.Category;
import tekgenesis.common.core.Decimals;
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
import tekgenesis.common.Predefined;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.cart.Products.ProductsRow;
import tekgenesis.common.core.Resource;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: Products.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ProductsBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.cart.Products");
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

    /** Returns a {@link FormTable<ProductsRow>} instance to handle Products manipulation */
    @NotNull public final FormTable<ProductsRow> getProducts() { return table(Field.PRODUCTS, ProductsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Products> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Products> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<Products> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(Products.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ProductsRowBase
        implements FormRowInstance<ProductsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(productId). */
        @NotNull public String getProductId() { return f.get(Field.PRODUCT_ID, String.class); }

        /** Sets the value of the display(productId). */
        @NotNull public ProductsRow setProductId(@NotNull String productId) {
            f.set(Field.PRODUCT_ID, productId);
            return (ProductsRow) this;
        }

        /** Returns the value of the display(model). */
        @NotNull public String getModel() { return f.get(Field.MODEL, String.class); }

        /** Sets the value of the display(model). */
        @NotNull public ProductsRow setModel(@NotNull String model) {
            f.set(Field.MODEL, model);
            return (ProductsRow) this;
        }

        /** Returns the value of the suggest_box(category). */
        @NotNull public Category getCategory() {
            return Predefined.ensureNotNull(Category.find(getCategoryKey()), "'category' not found");
        }

        /** Returns the key value of the suggest_box(category). */
        @NotNull public String getCategoryKey() { return f.get(Field.CATEGORY, String.class); }

        /** Sets the value of the suggest_box(category). */
        @NotNull public ProductsRow setCategory(@NotNull Category category) {
            f.set(Field.CATEGORY, category);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(description). */
        @Nullable public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the text_field(description). */
        @NotNull public ProductsRow setDescription(@Nullable String description) {
            f.set(Field.DESCRIPTION, description);
            return (ProductsRow) this;
        }

        /** Returns the value of the check_box(active). */
        public boolean isActive() { return f.get(Field.ACTIVE, Boolean.class); }

        /** Sets the value of the check_box(active). */
        @NotNull public ProductsRow setActive(boolean active) {
            f.set(Field.ACTIVE, active);
            return (ProductsRow) this;
        }

        /** Returns the value of the display(price). */
        @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

        /** Sets the value of the display(price). */
        @NotNull public ProductsRow setPrice(@NotNull BigDecimal price) {
            f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
            return (ProductsRow) this;
        }

        /** Returns the value of the image(image). */
        @NotNull public Resource getImage() { return f.get(Field.IMAGE, Resource.class); }

        /** Sets the value of the image(image). */
        @NotNull public ProductsRow setImage(@NotNull Resource image) {
            f.set(Field.IMAGE, image);
            return (ProductsRow) this;
        }

        /** Invoked when button(goTo) is clicked */
        @NotNull public abstract Action goToProduct();

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

        /** Remove row from table and delete associated Product instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Product instance = isDefined(Field.PRODUCT_ID) ? Product.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((ProductsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given product instance. */
        public void populate(@NotNull Product product) {
            setProductId(product.getProductId())
            	.setModel(product.getModel())
            	.setDescription(product.getDescription())
            	.setPrice(product.getPrice())
            	.setCategory(product.getCategory());
        }

        /** Copies field values to given product instance. */
        public void copyTo(@NotNull Product product) {
            product.setModel(getModel())
            	.setDescription(getDescription())
            	.setPrice(getPrice())
            	.setCategory(getCategory());
        }

        /** Return primary key of bound {@link Product} */
        @NotNull public String keyAsString() { return "" + getProductId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        PRODUCTS("products"),
        PRODUCT_ID("productId"),
        MODEL("model"),
        CATEGORY("category"),
        DESCRIPTION("description"),
        ACTIVE("active"),
        PRICE("price"),
        IMAGE("image"),
        GO_TO("goTo"),
        $H2("$H2"),
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
