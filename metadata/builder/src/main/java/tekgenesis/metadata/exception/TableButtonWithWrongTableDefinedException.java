
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Exception to throw when an add_row or remove_row button has no table defined.
 */
public class TableButtonWithWrongTableDefinedException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a TableButtonWithAmbiguousTableException. */
    public TableButtonWithWrongTableDefinedException(@NotNull final String widget, @NotNull final String tableName) {
        // noinspection DuplicateStringLiteralInspection
        super("Table '" + tableName + "' used by button '" + widget + "' is not defined.", widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2672446832640190745L;
}
