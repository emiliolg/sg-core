
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.cmd.Command;
import tekgenesis.common.cmd.CommandInfo;
import tekgenesis.common.cmd.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.database.SchemaDefinition.ChangeLevel;
import tekgenesis.sg.TaskEntry;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.TaskProps;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.app.Util.handleUncaughtExceptions;
import static tekgenesis.app.Util.setPropertiesOrExit;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.core.Constants.APPLICATION_RESET_DB;
import static tekgenesis.task.Task.listTasks;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * SuiGeneris Application.
 */
@CommandInfo(name        = "suigeneris", description = "[global options] command [command options]")
@SuppressWarnings({ "DuplicateStringLiteralInspection", "FieldMayBeFinal" })
public class SuiGeneris extends Command<SuiGeneris> {

    //~ Instance Fields ..............................................................................................................................

    @Option(description  = "Application Directory", defaultValue = "../webapp")
    @SuppressWarnings("SpellCheckingInspection")
    private String applicationDir = null;

    @Option(description = "Configuration File")
    private String config = "";

    @Option(description = "Models ClassPath")
    private String models = "";

    @Option(description  = "Running directory", defaultValue = "../run")
    private String runDir = null;

    //~ Methods ......................................................................................................................................

    @Override public void execute() {}

    private void addModelJars() {
        if (isNotEmpty(models)) {
            final String[] split = models.split(File.pathSeparator);
            for (final String s : split) {
                try {
                    addPath(new File(s));
                }
                catch (final Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    //~ Methods ......................................................................................................................................

    /** Main SuiGeneris App. */
    public static void main(String[] args) {
        new SuiGeneris().withArgs(args).withCommand(new Start()).withCommand(new Purge()).withCommand(new Db()).withCommand(new Task()).run();
    }

    private static void addPath(File... files)
        throws Exception
    {
        for (final File file : files) {
            final URL            u              = file.toURI().toURL();
            final URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            final Class<?>       urlClass       = URLClassLoader.class;
            final Method         method         = urlClass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(urlClassLoader, u);
        }
    }

    //~ Inner Classes ................................................................................................................................

    @CommandInfo(name        = "db", description = "Database initialization / update")
    static class Db extends Command<SuiGeneris> {
        @Option(description  = "Print SQL sentences to apply", defaultValue = "false")
        @SuppressWarnings("BooleanVariableAlwaysNegated")
        private boolean dry = false;

        @Option(description = "Input file for SQL sentences to execute")
        private String execute = null;

        @Option(description = "Output file for SQL sentences")
        private String out = null;

        @Override public void execute() {
            getCommonOptions().addModelJars();
            final File webAppDir = new File(getCommonOptions().applicationDir);
            setPropertiesOrExit(webAppDir, getCommonOptions().runDir, 0, getBoolean(APPLICATION_RESET_DB), null, getCommonOptions().config, 0);

            final SuiGenerisInstance app = new SuiGenerisInstance(false);

            final Writer writer = createDryWriter();

            if (execute != null) app.execSql(getSqlReader());
            else {
                handleUncaughtExceptions(Logger.getLogger(SuiGeneris.class));
                if (writer == null) app.startupDatabase();
                else {
                    final ChangeLevel changeLevel = app.checkDatabase(writer);
                    final boolean     wrote       = dumpDryWriter(writer);
                    assert wrote ? changeLevel != ChangeLevel.NONE : changeLevel == ChangeLevel.NONE;

                    switch (changeLevel) {
                    case NONE:
                        System.out.println("No Database changes to apply");
                        break;
                    case MINOR:
                        System.out.println("Minor changes");
                        System.exit(2);
                        break;
                    case MAJOR:
                        System.out.println("Major changes");
                        System.exit(10);
                        break;
                    }
                }
            }
            app.shutdownSilently();
            System.exit(0);
        }  // end method execute

        @Nullable private Writer createDryWriter() {
            if (!dry) return null;

            try {
                return out == null ? new StringWriter() : new FileWriter(out);
            }
            catch (final IOException e) {
                e.printStackTrace(System.err);
                System.exit(1);
                return null;
            }
        }

        private boolean dumpDryWriter(@NotNull final Writer writer) {
            Files.close(writer);

            if (out != null) return new File(out).length() > 0;

            final StringBuffer result = ((StringWriter) writer).getBuffer();

            final boolean wrote = result.length() > 0;
            if (wrote) System.out.println(result);
            return wrote;
        }  // end method dumpDryWriter

        @NotNull private FileReader getSqlReader() {
            try {
                return new FileReader(new File(execute));
            }
            catch (final FileNotFoundException e) {
                e.printStackTrace(System.err);
                System.exit(1);
                throw unreachable();
            }
        }
    }  // end class Db

    @CommandInfo(name        = "purge", description = "Clean unreferenced resources")
    static class Purge extends Command<SuiGeneris> {
        @Override public void execute() {
            getCommonOptions().addModelJars();
            setPropertiesOrExit(new File(getCommonOptions().applicationDir), getCommonOptions().runDir, 0, false, null, getCommonOptions().config, 0);

            final SuiGenerisInstance app = new SuiGenerisInstance(false);
            app.startupDatabase();
            app.purgeResources();
            app.shutdownSilently();
        }
    }

    @CommandInfo(name        = "start", description = "Start the Sui Generis Server")
    static class Start extends Command<SuiGeneris> {
        @Option(name         = "https-port", description = "HTTPS Listening Port", defaultValue = "8443")
        private int httpsPort = 0;

        @Option(
                name        = "key-store", description = {
                    "Specify a key store and enable HTTPS",
                    "Format: 'key-store-file:password:manager-password'"
                }
               )
        private String keyStore = null;

        @Option(description  = "HTTP Listening Port", defaultValue = "8080")
        private int port = 0;

        @Option(name        = "reset-db", description = "Drop and Create Clean Database")
        private boolean resetDb = false;

        @Option(name         = "safeMode", description = "Set Server as safe-mode. Server is not registered in Load Balancer", defaultValue = "true")
        private String safeMode = null;

        @Option(description  = "Thread Pool Max Threads", defaultValue = "200")
        private int threads = 0;

        @Override public void execute() {
            try {
                WebServer.run(getCommonOptions().applicationDir,
                    getCommonOptions().runDir,
                    port,
                    resetDb,
                    getCommonOptions().models,
                    getCommonOptions().config,
                    keyStore,
                    httpsPort,
                    threads,
                    safeMode);
            }
            catch (final Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }  // end class Start

    @CommandInfo(name        = "task", description = "Run task")
    static class Task extends Command<SuiGeneris> {
        @Option(description = "task data")
        private String data    = null;
        @Option(description    = "task id to run")
        private String taskFQN = null;

        @Override public void execute() {
            if (!listTasks().contains(taskFQN)) {
                System.err.println("Task model " + taskFQN + " not found.");
                return;
            }
            getCommonOptions().addModelJars();
            Util.setPropertiesOrExit(new File(getCommonOptions().applicationDir),
                getCommonOptions().runDir,
                0,
                false,
                null,
                getCommonOptions().config,
                0);
            final SuiGenerisInstance app = new SuiGenerisInstance(false);
            app.startup();
            runTask();
            app.shutdownSilently();
        }

        private void runTask() {
            String hostAddress;
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            }
            catch (final UnknownHostException e) {
                hostAddress = "Local";
            }
            final TaskProps taskProps = Context.getEnvironment().get(taskFQN, TaskProps.class);
            final TaskEntry taskEntry = TaskEntry.startExecution(taskFQN, hostAddress, taskProps.maxRunningTime);

            final ScheduledTask task = ScheduledTask.createFromFqn(taskFQN).get();
            runInTransaction(() -> task.getInstance().setData(data));
            task.initialize(taskEntry);
            task.call();
        }  // end method runTask
    }  // end class Task
}  // end class SuiGeneris
