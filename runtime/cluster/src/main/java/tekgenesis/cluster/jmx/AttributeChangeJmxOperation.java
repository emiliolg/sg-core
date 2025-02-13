
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import javax.management.AttributeList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.jmx.JmxOperation;

/**
 * Change a MBean's attribute using JMX.
 */
public class AttributeChangeJmxOperation extends JmxOperation {

    //~ Instance Fields ..............................................................................................................................

    private final AttributeList attributeList;

    //~ Constructors .................................................................................................................................

    /**
     * Default Constructor.
     *
     * @param  objectName     ObjectName
     * @param  attributeList  AttributeList
     */
    public AttributeChangeJmxOperation(@NotNull String objectName, @NotNull AttributeList attributeList) {
        super(objectName);
        this.attributeList = attributeList;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void doExecute() {
        JmxInvokerImpl.invoker(getEndpoint()).mbean(getObjectName()).setAttributesValue(attributeList);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7035421035583401366L;
}
