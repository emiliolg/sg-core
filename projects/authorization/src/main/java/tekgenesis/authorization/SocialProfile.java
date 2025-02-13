
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

import tekgenesis.authorization.g.SocialProfileBase;
import tekgenesis.authorization.social.Profile;
import tekgenesis.persistence.Initialize;

import static tekgenesis.authorization.g.SocialProfileTable.SOCIAL_PROFILE;
import static tekgenesis.common.Predefined.option;
import static tekgenesis.persistence.EntityListenerType.BEFORE_DELETE;
import static tekgenesis.persistence.Sql.deleteFrom;

/**
 * User class for Model: SocialProfile
 */
public class SocialProfile extends SocialProfileBase {

    //~ Methods ......................................................................................................................................

    /** Find profile associated used. */
    @Nullable public static User find(@NotNull final Profile profile) {
        return option(find(profile.getProvider(), profile.getId())).map(SocialProfile::getUser).getOrNull();
    }

    @Initialize static void init() {
        User.addListener(BEFORE_DELETE,
            instance -> {
                deleteFrom(SOCIAL_PROFILE).where(SOCIAL_PROFILE.USER_ID.eq(instance.getId())).execute();
                return true;
            });
    }
}
