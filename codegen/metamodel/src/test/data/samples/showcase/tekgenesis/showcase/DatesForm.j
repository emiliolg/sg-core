package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DatesForm */
public class DatesForm
    extends DatesFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** 
     * Invoked when date_box(dateFrom) value changes
     * Invoked when date_box(dateTo) value changes
     * Invoked when date_time_box(timeFrom) value changes
     * Invoked when date_time_box(timeTo) value changes
     * Invoked when double_date_box(doubleDateFrom) value changes
     * Invoked when double_date_box(doubleDateTo) value changes
     * Invoked when combo_date_box(dateCombo) value changes
     * Invoked when combo_date_box(dateCombo1) value changes
     */
    @Override @NotNull public Action show() { throw new IllegalStateException("To be implemented"); }

}
