package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MapDragForm */
public class MapDragForm
    extends MapDragFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when user clicks on location button on map map */
    @Override @NotNull public Action locate(double lat, double lng) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when map(map) value changes */
    @Override @NotNull public Action updateCoords() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class MapRow
        extends MapRowBase
    {

    }
}
