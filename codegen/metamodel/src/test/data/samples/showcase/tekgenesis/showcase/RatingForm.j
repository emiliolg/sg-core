package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: RatingForm */
public class RatingForm
    extends RatingFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when rating(hearts) value changes
     * Invoked when text_field(text2) value changes
     * Invoked when button(but) is clicked
     */
    @Override @NotNull public Action sync() { throw new IllegalStateException("To be implemented"); }

}
