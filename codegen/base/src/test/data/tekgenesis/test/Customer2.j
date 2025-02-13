package tekgenesis.test;

import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

public class Customer2
    extends CustomerBase
    implements Serializable
{

    //~ Fields ...................................................................................................................

    private final int code;
    @NotNull private String name;
    @NotNull private final CustomerType type;
    private boolean active;

    //~ Constructors .............................................................................................................

    protected Customer2() { }

    public Customer2(int code) { this.code = code; }

    //~ Getters ..................................................................................................................

    /** Returns the Type. */
    @NotNull public CustomerType getType() { return type; }

    /** Returns true if it is Active. */
    public boolean isActive() { return active; }

    //~ Setters ..................................................................................................................

    /** Sets the value of the Active. */
    public void setActive(boolean active) { this.active = active; }

    //~ Methods ..................................................................................................................

    public void setCode() { this.code = code; }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -5215917533270034878L;

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum CustomerType
    {
        CUSTOM,
        STANDARD,
        VIP;

    }
}
