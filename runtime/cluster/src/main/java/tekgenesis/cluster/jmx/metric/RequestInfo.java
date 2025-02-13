
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
 * RequestInfo.
 */
public class RequestInfo extends BasicMetric {

    //~ Instance Fields ..............................................................................................................................

    public String durationUnit  = null;
    public double max;
    public double mean;
    public double min;
    public double percentile50;
    public double percentile75;
    public double percentile95;
    public double percentile98;
    public double percentile99;
    public double percentile999;
    public double stdDev;
}
