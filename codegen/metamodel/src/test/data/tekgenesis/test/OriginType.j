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
 * Generated base class for type: OriginType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"id"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class OriginType
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty private int id;

    //~ Constructors .............................................................................................................

    /** Constructor for OriginType */
    @JsonCreator public OriginType(@JsonProperty("id") int id) { this.id = id; }

    //~ Getters ..................................................................................................................

    /** Returns the Id. */
    @JsonProperty public int getId() { return id; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof OriginType && equal(id, ((OriginType) that).id);
    }

    public int hashCode() { return Predefined.hashCodeAll(id); }

    /** Attempt to construct a OriginType instance from an InputStream. */
    @NotNull public static OriginType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, OriginType.class); }

    /** Attempt to construct a OriginType instance from a String. */
    @NotNull public static OriginType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, OriginType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -8482073472578945143L;

}
