
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

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.MappingCallback;
import tekgenesis.form.configuration.DynamicConfiguration;
import tekgenesis.form.configuration.UploadConfiguration;
import tekgenesis.model.KeyMap;
import tekgenesis.type.DynamicTypeConverter;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.authorization.PropertyScope.USER;
import static tekgenesis.authorization.User.hashPassword;
import static tekgenesis.authorization.g.PropertyTable.PROPERTY;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.authorization.shiro.AuthorizationUtils.clearAuthorizationCache;
import static tekgenesis.authorization.shiro.AuthorizationUtils.clearUserCache;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getPasswordStrengthScore;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Users Form class.
 */
@SuppressWarnings("WeakerAccess")
public class UserForm extends UserFormBase {

    //~ Methods ......................................................................................................................................

    /** Adds a role to the user. */
    @NotNull @Override public Action addRole() {
        if (!forms.hasPermission(UserForm.class, PredefinedPermission.UPDATE))
            return actions.getError().withMessage(Messages.NO_UPDATE_PERMISION.label());
        if (isDefined(Field.ID)) {
            final RoleAssignmentForm roleAssignment = forms.initialize(RoleAssignmentForm.class);
            roleAssignment.setUser(find());

            return actions.navigate(roleAssignment).callback(BackToUser.class);
        }
        return actions.getStay();
    }

    @NotNull @Override public Action copyRoles() {
        if (isDefined(Field.USER)) {
            final User user = getUser();
            if (user != null) {
                for (final RoleAssignment roleAssignment : RoleAssignment.listWhere(ROLE_ASSIGNMENT.USER_ID.eq(user.getId())).list().toList()) {
                    final RoleAssignment newAssignment = RoleAssignment.findOrCreate(keyAsString(),
                            roleAssignment.getRole().keyAsString(),
                            roleAssignment.getOu().keyAsString());
                    newAssignment.persist();
                }
            }
        }

        setChooseUserDialog(false);

        getRoles().clear();
        loadRoles(find());

        return actions.getDefault();
    }

    @Override public void copyTo(@NotNull User user) {
        super.copyTo(user);
        final String password = getPassword();
        // hash pass
        if (password != null) user.setPassword(hashPassword(password, getId()));
        else user.setPassword(getHashPass());
        user.setPicture(getPicture());
    }

    /** Create User Form. */
    @NotNull @Override public Action create() {
        final User oldUser = User.find(getId());
        if (oldUser != null)  // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("User id '" + getId() + "' already exists.");
        else
        {
            final Action action = super.create();
            if (isDefined(Field.ASSIGN_ROLES)) {
                final User assignRoles = getAssignRoles();
                if (assignRoles != null) RoleAssignment.listWhere(ROLE_ASSIGNMENT.USER_ID.eq(assignRoles.getId())).forEach(ra -> {
                    final RoleAssignment newAssignment = RoleAssignment.findOrCreate(keyAsString(),
                            ra.getRole().keyAsString(),
                            ra.getOu().keyAsString());newAssignment.persist();});
            }
            return action;
        }
    }                         // end method create

    @Override public void createOrUpdateProps(@NotNull User user) {
        user.getProps()                                                                          //
        .merge(getProps().filter(r1 -> r1.getValue() != null || r1.getProperty().isRequired()),  //
            (p, r) -> r.copyTo(p));
    }

    @NotNull @Override public Action fixCase() {
        if (Context.getProperties(ShiroProps.class).caseInsensitive && isDefined(Field.ID) && !isEmpty(getId())) setId(getId().toLowerCase());
        return actions.getDefault();
    }

    /** Loading properties. */
    @Override public void load() {
        setPasswordScore(getPasswordStrengthScore(3));

        if (!isDefined(Field.DEFAULT_OU)) {
            final OrgUnit ou = OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU);
            if (ou != null) setDefaultOu(ou);
        }

        final FormTable<PropsRow> properties = getProps();

        selectFrom(PROPERTY)             //
        .where(PROPERTY.SCOPE.eq(USER))  //
        .forEach(p -> properties.add().populate(p));

        setEnablePass(Context.getEnvironment().get(ShiroProps.class).builtInAuthentication);
        final UploadConfiguration configuration = configuration(Field.PICTURE);
        configuration.crop(true);
        configuration.ratio(1, 1);
        configuration.minSize(MIN_SIZE, MIN_SIZE);

