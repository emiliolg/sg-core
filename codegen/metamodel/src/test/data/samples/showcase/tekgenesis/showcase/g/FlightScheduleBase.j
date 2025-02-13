package tekgenesis.showcase.g;

import tekgenesis.showcase.FlightSchedule;
import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: FlightSchedule.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"fromSchedule", "toSchedule"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightScheduleBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String fromSchedule = "";
    @JsonProperty @Nullable private String toSchedule = null;

    //~ Getters ..................................................................................................................

    /** Returns the From Schedule. */
    @JsonProperty @NotNull public String getFromSchedule() { return fromSchedule; }

    /** Returns the To Schedule. */
    @JsonProperty @Nullable public String getToSchedule() { return toSchedule; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the From Schedule. */
    @NotNull public FlightSchedule setFromSchedule(@NotNull String fromSchedule) {
        this.fromSchedule = Strings.truncate(fromSchedule, 255);
        return (FlightSchedule) this;
    }

    /** Sets the value of the To Schedule. */
    @NotNull public FlightSchedule setToSchedule(@Nullable String toSchedule) {
        this.toSchedule = Strings.truncate(toSchedule, 255);
        return (FlightSchedule) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightScheduleBase && eq((FlightScheduleBase)that);
    }

    private boolean eq(@NotNull FlightScheduleBase that) {
        return equal(fromSchedule, that.fromSchedule) && equal(toSchedule, that.toSchedule);
    }

    public int hashCode() { return Predefined.hashCodeAll(fromSchedule, toSchedule); }

    /** Attempt to construct a FlightSchedule instance from an InputStream. */
    @NotNull public static FlightSchedule fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightSchedule.class); }

    /** Attempt to construct a FlightSchedule instance from a String. */
    @NotNull public static FlightSchedule fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightSchedule.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 555351073011951463L;

}
