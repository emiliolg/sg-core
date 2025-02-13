
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test.basic;

import tekgenesis.test.basic.g.StoreBase;

/**
 * User class for Entity: Store
 */
public class Store extends StoreBase {

    //~ Methods ......................................................................................................................................

    /** Use in tests. */
    public Data dataForTests() {
        return _data();
    }

    /** A virtual attribute. */
    public int getId10() {
        return data().id10;
    }

    //~ Inner Classes ................................................................................................................................

    public static final class Data extends OpenData {
        private int id10;

        public void onLoad(final Store instance) {
            id10 = id * 10;
        }
    }
}
