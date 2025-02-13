
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test.basic;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.test.basic.g.SearchableTypesBase;

import static tekgenesis.common.collections.Seq.repeat;

/**
 * User class for Model: SearchableTypes
 */
public class SearchableTypes extends SearchableTypesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Seq<Boolean> getBools() {
        return repeat(isBool()).take(2);
    }

    @NotNull @Override public Seq<DateOnly> getDates() {
        return repeat(getDate()).take(3);
    }

    @NotNull @Override public Seq<DateTime> getDateTimes() {
        return repeat(getDateTime()).take(2);
    }

    @NotNull @Override public Seq<BigDecimal> getDecimals() {
        return repeat(getDecimal()).take(1);
    }

    @NotNull @Override public Seq<Integer> getIntegers() {
        return repeat(getInteger()).take(2);
    }

    @NotNull @Override public Seq<Double> getReals() {
        return repeat(getReal()).take(0);
    }
    @NotNull @Override public Seq<String> getStrs() {
        return repeat(getStr()).take(2);
    }
}
