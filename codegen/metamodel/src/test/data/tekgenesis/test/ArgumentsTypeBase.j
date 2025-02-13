package tekgenesis.test;

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
 * Generated base class for type: ArgumentsType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"name", "last"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class ArgumentsTypeBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String name;
    @JsonProperty @NotNull private String last;

    //~ Constructors .............................................................................................................

    /** Constructor for ArgumentsType */
    @JsonCreator public ArgumentsTypeBase(@JsonProperty("name") @NotNull String name, @JsonProperty("last") @NotNull String last) {
        this.name = name;
        this.last = last;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @JsonProperty @NotNull public String getName() { return name; }

    /** Returns the Last. */
    @JsonProperty @NotNull public String getLast() { return last; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof ArgumentsTypeBase && eq((ArgumentsTypeBase)that);
    }

    private boolean eq(@NotNull ArgumentsTypeBase that) { return equal(name, that.name) && equal(last, that.last); }

    public int hashCode() { return Predefined.hashCodeAll(name, last); }

    /** Attempt to construct a ArgumentsType instance from an InputStream. */
    @NotNull public static ArgumentsType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, ArgumentsType.class); }

    /** Attempt to construct a ArgumentsType instance from a String. */
    @NotNull public static ArgumentsType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, ArgumentsType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 4653999250126367320L;

}
