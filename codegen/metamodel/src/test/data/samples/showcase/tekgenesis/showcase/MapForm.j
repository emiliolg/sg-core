package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MapForm */
public class MapForm
    extends MapFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(bt) is clicked */
    @Override @NotNull public Action doStuff() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class PlacesRow
        extends PlacesRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button($B3) is clicked */
        @Override @NotNull public Action go() { throw new IllegalStateException("To be implemented"); }

    }

    public class MapRow
        extends MapRowBase
    {

        //~ Methods ..................................................................................................................

        /** 
         * Invoked when button(button) is clicked
         * Invoked when button(button1) is clicked
         * Invoked when button(button3) is clicked
         * Invoked when button(button4) is clicked
         * Invoked when button(button5) is clicked
         * Invoked when button(button6) is clicked
         */
        @Override @NotNull public Action show() { throw new IllegalStateException("To be implemented"); }

    }

    public class Map2Row
        extends Map2RowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(button2) is clicked */
        @Override @NotNull public Action show() { throw new IllegalStateException("To be implemented"); }

    }
}
