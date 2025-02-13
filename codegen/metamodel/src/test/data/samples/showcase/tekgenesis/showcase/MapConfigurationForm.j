package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MapConfigurationForm */
public class MapConfigurationForm
    extends MapConfigurationFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button($B3) is clicked */
    @Override @NotNull public Action changeSize() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(zoomIn) is clicked */
    @Override @NotNull public Action zoomIn() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(zoomOut) is clicked */
    @Override @NotNull public Action zoomOut() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution3) is clicked */
    @Override @NotNull public Action size1280() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution2) is clicked */
    @Override @NotNull public Action size854() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution1) is clicked */
    @Override @NotNull public Action size640() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ArgentinaRow
        extends ArgentinaRowBase
    {

    }

    public class EthiopiaRow
        extends EthiopiaRowBase
    {

    }

    public class PilarRow
        extends PilarRowBase
    {

    }
}
