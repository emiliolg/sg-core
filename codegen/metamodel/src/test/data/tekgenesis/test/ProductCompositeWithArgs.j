package tekgenesis.test;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Generated base class for type: ProductCompositeWithArgs.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"left", "right"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class ProductCompositeWithArgs
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private Product2 left;
    @JsonProperty @NotNull private Product2 right;

    //~ Constructors .............................................................................................................

    /** Constructor for ProductCompositeWithArgs */
    @JsonCreator public ProductCompositeWithArgs(@JsonProperty("productId") @NotNull String productId, @JsonProperty("productId") @NotNull String productId) {
        left = new Product2(productId);
        right = new Product2(productId);
    }

    //~ Getters ..................................................................................................................

    /** Returns the Left. */
    @JsonProperty @NotNull public Product2 getLeft() { return left; }

    /** Returns the Right. */
    @JsonProperty @NotNull public Product2 getRight() { return right; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Left. */
    @NotNull public ProductCompositeWithArgs setLeft(@NotNull Product2 left) {
        this.left = left;
        return this;
    }

    /** Sets the value of the Right. */
    @NotNull public ProductCompositeWithArgs setRight(@NotNull Product2 right) {
        this.right = right;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof ProductCompositeWithArgs && eq((ProductCompositeWithArgs)that);
    }

    private boolean eq(@NotNull ProductCompositeWithArgs that) { return equal(left, that.left) && equal(right, that.right); }

    public int hashCode() { return Predefined.hashCodeAll(left, right); }

    /** Attempt to construct a ProductCompositeWithArgs instance from an InputStream. */
    @NotNull public static ProductCompositeWithArgs fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, ProductCompositeWithArgs.class);
    }

    /** Attempt to construct a ProductCompositeWithArgs instance from a String. */
    @NotNull public static ProductCompositeWithArgs fromJson(@NotNull final String value) {
        return JsonMapping.fromJson(value, ProductCompositeWithArgs.class);
    }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 6113863870952401605L;

}
