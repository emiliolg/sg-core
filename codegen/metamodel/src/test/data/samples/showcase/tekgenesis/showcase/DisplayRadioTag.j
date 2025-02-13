package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DisplayRadioTag */
public class DisplayRadioTag
    extends DisplayRadioTagBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when check_box(checkBox) value ui changes */
    @Override @NotNull public Action changeStyles() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when radio_group(radioTags) value changes */
    @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

}
