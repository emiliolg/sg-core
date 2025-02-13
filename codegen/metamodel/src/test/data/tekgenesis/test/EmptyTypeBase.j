package tekgenesis.test;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/** 
 * Generated base class for type: EmptyType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class EmptyTypeBase
    implements Serializable, Struct
{

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) { return this == that || that instanceof EmptyTypeBase; }

    public int hashCode() { return Predefined.hashCodeAll(null); }

    /** Attempt to construct a EmptyType instance from an InputStream. */
    @NotNull public static EmptyType fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, EmptyType.class); }

    /** Attempt to construct a EmptyType instance from a String. */
    @NotNull public static EmptyType fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, EmptyType.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7953990520611565782L;

}
