
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.oauth;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.social.*;
import tekgenesis.common.core.Constants;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.HeaderNames;

import static com.github.scribejava.core.utils.OAuthEncoder.decode;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.service.Status.OK;

/**
 * Base OAuth provider (supports only 2.0 clients).
 */
public abstract class OAuthProvider<U extends OAuthProfile> implements SocialProvider<OAuthCredential, U> {

    //~ Instance Fields ..............................................................................................................................

    protected CallbackManager callbacks      = null;
    private Integer           connectTimeout = null;
    private String            key            = null;
    private LoginManager      login          = null;
    private Integer           readTimeout    = null;

    private String  responseType  = null;
    private String  secret        = null;
    private boolean tokenAsHeader = false;

    //~ Constructors .................................................................................................................................

    protected OAuthProvider() {
        setup();
    }

    //~ Methods ......................................................................................................................................

    @Override public String redirect(@NotNull final WebContext context) {
        return getRedirectAction(context);
    }

    @NotNull @Override public CallbackManager getCallbackManager() {
        return callbacks;
    }

    /** Set provider callback manager. */
    public void setCallbacks(@NotNull final CallbackManager callbacks) {
        this.callbacks = callbacks;
    }

    /** Specify provider connect timeout. */
    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Nullable @Override public final OAuthCredential getCredentials(@NotNull final WebContext context) {
        return retrieveCredentials(context);
    }

    /** Specify provider key. */
    public void setKey(String key) {
        this.key = key;
    }

    /** Set provider login manager. */
    public void setLogin(@NotNull final LoginManager login) {
        this.login = login;
    }

    @NotNull @Override public LoginManager getLoginManager() {
        return login;
    }

    /** Return if token is sent as header. */
    public boolean isTokenAsHeader() {
        return tokenAsHeader;
    }

    /** Specify provider read timeout. */
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    /** Specify provider secret. */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /** Specify token sent as header. */
    public void setTokenAsHeader(boolean tokenAsHeader) {
        this.tokenAsHeader = tokenAsHeader;
    }

    @Nullable @Override public U getUserProfile(@NotNull final OAuthCredential credentials, @NotNull final WebContext context) {
        final U profile = retrieveUserProfile(credentials, context);
        if (profile != null) profile.setProvider(getName());
        return profile;
    }

    protected void addAccessTokenToProfile(U profile, @NotNull final OAuth2AccessToken token) {
        if (profile != null) profile.setAccessToken(token.getAccessToken());
    }

    /** Return the OAuth API for this client. */
    protected abstract BaseApi<OAuth20Service> api();

    /** Creates a new OAuth config for every authentication redirection. */
    protected OAuthConfig createOAuthConfig(@NotNull final WebContext context) {
        return createOAuthConfig(callbacks.computeCallback(context), null);
    }

    @NotNull protected OAuthConfig createOAuthConfig(@Nullable final String callback, @Nullable final String state) {
        return new OAuthConfig(getKey(),
            getSecret(),
            callback,
            SignatureType.Header,
            getOAuthScope(),
            null,
            getConnectTimeout(),
            getReadTimeout(),
            null,
            state,
            getResponseType());
    }

    /** Create an OAuth request. */
    protected OAuthRequest createOAuthRequest(@NotNull final String url, @NotNull final OAuth20Service service) {
        return new OAuthRequest(Verb.GET, url, service);
    }

    protected OAuth20Service createService(@NotNull WebContext context) {
        return api().createService(createOAuthConfig(context));
    }

    /** Setup callback manager. */
    protected CallbackManager defaultCallbackManager(SocialProviderProps props) {
        return new CallbackManager() {
            @NotNull @Override public String getCallbackPath() {
                return notEmpty(props.callback, CallbackManager.super.getCallbackPath());
            }
            @Override public String getProvider() {
                return getName();
            }
        };
    }

    /** Setup login manager. */
    protected LoginManager defaultLogginManager(SocialProviderProps props) {
        return new LoginManager() {};
    }

