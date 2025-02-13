package tekgenesis.sales.cart;

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
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.cart.ProductListService.PathRow;
import tekgenesis.sales.cart.ProductListService.ProductsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ProductListService.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ProductListServiceBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.cart.ProductListService");
    }

    /** Invoked when the form is loaded */
    public abstract void onLoad();

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

    /** Returns the value of the text_field(title). */
    @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

    /** Sets the value of the text_field(title). */
    @NotNull public ProductListService setTitle(@NotNull String title) {
        f.set(Field.TITLE, title);
        return (ProductListService) this;
    }

    /** Returns a {@link FormTable<PathRow>} instance to handle Path manipulation */
    @NotNull public final FormTable<PathRow> getPath() { return table(Field.PATH, PathRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<ProductsRow>} instance to handle Products manipulation */
    @NotNull public final FormTable<ProductsRow> getProducts() { return table(Field.PRODUCTS, ProductsRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductListService> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ProductListService> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ProductListService> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ProductListService.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PathRowBase
        implements FormRowInstance<PathRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(pathId). */
        @NotNull public String getPathId() { return f.get(Field.PATH_ID, String.class); }

        /** Sets the value of the text_field(pathId). */
        @NotNull public PathRow setPathId(@NotNull String pathId) {
            f.set(Field.PATH_ID, pathId);
            return (PathRow) this;
        }

        /** Returns the value of the text_field(pathDescription). */
        @NotNull public String getPathDescription() { return f.get(Field.PATH_DESCRIPTION, String.class); }

        /** Sets the value of the text_field(pathDescription). */
        @NotNull public PathRow setPathDescription(@NotNull String pathDescription) {
            f.set(Field.PATH_DESCRIPTION, pathDescription);
            return (PathRow) this;
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
        @NotNull public final FormTable<PathRow> table() { return getPath(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PathRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class ProductsRowBase
        implements FormRowInstance<ProductsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(id). */
        @NotNull public String getId() { return f.get(Field.ID, String.class); }

        /** Sets the value of the text_field(id). */
        @NotNull public ProductsRow setId(@NotNull String id) {
            f.set(Field.ID, id);
            return (ProductsRow) this;
        }

        /** Returns the value of the text_field(description). */
        @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the text_field(description). */
        @NotNull public ProductsRow setDescription(@NotNull String description) {
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
        TITLE("title"),
        PATH("path"),
        PATH_ID("pathId"),
        PATH_DESCRIPTION("pathDescription"),
        PRODUCTS("products"),
        ID("id"),
        DESCRIPTION("description"),
        PRICE("price");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
