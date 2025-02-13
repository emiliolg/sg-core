package tekgenesis.sales.basic.service;

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
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: Product.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"productId", "model", "description", "price", "created"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Product
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String productId = "";
    @JsonProperty @NotNull private String model = "";
    @JsonProperty @Nullable private String description = null;
    @JsonProperty @NotNull private BigDecimal price = BigDecimal.ZERO;
    @JsonProperty @NotNull private DateTime created = DateTime.EPOCH;

    //~ Getters ..................................................................................................................

    /** Returns the Product Id. */
    @JsonProperty @NotNull public String getProductId() { return productId; }

    /** Returns the Model. */
    @JsonProperty @NotNull public String getModel() { return model; }

    /** Returns the Description. */
    @JsonProperty @Nullable public String getDescription() { return description; }

    /** Returns the Price. */
    @JsonProperty @NotNull public BigDecimal getPrice() { return price; }

    /** Returns the Created. */
    @JsonProperty @NotNull public DateTime getCreated() { return created; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Product Id. */
    @NotNull public Product setProductId(@NotNull String productId) {
        this.productId = Strings.truncate(productId, 8);
        return this;
    }

    /** Sets the value of the Model. */
    @NotNull public Product setModel(@NotNull String model) {
        this.model = Strings.truncate(model, 30);
        return this;
    }

    /** Sets the value of the Description. */
    @NotNull public Product setDescription(@Nullable String description) {
        this.description = Strings.truncate(description, 100);
        return this;
    }

    /** Sets the value of the Price. */
    @NotNull public Product setPrice(@NotNull BigDecimal price) {
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return this;
    }

    /** Sets the value of the Created. */
    @NotNull public Product setCreated(@NotNull DateTime created) {
        this.created = created;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Product && eq((Product)that);
    }

    private boolean eq(@NotNull Product that) {
        return equal(productId, that.productId) && equal(model, that.model) && equal(description, that.description) && equal(price, that.price) && equal(created, that.created);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(productId, model, description, price, created);
    }

    /** Attempt to construct a Product instance from an InputStream. */
    @NotNull public static Product fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Product.class); }

    /** Attempt to construct a Product instance from a String. */
    @NotNull public static Product fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Product.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 234498937028345395L;

}
