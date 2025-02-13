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
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/**
 * Generated base class for type: CExtendedType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"cId"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class CExtendedTypeBase
    extends AParentType
    implements Serializable
{

    //~ Fields ...................................................................................................................

    @JsonProperty private int cId;

    //~ Constructors .............................................................................................................

    /** Constructor for CExtendedType */
    @JsonCreator public CExtendedTypeBase(@JsonProperty("id") int id, @JsonProperty("cId") int cId) {
        super(id);
        this.cId = cId;
    }

    //~ Getters ..................................................................................................................

    /** Returns the C Id. */
    @JsonProperty public int getCId() { return cId; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof CExtendedTypeBase && equal(cId, ((CExtendedTypeBase) that).cId);
    }

    public int hashCode() { return Predefined.hashCodeAll(cId); }

    /** Attempt to construct a CExtendedType instance from an InputStream. */
    @NotNull public static CExtendedType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, CExtendedType.class); }

    /** Attempt to construct a CExtendedType instance from a String. */
    @NotNull public static CExtendedType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, CExtendedType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 3216057571371187830L;

}