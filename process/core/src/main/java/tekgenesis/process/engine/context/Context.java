
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.engine.context;

import tekgenesis.common.collections.Stack;
import tekgenesis.process.interceptor.CommandContext;

/**
 * An Engine Context.
 */
public class Context {

    //~ Constructors .................................................................................................................................

    private Context() {}

    //~ Methods ......................................................................................................................................

    /** Get a thread local Command Context. */
    public static CommandContext getCommandContext() {
        final Stack<CommandContext> s = commandContextThreadLocal.get();
        return s.isEmpty() ? null : s.peek();
    }

    //~ Static Fields ................................................................................................................................

    private static final ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new StackThreadLocal();

    //~ Inner Classes ................................................................................................................................

    private static class StackThreadLocal extends ThreadLocal<Stack<CommandContext>> {
        @Override public Stack<CommandContext> get() {
            Stack<CommandContext> stack = super.get();
            if (stack == null) {
                stack = Stack.createStack();
                set(stack);
            }
            return stack;
        }
    }
}
