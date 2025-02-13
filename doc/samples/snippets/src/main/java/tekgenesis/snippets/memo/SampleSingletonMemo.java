
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

import tekgenesis.common.util.SingletonMemo;

/**
 * Simple Singleton Memo for documentation purposes.
 */
public class SampleSingletonMemo extends SingletonMemo<Integer, SampleSingletonMemo> {

    //~ Instance Fields ..............................................................................................................................

    private int count = 0;

    //~ Constructors .................................................................................................................................

    SampleSingletonMemo() {
        super(1, TimeUnit.HOURS);
    }

    //~ Methods ......................................................................................................................................

    @Override protected Integer calculate(long lastRefreshTime, @Nullable Integer oldValue) {
        return ++count;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Get the SampleSingletonMemo instance.
     *
     * @return  SampleSingletonMemo instance
     */
    public static SampleSingletonMemo getInstance() {
        return getInstance(SampleSingletonMemo.class);
    }
}
