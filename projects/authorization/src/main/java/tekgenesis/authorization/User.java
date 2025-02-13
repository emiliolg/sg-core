
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.HashSet;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.g.UserBase;
import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.util.Resources;
import tekgenesis.metadata.authorization.DeviceImpl;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.Property;
import tekgenesis.metadata.authorization.PropertyImpl;
import tekgenesis.metadata.authorization.Role;
import tekgenesis.metadata.authorization.RoleAssignment;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.type.resource.AbstractResource;

import static tekgenesis.authorization.g.UserTable.USER;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.metadata.form.model.FormConstants.VIEW_IMG_USERS_JPG;

/**
 * User class for Entity: User
 */
public class User extends UserBase implements tekgenesis.metadata.authorization.User {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String asString() {
        return Assignees.asString(this);
    }

    @NotNull @Override public String imagePath() {
        final Resource picture = getPicture();
        return picture != null ? Resources.imagePath(picture) : VIEW_IMG_USERS_JPG;
    }

    @Override public boolean includes(@NotNull tekgenesis.metadata.authorization.User user) {
        return equals(user);
    }

    /** Creates a new confirmation code and persists it. */
    public String persistNextConfirmation() {
        final String code = UUID.randomUUID().toString();
        setConfirmationCode(code);
        setConfirmationCodeExpiration(DateTime.current().addHours(2));
        persist();
        return code;
    }

    @NotNull @Override public String toString() {
        return getName();
    }

    @Override public void updateProperty(Property p) {
        final InnerEntitySeq<UserProperties> props = getProps();
        for (final UserProperties prop : props)
            if (prop.getPropertyId().equals(p.getId())) {
                prop.setValue(p.getValue().toString());
                prop.persist();
            }
    }

    @NotNull @Override public Iterable<tekgenesis.metadata.authorization.Device> getEnabledDevices() {
        return getDevices().filter(device -> device != null && !device.isDisabled()).map(device ->
                new DeviceImpl(device.getDeviceId(), device.getName()));
    }

    @NotNull @Override public String getImage() {
        String         result  = VIEW_IMG_USERS_JPG;
        final Resource picture = getPicture();
        if (picture != null) {
            final Resource.Entry master = picture.getEntry(AbstractResource.MASTER);
            if (master != null)
            // noinspection DuplicateStringLiteralInspection
            result = master.isExternal() ? master.getUrl() : "/sg/resource?sha=" + master.getSha();
        }
        return result;
    }

    @NotNull @Override public String getNameId() {
        return getName() + " (" + getId() + ")";
    }

    @NotNull @Override public Iterable<Property> getProperties() {
        return getProps().map(userProp -> PropertyImpl.createProperty(userProp.getPropertyId(), userProp.getPropertyName(), userProp.getValue()));
    }

    @Nullable @Override public Property getProperty(final String key) {
        final Option<UserProperties> userProp = getProps().filter(userProperties ->
                    userProperties != null && userProperties.getProperty().getId().equals(key)).getFirst();

        if (userProp.isPresent()) {
            final UserProperties prop = userProp.get();
            return PropertyImpl.createProperty(prop.getPropertyId(), prop.getPropertyName(), prop.getValue());
        }
        else return null;
    }

    @NotNull @Override public Seq<RoleAssignment> getRoleAssignmentsForOrganization(@NotNull OrganizationalUnit org) {
        return cast(AuthorizationUtils.getRoleAssignments(getId(), org));
    }

    @NotNull @Override public Seq<Role> getRoles() {
        return getRolesForOrganizations(AuthorizationUtils.getCurrentOrgUnit());
    }

    @NotNull @Override public Seq<Role> getRolesForOrganization(@NotNull OrganizationalUnit org) {
        return getRolesForOrganizations(org);
    }

    /** Get roles for given organization and ancestors. */
    private Seq<Role> getRolesForOrganizations(@NotNull final OrganizationalUnit org) {
        final Seq<Role> roles = AuthorizationUtils.getRoleAssignments(getId(), org).map(RoleAssignment::getRole);
        return seq(roles.into(new HashSet<>()));
    }

    //~ Methods ......................................................................................................................................

    /** Find user by given email. */
    @Nullable public static User findByEmail(@Nullable final String email) {
        return findWhere(USER.EMAIL.eq(email));
    }

    /** Obtain hash for user password. */
    public static String hashPassword(String password, String userId) {
        final String salt;
        if (UserPasswordHashDelimiterService.getDelimiter().isEmpty()) salt = userId;
        else salt = UserPasswordHashDelimiterService.getDelimiter().get().getFirstToken(userId);

        return new Sha256Hash(password, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toBase64();
    }

    //~ Static Fields ................................................................................................................................

    private static final int HASH_ITERATIONS = 512;

    private static final long serialVersionUID = -6370953905589727883L;

    //~ Inner Classes ................................................................................................................................

    public static final class Data extends OpenData {
        private static final long serialVersionUID = 0L;
    }
}  // end class User
