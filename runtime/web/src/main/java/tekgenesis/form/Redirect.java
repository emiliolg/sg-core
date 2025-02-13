
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

/**
 * Form navigation action.
 */
public interface Redirect extends Action {

    //~ Methods ......................................................................................................................................

    /** Specify if the redirect should be inline in the form box. */
    @NotNull Redirect inline();
}