        final KeyMap languages = getLanguages();
        setLocaleOptions(languages);
    }

    @NotNull @Override public Action openUserChooser() {
        setChooseUserDialog(true);
        return actions.getDefault();
    }

    /** Populating user and properties. */
    @NotNull @Override public User populate() {
        final User user = find();

        setEdit(true);

        setId(user.getId());

        setName(user.getName());
        setHashPass(user.getPassword());
        setEmail(user.getEmail());
        setDefaultOu(user.getDefaultOu());
        setPicture(user.getPicture());

        final String locale = user.getLocale();
        if (locale != null) {
            final KeyMap languages = getLanguages();
            if (!languages.containsKey(locale)) {
                languages.put(locale, locale);
                setLocaleOptions(languages);
            }
            setLocale(locale);
        }

        // Initializing properties.
        final FormTable<PropsRow> propertiesRows = getProps();
        for (final UserProperties userProperties : user.getProps())
            propertiesRows.get(getPropertyRowIndex(propertiesRows, userProperties.getProperty())).populate(userProperties);

        // Initializing roles.
        loadRoles(user);

        final FormTable<DevicesRow> devicesRows = getDevices();
        for (final Device device : user.getDevices())
            devicesRows.add().populate(device);

        return user;
    }

    /** Updates user locale. */
    @NotNull @Override public Action updateLocale() {
        final String locale = getLocale();
        if (locale != null) setInternalLocale(locale.toLowerCase());
        return actions.getDefault();
    }

    @NotNull @Override public Action viewAllPermissions() {
        return actions.detail(forms.initialize(PermissionList.class, map(getRoles(), RolesRowBase::getRoleKey).mkString(":")));
    }

    private void loadRoles(User user) {
        final FormTable<RolesRow> roles = getRoles();
        selectFrom(ROLE_ASSIGNMENT)                       //
        .where(ROLE_ASSIGNMENT.USER_ID.eq(user.getId()))  //
        .forEach(ra -> roles.add().populate(ra));
    }

    private int getPropertyRowIndex(FormTable<PropsRow> propertiesRows, Property property) {
        int i = 0;
        for (final PropsRow row : propertiesRows) {
            if (row.getProperty().equals(property)) return i;
            i++;
        }
        return -1;
    }

    //~ Methods ......................................................................................................................................

    @NotNull static KeyMap getLanguages() {
        final KeyMap languages = KeyMap.create();
        for (final Locale l : Locale.values())
            languages.put(l.key().toLowerCase(), l.label());
        return languages;
    }

    //~ Static Fields ................................................................................................................................

    private static final int MIN_SIZE = 150;

    //~ Inner Classes ................................................................................................................................

    public static class BackToUser implements MappingCallback<RoleAssignmentForm, UserForm> {
        @Override public void onSave(@NotNull RoleAssignmentForm roleAssignmentForm, @NotNull UserForm userForm) {
            userForm.getRoles().clear();
            userForm.loadRoles(userForm.find());
        }
    }

    public class DevicesRow extends DevicesRowBase {}

    public class PropsRow extends PropsRowBase {
        @Override public void copyTo(@NotNull UserProperties userProperties) {
            super.copyTo(userProperties);
            final Object value = getValue();
            if (value != null) {
                final DynamicTypeConverter converter = getDynamicConversion();
                userProperties.setValue(converter.toString(value));
            }
        }
        /** Populating properties row. */
        @Override public void populate(@NotNull UserProperties userProperties) {
            final Property property = userProperties.getProperty();
            populate(property);
            setValue(getDynamicConversion().fromString(userProperties.getValue()));
        }

        private void populate(@NotNull final Property p) {
            setProperty(p);
            setType(p.getType());
            setRequired(p.isRequired());

            p.configureDynamicType(this.<DynamicConfiguration>configuration(Field.VALUE).getTypeConfiguration());
        }

        private DynamicTypeConverter getDynamicConversion() {
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            return configuration.getTypeConverter();
        }
    }

    public class RolesRow extends RolesRowBase {
        /** Removes a role assignment. */
        @NotNull @Override public Action deleteAssignment() {
            if (!forms.hasPermission(UserForm.class, PredefinedPermission.UPDATE))
                return actions.getError().withMessage(Messages.NO_UPDATE_PERMISION.label());
            final RoleAssignment roleAssignment = RoleAssignment.find(keyAsString(), getRoleKey(), getOuKey());
            if (roleAssignment != null) {
                roleAssignment.delete();

                getRoles().removeCurrent();
                clearAuthorizationCache(getId());
                clearUserCache(getId());
            }
            return actions.getDefault();
        }

        @NotNull @Override public Action viewPermissions() {
            return actions.detail(forms.initialize(PermissionList.class, getRoleKey()));
        }
    }
}  // end class UserForm
