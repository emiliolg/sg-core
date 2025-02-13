package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.escapeCharOn;
import static tekgenesis.common.core.Strings.splitToArray;

/** 
 * Generated base class for form: AttrDiffersPKForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class AttrDiffersPKFormBase
    extends FormInstance<SomeEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final SomeEntity someEntity = SomeEntity.create(getAltoInt(), getMartinsString());
        copyTo(someEntity);
        someEntity.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final SomeEntity someEntity = find();
        copyTo(someEntity);
        someEntity.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public SomeEntity find() {
        final SomeEntity value = SomeEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public final void setPrimaryKey(@NotNull String key) {
        final String[] parts = splitToArray(key, 2);
        f.set(Field.ALTO_INT, Conversions.toInt(parts[0]));
        f.set(Field.MARTINS_STRING, parts[1]);
    }

    @NotNull public String keyAsString() {
        return "" + getAltoInt() + ":" + escapeCharOn(getMartinsString(), ':');
    }

    /** Invoked when populating a form instance */
    @NotNull public SomeEntity populate() {
        final SomeEntity someEntity = find();

        setMgBool(someEntity.isSomeBool());

        return someEntity;
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

    /** Copies field values to given someEntity instance. */
    public void copyTo(@NotNull SomeEntity someEntity) { someEntity.setSomeBool(isMgBool()); }

    /** Returns the value of the text_field(altoInt). */
    public int getAltoInt() { return f.get(Field.ALTO_INT, Integer.class); }

    /** Sets the value of the text_field(altoInt). */
    @NotNull public AttrDiffersPKForm setAltoInt(int altoInt) {
        f.set(Field.ALTO_INT, altoInt);
        return (AttrDiffersPKForm) this;
    }

    /** Returns the value of the check_box(mgBool). */
    public boolean isMgBool() { return f.get(Field.MG_BOOL, Boolean.class); }

    /** Sets the value of the check_box(mgBool). */
    @NotNull public AttrDiffersPKForm setMgBool(boolean mgBool) {
        f.set(Field.MG_BOOL, mgBool);
        return (AttrDiffersPKForm) this;
    }

    /** Returns the value of the text_field(martinsString). */
    @NotNull public String getMartinsString() { return f.get(Field.MARTINS_STRING, String.class); }

    /** Sets the value of the text_field(martinsString). */
    @NotNull public AttrDiffersPKForm setMartinsString(@NotNull String martinsString) {
        f.set(Field.MARTINS_STRING, martinsString);
        return (AttrDiffersPKForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AttrDiffersPKForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AttrDiffersPKForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<AttrDiffersPKForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(AttrDiffersPKForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        ALTO_INT("altoInt"),
        MG_BOOL("mgBool"),
        MARTINS_STRING("martinsString");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
