
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import tekgenesis.common.core.DateTime;

/**
 * Metric.
 */
public class Metric implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private DateTime            time   = null;
    private Map<String, Object> values = new HashMap<>();

    //~ Constructors .................................................................................................................................

    /**  */
    public Metric() {
        time = null;
    }

    /**  */
    public Metric(DateTime t) {
        time = t;
    }

    //~ Methods ......................................................................................................................................

    /** Add new metric-value. */
    public void addValue(String name, Object v) {
        values.put(name, v);
    }

    /**  */
    public DateTime getTime() {
        return time;
    }

    /** returns metric's value. */
    public Object getValue(String name) {
        return values.get(name);
    }

    /** returns all available metric's values. */
    public Map<String, Object> getValues() {
        return values;
    }

    /**  */
    public void setValues(Map<String, Object> v) {
        values = v;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1950206146395344814L;
}  // end class Metric
