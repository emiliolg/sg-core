
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric;

import javax.inject.Named;

import tekgenesis.service.ServiceProps;

/**
 * Metrics configuration.
 */
@Named("metric")
public class MetricProps extends ServiceProps {

    //~ Instance Fields ..............................................................................................................................

    /** prefix. */
    public String       domain   = "sg";
    public ReporterType reporter = ReporterType.NONE;
}
