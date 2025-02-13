
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

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.widget.ButtonType;

/**
 * Exception to throw when an add_row or remove_row button table reference is ambiguous.
 */
public class TableButtonWithAmbiguousTableException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a TableButtonWithAmbiguousTableException. */
    public TableButtonWithAmbiguousTableException(@NotNull final String widget, @NotNull final ButtonType buttonType, Seq<String> tables) {
        super("Button " + buttonType.name() + " '" + widget + "'" + " table reference is ambiguous. Should be one of " + tables.mkString(", "),
            widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2672446832640190745L;
}
