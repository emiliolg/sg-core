package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TableLocalGlobalOptionsShowcase */
public class TableLocalGlobalOptionsShowcase
    extends TableLocalGlobalOptionsShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resetCombos) is clicked */
    @Override @NotNull public Action resetCombos() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(comboButton) is clicked */
    @Override @NotNull public Action setE() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(comboTagsButton) is clicked */
    @Override @NotNull public Action setTagsE() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(change) is clicked */
    @Override @NotNull public Action changeOptions() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(button) is clicked */
        @Override @NotNull public Action setAtoB() { throw new IllegalStateException("To be implemented"); }

    }
}
