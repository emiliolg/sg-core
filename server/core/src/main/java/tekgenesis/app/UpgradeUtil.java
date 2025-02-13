
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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jetbrains.annotations.NotNull;

import tekgenesis.app.properties.ProxyProps;
import tekgenesis.cluster.ServerProps;
import tekgenesis.cluster.jmx.util.ClusterInfo;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.DatabaseConfig;
import tekgenesis.service.ServiceManager;
import tekgenesis.sg.MemberStatus;
import tekgenesis.sg.NodeEntry;

import static java.lang.String.format;

import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * Util class to Upgrade distribution.
 */
class UpgradeUtil {

    //~ Instance Fields ..............................................................................................................................

    private final ServiceManager serviceManager;

    //J-
    private void start(final InputStream is, final List<String> inLine) {
        new Thread(() -> {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
                String line;
                while ((line = reader.readLine()) != null)
                    inLine.add(line);
            } catch (final IOException e) {
                logger.warning(e);
            }
        }).start();
    }
    //J+

    //~ Constructors .................................................................................................................................

    UpgradeUtil(@NotNull ServiceManager sm) {
        serviceManager = sm;
    }

    //~ Methods ......................................................................................................................................

    Tuple<Integer, String> checkDbCompatibility(int buildNumber, @NotNull String branchName) {
        final DatabaseConfig databaseConfig = Context.getEnvironment().get(DatabaseConfig.class);
        final Properties     p              = new Properties();
        p.put("database.autoUpgrade", databaseConfig.autoUpgrade);
        p.put("database.driverClass", databaseConfig.driverClass);
        p.put("database.enforceVersion", databaseConfig.enforceVersion);
        p.put("database.jdbcUrl", databaseConfig.jdbcUrl);
        p.put("database.password", databaseConfig.password);
        p.put("database.schemaPrefix", databaseConfig.schemaPrefix);
        p.put("database.sessionCacheSize", databaseConfig.sessionCacheSize);
        p.put("database.systemPassword", databaseConfig.systemPassword);
        p.put("database.systemUser", databaseConfig.systemUser);
        p.put("database.user", databaseConfig.user);
        p.put("database.type", databaseConfig.type.name());

        try {
            p.store(new FileOutputStream(new File("/tmp/db_check.property")), "Db check compatibility property file");
            final String command = System.getProperty(Constants.BIN_DIR) + File.separator + "checkDbCompatibility.sh -c /tmp/db_check.property";

            return runCommand(buildNumber, branchName, false, command);
        }
        catch (final IOException e) {
            logger.error(e);
            return tuple(-1, "Unable to store property file to execute db compatibility check");
        }
    }

    /** Rollback current version to previous one, if it is an old one installed. */
    Tuple<Boolean, String> rollbackVersion() {
        final String command = System.getProperty(Constants.BIN_DIR) + File.separator + "rollbackUpdate.sh";

        final Tuple<Integer, String> ret = runCommand(-1, "", false, command);
        return tuple(ret.first() == 0, ret.second());
    }

    /**
     * @param   buildNumber  build number
     *
     * @return  Tuple Ok/Fail -> output
     */
    Tuple<Boolean, String> upgrade(int buildNumber, String branchName, boolean healthCheckEnabled) {
        final InetAddress inetAddress = ClusterInfo.getInstance().getLocal().getInetAddress();
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final String      ip = inetAddress == null ? "Local" : inetAddress.toString();

        stopServices(ip);

        @SuppressWarnings("DuplicateStringLiteralInspection")
        final String command = System.getProperty(Constants.BIN_DIR) + File.separator + "updateVersion.sh";

        final ProxyProps proxyProps = Context.getEnvironment().get(ProxyProps.class);
        proxyProps.enabled = healthCheckEnabled;
        Context.getEnvironment().put(proxyProps);

        final ServerProps serverProps = Context.getEnvironment().get(ServerProps.class);
        serverProps.safeMode = healthCheckEnabled;
        Context.getEnvironment().put(serverProps);

        final Tuple<Integer, String> ret = runCommand(buildNumber, branchName, healthCheckEnabled, command);
        return tuple(ret.first() == 0, ret.second());
    }

    @NotNull private Tuple<Integer, String> runCommand(int buildNumber, String branchName, boolean healthCheckEnabled, String command) {
        final InetAddress    inetAddress    = ClusterInfo.getInstance().getLocal().getInetAddress();
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final String         ip             = inetAddress == null ? "Local" : inetAddress.toString();
        final ProcessBuilder processBuilder = new ProcessBuilder(command,
                Integer.toString(buildNumber),
                branchName,
                Boolean.toString(healthCheckEnabled));
        processBuilder.redirectErrorStream(true);
        Tuple<Integer, String> ret;

        try {
            final String msg = format("Starting update version to : %d, branch: %s", buildNumber, branchName);
            logger.info(msg);

            updateStatus(ip, MemberStatus.UPDATING, msg);

            final Process start = processBuilder.start();

            final InputStream is = start.getInputStream();
            // the background thread watches the output from the process
            final List<String> inLine = new ArrayList<>();
            start(is, inLine);

            final InputStream  eis     = start.getErrorStream();
            final List<String> errLine = new ArrayList<>();
            start(eis, errLine);

            start.waitFor();

            final String result = mkString(inLine, "\n") +
                                  (!errLine.isEmpty() ? "\nUpdate Failed. Errors: " + mkString(errLine, "\n") : "\nUpdate Successful!");

            ret = tuple(start.exitValue(), result);
            logger.info(format("Update version finished. New version: %d", buildNumber));

            updateStatus(ip, MemberStatus.UPDATED, result);
        }
        catch (final Exception e) {
            logger.error("Update version Failed.", e);
            updateStatus(ip, MemberStatus.FAILED, e.getMessage());
            ret = tuple(-1, e.getMessage());
        }
        return ret;
    }  // end method runCommand

    private void stopServices(final String name) {
        // Stop all services
        serviceManager.stopAll(service -> {
            if (service == null) return;
            updateStatus(name, MemberStatus.STOP, format("Service '%s' stopped.", service.getName()));
        });
    }

    /**  */
    private void updateStatus(String node, MemberStatus status, String text) {
        NodeEntry.create(node).setStatus(status).setLog(text).persist();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(UpgradeUtil.class);
}  // end class UpgradeUtil
