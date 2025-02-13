package tekgenesis.test;

import tekgenesis.common.util.Conversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.codegen.JavaGenerationTest.State;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.Predefined.equal;

/** An Address class */
public class Address
{

    //~ Fields ...................................................................................................................

    @NotNull private final String street;
    private final int number;
    @Nullable private final State state;
    @NotNull private final String country;

    //~ Constructors .............................................................................................................

    /** Creates a Address. */
    public Address(@NotNull String street, int number, @Nullable State state, @NotNull String country) {
        this.street = street;
        this.number = number;
        this.state = state;
        this.country = country;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Street. */
    @NotNull public String getStreet() { return street; }

    /** Returns the Number. */
    public int getNumber() { return number; }

    /** Returns the State. */
    @Nullable public State getState() { return state; }

    /** Returns the Country. */
    @NotNull public String getCountry() { return country; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof Address && eq((Address)that);
    }

    private boolean eq(@NotNull Address that) {
        return equal(street, that.street) && equal(number, that.number) && equal(state, that.state) && equal(country, that.country);
    }

    public int hashCode() {
        return Predefined.hashCodeAll(street, number, state, country);
    }

    public String toString() {
        return "(" + street + "," + number + "," + state + "," + country + ")";
    }

    @NotNull public static final Address valueOf(@NotNull String str) {
        final String[] args = Strings.splitToArray(str, "(", ",", ")", 4);
        return new Address(args[0], Conversions.toInt(args[1]), State.valueOf(args[2]), args[3]);
    }

}
