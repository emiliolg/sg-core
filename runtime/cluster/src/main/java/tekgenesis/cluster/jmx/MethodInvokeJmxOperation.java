
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.jmx.JmxOperation;

/**
 * Invoke a MBean's method using JMX.
 */
public class MethodInvokeJmxOperation extends JmxOperation {

    //~ Instance Fields ..............................................................................................................................

    private final String   methodName;
    private final Object[] params;

    private final String[] signature;

    //~ Constructors .................................................................................................................................

    /** default constructor. */
    public MethodInvokeJmxOperation(@NotNull String objectName, @NotNull String methodName, @Nullable String[] signature,
                                    @Nullable final Object[] params) {
        super(objectName);
        this.methodName = methodName;
        this.signature  = signature;
        this.params     = params;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void doExecute() {
        JmxInvokerImpl.invoker(getEndpoint()).mbean(getObjectName()).invoke(methodName, signature, params);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 874924523233867906L;
}
