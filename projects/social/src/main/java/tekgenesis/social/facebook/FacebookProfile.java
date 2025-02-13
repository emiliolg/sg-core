
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.facebook;

import java.util.Arrays;
import java.util.List;

import tekgenesis.authorization.social.ProfileAttribute;
import tekgenesis.social.oauth.OAuthProfile;

import static tekgenesis.authorization.social.ProfileAttribute.*;
import static tekgenesis.authorization.social.ProfileAttribute.Definition.define;

/**
 * Facebook user profile.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "DuplicateStringLiteralInspection" })
public class FacebookProfile extends OAuthProfile {

    //~ Methods ......................................................................................................................................

    /** Get bio value. */
    public String getBio() {
        return bio.get(ctx());
    }

    /** Set bio value. */
    public void setBio(String value) {
        bio.set(ctx(), value);
    }

    /** Get birthday value. */
    public String getBirthday() {
        return birthday.get(ctx());
    }

    /** Set birthday value. */
    public void setBirthday(String value) {
        birthday.set(ctx(), value);
    }

    /** Get education value. */
    public String getEducation() {
        return education.get(ctx());
    }

    /** Set education value. */
    public void setEducation(String value) {
        education.set(ctx(), value);
    }

    /** Get email value. */
    @Override public String getEmail() {
        return email.get(ctx());
    }

    /** Set email value. */
    public void setEmail(String value) {
        email.set(ctx(), value);
    }

    /** Get favoriteAthletes value. */
    public String getFavoriteAthletes() {
        return favoriteAthletes.get(ctx());
    }

    /** Set favoriteAthletes value. */
    public void setFavoriteAthletes(String value) {
        favoriteAthletes.set(ctx(), value);
    }

    /** Get favoriteTeams value. */
    public String getFavoriteTeams() {
        return favoriteTeams.get(ctx());
    }

    /** Set favoriteTeams value. */
    public void setFavoriteTeams(String value) {
        favoriteTeams.set(ctx(), value);
    }

    /** Get firstName value. */
    public String getFirstName() {
        return firstName.get(ctx());
    }

    /** Set firstName value. */
    public void setFirstName(String value) {
        firstName.set(ctx(), value);
    }

    /** Get gender value. */
    public String getGender() {
        return gender.get(ctx());
    }

    /** Set gender value. */
    public void setGender(String value) {
        gender.set(ctx(), value);
    }

    /** Get hometown value. */
    public String getHometown() {
        return hometown.get(ctx());
    }

    /** Set hometown value. */
    public void setHometown(String value) {
        hometown.set(ctx(), value);
    }

    /** Get interestedIn value. */
    public String getInterestedIn() {
        return interestedIn.get(ctx());
    }

    /** Set interestedIn value. */
    public void setInterestedIn(String value) {
        interestedIn.set(ctx(), value);
    }

    /** Get languages value. */
    public String getLanguages() {
        return languages.get(ctx());
    }

    /** Set languages value. */
    public void setLanguages(String value) {
        languages.set(ctx(), value);
    }

    /** Get lastName value. */
    public String getLastName() {
        return lastName.get(ctx());
    }

    /** Set lastName value. */
    public void setLastName(String value) {
        lastName.set(ctx(), value);
    }

    /** Get link value. */
    public String getLink() {
        return link.get(ctx());
    }

    /** Set link value. */
    public void setLink(String value) {
        link.set(ctx(), value);
    }

    /** Get locale value. */
    public String getLocale() {
        return locale.get(ctx());
    }

    /** Set locale value. */
    public void setLocale(String value) {
        locale.set(ctx(), value);
    }

    /** Get location value. */
    public String getLocation() {
        return location.get(ctx());
    }

    /** Set location value. */
    public void setLocation(String value) {
        location.set(ctx(), value);
    }

    /** Get middleName value. */
    public String getMiddleName() {
        return middleName.get(ctx());
    }

    /** Set middleName value. */
    public void setMiddleName(String value) {
        middleName.set(ctx(), value);
    }

    /** Get name value. */
    @Override public String getName() {
        return name.get(ctx());
    }

    /** Set name value. */
    public void setName(String value) {
        name.set(ctx(), value);
    }

    /** Get political value. */
    public String getPolitical() {
        return political.get(ctx());
    }

    /** Set political value. */
    public void setPolitical(String value) {
        political.set(ctx(), value);
    }

    /** Get quotes value. */
    public String getQuotes() {
        return quotes.get(ctx());
    }

    /** Set quotes value. */
    public void setQuotes(String value) {
        quotes.set(ctx(), value);
    }

    /** Get relationshipStatus value. */
    public String getRelationshipStatus() {
        return relationshipStatus.get(ctx());
    }

    /** Set relationshipStatus value. */
    public void setRelationshipStatus(String value) {
        relationshipStatus.set(ctx(), value);
    }

    /** Get religion value. */
    public String getReligion() {
        return religion.get(ctx());
    }

    /** Set religion value. */
    public void setReligion(String value) {
        religion.set(ctx(), value);
    }

    /** Get significantOther value. */
    public String getSignificantOther() {
        return significantOther.get(ctx());
    }

    /** Set significantOther value. */
    public void setSignificantOther(String value) {
        significantOther.set(ctx(), value);
    }

    /** Get thirdPartyId value. */
    public String getThirdPartyId() {
        return thirdPartyId.get(ctx());
    }

    /** Set thirdPartyId value. */
    public void setThirdPartyId(String value) {
        thirdPartyId.set(ctx(), value);
    }

    /** Get timezone value. */
    public Integer getTimezone() {
        return timezone.get(ctx());
    }

    /** Set timezone value. */
    public void setTimezone(Integer value) {
        timezone.set(ctx(), value);
    }

    /** Get updatedTime value. */
    public String getUpdatedTime() {
        return updatedTime.get(ctx());
    }

    /** Set updatedTime value. */
    public void setUpdatedTime(String value) {
        updatedTime.set(ctx(), value);
    }

    /** Get verified value. */
    public Boolean getVerified() {
        return verified.get(ctx());
    }

    /** Set verified value. */
    public void setVerified(Boolean value) {
        verified.set(ctx(), value);
    }

    /** Get website value. */
    public String getWebsite() {
        return website.get(ctx());
    }

    /** Set website value. */
    public void setWebsite(String value) {
        website.set(ctx(), value);
    }

    /** Get work value. */
    public String getWork() {
        return work.get(ctx());
    }

    /** Set work value. */
    public void setWork(String value) {
        work.set(ctx(), value);
    }

    //~ Static Fields ................................................................................................................................

    public static final ProfileAttribute<String> bio              = define("bio", STRING);
    public static final ProfileAttribute<String> birthday         = define("birthday", STRING);
    public static final ProfileAttribute<String> education        = define("education", STRING);
    public static final ProfileAttribute<String> email            = define("email", STRING);
    public static final ProfileAttribute<String> favoriteAthletes = define("favoriteAthletes", STRING);
    public static final ProfileAttribute<String> favoriteTeams    = define("favoriteTeams", STRING);
    public static final ProfileAttribute<String> firstName        = define("firstName", STRING);
    public static final ProfileAttribute<String> gender           = define("gender", STRING);
    public static final ProfileAttribute<String> hometown         = define("hometown", STRING);
    public static final ProfileAttribute<String> interestedIn     = define("interestedIn", STRING);
    public static final ProfileAttribute<String> languages        = define("languages", STRING);
    public static final ProfileAttribute<String> lastName         = define("lastName", STRING);
    public static final ProfileAttribute<String> link             = define("link", STRING);
    public static final ProfileAttribute<String> locale           = define("locale", STRING);
    public static final ProfileAttribute<String> location         = define("location", STRING);
    public static final ProfileAttribute<String> middleName       = define("middleName", STRING);

    public static final ProfileAttribute<String>  name               = define("name", STRING);
    public static final ProfileAttribute<String>  political          = define("political", STRING);
    public static final ProfileAttribute<String>  quotes             = define("quotes", STRING);
    public static final ProfileAttribute<String>  relationshipStatus = define("relationshipStatus", STRING);
    public static final ProfileAttribute<String>  religion           = define("religion", STRING);
    public static final ProfileAttribute<String>  significantOther   = define("significantOther", STRING);
    public static final ProfileAttribute<String>  thirdPartyId       = define("thirdPartyId", STRING);
    public static final ProfileAttribute<Integer> timezone           = define("timezone", INTEGER);
    public static final ProfileAttribute<String>  updatedTime        = define("updatedTime", STRING);
    public static final ProfileAttribute<Boolean> verified           = define("verified", BOOLEAN);
    public static final ProfileAttribute<String>  website            = define("website", STRING);
    public static final ProfileAttribute<String>  work               = define("work", STRING);

    public static final List<ProfileAttribute<?>> fields = Arrays.asList(bio,
            birthday,
            education,
            email,
            favoriteAthletes,
            favoriteTeams,
            firstName,
            gender,
            hometown,
            interestedIn,
            languages,
            lastName,
            link,
            locale,
            location,
            middleName,
            name,
            political,
            quotes,
            relationshipStatus,
            religion,
            significantOther,
            thirdPartyId,
            timezone,
            updatedTime,
            verified,
            website,
            work);
}  // end class FacebookProfile
