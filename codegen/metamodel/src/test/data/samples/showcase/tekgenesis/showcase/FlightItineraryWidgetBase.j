package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightItineraryWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightItineraryWidgetBase
    extends WidgetInstance<FlightItinerary>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightItinerary instance. */
    public void populate(@NotNull FlightItinerary flightItinerary) {
        setAirline(flightItinerary.getAirline())
        	.setDeparture(flightItinerary.getDeparture())
        	.setArrival(flightItinerary.getArrival())
        	.setLegs(flightItinerary.getLegs());
    }

    /** Copies field values to given flightItinerary instance. */
    public void copyTo(@NotNull FlightItinerary flightItinerary) {
        flightItinerary.setAirline(getAirline())
        	.setDeparture(getDeparture())
        	.setArrival(getArrival())
        	.setLegs(getLegs());
    }

    /** Returns the value of the display(airline). */
    @NotNull public String getAirline() { return f.get(Field.AIRLINE, String.class); }

    /** Sets the value of the display(airline). */
    @NotNull public FlightItineraryWidget setAirline(@NotNull String airline) {
        f.set(Field.AIRLINE, airline);
        return (FlightItineraryWidget) this;
    }

    /** Returns the value of the display(departure). */
    @NotNull public DateTime getDeparture() {
        return DateTime.fromMilliseconds(f.get(Field.DEPARTURE, Long.class));
    }

    /** Sets the value of the display(departure). */
    @NotNull public FlightItineraryWidget setDeparture(@NotNull DateTime departure) {
        f.set(Field.DEPARTURE, departure);
        return (FlightItineraryWidget) this;
    }

    /** Returns the value of the display(legs). */
    public int getLegs() { return f.get(Field.LEGS, Integer.class); }

    /** Sets the value of the display(legs). */
    @NotNull public FlightItineraryWidget setLegs(int legs) {
        f.set(Field.LEGS, legs);
        return (FlightItineraryWidget) this;
    }

    /** Returns the value of the display(arrival). */
    @NotNull public DateTime getArrival() {
        return DateTime.fromMilliseconds(f.get(Field.ARRIVAL, Long.class));
    }

    /** Sets the value of the display(arrival). */
    @NotNull public FlightItineraryWidget setArrival(@NotNull DateTime arrival) {
        f.set(Field.ARRIVAL, arrival);
        return (FlightItineraryWidget) this;
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

    @NotNull private static final Logger logger = Logger.getLogger(FlightItineraryWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        AIRLINE("airline"),
        DEPARTURE("departure"),
        LEGS("legs"),
        ARRIVAL("arrival");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
