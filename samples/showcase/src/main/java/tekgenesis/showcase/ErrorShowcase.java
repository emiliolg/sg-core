
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.form.Action;
import tekgenesis.form.Navigate;

/**
 * User class for Form: ErrorShowcase
 */
@Generated(value = "tekgenesis/showcase/ErrorShowcase.mm", date = "1362054942463")
@SuppressWarnings("WeakerAccess")
public class ErrorShowcase extends ErrorShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action autoCloseMessage() {
        return actions.getDefault().withMessage("This message will autoclose in 3, 2, 1...");
    }

    @NotNull @Override public Action defaultWarningPersistent() {
        final Action action = actions.getDefault();
        action.withCustomMessage(DEFAULT_WARNING).warning().sticky();
        return action;
    }

    @NotNull @Override public Action defaultWithErrorAutoclosing() {
        final Action action = actions.getDefault();
        action.withCustomMessage(DEFAULT_ERROR).error();
        return action;
    }

    /** Invoked when button(button) is clicked. */
    @NotNull @Override public Action error() {
        throw new IllegalStateException("Error!!!!");
    }

    @NotNull @Override public Action errorAutoClose() {
        final Action error = actions().getError();
        error.withCustomMessage(ERROR_AUTO_CLOSE).notSticky();
        return error;
    }

    @NotNull @Override public Action errorSuccessMessage() {
        final Action action = actions.getError();
        action.withCustomMessage(ERROR_SUCCESS).success();
        return action;
    }

    @NotNull @Override public Action errorWarningMessage() {
        final Action action = actions.getError();
        action.withCustomMessage(ERROR_WARNING).warning();
        return action;
    }

    @NotNull @Override public Action killSession() {
        SecurityUtils.getSession().logout();
        return actions.getDefault();
    }

    @NotNull @Override public Action navigateNormal() {
        return actions.navigate(NiceToNavigateTo.class).withMessage(NORMAL_NAVIGATE);
    }

    @NotNull @Override public Action navigatewithError() {
        final Navigate<NiceToNavigateTo> navigate = actions.navigate(NiceToNavigateTo.class);
        navigate.withCustomMessage(NAVIGATE_ERROR).error();
        return navigate;
    }

    @NotNull @Override public Action navigatewithWarning() {
        final Navigate<NiceToNavigateTo> navigate = actions.navigate(NiceToNavigateTo.class);
        navigate.withCustomMessage(NAVIGATE_WARNING).warning().sticky();
        return navigate;
    }

    @NotNull @Override public Action persistentMessage() {
        final Action action = actions.getDefault();
        action.withCustomMessage(PERSISTENT_MESSAGE).sticky();
        return action;
    }

    //~ Static Fields ................................................................................................................................

    public static final String  ERROR_SUCCESS      = "This is an error action but with success style";
    public static final String  ERROR_WARNING      = "This is an error action but with warning style";
    public static final String  ERROR_AUTO_CLOSE   = "This is an error action but with auto close timer";
    public static final String  DEFAULT_ERROR      = "This is a default action but will autoclose and has error style";
    public static final String  DEFAULT_WARNING    = "This is a default action but sticky and warning style";
    public static final String  NORMAL_NAVIGATE    = "I navigated as usual.";
    public static final String  NAVIGATE_ERROR     = "I navigated as usual but the message has error style";
    public static final String  NAVIGATE_WARNING   = "I navigated as usual but the message is sticky and has warning style";
    private static final String PERSISTENT_MESSAGE = "This message is sticky, it won't close.";
}  // end class ErrorShowcase
