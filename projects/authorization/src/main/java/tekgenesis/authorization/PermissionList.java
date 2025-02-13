
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

import tekgenesis.common.core.QName;
import tekgenesis.form.FormTable;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;

import static java.util.Arrays.asList;

import static tekgenesis.authorization.g.RolePermissionTable.ROLE_PERMISSION;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * User class for Form: PermissionList
 */
public class PermissionList extends PermissionListBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getPermissionsList().clear();
        addPermissions(getPermissionsList(), getRoleId());
    }

    @NotNull @Override public Object populate() {
        load();
        return getRoleId();
    }

    //~ Methods ......................................................................................................................................

    private static void addPermissions(FormTable<PermissionsListRow> table, String roleId) {
        final ModelRepository repository = getModelRepository();

        //J-
        selectFrom(ROLE_PERMISSION)
        .where(ROLE_PERMISSION.ROLE_ID.in(asList(roleId.split(":"))))
        .forEach(rp -> {
            final QName fqn = rp.getQName();
            repository.getModel(fqn, Form.class)
            .ifPresent(f -> table.add().populate(mkString(rp.getPermissions()),
                    fqn,
                    localizer(f).localize().getLabel()));
        });
        //J+
    }

    //~ Inner Classes ................................................................................................................................

    public class PermissionsListRow extends PermissionsListRowBase {
        /** Creates a row with the given data. */
        public void populate(String permission, QName fqn, String applicationName) {
            setPermission(permission);
            setApplicationId(fqn.getFullName());
            setApplicationName(applicationName);
        }
    }
}  // end class PermissionList
