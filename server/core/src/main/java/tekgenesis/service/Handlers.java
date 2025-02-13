
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import javax.servlet.ServletRequest;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.service.server.Request;
import tekgenesis.common.service.server.Response;
import tekgenesis.common.util.Reflection;
import tekgenesis.security.shiro.web.URLConstants;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.type.InterfaceType;

/**
 * Service Handlers utility class.
 */
public class Handlers {

    //~ Constructors .................................................................................................................................

    private Handlers() {}

    //~ Methods ......................................................................................................................................

    /** Initialize handler factory instance. */
    @NotNull public static Factory factory(@NotNull Request request, @NotNull Response response, @NotNull ReferenceSupplier<Forwarder> supplier) {
        final HtmlBuilder        htmlBuilder = Context.getSingleton(HtmlBuilder.class);
        final InterfaceTypeProxy proxy       = InterfaceTypeProxy.getFromRequest(request);
        return new FactoryImpl(htmlBuilder, proxy, request, response, supplier);
    }

    /** Initialize handler instance for user usage. */
    public static Reflection.Instance initialize(@NotNull String handlerClass, @NotNull Factory factory) {
        return Reflection.Instance.create(handlerClass, factory);
    }

    /** Return expected InterfaceTypeProxy on request. */
    public static <T extends InterfaceType> InterfaceTypeProxy getFromRequest(ServletRequest request) {
        return (InterfaceTypeProxy) request.getAttribute(URLConstants.INTERFACE_TYPE_PROXY);
    }
}
