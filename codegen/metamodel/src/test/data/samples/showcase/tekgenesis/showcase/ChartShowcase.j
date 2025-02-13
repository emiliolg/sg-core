package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ChartShowcase */
public class ChartShowcase
    extends ChartShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void data() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B3) is clicked */
    @Override @NotNull public Action more() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(stack) value changes */
    @Override @NotNull public Action stack() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B5) is clicked */
    @Override @NotNull public Action nextRnds() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution3) is clicked */
    @Override @NotNull public Action size1280() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution2) is clicked */
    @Override @NotNull public Action size854() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution1) is clicked */
    @Override @NotNull public Action size640() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution0) is clicked */
    @Override @NotNull public Action sizeAuto() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class LineRow
        extends LineRowBase
    {

    }

    public class ColumnRow
        extends ColumnRowBase
    {

    }

    public class BarRow
        extends BarRowBase
    {

    }

    public class PieRow
        extends PieRowBase
    {

    }
}
