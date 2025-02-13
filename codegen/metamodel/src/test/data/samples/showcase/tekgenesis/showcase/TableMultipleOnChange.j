package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TableMultipleOnChange */
public class TableMultipleOnChange
    extends TableMultipleOnChangeBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(twoTimesField) value changes */
    @Override @NotNull public Action bchanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(setField10) is clicked */
    @Override @NotNull public Action setField() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(any) is clicked */
    @Override @NotNull public Action anyClick() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(sum) value changes */
    @Override @NotNull public Action sumchanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(otherField) value changes */
    @Override @NotNull public Action echanged() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(column2) value changes */
        @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

    }
}
