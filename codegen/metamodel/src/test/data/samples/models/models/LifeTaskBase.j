package models;

import tekgenesis.task.LifecycleTask;
import tekgenesis.task.LifecycleTaskInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.metadata.task.TransactionMode;

/** 
 * Generated base class for task: LifeTask.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings("WeakerAccess")
public abstract class LifeTaskBase
    extends LifecycleTaskInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    protected LifeTaskBase(@NotNull LifecycleTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public TransactionMode getTransactionMode() { return TransactionMode.ALL; }

    @Override public int getBatchSize() { return 1; }

    @Override @NotNull public String getExclusionGroup() { return ""; }

    @Override public int getPurgePolicy() { return 15; }

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(LifeTask.class);

}
