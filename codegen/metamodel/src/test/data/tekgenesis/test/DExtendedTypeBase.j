package tekgenesis.test;

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
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/**
 * Generated base class for type: DExtendedType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"dId", "dni"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class DExtendedTypeBase
    extends AParentType
    implements Serializable
{

    //~ Fields ...................................................................................................................

    @JsonProperty private int dId;
    @JsonProperty @Nullable private Integer dni;

    //~ Constructors .............................................................................................................

    /** Constructor for DExtendedType */
    @JsonCreator public DExtendedTypeBase(@JsonProperty("id") int id, @JsonProperty("dId") int dId, @JsonProperty("dni") @Nullable Integer dni) {
        super(id);
        this.dId = dId;
        this.dni = dni;
    }

    //~ Getters ..................................................................................................................

    /** Returns the D Id. */
    @JsonProperty public int getDId() { return dId; }

    /** Returns the Dni. */
    @JsonProperty @Nullable public Integer getDni() { return dni; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof DExtendedTypeBase && eq((DExtendedTypeBase)that);
    }

    private boolean eq(@NotNull DExtendedTypeBase that) { return equal(dId, that.dId) && equal(dni, that.dni); }

    public int hashCode() { return Predefined.hashCodeAll(dId, dni); }

    /** Attempt to construct a DExtendedType instance from an InputStream. */
    @NotNull public static DExtendedType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, DExtendedType.class); }

    /** Attempt to construct a DExtendedType instance from a String. */
    @NotNull public static DExtendedType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, DExtendedType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -8686969574724330256L;

}