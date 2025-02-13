
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * Operation not allowed.
 */
@SuppressWarnings("WeakerAccess")
public class OperationNotAllowedException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** default constructor. */
    public OperationNotAllowedException(@NotNull String msgInfo) {
        super("The operation is not allowed. (" + msgInfo + ")");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2417371898406813971L;
}
