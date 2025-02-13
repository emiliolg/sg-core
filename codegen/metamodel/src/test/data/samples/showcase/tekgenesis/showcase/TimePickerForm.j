package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TimePickerForm */
public class TimePickerForm
    extends TimePickerFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when time_picker(timePicker) value ui changes
     * Invoked when time_picker(timePickerStep5) value ui changes
     * Invoked when time_picker(timePickerStep15) value ui changes
     * Invoked when time_picker(from) value ui changes
     * Invoked when time_picker(to) value ui changes
     */
    @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when time_picker(withTz) value ui changes */
    @Override @NotNull public Action tzAwareValue() { throw new IllegalStateException("To be implemented"); }

}
