package tekgenesis.sales.basic;

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
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.CategoryForm.ProductsRow;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CategoryForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CategoryFormBase
    extends FormInstance<Category>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Category category = Category.create(getId());
        copyTo(category);
        category.insert();
        createOrUpdateProducts(category);
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Category category = find();
        copyTo(category);
        category.update();
        createOrUpdateProducts(category);
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Category find() {
        final Category value = Category.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toLong(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Category populate() {
        final Category category = find();

        setName(category.getName())
        	.setDescr(category.getDescr());

        getProducts().populate(category.getProducts(), ProductsRow::populate);

        return category;
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

    /** Copies field values to given category instance. */
    public void copyTo(@NotNull Category category) {
        category.setName(getName())
        	.setDescr(getDescr());
    }

    /** Updates external references to products. */
    public void createOrUpdateProducts(@NotNull Category category) {
        for (final ProductsRow r : getProducts()) {
        	final Product product = Product.find(r.getProductId());
        	if (product == null) {
        		final Product newProduct = Product.create(r.getProductId());
        		r.copyTo(newProduct);
        		newProduct.setCategory(category);
        		newProduct.insert();
        	}
        	else {
        		r.copyTo(product);
        		product.setCategory(category);
        		product.update();
        	}
        }
    }

    /** Returns the value of the text_field(id). */
    public long getId() { return f.get(Field.ID, Long.class); }

    /** Sets the value of the text_field(id). */
    @NotNull public CategoryForm setId(long id) {
        f.set(Field.ID, id);
        return (CategoryForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public CategoryForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (CategoryForm) this;
    }

    /** Returns the value of the text_area(descr). */
    @NotNull public String getDescr() { return f.get(Field.DESCR, String.class); }

    /** Sets the value of the text_area(descr). */
    @NotNull public CategoryForm setDescr(@NotNull String descr) {
        f.set(Field.DESCR, descr);
        return (CategoryForm) this;
    }

    /** Returns a {@link FormTable<ProductsRow>} instance to handle Products manipulation */
    @NotNull public final FormTable<ProductsRow> getProducts() { return table(Field.PRODUCTS, ProductsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(state). */
    public void setStateOptions(@NotNull Iterable<State> items) { f.opts(Field.STATE, items); }

    /** Sets the options of the combo_box(state) with the given KeyMap. */
    public void setStateOptions(@NotNull KeyMap items) { f.opts(Field.STATE, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CategoryForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CategoryForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CategoryForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CategoryForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ProductsRowBase
        implements FormRowInstance<ProductsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(productId). */
        @NotNull public String getProductId() { return f.get(Field.PRODUCT_ID, String.class); }

        /** Sets the value of the text_field(productId). */
        @NotNull public ProductsRow setProductId(@NotNull String productId) {
            f.set(Field.PRODUCT_ID, productId);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(model). */
        @NotNull public String getModel() { return f.get(Field.MODEL, String.class); }

        /** Sets the value of the text_field(model). */
        @NotNull public ProductsRow setModel(@NotNull String model) {
            f.set(Field.MODEL, model);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(description). */
        @Nullable public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the text_field(description). */
        @NotNull public ProductsRow setDescription(@Nullable String description) {
            f.set(Field.DESCRIPTION, description);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(price). */
        @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

        /** Sets the value of the text_field(price). */
        @NotNull public ProductsRow setPrice(@NotNull BigDecimal price) {
            f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
            return (ProductsRow) this;
        }

        /** Returns the value of the combo_box(state). */
        @NotNull public State getState() { return State.valueOf(f.get(Field.STATE, String.class)); }

        /** Sets the value of the combo_box(state). */
        @NotNull public ProductsRow setState(@NotNull State state) {
            f.set(Field.STATE, state);
            return (ProductsRow) this;
        }

        /** Sets the options of the combo_box(state). */
        public void setStateOptions(@NotNull Iterable<State> items) { f.opts(Field.STATE, items); }

        /** Sets the options of the combo_box(state) with the given KeyMap. */
        public void setStateOptions(@NotNull KeyMap items) { f.opts(Field.STATE, items); }

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
            	.setState(product.getState());
        }

        /** Copies field values to given product instance. */
        public void copyTo(@NotNull Product product) {
            product.setModel(getModel())
            	.setDescription(getDescription())
            	.setPrice(getPrice())
            	.setState(getState());
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
        $S2("$S2"),
        ID("id"),
        NAME("name"),
        DESCR("descr"),
        PRODUCTS("products"),
        PRODUCT_ID("productId"),
        MODEL("model"),
        DESCRIPTION("description"),
        PRICE("price"),
        STATE("state"),
        $H3("$H3"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
        $F4("$F4"),
        $B5("$B5"),
        $B6("$B6"),
        $B7("$B7");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
