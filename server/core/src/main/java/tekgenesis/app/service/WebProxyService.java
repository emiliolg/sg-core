
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.app.properties.ProxyProps;
import tekgenesis.common.core.Constants;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;
import tekgenesis.websocket.server.http.HttpForwarder;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * WebProxyService.
 */
public class WebProxyService extends Service {

    //~ Instance Fields ..............................................................................................................................

    private final List<HttpForwarder> httpForwarders = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    /** Create the service. */
    public WebProxyService(final ServiceManager serviceManager) {
        super(serviceManager, SERVICE_NAME, SERVICE_START_ORDER, ProxyProps.class);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void doShutdown() {
        unregister();
        for (final HttpForwarder httpForwarder : httpForwarders) {
            try {
                httpForwarder.stop();
            }
            catch (final Exception e) {
                // ignore
            }
        }
    }

    @Override protected void doStart() {
        final ApplicationProps config = getEnv().get(ApplicationProps.class);
        if (isNotEmpty(config.webProxyUrl)) {
            @SuppressWarnings("DuplicateStringLiteralInspection")
            final String[] urls = config.webProxyUrl.split(";");
            for (final String url : urls)
                registerForwarder(url, config.webProxyTimeout);
        }
        register();
    }

    private void notifyProxy(boolean enable) {
        final ProxyProps properties = getEnv().get(ProxyProps.class);
        if (!isEmpty(properties.host) && !isEmpty(properties.backend)) {
            try {
                final Socket      socket = new Socket(properties.host, properties.port);
                final PrintWriter out    = new PrintWriter(socket.getOutputStream(), true);
                // noinspection DuplicateStringLiteralInspection
                out.println((enable ? "enable" : "disable") + " server " + properties.backend + "/" + InetAddress.getLocalHost().getHostName());
                out.flush();
                out.close();
                socket.close();
            }
            catch (final IOException e) {
                logger.error(e);
            }
        }
    }

    private void register() {
        notifyProxy(true);
    }

    private void registerForwarder(String proxyUrl, int timeout) {
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final HttpForwarder httpForwarder = new HttpForwarder(proxyUrl,
                "http://localhost:" + System.getProperty(Constants.PORT_OPT, "8080"),
                timeout);
        try {
            httpForwarders.add(httpForwarder);
            httpForwarder.start();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void unregister() {
        notifyProxy(false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -602900887366484650L;

    private static final String SERVICE_NAME        = WebProxyService.class.getSimpleName();
    private static final int    SERVICE_START_ORDER = 6;
}  // end class WebProxyService
