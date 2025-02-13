
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.shiro.sso.SSOProps;
import tekgenesis.cluster.ClusterProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.core.Constants.REDIRECTION;
import static tekgenesis.security.shiro.web.URLConstants.SUCCESS_URI;

/**
 * Shiro Authentication filter that supports OU.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ShiroClientTokenAuthenticationFilter extends ShiroAuthenticationFilter {

    //~ Methods ......................................................................................................................................

    @Override protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
        throws Exception
    {
        super.onLoginSuccess(token, subject, request, response);
        final HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final SimpleCookie        tokenCookie  = getSuigenTokenCookie(subject);
        tokenCookie.saveTo(httpRequest, httpResponse);

        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return !SUCCESS_URI.equals(request.getParameter(REDIRECTION)) && super.isAccessAllowed(request, response, mappedValue);
    }

    //~ Methods ......................................................................................................................................

    /** Decrypt client session. */
    public static byte[] decrypt(byte[] bytes)
        throws BadPaddingException, IllegalBlockSizeException
    {
        return decryptCipher.doFinal(bytes);
    }

    /** Create cookie for session. */
    @NotNull public static SimpleCookie getSuigenTokenCookie(Subject subject)
        throws JsonProcessingException
    {
        final SimpleCookie cookie       = new SimpleCookie(SuiGenerisClientSessionManager.SUIGEN_SESSION_TOKEN);
        final ObjectMapper objectMapper = new ObjectMapper();

        final Session      session      = subject.getSession();
        final TokenSession tokenSession = new TokenSession(session);
        try {
            cookie.setValue(Base64.getEncoder().encodeToString(encrypt(objectMapper.writeValueAsBytes(tokenSession))));
        }
        catch (BadPaddingException | IllegalBlockSizeException e) {
            logger.error(e);
        }
        return cookie;
    }
    /** Encrypt client session. */
    private static byte[] encrypt(byte[] bytes)
        throws BadPaddingException, IllegalBlockSizeException
    {
        return encryptCipher.doFinal(bytes);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ShiroClientTokenAuthenticationFilter.class);

    @NonNls private static final String IS_MOBILE      = "mobile";
    @NonNls public static final String  CLIENT_IP      = "client-ip";
    @NonNls public static final String  TIMEZONE_PARAM = "timezone";

    private static final Cipher encryptCipher;
    private static final Cipher decryptCipher;

    private static final String CIPHER_ALG = "AES/CBC/PKCS5PADDING";

    static {
        try {
            final String          clusterName = Context.getProperties(ClusterProps.class).getClusterName();
            final IvParameterSpec iv          = new IvParameterSpec(Arrays.copyOf(clusterName.getBytes("UTF-8"), 16));

            final String    secretKey = Context.getProperties(SSOProps.class).secret;
            final SecretKey secret    = new SecretKeySpec(Arrays.copyOf(secretKey.getBytes("UTF-8"), 16), "AES");

            encryptCipher = Cipher.getInstance(CIPHER_ALG);
            decryptCipher = Cipher.getInstance(CIPHER_ALG);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secret, iv);
            decryptCipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(encryptCipher.getIV()));
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | UnsupportedEncodingException |
               NoSuchPaddingException e)
        {
            throw new RuntimeException(e);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class ExpiredPasswordException extends AuthenticationException {
        private static final long serialVersionUID = 6633604402156071250L;
    }

    private static class InactivatedAccountPasswordException extends AuthenticationException {
        private static final long serialVersionUID = 6633604402156071251L;
    }
}  // end class ShiroClientTokenAuthenticationFilter
