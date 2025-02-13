
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.Property;
import tekgenesis.metadata.authorization.User;

/**
 * User class for Form: UserDetailsForm
 */
@Generated(value = "tekgenesis/showcase/UserDetails.mm", date = "1361194500471")
@SuppressWarnings("WeakerAccess")
public class UserDetailsForm extends UserDetailsFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void loadDetails() {
        final User user = context.getUser();
        if (user != null) {
            setId(user.getId());
            setName(user.getName());

            final OrganizationalUnit defaultOu = user.getDefaultOu();
            if (defaultOu != null) setOu(defaultOu.getName());

            for (Property property : user.getProperties()) {
                PropsRow prop = getProps().add();
                prop.setPropId(property.getId());
                prop.setPropName(property.getName());
                prop.setPropValue(property.asString());
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class PropsRow extends PropsRowBase {}
}
