
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.util.ArrayList;
import java.util.Collection;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Strings;
import tekgenesis.lang.mm.MMModuleComponent;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MM Run configuration. It extends the java run configuration
 */
public class MMRunConfiguration extends MMRunConfigurationBase {

    //~ Instance Fields ..............................................................................................................................

    private boolean autoLogin                = true;
    private String  autologinPass            = Constants.SHIRO_ADMIN_PASS;
    private String  autologinUser            = Constants.SHIRO_ADMIN_USER;
    private boolean enableLifecycleExecution = true;

    private boolean enableTaskExecution = false;

    private boolean launchBrowser = true;
    private String  port          = "8080";

    //~ Constructors .................................................................................................................................

    /** Constructor that receives a module and aconfiguration factory. */
    public MMRunConfiguration(JavaRunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        this("SuiGeneris Run", runConfigurationModule, factory);
    }

    /** Constructor that receives the config name, a module and a configuration factory. */
    protected MMRunConfiguration(String name, JavaRunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        super(name, runConfigurationModule, factory);
    }

    //~ Methods ......................................................................................................................................

    @Override public void checkConfiguration()
        throws RuntimeConfigurationException
    {
        super.checkConfiguration();

        if (!Strings.isNumeric(getPort())) throw new RuntimeConfigurationError(MSGS.invalidPortNumber());
    }

    @Override public void readExternal(Element element)
        throws InvalidDataException
    {
        super.readExternal(element);
        port = readOption(PORT, element, "8080");

        enableTaskExecution      = Boolean.valueOf(readOption(ENABLE_TASK_SERVICE, element, Boolean.FALSE.toString()));
        enableLifecycleExecution = Boolean.valueOf(readOption(ENABLE_LIFECYCLE, element, Boolean.TRUE.toString()));
        launchBrowser            = Boolean.valueOf(readOption(LAUNCH_BROWSER, element, Boolean.TRUE.toString()));
    }
    @Override public void writeExternal(Element element)
        throws WriteExternalException
    {
        super.writeExternal(element);
        writeOption(PORT, getPort(), element);
        writeOption(ENABLE_TASK_SERVICE, String.valueOf(isEnableTaskExecution()), element);
        writeOption(ENABLE_LIFECYCLE, String.valueOf(isEnableLifecycleExecution()), element);
        writeOption(LAUNCH_BROWSER, String.valueOf(isLaunchBrowser()), element);
    }

    /** Set autologin option. */
    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    /** Get autologin password. */
    public String getAutologinPass() {
        return autologinPass;
    }

    /** Set autologin user. */
    public void setAutologinPass(char[] autologinPass) {
        this.autologinPass = String.valueOf(autologinPass);
    }

    /** set autologin password. */
    public void setAutologinPass(String autologinPass) {
        this.autologinPass = autologinPass;
    }

    /** Get autologin user. */
    public String getAutologinUser() {
        return autologinUser;
    }

    /** Set autologin user. */
    public void setAutologinUser(String autologinUser) {
        this.autologinUser = autologinUser;
    }

    @NotNull @Override public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MMRunConfigurationEditor(getProject());
    }

    /** .* */
    public void setEnableTaskExecution(boolean enabled) {
        enableTaskExecution = enabled;
    }

    /** Set this to true to launch browser on startup. */
    public void setLaunchBrowser(boolean launchBrowser) {
        this.launchBrowser = launchBrowser;
    }

    /** Returns true if the configuration has autologin enabled. */
    public boolean isAutoLogin() {
        return autoLogin;
    }

    /** .* */
    public boolean isEnableLifecycleExecution() {
        return enableLifecycleExecution;
    }

    /** .* */
    public boolean isEnableTaskExecution() {
        return enableTaskExecution;
    }

    /** Returns true if this configuration is set to launch browser. */
    public boolean isLaunchBrowser() {
        return launchBrowser;
    }

    @Override public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment)
        throws ExecutionException
    {
        try {
            checkConfiguration();
        }
        catch (final RuntimeConfigurationException e) {
            throw new ExecutionException(e.getMessage());
        }

        final JavaCommandLineState state = new MMCommandLineState(executionEnvironment, this);

        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
        return state;
    }

    @Override public Collection<Module> getValidModules() {
        final ArrayList<Module> modules = new ArrayList<>();
        for (final Module module : getAllModules()) {
            if (MMModuleComponent.getMMDir(module) != null) modules.add(module);
        }
        return modules;
    }

    @Deprecated @Override
    @SuppressWarnings({ "InstanceofThis", "deprecation" })
    protected ModuleBasedConfiguration<JavaRunConfigurationModule> createInstance() {
        return new MMRunConfiguration(getName(), new JavaRunConfigurationModule(getProject(), true), MMConfigurationType.getFactory());
    }

    protected String readOption(String optionName, Element element, @Nullable String defaultValue) {
        final Element child  = element.getChild(optionName);
        String        result = defaultValue;
        if (child != null) {
            final String value = child.getAttributeValue(VALUE);
            if (value != null && !"null".equals(value)) result = value;
        }
        return result;
    }

    protected void writeOption(String optionName, String value, Element parent) {
        final Element element = new Element(optionName);
        element.setAttribute(VALUE, value);
        parent.addContent(element);
    }

    void setEnableLifeCycleExecution(boolean enabled) {
        enableLifecycleExecution = enabled;
    }

    String getPort() {
        return port;
    }

    void setPort(String port) {
        this.port = port;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String VALUE = "value";

    private static final String PORT                = "port";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String LAUNCH_BROWSER      = "launchBrowser";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String ENABLE_TASK_SERVICE = "enableTaskService";

    private static final String ENABLE_LIFECYCLE = "enableLifecycle";
}  // end class MMRunConfiguration
