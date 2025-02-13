
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
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormAction;
import tekgenesis.metadata.workflow.CaseBuilder;
import tekgenesis.metadata.workflow.TaskBuilder;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.metadata.workflow.CaseBuilder.create;
import static tekgenesis.mmcompiler.ast.MMToken.FIELD;
import static tekgenesis.mmcompiler.ast.MMToken.OPTION;
import static tekgenesis.mmcompiler.builder.ASTMetaModelReference.unresolvedMetaModel;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;

/**
 * Create a Case maker.
 */
class CaseMaker extends Maker {

    //~ Constructors .................................................................................................................................

    CaseMaker(MetaModelAST caseNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(caseNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    protected CaseBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final CaseBuilder builder = create(sourceName, fqn.getQualification(), fqn.getName(), context.getSchema()).label(label)
                                    .withModifiers(modifiers);

        for (final MetaModelAST n : rootNode) {
            switch (n.getType()) {
            case ENTITY_REF:
                getMetaModelReference(Entity.class, n).ifPresent(builder::withEntity);
                break;
            case FORM_REF:
                getMetaModelReference(Form.class, n).ifPresent(builder::withDefaultForm);
                break;
            case LIST:
                n.children(FIELD).forEach(f -> addTask(builder, f));
                break;
            case NOTIFY:
                builder.notifyCase();
                break;
            default:
                // Ignore
            }
        }

        checkBuilder(builder);

        return builder;
    }  // end method createBuilder

    private void addOptions(TaskBuilder<?> builder, MetaModelAST fieldNode) {
        for (final MetaModelAST n : fieldNode) {
            try {
                if (n.hasType(OPTION)) buildTaskOption(builder, n);
            }
            catch (final BuilderException e) {
                error(n, e);
            }
        }
    }

    private void addTask(CaseBuilder builder, MetaModelAST field) {
        try {
            builder.addTask(buildTask(builder, field));
        }
        catch (final DuplicateFieldException e) {
            error(field, e);
        }
    }

    private <T extends TaskBuilder<T>> TaskBuilder<T> buildTask(CaseBuilder container, MetaModelAST taskNode) {
        assertType(taskNode, FIELD);

        final TaskBuilder<T> builder = container.task();

        final Tuple<MetaModelAST, String> idLabel = retrieveLabeledId(taskNode);
        builder.id(idLabel.first().getText()).label(idLabel.second());

        addOptions(builder, taskNode);

        return cast(builder);
    }

    private void buildTaskOption(TaskBuilder<?> builder, MetaModelAST n)
        throws BuilderException
    {
        final FieldOption opt = retrieveOption(n);
        if (opt == null) return;

        switch (opt) {
        case ACTIONS:
            for (final MetaModelAST identifier : n.getChild(1).children())
                builder.withAction(retrieveFormAction(identifier));
            break;
        case FORM:
            final MetaModelAST arg = n.getChild(1).getEffectiveNode();
            builder.form(unresolvedMetaModel(this, arg, context, retrieveReferenceQualifiedId(arg)));
            break;
        case PROCESS:
            builder.process(n.getChild(1).getText());
            break;
        default:
            // Ignore
        }
    }

    private FormAction retrieveFormAction(MetaModelAST identifier) {
        final Tuple<MetaModelAST, String> lid    = retrieveLabeledId(identifier);
        final String                      action = lid._1().getText();
        final String                      label  = lid._2();
        return new FormAction(action, isEmpty(label) ? toWords(fromCamelCase(action)) : label);
    }
}  // end class CaseMaker
