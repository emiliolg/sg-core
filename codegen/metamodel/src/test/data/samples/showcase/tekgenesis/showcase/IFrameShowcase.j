package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: IFrameShowcase */
public class IFrameShowcase
    extends IFrameShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(urlText) value changes */
    @Override @NotNull public Action urlTextChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when iframe(test) value changes */
    @Override @NotNull public Action testChanged() { throw new IllegalStateException("To be implemented"); }

}
