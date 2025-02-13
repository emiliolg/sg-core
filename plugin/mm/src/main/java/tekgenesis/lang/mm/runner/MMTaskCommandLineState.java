
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.io.File;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.util.Files;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * MM Run command line.
 */
class MMTaskCommandLineState extends MMCommandLineStateBase {

    //~ Instance Fields ..............................................................................................................................

    private final MMRunTaskConfiguration configuration;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the ExecutionEnvironment and the run configuration */
    public MMTaskCommandLineState(ExecutionEnvironment env, MMRunTaskConfiguration configuration) {
        super(env, configuration);
        this.configuration = configuration;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("rawtypes")
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner)
        throws ExecutionException
    {
        final File runDir = new File(configuration.getRunDir());
        if (configuration.cleanRunDir()) Files.remove(runDir);
        runDir.mkdirs();

        return super.execute(executor, runner);
    }  // end method execute

    @Override protected void addExtraVmParameters(ParametersList vmParameters) {
        vmParameters.add("-D" + "taskService.service.enabled=false");
    }

    @Override protected void configCommand(JavaParameters javaParameters) {
        javaParameters.getProgramParametersList().add("task");

        javaParameters.getProgramParametersList().add("-t", configuration.getTaskId());
        if (isNotEmpty(configuration.getData())) javaParameters.getProgramParametersList().add("-d", configuration.getData());
    }
}  // end class MMTaskCommandLineState
