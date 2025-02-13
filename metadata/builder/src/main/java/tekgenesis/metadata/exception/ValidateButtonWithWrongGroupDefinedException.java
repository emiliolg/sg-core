
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
 * Exception to throw when a validate button has no group defined.
 */
public class ValidateButtonWithWrongGroupDefinedException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a ValidateButtonWithWrongGroupDefinedException. */
    public ValidateButtonWithWrongGroupDefinedException(@NotNull final String widget, @NotNull final String groupName) {
        // noinspection DuplicateStringLiteralInspection
        super("Group '" + groupName + "' used by button '" + widget + "' is not defined or it's not a group", widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2672446832640190745L;
}
