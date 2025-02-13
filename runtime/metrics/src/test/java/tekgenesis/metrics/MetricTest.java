
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metrics;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.metric.MetricProps;
import tekgenesis.metric.ReporterType;
import tekgenesis.metric.core.*;
import tekgenesis.metric.service.MetricService;
import tekgenesis.persistence.TableFactory;
import tekgenesis.service.ServiceManager;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.metric.core.MetricNamespace.resolveSource;

/**
 * Metric API Test.
 */
@RunWith(Parameterized.class)
public class MetricTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName      = null;
    private MBeanServer                    mBeanServer = null;

    private MetricService metricService = null;
    @Rule public DbRule   db            = new DbRule() {
            @Override protected void after() {
                if (metricService != null) metricService.stop();
                super.after();
            }

            @Override protected void before() {
                createDatabase(dbName);
            }

            @Override protected void initTableFactory() {
                TableFactory.setFactory(new TableFactory(createStoreHandlerFactory()));

                final MetricProps props = new MetricProps();
                props.domain   = TEST_METRIC;
                props.reporter = ReporterType.JMX;
                env.put(props);

                final ServiceManager serviceManager = new ServiceManager(env);
                Context.getContext().setSingleton(ServiceManager.class, serviceManager);

                metricService = new MetricService(serviceManager);
                metricService.start();
                mBeanServer = ManagementFactory.getPlatformMBeanServer();
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void callMetricTest() {
        final CallMetric meter = MetricsFactory.call(DOMAIN, getClass(), "callTested");

        assertMetric(DOMAIN, getClass(), "status.failed", 0);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 0);
        assertMetric(DOMAIN, getClass(), "callTested", 0);

        meter.start();
        meter.mark(true);
        assertMetric(DOMAIN, getClass(), "status.failed", 0);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 1);
        assertMetric(DOMAIN, getClass(), "callTested", 0);

        meter.mark(false);
        assertMetric(DOMAIN, getClass(), "status.failed", 1);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 1);
        assertMetric(DOMAIN, getClass(), "callTested", 0);

        meter.stop();
        assertMetric(DOMAIN, getClass(), "status.failed", 1);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 1);
        assertMetric(DOMAIN, getClass(), "callTested", 1);
    }

    @Test public void meterMetricTest() {
        final MeterMetric meter = MetricsFactory.meter(DOMAIN, getClass(), "meterTested");
        assertMetric(DOMAIN, getClass(), "meterTested", 0);
        meter.mark();
        assertMetric(DOMAIN, getClass(), "meterTested", 1);
    }

    @Test public void statusMetricTest() {
        final StatusMeterMetric meter = new StatusMeterMetric(DOMAIN, getClass().getName());

        assertMetric(DOMAIN, getClass(), "status.failed", 0);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 0);

        meter.mark(true);

        assertMetric(DOMAIN, getClass(), "status.failed", 0);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 1);

        meter.mark(false);
        assertMetric(DOMAIN, getClass(), "status.failed", 1);
        assertMetric(DOMAIN, getClass(), "status.succeeded", 1);
    }

    @Test public void timeMetricTest() {
        final TimeMetric meter = MetricsFactory.time(DOMAIN, getClass(), "timeTested");
        assertMetric(DOMAIN, getClass(), "timeTested", 0);
        meter.start();
        meter.stop();
        assertMetric(DOMAIN, getClass(), "timeTested", 1);
    }

    private void assertMetric(String namespace, Class<? extends MetricTest> aClass, String target, long i) {
        try {
            final String objectName = String.format("%s.%s:name=%s.%s.%s",
                    TEST_METRIC,
                    Constants.LOCALHOST,
                    namespace,
                    resolveSource(aClass.getName()),
                    target);
            assertThat(mBeanServer.getAttribute(new ObjectName(objectName), "Count")).isEqualTo(i);
        }
        catch (final Throwable e) {
            Assertions.fail("Unable to retrieve Reporter information", e);
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final String TEST_METRIC = "test-metric";
    private static final String DOMAIN      = "Test";
}  // end class MetricTest
