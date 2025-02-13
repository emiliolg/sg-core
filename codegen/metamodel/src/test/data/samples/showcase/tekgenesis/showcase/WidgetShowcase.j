package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.ExecutionFeedback;
import org.jetbrains.annotations.NotNull;

/** User class for form: WidgetShowcase */
public class WidgetShowcase
    extends WidgetShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when color_picker(colorpicker) value changes */
    @Override @NotNull public Action printColor() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when breadcrumb(breadcrumb) value changes */
    @Override @NotNull public Action breadCrumbNavigation() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(suggest) value changes */
    @Override @NotNull public Action makeChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when pick_list(pick) value ui changes */
    @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when tree_view(treeView) value changes */
    @Override @NotNull public Action nodeSelected() { throw new IllegalStateException("To be implemented"); }

    /** 
     * Invoked when button($B7) is clicked
     * Invoked when button($B33) is clicked
     */
    @Override @NotNull public Action doStuff() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(synBtn) is clicked */
    @Override @NotNull public Action doWait() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(longBtn) is clicked */
    @Override @NotNull public Action doLongExecution(@NotNull ExecutionFeedback feedback) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(btn) is clicked */
    @Override @NotNull public Action randomProgress() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(download) is clicked */
    @Override @NotNull public Action download() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when date_box(datebox2) value changes */
    @Override @NotNull public Action viewDate() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when date_time_box(datetimebox) value changes */
    @Override @NotNull public Action viewDateTimeBox() { throw new IllegalStateException("To be implemented"); }

}
