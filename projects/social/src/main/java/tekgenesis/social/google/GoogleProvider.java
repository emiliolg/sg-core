
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.User;
import tekgenesis.authorization.social.ProfileAttribute;
import tekgenesis.authorization.social.SocialConstants;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.social.oauth.OAuthStateProvider;

import static tekgenesis.authorization.social.Profile.id;
import static tekgenesis.authorization.social.Provider.GOOGLE;

/**
 * Google social implementation.
 */
public class GoogleProvider extends OAuthStateProvider<GoogleProfile> {

    //~ Constructors .................................................................................................................................

    /** Create google client. */
    public GoogleProvider() {
        setResponseType(SocialConstants.RESPONSE_TYPE_CODE);
    }

    //~ Methods ......................................................................................................................................

    @Override public void userCreated(@NotNull GoogleProfile profile, @NotNull User user) {
        super.userCreated(profile, user);
        user.setName(profile.getDisplayName());
        user.setEmail(profile.getEmail());
        final Resource picture = Context.getSingleton(ResourceHandler.class).create().upload(profile.getDisplayName(), profile.getImage());
        user.setPicture(picture);
    }

    @Override public String getName() {
        return GOOGLE.id();
    }

    @Override protected GoogleApi20 api() {
        return GoogleApi20.instance();
    }

    @Override protected GoogleProfile extractUserProfile(@NotNull String body) {
        final GoogleProfile profile = new GoogleProfile();
        final JsonNode      json    = getJsonNode(body);
        if (json != null) {
            id.setFromJson(profile.ctx(), json);
            for (final ProfileAttribute<?> field : GoogleProfile.fields)
                field.setFromJson(profile.ctx(), json);
        }
        return profile;
    }

    @Override protected String getOAuthScope() {
        return "profile email";
    }

    @Override protected String getProfileUrl(OAuth2AccessToken token) {
        return "https://www.googleapis.com/plus/v1/people/me";
    }
}  // end class GoogleProvider
