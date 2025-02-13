
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.task;

import java.io.File;

import org.jetbrains.annotations.NonNls;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.task.Task;
import tekgenesis.metadata.task.TaskType;

import static tekgenesis.common.core.Constants.PROCESS;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.util.JavaReservedWords.VOID;

/**
 * Task Code Generator.
 */
public class TaskCodeGenerator extends ClassGenerator {

    //~ Constructors .................................................................................................................................

    /** Create a Form code generator. */
    public TaskCodeGenerator(JavaCodeGenerator generator, Task task, String baseExt) {
        super(generator, capitalizeFirst(task.getName()));

        addConstructor(task.getType());
        withSuperclass(task.getFullName() + baseExt).withComments("User class for Task: " + task.getName());
        switch (task.getType()) {
        case NODE_LIFE_CYCLE:
        case CLUSTER_LIFECYCLE:
            override("onShutdown", extractImport(TEKGENESIS_TASK_STATUS)).notNull();
            override("onStartup", extractImport(TEKGENESIS_TASK_STATUS)).notNull();
            break;
        case NODE_RUNNABLE:
        case RUNNABLE:
            override("run", extractImport(TEKGENESIS_TASK_STATUS)).notNull();
            break;
        case PROCESSOR:
            // noinspection DuplicateStringLiteralInspection
            override("enumerate", generic(Seq.class, Object.class));
            override(PROCESS, extractImport(TEKGENESIS_TASK_STATUS)).arg("o", Object.class);
            withSuperclass(generic(task.getFullName() + baseExt, Object.class.getName()));
            break;
        case IMPORTER:
            final Method override1 = override(PROCESS, VOID);
            override1.arg("file", File.class);

            final Method override = override(PROCESS, VOID);
            // noinspection DuplicateStringLiteralInspection
            override.arg(QNAME, QName.class);
            override.arg("file", File.class);
            break;
        }
    }

    //~ Methods ......................................................................................................................................

    private void addConstructor(final TaskType type) {
        constructor().asPrivate().arg("task", taskClass(type)).superArg().notNull();
    }

    private Method override(String methodName, String methodType) {
        return method(methodName, methodType).override().throwNew(IllegalStateException.class, "\"to be implemented\"");
    }

    //~ Methods ......................................................................................................................................

    static String taskClass(final TaskType type) {
        switch (type) {
        case IMPORTER:
            return "tekgenesis.task.ImporterTask";
        case CLUSTER_LIFECYCLE:
        case NODE_LIFE_CYCLE:
            return "tekgenesis.task.LifecycleTask";
        default:
            // noinspection DuplicateStringLiteralInspection
            return "tekgenesis.task.ScheduledTask";
        }
    }

    //~ Static Fields ................................................................................................................................

    @NonNls static final String QNAME = "qname";

    private static final String TEKGENESIS_TASK_STATUS = "tekgenesis.task.Status";
}  // end class TaskCodeGenerator
