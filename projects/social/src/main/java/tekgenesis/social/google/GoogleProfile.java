
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.google;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import tekgenesis.authorization.social.ProfileAttribute;
import tekgenesis.social.oauth.OAuthProfile;

import static tekgenesis.authorization.social.ProfileAttribute.Definition.define;
import static tekgenesis.authorization.social.ProfileAttribute.STRING;

/**
 * Google user profile.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class GoogleProfile extends OAuthProfile {

    //~ Methods ......................................................................................................................................

    /** Get birthday value. */
    public String getBirthday() {
        return birthday.get(ctx());
    }

    /** Set birthday value. */
    public void setBirthday(String value) {
        birthday.set(ctx(), value);
    }

    /** Get displayName value. */
    public String getDisplayName() {
        return displayName.get(ctx());
    }

    /** Set displayName value. */
    public void setDisplayName(String value) {
        displayName.set(ctx(), value);
    }

    /** Get email value. */
    @Override public String getEmail() {
        final List<GoogleEmail> mails = getEmails();
        return mails.isEmpty() ? "" : mails.get(0).getEmail();
    }

    /** Get emails value. */
    public List<GoogleEmail> getEmails() {
        return emails.get(ctx());
    }

    /** Get familyName value. */
    public String getFamilyName() {
        return familyName.get(ctx());
    }

    /** Set familyName value. */
    public void setFamilyName(String value) {
        familyName.set(ctx(), value);
    }

    /** Get gender value. */
    public String getGender() {
        return gender.get(ctx());
    }

    /** Set gender value. */
    public void setGender(String value) {
        gender.set(ctx(), value);
    }

    /** Get givenName value. */
    public String getGivenName() {
        return givenName.get(ctx());
    }

    /** Set givenName value. */
    public void setGivenName(String value) {
        givenName.set(ctx(), value);
    }

    /** Get image value. */
    public String getImage() {
        return image.get(ctx());
    }

    /** Set image value. */
    public void setImage(String value) {
        image.set(ctx(), value);
    }

    /** Get language value. */
    public String getLanguage() {
        return language.get(ctx());
    }

    /** Set language value. */
    public void setLanguage(String value) {
        language.set(ctx(), value);
    }

    /** Get profile name. */
    @Override public String getName() {
        return getDisplayName();
    }

    /** Get url value. */
    public String getUrl() {
        return url.get(ctx());
    }

    /** Set url value. */
    public void setUrl(String value) {
        url.set(ctx(), value);
    }

    //~ Static Fields ................................................................................................................................

    public static final ProfileAttribute<String>            gender      = define("gender", STRING);
    public static final ProfileAttribute<String>            displayName = define("displayName", STRING);
    public static final ProfileAttribute<String>            givenName   = define("name.givenName", STRING);
    public static final ProfileAttribute<String>            familyName  = define("name.familyName", STRING);
    public static final ProfileAttribute<String>            url         = define("url", STRING);
    public static final ProfileAttribute<String>            image       = define("image.url", STRING);
    public static final ProfileAttribute<String>            language    = define("language", STRING);
    public static final ProfileAttribute<String>            birthday    = define("birthday", STRING);
    public static final ProfileAttribute<List<GoogleEmail>> emails      = define("emails", new ProfileAttribute.ListAttribute<>(GoogleEmail.class));

    public static final List<ProfileAttribute<?>> fields = Arrays.asList(gender,
            displayName,
            givenName,
            familyName,
            url,
            image,
            language,
            birthday,
            emails);

    //~ Inner Classes ................................................................................................................................

    public static class GoogleEmail {
        @JsonProperty("value")
        public String email = null;
        public String type  = null;

        /** Get email. */
        public String getEmail() {
            return email;
        }

        /** Set email. */
        public void setEmail(String email) {
            this.email = email;
        }

        /** Get email type. */
        public String getType() {
            return type;
        }

        /** Set email type. */
        public void setType(String type) {
            this.type = type;
        }
    }
}  // end class GoogleProfile
