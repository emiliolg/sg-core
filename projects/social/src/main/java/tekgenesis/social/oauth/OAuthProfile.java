
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.oauth;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.social.Profile;
import tekgenesis.authorization.social.ProfileAttribute;

import static tekgenesis.authorization.social.ProfileAttribute.Definition.define;
import static tekgenesis.authorization.social.ProfileAttribute.STRING;
import static tekgenesis.authorization.social.SocialConstants.ACCESS_TOKEN;

/**
 * OAuth social profile.
 */
public abstract class OAuthProfile extends Profile {

    //~ Methods ......................................................................................................................................

    /** Get the profile access token. */
    public String getAccessToken() {
        return accessToken.get(ctx());
    }

    /** Set profile access token. */
    public void setAccessToken(@NotNull final String value) {
        accessToken.set(ctx(), value);
    }

    //~ Static Fields ................................................................................................................................

    public static final ProfileAttribute<String> accessToken = define(ACCESS_TOKEN, STRING);
}
