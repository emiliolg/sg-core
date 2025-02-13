package tekgenesis.test;

import java.util.ArrayList;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: ProductList.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"id", "products"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class ProductList
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String id = "";
    @JsonProperty @NotNull private List<Product> products = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Id. */
    @JsonProperty @NotNull public String getId() { return id; }

    /** Returns the Products. */
    @JsonProperty @NotNull public List<Product> getProducts() { return products; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Id. */
    @NotNull public ProductList setId(@NotNull String id) {
        this.id = Strings.truncate(id, 255);
        return this;
    }

    /** Sets the value of the Products. */
    @NotNull public ProductList setProducts(@NotNull List<Product> products) {
        this.products = products;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof ProductList && eq((ProductList)that);
    }

    private boolean eq(@NotNull ProductList that) { return equal(id, that.id) && equal(products, that.products); }

    public int hashCode() { return Predefined.hashCodeAll(id, products); }

    /** Attempt to construct a ProductList instance from an InputStream. */
    @NotNull public static ProductList fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, ProductList.class); }

    /** Attempt to construct a ProductList instance from a String. */
    @NotNull public static ProductList fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, ProductList.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -7990949250869964169L;

}
