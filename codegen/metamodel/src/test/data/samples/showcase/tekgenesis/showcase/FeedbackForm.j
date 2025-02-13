package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.ExecutionFeedback;
import org.jetbrains.annotations.NotNull;

/** User class for form: FeedbackForm */
public class FeedbackForm
    extends FeedbackFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(button) is clicked */
    @Override @NotNull public Action feedbackAction(@NotNull ExecutionFeedback feedback) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(button2) is clicked */
    @Override @NotNull public Action feedbackException(@NotNull ExecutionFeedback feedback) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(asd) is clicked */
    @Override @NotNull public Action simpleClick() { throw new IllegalStateException("To be implemented"); }

}
