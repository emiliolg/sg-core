
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.core;

/**
 * Metrics register.
 */
public final class MetricsFactory {

    //~ Constructors .................................................................................................................................

    private MetricsFactory() {}

    //~ Methods ......................................................................................................................................

    /**
     * Register a new Call Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace    namespace for the schema
     * @param   targetClass  it's name will be used as target name for the schema
     * @param   name         action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static CallMetric call(String namespace, Class<?> targetClass, String name) {
        return call(namespace, targetClass.getName(), name);
    }
    /**
     * Register a new Time Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace  namespace for the schema
     * @param   target     target name for the schema
     * @param   name       action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static CallMetric call(String namespace, String target, String name) {
        return new CallMetric(namespace, target, name);
    }

    /**
     * Register a new Meter Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace    namespace for the schema
     * @param   targetClass  it's name will be used as target name for the schema
     * @param   name         action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static MeterMetric meter(String namespace, Class<?> targetClass, String name) {
        return meter(namespace, targetClass.getName(), name);
    }

    /**
     * Register a new Meter Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace  namespace for the schema
     * @param   target     target name for the schema
     * @param   name       action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static MeterMetric meter(String namespace, String target, String name) {
        return new MeterMetric(namespace, target, name);
    }

    /**
     * Register a new Time Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace  namespace for the schema
     * @param   target     target name for the schema
     * @param   name       action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static TimeMetric time(String namespace, String target, String name) {
        return new TimeMetric(namespace, target, name);
    }

    /**
     * Register a new Time Metric. This Metric will be reported using a schema defined by the
     * parameters. The schema will be of the form: <hostname>.<namespace>.<instrumented
     * section>.<target (noun)>.<action (past tense verb)>
     *
     * @param   namespace    namespace for the schema
     * @param   targetClass  it's name will be used as target name for the schema
     * @param   name         action name for the schema
     *
     * @return  The newly created Meter Metric
     */
    public static TimeMetric time(String namespace, Class<?> targetClass, String name) {
        return time(namespace, targetClass.getName(), name);
    }
}  // end class MetricsFactory
