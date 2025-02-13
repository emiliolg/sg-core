
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.g.PermissionsBase;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.form.Suggestion;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.authorization.ApplicationPermissionsFormBase.Field.ROLE;
import static tekgenesis.authorization.Applications.applicationMapFromQuery;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.authorization.g.RolePermissionTable.ROLE_PERMISSION;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.core.Constants.HELP_URI;
import static tekgenesis.common.core.Constants.PERMISSION_ALL;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;

/**
 * User class for Form: ApplicationPermissionsForm
 */

public class ApplicationPermissionsForm extends ApplicationPermissionsFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("OverlyNestedMethod")
    public Action search() {
        Criteria              c          = Criteria.EMPTY;
        final ModelRepository repository = getModelRepository();

        final String applicationId = getApplicationId();
        if (applicationId != null) {
            final QName fqn = createQName(applicationId);
            c = repository.getModel(fqn, Form.class).map(form -> {
                        setApplicationName(localizer(form).localize().getLabel());
                        setApplicationHelpLink(HELP_URI + fqn);
                        return ROLE_PERMISSION.DOMAIN.eq(fqn.getQualification()).and(ROLE_PERMISSION.APPLICATION.eq(fqn.getName()));
                    }).orElse(c);
        }

        if (isDefined(ROLE) && getRole() != null) c = c.and(ROLE_PERMISSION.ROLE_ID.eq(getRole().getId()));

        if (c == Criteria.EMPTY) {
            getResults().clear();
            return actions.getDefault();
        }

        getResults().clear();

        RolePermission.listWhere(c).forEach(rp ->
                RoleAssignment.listWhere(ROLE_ASSIGNMENT.ROLE_ID.eq(rp.getRoleId()))  //
                .forEach(ra -> populate(ra, rp, repository)));

        return actions.getDefault();
    }  // end method search

    private void populate(RoleAssignment assignment, RolePermission rolePermission, ModelRepository repository) {
        final InnerEntitySeq<Permissions> permissions = rolePermission.getPermissions();
        if (!permissions.isEmpty()) {
            final ResultsRow row = getResults().add();

            row.setUserId(assignment.getUserId());
            row.setUserName(assignment.getUser().getName());

            row.setRoleId(rolePermission.getRoleId());
            row.setRoleName(rolePermission.getRole().getName());

            final String       currentFormId    = rolePermission.getApplication();
            final QName        currentFqn       = createQName(rolePermission.getDomain(), currentFormId);
            final Option<Form> currentForm      = repository.getModel(currentFqn, Form.class);
            String             currentFormLabel = currentFormId;
            if (currentForm.isPresent()) currentFormLabel = localizer(currentForm.get(), Context.getContext().getLocale()).localize().getLabel();

            row.setPermissionApplicationId(currentFqn.getFullName());
            row.setPermissionApplicationHelpLink(HELP_URI + currentFqn);
            row.setPermissionApplicationName(currentFormLabel);

            if (permissions.getFirst().get().getPermission().equals(PERMISSION_ALL)) {
                row.setAll(true);
                row.setPermission(PERMISSION_ALL);
            }
            else {
                row.setAll(false);
                row.setPermissionTags(permissions.map(PermissionsBase::getPermission));
            }
        }
    }

    //~ Methods ......................................................................................................................................

    /** Invoked when the user type something on suggest_box(application) to create suggest list. */
    @NotNull public static Iterable<Suggestion> loadApplications(@Nullable final String query) {
        return applicationMapFromQuery(query);
    }

    //~ Inner Classes ................................................................................................................................

    public class ResultsRow extends ResultsRowBase {}
}  // end class ApplicationPermissionsForm
