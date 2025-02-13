
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.model.OAuth2AccessToken;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.User;
import tekgenesis.authorization.social.ProfileAttribute;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.social.oauth.OAuthStateProvider;

import static tekgenesis.authorization.social.Profile.id;
import static tekgenesis.authorization.social.Provider.FACEBOOK;

/**
 * Facebook social implementation.
 */
public class FacebookProvider extends OAuthStateProvider<FacebookProfile> {

    //~ Instance Fields ..............................................................................................................................

    protected String fields = DEFAULT_FIELDS;
    protected int    limit  = 0;

    //~ Methods ......................................................................................................................................

    @Override public void userCreated(@NotNull FacebookProfile profile, @NotNull User user) {
        super.userCreated(profile, user);

        user.setName(profile.getName());
        user.setEmail(profile.getEmail());
        final Resource picture = Context.getSingleton(ResourceHandler.class)
                                 .create()
                                 .upload(profile.getName(), "https://graph.facebook.com/" + profile.getId() + "/picture");
        user.setPicture(picture);
    }

    @Override public String getName() {
        return FACEBOOK.id();
    }

    @Override protected FacebookApi api() {
        return FacebookApi.instance();
    }

    @Override protected FacebookProfile extractUserProfile(@NotNull String body) {
        final FacebookProfile profile = new FacebookProfile();

        final JsonNode json = getJsonNode(body);
        if (json != null) {
            id.setFromJson(profile.ctx(), json);
            for (final ProfileAttribute<?> field : FacebookProfile.fields)
                field.setFromJson(profile.ctx(), json);
        }
        return profile;
    }

    @Override protected String getOAuthScope() {
        return "email,user_friends";
    }

    @Override protected String getProfileUrl(OAuth2AccessToken token) {
        String url = BASE_URL + "?fields=" + fields;
        if (limit > 0) url += "&limit=" + limit;
        // May include the appsecret_proof parameter...
        return url;
    }

    //~ Static Fields ................................................................................................................................

    protected static final String BASE_URL = "https://graph.facebook.com/v2.4/me";

    protected static final String DEFAULT_FIELDS = "id,name,first_name,last_name,gender,locale,email";
}  // end class FacebookProvider
