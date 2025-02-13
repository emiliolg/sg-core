
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

// ...............................................................................................................................
//
// (C) Copyright  2011/2016 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import javax.management.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.AttributeChangeJmxOperation;
import tekgenesis.cluster.jmx.MethodInvokeJmxOperation;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.BaseEnvironment;
import tekgenesis.common.env.properties.LoggingProps;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvoker;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.DynamicConfiguration;
import tekgenesis.form.configuration.DynamicTypeConfiguration;
import tekgenesis.task.jmx.JmxConstants;
import tekgenesis.type.permission.PredefinedPermission;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Thread.currentThread;

import static tekgenesis.cluster.jmx.notification.Notifier.broadcast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Constants.CANCEL;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.util.Conversions.fromString;
import static tekgenesis.common.util.Primitives.wrapIfNeeded;
import static tekgenesis.console.ConfigurationFormBase.Field.LOGGER_LEVEL;
import static tekgenesis.console.Messages.ERROR_APPLYING_CHANGES;
import static tekgenesis.task.jmx.JmxConstants.*;

/**
 * User class for Form: ConfigurationForm
 */
public class ConfigurationForm extends ConfigurationFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(applyAttrChanges) is clicked. */
    @NotNull @Override public Action applyChanges() {
        final AttributeList attrs = new AttributeList();

        final FormTable<BeansAttsRow> beansAtts = getBeansAtts();

        final Seq<BeansAttsRow> map = filter(beansAtts, beansAttsRow -> beansAttsRow != null && beansAttsRow.isChanged());

        for (final BeansAttsRow attRow : map) {
            attrs.add(new Attribute(attRow.getAttName(), attRow.getAttValue()));
            attRow.setChanged(false);
        }

        try {
            final String serviceId = getSelectedServiceId();

            broadcast(new AttributeChangeJmxOperation(serviceId, attrs));

            return actions.getDefault().withMessage("Attributes successfully changed");
        }
        finally {
            setIsDirty(false);
        }
    }

    @NotNull @Override public Action applyLoggingChanges() {
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
            broadcast(new AttributeChangeJmxOperation(LOGGING_MBEAN_NAME, attrs));

            final FormTable<LoggersTableRow> loggersTable = getLoggersTable();
            for (final LoggersTableRow loggersTableRow : loggersTable) {
                final String loggerName = loggersTableRow.getLoggerName();

                if (loggersTableRow.isDefined(LOGGER_LEVEL)) {
                    final LOG_LEVEL loggerLevel = loggersTableRow.getLoggerLevel();

                    broadcast(
                        new MethodInvokeJmxOperation(LOG_BACK_NAME,
                            ConsoleConstants.SET_LOGGER_LEVEL,
                            new String[] { String.class.getCanonicalName(), String.class.getCanonicalName() },
                            new Object[] { loggerName, loggerLevel.name() }));
                }
            }
            // applying changes
            for (final ObjectName objectName : objectNameList)
                broadcast(new MethodInvokeJmxOperation(objectName.getCanonicalName(), APPLY, null, null));
        }
        catch (final MalformedObjectNameException e) {
            cancelChanges(objectNameList);
            logger.error(e);

            return actions.getError().withMessage(ERROR_APPLYING_CHANGES.label(e.getMessage()));
        }
        return actions.getDefault();
    }  // end method applyLoggingChanges

    @NotNull @Override public Action applyXNodeHeader() {
        try {
            final AttributeList attrs = new AttributeList();

            attrs.add(new Attribute(ADD_ID_REF_HEADER, Boolean.toString(isXNodeHeader())));
            broadcast(new AttributeChangeJmxOperation(LOGGING_MBEAN_NAME, attrs));

            // applying changes
            broadcast(new MethodInvokeJmxOperation(LOGGING_MBEAN_NAME, APPLY, null, null));

            return actions.getDefault();
        }
        catch (final Exception e) {
            logger.error(e);
            return actions.getError().withMessage("Error trying to Apply changes.");
        }
    }

    @NotNull @Override public Action cancelLoggingChanges() {
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

            cancelChanges(objectNameList);
            return actions.getDefault();
        }
        catch (final MalformedObjectNameException e) {
            logger.error(e);
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Error trying to cancel changes.");
        }
    }

    @NotNull @Override public Action filterLoggers() {
        getLoggersTable().clear();
        final RemoteMember remoteMember = getRemoteMember();

        final JmxEndpoint jmxEndpoint = remoteMember.getJmxEndpoint();

        final ArrayList<String> loggerList = JmxInvokerImpl.invoker(jmxEndpoint).mbean(LOG_BACK_NAME).getAttribute(ConsoleConstants.LOGGER_LIST);
        final String            prefix     = getLoogerSearch().toLowerCase();

        filter(loggerList, loggerName -> loggerName.toLowerCase().startsWith(prefix))  //
        .forEach(loggerName -> createLoggerRow(jmxEndpoint, loggerName));

        return actions.getDefault();
    }

    @NotNull @Override public Action filterProperties() {
        loadPropertiesTab(getSearchPropsBox());
        return actions.getDefault();
    }

    /** Invoked when table(servicesTab) is clicked. */
    @NotNull @Override public Action selectMBean() {
        try {
            setLoading(true);
            final String serviceId = getServicesTab().getCurrent().getServiceId();

            final RemoteMember remoteMember = getRemoteMember();

            final JmxEndpoint jmxEndpoint = remoteMember.getJmxEndpoint();

            final MBeanInfo mBeanInfo = JmxInvokerImpl.invoker(jmxEndpoint).mbean(serviceId).getInfo();

            fillAttributeTable(serviceId, jmxEndpoint, mBeanInfo);
            fillOperationTable(mBeanInfo);

            setSelectedServiceId(serviceId);

            return actions.getDefault();
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return actions.getError().withMessage("An error occurs trying get the list of availables MBeans. (" + e.getMessage() + "");
        }
        finally {
            setLoading(false);
        }
    }

    @NotNull @Override public Action tabSelection() {
        final int tab = getConfigurationTab();

        switch (tab) {
        case 0:
            final LoggingProps properties = Context.getProperties(LoggingProps.class);
            setXNodeHeader(properties.addIdRefResponseHeader);
            break;
        case 1:
            loadConfigurationTab();
            break;
        case 2:
            loadPropertiesTab(null);
            break;
        case 3:
            loadMBeans();
            break;
        }
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
        return actions.getDefault();
    }

    private void cancelChanges(List<ObjectName> objectNameList) {
        // cancel changes
        for (final ObjectName objectName : objectNameList)
            broadcast(new MethodInvokeJmxOperation(objectName.getCanonicalName(), CANCEL, null, null));
    }

    @SuppressWarnings("Duplicates")
    private void createLoggerRow(JmxEndpoint jmxConfiguration, String loggerName) {
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

    private void fillAttributeTable(@NotNull String mbean, @NotNull JmxEndpoint jmxConfiguration, @NotNull MBeanInfo mBeanInfo) {
        final MBeanAttributeInfo[] infos      = mBeanInfo.getAttributes();
        final String[]             attributes = new String[infos.length];

        for (int i = 0; i < infos.length; i++)
            attributes[i] = infos[i].getName();

        final AttributeList attributesValue = JmxInvokerImpl.invoker(jmxConfiguration).mbean(mbean).getAttributesValue(attributes);

        getBeansAtts().clear();

        final List<Attribute> asList = attributesValue.asList();
        for (int i = 0; i < asList.size(); i++) {
            final Attribute    attribute = asList.get(i);
            final BeansAttsRow row       = getBeansAtts().add();
            row.setAttName(attribute.getName());
            row.setIsWritable(infos[i].isWritable());
            if (attribute.getValue() != null) row.setAttValue(attribute.getValue());
            row.configure();
        }
    }

    private void fillOperationTable(@NotNull MBeanInfo mBeanInfo) {
        final MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        getBeansOps().clear();
        for (final MBeanOperationInfo operation : operations) {
            final BeansOpsRow row = getBeansOps().add();
            row.setOpName(operation.getName());
            final MBeanParameterInfo[] signature = operation.getSignature();
            final int                  cantArgs  = signature == null ? 0 : signature.length;
            row.setCantArgs(cantArgs);

            if (signature != null) {
                int                 argNro       = 1;
                final StringBuilder strSignature = new StringBuilder();
                for (final MBeanParameterInfo mBeanParameterInfo : signature) {
                    if (argNro < MAX_ARGS_COLS) {
                        final String type = mBeanParameterInfo.getType();

                        strSignature.append(type).append("|");

                        try {
                            final Method method = row.getClass().getMethod("setOpArg" + argNro, String.class);
                            method.invoke(row, "(" + type + ")");
                        }
                        catch (final Exception e) {
                            row.setOpRowStyle("red");
                            logger.error(e);
                        }
                        argNro++;
                    }
                    else {
                        row.setOpRowStyle("red");
                        logger.warning("The operation '" + row.getOpName() + " has more than '" + MAX_ARGS_COLS + "' arguments.");
                    }
                }
                row.setSignature(strSignature.toString());
            }
        }
    }

    private void loadConfigurationTab() {
        final RemoteMember remoteMember = getRemoteMember();

        if (remoteMember.getJmxEndpoint().isAvailable()) {
            final MBeanInfo loggingMbean = JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint()).mbean(LOGGING_MBEAN_NAME).getInfo();

            if (loggingMbean != null) {
                setLogInfoAvailable(true);

                final String[]             attrs      = new String[loggingMbean.getAttributes().length];
                final MBeanAttributeInfo[] attributes = loggingMbean.getAttributes();

                for (int i = 0; i < attributes.length; i++)
                    attrs[i] = attributes[i].getName();

                final JmxInvoker    invoker         = JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint());
                final AttributeList attributesValue = invoker.mbean(LOGGING_MBEAN_NAME).getAttributesValue(attrs);

                final Map<String, Object> attrValueMap = new HashMap<>();

                for (final Object o : attributesValue) {
                    final Attribute a = (Attribute) o;
                    attrValueMap.put(a.getName(), a.getValue());
                }

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

                final String loggers = invoker.mbean(LOGGING_MBEAN_NAME).getAttribute(ConsoleConstants.LOGGERS);

                final ImmutableList<String> loggersName = Strings.split(loggers, ',');
                getLoggersTable().clear();
                for (final String loggerName : loggersName) {
                    final LoggersTableRow add = getLoggersTable().add();

                    final Object level = invoker.mbean(CONFIGURATION_LOGGER_NAME + loggerName).getAttribute(JmxConstants.LEVEL);

                    if (level != null) {
                        final LOG_LEVEL log_level = LOG_LEVEL.valueOf(level.toString().toUpperCase());
                        add.setLoggerLevel(log_level);
                    }
                    add.setLoggerName(loggerName);
                }
            }
        }
    }  // end method loadConfigurationTab

    private void loadMBeans() {
        final RemoteMember remoteMember = getRemoteMember();

        if (remoteMember.getJmxEndpoint().isAvailable()) {
            final Seq<ObjectName> allBeans = seq(JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint()).mbean("").queryMBean("*:*"));

            final ImmutableCollection<ObjectName> sort = sorted(allBeans, ObjectName::compareTo);

            for (final ObjectName objectName : sort) {
                final ServicesTabRow row = getServicesTab().add();
                row.setServiceId(objectName.getCanonicalName());
                row.setDomain(objectName.getDomain());
                String type = objectName.getKeyProperty("type");

                if (isEmpty(type)) type = objectName.getKeyProperty("Type");
                if (isNotEmpty(type)) row.setType(type);

                String name = objectName.getKeyProperty("name");
                if (isEmpty(name)) name = objectName.getKeyProperty("Name");
                if (isNotEmpty(name)) row.setName(name);
            }
        }
    }

    private void loadPropertiesTab(@Nullable String filter) {
        final Environment environment = Context.getEnvironment();
        getPropertyTable().clear();

        final Map<String, BaseEnvironment.Entry<?>> envMap = Reflection.getPrivateField(environment, ConsoleConstants.VALUES);

        if (envMap != null) {
            for (final String key : envMap.keySet()) {
                final boolean mustAdd = filter == null || key.toLowerCase().contains(filter.toLowerCase());

                if (mustAdd) {
                    final BaseEnvironment.Entry<?> entry     = envMap.get(key);
                    final Object                   propValue = entry.getValue();
                    if (propValue != null) processPropertyFields(key, entry, propValue);
                }
            }
        }
    }

    private void processPropertyFields(String key, BaseEnvironment.Entry<?> entry, Object propValue) {
        final Set<java.lang.reflect.Field> publicFields = Reflection.getPublicFields(entry.getClazz());

        final String jmxEndpoint = getMBeanName(key);

        for (final java.lang.reflect.Field field : publicFields) {
            final Object           fieldValue = Reflection.getFieldValue(propValue, field);
            final PropertyTableRow row        = getPropertyTable().add();

            row.setAttrName(field.getName());
            row.setEndpoint(jmxEndpoint);
            row.setPropName(key + "." + field.getName());
            row.setClazz(entry.getClazz().getName());
            if (fieldValue != null) row.setPropValue(fieldValue.toString());
        }
    }

    private String getMBeanName(String key) {
        final String jmxEndpoint;
        final int    i = key.indexOf(".");
        if (i > 0) jmxEndpoint = String.format(ConsoleConstants.CONFIGURATION_MBEAN, key.substring(0, i), key.substring(i + 1));
        else jmxEndpoint = String.format(ConsoleConstants.DEFAULT_CONFIGURATION_MBEAN, key);
        return jmxEndpoint;
    }

    /** @return  the first available member in the cluster */
    private RemoteMember getRemoteMember() {
        final RemoteCluster cluster = Context.getSingleton(Clusters.class).getActiveCluster().get();

        // Get first member
        return cluster.getMembers().get(0);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger        = getLogger(ConfigurationForm.class);
    private static final int    MAX_ARGS_COLS = 6;

    //~ Inner Classes ................................................................................................................................

    public class BeansAttsRow extends BeansAttsRowBase {
        /** Configure dynamic row. */
        public void configure() {
            final DynamicConfiguration     configuration     = configuration(Field.ATT_VALUE);
            final DynamicTypeConfiguration typeConfiguration = configuration.getTypeConfiguration();

            final Object attValue = isDefined(Field.ATT_VALUE) ? getAttValue() : null;

            if (attValue == null || attValue instanceof String || attValue instanceof DateTime) typeConfiguration.stringType();
            if (attValue instanceof Integer) typeConfiguration.intType();
            if (attValue instanceof Double || attValue instanceof Float) typeConfiguration.decimalType();
            if (attValue instanceof BigDecimal) typeConfiguration.realType();
            if (attValue instanceof Boolean) typeConfiguration.booleanType();
            if (attValue instanceof DateOnly) typeConfiguration.dateType();
            else {
                typeConfiguration.stringType();
                if (attValue != null) setAttValue(getAttValue().toString());
            }
        }
        /** Invoked when dynamic(attValue) value changes. */
        @NotNull @Override public Action markAttrsChanged() {
            if (!isLoading()) {
                setChanged(true);
                setIsDirty(true);
            }
            return actions.getDefault();
        }
    }  // end class BeansAttsRow

    public class BeansOpsRow extends BeansOpsRowBase {
        /** Invoked when button(opInvoke) is clicked. */
        @NotNull @Override public Action invokeOperation() {
            final String methodName = getOpName();

            try {
                final String serviceId = getSelectedServiceId();

                final String currentSignature = getSignature();
                String[]     signature        = null;
                Object[]     params           = null;
                if (isNotEmpty(currentSignature)) {
                    signature = currentSignature.split("\\|");
                    params    = new Object[getCantArgs()];
                    for (int idx = 0; idx < getCantArgs(); idx++) {
                        final String type   = wrapIfNeeded(signature[idx]);
                        final Method method = getClass().getMethod("getOpArg" + (idx + 1));
                        method.invoke(this);
                        params[idx] = fromString(getOpArg1(), Class.forName(type, true, currentThread().getContextClassLoader()));
                    }
                }

                broadcast(new MethodInvokeJmxOperation(serviceId, methodName, signature, params));

                return actions.getDefault().withMessage("Operation successfully Invoked");
            }
            catch (final Exception e) {
                logger.error(e);
                return actions.getError()
                       .withMessage("An error occurred trying to invoke the operation '" + methodName + "' (" + e.getMessage() + ")");
            }
        }
    }

    public class LoggersTableRow extends LoggersTableRowBase {
        @NotNull @Override public Action applyLogLevel() {
            final Action action = actions.getDefault();
            if (!isLoggerDirtyValue())

                broadcast(
                    new MethodInvokeJmxOperation(LOG_BACK_NAME,
                        ConsoleConstants.SET_LOGGER_LEVEL,
                        new String[] { String.class.getCanonicalName(), String.class.getCanonicalName() },
                        new Object[] { getLoggerName(), getLoggerLevel().name() }));
            return action;
        }
    }

    public class PropertyTableRow extends PropertyTableRowBase {
        @NotNull @Override public Action editPropertyValue() {
            final Option<PropertyTableRow> current = getPropertyTable().current();
            if (current.isPresent()) setEditProp(true);

            return actions.getDefault();
        }
        @NotNull @Override public Action updatePropertyValue() {
            if (isEditProp()) {
                setEditProp(false);

                final PropertyTableRow current = getPropertyTable().getCurrent();
                final String[]         parts   = current.getPropName().split("\\.");
                final String           scope   = parts.length > 2 ? parts[0] : "";

                try {
                    final Class<?>           aClass        = Class.forName(current.getClazz(), true, Thread.currentThread().getContextClassLoader());
                    final List<RemoteMember> remoteMembers = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();

                    remoteMembers.forEach((m) -> m.updateProperty(scope, aClass, current.getAttrName(), current.getPropValue()));
                }
                catch (final ClassNotFoundException e) {
                    logger.error(e);
                    return actions.getError().withMessage("Unable to updated property ");
                }
            }
            return actions.getDefault();
        }
    }

    public class ServicesTabRow extends ServicesTabRowBase {}
}  // end class ConfigurationForm
