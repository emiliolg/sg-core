
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
 * A command context.
 */
public class CommandContext {

    //~ Instance Fields ..............................................................................................................................

    private final Command<?> command;

    //~ Constructors .................................................................................................................................

    /** Create a commandContext. */
    public CommandContext(Command<?> command) {
        this.command = command;
    }

    //~ Methods ......................................................................................................................................

    /** Get the command being executed. */
    public Command<?> getCommand() {
        return command;
    }
}
