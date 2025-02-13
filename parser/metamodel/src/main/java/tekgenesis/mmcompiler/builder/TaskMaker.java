
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.EnumSet;

import tekgenesis.common.core.QName;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.InvalidModifiersException;
import tekgenesis.metadata.task.TaskBuilder;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;

import static tekgenesis.common.core.Strings.unquote;
import static tekgenesis.mmcompiler.builder.ASTMetaModelReference.unresolvedMetaModel;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;

/**
 * Task Maker.
 */
final class TaskMaker extends Maker {

    //~ Constructors .................................................................................................................................

    TaskMaker(MetaModelAST formNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(formNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected ModelBuilder<?, ?> createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        if (modifiers.size() > 1) error(rootNode, new InvalidModifiersException(modifiers, fqn.getName()));
        final TaskType    taskType = getTaskType(modifiers, fqn.getName());
        final TaskBuilder builder  = TaskBuilder.create(sourceName, fqn.getQualification(), fqn.getName(), taskType)
                                     .label(label)
                                     .withModifiers(modifiers);
        for (final MetaModelAST n : rootNode) {
            switch (n.getType()) {
            case SCHEDULE:
                final MetaModelAST child = n.getChild(0);
                if (child.getType() == MMToken.TASK_REF) {
                    final MetaModelReference reference = unresolvedMetaModel(this, n, context, retrieveReferenceQualifiedId(child));
                    builder.scheduleAfter(reference.getFullName());
                }
                else builder.cronExpression(unquote(child.getText()));
                break;
            case EXCLUSION_GROUP:
                builder.exclusionGroup(unquote(n.getChild(0).getText()));
                break;
            case CLUSTER:
                builder.asClusterLifecycleTask();
                break;
            case NODE:
                // Only marked as node runnable if the task is not a lifecycle
                if (taskType != TaskType.NODE_LIFE_CYCLE) builder.asNodeRunnableTask();
                break;
            case TRANSACTION:
                makeTransactionMode(builder, n);
                break;
            case PATTERN:
                builder.withPattern(unquote(n.getChild(0).getText()));
                break;
            default:
                // Ignore
            }
        }

        checkBuilder(builder);
        return builder;
    }  // end method createBuilder

    private TaskBuilder makeTransactionMode(TaskBuilder builder, MetaModelAST transaction) {
        for (final MetaModelAST n : transaction) {
            switch (n.getType()) {
            case ALL:
                return builder.withTransactionMode(TransactionMode.ALL);
            case NONE:
                return builder.withTransactionMode(TransactionMode.NONE);
            case ISOLATED:
                return builder.withTransactionMode(TransactionMode.ISOLATED);
            case EACH:
                return builder.commitEach(Integer.parseInt(n.getChild(0).getText()));
            default:
                throw new IllegalStateException("Invalid Node Type: " + n.getText());
            }
        }
        return builder;
    }

    private TaskType getTaskType(EnumSet<Modifier> modifiers, String id) {
        TaskType type = TaskType.RUNNABLE;
        for (final Modifier modifier : modifiers) {
            switch (modifier) {
            case LIFECYCLE:
                type = TaskType.NODE_LIFE_CYCLE;
                break;
            case PROCESSOR:
                type = TaskType.PROCESSOR;
                break;
            case IMPORTER:
                type = TaskType.IMPORTER;
                break;
            case RUNNABLE:
                break;
            default:
                error(rootNode, new InvalidModifiersException(modifiers, id));
                break;
            }
        }
        return type;
    }
}  // end class TaskMaker
