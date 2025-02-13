package tekgenesis.test;

import java.math.BigDecimal;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Generated base class for type: Product2.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"productId", "model", "description", "price", "created"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Product2
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String productId;
    @JsonProperty @NotNull private String model = "";
    @JsonProperty @Nullable private String description = null;
    @JsonProperty @NotNull private BigDecimal price = BigDecimal.ZERO;
    @JsonProperty @NotNull private DateTime created = DateTime.EPOCH;

    //~ Constructors .............................................................................................................

    /** Constructor for Product2 */
    @JsonCreator public Product2(@JsonProperty("productId") @NotNull String productId) { this.productId = productId; }

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

    /** Sets the value of the Model. */
    @NotNull public Product2 setModel(@NotNull String model) {
        this.model = Strings.truncate(model, 30);
        return this;
    }

    /** Sets the value of the Description. */
    @NotNull public Product2 setDescription(@Nullable String description) {
        this.description = Strings.truncate(description, 100);
        return this;
    }

    /** Sets the value of the Price. */
    @NotNull public Product2 setPrice(@NotNull BigDecimal price) {
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return this;
    }

    /** Sets the value of the Created. */
    @NotNull public Product2 setCreated(@NotNull DateTime created) {
        this.created = created;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Product2 && equal(productId, ((Product2) that).productId);
    }

    public int hashCode() { return Predefined.hashCodeAll(productId); }

    /** Attempt to construct a Product2 instance from an InputStream. */
    @NotNull public static Product2 fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Product2.class); }

    /** Attempt to construct a Product2 instance from a String. */
    @NotNull public static Product2 fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Product2.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 6154845739786404940L;

}
