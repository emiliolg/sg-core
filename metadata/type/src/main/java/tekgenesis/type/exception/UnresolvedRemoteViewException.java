
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
 * Exception thrown when view entity is not found.
 */
public class UnresolvedRemoteViewException extends UnresolvedTypeReferenceException {

    //~ Constructors .................................................................................................................................

    UnresolvedRemoteViewException() {}

    /** Creates exception. */
    public UnresolvedRemoteViewException(String reference, String referenced) {
        super(reference, referenced);
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return unresolvedView(reference, referenced);
    }

    //~ Methods ......................................................................................................................................

    /** Message for an Unresolved Reference. */
    @SuppressWarnings("WeakerAccess")
    public static String unresolvedView(@NotNull String reference, @NotNull String referenced) {
        return "Could not find remote view for entity '" + reference + "'" + (referenced.isEmpty() ? "" : " referenced from  '" + referenced + "'");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -341527236110407817L;
}
