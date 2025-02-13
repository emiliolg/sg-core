
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.Nullable;

/**
 * User class for Form: TestProtectedForm
 */
public class TestProtectedForm extends TestProtectedFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void copyToProtectedFields(@Nullable TestProtected testprotected) {}
    @Override public void populateProtectedFields(@Nullable TestProtected testprotected) {}

    //~ Inner Classes ................................................................................................................................

    public class InnersRow extends InnersRowBase {
        @Override public void copyToProtectedFields(@Nullable InnerTestProtected innertestprotected) {}

        @Override public void populateProtectedFields(@Nullable InnerTestProtected innertestprotected) {}
    }
}
