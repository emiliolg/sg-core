package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ExpressionShowcase */
public class ExpressionShowcase
    extends ExpressionShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(nick) value changes */
    @Override @NotNull public Action isNickAvailable() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B5) is clicked */
    @Override @NotNull public Action invoke() { throw new IllegalStateException("To be implemented"); }

}
