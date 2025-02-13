package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
import tekgenesis.showcase.FlightForm.SectionsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FlightForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightFormBase
    extends FormInstance<Flight>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Flight flight = Flight.create(getId());
        copyTo(flight);
        flight.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Flight flight = find();
        copyTo(flight);
        flight.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Flight find() {
        final Flight value = Flight.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Flight populate() {
        final Flight flight = find();

        setName(flight.getName())
        	.setFrom(flight.getFrom())
        	.setTo(flight.getTo())
        	.setPrice(flight.getPrice());

        return flight;
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

    /** Copies field values to given flight instance. */
    public void copyTo(@NotNull Flight flight) {
        flight.setName(getName())
        	.setFrom(getFrom())
        	.setTo(getTo())
        	.setPrice(getPrice());
    }

    /** Returns the value of the internal(displaying). */
    public boolean isDisplaying() { return f.get(Field.DISPLAYING, Boolean.class); }

    /** Sets the value of the internal(displaying). */
    @NotNull public FlightForm setDisplaying(boolean displaying) {
        f.set(Field.DISPLAYING, displaying);
        return (FlightForm) this;
    }

    /** Returns the value of the text_field(id). */
    public int getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the text_field(id). */
    @NotNull public FlightForm setId(int id) {
        f.set(Field.ID, id);
        return (FlightForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public FlightForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (FlightForm) this;
    }

    /** Returns the value of the text_field(from). */
    @NotNull public String getFrom() { return f.get(Field.FROM, String.class); }

    /** Sets the value of the text_field(from). */
    @NotNull public FlightForm setFrom(@NotNull String from) {
        f.set(Field.FROM, from);
        return (FlightForm) this;
    }

    /** Returns the value of the text_field(to). */
    @NotNull public String getTo() { return f.get(Field.TO, String.class); }

    /** Sets the value of the text_field(to). */
    @NotNull public FlightForm setTo(@NotNull String to) {
        f.set(Field.TO, to);
        return (FlightForm) this;
    }

    /** Returns the value of the text_field(price). */
    public int getPrice() { return f.get(Field.PRICE, Integer.class); }

    /** Sets the value of the text_field(price). */
    @NotNull public FlightForm setPrice(int price) {
        f.set(Field.PRICE, price);
        return (FlightForm) this;
    }

    /** Returns a {@link FormTable<SectionsRow>} instance to handle Sections manipulation */
    @NotNull public final FormTable<SectionsRow> getSections() { return table(Field.SECTIONS, SectionsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FlightForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FlightForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FlightForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FlightForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SectionsRowBase
        implements FormRowInstance<SectionsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(sectionFrom). */
        @Nullable public String getSectionFrom() { return f.get(Field.SECTION_FROM, String.class); }

        /** Sets the value of the text_field(sectionFrom). */
        @NotNull public SectionsRow setSectionFrom(@Nullable String sectionFrom) {
            f.set(Field.SECTION_FROM, sectionFrom);
            return (SectionsRow) this;
        }

        /** Returns the value of the text_field(sectionTo). */
        @Nullable public String getSectionTo() { return f.get(Field.SECTION_TO, String.class); }

        /** Sets the value of the text_field(sectionTo). */
        @NotNull public SectionsRow setSectionTo(@Nullable String sectionTo) {
            f.set(Field.SECTION_TO, sectionTo);
            return (SectionsRow) this;
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
        @NotNull public final FormTable<SectionsRow> table() { return getSections(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SectionsRow) this);
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
        DISPLAYING("displaying"),
        $T2("$T2"),
        DATA("data"),
        ID("id"),
        NAME("name"),
        FROM("from"),
        TO("to"),
        PRICE("price"),
        SECTIONS_TAB("sectionsTab"),
        SECTIONS("sections"),
        SECTION_FROM("sectionFrom"),
        SECTION_TO("sectionTo"),
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
