
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

/**
 * ServiceMBean.
 */
public interface ServiceMBean {

    //~ Methods ......................................................................................................................................

    /** disable service. */
    void disable();

    /** enable service. */
    void enable();

    /** start service. */
    void start();

    /** stop service. */
    void stop();

    /** @return  true if the service is enabled */
    boolean isEnabled();

    /** @return  true if the service is running */
    boolean isRunning();

    /** @return  the service name */
    String getName();
}
