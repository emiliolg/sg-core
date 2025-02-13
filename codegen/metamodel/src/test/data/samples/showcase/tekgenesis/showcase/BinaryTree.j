package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: BinaryTree */
public class BinaryTree
    extends BinaryTreeBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(deselect) is clicked */
    @Override @NotNull public Action deselect() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class LettersRow
        extends LettersRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(element) is clicked */
        @Override @NotNull public Action select() { throw new IllegalStateException("To be implemented"); }

    }
}
