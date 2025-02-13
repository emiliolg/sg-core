package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.escapeCharOn;

/** 
 * Generated base class for form: ProductDescription.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ProductDescriptionBase
    extends FormInstance<Object>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    public void setPrimaryKey(@NotNull String key) { f.set(Field.PRODUCT, Product.find(key)); }

    @NotNull public String keyAsString() { return "" + escapeCharOn(getProductKey(), ':'); }

    /** Invoked when populating a form instance */
    @NotNull public abstract Object populate();

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

    /** Returns the value of the internal(product). */
    @NotNull public Product getProduct() {
        return Predefined.ensureNotNull(Product.find(getProductKey()), "'product' not found");
    }

    /** Returns the key value of the internal(product). */
    @NotNull public String getProductKey() { return f.get(Field.PRODUCT, String.class); }

    /** Sets the value of the internal(product). */
    @NotNull public ProductDescription setProduct(@NotNull Product product) {
        f.set(Field.PRODUCT, product);
        return (ProductDescription) this;
    }

    /** Returns the value of the text_field(model). */
    @NotNull public String getModel() { return f.get(Field.MODEL, String.class); }

    /** Sets the value of the text_field(model). */
    @NotNull public ProductDescription setModel(@NotNull String model) {
        f.set(Field.MODEL, model);
        return (ProductDescription) this;
    }

    /** Returns the value of the text_field(description). */
    @Nullable public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

    /** Sets the value of the text_field(description). */
    @NotNull public ProductDescription setDescription(@Nullable String description) {
        f.set(Field.DESCRIPTION, description);
        return (ProductDescription) this;
    }

    /** Returns the value of the text_field(price). */
    @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

    /** Sets the value of the text_field(price). */
    @NotNull public ProductDescription setPrice(@NotNull BigDecimal price) {
        f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
        return (ProductDescription) this;
    }

    /** Invoked when button(review) is clicked */
    @NotNull public abstract Action review();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductDescription> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductDescription> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ProductDescription> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ProductDescription.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        PRODUCT("product"),
        MODEL("model"),
        DESCRIPTION("description"),
        PRICE("price"),
        REVIEW("review"),
        $F2("$F2"),
        SUBMIT("submit"),
        DRAFT("draft"),
        $B3("$B3"),
        $B4("$B4");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
