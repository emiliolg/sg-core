package models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.task.ScheduledTask;
import tekgenesis.common.collections.Seq;
import tekgenesis.task.Status;

/** User class for Task: ProcesorTask */
public class ProcesorTask
    extends ProcesorTaskBase<Object>
{

    //~ Constructors .............................................................................................................

    private ProcesorTask(@NotNull ScheduledTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @Nullable public Seq<Object> enumerate() { throw new IllegalStateException("to be implemented"); }

    @Override @Nullable public Status process(@Nullable Object o) { throw new IllegalStateException("to be implemented"); }

}
