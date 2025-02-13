package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.Flights.FlightsRow;
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
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: Flights.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightsBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.Flights");
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

    /** Returns a {@link FormTable<FlightsRow>} instance to handle Flights manipulation */
    @NotNull public final FormTable<FlightsRow> getFlights() { return table(Field.FLIGHTS, FlightsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on loading table(flights). */
    public void loadFlights() {
        final FormTable<FlightsRow> table = getFlights();
        Flight.forEach(currentFlight -> table.add().populate(currentFlight));
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Flights> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Flights> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<Flights> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(Flights.class);

    //~ Inner Classes ............................................................................................................

    public abstract class FlightsRowBase
        implements FormRowInstance<FlightsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(id). */
        public int getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the text_field(id). */
        @NotNull public FlightsRow setId(int id) {
            f.set(Field.ID, id);
            return (FlightsRow) this;
        }

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public FlightsRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (FlightsRow) this;
        }

        /** Returns the value of the text_field(from). */
        @NotNull public String getFrom() { return f.get(Field.FROM, String.class); }

        /** Sets the value of the text_field(from). */
        @NotNull public FlightsRow setFrom(@NotNull String from) {
            f.set(Field.FROM, from);
            return (FlightsRow) this;
        }

        /** Returns the value of the text_field(to). */
        @NotNull public String getTo() { return f.get(Field.TO, String.class); }

        /** Sets the value of the text_field(to). */
        @NotNull public FlightsRow setTo(@NotNull String to) {
            f.set(Field.TO, to);
            return (FlightsRow) this;
        }

        /** Returns the value of the text_field(price). */
        public int getPrice() { return f.get(Field.PRICE, Integer.class); }

        /** Sets the value of the text_field(price). */
        @NotNull public FlightsRow setPrice(int price) {
            f.set(Field.PRICE, price);
            return (FlightsRow) this;
        }

        /** Create and set a new FlightForm instance */
        @NotNull public FlightForm createFlightSubform() { return f.init(Field.FLIGHT_SUBFORM, FlightForm.class); }

        /** Create and populates set a new FlightForm instance with a pk */
        @NotNull public FlightForm createFlightSubform(@NotNull String key) { return f.init(Field.FLIGHT_SUBFORM, FlightForm.class, key); }

        /** 
         * Get the FlightForm if defined, or null otherwise.
         * @see #createFlightSubform
         */
        @Nullable public FlightForm getFlightSubform() { return f.subform(Field.FLIGHT_SUBFORM, FlightForm.class); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<FlightsRow> table() { return getFlights(); }

        /** Remove row from table and delete associated Flight instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Flight instance = isDefined(Field.ID) ? Flight.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((FlightsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given flight instance. */
        public void populate(@NotNull Flight flight) {
            setId(flight.getIdKey())
            	.setName(flight.getName())
            	.setFrom(flight.getFrom())
            	.setTo(flight.getTo())
            	.setPrice(flight.getPrice());
        }

        /** Copies field values to given flight instance. */
        public void copyTo(@NotNull Flight flight) {
            flight.setName(getName())
            	.setFrom(getFrom())
            	.setTo(getTo())
            	.setPrice(getPrice());
        }

        /** Return primary key of bound {@link Flight} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        RESULTS("results"),
        FLIGHTS("flights"),
        ID("id"),
        NAME("name"),
        FROM("from"),
        TO("to"),
        PRICE("price"),
        FLIGHT_SUBFORM("flightSubform");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
