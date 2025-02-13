
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.task;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.type.Modifier;

/**
 * Task Builder.
 */
public final class TaskBuilder extends ModelBuilder.Default<Task, TaskBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private int batchSize;

    @NotNull private String cronExpression = "";

    @Nullable private String exclusionGroup;
    private String           pattern;
    @Nullable private String scheduleAfter;

    private TransactionMode transactionMode = TransactionMode.ALL;
    private TaskType        type;

    //~ Constructors .................................................................................................................................

    private TaskBuilder(@NotNull String src, @NotNull String pkg, @NotNull String name, TaskType type) {
        super(src, pkg, name);
        exclusionGroup  = null;
        this.type       = type;
        pattern         = null;
        transactionMode = TransactionMode.ALL;
        batchSize       = 1;
        scheduleAfter   = null;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Add exclusion group.
     *
     * @param  group  group
     */
    @SuppressWarnings("UnusedReturnValue")
    public TaskBuilder addExclusionGroup(@NotNull String group) {
        exclusionGroup = group;
        return this;
    }

    /** Mark task as Lifecycle Server Task.* */
    public TaskBuilder asClusterLifecycleTask() {
        type = TaskType.CLUSTER_LIFECYCLE;
        return this;
    }

    /** Mark task as Node Server Task.* */
    public TaskBuilder asNodeRunnableTask() {
        type = TaskType.NODE_RUNNABLE;
        return this;
    }

    @Override public Task build() {
        return new Task(sourceName,
            domain,
            id,
            type,
            label,
            cronExpression,
            exclusionGroup,
            transactionMode,
            batchSize,
            scheduleAfter,
            modifiers,
            pattern);
    }

    @NotNull @Override public List<BuilderError> check() {
        return Colls.emptyList();
    }

    /** Mark the transaction mode as EACH and specify the batch size. */
    public TaskBuilder commitEach(int i) {
        transactionMode = TransactionMode.EACH;
        batchSize       = i;
        return this;
    }

    /**
     * set cron expression.
     *
     * @param  cronExpression  cron expression
     */
    @SuppressWarnings({ "UnusedReturnValue", "ParameterHidesMemberVariable" })
    public TaskBuilder cronExpression(@NotNull String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    /** Set exclusion group. */
    public TaskBuilder exclusionGroup(@Nullable String name) {
        exclusionGroup = name;
        return this;
    }

    /** Set schedule after task fqn. */
    public TaskBuilder scheduleAfter(String fqn) {
        scheduleAfter  = fqn;
        cronExpression = "";
        return this;
    }

    @Override public TaskBuilder withModifiers(EnumSet<Modifier> mod) {
        modifiers.addAll(mod);
        return this;
    }

    /** Sets pattern for importer tasks. */
    public TaskBuilder withPattern(String s) {
        pattern = s;
        return this;
    }

    /** Sets transaction mode. */
    public TaskBuilder withTransactionMode(TransactionMode mode) {
        transactionMode = mode;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Creates a TaskBuilder. */
    public static TaskBuilder create(String sourceName, final String packageId, final String name, final TaskType type) {
        return new TaskBuilder(sourceName, packageId, name, type);
    }
}  // end class TaskBuilder
