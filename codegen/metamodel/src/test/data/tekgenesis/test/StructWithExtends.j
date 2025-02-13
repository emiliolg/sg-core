package tekgenesis.test;

import java.math.BigDecimal;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
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
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: StructWithExtends.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"description", "price", "created"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class StructWithExtends
    extends SimpleBaseStruct
    implements Serializable
{

    //~ Fields ...................................................................................................................

    @JsonProperty @Nullable private String description = null;
    @JsonProperty @NotNull private BigDecimal price = BigDecimal.ZERO;
    @JsonProperty @NotNull private DateTime created = DateTime.EPOCH;

    //~ Getters ..................................................................................................................

    /** Returns the Description. */
    @JsonProperty @Nullable public String getDescription() { return description; }

    /** Returns the Price. */
    @JsonProperty @NotNull public BigDecimal getPrice() { return price; }

    /** Returns the Created. */
    @JsonProperty @NotNull public DateTime getCreated() { return created; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Description. */
    @NotNull public StructWithExtends setDescription(@Nullable String description) {
        this.description = Strings.truncate(description, 100);
        return this;
    }

    /** Sets the value of the Price. */
    @NotNull public StructWithExtends setPrice(@NotNull BigDecimal price) {
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return this;
    }

    /** Sets the value of the Created. */
    @NotNull public StructWithExtends setCreated(@NotNull DateTime created) {
        this.created = created;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof StructWithExtends && eq((StructWithExtends)that);
    }

    private boolean eq(@NotNull StructWithExtends that) {
        return equal(description, that.description) && equal(price, that.price) && equal(created, that.created);
    }

    public int hashCode() { return Predefined.hashCodeAll(description, price, created); }

    /** Attempt to construct a StructWithExtends instance from an InputStream. */
    @NotNull public static StructWithExtends fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, StructWithExtends.class);
    }

    /** Attempt to construct a StructWithExtends instance from a String. */
    @NotNull public static StructWithExtends fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, StructWithExtends.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -7556374209836180055L;

}
