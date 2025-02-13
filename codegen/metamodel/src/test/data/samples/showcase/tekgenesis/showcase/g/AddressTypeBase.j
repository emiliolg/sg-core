package tekgenesis.showcase.g;

import tekgenesis.showcase.AddressType;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: AddressType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"street", "city", "state", "zip", "country"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class AddressTypeBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String street = "";
    @JsonProperty @NotNull private String city = "";
    @JsonProperty @Nullable private String state = null;
    @JsonProperty @Nullable private String zip = null;
    @JsonProperty @NotNull private String country = "";

    //~ Getters ..................................................................................................................

    /** Returns the Street. */
    @JsonProperty @NotNull public String getStreet() { return street; }

    /** Returns the City. */
    @JsonProperty @NotNull public String getCity() { return city; }

    /** Returns the State. */
    @JsonProperty @Nullable public String getState() { return state; }

    /** Returns the Zip. */
    @JsonProperty @Nullable public String getZip() { return zip; }

    /** Returns the Country. */
    @JsonProperty @NotNull public String getCountry() { return country; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Street. */
    @NotNull public AddressType setStreet(@NotNull String street) {
        this.street = Strings.truncate(street, 60);
        return (AddressType) this;
    }

    /** Sets the value of the City. */
    @NotNull public AddressType setCity(@NotNull String city) {
        this.city = Strings.truncate(city, 40);
        return (AddressType) this;
    }

    /** Sets the value of the State. */
    @NotNull public AddressType setState(@Nullable String state) {
        this.state = Strings.truncate(state, 40);
        return (AddressType) this;
    }

    /** Sets the value of the Zip. */
    @NotNull public AddressType setZip(@Nullable String zip) {
        this.zip = Strings.truncate(zip, 10);
        return (AddressType) this;
    }

    /** Sets the value of the Country. */
    @NotNull public AddressType setCountry(@NotNull String country) {
        this.country = Strings.truncate(country, 30);
        return (AddressType) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof AddressTypeBase && eq((AddressTypeBase)that);
    }

    private boolean eq(@NotNull AddressTypeBase that) {
        return equal(street, that.street) && equal(city, that.city) && equal(state, that.state) && equal(zip, that.zip) && equal(country, that.country);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(street, city, state, zip, country);
    }

    /** Attempt to construct a AddressType instance from an InputStream. */
    @NotNull public static AddressType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, AddressType.class); }

    /** Attempt to construct a AddressType instance from a String. */
    @NotNull public static AddressType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, AddressType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7698884790350466103L;

}
