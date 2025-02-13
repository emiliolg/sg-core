
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
 * A chain of CommandExecutors.
 */
abstract class CommandInterceptor implements CommandExecutor {

    //~ Instance Fields ..............................................................................................................................

    // todo use something safer...
    CommandExecutor next = null;

    //~ Methods ......................................................................................................................................

    /** Sets the next Executor to be invoked. */
    public void setNext(CommandExecutor next) {
        this.next = next;
    }
}
