
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.PermissionsBase;
import tekgenesis.authorization.g.RolePermissionBase;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.link.Link;
import tekgenesis.metadata.link.LinkLocalizer;
import tekgenesis.type.permission.PredefinedPermission;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static tekgenesis.authorization.ApplicationType.FORM;
import static tekgenesis.authorization.ApplicationType.LINK;
import static tekgenesis.authorization.shiro.AuthorizationUtils.clearAuthorizationCache;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Constants.HELP_URI;
import static tekgenesis.common.core.Constants.PERMISSION_ALL;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.type.permission.PredefinedPermission.READ;
import static tekgenesis.type.permission.PredefinedPermission.values;

/**
 * User class for Form: PermissionsForm
 */
public class PermissionsForm extends PermissionsFormBase {

    //~ Methods ......................................................................................................................................

    /** Add to all the listed permissions, the ones selected in massive permissions. */
    @NotNull @Override public Action addToSelected() {
        if (!forms.hasPermission(PermissionsForm.class, PredefinedPermission.CREATE))
            return actions.getError().withMessage(Messages.NO_CREATE_PERMISION.label());

        final Seq<String> permissionsToAdd = getMassPermissions();

        for (final ApplicationsRow row : getApplications()) {
            if (row.isSelected() && !row.isAll()) row.addPermissions(permissionsToAdd);
            row.setSelected(false);
        }
        setToAll(false);
        setMassPermissions(Colls.emptyIterable());
        return actions.getDefault();
    }

    @NotNull @Override public Action applyChanges() {
        for (final ApplicationsRow row : getApplications()) {
            if (row.isDirty()) {
                final Action action = row.updateRow();
                if (action.isError()) return action;
            }
        }
        return actions.getDefault();
    }

    @NotNull @Override public Action cancelChanges() {
        for (final ApplicationsRow row : getApplications()) {
            if (row.isDirty()) {
                final Action action = row.cancelRow();
                if (action.isError()) return action;
            }
        }
        return actions.getDefault();
    }

    /**
     * Invoked when the form is loaded. Load domains and fill the massive permissions options to the
     * predefined permissions.
     */
    @Override public void load() {
        // Populate domains and set the first one as default.
        final List<String> domains = new ArrayList<>();
        domains.addAll(getModelRepository().getDomains());
        setDomainOptions(domains);

        setMassPermissionsOptions(getPredefinedPermissions());
    }

    /** Remove from all the listed permissions, the ones selected in massive permissions. */
    @NotNull @Override public Action removeFromSelected() {
        if (!forms.hasPermission(PermissionsForm.class, PredefinedPermission.DELETE))
            return actions.getError().withMessage(Messages.NO_DELETE_PERMISION.label());

        final Seq<String> permissionsToRemove = getMassPermissions();
        for (final ApplicationsRow row : getApplications()) {
            if (row.isSelected() && !row.isAll()) row.removePermissions(permissionsToRemove);
            row.setSelected(false);
        }
        setToAll(false);
        setMassPermissions(Colls.emptyIterable());
        return actions.getDefault();
    }

    @NotNull @Override public Action selectAll() {
        for (final ApplicationsRow row : getApplications())
            row.setSelected(isToAll());
        return actions.getDefault();
    }

    /**
     * Invoked when combo_box(domain) value changes. Reloads applications and it's permissions given
     * the selected role and domain.
     */
    @NotNull @Override public Action updateApplications() {
        // Clear rows as they will be all new.
        final FormTable<ApplicationsRow> table = getApplications();
        table.clear();

        if (isDefined(Field.ROLE) && isDefined(Field.DOMAIN)) {
            final Map<String, RolePermission> rolePermissions = new HashMap<>();

            for (final RolePermission rolePermission : RolePermission.listByDomain(getRoleKey(), getDomain()))
                rolePermissions.put(rolePermission.getApplication(), rolePermission);

            // First populate Forms...
            for (final Form f : getModelRepository().getModels(getDomain(), Form.class)) {
                final Form            form = localizer(f).localize();
                final ApplicationsRow row  = table.add();
                row.setApplicationId(form.getName());
                row.setHelpLink(HELP_URI + getDomain() + "." + form.getName());
                row.setApplication(form.getLabel());
                row.setApplicationType(FORM);

                // set permission options to the row.
                row.setPermissionsOptions(form.getPermissions().toStrings());

                // set permission to the row if any.
                retrievePermissions(rolePermissions, row);
            }

            // ...then populate Links.
            for (final Link l : getModelRepository().getModels(getDomain(), Link.class)) {
                final Link            link = new LinkLocalizer(l, Context.getContext().getLocale()).localize();
                final ApplicationsRow row  = table.add();
                row.setApplicationId(link.getName());
                row.setHelpLink(HELP_URI + getDomain() + "." + link.getName());
                row.setApplication(link.getLabel());
                row.setApplicationType(LINK);

                // set permission options to the row.
                row.setPermissionsOptions(map(singletonList(READ), PredefinedPermission::getName));

                // set permission to the row if any.
                retrievePermissions(rolePermissions, row);
            }

            getApplications().sort(Comparator.comparing(ApplicationsRowBase::getApplicationId));
        }
        else if (isDefined(Field.DOMAIN)) setDomainHelpLink(HELP_URI + getDomain());

        return actions.getDefault();
    }  // end method updateApplications

