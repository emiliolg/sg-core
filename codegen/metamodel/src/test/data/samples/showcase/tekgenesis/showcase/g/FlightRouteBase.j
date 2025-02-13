package tekgenesis.showcase.g;

import tekgenesis.showcase.Airport;
import java.util.ArrayList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.showcase.FlightItinerary;
import tekgenesis.showcase.FlightRoute;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: FlightRoute.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"date", "origin", "destiny", "itineraries"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightRouteBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private DateOnly date = DateOnly.EPOCH;
    @JsonProperty @NotNull private Airport origin = new Airport();
    @JsonProperty @NotNull private Airport destiny = new Airport();
    @JsonProperty @NotNull private List<FlightItinerary> itineraries = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Date. */
    @JsonProperty @NotNull public DateOnly getDate() { return date; }

    /** Returns the Origin. */
    @JsonProperty @NotNull public Airport getOrigin() { return origin; }

    /** Returns the Destiny. */
    @JsonProperty @NotNull public Airport getDestiny() { return destiny; }

    /** Returns the Itineraries. */
    @JsonProperty @NotNull public List<FlightItinerary> getItineraries() { return itineraries; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Date. */
    @NotNull public FlightRoute setDate(@NotNull DateOnly date) {
        this.date = date;
        return (FlightRoute) this;
    }

    /** Sets the value of the Origin. */
    @NotNull public FlightRoute setOrigin(@NotNull Airport origin) {
        this.origin = origin;
        return (FlightRoute) this;
    }

    /** Sets the value of the Destiny. */
    @NotNull public FlightRoute setDestiny(@NotNull Airport destiny) {
        this.destiny = destiny;
        return (FlightRoute) this;
    }

    /** Sets the value of the Itineraries. */
    @NotNull public FlightRoute setItineraries(@NotNull List<FlightItinerary> itineraries) {
        this.itineraries = itineraries;
        return (FlightRoute) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightRouteBase && eq((FlightRouteBase)that);
    }

    private boolean eq(@NotNull FlightRouteBase that) {
        return equal(date, that.date) && equal(origin, that.origin) && equal(destiny, that.destiny) && equal(itineraries, that.itineraries);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(date, origin, destiny, itineraries);
    }

    /** Attempt to construct a FlightRoute instance from an InputStream. */
    @NotNull public static FlightRoute fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightRoute.class); }

    /** Attempt to construct a FlightRoute instance from a String. */
    @NotNull public static FlightRoute fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightRoute.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -1776642682670642316L;

}
