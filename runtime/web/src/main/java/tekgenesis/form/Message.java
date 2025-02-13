
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
 * A message to be used in forms.
 */
@SuppressWarnings("UnusedReturnValue")
public interface Message {

    //~ Methods ......................................................................................................................................

    /** Mutates message to an info one. */
    @NotNull Message info();

    /** Mutates message to an inline one. */
    @NotNull Message inline();

    /** Mutates message to a success one. */
    @NotNull Message success();

    /** Mutates message to a warning one. */
    @NotNull Message warning();
}
