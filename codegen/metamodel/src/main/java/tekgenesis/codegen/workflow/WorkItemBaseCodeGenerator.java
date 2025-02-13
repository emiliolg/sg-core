
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.workflow;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.entity.EntityBaseCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;

import static java.lang.String.format;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Strings.setterName;

/**
 * Generator for Work Item base classes.
 */
public class WorkItemBaseCodeGenerator extends EntityBaseCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Entity caseEntity;
    private final DbObject        payload;
    @NotNull private final String workItemClass;

    //~ Constructors .................................................................................................................................

    /** Create a WorkItemBaseCodeGenerator. */
    public WorkItemBaseCodeGenerator(JavaCodeGenerator cg, @NotNull final Entity caseEntity, @NotNull final Entity workItemEntity, DbObject payload) {
        super(cg, workItemEntity, workItemEntity.getName());
        this.caseEntity = caseEntity;
        this.payload    = payload;
        workItemClass   = workItemEntity.getName();
        generateWorkItemsMethods();
        addNotifier();
    }

    //~ Methods ......................................................................................................................................

    @Override protected void addInterfaces(String type, String keyType) {
        super.addInterfaces(type, keyType);
        final String caseKey    = makePrimaryKeyType(this, caseEntity);
        final String payloadKey = makePrimaryKeyType(this, payload);
        asSerializable();
        withInterfaces(
            generic(MMCodeGenConstants.WORK_ITEM_INSTANCE, type, keyType, caseEntity.getFullName(), caseKey, payload.getFullName(), payloadKey));
    }

    private void addGetPriority() {
        final Method m = getter(PRIORITY, WORK_ITEM_PRIORITY);
        m.return_(invokeStatic(WORK_ITEM_PRIORITY, "find", refData(PRIORITY_CODE))).notNull();
    }

    /**
     * Criteria criteria = A_WORK_ITEM.OU_NAME.isNull().and(A_WORK_ITEM.ASSIGNEE.in(assignees)); for
     * (final RoleAssignment assignment : user.getRoleAssignmentsForOrganization(orgUnit)) {
     * criteria = criteria.or(A_WORK_ITEM.OU_NAME.in(assignment.getOu().getHierarchy()).and(
     * A_WORK_ITEM.ASSIGNEE.eq(user.asString()).or(A_WORK_ITEM.ASSIGNEE.eq(assignment.getRole().asString()))));
     * } return selectFrom(A_WORK_ITEM).where(A_WORK_ITEM.CLOSED.eq(false), criteria);
     */
    private void addListByAssigneesAndOrganization() {
        final Method method = method(LIST_BY_ASSIGNEES, generic(SELECT, workItemClass)).notNull()
                              .withComments("List matching work items for given user and organization.")
                              .asStatic();
        method.arg(USER, USER_CLASS).notNull();
        method.arg(ORG_UNIT, ORGANIZATIONAL_UNIT_CLASS).notNull();
        method.arg(ASSIGNEES, generic(Set.class, String.class)).notNull();

        method.assign(extractImport(CRITERIA_CLASS) + " " + CRITERIA_ARG,
            invoke(invoke(column(ORG_UNIT_NAME), "isNull"), "and", invoke(column(ASSIGNEE), "in", ASSIGNEES)));

        method.startForEach(ROLE_ASSIGNMENT_CLASS, ASSIGNMENT, invoke(USER, "getRoleAssignmentsForOrganization", ORG_UNIT));

        final String hierarchy = invoke(column(ORG_UNIT_NAME), "in", invoke(invoke(ASSIGNMENT, "getOu"), "getHierarchy"));
        final String user      = invoke(column(ASSIGNEE), "eq", invoke(USER, AS_STRING));
        final String role      = invoke(column(ASSIGNEE), "eq", invoke(invoke(ASSIGNMENT, "getRole"), AS_STRING));
        method.assign(CRITERIA_ARG, invoke(CRITERIA_ARG, "or", invoke(hierarchy, "and", invoke(user, "or", role))));

        method.endFor();

        method.return_(
            invoke(invokeStatic(SELECT_FROM_METHOD, tableSingleton), WHERE, invoke(column(CLOSED), "eq", Boolean.FALSE.toString()), CRITERIA_ARG));
    }

    private void addNotifier() {
        final String listener = format("w -> { %s; return true; }",
                invokeStatic(NOTIFIER_CLASS, "notify", "w", invoke("w", "getId"), invoke("w", "getUpdateTime")));
        method("initialize").asStatic()
            .withAnnotation(extractImport(INITIALIZE_ANNOTATION))
            .statement(invoke("", ADD_LISTENER, refStatic(ENTITY_LISTENER_TYPE, "AFTER_PERSIST"), listener));
    }

    private void addSetAssignee() {
        final Method m = setter(ASSIGNEE);
        m.arg(ASSIGNEE, "tekgenesis.metadata.authorization.Assignee").notNull();
        m.statement(invoke("", setterName(ASSIGNEE), invoke(ASSIGNEE, AS_STRING, "")));
    }

    private void addSetOrgUnit() {
        final Method m = setter(ORGANIZATIONAL_UNIT);
        m.arg(ORG_UNIT, ORGANIZATIONAL_UNIT_CLASS).notNull();
        m.statement(invoke("", setterName(ORG_UNIT_NAME), invoke(ORG_UNIT, GET_NAME, "")));
    }

    private void generateWorkItemsMethods() {
        addListByAssigneesAndOrganization();
        addSetAssignee();
        addSetOrgUnit();
        addGetPriority();

        // Implement process()
    }
}  // end class WorkItemBaseCodeGenerator
