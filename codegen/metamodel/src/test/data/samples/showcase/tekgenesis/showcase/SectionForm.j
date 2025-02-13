package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SectionForm */
public class SectionForm
    extends SectionFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(more) is clicked */
    @Override @NotNull public Action more() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clear) is clicked */
    @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(remove) is clicked */
    @Override @NotNull public Action removeFirstRoom() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class RoomsRow
        extends RoomsRowBase
    {

        //~ Methods ..................................................................................................................

        /** 
         * Invoked when text_field(adults) value changes
         * Invoked when text_field(children) value changes
         */
        @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

    }

    public class PathRow
        extends PathRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when display(display) is clicked */
        @Override @NotNull public Action link() { throw new IllegalStateException("To be implemented"); }

    }

    public class SecRow
        extends SecRowBase
    {

    }

    public class CellsRow
        extends CellsRowBase
    {

    }

    public class SomeRow
        extends SomeRowBase
    {

    }

    public class ScrollRow
        extends ScrollRowBase
    {

    }
}
