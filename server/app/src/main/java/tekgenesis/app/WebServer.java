
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;

import ch.qos.logback.classic.Logger;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.spdy.api.SPDY;
import org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnectionFactory;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jetbrains.annotations.Nullable;

import static java.lang.System.getProperty;

import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.classic.Level.toLevel;

import static org.slf4j.LoggerFactory.getLogger;

import static tekgenesis.app.Util.setPropertiesOrExit;
import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * SuiGeneris Web Server.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MethodWithTooManyParameters" })
class WebServer {

    //~ Constructors .................................................................................................................................

    private WebServer() {}

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("MethodWithTooManyParameters")
    static void run(final String applicationDir, final String runDir, final int port, final boolean resetDb, final String classPath,
                    final String sgProperties, @Nullable final String httpsSpec, final int httpsPort, final int threads, final String safeMode)
        throws Exception
    {
        // Jetty log level.
        ((Logger) getLogger("org.eclipse.jetty")).setLevel(toLevel(getProperty("org.eclipse.jetty.LEVEL"), INFO));
        ((Logger) getLogger("org.apache.commons")).setLevel(INFO);

        // Configure Thread Pool.
        final QueuedThreadPool threadPool = new QueuedThreadPool(threads);

        // Create Server.
        final Server server = new Server(threadPool);

        // Configure Connector.
        final ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Enable parsing of parts of web.xml
        final Configuration.ClassList classList = Configuration.ClassList.setServerDefault(server);
        classList.addAfter(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());

        if (isNotEmpty(httpsSpec)) enableHttps(server, httpsSpec, httpsPort);

        final File webAppDir = new File(applicationDir);

        setPropertiesOrExit(webAppDir, runDir, port, resetDb, safeMode, sgProperties, httpsPort);

        final WebAppContext context = new WebAppContext();
        context.setWar(applicationDir);
        context.setContextPath("/");
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        setClassPath(context, classPath, new File(webAppDir, "/lib"));

        server.setHandler(context);

        final MBeanServer    mBeanServer    = ManagementFactory.getPlatformMBeanServer();
        final MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        server.addEventListener(mBeanContainer);

        final AtomicInteger exitCode = new AtomicInteger();
        exitCode.set(0);

        server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
                @Override public void lifeCycleStarted(LifeCycle event) {
                    if (event.isRunning()) {
                        try {
                            final String hostName = InetAddress.getLocalHost().getHostName();
                            System.out.println("Application available at:\nhttp://" + hostName + ":" + connector.getPort());
                            // noinspection SpellCheckingInspection
                            if (server.getConnectors().length > 1 &&
                                "SSL-alpn".equals(server.getConnectors()[1].getDefaultConnectionFactory().getProtocol()))

                                System.out.println("\tor\nhttps://" + hostName + ":" + ((ServerConnector) server.getConnectors()[1]).getPort());
                            System.out.println();
                        }
                        catch (final UnknownHostException e)
                        {
                            // ignore
                        }
                    }
                }

                @Override public void lifeCycleStopped(LifeCycle event) {
                    exitCode.set(RESTART_CODE);
                }
            });

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    server.stop();
                }
                catch (final Exception e) {
                    System.err.println(e.getMessage());
                }
            }));
        server.join();

        System.exit(exitCode.get());
    }  // end method run

    @SuppressWarnings("SpellCheckingInspection")
    private static void enableHttps(final Server server, final String httpsSpec, final int port) {
        final String[] s = httpsSpec.split(":");
        if (s.length != 3) {
            System.out.printf("Wrong https specification %s%n", httpsSpec);
            System.exit(1);
        }

        final SslContextFactory factory = new SslContextFactory(s[0]);
        factory.setKeyStorePassword(s[1]);
        factory.setKeyManagerPassword(s[2]);
        factory.setProtocol("TLSv1");
        final HttpConfiguration httpConfig = new HttpConfiguration();

        final SslConnectionFactory        ssl  = new SslConnectionFactory(factory, "alpn");
        final ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory("spdy/3", "http/1.1");
        alpn.setDefaultProtocol("http/1.1");
        final HTTPSPDYServerConnectionFactory spdy = new HTTPSPDYServerConnectionFactory(SPDY.V3, httpConfig);
        httpConfig.addCustomizer(new SecureRequestCustomizer());
        final HttpConnectionFactory http = new HttpConnectionFactory(httpConfig);

        final ServerConnector connector = new ServerConnector(server, ssl, alpn, spdy, http);

        connector.setPort(port);  // your port
        server.addConnector(connector);
    }

    @SuppressWarnings("Duplicates")
    private static void setClassPath(final WebAppContext context, final String classPath, final File libDir) {
        final File[] extraJars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

        final StringBuilder builder = new StringBuilder();
        builder.append(classPath.replace(File.pathSeparator, ";"));
        if (extraJars != null) {
            boolean notFirst = !builder.toString().isEmpty();
            for (final File extraJar : extraJars) {
                if (notFirst) builder.append(';');
                else notFirst = true;
                builder.append(extraJar.getAbsolutePath());
            }
        }
        context.setExtraClasspath(builder.toString());
    }

    //~ Static Fields ................................................................................................................................

    private static final int RESTART_CODE = 124;
}  // end class WebServer
