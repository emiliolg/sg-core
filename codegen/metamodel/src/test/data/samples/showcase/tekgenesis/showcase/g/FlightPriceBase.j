package tekgenesis.showcase.g;

import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.showcase.FlightPrice;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
 * Generated base class for type: FlightPrice.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"price", "tax"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightPriceBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private BigDecimal price = BigDecimal.ZERO;
    @JsonProperty @NotNull private BigDecimal tax = BigDecimal.ZERO;

    //~ Getters ..................................................................................................................

    /** Returns the Price. */
    @JsonProperty @NotNull public BigDecimal getPrice() { return price; }

    /** Returns the Tax. */
    @JsonProperty @NotNull public BigDecimal getTax() { return tax; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Price. */
    @NotNull public FlightPrice setPrice(@NotNull BigDecimal price) {
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return (FlightPrice) this;
    }

    /** Sets the value of the Tax. */
    @NotNull public FlightPrice setTax(@NotNull BigDecimal tax) {
        this.tax = Decimals.scaleAndCheck("tax", tax, false, 10, 2);
        return (FlightPrice) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightPriceBase && eq((FlightPriceBase)that);
    }

    private boolean eq(@NotNull FlightPriceBase that) { return equal(price, that.price) && equal(tax, that.tax); }

    public int hashCode() { return Predefined.hashCodeAll(price, tax); }

    /** Attempt to construct a FlightPrice instance from an InputStream. */
    @NotNull public static FlightPrice fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightPrice.class); }

    /** Attempt to construct a FlightPrice instance from a String. */
    @NotNull public static FlightPrice fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightPrice.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 5404398193512288375L;

}
