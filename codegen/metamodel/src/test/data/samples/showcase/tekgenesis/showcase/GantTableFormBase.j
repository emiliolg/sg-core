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
import tekgenesis.showcase.GantTableForm.GantRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: GantTableForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class GantTableFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.GantTableForm");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Returns a {@link FormTable<GantRow>} instance to handle Gant manipulation */
    @NotNull public final FormTable<GantRow> getGant() { return table(Field.GANT, GantRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GantTableForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GantTableForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<GantTableForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(GantTableForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class GantRowBase
        implements FormRowInstance<GantRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the display(name). */
        @NotNull public GantRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (GantRow) this;
        }

        /** Returns the value of the internal(description). */
        @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the internal(description). */
        @NotNull public GantRow setDescription(@NotNull String description) {
            f.set(Field.DESCRIPTION, description);
            return (GantRow) this;
        }

        /** Returns the value of the internal(from). */
        public double getFrom() { return f.get(Field.FROM, Double.class); }

        /** Sets the value of the internal(from). */
        @NotNull public GantRow setFrom(double from) {
            f.set(Field.FROM, from);
            return (GantRow) this;
        }

        /** Returns the value of the internal(amount). */
        public double getAmount() { return f.get(Field.AMOUNT, Double.class); }

        /** Sets the value of the internal(amount). */
        @NotNull public GantRow setAmount(double amount) {
            f.set(Field.AMOUNT, amount);
            return (GantRow) this;
        }

        /** Returns the value of the internal(day). */
        public int getDay() { return f.get(Field.DAY, Integer.class); }

        /** Sets the value of the internal(day). */
        @NotNull public GantRow setDay(int day) {
            f.set(Field.DAY, day);
            return (GantRow) this;
        }

        /** Returns the value of the progress(sunday). */
        public double getSunday() { return f.get(Field.SUNDAY, Double.class); }

        /** Returns the value of the display(monday). */
        public boolean isMonday() { return f.get(Field.MONDAY, Boolean.class); }

        /** Sets the value of the display(monday). */
        @NotNull public GantRow setMonday(boolean monday) {
            f.set(Field.MONDAY, monday);
            return (GantRow) this;
        }

        /** Returns the value of the display(tuesday). */
        public boolean isTuesday() { return f.get(Field.TUESDAY, Boolean.class); }

        /** Sets the value of the display(tuesday). */
        @NotNull public GantRow setTuesday(boolean tuesday) {
            f.set(Field.TUESDAY, tuesday);
            return (GantRow) this;
        }

        /** Returns the value of the display(wednesday). */
        public boolean isWednesday() { return f.get(Field.WEDNESDAY, Boolean.class); }

        /** Sets the value of the display(wednesday). */
        @NotNull public GantRow setWednesday(boolean wednesday) {
            f.set(Field.WEDNESDAY, wednesday);
            return (GantRow) this;
        }

        /** Returns the value of the display(thursday). */
        public boolean isThursday() { return f.get(Field.THURSDAY, Boolean.class); }

        /** Sets the value of the display(thursday). */
        @NotNull public GantRow setThursday(boolean thursday) {
            f.set(Field.THURSDAY, thursday);
            return (GantRow) this;
        }

        /** Returns the value of the display(friday). */
        public boolean isFriday() { return f.get(Field.FRIDAY, Boolean.class); }

        /** Sets the value of the display(friday). */
        @NotNull public GantRow setFriday(boolean friday) {
            f.set(Field.FRIDAY, friday);
            return (GantRow) this;
        }

        /** Returns the value of the display(saturday). */
        public boolean isSaturday() { return f.get(Field.SATURDAY, Boolean.class); }

        /** Sets the value of the display(saturday). */
        @NotNull public GantRow setSaturday(boolean saturday) {
            f.set(Field.SATURDAY, saturday);
            return (GantRow) this;
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
        @NotNull public final FormTable<GantRow> table() { return getGant(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((GantRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        GANT("gant"),
        NAME("name"),
        DESCRIPTION("description"),
        FROM("from"),
        AMOUNT("amount"),
        DAY("day"),
        SUNDAY("sunday"),
        MONDAY("monday"),
        TUESDAY("tuesday"),
        WEDNESDAY("wednesday"),
        THURSDAY("thursday"),
        FRIDAY("friday"),
        SATURDAY("saturday");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
