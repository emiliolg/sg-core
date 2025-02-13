package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ClassroomForm */
public class ClassroomForm
    extends ClassroomFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(fill) value changes */
    @Override @NotNull public Action fill() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clear) is clicked */
    @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clearFill) is clicked */
    @Override @NotNull public Action clearAndFill() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(random) is clicked */
    @Override @NotNull public Action random() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(first) is clicked */
    @Override @NotNull public Action first() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(second) is clicked */
    @Override @NotNull public Action second() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(navigate) is clicked */
    @Override @NotNull public Action navigateToWidgetShowcase() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when table(students) is clicked */
    @Override @NotNull public Action rowClicked() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class StudentsRow
        extends StudentsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(view) is clicked */
        @Override @NotNull public Action swipe() { throw new IllegalStateException("To be implemented"); }

    }
}
