
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

import tekgenesis.common.core.DateTime;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;

import static java.lang.String.format;

import static tekgenesis.authorization.Messages.EXPIRED_LINK;
import static tekgenesis.authorization.Messages.PASSWORD_RESET_SUCCESFULLY;
import static tekgenesis.authorization.Messages.USED_LINK;
import static tekgenesis.authorization.ResetPasswordBase.Field.CONFIRM_PASSWORD;
import static tekgenesis.authorization.ResetPasswordBase.Field.PASSWORD;
import static tekgenesis.authorization.User.hashPassword;
import static tekgenesis.authorization.g.UserBase.findWhere;
import static tekgenesis.authorization.g.UserTable.USER;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getPasswordStrengthScore;
import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * User class for Form: ResetPassword
 */
public class ResetPassword extends ResetPasswordBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        setPasswordScore(getPasswordStrengthScore(3));
    }

    // Nothing to populate...
    @NotNull @Override public Object populate() {
        final String code = getConfirmationCode();

        if (isNotEmpty(code)) {
            logger.info(format("Load reset password form for code %s", code));

            final User user = findWhere(USER.CONFIRMATION_CODE.eq(code));

            if (user != null) {
                logger.info(format("Trying to reset password for user %s", user));

                if (isCodeExpired(user.getConfirmationCodeExpiration())) {
                    logger.error(format("Code %s for user %s expired.", code, user));

                    setExpired(true);
                    setShowError(true);
                    setError(EXPIRED_LINK.label());
                }
            }
            else {
                logger.error(format("User for code %s not found.", code));

                setShowError(true);
                setError(USED_LINK.label());
            }
        }
        else logger.info("Load reset password form with no code.");

        return code;
    }

    /** Invoked when button(reset) is clicked. */
    @NotNull @Override public Action resetPassword() {
        if (isNotEmpty(getConfirmationCode())) {
            final String code = getConfirmationCode();
            final User   user = findWhere(USER.CONFIRMATION_CODE.eq(code));

            if (user != null) {
                if (!isCodeExpired(user.getConfirmationCodeExpiration())) {
                    // reset password
                    user.setPassword(hashPassword(getPassword(), user.getId()));

                    // reset code (links can be used only once).
                    user.setConfirmationCode(null);
                    user.setConfirmationCodeExpiration(null);

                    user.persist();

                    logger.info(format("Password reset for user %s", user));
                    setShowInfo(true);
                    setInfo(PASSWORD_RESET_SUCCESFULLY.label());

                    reset(PASSWORD, CONFIRM_PASSWORD);
                }
            }
        }
        return actions.getDefault();
    }

    private boolean isCodeExpired(@Nullable DateTime codeExpiration) {
        return codeExpiration != null && codeExpiration.isLessThan(DateTime.current());
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ResetPassword.class);
}  // end class ResetPassword
