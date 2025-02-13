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
 * Generated base class for type: BaseStruct.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"productId", "model"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class BaseStructBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String productId = "";
    @JsonProperty @NotNull private String model = "";

    //~ Getters ..................................................................................................................

    /** Returns the Product Id. */
    @JsonProperty @NotNull public String getProductId() { return productId; }

    /** Returns the Model. */
    @JsonProperty @NotNull public String getModel() { return model; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Product Id. */
    @NotNull public BaseStruct setProductId(@NotNull String productId) {
        this.productId = Strings.truncate(productId, 8);
        return (BaseStruct) this;
    }

    /** Sets the value of the Model. */
    @NotNull public BaseStruct setModel(@NotNull String model) {
        this.model = Strings.truncate(model, 30);
        return (BaseStruct) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof BaseStructBase && eq((BaseStructBase)that);
    }

    private boolean eq(@NotNull BaseStructBase that) {
        return equal(productId, that.productId) && equal(model, that.model);
    }

    public int hashCode() { return Predefined.hashCodeAll(productId, model); }

    /** Attempt to construct a BaseStruct instance from an InputStream. */
    @NotNull public static BaseStruct fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, BaseStruct.class); }

    /** Attempt to construct a BaseStruct instance from a String. */
    @NotNull public static BaseStruct fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, BaseStruct.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -2611271333511974947L;

}
