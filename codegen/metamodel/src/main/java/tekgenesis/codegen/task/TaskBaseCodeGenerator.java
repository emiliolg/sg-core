
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
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.task.Task;
import tekgenesis.metadata.task.TransactionMode;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.task.TaskCodeGenerator.QNAME;
import static tekgenesis.common.core.Constants.ACCEPTS;
import static tekgenesis.common.core.Constants.FILTER;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.FALSE;
import static tekgenesis.metadata.task.TaskConstants.GET_CRON_EXPRESSION;
import static tekgenesis.metadata.task.TaskConstants.GET_SCHEDULE_AFTER;

/**
 * Task Base class generator.
 */
public final class TaskBaseCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Task task;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public TaskBaseCodeGenerator(JavaCodeGenerator cg, @NotNull Task task, String baseExt) {
        super(cg, task.getName() + baseExt);
        this.task = task;
    }

    //~ Methods ......................................................................................................................................

    public void populate() {
        // generate code for form base class
        withComments("Generated base class for task: " + task.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(WEAKER_ACCESS);
        addConstructor();

        switch (task.getType()) {
        case CLUSTER_LIFECYCLE:
        case NODE_LIFE_CYCLE:
            asAbstract().withSuperclass(LIFECYCLE_TASK_INSTANCE);
            break;
        case NODE_RUNNABLE:
        case RUNNABLE:
            addCronExpression();
            addScheduleAfter();
            asAbstract().withSuperclass(SCHEDULED_TASK_INSTANCE);
            break;
        case PROCESSOR:
            asAbstract().withSuperclass(generic(PROCESSOR_TASK_INSTANCE, "T"));
            addCronExpression();
            addScheduleAfter();
            addGenericVar("T");
            break;
        case IMPORTER:
            asAbstract().withSuperclass(IMPORTER_TASK_INSTANCE).withInterfaces(extractImport(IMPORTER));
            addAccepts();
            break;
        }
        addTransactionMode();
        addBatchSize();
        addExclusionGroup();
        addPurgePolicy();

        super.populate();

        // add logger to base
        addLogger(task.getFullName());
    }  // end method populate

    @Override public String getSourceName() {
        return task.getSourceName();
    }

    private String acceptImplementation() {
        final String pattern = task.getPattern();
        if (pattern == null) return FALSE;
        field(FILTER, Pattern.class).asFinal().asStatic().notNull().withValue(invokeStatic(Pattern.class, "compile", quoted(pattern)));
        // noinspection DuplicateStringLiteralInspection
        return invoke(invoke(FILTER, "matcher", invoke("file", "getPath")), "matches");
    }

    private void addAccepts() {
        final String result = acceptImplementation();

        method(ACCEPTS, Boolean.TYPE).override().return_(result).arg("file", File.class).notNull();

        final Method m = method(ACCEPTS, Boolean.TYPE).override().return_(result);
        m.arg(QNAME, QName.class).notNull();
        m.arg("file", File.class).notNull();
    }

    private void addBatchSize() {
        method("getBatchSize", Integer.class).override().notNull().return_(String.valueOf(task.getBatchSize()));
    }

    private void addConstructor() {
        constructor().asProtected().arg("task", TaskCodeGenerator.taskClass(task.getType())).notNull().superArg();
    }
    private void addCronExpression() {
        final String ce = quoted(task.getCronExpression());
        method(GET_CRON_EXPRESSION, String.class).override().notNull().return_(ce);
    }

    private void addExclusionGroup() {
        final String eg = quoted(Predefined.notNull(task.getExclusionGroup()));
        method("getExclusionGroup", String.class).override().notNull().return_(eg);
    }

    private void addPurgePolicy() {
        method("getPurgePolicy", Integer.TYPE).override().notNull().return_(String.valueOf(task.getPurgePolicy()));
    }

    private void addScheduleAfter() {
        final String ce = quoted(Predefined.notNull(task.getScheduleAfter()));
        method(GET_SCHEDULE_AFTER, String.class).override().notNull().return_(ce);
    }

    private void addTransactionMode() {
        method("getTransactionMode", TransactionMode.class).override()
            .notNull()
            .return_(refStatic(TransactionMode.class, task.getTransactionMode().name()));
    }
}  // end class TaskBaseCodeGenerator
