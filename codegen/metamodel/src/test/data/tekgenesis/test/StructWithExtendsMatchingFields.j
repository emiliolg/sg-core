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
 * Generated base class for type: StructWithExtendsMatchingFields.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"description", "price", "created"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class StructWithExtendsMatchingFields
    extends BaseStruct
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
    @NotNull public StructWithExtendsMatchingFields setDescription(@Nullable String description) {
        this.description = Strings.truncate(description, 100);
        return this;
    }

    /** Sets the value of the Price. */
    @NotNull public StructWithExtendsMatchingFields setPrice(@NotNull BigDecimal price) {
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return this;
    }

    /** Sets the value of the Created. */
    @NotNull public StructWithExtendsMatchingFields setCreated(@NotNull DateTime created) {
        this.created = created;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof StructWithExtendsMatchingFields && eq((StructWithExtendsMatchingFields)that);
    }

    private boolean eq(@NotNull StructWithExtendsMatchingFields that) {
        return equal(description, that.description) && equal(price, that.price) && equal(created, that.created);
    }

    public int hashCode() { return Predefined.hashCodeAll(description, price, created); }

    /** Attempt to construct a StructWithExtendsMatchingFields instance from an InputStream. */
    @NotNull public static StructWithExtendsMatchingFields fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, StructWithExtendsMatchingFields.class);
    }

    /** Attempt to construct a StructWithExtendsMatchingFields instance from a String. */
    @NotNull public static StructWithExtendsMatchingFields fromJson(@NotNull final String value) {
        return JsonMapping.fromJson(value, StructWithExtendsMatchingFields.class);
    }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -7882912681076632652L;

}
