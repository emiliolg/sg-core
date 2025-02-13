
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
 * User class for Form: Bs3
 */
public class Bs3 extends Bs3Base {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        for (int i = 0; i < 3; i++)
            getTab().add();
    }

    //~ Inner Classes ................................................................................................................................

    public class TabRow extends TabRowBase {}
}
