package tekgenesis.showcase.g;

import tekgenesis.showcase.Airport;
import java.io.InputStream;
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
 * Generated base class for type: Airport.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"name", "city"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class AirportBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String name = "";
    @JsonProperty @NotNull private String city = "";

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the City. */
    @JsonProperty @NotNull public String getCity() { return city; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Name. */
    @NotNull public Airport setName(@NotNull String name) {
        this.name = Strings.truncate(name, 255);
        return (Airport) this;
    }

    /** Sets the value of the City. */
    @NotNull public Airport setCity(@NotNull String city) {
        this.city = Strings.truncate(city, 255);
        return (Airport) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof AirportBase && eq((AirportBase)that);
    }

    private boolean eq(@NotNull AirportBase that) { return equal(name, that.name) && equal(city, that.city); }

    public int hashCode() { return Predefined.hashCodeAll(name, city); }

    /** Attempt to construct a Airport instance from an InputStream. */
    @NotNull public static Airport fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Airport.class); }

    /** Attempt to construct a Airport instance from a String. */
    @NotNull public static Airport fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Airport.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -8544044548156531502L;

}
