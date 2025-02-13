package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TextFieldShowcaseForm */
public class TextFieldShowcaseForm
    extends TextFieldShowcaseFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when text_field(changeText) value changes
     * Invoked when text_field(uiChangeText) value ui changes
     * Invoked when text_field(changeTextDelay) value changes
     * Invoked when text_field(uiChangeTextDelay) value ui changes
     */
    @Override @NotNull public Action changedText() { throw new IllegalStateException("To be implemented"); }

    /** 
     * Invoked when table(table) is clicked
     * Invoked when button(add) is clicked
     */
    @Override @NotNull public Action createNewRow() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

    }
}
