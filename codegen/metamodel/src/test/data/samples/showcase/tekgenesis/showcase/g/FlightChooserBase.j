package tekgenesis.showcase.g;

import java.util.ArrayList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.showcase.FlightChooser;
import tekgenesis.showcase.FlightOption;
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
 * Generated base class for type: FlightChooser.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"origin", "destiny", "from", "to", "options"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public class FlightChooserBase
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private String origin = "";
    @JsonProperty @NotNull private String destiny = "";
    @JsonProperty @NotNull private DateOnly from = DateOnly.EPOCH;
    @JsonProperty @NotNull private DateOnly to = DateOnly.EPOCH;
    @JsonProperty @NotNull private List<FlightOption> options = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Origin. */
    @JsonProperty @NotNull public String getOrigin() { return origin; }

    /** Returns the Destiny. */
    @JsonProperty @NotNull public String getDestiny() { return destiny; }

    /** Returns the From. */
    @JsonProperty @NotNull public DateOnly getFrom() { return from; }

    /** Returns the To. */
    @JsonProperty @NotNull public DateOnly getTo() { return to; }

    /** Returns the Options. */
    @JsonProperty @NotNull public List<FlightOption> getOptions() { return options; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Origin. */
    @NotNull public FlightChooser setOrigin(@NotNull String origin) {
        this.origin = Strings.truncate(origin, 255);
        return (FlightChooser) this;
    }

    /** Sets the value of the Destiny. */
    @NotNull public FlightChooser setDestiny(@NotNull String destiny) {
        this.destiny = Strings.truncate(destiny, 255);
        return (FlightChooser) this;
    }

    /** Sets the value of the From. */
    @NotNull public FlightChooser setFrom(@NotNull DateOnly from) {
        this.from = from;
        return (FlightChooser) this;
    }

    /** Sets the value of the To. */
    @NotNull public FlightChooser setTo(@NotNull DateOnly to) {
        this.to = to;
        return (FlightChooser) this;
    }

    /** Sets the value of the Options. */
    @NotNull public FlightChooser setOptions(@NotNull List<FlightOption> options) {
        this.options = options;
        return (FlightChooser) this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof FlightChooserBase && eq((FlightChooserBase)that);
    }

    private boolean eq(@NotNull FlightChooserBase that) {
        return equal(origin, that.origin) && equal(destiny, that.destiny) && equal(from, that.from) && equal(to, that.to) && equal(options, that.options);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(origin, destiny, from, to, options);
    }

    /** Attempt to construct a FlightChooser instance from an InputStream. */
    @NotNull public static FlightChooser fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, FlightChooser.class); }

    /** Attempt to construct a FlightChooser instance from a String. */
    @NotNull public static FlightChooser fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, FlightChooser.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 2953773147431691887L;

}
