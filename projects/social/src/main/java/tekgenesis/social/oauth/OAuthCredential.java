
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

import tekgenesis.authorization.social.SocialCredential;

/**
 * OAuth 2.0 credential.
 */
public class OAuthCredential implements SocialCredential {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String code;
    @NotNull private final String provider;

    //~ Constructors .................................................................................................................................

    /** Construct oauth 2.0 credential with code and social provider. */
    public OAuthCredential(@NotNull final String code, @NotNull final String provider) {
        this.code     = code;
        this.provider = provider;
    }

    //~ Methods ......................................................................................................................................

    /** Return code. */
    @NotNull public String getCode() {
        return code;
    }

    /** Return provider. */
    @NotNull public String getProvider() {
        return provider;
    }
}
