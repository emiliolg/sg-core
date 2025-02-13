
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * MM Run Task configuration. Created by Jose on 8/27/14.
 */
public class MMRunTaskConfiguration extends MMRunConfigurationBase {

    //~ Instance Fields ..............................................................................................................................

    private String data   = null;
    private String taskId = null;

    //~ Constructors .................................................................................................................................

    /** Constructor that receives a module and a configuration factory. */
    public MMRunTaskConfiguration(JavaRunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        this("SuiGeneris Run Task", runConfigurationModule, factory);
    }

    /** Constructor that receives the config name, a module and a configuration factory. */
    protected MMRunTaskConfiguration(String name, JavaRunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        super(name, runConfigurationModule, factory);
    }

    //~ Methods ......................................................................................................................................

    @Override public void readExternal(Element element)
        throws InvalidDataException
    {
        super.readExternal(element);
        taskId = readOption(TASK_ID, element, null);
        data   = readOption(DATA, element, null);
    }

    @Override public void writeExternal(Element element)
        throws WriteExternalException
    {
        super.writeExternal(element);
        writeOption(TASK_ID, String.valueOf(taskId), element);
        writeOption(DATA, String.valueOf(data), element);
    }

    @NotNull @Override public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MMRunTaskConfigurationEditor(getProject());
    }

    /** Get Task Data. */
    public String getData() {
        return data;
    }

    /** Set Task Data. */
    public void setData(String data) {
        this.data = data;
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

        final JavaCommandLineState state = new MMTaskCommandLineState(executionEnvironment, this);

        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
        return state;
    }

    /** Get Task Id. */
    public String getTaskId() {
        return taskId;
    }

    /** Set Task Id. */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Deprecated @Override
    @SuppressWarnings({ "InstanceofThis", "deprecation" })
    protected ModuleBasedConfiguration<JavaRunConfigurationModule> createInstance() {
        return new MMRunTaskConfiguration(getName(), new JavaRunConfigurationModule(getProject(), true), MMTaskConfigurationType.getFactory());
    }

    //~ Static Fields ................................................................................................................................

    private static final String TASK_ID = "taskId";
    private static final String DATA    = "data";
}  // end class MMRunTaskConfiguration
