package tekgenesis.sales.basic.service;

import tekgenesis.common.exception.ApplicationException;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.Predefined;

/** 
 * Generated exception class for Enum: HandlerError.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
public final class HandlerErrorApplicationException
    extends ApplicationException
{

    //~ Constructors .............................................................................................................

    /** Constructor for HandlerErrorApplicationException */
    public HandlerErrorApplicationException(@NotNull final HandlerError ex, @NotNull Object... args) { super(ex,args); }

    //~ Methods ..................................................................................................................

    /** Returns the Exception as an Enumeration */
    @NotNull public HandlerError getException() { return Predefined.cast(getEnumeration()); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 8330919974198508813L;

}
