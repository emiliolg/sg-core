
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.shiro.codec.Base64;

import static java.net.URLEncoder.encode;

import static tekgenesis.common.core.Constants.BEARER;
import static tekgenesis.common.service.HeaderNames.*;
import static tekgenesis.sales.basic.Curl.get;
import static tekgenesis.sales.basic.Curl.post;

class TwitterApi {

    //~ Constructors .................................................................................................................................

    private TwitterApi() {}

    //~ Methods ......................................................................................................................................

    static boolean isFollowed(String source, String target) {
        final Relationship relationship = getRelation(source, target);
        return relationship != null && relationship.source.isFollowed();
    }

    static boolean isFollowing(String source, String target) {
        final Relationship relationship = getRelation(source, target);
        return relationship != null && relationship.source.isFollowing();
    }

    static User getUser(String name) {
        authenticate();
        if (isAuthenticated()) {
            final Reader reader = get(user(name), getTwitterRequestMap()).reader();
            return reader != null ? new Gson().fromJson(reader, User.class) : User.NONE;
        }
        return User.NONE;
    }

    @SuppressWarnings("NonThreadSafeLazyInitialization")
    private static void authenticate() {
        if (!isAuthenticated()) AUTH_RESPONSE = requestBearerToken("https://api.twitter.com/oauth2/token");
    }

    // Encodes the consumer key and secret to create the basic authorization key
    private static String encodeKeys() {
        try {
            final String fullKey = encode(CONSUMER_KEY, "UTF-8") + ":" + encode(CONSUMER_SECRET, "UTF-8");
            return new String(Base64.encode(fullKey.getBytes()));
        }
        catch (final UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String extract(String source) {
        return source.contains("@") ? source.substring(source.indexOf("@")) : source;
    }

    private static String relation(String source, String target) {
        return TWITTER_API + "friendships/show.json?source_screen_name=" + extract(source) + "&target_screen_name=" + extract(target);
    }

    // Constructs the request for requesting a bearer token and returns that token as a string
    private static AuthResponse requestBearerToken(String endPointUrl) {
        final HashMap<String, String> requestProps = new HashMap<>();
        requestProps.put("Host", API_TWITTER_COM);
        requestProps.put(USER_AGENT, TEK_DEMO);
        requestProps.put(AUTHORIZATION, "Basic " + encodeKeys());
        requestProps.put(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
        requestProps.put(CONTENT_LENGTH, "29");

        final String result = post(endPointUrl, requestProps, "grant_type=client_credentials").invoke();
        return new Gson().fromJson(result, AuthResponse.class);
    }

    private static String user(String name) {
        return TWITTER_API + "users/show.json?screen_name=" + extract(name);
    }

    private static boolean isAuthenticated() {
        return AUTH_RESPONSE != null;
    }

    private static Relationship getRelation(String source, String target) {
        authenticate();
        if (isAuthenticated()) {
            final Reader reader = get(relation(source, target), getTwitterRequestMap()).reader();
            return new Gson().fromJson(reader, Response.class).relationship;
        }
        return null;
    }

    private static Map<String, String> getTwitterRequestMap() {
        final Map<String, String> requestProps = new HashMap<>();
        requestProps.put(HOST, API_TWITTER_COM);
        requestProps.put(USER_AGENT, TEK_DEMO);
        requestProps.put(AUTHORIZATION, BEARER + AUTH_RESPONSE.access_token);
        return requestProps;
    }

    //~ Static Fields ................................................................................................................................

    private static final String TWITTER_API   = "https://api.twitter.com/1.1/";
    private static AuthResponse AUTH_RESPONSE = null;

    /** Talk to lucas about this two... */
    private static final String CONSUMER_KEY    = "46Q3SJbyQZJIVpRFQkngYg";
    private static final String CONSUMER_SECRET = "zm0Dkaw7wFI6l2tvG9zS8WNvdO3PFiY1Z6rBqw243E";
    private static final String API_TWITTER_COM = "api.twitter.com";
    private static final String TEK_DEMO        = "TekDemo";

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    static class AuthResponse {
        String access_token;
        String token_type;
    }

    @SuppressWarnings({ "UnusedDeclaration", "InstanceVariableMayNotBeInitialized" })
    static class Relation {
        private boolean followed_by;
        private boolean following;
        private String  id;
        private String  screen_name;

        boolean isFollowed() {
            return followed_by;
        }
        boolean isFollowing() {
            return following;
        }
        String getId() {
            return id;
        }
        String getName() {
            return screen_name;
        }
    }

    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    static class Relationship {
        Relation source;
        Relation target;
    }

    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    static class Response {
        Relationship relationship;
    }

    @SuppressWarnings({ "InstanceVariableMayNotBeInitialized", "UnusedDeclaration" })
    static class User {
        private String  description;
        private Integer id;
        private String  location;
        private String  name;
        private String  profile_image_url;
        private String  screen_name;

        String getDescription() {
            return description;
        }
        Integer getId() {
            return id;
        }
        String getLocation() {
            return location;
        }
        String getName() {
            return name;
        }
        String getProfileImage() {
            return profile_image_url;
        }
        String getScreenName() {
            return screen_name;
        }

        static User NONE = new User();
    }
}  // end class TwitterApi
