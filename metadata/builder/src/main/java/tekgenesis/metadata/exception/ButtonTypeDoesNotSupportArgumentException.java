
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

import tekgenesis.common.core.Strings;
import tekgenesis.metadata.form.widget.ButtonType;

/**
 * Exception thrown when Button type does not support a table for argument.
 */
public class ButtonTypeDoesNotSupportArgumentException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a ButtonTypeDoesNotSupportArgumentException. */
    public ButtonTypeDoesNotSupportArgumentException(@NotNull String widget, ButtonType buttonType, String tableName) {
        super(Strings.capitalizeFirst(buttonType.name().toLowerCase()) + " button '" + widget + "' does not support argument '" + tableName + "'",
            widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2501354172696000935L;
}
