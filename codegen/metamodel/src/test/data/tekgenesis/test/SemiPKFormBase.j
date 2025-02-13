package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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

/** 
 * Generated base class for form: SemiPKForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SemiPKFormBase
    extends FormInstance<SomeEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public abstract Action create();

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
    @NotNull public abstract SomeEntity find();

    /** Invoked when populating a form instance */
    @NotNull public SomeEntity populate() {
        final SomeEntity someEntity = find();

        setSomeBool(someEntity.isSomeBool());

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
    public void copyTo(@NotNull SomeEntity someEntity) { someEntity.setSomeBool(isSomeBool()); }

    /** Returns the value of the text_field(oneInt). */
    public int getOneInt() { return f.get(Field.ONE_INT, Integer.class); }

    /** Sets the value of the text_field(oneInt). */
    @NotNull public SemiPKForm setOneInt(int oneInt) {
        f.set(Field.ONE_INT, oneInt);
        return (SemiPKForm) this;
    }

    /** Returns the value of the check_box(someBool). */
    public boolean isSomeBool() { return f.get(Field.SOME_BOOL, Boolean.class); }

    /** Sets the value of the check_box(someBool). */
    @NotNull public SemiPKForm setSomeBool(boolean someBool) {
        f.set(Field.SOME_BOOL, someBool);
        return (SemiPKForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SemiPKForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SemiPKForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SemiPKForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SemiPKForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        ONE_INT("oneInt"),
        SOME_BOOL("someBool");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
