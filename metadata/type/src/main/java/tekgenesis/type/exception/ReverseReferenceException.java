
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.exception;

/**
 * Exception to throw when a reference can not be resolved.
 */
@SuppressWarnings({ "NonFinalFieldOfException", "FieldMayBeFinal" })
public class ReverseReferenceException extends TypeException {

    //~ Instance Fields ..............................................................................................................................

    private boolean dup;

    private String referenced;

    //~ Constructors .................................................................................................................................

    /** Default contructor. */
    public ReverseReferenceException() {
        dup        = false;
        referenced = null;
    }

    /** Creates an ReverseReferenceException for a reference type. */
    public ReverseReferenceException(String referenced, boolean dup) {
        this.referenced = referenced;
        this.dup        = dup;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return message(referenced, dup);
    }

    //~ Methods ......................................................................................................................................

    /** Creates an exception for duplicate Reverse Reference. */
    public static ReverseReferenceException duplicateReference(String name) {
        return new ReverseReferenceException(name, true);
    }

    /** Message for an Unresolved Reference. */
    public static String message(String referenced, boolean dup) {
        return "Cannot resolve reverse reference for '" + referenced + "'. " + (dup ? "More than one" : "No") + " attribute with the required model.";
    }

    /** Creates an exception for an absent Reverse Reference. */
    public static ReverseReferenceException referenceNotFound(String name) {
        return new ReverseReferenceException(name, false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270591905599L;
}  // end class ReverseReferenceException
