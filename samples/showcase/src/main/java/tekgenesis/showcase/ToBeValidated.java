
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

/**
 * User class for Form: ToBeValidated
 */
public class ToBeValidated extends ToBeValidatedBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        createInner();

        for (int i = 0; i < 3; i++)
            getSection().add().createNested();
    }

    //~ Inner Classes ................................................................................................................................

    public class SectionRow extends SectionRowBase {}
}
