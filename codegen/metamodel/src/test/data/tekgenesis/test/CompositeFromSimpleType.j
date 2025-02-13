package tekgenesis.test;

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
 * Generated base class for type: CompositeFromSimpleType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"left"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class CompositeFromSimpleType
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String left = "";

    //~ Getters ..................................................................................................................

    /** Returns the Left. */
    @JsonProperty @NotNull public String getLeft() { return left; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Left. */
    @NotNull public CompositeFromSimpleType setLeft(@NotNull String left) {
        this.left = Strings.truncate(left, 8);
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof CompositeFromSimpleType && equal(left, ((CompositeFromSimpleType) that).left);
    }

    public int hashCode() { return Predefined.hashCodeAll(left); }

    /** Attempt to construct a CompositeFromSimpleType instance from an InputStream. */
    @NotNull public static CompositeFromSimpleType fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, CompositeFromSimpleType.class);
    }

    /** Attempt to construct a CompositeFromSimpleType instance from a String. */
    @NotNull public static CompositeFromSimpleType fromJson(@NotNull final String value) {
        return JsonMapping.fromJson(value, CompositeFromSimpleType.class);
    }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -4666287493973430827L;

}
