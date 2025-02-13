package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** User class for form: SortableTable */
public class SortableTable
    extends SortableTableBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(test) value changes */
    @Override @NotNull public Action changeScalar() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when table(table) is clicked */
    @Override @NotNull public Action select() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(currency) value changes */
        @Override @NotNull public Action change() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when the user type something on suggest_box(simple) to create suggest list */
        @Override @NotNull public Iterable<SimpleEntity> suggest(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

    }
}
