package models;

import org.jetbrains.annotations.NotNull;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/** User class for Task: RunnableTask */
public class RunnableTask
    extends RunnableTaskBase
{

    //~ Constructors .............................................................................................................

    private RunnableTask(@NotNull ScheduledTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public Status run() { throw new IllegalStateException("to be implemented"); }

}
