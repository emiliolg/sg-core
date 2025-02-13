package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: AddressWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class AddressWidgetBase
    extends WidgetInstance<Address>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given address instance. */
    public void populate(@NotNull Address address) {
        setStreet(address.getStreet())
        	.setNumber(address.getNumber())
        	.setPostal(address.getPostal())
        	.setPrimary(address.isPrimary());
    }

    /** Copies field values to given address instance. */
    public void copyTo(@NotNull Address address) {
        address.setPostal(getPostal())
        	.setPrimary(isPrimary());
    }

    /** Returns the value of the text_field(street). */
    @NotNull public String getStreet() { return f.get(Field.STREET, String.class); }

    /** Sets the value of the text_field(street). */
    @NotNull public AddressWidget setStreet(@NotNull String street) {
        f.set(Field.STREET, street);
        return (AddressWidget) this;
    }

    /** Returns the value of the text_field(number). */
    public int getNumber() { return f.get(Field.NUMBER, Integer.class); }

    /** Sets the value of the text_field(number). */
    @NotNull public AddressWidget setNumber(int number) {
        f.set(Field.NUMBER, number);
        return (AddressWidget) this;
    }

    /** Returns the value of the text_field(postal). */
    public int getPostal() { return f.get(Field.POSTAL, Integer.class); }

    /** Sets the value of the text_field(postal). */
    @NotNull public AddressWidget setPostal(int postal) {
        f.set(Field.POSTAL, postal);
        return (AddressWidget) this;
    }

    /** Returns the value of the check_box(primary). */
    public boolean isPrimary() { return f.get(Field.PRIMARY, Boolean.class); }

    /** Sets the value of the check_box(primary). */
    @NotNull public AddressWidget setPrimary(boolean primary) {
        f.set(Field.PRIMARY, primary);
        return (AddressWidget) this;
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

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(AddressWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        STREET("street"),
        NUMBER("number"),
        POSTAL("postal"),
        PRIMARY("primary");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
