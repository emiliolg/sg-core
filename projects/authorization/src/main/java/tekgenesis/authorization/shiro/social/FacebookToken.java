
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.social;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.UsernamePasswordToken;

import tekgenesis.authorization.social.SocialConstants;

/**
 * Social login token.
 */
public class FacebookToken extends UsernamePasswordToken {

    //~ Instance Fields ..............................................................................................................................

    private String appId     = null;
    private String appSecret = null;

    private String code        = null;
    private String redirectUri = null;
    private String token       = null;

    //~ Constructors .................................................................................................................................

    /** Constructor with facebook oauth code and redirect uri. */
    private FacebookToken() {}

    //~ Methods ......................................................................................................................................

    /** Set remember me. */
    public FacebookToken rememberMe() {
        setRememberMe(true);
        return this;
    }

    /** Use authentication with access token. */
    public FacebookToken withAccessToken(String accessToken) {
        token = accessToken;
        return this;
    }

    /** Use authentication with code. */
    @SuppressWarnings("ParameterHidesMemberVariable")
    public FacebookToken withCode(String code, String appId, String appSecret, String redirectUri) {
        this.code        = code;
        this.appId       = appId;
        this.appSecret   = appSecret;
        this.redirectUri = redirectUri;
        return this;
    }

    /**
     * Obtain access token.
     *
     * <p>throws IllegalStateException if no code or accesstoken has been provided</p>
     */
    public String getAccessToken() {
        if (token == null) {
            if (code == null) throw new IllegalStateException("Cannot obtain token without code");
            try {
                final URL    authUrl      = new URL(
                        "https://graph.facebook.com/oauth/access_token?" + "client_id=" + appId + "&redirect_uri=" + redirectUri + "&client_secret=" +
                        appSecret + "&code=" + code);
                final String authResponse = readURL(authUrl);
                token = getPropsMap(authResponse).get(SocialConstants.ACCESS_TOKEN);
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return token;
    }

    /** Return Application Id. */
    public String getAppId() {
        return appId;
    }

    /** Return Application Secret. */
    public String getAppSecret() {
        return appSecret;
    }

    /** Returns code. */
    public String getCode() {
        return code;
    }

    @Override public Object getCredentials() {
        return null;  // credentials handled by facebook - we don't need them
    }

    @Override public Object getPrincipal() {
        return null;  // not known - facebook does the login
    }

    /** Returns redirect uri. */
    public String getRedirectUri() {
        return redirectUri;
    }

    private Map<String, String> getPropsMap(String someString) {
        final String[]            pairs = someString.split("&");
        final Map<String, String> props = new HashMap<>();
        for (final String propPair : pairs) {
            final String[] pair = propPair.split("=");
            props.put(pair[0], pair[1]);
        }
        return props;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create FacebookToken. Either withCode or withAccessToken should be called to be able to
     * authenticate
     */
    public static FacebookToken create() {
        return new FacebookToken();
    }

    static String readURL(URL url)
        throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final InputStream           is   = url.openStream();
        int                         r;
        while ((r = is.read()) != -1)
            baos.write(r);
        return new String(baos.toByteArray());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class FacebookToken
