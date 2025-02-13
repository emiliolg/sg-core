
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.concurrent.TimeUnit;

import tekgenesis.common.util.SingletonMemo;

/**
 * Sample.
 */
class MemoSample extends SingletonMemo<Integer, MemoSample> {

    //~ Instance Fields ..............................................................................................................................

    private int value;

    //~ Constructors .................................................................................................................................

    protected MemoSample() {
        super(5, TimeUnit.MINUTES);
    }

    //~ Methods ......................................................................................................................................

    @Override protected Integer calculate(long lastRefreshTime, Integer oldValue) {
        return ++value;
    }
}
