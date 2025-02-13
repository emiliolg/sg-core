package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
 * Generated base class for form: PersonWithDniForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PersonWithDniFormBase
    extends FormInstance<PersonWithDni>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final PersonWithDni personWithDni = PersonWithDni.create(getDniKey());
        copyTo(personWithDni);
        personWithDni.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final PersonWithDni personWithDni = find();
        copyTo(personWithDni);
        personWithDni.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public PersonWithDni find() {
        final PersonWithDni value = PersonWithDni.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.DNI, DNI.find(key)); }

    @NotNull public String keyAsString() { return "" + escapeCharOn(getDniKey(), ':'); }

    /** Invoked when populating a form instance */
    @NotNull public PersonWithDni populate() {
        final PersonWithDni personWithDni = find();

        setName(personWithDni.getName())
        	.setLastname(personWithDni.getLastname());

        return personWithDni;
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

    /** Copies field values to given personWithDni instance. */
    public void copyTo(@NotNull PersonWithDni personWithDni) {
        personWithDni.setName(getName())
        	.setLastname(getLastname());
    }

    /** Returns the value of the suggest_box(dni). */
    @NotNull public DNI getDni() {
        return Predefined.ensureNotNull(DNI.find(getDniKey()), "'dni' not found");
    }

    /** Returns the key value of the suggest_box(dni). */
    @NotNull public String getDniKey() { return f.get(Field.DNI, String.class); }

    /** Sets the value of the suggest_box(dni). */
    @NotNull public PersonWithDniForm setDni(@NotNull DNI dni) {
        f.set(Field.DNI, dni);
        return (PersonWithDniForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public PersonWithDniForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (PersonWithDniForm) this;
    }

    /** Returns the value of the text_field(lastname). */
    @NotNull public String getLastname() { return f.get(Field.LASTNAME, String.class); }

    /** Sets the value of the text_field(lastname). */
    @NotNull public PersonWithDniForm setLastname(@NotNull String lastname) {
        f.set(Field.LASTNAME, lastname);
        return (PersonWithDniForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PersonWithDniForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PersonWithDniForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<PersonWithDniForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(PersonWithDniForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        DNI("dni"),
        NAME("name"),
        LASTNAME("lastname"),
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
