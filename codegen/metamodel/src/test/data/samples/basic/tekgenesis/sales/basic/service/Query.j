package tekgenesis.sales.basic.service;

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
 * Generated base class for type: Query.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"q", "result"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class Query
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String q = "";
    @JsonProperty @NotNull private List<Product> result = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Q. */
    @JsonProperty @NotNull public String getQ() { return q; }

    /** Returns the Result. */
    @JsonProperty @NotNull public List<Product> getResult() { return result; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Q. */
    @NotNull public Query setQ(@NotNull String q) {
        this.q = Strings.truncate(q, 255);
        return this;
    }

    /** Sets the value of the Result. */
    @NotNull public Query setResult(@NotNull List<Product> result) {
        this.result = result;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof Query && eq((Query)that);
    }

    private boolean eq(@NotNull Query that) { return equal(q, that.q) && equal(result, that.result); }

    public int hashCode() { return Predefined.hashCodeAll(q, result); }

    /** Attempt to construct a Query instance from an InputStream. */
    @NotNull public static Query fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Query.class); }

    /** Attempt to construct a Query instance from a String. */
    @NotNull public static Query fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Query.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -6723860256720085352L;

}
