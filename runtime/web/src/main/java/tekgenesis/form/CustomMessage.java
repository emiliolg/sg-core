
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
 * A message to be used on Actions.
 */
public interface CustomMessage {

    //~ Methods ......................................................................................................................................

    /** Mutates custom message to an error one. */
    @NotNull CustomMessage error();

    /** Forces the message to auto close. */
    @NotNull CustomMessage notSticky();

    /** Sets the message to auto close or not. By default, message will auto close. */
    @NotNull CustomMessage sticky();

    /** Mutates custom message to a success one. */
    @NotNull CustomMessage success();

    /** Mutates custom message to a warning one. */
    @NotNull CustomMessage warning();
}
