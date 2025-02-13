package tekgenesis.showcase.g;

import java.util.ArrayList;
import tekgenesis.showcase.FlightSchedule;
import tekgenesis.showcase.FlightType;
import java.io.InputStream;
import tekgenesis.common.core.Integers;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: FlightType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"idKey", "name", "from", "to", "price", "schedules"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightTypeBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty private int idKey = 0;
    @JsonProperty @NotNull private String name = "";
    @JsonProperty @NotNull private String from = "";
    @JsonProperty @NotNull private String to = "";
    @JsonProperty private int price = 0;
    @JsonProperty @NotNull private List<FlightSchedule> schedules = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Id Key. */
    @JsonProperty public int getIdKey() { return idKey; }

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the From. */
    @JsonProperty @NotNull public String getFrom() { return from; }

    /** Returns the To. */
    @JsonProperty @NotNull public String getTo() { return to; }

    /** Returns the Price. */
    @JsonProperty public int getPrice() { return price; }

    /** Returns the Schedules. */
    @JsonProperty @NotNull public List<FlightSchedule> getSchedules() { return schedules; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Id Key. */
    @NotNull public FlightType setIdKey(int idKey) {
        this.idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return (FlightType) this;
    }

    /** Sets the value of the Name. */
    @NotNull public FlightType setName(@NotNull String name) {
        this.name = Strings.truncate(name, 255);
        return (FlightType) this;
    }

    /** Sets the value of the From. */
    @NotNull public FlightType setFrom(@NotNull String from) {
        this.from = Strings.truncate(from, 255);
        return (FlightType) this;
    }

    /** Sets the value of the To. */
    @NotNull public FlightType setTo(@NotNull String to) {
        this.to = Strings.truncate(to, 255);
        return (FlightType) this;
    }

    /** Sets the value of the Price. */
    @NotNull public FlightType setPrice(int price) {
        this.price = Integers.checkSignedLength("price", price, false, 9);
        return (FlightType) this;
    }

    /** Sets the value of the Schedules. */
    @NotNull public FlightType setSchedules(@NotNull List<FlightSchedule> schedules) {
        this.schedules = schedules;
        return (FlightType) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightTypeBase && eq((FlightTypeBase)that);
    }

    private boolean eq(@NotNull FlightTypeBase that) {
        return equal(idKey, that.idKey) && equal(name, that.name) && equal(from, that.from) && equal(to, that.to) && equal(price, that.price) && equal(schedules, that.schedules);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(idKey, name, from, to, price, schedules);
    }

    /** Attempt to construct a FlightType instance from an InputStream. */
    @NotNull public static FlightType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightType.class); }

    /** Attempt to construct a FlightType instance from a String. */
    @NotNull public static FlightType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7891915710379475103L;

}
