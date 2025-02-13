
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;

import org.junit.Test;

import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.task.TaskBaseCodeGenerator;
import tekgenesis.codegen.task.TaskCodeGenerator;
import tekgenesis.metadata.task.Task;
import tekgenesis.metadata.task.TaskBuilder;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.repository.ModelRepository;

/**
 * Task Generation test;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class TaskGenTest {

    //~ Instance Fields ..............................................................................................................................

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    private final ModelRepository rep = new ModelRepository();

    //~ Methods ......................................................................................................................................

    @Test public void lifecycleClientTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "LifecycleClientTask", TaskType.NODE_LIFE_CYCLE);
        final Task        t       = builder.label("Task").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void lifecycleNonTransactional() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "LifecycleDepsTxTask", TaskType.NODE_LIFE_CYCLE);
        final Task        t       = builder.label("Task").withTransactionMode(TransactionMode.NONE).build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void lifecycleServerTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "LifecycleServerTask", TaskType.CLUSTER_LIFECYCLE);
        final Task        t       = builder.label("Task").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void lifecycleTransactionalTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "LifecycleTxTask", TaskType.NODE_LIFE_CYCLE);
        final Task        t       = builder.label("Task").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void lifecycleWithDependsOnTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "LifecycleDepsTask", TaskType.NODE_LIFE_CYCLE);
        final Task        t       = builder.label("Task").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void scheduleSimpleTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "ScheduleSimpleTask", TaskType.RUNNABLE);
        final Task        t       = builder.label("Task").cronExpression("never").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void scheduleTaskWithExclusionGroupName() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "ScheduleSimpleTask", TaskType.RUNNABLE);
        final Task        t       = builder.label("Task").cronExpression("never").addExclusionGroup("group1").build();
        rep.add(t);
        checkFile(t);
    }

    @Test public void scheduleTransactionalTask() {
        final TaskBuilder builder = TaskBuilder.create("", "tekgenesis.test", "ScheduleTransactionalTask", TaskType.RUNNABLE);
        final Task        t       = builder.label("Task").cronExpression("never").build();
        rep.add(t);
        checkFile(t);
    }

    private void checkFile(Task t) {
        final JavaCodeGenerator     cg   = new JavaCodeGenerator(outputDir, t.getDomain());
        final TaskBaseCodeGenerator base = new TaskBaseCodeGenerator(cg, t, "Base");
        base.generate();
        final TaskCodeGenerator user = new TaskCodeGenerator(cg, t, "Base");

        user.generate();

        // final String fileName = t.getImplementationClassFullName().replaceAll("\\.", File.separator) + "Base";
        // checkDiff(new File(outputDir, fileName + Constants.JAVA_EXT), new File(goldenDir, fileName + ".tj"));
    }
}  // end class TaskGenTest
