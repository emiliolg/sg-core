package models;

import java.util.ArrayList;
import java.io.InputStream;
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
 * Generated base class for type: Person.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"countries"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Person
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private List<String> countries = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Countries. */
    @JsonProperty @NotNull public List<String> getCountries() { return countries; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Countries. */
    @NotNull public Person setCountries(@NotNull List<String> countries) {
        this.countries = Strings.truncate(countries, 255);
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Person && equal(countries, ((Person) that).countries);
    }

    public int hashCode() { return Predefined.hashCodeAll(countries); }

    /** Attempt to construct a Person instance from an InputStream. */
    @NotNull public static Person fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Person.class); }

    /** Attempt to construct a Person instance from a String. */
    @NotNull public static Person fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Person.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -6129324419371212523L;

}
