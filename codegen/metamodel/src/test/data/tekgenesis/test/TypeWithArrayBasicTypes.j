package tekgenesis.test;

import java.util.ArrayList;
import java.math.BigDecimal;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import java.io.InputStream;
import tekgenesis.common.core.Integers;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Reals;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: TypeWithArrayBasicTypes.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"productIds", "models", "prices", "amounts", "creations"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class TypeWithArrayBasicTypes
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private List<String> productIds = new ArrayList<>();
    @JsonProperty @NotNull private List<Double> models = new ArrayList<>();
    @JsonProperty @NotNull private List<BigDecimal> prices = new ArrayList<>();
    @JsonProperty @NotNull private List<Integer> amounts = new ArrayList<>();
    @JsonProperty @NotNull private List<DateTime> creations = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Product Ids. */
    @JsonProperty @NotNull public List<String> getProductIds() { return productIds; }

    /** Returns the Models. */
    @JsonProperty @NotNull public List<Double> getModels() { return models; }

    /** Returns the Prices. */
    @JsonProperty @NotNull public List<BigDecimal> getPrices() { return prices; }

    /** Returns the Amounts. */
    @JsonProperty @NotNull public List<Integer> getAmounts() { return amounts; }

    /** Returns the Creations. */
    @JsonProperty @NotNull public List<DateTime> getCreations() { return creations; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Product Ids. */
    @NotNull public TypeWithArrayBasicTypes setProductIds(@NotNull List<String> productIds) {
        this.productIds = Strings.truncate(productIds, 8);
        return this;
    }

    /** Sets the value of the Models. */
    @NotNull public TypeWithArrayBasicTypes setModels(@NotNull List<Double> models) {
        this.models = Reals.checkSigned("models", models, false);
        return this;
    }

    /** Sets the value of the Prices. */
    @NotNull public TypeWithArrayBasicTypes setPrices(@NotNull List<BigDecimal> prices) {
        this.prices = Decimals.scaleAndCheck("prices", prices, false, 10, 2);
        return this;
    }

    /** Sets the value of the Amounts. */
    @NotNull public TypeWithArrayBasicTypes setAmounts(@NotNull List<Integer> amounts) {
        this.amounts = Integers.checkSignedLength("amounts", amounts, false, 9);
        return this;
    }

    /** Sets the value of the Creations. */
    @NotNull public TypeWithArrayBasicTypes setCreations(@NotNull List<DateTime> creations) {
        this.creations = creations;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof TypeWithArrayBasicTypes && eq((TypeWithArrayBasicTypes)that);
    }

    private boolean eq(@NotNull TypeWithArrayBasicTypes that) {
        return equal(productIds, that.productIds) && equal(models, that.models) && equal(prices, that.prices) && equal(amounts, that.amounts) && equal(creations, that.creations);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(productIds, models, prices, amounts, creations);
    }

    /** Attempt to construct a TypeWithArrayBasicTypes instance from an InputStream. */
    @NotNull public static TypeWithArrayBasicTypes fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, TypeWithArrayBasicTypes.class);
    }

    /** Attempt to construct a TypeWithArrayBasicTypes instance from a String. */
    @NotNull public static TypeWithArrayBasicTypes fromJson(@NotNull final String value) {
        return JsonMapping.fromJson(value, TypeWithArrayBasicTypes.class);
    }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -4902555915942253379L;

}
