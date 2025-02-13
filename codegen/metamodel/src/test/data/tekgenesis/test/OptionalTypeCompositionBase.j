package tekgenesis.test;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: OptionalTypeComposition.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"left", "right"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class OptionalTypeCompositionBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @Nullable private Product left = null;
    @JsonProperty @Nullable private Product right = null;

    //~ Getters ..................................................................................................................

    /** Returns the Left. */
    @JsonProperty @Nullable public Product getLeft() { return left; }

    /** Returns the Right. */
    @JsonProperty @Nullable public Product getRight() { return right; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Left. */
    @NotNull public OptionalTypeComposition setLeft(@Nullable Product left) {
        this.left = left;
        return (OptionalTypeComposition) this;
    }

    /** Sets the value of the Right. */
    @NotNull public OptionalTypeComposition setRight(@Nullable Product right) {
        this.right = right;
        return (OptionalTypeComposition) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof OptionalTypeCompositionBase && eq((OptionalTypeCompositionBase)that);
    }

    private boolean eq(@NotNull OptionalTypeCompositionBase that) { return equal(left, that.left) && equal(right, that.right); }

    public int hashCode() { return Predefined.hashCodeAll(left, right); }

    /** Attempt to construct a OptionalTypeComposition instance from an InputStream. */
    @NotNull public static OptionalTypeComposition fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, OptionalTypeComposition.class);
    }

    /** Attempt to construct a OptionalTypeComposition instance from a String. */
    @NotNull public static OptionalTypeComposition fromJson(@NotNull final String value) {
        return JsonMapping.fromJson(value, OptionalTypeComposition.class);
    }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 345321712378932240L;

}
