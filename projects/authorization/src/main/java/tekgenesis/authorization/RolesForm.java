
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.authorization.Messages.ROLE_ASSIGNMENTS_FOR;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Roles Form class.
 */
@SuppressWarnings("WeakerAccess")
public class RolesForm extends RolesFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action removeRole() {
        if (getRoles().getCurrent().isDefined(Field.ID)) {
            if (!forms.hasPermission(RolesForm.class, PredefinedPermission.DELETE))
                return actions.getError().withMessage(Messages.NO_DELETE_PERMISION.label());
        }
        return super.removeRole();
    }

    /** Save overridden to provide disable on edit functionality. */
    @NotNull @Override public Action saveRole() {
        if (!forms.hasPermission(RolesForm.class, PredefinedPermission.UPDATE)) return actions.getDefault();
        final Action action = super.saveRole();
        getRoles().getCurrent().setEdit(true);
        return action;
    }

    //~ Inner Classes ................................................................................................................................

    public class AssignmentsRow extends AssignmentsRowBase {}

    /**
     * Roles table.
     */
    public class RolesRow extends RolesRowBase {
        /** Assign the selected role to a user. */
        @NotNull @Override public Action assignRole() {
            final RoleAssignmentForm roleAssignment = forms.initialize(RoleAssignmentForm.class);
            final Role               role           = Role.find(getId());
            if (role != null) {
                roleAssignment.setRole(role);
                return actions.navigate(roleAssignment);
            }
            return actions.getDefault();
        }
        /** Populate overridden to provide disable on edit functionality. */
        @Override public void populate(@NotNull Role role) {
            super.populate(role);
            setEdit(true);
        }

        @NotNull @Override public Action viewAssignees() {
            final ImmutableList<RoleAssignment> assignments = selectFrom(ROLE_ASSIGNMENT).where(ROLE_ASSIGNMENT.ROLE_ID.eq(getId())).list();

            setAssignmentsFor(ROLE_ASSIGNMENTS_FOR.label(getName()));
            getAssignments().clear();

            if (!assignments.isEmpty()) {
                for (final RoleAssignment assignment : assignments) {
                    final AssignmentsRow row = getAssignments().add();
                    row.setOrgUnit(assignment.getOuName());
                    row.setUser(assignment.getUserId());
                }
            }

            setAssignmentsDialog(true);
            return actions.getDefault();
        }

        @NotNull @Override public Action viewPermissions() {
            return actions.detail(forms.initialize(PermissionList.class, getId()));
        }
    }  // end class RolesRow
}  // end class RolesForm
