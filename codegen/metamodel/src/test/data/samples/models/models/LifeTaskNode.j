package models;

import tekgenesis.task.LifecycleTask;
import org.jetbrains.annotations.NotNull;
import tekgenesis.task.Status;

/** User class for Task: LifeTaskNode */
public class LifeTaskNode
    extends LifeTaskNodeBase
{

    //~ Constructors .............................................................................................................

    private LifeTaskNode(@NotNull LifecycleTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public Status onShutdown() { throw new IllegalStateException("to be implemented"); }

    @Override @NotNull public Status onStartup() { throw new IllegalStateException("to be implemented"); }

}
