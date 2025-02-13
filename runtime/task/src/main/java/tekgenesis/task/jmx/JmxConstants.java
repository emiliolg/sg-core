
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.jmx;

/**
 * JMX Constants.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface JmxConstants {

    //~ Instance Fields ..............................................................................................................................

    /**  */
    String ADD_ID_REF_HEADER = "addIdRefResponseHeader";

    /**  */
    String APPLY = "apply";

    String CACHES  = "tekgenesis.cluster:Name=Caches,type=runtime";
    String CLUSTER = "tekgenesis.cluster:Name=Cluster,type=runtime";

    String CONFIGURATION_LOGGER_NAME = "tekgenesis.configuration:type=logger,Name=";

    /**  */
    String CONSOLE_OUTPUT = "consoleOutput";
    /**  */
    String CONTEXT_NAME = "contextName";
    /**  */
    String DEBUG_ALL = "debugAll";
    /**  */
    String   DEBUG_TEK    = "debugTekgenesis";
    int      DEFAULT_PORT = 9500;
    Object[] EMPTY_ARGS   = {};

    String[] EMPTY_SIGNATURE = {};
    String   ENTITIES        = "tekgenesis.cluster:Name=Entities,type=runtime";
    /**  */
    String FILE_OUTPUT = "fileOutput";
    /**  */
    String GELF_FACILITY = "gelfFacility";
    /**  */
    String GELF_OUTPUT = "gelfOutput";
    /**  */
    String GELF_PORT = "gelfServerPort";
    /**  */
    String GELF_SERVER = "gelfServer";

    String GET_MEMBERS = "Members";

    /** Hikari Mbean name.* */
    String HIKARI_MBEAN = "com.zaxxer.hikari:type=Pool ()";

    /** Hikari MBEANs. */
    String HIKARI_MBEANS_QUERY = HIKARI_MBEAN + "*";

    /** Jetty MBEAN. */
    String JETTY_NODE = "org.eclipse.jetty.server:type=server,id=0";

    /** Level.* */
    String LEVEL = "level";

    String LOG_BACK_NAME = "ch.qos.logback.classic:Name=default,Type=ch.qos.logback.classic.jmx.JMXConfigurator";
    /**  */
    String LOG_DIR = "logDir";
    /**  */
    String LOG_FILENAME = "logFileName";

    /** Logging MBEAN. */
    String LOGGING_MBEAN_NAME = "tekgenesis.configuration:Name=default,type=logging";
    /**  */
    String MAX_DAYS = "maxDays";
    /**  */
    String MAX_FILESIZE = "maxFileSize";
    String MEMOS        = "tekgenesis.cluster:Name=Memos,type=runtime";
    String NODE         = "tekgenesis.cluster:Name=Node,type=runtime";

    String RESTART = "restart";

    /**  */
    String ROOT_LOG_LEVEL = "rootLoggerLevel";
    String RUNTIME        = "runtime";

    String SAFE_MODE = "safeMode";

    String START = "start";
    String STOP  = "stop";

    String TEKGENESIS_DAEMON_NAME = "tekgenesis.daemon:Name=";
    String UPDATE_PROPERTY        = "updateProperty";
    String UPGRADE                = "upgrade";
    /**  */
    String XML_OUTPUT = "xmlOutput";
}  // end interface JmxConstants
