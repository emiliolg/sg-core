package tekgenesis.sales.basic.service;

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
 * Generated base class for type: Country.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"name", "iso2"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Country
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String name = "";
    @JsonProperty @NotNull private String iso2 = "";

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the Iso2. */
    @JsonProperty @NotNull public String getIso2() { return iso2; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Name. */
    @NotNull public Country setName(@NotNull String name) {
        this.name = Strings.truncate(name, 30);
        return this;
    }

    /** Sets the value of the Iso2. */
    @NotNull public Country setIso2(@NotNull String iso2) {
        this.iso2 = Strings.truncate(iso2, 2);
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Country && eq((Country)that);
    }

    private boolean eq(@NotNull Country that) { return equal(name, that.name) && equal(iso2, that.iso2); }

    public int hashCode() { return Predefined.hashCodeAll(name, iso2); }

    /** Attempt to construct a Country instance from an InputStream. */
    @NotNull public static Country fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Country.class); }

    /** Attempt to construct a Country instance from a String. */
    @NotNull public static Country fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Country.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 4533191144412885251L;

}
