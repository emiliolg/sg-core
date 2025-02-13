package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Resource;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ProductForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ProductFormBase
    extends FormInstance<Product>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Product product = Product.create(getProductId());
        copyTo(product);
        product.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Product product = find();
        copyTo(product);
        product.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Product find() {
        final Product value = Product.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.PRODUCT_ID, key); }

    @NotNull public String keyAsString() { return "" + getProductId(); }

    /** Invoked when populating a form instance */
    @NotNull public Product populate() {
        final Product product = find();

        setModel(product.getModel())
        	.setDescription(product.getDescription())
        	.setPrice(product.getPrice())
        	.setState(product.getState())
        	.setCategory(product.getCategory());

        return product;
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

    /** Copies field values to given product instance. */
    public void copyTo(@NotNull Product product) {
        product.setModel(getModel())
        	.setDescription(getDescription())
        	.setPrice(getPrice())
        	.setState(getState())
        	.setCategory(getCategory());
    }

    /** Returns the value of the text_field(productId). */
    @NotNull public String getProductId() { return f.get(Field.PRODUCT_ID, String.class); }

    /** Sets the value of the text_field(productId). */
    @NotNull public ProductForm setProductId(@NotNull String productId) {
        f.set(Field.PRODUCT_ID, productId);
        return (ProductForm) this;
    }

    /** Returns the value of the text_field(model). */
    @NotNull public String getModel() { return f.get(Field.MODEL, String.class); }

    /** Sets the value of the text_field(model). */
    @NotNull public ProductForm setModel(@NotNull String model) {
        f.set(Field.MODEL, model);
        return (ProductForm) this;
    }

    /** Returns the value of the text_field(description). */
    @Nullable public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

    /** Sets the value of the text_field(description). */
    @NotNull public ProductForm setDescription(@Nullable String description) {
        f.set(Field.DESCRIPTION, description);
        return (ProductForm) this;
    }

    /** Returns the value of the text_field(price). */
    @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

    /** Sets the value of the text_field(price). */
    @NotNull public ProductForm setPrice(@NotNull BigDecimal price) {
        f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
        return (ProductForm) this;
    }

    /** Returns the value of the combo_box(state). */
    @NotNull public State getState() { return State.valueOf(f.get(Field.STATE, String.class)); }

    /** Sets the value of the combo_box(state). */
    @NotNull public ProductForm setState(@NotNull State state) {
        f.set(Field.STATE, state);
        return (ProductForm) this;
    }

    /** Sets the options of the combo_box(state). */
    public void setStateOptions(@NotNull Iterable<State> items) { f.opts(Field.STATE, items); }

    /** Sets the options of the combo_box(state) with the given KeyMap. */
    public void setStateOptions(@NotNull KeyMap items) { f.opts(Field.STATE, items); }

    /** Invoked when the 'Create new' options of the suggest_box(category) is clicked */
    @NotNull public abstract Action createCategory(@Nullable String text);

    /** Returns the value of the suggest_box(category). */
    @NotNull public Category getCategory() {
        return Predefined.ensureNotNull(Category.find(getCategoryKey()), "'category' not found");
    }

    /** Returns the key value of the suggest_box(category). */
    @NotNull public String getCategoryKey() { return f.get(Field.CATEGORY, String.class); }

    /** Sets the value of the suggest_box(category). */
    @NotNull public ProductForm setCategory(@NotNull Category category) {
        f.set(Field.CATEGORY, category);
        return (ProductForm) this;
    }

    /** Returns the value of the upload(gallery). */
    @NotNull public Seq<Resource> getGallery() { return f.getArray(Field.GALLERY, Resource.class); }

    /** Sets the value of the upload(gallery). */
    @NotNull public ProductForm setGallery(@NotNull Iterable<Resource> gallery) {
        f.setArray(Field.GALLERY, gallery);
        return (ProductForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ProductForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ProductForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        PRODUCT_ID("productId"),
        MODEL("model"),
        DESCRIPTION("description"),
        PRICE("price"),
        STATE("state"),
        CATEGORY("category"),
        GALLERY("gallery"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
