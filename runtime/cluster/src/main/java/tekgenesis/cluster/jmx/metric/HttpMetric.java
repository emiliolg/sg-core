
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.metric;

import javax.management.Attribute;
import javax.management.AttributeList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.jmx.util.MemberInfo;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * HTTP Request Metrics.
 */
public final class HttpMetric {

    //~ Instance Fields ..............................................................................................................................

    private final JmxEndpoint conn;

    //~ Constructors .................................................................................................................................

    /** .* */
    public HttpMetric(@NotNull MemberInfo memberInfo) {
        conn = memberInfo.getJmxEndpoint();
    }
    /** .* */
    public HttpMetric(@NotNull JmxEndpoint c) {
        conn = c;
    }

    //~ Methods ......................................................................................................................................

    /** .* */
    public long getActiveRequests() {
        try {
            @SuppressWarnings("DuplicateStringLiteralInspection")
            final Object activeRequests = JmxInvokerImpl.invoker(conn)
                                          .mbean("metrics:name=com.codahale.metrics.servlet.AbstractInstrumentedFilter.activeRequests")
                                          .getAttribute("Count");
            return activeRequests != null ? (Long) activeRequests : 0;
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return 0;
        }
    }

    /** .* */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public BasicMetric getByRequestCode(String code) {
        try {
            final String        objectName = "metrics:name=com.codahale.metrics.servlet.AbstractInstrumentedFilter.responseCodes." + code;
            final AttributeList attrList   = JmxInvokerImpl.invoker(conn).mbean(objectName).getAttributesValue(ATTRIBUTES);

            final BasicMetric requestInfo = new BasicMetric();
            if (attrList != null) {
                requestInfo.meanRate          = getAttributeValue(attrList, 0);
                requestInfo.oneMinuteRate     = getAttributeValue(attrList, 1);
                requestInfo.fiveMinuteRate    = getAttributeValue(attrList, 2);
                requestInfo.fifteenMinuteRate = getAttributeValue(attrList, 3);
                requestInfo.rateUnit          = getAttributeValue(attrList, 4);
                requestInfo.count             = getAttributeValue(attrList, 5);
            }

            return requestInfo;
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return RequestMetric.EMPTY;
        }
    }

    /** .* */
    @SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
    public RequestMetric getMetric() {
        try {
            final String        mbeanName = "metrics:name=com.codahale.metrics.servlet.AbstractInstrumentedFilter.requests";
            final AttributeList attrList  = JmxInvokerImpl.invoker(conn).mbean(mbeanName).getAttributesValue(STATS_ATTR);

            final RequestMetric requestInfo = new RequestMetric();
            if (attrList != null) {
                requestInfo.percentile75      = getAttributeValue(attrList, 0);
                requestInfo.percentile95      = getAttributeValue(attrList, 1);
                requestInfo.percentile98      = getAttributeValue(attrList, 2);
                requestInfo.percentile99      = getAttributeValue(attrList, 3);
                requestInfo.percentile999     = getAttributeValue(attrList, 4);
                requestInfo.durationUnit      = getAttributeValue(attrList, 5);
                requestInfo.max               = getAttributeValue(attrList, 6);
                requestInfo.mean              = getAttributeValue(attrList, 7);
                requestInfo.stdDev            = getAttributeValue(attrList, 8);
                requestInfo.min               = getAttributeValue(attrList, 9);
                requestInfo.percentile50      = getAttributeValue(attrList, 10);
                requestInfo.meanRate          = getAttributeValue(attrList, 11);
                requestInfo.oneMinuteRate     = getAttributeValue(attrList, 12);
                requestInfo.fiveMinuteRate    = getAttributeValue(attrList, 13);
                requestInfo.fifteenMinuteRate = getAttributeValue(attrList, 14);
                requestInfo.rateUnit          = getAttributeValue(attrList, 15);
                requestInfo.count             = getAttributeValue(attrList, 16);
            }

            return requestInfo;
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return RequestMetric.EMPTY;
        }
    }

    private <T> T getAttributeValue(AttributeList attrList, int pos) {
        return cast(((Attribute) attrList.get(pos)).getValue());
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String[] ATTRIBUTES = {
        "MeanRate",
        "OneMinuteRate",
        "FiveMinuteRate",
        "FifteenMinuteRate",
        "RateUnit",
        "Count"
    };
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String[] STATS_ATTR = {
        "75thPercentile",
        "95thPercentile",
        "98thPercentile",
        "99thPercentile",
        "999thPercentile",
        "DurationUnit",
        "Max",
        "Mean",
        "StdDev",
        "Min",
        "50thPercentile",
        "MeanRate",
        "OneMinuteRate",
        "FiveMinuteRate",
        "FifteenMinuteRate",
        "RateUnit",
        "Count"
    };

    private static final Logger logger = getLogger(HttpMetric.class);
}  // end class HttpMetric
