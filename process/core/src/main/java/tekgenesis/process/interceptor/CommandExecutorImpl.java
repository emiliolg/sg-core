
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.interceptor;

import tekgenesis.process.engine.context.Context;

/**
 * An implementation of a CommandInterceptor.
 */
public class CommandExecutorImpl extends CommandInterceptor {

    //~ Methods ......................................................................................................................................

    public <T> T execute(Command<T> command) {
        return command.execute(Context.getCommandContext());
    }
}