    private void retrievePermissions(Map<String, RolePermission> rolePermissions, ApplicationsRow row) {
        final RolePermission rolePermission = rolePermissions.get(row.getApplicationId());
        if (rolePermission != null) {
            final Option<Permissions> first = rolePermission.getPermissions().getFirst();
            if (first.isPresent() && PERMISSION_ALL.equals(first.get().getPermission())) row.setAll(true);
            else row.setPermissions(rolePermission.getPermissions().toStrings());
        }
        else row.setPermissions(Colls.emptyIterable());
    }

    /** Get predefined permissions. */
    private Seq<String> getPredefinedPermissions() {
        return map(asList(values()), PredefinedPermission::getName);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Applications row.
     */
    public class ApplicationsRow extends ApplicationsRowBase {
        /** Add given permissions to a role for a given domain. */
        public void addPermissions(Seq<String> permissionsToAdd) {
            final List<String> result = new ArrayList<>();
            result.addAll(getPermissions().toList());
            for (final String permission : permissionsToAdd)
                // noinspection SuspiciousMethodCalls
                if (!result.contains(permissionsToAdd)) {
                    if (getApplicationType() != LINK || PredefinedPermission.READ.getName().equals(permission)) result.add(permission);
                }
            setPermissions(result);
            setDirty(true);
        }

        /** When selecting all, clear the current row permissions. */
        @NotNull @Override public Action allSelected() {
            if (!forms.hasPermission(PermissionsForm.class, PredefinedPermission.DELETE))
                return actions.getError().withMessage(Messages.NO_DELETE_PERMISION.label());

            if (isAll()) setPermissions(Colls.emptyIterable());
            setDirty(true);

            return actions.getDefault();
        }

        /** Cancels current row resetting its dirty state. */
        @NotNull @Override public Action cancelRow() {
            final RolePermission rolePermission = RolePermission.findByApplication(getRoleKey(), getDomain(), getApplicationId());

            if (rolePermission == null) {  // it never existed.
                setAll(false);
                setPermissions(Colls.emptyIterable());
            }
            else {
                final Option<Permissions> first = rolePermission.getPermissions().getFirst();
                if (first.isPresent()) {
                    setAll(first.get().getPermission().equals(PERMISSION_ALL));
                    setPermissions(rolePermission.getPermissions().map(PermissionsBase::getPermission));
                }
            }
            setDirty(false);
            return actions.getDefault();
        }

        /** Remove given permissions to a role for a given domain. */
        public void removePermissions(Seq<String> permissionsToRemove) {
            final List<String> result = new ArrayList<>();
            for (final String permission : getPermissions())
                if (!permissionsToRemove.contains(permission)) result.add(permission);
            setPermissions(result);
            setDirty(true);
        }

        /** Updates current row resetting its dirty state. */
        @NotNull @Override public Action updateRow() {
            RolePermission rolePermission = RolePermission.findByApplication(getRoleKey(), getDomain(), getApplicationId());

            if (rolePermission == null) {
                if (!forms.hasPermission(PermissionsForm.class, PredefinedPermission.CREATE))
                    return actions.getError().withMessage(Messages.NO_CREATE_PERMISION.label());

                rolePermission = RolePermissionBase.create();
                rolePermission.setRole(getRole());
                rolePermission.setDomain(getDomain());
                rolePermission.setApplication(getApplicationId());
            }
            else {
                if (!forms.hasPermission(PermissionsForm.class, PredefinedPermission.UPDATE))
                    return actions.getError().withMessage(Messages.NO_UPDATE_PERMISION.label());
                rolePermission.getPermissions().deleteAll();
            }

            if (isAll()) rolePermission.getPermissions().add().setPermission(PERMISSION_ALL);
            else for (final String permission : getPermissions())
                rolePermission.getPermissions().add().setPermission(permission);

            if (rolePermission.getPermissions().isEmpty()) rolePermission.delete();
            else rolePermission.persist();

            clearAuthorizationCache();
            setDirty(false);
            return actions.getDefault();
        }

        /** Validates if Request Approved && Create or Update are selected together. */
        @NotNull @Override public Action validateSelection() {
            setDirty(true);  // no errors, thous a valid selection, so dirtying the row.
            return actions.getDefault();
        }
    }  // end class ApplicationsRow
}  // end class PermissionsForm
