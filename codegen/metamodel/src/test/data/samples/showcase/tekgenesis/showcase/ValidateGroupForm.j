package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ValidateGroupForm */
public class ValidateGroupForm
    extends ValidateGroupFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when button(horButt) is clicked
     * Invoked when button(verButt) is clicked
     * Invoked when button(footButt) is clicked
     * Invoked when button(dialogButt) is clicked
     * Invoked when button(allButt) is clicked
     */
    @Override @NotNull public Action getDefault() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class HorTableRow
        extends HorTableRowBase
    {

    }
}
