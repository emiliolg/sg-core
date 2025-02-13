package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DialogsForm */
public class DialogsForm
    extends DialogsFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(open) is clicked */
    @Override @NotNull public Action openDialog() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(foc) is clicked */
    @Override @NotNull public Action focusText() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(foc2) value changes */
    @Override @NotNull public Action putText4() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(confirm) is clicked */
    @Override @NotNull public Action confirm() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B1) is clicked */
    @Override @NotNull public Action showOtherDialog() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B2) is clicked */
    @Override @NotNull public Action hideDialog() { throw new IllegalStateException("To be implemented"); }

    /** 
     * Invoked when dialog(other) value ui changes
     * Invoked when button($B8) is clicked
     */
    @Override @NotNull public Action goBackToPrevious() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B4) is clicked */
    @Override @NotNull public Action openTableZero() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B5) is clicked */
    @Override @NotNull public Action openTableOne() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B6) is clicked */
    @Override @NotNull public Action openSectionZero() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B7) is clicked */
    @Override @NotNull public Action openSectionOne() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button($B9) is clicked */
        @Override @NotNull public Action goBackToOther() { throw new IllegalStateException("To be implemented"); }

    }

    public class SectionRow
        extends SectionRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button($B10) is clicked */
        @Override @NotNull public Action goBackToOther() { throw new IllegalStateException("To be implemented"); }

    }
}
