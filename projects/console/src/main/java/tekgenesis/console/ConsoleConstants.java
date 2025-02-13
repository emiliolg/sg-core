
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

/**
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface ConsoleConstants {

    //~ Instance Fields ..............................................................................................................................

    String ACTIVE_CONNECTIONS          = "ActiveConnections";
    String APPLY                       = "apply";
    String CACHE_DUMP_FILE_FORMAT      = "%s-%s-%s_dump.csv";
    String CLEAR_CACHE                 = "clearCache";
    String CONFIGURATION_MBEAN         = "tekgenesis.configuration:Name=%s,type=%s";
    String CPU_USAGE                   = "cpuUsage";
    String DEFAULT_CONFIGURATION_MBEAN = "tekgenesis.configuration:Name=default,type=%s";
    String GET_LOGGER_LEVEL            = "getLoggerLevel";
    String IDLE_CONNECTIONS            = "IdleConnections";
    String LOGGER_LIST                 = "LoggerList";
    String LOGGERS                     = "loggers";
    String MEMORY_SUMMARY              = "Committed :%s <br> Init : %s <br> Used : %s<br> Max : %s";
    String MEMORY_USAGE                = "memoryUsage";
    String RESET_STATS                 = "resetStats";
    String SET_LOGGER_LEVEL            = "setLoggerLevel";
    String SET_USAGE_NODE              = "setUsgNode";
    String SUSPEND                     = "suspend";
    String THREAD_DUMP_FILE_FORMAT     = "%s-%s_dump.txt";
    String THREAD_WAITING_CONNECTIONS  = "ThreadsAwaitingConnection";
    String TIME_FORMAT                 = "HH:mm:ss";
    String TOTAL_CONNECTIONS           = "TotalConnections";
    String VALUES                      = "values";
}
