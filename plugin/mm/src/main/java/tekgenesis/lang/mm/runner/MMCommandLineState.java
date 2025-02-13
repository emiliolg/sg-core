
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.awt.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;

import javax.swing.*;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.MMHotSwapManager;

import static tekgenesis.common.core.Constants.HTTP_LOCALHOST;
import static tekgenesis.common.core.Constants.LOGIN_URI;

/**
 * MM Run command line.
 */
class MMCommandLineState extends MMCommandLineStateBase {

    //~ Instance Fields ..............................................................................................................................

    private final MMRunConfiguration configuration;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the ExecutionEnvironment and the run configuration */
    public MMCommandLineState(ExecutionEnvironment env, MMRunConfiguration configuration) {
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

        final ExecutionResult result = super.execute(executor, runner);

        final String baseUri = HTTP_LOCALHOST + configuration.getPort();

        final SwingWorker<String, Object> worker = createLaunchBrowserWorker(baseUri);

        final ProcessHandler handler = result.getProcessHandler();

        final ProcessAdapter listener = new ProcessAdapter() {
                @Override public void startNotified(ProcessEvent event) {
                    worker.execute();
                }

                @Override public void processWillTerminate(ProcessEvent event, boolean b) {
                    worker.cancel(true);
                }

                @Override public void processTerminated(ProcessEvent event) {
                    handler.removeProcessListener(this);
                }
            };

        handler.addProcessListener(listener);

        final MMHotSwapManager component = MMHotSwapManager.getInstance(getEnvironment().getProject());
        component.register(result, baseUri);

        return result;
    }  // end method execute

    protected void addExtraVmParameters(ParametersList vmParameters) {
        if (configuration.isAutoLogin())
            vmParameters.add("-Dshiro.autoLogin=" + configuration.getAutologinUser() + ":" + configuration.getAutologinPass());

        vmParameters.add("-DtaskService.taskservice.enabled=" + configuration.isEnableTaskExecution());
        vmParameters.add("-Dapplication.lifecycleTaskEnabled=" + configuration.isEnableLifecycleExecution());
    }

    protected void configCommand(JavaParameters javaParameters) {
        // noinspection DuplicateStringLiteralInspection
        javaParameters.getProgramParametersList().add("start");

        javaParameters.getProgramParametersList().add("-p", configuration.getPort());
    }
    // end method createJavaParameters

    private SwingWorker<String, Object> createLaunchBrowserWorker(final String baseUri) {
        return new SwingWorker<String, Object>() {
            @Override protected String doInBackground()
                throws Exception
            {
                if (configuration.isLaunchBrowser() && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        final URI uri = new URI(baseUri + LOGIN_URI);
                        for (int i = 0; i < 10; i++) {
                            if (exists(uri)) {
                                Desktop.getDesktop().browse(uri);
                                break;
                            }
                            else Thread.sleep(1000);
                        }
                    }
                    catch (final Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                return "";
            }

            private boolean exists(URI uri) {
                try {
                    HttpURLConnection.setFollowRedirects(false);
                    final HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
                    con.setRequestMethod("HEAD");
                    return (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP);
                }
                catch (final Exception e) {
                    return false;
                }
            }
        };
    }
}  // end class MMCommandLineState
