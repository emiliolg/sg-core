package tekgenesis.sales.basic;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Generated base class for type: StoreType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"name", "address", "latitude", "longitude"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class StoreType
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String name;
    @JsonProperty @NotNull private String address;
    @JsonProperty private double latitude;
    @JsonProperty private double longitude;

    //~ Constructors .............................................................................................................

    /** Constructor for StoreType */
    @JsonCreator public StoreType(@JsonProperty("name") @NotNull String name, @JsonProperty("address") @NotNull String address, @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the Address. */
    @JsonProperty @NotNull public String getAddress() { return address; }

    /** Returns the Latitude. */
    @JsonProperty public double getLatitude() { return latitude; }

    /** Returns the Longitude. */
    @JsonProperty public double getLongitude() { return longitude; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof StoreType && eq((StoreType)that);
    }

    private boolean eq(@NotNull StoreType that) {
        return equal(name, that.name) && equal(address, that.address) && equal(latitude, that.latitude) && equal(longitude, that.longitude);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(name, address, latitude, longitude);
    }

    /** Attempt to construct a StoreType instance from an InputStream. */
    @NotNull public static StoreType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, StoreType.class); }

    /** Attempt to construct a StoreType instance from a String. */
    @NotNull public static StoreType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, StoreType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -4367642915274736668L;

}
