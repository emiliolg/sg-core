package models;

import org.jetbrains.annotations.NotNull;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/** User class for Task: MyTask */
public class MyTask
    extends MyTaskBase
{

    //~ Constructors .............................................................................................................

    private MyTask(@NotNull ScheduledTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public Status run() { throw new IllegalStateException("to be implemented"); }

}
