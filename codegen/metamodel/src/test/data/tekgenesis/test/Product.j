package tekgenesis.test;

import java.math.BigDecimal;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import java.io.InputStream;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: Product.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"productId", "model", "description", "price", "created"})
public class Product
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @NotNull private String productId = "";
    @NotNull private String model = "";
    @Nullable private String description = null;
    @NotNull private BigDecimal price = BigDecimal.ZERO;
    @NotNull private DateTime created = DateTime.EPOCH;

    //~ Getters ..................................................................................................................

    /** Returns the Product Id. */
    @NotNull public String getProductId() { return productId; }

    /** Returns the Model. */
    @NotNull public String getModel() { return model; }

    /** Returns the Description. */
    @Nullable public String getDescription() { return description; }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return price; }

    /** Returns the Created. */
    @NotNull public DateTime getCreated() { return created; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Product Id. */
    public void setProductId(@NotNull String productId) { this.productId = Strings.truncate(productId, 8); }

    /** Sets the value of the Model. */
    public void setModel(@NotNull String model) { this.model = Strings.truncate(model, 30); }

    /** Sets the value of the Description. */
    public void setDescription(@Nullable String description) { this.description = Strings.truncate(description, 100); }

    /** Sets the value of the Price. */
    public void setPrice(@NotNull BigDecimal price) { this.price = Decimals.scaleAndCheck(price, false, 10, 2); }

    /** Sets the value of the Created. */
    public void setCreated(@NotNull DateTime created) { this.created = created; }

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

    private static final long serialVersionUID = -7455029451241576987L;

}
