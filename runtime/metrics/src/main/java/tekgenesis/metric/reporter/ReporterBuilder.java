
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.reporter;

import tekgenesis.common.core.Option;
import tekgenesis.metric.ReporterType;

/**
 * Reporter Builder.
 */
public final class ReporterBuilder {

    //~ Constructors .................................................................................................................................

    private ReporterBuilder() {}

    //~ Methods ......................................................................................................................................

    /** @return  Option<Reporter> */
    public static Option<Reporter> build(ReporterType reporter) {
        if (reporter == ReporterType.GRAPHITE) return Option.of(new GraphiteReporter());
        if (reporter == ReporterType.JMX) return Option.of(new JmxReporter());
        else return Option.empty();
    }
}
