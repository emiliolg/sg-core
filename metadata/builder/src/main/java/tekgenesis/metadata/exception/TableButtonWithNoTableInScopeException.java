
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

import tekgenesis.metadata.form.widget.ButtonType;

/**
 * Exception to throw when an add_row or remove_row button has no table in scope.
 */
public class TableButtonWithNoTableInScopeException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a TableButtonWithNoTableInScopeException. */
    public TableButtonWithNoTableInScopeException(@NotNull final String widget, @NotNull final ButtonType buttonType) {
        super("Adding button " + buttonType.name() + " '" + widget + "' but no table defined in scope.", widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8514488509573911472L;
}
