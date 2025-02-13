
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
 * User class for form: DisabledTable
 */
public class DisabledTable extends DisabledTableBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        for (int i = 0; i < 7; i++) {
            getFirstTable().add();
            getSecondTable().add();
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class FirstTableRow extends FirstTableRowBase {}

    public class SecondTableRow extends SecondTableRowBase {}
}
