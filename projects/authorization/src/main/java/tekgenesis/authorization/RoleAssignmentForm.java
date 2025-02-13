
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

import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.form.Action;

import static tekgenesis.authorization.shiro.AuthorizationUtils.clearAuthorizationCache;
import static tekgenesis.authorization.shiro.AuthorizationUtils.clearUserCache;

/**
 * Role Assignment Form class.
 */
@SuppressWarnings("WeakerAccess")
public class RoleAssignmentForm extends RoleAssignmentFormBase {

    //~ Methods ......................................................................................................................................

    /** Creates a role assignment and stays. */
    @NotNull @Override public Action create() {
        final RoleAssignment roleAssignment = RoleAssignment.find(getUserKey(), getRoleKey(), getOuKey());
        if (roleAssignment != null)
        // noinspection DuplicateStringLiteralInspection
        return actions.getDefault().withMessage("Role Assignment for user '" + getUser().getName() + "' already exists.");
        else {
            super.create();
            final String id = getUser().getId();
            clearAuthorizationCache(id);
            clearUserCache(id);
            return actions.getDefault();
        }
    }

    /** load method. */
    @Override public void load() {
        if (!isDefined(Field.OU)) {
            final OrgUnit ou = OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU);
            if (ou != null) setOu(ou);
        }
    }

    @NotNull @Override public Action update() {
        clearAuthorizationCache(getUser().getId());
        return super.update();
    }
}
