package models;

import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.task.ProcessorTaskInstance;
import tekgenesis.task.ScheduledTask;
import tekgenesis.metadata.task.TransactionMode;

/** 
 * Generated base class for task: ProcesorTask.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ProcesorTaskBase<T>
    extends ProcessorTaskInstance<T>
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    protected ProcesorTaskBase(@NotNull ScheduledTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public String getCronExpression() { return "0/1"; }

    @Override @NotNull public String getScheduleAfter() { return ""; }

    @Override @NotNull public TransactionMode getTransactionMode() { return TransactionMode.EACH; }

    @Override public int getBatchSize() { return 10; }

    @Override @NotNull public String getExclusionGroup() { return ""; }

    @Override public int getPurgePolicy() { return 15; }

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ProcesorTask.class);

}
