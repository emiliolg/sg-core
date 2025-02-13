
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.snippets.memo;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.MemoMap;

/**
 * Simple Map Memo provided as a sample for documentation.
 */
public class SampleMapMemo extends MemoMap<Integer, Integer, SampleMapMemo> {

    //~ Instance Fields ..............................................................................................................................

    private int count;

    //~ Constructors .................................................................................................................................

    SampleMapMemo() {
        super(DEFAULT_DURATION, TimeUnit.MINUTES);
        count = -1;
    }

    //~ Methods ......................................................................................................................................

    @Override protected Integer calculate(Integer key, long lastRefreshTime, @Nullable Integer oldValue) {
        return ++count * key;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Get the SampleMapMemo Instance.
     *
     * @return  the SampleMapMemo instance
     */
    public static SampleMapMemo getInstance() {
        return getInstance(SampleMapMemo.class);
    }

    //~ Static Fields ................................................................................................................................

    private static final long DEFAULT_DURATION = 30;
}
