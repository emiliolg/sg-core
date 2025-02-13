package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: TestProtectedForm */
public class TestProtectedForm
    extends TestProtectedFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked from {@link #populate(TestProtected) method} to handle protected fields. */
    @Override public void populateProtectedFields(@NotNull TestProtected testProtected) { throw new IllegalStateException("To be implemented"); }

    /** Invoked from {@link #copyTo(TestProtected) method} to handle protected fields. */
    @Override public void copyToProtectedFields(@NotNull TestProtected testProtected) { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class InnersRow
        extends InnersRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked from {@link #populate(InnerTestProtected) method} to handle protected fields. */
        @Override public void populateProtectedFields(@NotNull InnerTestProtected innerTestProtected) { throw new IllegalStateException("To be implemented"); }

        /** Invoked from {@link #copyTo(InnerTestProtected) method} to handle protected fields. */
        @Override public void copyToProtectedFields(@NotNull InnerTestProtected innerTestProtected) { throw new IllegalStateException("To be implemented"); }

    }
}
