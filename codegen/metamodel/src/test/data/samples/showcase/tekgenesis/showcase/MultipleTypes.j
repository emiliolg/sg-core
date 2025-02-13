package tekgenesis.showcase;

import java.util.ArrayList;
import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import java.io.InputStream;
import tekgenesis.common.core.Integers;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tekgenesis.common.json.JsonMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Reals;
import java.io.Serializable;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Struct;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static tekgenesis.common.Predefined.equal;

/** 
 * Generated base class for type: MultipleTypes.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
@JsonPropertyOrder({"decimals", "strings", "ints", "reals", "bools", "dates", "dateTimes", "entities"})
@JsonAutoDetect(getterVisibility=NONE,isGetterVisibility=NONE,setterVisibility=NONE,creatorVisibility=NONE,fieldVisibility=NONE)
public final class MultipleTypes
    implements Serializable, Struct
{

    //~ Fields ...................................................................................................................

    @JsonProperty @NotNull private List<BigDecimal> decimals = new ArrayList<>();
    @JsonProperty @NotNull private List<String> strings = new ArrayList<>();
    @JsonProperty @NotNull private List<Integer> ints = new ArrayList<>();
    @JsonProperty @NotNull private List<Double> reals = new ArrayList<>();
    @JsonProperty @NotNull private List<Boolean> bools = new ArrayList<>();
    @JsonProperty @NotNull private List<DateOnly> dates = new ArrayList<>();
    @JsonProperty @NotNull private List<DateTime> dateTimes = new ArrayList<>();
    @JsonProperty @NotNull private List<PersonType> entities = new ArrayList<>();

    //~ Getters ..................................................................................................................

    /** Returns the Decimals. */
    @JsonProperty @NotNull public List<BigDecimal> getDecimals() { return decimals; }

    /** Returns the Strings. */
    @JsonProperty @NotNull public List<String> getStrings() { return strings; }

    /** Returns the Ints. */
    @JsonProperty @NotNull public List<Integer> getInts() { return ints; }

    /** Returns the Reals. */
    @JsonProperty @NotNull public List<Double> getReals() { return reals; }

    /** Returns the Bools. */
    @JsonProperty @NotNull public List<Boolean> getBools() { return bools; }

    /** Returns the Dates. */
    @JsonProperty @NotNull public List<DateOnly> getDates() { return dates; }

    /** Returns the Date Times. */
    @JsonProperty @NotNull public List<DateTime> getDateTimes() { return dateTimes; }

    /** Returns the Entities. */
    @JsonProperty @NotNull public List<PersonType> getEntities() { return entities; }

    //~ Methods ..................................................................................................................

    /** Sets the value of the Decimals. */
    @NotNull public MultipleTypes setDecimals(@NotNull List<BigDecimal> decimals) {
        this.decimals = Decimals.scaleAndCheck("decimals", decimals, false, 10, 2);
        return this;
    }

    /** Sets the value of the Strings. */
    @NotNull public MultipleTypes setStrings(@NotNull List<String> strings) {
        this.strings = Strings.truncate(strings, 255);
        return this;
    }

    /** Sets the value of the Ints. */
    @NotNull public MultipleTypes setInts(@NotNull List<Integer> ints) {
        this.ints = Integers.checkSignedLength("ints", ints, false, 9);
        return this;
    }

    /** Sets the value of the Reals. */
    @NotNull public MultipleTypes setReals(@NotNull List<Double> reals) {
        this.reals = Reals.checkSigned("reals", reals, false);
        return this;
    }

    /** Sets the value of the Bools. */
    @NotNull public MultipleTypes setBools(@NotNull List<Boolean> bools) {
        this.bools = bools;
        return this;
    }

    /** Sets the value of the Dates. */
    @NotNull public MultipleTypes setDates(@NotNull List<DateOnly> dates) {
        this.dates = dates;
        return this;
    }

    /** Sets the value of the Date Times. */
    @NotNull public MultipleTypes setDateTimes(@NotNull List<DateTime> dateTimes) {
        this.dateTimes = dateTimes;
        return this;
    }

    /** Sets the value of the Entities. */
    @NotNull public MultipleTypes setEntities(@NotNull List<PersonType> entities) {
        this.entities = entities;
        return this;
    }

    public boolean equals(Object that) {
        return this == that || that instanceof MultipleTypes && eq((MultipleTypes)that);
    }

    private boolean eq(@NotNull MultipleTypes that) {
        return equal(decimals, that.decimals) && equal(strings, that.strings) && equal(ints, that.ints) && equal(reals, that.reals) && equal(bools, that.bools) && equal(dates, that.dates) && equal(dateTimes, that.dateTimes) && equal(entities, that.entities);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(decimals, strings, ints, reals, bools, dates, dateTimes, entities);
    }

    /** Attempt to construct a MultipleTypes instance from an InputStream. */
    @NotNull public static MultipleTypes fromJson(@NotNull final InputStream stream) { return JsonMapping.fromJson(stream, MultipleTypes.class); }

    /** Attempt to construct a MultipleTypes instance from a String. */
    @NotNull public static MultipleTypes fromJson(@NotNull final String value) { return JsonMapping.fromJson(value, MultipleTypes.class); }

    @Override @NotNull public String toString() { return toJson(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -3707840157520007136L;

}
