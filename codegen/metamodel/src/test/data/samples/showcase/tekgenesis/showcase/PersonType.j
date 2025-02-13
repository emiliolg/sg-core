package tekgenesis.showcase;

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
 * Generated base class for type: PersonType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"name", "age"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class PersonType
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String name = "";
    @JsonProperty private int age = 0;

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the Age. */
    @JsonProperty public int getAge() { return age; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Name. */
    @NotNull public PersonType setName(@NotNull String name) {
        this.name = Strings.truncate(name, 50);
        return this;
    }

    /** Sets the value of the Age. */
    @NotNull public PersonType setAge(int age) {
        this.age = Integers.checkSignedLength("age", age, false, 9);
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof PersonType && eq((PersonType)that);
    }

    private boolean eq(@NotNull PersonType that) { return equal(name, that.name) && equal(age, that.age); }

    public int hashCode() { return Predefined.hashCodeAll(name, age); }

    /** Attempt to construct a PersonType instance from an InputStream. */
    @NotNull public static PersonType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, PersonType.class); }

    /** Attempt to construct a PersonType instance from a String. */
    @NotNull public static PersonType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, PersonType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -1577246076899207130L;

}
