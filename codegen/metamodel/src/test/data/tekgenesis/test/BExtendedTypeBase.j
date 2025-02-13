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

/**
 * Generated base class for type: BExtendedType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class BExtendedTypeBase
    extends AParentType
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for BExtendedType */
    @JsonCreator public BExtendedTypeBase(@JsonProperty("id") int id) { super(id); }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) { return this == that || that instanceof BExtendedTypeBase; }

    public int hashCode() { return Predefined.hashCodeAll(null); }

    /** Attempt to construct a BExtendedType instance from an InputStream. */
    @NotNull public static BExtendedType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, BExtendedType.class); }

    /** Attempt to construct a BExtendedType instance from a String. */
    @NotNull public static BExtendedType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, BExtendedType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7072655180380407960L;

}