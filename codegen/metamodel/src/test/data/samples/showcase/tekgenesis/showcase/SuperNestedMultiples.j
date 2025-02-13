package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: SuperNestedMultiples */
public class SuperNestedMultiples
    extends SuperNestedMultiplesBase
{

    //~ Inner Classes ............................................................................................................

    public class SectionsRow
        extends SectionsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(delete) on subform(subf) is clicked */
        @Override @NotNull public Action delete() { throw new IllegalStateException("To be implemented"); }

    }
}
