package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ErrorShowcase */
public class ErrorShowcase
    extends ErrorShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(persistentMessage) is clicked */
    @Override @NotNull public Action persistentMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(autoCloseMessage) is clicked */
    @Override @NotNull public Action autoCloseMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(errorWithSuccess) is clicked */
    @Override @NotNull public Action errorSuccessMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(errorWithWarning) is clicked */
    @Override @NotNull public Action errorWarningMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(errorAutoClose) is clicked */
    @Override @NotNull public Action errorAutoClose() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(defaultError) is clicked */
    @Override @NotNull public Action defaultWithErrorAutoclosing() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(defaultWarning) is clicked */
    @Override @NotNull public Action defaultWarningPersistent() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(navigateNormal) is clicked */
    @Override @NotNull public Action navigateNormal() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(navigateError) is clicked */
    @Override @NotNull public Action navigatewithError() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(navigateWarning) is clicked */
    @Override @NotNull public Action navigatewithWarning() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(button) is clicked */
    @Override @NotNull public Action error() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(logout) is clicked */
    @Override @NotNull public Action killSession() { throw new IllegalStateException("To be implemented"); }

}
