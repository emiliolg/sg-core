
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.workflow;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.entity.EntityBaseCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.metadata.workflow.Task;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.ENTITY_STRING;
import static tekgenesis.common.core.Constants.ID;
import static tekgenesis.common.core.Strings.*;

/**
 * Generator for Case base classes.
 */
public class CaseBaseCodeGenerator extends EntityBaseCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Case     c;
    private final DbObject payload;
    private final String   workItemClass;
    private final Entity   workItemEntity;

    //~ Constructors .................................................................................................................................

    /** Create a CaseBaseCodeGenerator. */
    public CaseBaseCodeGenerator(JavaCodeGenerator cg, Case c, @NotNull final Entity e, Entity workItemEntity, DbObject payload) {
        super(cg, e, e.getName());
        this.c              = c;
        this.workItemEntity = workItemEntity;
        workItemClass       = workItemEntity.getImplementationClassName();
        generateTasks();
        generateProcess();
        this.payload = payload;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void addCreateMethods() {
        super.addCreateMethods();

        final String entityName = payload.getName();
        final String param      = deCapitalizeFirst(entityName);
        final Method create     = method(CREATE_METHOD, c.getName()).asStatic().asPublic().notNull();
        create.withComments("Creates a new " + link(c.getName()) + " instance.");
        create.arg(param, entityName).notNull();
        create.declareNew(c.getName(), RESULT);
        create.statement(invoke(RESULT, setterName(ENTITY_STRING), param));
        create.statement(invoke(RESULT, setterName(CREATION), invokeStatic(DateTime.class, CURRENT)));
        create.return_(RESULT);
    }

    @Override protected void addInterfaces(String type, String keyType) {
        super.addInterfaces(type, keyType);
        final String payloadKey = makePrimaryKeyType(this, payload);
        final String wiKey      = makePrimaryKeyType(this, workItemEntity);
        asSerializable();
        withInterfaces(generic(MMCodeGenConstants.CASE_INSTANCE, type, keyType, payload.getFullName(), payloadKey, workItemClass, wiKey));
    }

    private void generateProcess() {
        final Method method = method(Constants.PROCESS);
        method.arg(ITEM, workItemClass).notNull();
        method.arg(RESULT, String.class).notNull();
    }

    private void generateTasks() {
        for (final Task task : c.getChildren()) {
            final Method method;

            final MetaModelReference reference = task.getForm();
            if (reference.isNotEmpty()) {
                final String form = reference.getFullName();
                method = method(task.getName(), workItemClass).notNull();
                method.withComments("Task to create a work item with " + link(form) + " as associated form.");
                method.declareNew(workItemClass, ITEM);
                method.statement(invoke(ITEM, setterName(TASK), quoted(task.getName())));
                method.statement(
                    invoke(ITEM, setterName(deCapitalizeFirst(PARENT_CASE + capitalizeFirst(ID))), invoke("", getterName(ID, Constants.INT))));
                method.statement(invoke(ITEM, setterName(CREATION), invokeStatic(DateTime.class, CURRENT)));
                method.return_(ITEM);
            }
            else if (isNotEmpty(task.getProcess())) {
                method = method(task.getName());
                method.withComments("Task to create a '" + task.getProcess() + "' instance.");
            }
        }
    }

    /*@Override protected void addInterfaces(String className, String primaryKeyType) {
     *  asSerializable().withInterfaces(generic(MMCodeGenConstants.CASE_INSTANCE, className, primaryKeyType));
     *}*/
}  // end class CaseBaseCodeGenerator
