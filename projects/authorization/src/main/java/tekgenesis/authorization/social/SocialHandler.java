
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.Views;
import tekgenesis.common.core.Option;
import tekgenesis.common.service.Headers;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.authorization.social.Provider.FACEBOOK;
import static tekgenesis.authorization.social.Provider.GOOGLE;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_HOST;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_PROTO;

/**
 * User class for Handler: SocialHandler
 */
public class SocialHandler extends SocialHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    SocialHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/sg/social/login". */
    @NotNull @Override public Result<Html> login() {
        final WebContext context = createWebContext();
        return ok(views.tekgenesisAuthorizationSocialLogin(socialLoginUrl(FACEBOOK, context), socialLoginUrl(GOOGLE, context)));
    }

    /** Return social login url. */
    public String socialLoginUrl(@NotNull final Provider provider, @NotNull final WebContext webContext) {
        final Option<SocialProvider<SocialCredential, Profile>> client = SocialProviderService.findProvider(provider.id());
        return client.map(p -> p.redirect(webContext)).orElse("#");
    }

    private WebContext createWebContext() {
        return new WebContext() {
            @NotNull @Override public Headers getHeaders() {
                return req.getHeaders();
            }

            @Nullable @Override public String getRequestParameter(@NotNull String parameter) {
                return req.getParameters().getFirst(parameter).getOrNull();
            }

            @NotNull @Override public String getScheme() {
                return notEmpty(req.getHeaders().getOrEmpty(X_FORWARDED_PROTO), req.getScheme());
            }

            @NotNull @Override public String getHost() {
                return notEmpty(req.getHeaders().getOrEmpty(X_FORWARDED_HOST), req.getHost());
            }

            @NotNull @Override public String getUri() {
                return req.getUri();
            }

            @NotNull @Override public String getUrl() {
                return req.getUrl();
            }
        };
    }
}  // end class SocialHandler
