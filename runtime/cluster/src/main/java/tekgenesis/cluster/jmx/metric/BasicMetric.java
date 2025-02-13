
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.metric;

/**
 * BasicRequestInfo.
 */
public class BasicMetric {

    //~ Instance Fields ..............................................................................................................................

    public long   count;
    public double fifteenMinuteRate;
    public double fiveMinuteRate;

    public double meanRate;
    public double oneMinuteRate;
    public String rateUnit = null;
}
