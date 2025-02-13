
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server;

import java.io.File;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import tekgenesis.app.SuiGenerisInstance;
import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.env.impl.PropertiesEnvironment;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.tools.test.TestUtils;
import tekgenesis.common.util.Files;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.es.IndexProperties;
import tekgenesis.security.shiro.ShiroSession;
import tekgenesis.service.ServiceProps;
import tekgenesis.task.TaskService;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.view.client.InitTestContextService;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;

/**
 * Service to initialize the context for the tests (since we don't have a web.xml).
 */
@SuppressWarnings("GwtServiceNotRegistered")  // In tests we don't have web.xml
public class InitTestContextServiceServlet extends RemoteServiceServlet implements InitTestContextService {

    //~ Instance Fields ..............................................................................................................................

    private SuiGenerisInstance application = null;

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void destroyContext() {
        final String testId       = (String) getServletContext().getAttribute(CURRENT_TEST_ID);
        final String testDatabase = (String) getServletContext().getAttribute("testDatabases");
        System.out.println("[SERVER] Shutdown SuiGeneris for " + testId + " (" + testDatabase + ")");
        final long elapsed = System.currentTimeMillis();
        shutdown();
        System.out.println("Shutdown elapsed: " + (System.currentTimeMillis() - elapsed) + "ms");

        cleanEnvironment(testId);
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void initTestContext(final String[] projectPath, String testId) {
        shutdown();

        System.setProperty(SUIGEN_DEVMODE, "true");
        System.setProperty(Constants.WAIT_FOR_INDEX, "true");

        final String testDatabase = testDatabases();
        System.out.println("[SERVER] Startup SuiGeneris for " + testId + " (" + testDatabase + ")");
        final long elapased = System.currentTimeMillis();

        final Environment env = new PropertiesEnvironment();
        env.put(databaseConfig(testId));
        env.put(shiroProps());

        final ApplicationProps apps = applicationProps();
        apps.seedDB = true;
        env.put(apps);

        final ServiceProps serviceProps = new ServiceProps();
        serviceProps.enabled = false;
        env.put(deCapitalizeFirst(TaskService.SERVICE_NAME), serviceProps);

        final IndexProperties indexProperties = IndexProperties.DEFAULT;
        indexProperties.port = 9500;
        env.put(indexProperties);

        Files.remove(new File(apps.runDir, "es"));
        application = new SuiGenerisInstance(env, false);
        application.noCluster().withElasticSearch();
        application.startup();

        SecurityUtils.getSession().authenticate(SHIRO_ADMIN_USER, SHIRO_ADMIN_PASS);

        getServletContext().setAttribute(CURRENT_TEST_ID, testId);
        getServletContext().setAttribute("testDatabases", testDatabase);
        System.out.println(
            "[SERVER] Startup SuiGeneris for " + testId + " (" + testDatabase + ") elapsed: " + (System.currentTimeMillis() - elapased) + "ms");
    }

    private void shutdown() {
        if (application != null) {
            application.shutdownSilently();
            application = null;
        }
    }

    //~ Methods ......................................................................................................................................

    private static ApplicationProps applicationProps() {
        final String testRunDir = Files.normalize(new File("target/form-view/test-run").getAbsolutePath());

        final ApplicationProps applicationProps = new ApplicationProps();
        applicationProps.runDir               = testRunDir;
        applicationProps.resetDB              = true;
        applicationProps.lifecycleTaskEnabled = false;
        return applicationProps;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private static void cleanEnvironment(String testId) {
        if (!isEmpty(testId)) {
            final HikariDatabaseConfig  databaseConfig  = databaseConfig(testId);
            final HikariDatabaseFactory databaseFactory = new HikariDatabaseFactory(new MemoryEnvironment(), new JDBCTransactionManager());
            final Database              database        = databaseFactory.open(DEFAULT_SCOPE, databaseConfig);

            database.getDatabaseType().dropDatabase(database, true);
        }

        System.getProperties().remove(SUIGEN_PROPS);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static HikariDatabaseConfig databaseConfig(String testId) {
        final String hostname = TestUtils.getHostName();

        final String schemaPrefix = hostname.toUpperCase() + "_";

        final DatabaseType databaseType;
        final String       dbDriver;
        final String       dbUrl;
        final String       dbSystemUrl;

        String dbUser       = (hostname + "_sui" + Math.abs(testId.hashCode())).toUpperCase().substring(0, END_INDEX);
        String dbSystemUser = ShiroSession.SYSTEM;
        String dbSystemPass = SHIRO_ADMIN_PASS;

        final String testDatabase = testDatabases();
        if ("oracle".equalsIgnoreCase(testDatabase)) {
            databaseType = DatabaseType.ORACLE;
            dbDriver     = DatabaseType.ORACLE.getDefaultDriverClassName();
            dbUrl        = System.getProperty("test.oracleUrl", "jdbc:oracle:thin:@oracle.tekgenesis.com:1521:oracle");
            dbSystemUrl  = dbUrl;
        }
        else if ("postgres".equalsIgnoreCase(testDatabase)) {
            databaseType = DatabaseType.POSTGRES;
            dbDriver     = DatabaseType.POSTGRES.getDefaultDriverClassName();
            dbUrl        = "jdbc:postgresql://postgredb.tekgenesis.com:5432/" + testId + hostname.toLowerCase();
            dbSystemUrl  = "jdbc:postgresql://postgredb.tekgenesis.com:5432/template1";
            dbUser       = dbUser.toLowerCase();
            dbSystemUser = "postgres";
        }
        else {
            final File dbFile = new File("target/form-view/test-run/db-" + testId);
            Files.remove(dbFile);

            // noinspection ResultOfMethodCallIgnored
            dbFile.mkdirs();

            databaseType = DatabaseType.HSQLDB_NOSEQ;
            dbDriver     = DatabaseType.HSQLDB.getDefaultDriverClassName();
            dbUrl        = HSQLDB_FILE_PROTOCOL + dbFile.getAbsolutePath() + "/" + testId;
            dbSystemUrl  = dbUrl;
            dbSystemUser = "sa";
            dbSystemPass = "";
        }

        final HikariDatabaseConfig databaseConfig = new HikariDatabaseConfig();
        databaseConfig.schemaPrefix   = schemaPrefix.toUpperCase();
        databaseConfig.type           = databaseType;
        databaseConfig.driverClass    = dbDriver;
        databaseConfig.jdbcUrl        = dbUrl;
        databaseConfig.user           = dbUser;
        databaseConfig.password       = SHIRO_ADMIN_PASS;
        databaseConfig.systemUser     = dbSystemUser;
        databaseConfig.systemPassword = dbSystemPass;
        databaseConfig.setAdminUrl(dbSystemUrl);

        return databaseConfig;
    }  // end method databaseConfig

    private static ShiroProps shiroProps() {
        final ShiroProps shiroProps = new ShiroProps();
        shiroProps.adminUserConfig = "password, role1, role2, role3";
        return shiroProps;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static String testDatabases() {
        String testDatabase = null;
        if (Boolean.getBoolean("suigen.tests.oracle")) testDatabase = "oracle";
        else if (Boolean.getBoolean("suigen.tests.postgres")) testDatabase = "postgres";

        if (isEmpty(testDatabase)) testDatabase = System.getProperty("test.databases");

        return isEmpty(testDatabase) ? "hsqldb" : testDatabase;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5174040893403926549L;

    private static final int    END_INDEX            = 16;
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String HSQLDB_FILE_PROTOCOL = "jdbc:hsqldb:file:";
}  // end class InitTestContextServiceServlet
