package tekgenesis.test;

import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;
import java.io.Serializable;
import static tekgenesis.common.Predefined.equal;

public class Customer
    implements Serializable
{

    //~ Fields ...................................................................................................................

    private int code;

    @NotNull private String name;

    @NotNull private final CustomerType type;

    private boolean active;

    //~ Constructors .............................................................................................................

    protected Customer() { }

    public Customer(int code) { this.code = code; }

    //~ Getters ..................................................................................................................

    /** Returns the Name. */
    @NotNull public String getName() { return name; }

    /** Returns the Type. */
    @NotNull public CustomerType getType() { return type; }

    /** Returns true if it is Active. */
    public boolean isActive() { return active; }

    //~ Setters ..................................................................................................................

    /** Sets the value of the Name. */
    public void setName(@NotNull String name) { this.name = name; }

    /** Sets the value of the Active. */
    public void setActive(boolean active) { this.active = active; }

    //~ Methods ..................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof Customer && eq((Customer)that);
    }

    private boolean eq(@NotNull Customer that) { return equal(code, that.code) && equal(name, that.name); }

    public int hashCode() { return Predefined.hashCodeAll(code, name); }

    public void setCode(@NotNull String code) { this.code = code; }

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum CustomerType
    {
        CUSTOM,
        STANDARD,
        VIP;

    }
}
