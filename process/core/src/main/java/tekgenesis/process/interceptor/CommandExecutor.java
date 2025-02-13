
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.interceptor;

/**
 * A command executor for internal usage/
 */
@SuppressWarnings("WeakerAccess")
public interface CommandExecutor {

    //~ Methods ......................................................................................................................................

    /** Executes a Command. */
    <T> T execute(Command<T> command);
}
