package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ClickChartShowcase */
public class ClickChartShowcase
    extends ClickChartShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void data() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when chart(column) is clicked */
    @Override @NotNull public Action doClick(@NotNull Field field) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(reload) is clicked */
    @Override @NotNull public Action reload() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when chart(line) is clicked */
    @Override @NotNull public Action lineClick(@NotNull Field field) { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ColumnRow
        extends ColumnRowBase
    {

    }

    public class LineRow
        extends LineRowBase
    {

    }
}
