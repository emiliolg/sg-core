package tekgenesis.showcase.g;

import tekgenesis.common.core.DateTime;
import tekgenesis.showcase.FlightItinerary;
import java.io.InputStream;
import tekgenesis.common.core.Integers;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: FlightItinerary.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"airline", "departure", "arrival", "legs"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightItineraryBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String airline = "";
    @JsonProperty @NotNull private DateTime departure = DateTime.EPOCH;
    @JsonProperty @NotNull private DateTime arrival = DateTime.EPOCH;
    @JsonProperty private int legs = 0;

    //~ Getters ..................................................................................................................

    /** Returns the Airline. */
    @JsonProperty @NotNull public String getAirline() { return airline; }

    /** Returns the Departure. */
    @JsonProperty @NotNull public DateTime getDeparture() { return departure; }

    /** Returns the Arrival. */
    @JsonProperty @NotNull public DateTime getArrival() { return arrival; }

    /** Returns the Legs. */
    @JsonProperty public int getLegs() { return legs; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Airline. */
    @NotNull public FlightItinerary setAirline(@NotNull String airline) {
        this.airline = Strings.truncate(airline, 255);
        return (FlightItinerary) this;
    }

    /** Sets the value of the Departure. */
    @NotNull public FlightItinerary setDeparture(@NotNull DateTime departure) {
        this.departure = departure;
        return (FlightItinerary) this;
    }

    /** Sets the value of the Arrival. */
    @NotNull public FlightItinerary setArrival(@NotNull DateTime arrival) {
        this.arrival = arrival;
        return (FlightItinerary) this;
    }

    /** Sets the value of the Legs. */
    @NotNull public FlightItinerary setLegs(int legs) {
        this.legs = Integers.checkSignedLength("legs", legs, false, 9);
        return (FlightItinerary) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightItineraryBase && eq((FlightItineraryBase)that);
    }

    private boolean eq(@NotNull FlightItineraryBase that) {
        return equal(airline, that.airline) && equal(departure, that.departure) && equal(arrival, that.arrival) && equal(legs, that.legs);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(airline, departure, arrival, legs);
    }

    /** Attempt to construct a FlightItinerary instance from an InputStream. */
    @NotNull public static FlightItinerary fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightItinerary.class); }

    /** Attempt to construct a FlightItinerary instance from a String. */
    @NotNull public static FlightItinerary fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightItinerary.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 1804131348670198179L;

}
