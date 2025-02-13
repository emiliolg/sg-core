package tekgenesis.showcase.g;

import tekgenesis.showcase.Coordinate;
import java.io.InputStream;
import tekgenesis.common.core.Integers;
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
 * Generated base class for type: Coordinate.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"lat", "lng"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class CoordinateBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty private int lat = 0;
    @JsonProperty private int lng = 0;

    //~ Getters ..................................................................................................................

    /** Returns the Lat. */
    @JsonProperty public int getLat() { return lat; }

    /** Returns the Lng. */
    @JsonProperty public int getLng() { return lng; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Lat. */
    @NotNull public Coordinate setLat(int lat) {
        this.lat = Integers.checkSignedLength("lat", lat, true, 9);
        return (Coordinate) this;
    }

    /** Sets the value of the Lng. */
    @NotNull public Coordinate setLng(int lng) {
        this.lng = Integers.checkSignedLength("lng", lng, true, 9);
        return (Coordinate) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof CoordinateBase && eq((CoordinateBase)that);
    }

    private boolean eq(@NotNull CoordinateBase that) { return equal(lat, that.lat) && equal(lng, that.lng); }

    public int hashCode() { return Predefined.hashCodeAll(lat, lng); }

    /** Attempt to construct a Coordinate instance from an InputStream. */
    @NotNull public static Coordinate fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, Coordinate.class); }

    /** Attempt to construct a Coordinate instance from a String. */
    @NotNull public static Coordinate fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, Coordinate.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7191868585939197362L;

}
