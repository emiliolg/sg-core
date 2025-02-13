package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.DateTime;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CustomerSearchableForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerSearchableFormBase
    extends FormInstance<CustomerSearchable>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final CustomerSearchable customerSearchable = CustomerSearchable.create();
        copyTo(customerSearchable);
        customerSearchable.insert();
        setId(customerSearchable.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final CustomerSearchable customerSearchable = find();
        copyTo(customerSearchable);
        customerSearchable.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public CustomerSearchable find() {
        final CustomerSearchable value = CustomerSearchable.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public CustomerSearchable populate() {
        final CustomerSearchable customerSearchable = find();

        setFirstName(customerSearchable.getFirstName())
        	.setLastName(customerSearchable.getLastName())
        	.setDocument(customerSearchable.getDocument())
        	.setBirthDate(customerSearchable.getBirthDate())
        	.setSex(customerSearchable.getSex())
        	.setSomeExtraField(customerSearchable.getSomeExtraField());

        return customerSearchable;
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

    /** Copies field values to given customerSearchable instance. */
    public void copyTo(@NotNull CustomerSearchable customerSearchable) {
        customerSearchable.setFirstName(getFirstName())
        	.setLastName(getLastName())
        	.setDocument(getDocument())
        	.setBirthDate(getBirthDate())
        	.setSex(getSex());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public CustomerSearchableForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (CustomerSearchableForm) this;
    }

    /** Returns the value of the text_field(firstName). */
    @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

    /** Sets the value of the text_field(firstName). */
    @NotNull public CustomerSearchableForm setFirstName(@NotNull String firstName) {
        f.set(Field.FIRST_NAME, firstName);
        return (CustomerSearchableForm) this;
    }

    /** Returns the value of the text_field(lastName). */
    @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

    /** Sets the value of the text_field(lastName). */
    @NotNull public CustomerSearchableForm setLastName(@NotNull String lastName) {
        f.set(Field.LAST_NAME, lastName);
        return (CustomerSearchableForm) this;
    }

    /** Returns the value of the text_field(document). */
    @NotNull public BigDecimal getDocument() { return f.get(Field.DOCUMENT, BigDecimal.class); }

    /** Sets the value of the text_field(document). */
    @NotNull public CustomerSearchableForm setDocument(@NotNull BigDecimal document) {
        f.set(Field.DOCUMENT, Decimals.scaleAndCheck("document", document, false, 10, 0));
        return (CustomerSearchableForm) this;
    }

    /** Returns the value of the date_time_box(birthDate). */
    @NotNull public DateTime getBirthDate() {
        return DateTime.fromMilliseconds(f.get(Field.BIRTH_DATE, Long.class));
    }

    /** Sets the value of the date_time_box(birthDate). */
    @NotNull public CustomerSearchableForm setBirthDate(@NotNull DateTime birthDate) {
        f.set(Field.BIRTH_DATE, birthDate);
        return (CustomerSearchableForm) this;
    }

    /** Returns the value of the combo_box(sex). */
    @NotNull public Sex getSex() { return Sex.valueOf(f.get(Field.SEX, String.class)); }

    /** Sets the value of the combo_box(sex). */
    @NotNull public CustomerSearchableForm setSex(@NotNull Sex sex) {
        f.set(Field.SEX, sex);
        return (CustomerSearchableForm) this;
    }

    /** Sets the options of the combo_box(sex). */
    public void setSexOptions(@NotNull Iterable<Sex> items) { f.opts(Field.SEX, items); }

    /** Sets the options of the combo_box(sex) with the given KeyMap. */
    public void setSexOptions(@NotNull KeyMap items) { f.opts(Field.SEX, items); }

    /** Returns the value of the text_field(someExtraField). */
    @NotNull public String getSomeExtraField() { return f.get(Field.SOME_EXTRA_FIELD, String.class); }

    /** Sets the value of the text_field(someExtraField). */
    @NotNull public CustomerSearchableForm setSomeExtraField(@NotNull String someExtraField) {
        f.set(Field.SOME_EXTRA_FIELD, someExtraField);
        return (CustomerSearchableForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerSearchableForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerSearchableForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerSearchableForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerSearchableForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        DOCUMENT("document"),
        BIRTH_DATE("birthDate"),
        SEX("sex"),
        SOME_EXTRA_FIELD("someExtraField"),
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
