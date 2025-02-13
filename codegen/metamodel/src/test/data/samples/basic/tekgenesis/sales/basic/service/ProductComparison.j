package tekgenesis.sales.basic.service;

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
 * Generated base class for type: ProductComparison.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"left", "rigth"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class ProductComparison
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private Product left = new Product();
    @JsonProperty @NotNull private Product rigth = new Product();

    //~ Getters ..................................................................................................................

    /** Returns the Left. */
    @JsonProperty @NotNull public Product getLeft() { return left; }

    /** Returns the Rigth. */
    @JsonProperty @NotNull public Product getRigth() { return rigth; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Left. */
    @NotNull public ProductComparison setLeft(@NotNull Product left) {
        this.left = left;
        return this;
    }

    /** Sets the value of the Rigth. */
    @NotNull public ProductComparison setRigth(@NotNull Product rigth) {
        this.rigth = rigth;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof ProductComparison && eq((ProductComparison)that);
    }

    private boolean eq(@NotNull ProductComparison that) { return equal(left, that.left) && equal(rigth, that.rigth); }

    public int hashCode() { return Predefined.hashCodeAll(left, rigth); }

    /** Attempt to construct a ProductComparison instance from an InputStream. */
    @NotNull public static ProductComparison fromJson(@NotNull final InputStream stream) {
        return JsonMapping.fromJson(stream, ProductComparison.class);
    }

    /** Attempt to construct a ProductComparison instance from a String. */
    @NotNull public static ProductComparison fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, ProductComparison.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 1158257176035795440L;

}
