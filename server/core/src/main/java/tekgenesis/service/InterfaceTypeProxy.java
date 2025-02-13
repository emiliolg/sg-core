
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.server.Request;
import tekgenesis.metadata.handler.RouteMatch;
import tekgenesis.security.shiro.web.URLConstants;
import tekgenesis.type.InterfaceType;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * InterfaceType invocation proxy.
 */
public class InterfaceTypeProxy implements InvocationHandler {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Object> map = new HashMap<>();

    //~ Methods ......................................................................................................................................

    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        final String name = method.getName();

        if (isGetter(name)) return map.get(getFieldName(name));

        if (isSetter(name)) return map.put(getFieldName(name), args[0]);

        logger.error(String.format("Cannot handle method '%s' on interface proxy.", name));

        return null;
    }

    /** Proxy given type. */
    @NotNull public <T extends InterfaceType> T proxy(@NotNull Class<T> context) {
        return cast(Proxy.newProxyInstance(context.getClassLoader(), new Class<?>[] { context }, this));
    }

    private String getFieldName(String name) {
        final int beginIndex = name.startsWith("is") ? 2 : 3;
        return name.substring(beginIndex);
    }

    private boolean isGetter(String name) {
        return name.startsWith("get") || name.startsWith("is");
    }

    private boolean isSetter(String name) {
        return name.startsWith("set");
    }

    //~ Methods ......................................................................................................................................

    /** Return expected InterfaceTypeProxy on request. */
    public static InterfaceTypeProxy getFromRequest(@NotNull Request request) {
        return (InterfaceTypeProxy) request.getAttribute(URLConstants.INTERFACE_TYPE_PROXY);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(InterfaceTypeProxy.class);

    //~ Inner Interfaces .............................................................................................................................

    public interface RoutingContext extends InterfaceType {
        /** Get RouteMatch from context. */
        RouteMatch getRouteMatch();
        /** Set RouteMatch in context. */
        void setRouteMatch(@NotNull RouteMatch routeMatch);
    }
}  // end class InterfaceTypeProxy
