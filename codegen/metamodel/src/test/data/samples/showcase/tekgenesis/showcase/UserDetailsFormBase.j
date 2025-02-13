package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
import tekgenesis.showcase.UserDetailsForm.PropsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: UserDetailsForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class UserDetailsFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.UserDetailsForm");
    }

    /** Invoked when the form is loaded */
    public abstract void loadDetails();

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

    /** Returns the value of the display(id). */
    @NotNull public String getId() { return f.get(Field.ID, String.class); }

    /** Sets the value of the display(id). */
    @NotNull public UserDetailsForm setId(@NotNull String id) {
        f.set(Field.ID, id);
        return (UserDetailsForm) this;
    }

    /** Returns the value of the display(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the display(name). */
    @NotNull public UserDetailsForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (UserDetailsForm) this;
    }

    /** Returns the value of the display(ou). */
    @NotNull public String getOu() { return f.get(Field.OU, String.class); }

    /** Sets the value of the display(ou). */
    @NotNull public UserDetailsForm setOu(@NotNull String ou) {
        f.set(Field.OU, ou);
        return (UserDetailsForm) this;
    }

    /** Returns a {@link FormTable<PropsRow>} instance to handle Props manipulation */
    @NotNull public final FormTable<PropsRow> getProps() { return table(Field.PROPS, PropsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<UserDetailsForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<UserDetailsForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<UserDetailsForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(UserDetailsForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PropsRowBase
        implements FormRowInstance<PropsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(propId). */
        @NotNull public String getPropId() { return f.get(Field.PROP_ID, String.class); }

        /** Sets the value of the display(propId). */
        @NotNull public PropsRow setPropId(@NotNull String propId) {
            f.set(Field.PROP_ID, propId);
            return (PropsRow) this;
        }

        /** Returns the value of the display(propName). */
        @NotNull public String getPropName() { return f.get(Field.PROP_NAME, String.class); }

        /** Sets the value of the display(propName). */
        @NotNull public PropsRow setPropName(@NotNull String propName) {
            f.set(Field.PROP_NAME, propName);
            return (PropsRow) this;
        }

        /** Returns the value of the display(propValue). */
        @NotNull public String getPropValue() { return f.get(Field.PROP_VALUE, String.class); }

        /** Sets the value of the display(propValue). */
        @NotNull public PropsRow setPropValue(@NotNull String propValue) {
            f.set(Field.PROP_VALUE, propValue);
            return (PropsRow) this;
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
        @NotNull public final FormTable<PropsRow> table() { return getProps(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PropsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        NAME("name"),
        OU("ou"),
        PROPS("props"),
        PROP_ID("propId"),
        PROP_NAME("propName"),
        PROP_VALUE("propValue"),
        $F2("$F2"),
        CLOSE("close");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
