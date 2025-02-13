package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: PaymentTypeForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PaymentTypeFormBase
    extends FormInstance<PaymentType>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final PaymentType paymentType = PaymentType.create();
        copyTo(paymentType);
        paymentType.insert();
        setId(paymentType.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final PaymentType paymentType = find();
        copyTo(paymentType);
        paymentType.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public PaymentType find() {
        final PaymentType value = PaymentType.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public PaymentType populate() {
        final PaymentType paymentType = find();

        setType(paymentType.getType())
        	.setSubtype(paymentType.getSubtype())
        	.setDescription(paymentType.getDescription());

        return paymentType;
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

    /** Copies field values to given paymentType instance. */
    public void copyTo(@NotNull PaymentType paymentType) {
        paymentType.setType(getType())
        	.setSubtype(getSubtype())
        	.setDescription(getDescription());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public PaymentTypeForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (PaymentTypeForm) this;
    }

    /** Returns the value of the combo_box(type). */
    @NotNull public PaymentOption getType() {
        return PaymentOption.valueOf(f.get(Field.TYPE, String.class));
    }

    /** Sets the value of the combo_box(type). */
    @NotNull public PaymentTypeForm setType(@NotNull PaymentOption type) {
        f.set(Field.TYPE, type);
        return (PaymentTypeForm) this;
    }

    /** Sets the options of the combo_box(type). */
    public void setTypeOptions(@NotNull Iterable<PaymentOption> items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the combo_box(type) with the given KeyMap. */
    public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

    /** Returns the value of the text_field(subtype). */
    @NotNull public String getSubtype() { return f.get(Field.SUBTYPE, String.class); }

    /** Sets the value of the text_field(subtype). */
    @NotNull public PaymentTypeForm setSubtype(@NotNull String subtype) {
        f.set(Field.SUBTYPE, subtype);
        return (PaymentTypeForm) this;
    }

    /** Returns the value of the text_field(description). */
    @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

    /** Sets the value of the text_field(description). */
    @NotNull public PaymentTypeForm setDescription(@NotNull String description) {
        f.set(Field.DESCRIPTION, description);
        return (PaymentTypeForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PaymentTypeForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PaymentTypeForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<PaymentTypeForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(PaymentTypeForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        TYPE("type"),
        SUBTYPE("subtype"),
        DESCRIPTION("description"),
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
