package tekgenesis.test;

import tekgenesis.common.exception.ApplicationException;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;

/** 
 * Generated exception class for Enum: EnumTestException.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
public final class EnumTestApplicationException
    extends ApplicationException
{

    //~ Constructors .............................................................................................................

    /** Constructor for EnumTestApplicationException */
    public EnumTestApplicationException(@NotNull final EnumTestException ex, @NotNull Object... args) { super(ex,args); }

    //~ Methods ..................................................................................................................

    /** Returns the Exception as an Enumeration */
    @NotNull public EnumTestException getException() { return Predefined.cast(getEnumeration()); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 3001074200473610804L;

}
