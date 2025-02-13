
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.common.IndentedWriter;
import tekgenesis.metadata.task.Task;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Strings.quoted;

/**
 * Task Dumper.
 */
final class TaskDumper extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    private final Task task;

    //~ Constructors .................................................................................................................................

    TaskDumper(Task task, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(task, repository, writer, preferences);
        this.task = task;
    }

    //~ Methods ......................................................................................................................................

    @Override ModelDumper dump() {
        beginModel();
        dumpModelOptions();
        print(MMToken.SEMICOLON);
        newLine();
        return this;
    }

    @Override void dumpModelOptions() {
        if (task.getType() == TaskType.CLUSTER_LIFECYCLE) newLine().indent().print(MMToken.CLUSTER).unIndent();
        else if (task.getType() == TaskType.NODE_LIFE_CYCLE && isFull()) newLine().indent().print(MMToken.NODE).unIndent();

        final String schedule = task.getCronExpression();
        if (isNotEmpty(schedule)) newLine().indent().print(MMToken.SCHEDULE).space().print(quoted(schedule)).unIndent();

        final String exclusionGroup = task.getExclusionGroup();
        if (isNotEmpty(exclusionGroup)) newLine().indent().print(MMToken.EXCLUSION_GROUP).space().print(quoted(exclusionGroup)).unIndent();

        switch (task.getTransactionMode()) {
        case ALL:
            if (isFull()) newLine().indent().print(MMToken.TRANSACTION).space().print(MMToken.ALL).unIndent();
            break;
        case NONE:
            newLine().indent().print(MMToken.TRANSACTION).space().print(MMToken.NONE).unIndent();
            break;
        case ISOLATED:
            newLine().indent().print(MMToken.TRANSACTION).space().print(MMToken.ISOLATED).unIndent();
            break;
        case EACH:
            newLine().indent().print(MMToken.TRANSACTION).space().print(MMToken.EACH).space().print(task.getBatchSize()).unIndent();
            break;
        }

        if (isNotEmpty(task.getPattern())) newLine().indent().print(MMToken.PATTERN).space().print(quoted(task.getPattern())).unIndent();
    }  // end method dumpModelOptions
}  // end class TaskDumper
