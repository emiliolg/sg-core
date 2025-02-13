
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.authorization.Messages.*;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.REDIRECTION;
import static tekgenesis.metadata.form.model.FormConstants.MODAL_LOGIN;

/**
 * User class for Handler: PasswordHandler
 */
public class AuthorizationHandler extends AuthorizationHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    AuthorizationHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> forgot() {
        return ok(form(FORGOT_PASSWORD_LABEL.label(), ForgotPassword.class, ""));
    }

    @NotNull @Override public Result<Html> login(@Nullable final String username) {
        final boolean        modal             = req.getParameters().containsKey(MODAL_LOGIN);
        final Option<String> redirectionOption = req.getParameters().getFirst(REDIRECTION);
        final String         redirection       = redirectionOption.isPresent() ? "?" + REDIRECTION + "=" + redirectionOption.get() : "";

        final ShiroProps shiroProps          = Context.getEnvironment().get(ShiroProps.class);
        final String     forgotPasswordUrl   = notEmpty(shiroProps.forgotPasswordUrl, Routes.forgot().getUrl());
        final String     forgotPasswordLabel = notEmpty(shiroProps.forgotPasswordLabel, FORGOT_PASSWORD_LABEL.label());

        return ok(views.tekgenesisAuthorizationLogin(notNull(username), loginErrorMsg(), forgotPasswordUrl, forgotPasswordLabel, modal, redirection));
    }

    @NotNull @Override public Result<Html> reset(@NotNull String confirmationCode) {
        return ok(form(RESET_PASSWORD.label(), ResetPassword.class, confirmationCode));
    }

    @NotNull @Override public Result<Html> success() {
        return ok();
    }

    @NotNull @Override public Result<Html> unauthorizedPage() {
        return ok(views.tekgenesisAuthorizationUnauthorized());
    }

    @NotNull private Html form(final String title, final Class<?> form, final String pk) {
        return views.tekgenesisAuthorizationForm(title, form.getCanonicalName(), pk);
    }

    @NotNull private String loginErrorMsg() {
        if (req.getAttribute(SHIRO_LOGIN_FAILURE_ATTR) != null) return WRONG_CREDENTIALS.label();
        if (req.getAttribute(SHIRO_PASSWORD_EXPIRED_ATTR) != null) return EXPIRED_CREDENTIALS.label();
        if (req.getAttribute(SHIRO_INACTIVE_ACCOUNT_ATTR) != null) return INACTIVE_ACCOUNT.label();
        return "";
    }

    //~ Static Fields ................................................................................................................................

    public static final String SHIRO_LOGIN_FAILURE_ATTR    = "shiroLoginFailure";
    public static final String SHIRO_PASSWORD_EXPIRED_ATTR = "shiroPasswordExpired";
    public static final String SHIRO_INACTIVE_ACCOUNT_ATTR = "shiroInactiveAccount";
}  // end class AuthorizationHandler
