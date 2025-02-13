
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Strings;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvoker;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.task.jmx.JmxConstants;

import static java.lang.Boolean.parseBoolean;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.CANCEL;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.console.Messages.ERROR_APPLYING_CHANGES;
import static tekgenesis.task.jmx.JmxConstants.*;

/**
 * User class for Form: LoggerConfiguration
 */
public class LoggerConfiguration extends LoggerConfigurationBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(applyChange) is clicked. */
    @NotNull @Override public Action applyLoggingChanges() {
        final RemoteMember remoteMember = getMember();
        if (remoteMember == null) return actions.getDefault();

        final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();

        final AttributeList attrs = new AttributeList();

        attrs.add(new Attribute(ROOT_LOG_LEVEL, getRootLoggerLevel().name()));
        attrs.add(new Attribute(LOG_FILENAME, getLogFileName()));
        attrs.add(new Attribute(MAX_FILESIZE, getMaxFileSize()));
        attrs.add(new Attribute(LOG_DIR, getLogDir()));
        attrs.add(new Attribute(MAX_DAYS, getMaxDays()));
        attrs.add(new Attribute(CONSOLE_OUTPUT, Boolean.toString(isConsoleOutput())));
        attrs.add(new Attribute(XML_OUTPUT, Boolean.toString(isXmlOutput())));
        attrs.add(new Attribute(FILE_OUTPUT, Boolean.toString(isFileOutput())));
        attrs.add(new Attribute(DEBUG_ALL, Boolean.toString(isDebugAll())));
        attrs.add(new Attribute(DEBUG_TEK, Boolean.toString(isDebugTekgenesis())));
        attrs.add(new Attribute(CONTEXT_NAME, getContextName()));
        attrs.add(new Attribute(GELF_SERVER, getGelfServer()));
        attrs.add(new Attribute(GELF_PORT, getGelfServerPort()));
        attrs.add(new Attribute(GELF_FACILITY, getGelfFacility()));
        attrs.add(new Attribute(GELF_OUTPUT, Boolean.toString(isGelfOutput())));

        final List<ObjectName> objectNameList = new ArrayList<>();
        try {
            final ObjectName configObjectName = new ObjectName(LOGGING_MBEAN_NAME);
            objectNameList.add(configObjectName);

            final JmxInvoker invoker = JmxInvokerImpl.invoker(jmxConfiguration);

            invoker.mbean(LOGGING_MBEAN_NAME).setAttributesValue(attrs);

            final FormTable<LoggersTableRow> loggersTable = getLoggersTable();
            for (final LoggersTableRow loggersTableRow : loggersTable) {
                final String    loggerName  = loggersTableRow.getLoggerName();
                final LOG_LEVEL loggerLevel = loggersTableRow.getLoggerLevel();

                final AttributeList attrsList = new AttributeList();
                attrsList.add(new Attribute(JmxConstants.LEVEL, loggerLevel.name()));

                final String     name       = JmxConstants.CONFIGURATION_LOGGER_NAME + loggerName;
                final ObjectName objectName = new ObjectName(name);

                invoker.mbean(name).setAttributesValue(attrsList);
                objectNameList.add(objectName);
            }
            // applying changes
            for (final ObjectName objectName : objectNameList)
                invoker.mbean(objectName.getCanonicalName()).invoke(APPLY, null, null);
        }
        catch (final MalformedObjectNameException e) {
            cancelChanges(jmxConfiguration, objectNameList);
            logger.error(e);
            return actions.getError().withMessage(ERROR_APPLYING_CHANGES.label(e.getMessage()));
        }
        return actions.getDefault();
    }  // end method applyLoggingChanges

    /** Invoked when button(cancelChanges) is clicked. */
    @NotNull @Override public Action cancelLoggingChanges() {
        final RemoteMember remoteMember = getMember();
        if (remoteMember == null) return actions.getDefault();

        final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();

        try {
            final ObjectName       configObjectName = new ObjectName(LOGGING_MBEAN_NAME);
            final List<ObjectName> objectNameList   = new ArrayList<>();
            objectNameList.add(configObjectName);

            final FormTable<LoggersTableRow> loggersTable = getLoggersTable();

            for (final LoggersTableRow loggersTableRow : loggersTable) {
                final String     loggerName = loggersTableRow.getLoggerName();
                final ObjectName objectName = new ObjectName(CONFIGURATION_LOGGER_NAME + loggerName);
                objectNameList.add(objectName);
            }

            cancelChanges(jmxConfiguration, objectNameList);
            return actions.getDefault();
        }
        catch (final MalformedObjectNameException e) {
            logger.error(e);
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Error trying to cancel changes.");
        }
    }

    /** Invoked when text_field(loggerSearch) value changes. */
    @NotNull @Override
    @SuppressWarnings("Duplicates")
    public Action filterLoggers() {
        getLoggersTable().clear();

        final RemoteMember remoteMember = getMember();
        if (remoteMember == null) return actions.getDefault();

        final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();

        final ArrayList<String> loggerList = JmxInvokerImpl.invoker(jmxConfiguration).mbean(LOG_BACK_NAME).getAttribute(ConsoleConstants.LOGGER_LIST);

        for (final String loggerName : loggerList) {
            if (loggerName.toLowerCase().startsWith(getLoggerSearch().toLowerCase())) {
                final LoggersTableRow row = getLoggersTable().add();

                final String level = JmxInvokerImpl.invoker(jmxConfiguration)
                                     .mbean(LOG_BACK_NAME)
                                     .invoke(ConsoleConstants.GET_LOGGER_LEVEL,
                        new String[] { String.class.getCanonicalName() },
                        new Object[] { loggerName });
                row.setLoggerName(loggerName);
                if (isNotEmpty(level)) {
                    row.setLoggerDirtyValue(true);
                    final LOG_LEVEL logLevel = LOG_LEVEL.valueOf(level);
                    row.setLoggerLevel(logLevel);
                    row.setLoggerDirtyValue(false);
                }
            }
        }

        return actions.getDefault();
    }

    @Override public void loadConfiguration() {
        if (!isDefined(Field.CURRENT_NODE)) return;

        final RemoteMember remoteMember = getMember();
        if (remoteMember == null) return;

        final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();
        if (jmxConfiguration.isAvailable()) {
            final MBeanInfo loggingMbean = JmxInvokerImpl.invoker(jmxConfiguration).mbean(LOGGING_MBEAN_NAME).getInfo();

            if (loggingMbean != null) {
                setLogInfoAvailable(true);

                final String[]             attrs      = new String[loggingMbean.getAttributes().length];
                final MBeanAttributeInfo[] attributes = loggingMbean.getAttributes();

                for (int i = 0; i < attributes.length; i++)
                    attrs[i] = attributes[i].getName();

                final JmxInvoker    invoker         = JmxInvokerImpl.invoker(jmxConfiguration);
                final AttributeList attributesValue = invoker.mbean(LOGGING_MBEAN_NAME).getAttributesValue(attrs);

                final Map<String, Object> attrValueMap = new HashMap<>();

                for (final Object o : attributesValue) {
                    final Attribute a = (Attribute) o;
                    attrValueMap.put(a.getName(), a.getValue());
                }

                setLogAttributes(attrValueMap);

                final Object loggers = invoker.mbean(LOGGING_MBEAN_NAME).getAttribute(ConsoleConstants.LOGGERS);

                final ImmutableList<String> loggersName = Strings.split((String) loggers, ',');
                getLoggersTable().clear();
                for (final String loggerName : loggersName) {
                    final LoggersTableRow add = getLoggersTable().add();

                    final Object level = invoker.mbean(CONFIGURATION_LOGGER_NAME + loggerName).getAttribute(LEVEL);

                    if (level != null) {
                        final LOG_LEVEL log_level = LOG_LEVEL.valueOf(level.toString().toUpperCase());
                        add.setLoggerLevel(log_level);
                    }
                    add.setLoggerName(loggerName);
                }
            }
        }
    }  // end method loadConfiguration

    @NotNull @Override public Object populate() {
        return getCurrentNode();
    }

    private void cancelChanges(JmxEndpoint connection, List<ObjectName> objectNameList) {
        // cancel changes
        for (final ObjectName objectName : objectNameList)
            JmxInvokerImpl.invoker(connection).mbean(objectName.getCanonicalName()).invoke(CANCEL, null, null);
    }

    private void setLogAttributes(Map<String, Object> attrValueMap) {
        final String loggerLevel = (String) attrValueMap.get(ROOT_LOG_LEVEL);
        setRootLoggerLevel(LOG_LEVEL.valueOf(loggerLevel.toUpperCase()));
        setLogFileName((String) attrValueMap.get(LOG_FILENAME));
        setMaxFileSize((String) attrValueMap.get(MAX_FILESIZE));
        setLogDir((String) attrValueMap.get(LOG_DIR));
        setMaxDays((String) attrValueMap.get(MAX_DAYS));
        setConsoleOutput(parseBoolean((String) attrValueMap.get(CONSOLE_OUTPUT)));
        setXmlOutput(parseBoolean((String) attrValueMap.get(XML_OUTPUT)));
        setFileOutput(parseBoolean((String) attrValueMap.get(FILE_OUTPUT)));
        setDebugAll(parseBoolean((String) attrValueMap.get(DEBUG_ALL)));
        setDebugTekgenesis(parseBoolean((String) attrValueMap.get(DEBUG_TEK)));
        setContextName((String) attrValueMap.get(CONTEXT_NAME));
        setGelfServer((String) attrValueMap.get(GELF_SERVER));
        setGelfServerPort((String) attrValueMap.get(GELF_PORT));
        setGelfFacility((String) attrValueMap.get(GELF_FACILITY));
        setGelfOutput(parseBoolean((String) attrValueMap.get(GELF_OUTPUT)));
    }

    @Nullable private RemoteMember getMember() {
        final List<RemoteMember> remoteMembers = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();
        for (final RemoteMember remoteMember : remoteMembers)
            if (remoteMember.getName().equals(getCurrentNode())) return remoteMember;

        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(LoggerConfiguration.class);

    //~ Inner Classes ................................................................................................................................

    public class LoggersTableRow extends LoggerConfigurationBase.LoggersTableRowBase {
        @NotNull @Override public Action applyLogLevel() {
            final RemoteMember remoteMember = getMember();
            if (remoteMember == null) return actions.getDefault();

            final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();
            final Action      action           = actions.getDefault();
            if (!isLoggerDirtyValue())

                JmxInvokerImpl.invoker(jmxConfiguration)
                    .mbean(LOG_BACK_NAME)
                    .invoke(ConsoleConstants.SET_LOGGER_LEVEL,
                        new String[] { String.class.getCanonicalName(), String.class.getCanonicalName() },
                        new Object[] { getLoggerName(), getLoggerLevel().name() });
            return action;
        }
    }
}  // end class LoggerConfiguration
