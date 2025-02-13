package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ItemViewer */
public class ItemViewer
    extends ItemViewerBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when tags(colors) value changes */
    @Override @NotNull public Action refresh() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button($B3) is clicked */
        @Override @NotNull public Action navigate() { throw new IllegalStateException("To be implemented"); }

    }
}
