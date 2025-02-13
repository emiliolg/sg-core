package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DateShowcaseForm */
public class DateShowcaseForm
    extends DateShowcaseFormBase
{

    //~ Methods ..................................................................................................................

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

    /** Invoked when button(addMessage) is clicked */
    @Override @NotNull public Action addMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(addMessages) is clicked */
    @Override @NotNull public Action addMessages() { throw new IllegalStateException("To be implemented"); }

}
