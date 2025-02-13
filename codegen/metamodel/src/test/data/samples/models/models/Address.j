package models;

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
 * Generated base class for type: Address.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"street", "number", "country"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Address
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String street = "";
    @JsonProperty private int number = 0;
    @JsonProperty @NotNull private String country = "";

    //~ Getters ..................................................................................................................

    /** Returns the Street. */
    @JsonProperty @NotNull public String getStreet() { return street; }

    /** Returns the Number. */
    @JsonProperty public int getNumber() { return number; }

    /** Returns the Country. */
    @JsonProperty @NotNull public String getCountry() { return country; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Street. */
    @NotNull public Address setStreet(@NotNull String street) {
        this.street = Strings.truncate(street, 50);
        return this;
    }

    /** Sets the value of the Number. */
    @NotNull public Address setNumber(int number) {
        this.number = Integers.checkSignedLength("number", number, false, 9);
        return this;
    }

    /** Sets the value of the Country. */
    @NotNull public Address setCountry(@NotNull String country) {
        this.country = Strings.truncate(country, 255);
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Address && eq((Address)that);
    }

    private boolean eq(@NotNull Address that) {
        return equal(street, that.street) && equal(number, that.number) && equal(country, that.country);
    }

    public int hashCode() { return Predefined.hashCodeAll(street, number, country); }

    /** Attempt to construct a Address instance from an InputStream. */
    @NotNull public static Address fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Address.class); }

    /** Attempt to construct a Address instance from a String. */
    @NotNull public static Address fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Address.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7525623067583961918L;

}
