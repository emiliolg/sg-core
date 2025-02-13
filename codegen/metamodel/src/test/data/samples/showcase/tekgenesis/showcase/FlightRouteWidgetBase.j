package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.FlightRouteWidget.ItinerariesRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightRouteWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightRouteWidgetBase
    extends WidgetInstance<FlightRoute>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightRoute instance. */
    public void populate(@NotNull FlightRoute flightRoute) {
        setDate(flightRoute.getDate());

        getOrigin().populate(flightRoute.getOrigin());

        getDestiny().populate(flightRoute.getDestiny());

        getItineraries().populate(flightRoute.getItineraries(), ItinerariesRow::populate);
    }

    /** Copies field values to given flightRoute instance. */
    public void copyTo(@NotNull FlightRoute flightRoute) {
        flightRoute.setDate(getDate());

        getOrigin().copyTo(flightRoute.getOrigin());

        getDestiny().copyTo(flightRoute.getDestiny());

        getItineraries().mergeInto(flightRoute.getItineraries(), ItinerariesRow::copyTo, FlightItinerary::new);
    }

    /** Returns the value of the display(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the display(date). */
    @NotNull public FlightRouteWidget setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (FlightRouteWidget) this;
    }

    /** Define {@link AirportWidget instance} to be used during interaction. */
    @NotNull AirportWidget defineOrigin() { return new AirportWidget(); }

    /** Get widget {@link AirportWidget}. */
    @NotNull public AirportWidget getOrigin() { return f.widget(Field.ORIGIN, AirportWidget.class); }

    /** Define {@link AirportWidget instance} to be used during interaction. */
    @NotNull AirportWidget defineDestiny() { return new AirportWidget(); }

    /** Get widget {@link AirportWidget}. */
    @NotNull public AirportWidget getDestiny() { return f.widget(Field.DESTINY, AirportWidget.class); }

    /** Returns the value of the display(airlines). */
    @NotNull public String getAirlines() { return f.get(Field.AIRLINES, String.class); }

    /** Returns the value of the display(out). */
    @NotNull public String getOut() { return f.get(Field.OUT, String.class); }

    /** Returns the value of the display(legs). */
    @NotNull public String getLegs() { return f.get(Field.LEGS, String.class); }

    /** Returns the value of the display(in). */
    @NotNull public String getIn() { return f.get(Field.IN, String.class); }

    /** Returns a {@link FormTable<ItinerariesRow>} instance to handle Itineraries manipulation */
    @NotNull public final FormTable<ItinerariesRow> getItineraries() { return table(Field.ITINERARIES, ItinerariesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

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

    @NotNull private static final Logger logger = Logger.getLogger(FlightRouteWidget.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItinerariesRowBase
        implements FormRowInstance<ItinerariesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Define {@link FlightItineraryWidget instance} to be used during interaction. */
        @NotNull FlightItineraryWidget defineItinerary() { return new FlightItineraryWidget(); }

        /** Get widget {@link FlightItineraryWidget}. */
        @NotNull public FlightItineraryWidget getItinerary() {
            return f.widget(Field.ITINERARY, FlightItineraryWidget.class);
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
        @NotNull public final FormTable<ItinerariesRow> table() { return getItineraries(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ItinerariesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given flightItinerary instance. */
        public void populate(@NotNull FlightItinerary flightItinerary) {

            getItinerary().populate(flightItinerary);
        }

        /** Copies field values to given flightItinerary instance. */
        public void copyTo(@NotNull FlightItinerary flightItinerary) {

            getItinerary().copyTo(flightItinerary);
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        DATE("date"),
        ORIGIN("origin"),
        DESTINY("destiny"),
        $H1("$H1"),
        AIRLINES("airlines"),
        OUT("out"),
        LEGS("legs"),
        IN("in"),
        ITINERARIES("itineraries"),
        ITINERARY("itinerary");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