    /** Extract the user profile from response (JSON, XML...) of profile url. */
    protected abstract U extractUserProfile(@NotNull final String body);

    /** Return true if authentication has been cancelled. */
    protected boolean hasBeenCancelled(WebContext context) {
        return false;
    }

    @Nullable protected OAuthCredential retrieveCredentials(@NotNull final WebContext context) {
        // Check if the authentication has been cancelled
        if (hasBeenCancelled(context)) {
            logger.debug("Authentication has been cancelled by user");
            return null;
        }

        final String codeParameter = context.getRequestParameter(OAUTH_CODE);
        if (codeParameter != null) return new OAuthCredential(decode(codeParameter), getName());

        throw SocialException.noCredentialFound();
    }

    /** Setup provider from properties. */
    protected void setup() {
        final SocialProviderProps props = getSocialProviderProps();
        setKey(props.key);
        setSecret(props.secret);
        callbacks = defaultCallbackManager(props);
        login     = defaultLogginManager(props);
    }

    protected void signRequest(@NotNull final OAuth2AccessToken token, @NotNull final OAuthRequest request) {
        ((OAuth20Service) request.getService()).signRequest(token, request);
        if (isTokenAsHeader()) request.addHeader(HeaderNames.AUTHORIZATION, Constants.BEARER + token.getAccessToken());
    }

    protected OAuth2AccessToken getAccessToken(@NotNull final OAuth20Service service, @NotNull final OAuthCredential credentials) {
        final String code = credentials.getCode();
        return service.getAccessToken(code);
    }

    @NotNull protected String getCallback(@NotNull WebContext context) {
        return callbacks.computeCallback(context);
    }

    protected Integer getConnectTimeout() {
        return connectTimeout;
    }

    @Nullable protected JsonNode getJsonNode(@NotNull String body) {
        try {
            return JsonMapping.json().readTree(body);
        }
        catch (final IOException e) {
            logger.error(e);
        }
        return null;
    }

    protected String getKey() {
        return key;
    }

    protected abstract String getOAuthScope();

    /** Retrieve the profile url for the already authenticated user. */
    protected abstract String getProfileUrl(final OAuth2AccessToken token);

    protected Integer getReadTimeout() {
        return readTimeout;
    }

    protected String getRedirectAction(@NotNull final WebContext context) {
        return createService(context).getAuthorizationUrl();
    }

    protected String getResponseType() {
        return responseType;
    }

    protected void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    protected String getSecret() {
        return secret;
    }

    @Nullable private U retrieveUserProfile(@NotNull final OAuthCredential credentials, @NotNull final WebContext context) {
        try {
            final OAuth20Service    service = createService(context);
            final OAuth2AccessToken token   = getAccessToken(service, credentials);
            return retrieveUserProfileFromToken(service, token);
        }
        catch (final OAuthException e) {
            logger.error(e);
        }
        return null;
    }

    private U retrieveUserProfileFromToken(@NotNull final OAuth20Service service, @NotNull final OAuth2AccessToken token) {
        final String body = sendRequestForData(token, getProfileUrl(token), service);
        if (isEmpty(body)) throw SocialException.communicationException(token.getAccessToken());
        final U profile = extractUserProfile(body);
        addAccessTokenToProfile(profile, token);
        return profile;
    }

    @Nullable private String sendRequestForData(@NotNull final OAuth2AccessToken token, @NotNull final String url,
                                                @NotNull final OAuth20Service service) {
        final OAuthRequest request = createOAuthRequest(url, service);
        signRequest(token, request);
        final Response response = request.send();
        final int      code     = response.getCode();
        final String   body     = response.getBody();
        if (code != OK.code()) throw SocialException.communicationException(code, body);
        return body;
    }

    //~ Static Fields ................................................................................................................................

    private static final String   OAUTH_CODE = "code";
    protected static final Logger logger     = Logger.getLogger(OAuthProvider.class);
}  // end class OAuthProvider
