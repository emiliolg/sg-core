
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

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;

@SuppressWarnings("JavaDoc")
interface DateOutput {

    //~ Methods ......................................................................................................................................

    @Nullable DateOnly getDateCombo();
    @Nullable DateOnly getDateCombo1();
    @Nullable DateOnly getDateFrom();
    @Nullable DateOnly getDateTo();
    @Nullable DateOnly getDoubleDateFrom();
    @Nullable DateOnly getDoubleDateTo();
    @Nullable DateTime getTimeFrom();
    @Nullable DateTime getTimeTo();
}
