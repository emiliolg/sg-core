package tekgenesis.sales.basic;

import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.task.RefreshRemoteViewTaskBase;
import tekgenesis.task.ScheduledTask;

/** 
 * Generated RefreshViewTask class for remote view : CategoryCompositeView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings("WeakerAccess")
public class TekgenesisSalesBasicRefreshRemoteViewTask
    extends RefreshRemoteViewTaskBase
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    private TekgenesisSalesBasicRefreshRemoteViewTask(@NotNull ScheduledTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override @Nullable public String getDomain() { return "tekgenesis.sales.basic"; }

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(CategoryCompositeView.class);

}
