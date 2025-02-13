package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ChartDataShowcase */
public class ChartDataShowcase
    extends ChartDataShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void data() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution3) is clicked */
    @Override @NotNull public Action size1280() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution2) is clicked */
    @Override @NotNull public Action size854() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution1) is clicked */
    @Override @NotNull public Action size640() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resolution0) is clicked */
    @Override @NotNull public Action size320() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(barWidth) value ui changes */
    @Override @NotNull public Action changeWidth() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when combo_box(seriesMode) value ui changes */
    @Override @NotNull public Action stack() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(hoverable) value ui changes */
    @Override @NotNull public Action hoverable() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(legend) value ui changes */
    @Override @NotNull public Action legend() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(showLabels) value ui changes */
    @Override @NotNull public Action showLabels() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(verticalLabels) value ui changes */
    @Override @NotNull public Action toVerticalLabels() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when toggle_button(stepped) value ui changes */
    @Override @NotNull public Action toStepped() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(defaultColors) is clicked */
    @Override @NotNull public Action defaultColors() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(red) is clicked */
    @Override @NotNull public Action red() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(blue) is clicked */
    @Override @NotNull public Action blue() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(green) is clicked */
    @Override @NotNull public Action green() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(palette1) is clicked */
    @Override @NotNull public Action palette1() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(palette2) is clicked */
    @Override @NotNull public Action palette2() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(palette3) is clicked */
    @Override @NotNull public Action palette3() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when chart(column) is clicked */
    @Override @NotNull public Action doClick(@NotNull Field field) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(data) is clicked */
    @Override @NotNull public Action reloadData() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ColumnRow
        extends ColumnRowBase
    {

    }
}
