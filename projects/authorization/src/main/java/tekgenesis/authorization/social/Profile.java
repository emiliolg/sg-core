
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.authorization.social.ProfileAttribute.Definition.define;
import static tekgenesis.authorization.social.ProfileAttribute.STRING;

/**
 * Social profile.
 */
public abstract class Profile {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Object> attributes = new HashMap<>();

    //~ Methods ......................................................................................................................................

    /** Return attributes context. */
    @NotNull public Map<String, Object> ctx() {
        return attributes;
    }

    /** Get profile email. */
    public abstract String getEmail();

    /**
     * Get the user identifier within provider. This identifier is unique for this provider but not
     * necessarily through all providers.
     */
    public String getId() {
        return id.get(ctx());
    }

    /**
     * Set the user identifier. This identifier is unique for this provider but not necessarily
     * through all providers.
     */
    public void setId(@NotNull final String value) {
        id.set(ctx(), value);
    }

    /** Get profile name. */
    public abstract String getName();

    /**
     * Get the user identifier with a prefix which is the profile provider. This identifier is
     * unique through all providers.
     */
    public String getProfileId() {
        return getProvider() + "#" + getId();
    }

    /** Get profile social provider. */
    public String getProvider() {
        return provider.get(ctx());
    }

    /** Set social profile provider. */
    public void setProvider(@NotNull final String value) {
        provider.set(ctx(), value);
    }

    //~ Static Fields ................................................................................................................................

    public static final ProfileAttribute<String> id       = define(SocialConstants.ID, STRING);
    public static final ProfileAttribute<String> provider = define(SocialConstants.PROVIDER, STRING);
}  // end class SocialProfile
