package tekgenesis.showcase.g;

import tekgenesis.showcase.FlightOption;
import tekgenesis.showcase.FlightPrice;
import tekgenesis.showcase.FlightRoute;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: FlightOption.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"out", "in", "price"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightOptionBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private FlightRoute out = new FlightRoute();
    @JsonProperty @NotNull private FlightRoute in = new FlightRoute();
    @JsonProperty @NotNull private FlightPrice price = new FlightPrice();

    //~ Getters ..................................................................................................................

    /** Returns the Out. */
    @JsonProperty @NotNull public FlightRoute getOut() { return out; }

    /** Returns the In. */
    @JsonProperty @NotNull public FlightRoute getIn() { return in; }

    /** Returns the Price. */
    @JsonProperty @NotNull public FlightPrice getPrice() { return price; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Out. */
    @NotNull public FlightOption setOut(@NotNull FlightRoute out) {
        this.out = out;
        return (FlightOption) this;
    }

    /** Sets the value of the In. */
    @NotNull public FlightOption setIn(@NotNull FlightRoute in) {
        this.in = in;
        return (FlightOption) this;
    }

    /** Sets the value of the Price. */
    @NotNull public FlightOption setPrice(@NotNull FlightPrice price) {
        this.price = price;
        return (FlightOption) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightOptionBase && eq((FlightOptionBase)that);
    }

    private boolean eq(@NotNull FlightOptionBase that) {
        return equal(out, that.out) && equal(in, that.in) && equal(price, that.price);
    }

    public int hashCode() { return Predefined.hashCodeAll(out, in, price); }

    /** Attempt to construct a FlightOption instance from an InputStream. */
    @NotNull public static FlightOption fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightOption.class); }

    /** Attempt to construct a FlightOption instance from a String. */
    @NotNull public static FlightOption fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightOption.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -8822151085338197065L;

}
