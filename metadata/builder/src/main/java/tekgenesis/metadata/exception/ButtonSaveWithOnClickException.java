
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
 * Exception to throw when an add_row or remove_row button table reference is ambiguous.
 */
public class ButtonSaveWithOnClickException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a TableButtonWithAmbiguousTableException. */
    public ButtonSaveWithOnClickException(@NotNull final String widget) {
        super(
            "Save button '" + widget + "' cannot have 'on_click' method. " +
            "Override create or update or use button type 'validate' with 'on_click'.",
            widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2672446832640190745L;
}
