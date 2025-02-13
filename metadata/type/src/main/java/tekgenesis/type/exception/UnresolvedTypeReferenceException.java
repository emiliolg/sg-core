
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Exception to throw when a reference can not be resolved.
 */
@SuppressWarnings({ "NonFinalFieldOfException", "FieldMayBeFinal" })
public class UnresolvedTypeReferenceException extends TypeException {

    //~ Instance Fields ..............................................................................................................................

    String reference;
    String referenced;

    //~ Constructors .................................................................................................................................

    /** constructor.* */
    UnresolvedTypeReferenceException() {
        reference  = null;
        referenced = null;
    }

    /** Creates an UnresolvedReferenceException for a reference type. */
    public UnresolvedTypeReferenceException(@NotNull String type) {
        this(type, "");
    }

    /** Creates an UnresolvedReferenceException for a reference type with reference point. */
    @SuppressWarnings("WeakerAccess")
    public UnresolvedTypeReferenceException(@NotNull String reference, @NotNull String referenced) {
        this.reference  = reference;
        this.referenced = referenced;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return unresolvedReference(reference, referenced);
    }

    /** Return unresolved reference. */
    public String getReference() {
        return reference;
    }

    //~ Methods ......................................................................................................................................

    /** Message for an Unresolved Reference. */
    @SuppressWarnings("WeakerAccess")
    public static String unresolvedReference(@NotNull String reference, @NotNull String referenced) {
        return "Could not find model '" + reference + "'" + (referenced.isEmpty() ? "" : " referenced from '" + referenced + "'");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5207551070849182720L;
}  // end class UnresolvedTypeReferenceException
