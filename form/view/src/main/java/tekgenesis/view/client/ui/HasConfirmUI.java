
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.Nullable;

/**
 * A Widget that displays a confirmation dialog before performing the action, used to set the
 * confirmation text for the expression 'confirm'
 */
public interface HasConfirmUI {

    //~ Methods ......................................................................................................................................

    /** Set confirmation message text. */
    void setConfirmationText(@Nullable final String confirmationText);
}
