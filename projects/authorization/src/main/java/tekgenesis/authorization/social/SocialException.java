
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import org.jetbrains.annotations.NotNull;

/**
 * Social exceptions.
 */
public class SocialException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    private SocialException(String message) {
        super(message);
    }

    private SocialException(String message, Object... args) {
        this(String.format(message, args));
    }

    //~ Methods ......................................................................................................................................

    /** Unable to retrieve profile. */
    public static SocialException cannotRetrieveProfile(@NotNull final SocialProvider<SocialCredential, Profile> client,
                                                        @NotNull final SocialCredential                          credentials) {
        return new SocialException("No profile retrieved from authentication using client : %s and credentials %s", client, credentials);
    }

    /** Unable to stablish connection to social account. */
    public static SocialException communicationException(String token) {
        return new SocialException("Not data found for accessToken: %s", token);
    }

    /** Unable to stablish connection to social account. */
    public static SocialException communicationException(int code, String content) {
        return new SocialException("Failed to retrieve data / failed code : %s and body : %s", code, content);
    }

    /** No credential found exception. */
    public static SocialException noCredentialFound() {
        return new SocialException("No credential found");
    }

    /** Cannot find client exception. */
    static SocialException cannotFindClient(@NotNull final String client) {
        return new SocialException("Cannot find enabled social client '%s'", client);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 5453896992931072016L;
}  // end class SocialException
