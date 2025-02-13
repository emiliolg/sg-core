
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.util.ServiceLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;

import tekgenesis.app.env.DatabaseEnvironment;
import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.app.service.*;
import tekgenesis.app.sse.RedisSSEService;
import tekgenesis.app.sse.SSEProps;
import tekgenesis.app.sse.SSEService;
import tekgenesis.app.sse.SSEType;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.cache.CacheManager;
import tekgenesis.cache.CacheType;
import tekgenesis.cache.database.ClearCacheMessageHandler;
import tekgenesis.cache.database.RebuildCacheMessageHandler;
import tekgenesis.cache.database.SessionCacheFactory;
import tekgenesis.cache.database.infinispan.CacheManagerConfig;
import tekgenesis.cache.database.infinispan.CacheManagerConfig.NoCluster;
import tekgenesis.cache.database.infinispan.ClusterCacheManagerConfig;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.*;
import tekgenesis.cluster.jmx.notification.RemoteApp;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.context.ContextImpl;
import tekgenesis.common.env.impl.BaseEnvironment;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.database.Databases;
import tekgenesis.database.SchemaDefinition;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.es.IndexProperties;
import tekgenesis.eventbus.EventBus;
import tekgenesis.external.NavigationFactory;
import tekgenesis.form.*;
import tekgenesis.form.configuration.TypeConvertersImpl;
import tekgenesis.form.filter.FilterFactory;
import tekgenesis.form.filter.FilterFactoryImpl;
import tekgenesis.index.ElasticSearchIndexer;
import tekgenesis.index.IndexManager;
import tekgenesis.metric.service.MetricService;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.persistence.DbStoreHandlerFactory;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.persistence.TableFactory;
import tekgenesis.persistence.etl.ImporterService;
import tekgenesis.persistence.ix.IxStoreHandleFactory;
import tekgenesis.persistence.resource.DbResourceHandler;
import tekgenesis.persistence.resource.ResourcesGC;
import tekgenesis.persistence.sql.SqlStoreHandlerFactory;
import tekgenesis.repository.ModelRepository;
import tekgenesis.security.shiro.ShiroConfiguration;
import tekgenesis.security.shiro.ShiroSession;
import tekgenesis.service.ServiceManager;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlBuilderImpl;
import tekgenesis.sg.MemberStatus;
import tekgenesis.sg.NodeEntry;
import tekgenesis.task.TaskService;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionManager;

import static java.lang.Boolean.parseBoolean;

import static tekgenesis.authorization.shiro.AuthorizationUtils.SUIGENERIS_AUTHORIZATION_CACHE;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Times.*;
import static tekgenesis.common.env.context.Context.*;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.persistence.IxEntityTable.IX_STORE_TYPE;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Sui-Generis application instance.
 */
@SuppressWarnings({ "InstanceVariableMayNotBeInitialized", "OverlyCoupledClass", "ClassWithTooManyMethods" })
public class SuiGenerisInstance {

    //~ Instance Fields ..............................................................................................................................

    private CacheManagerConfig cacheManagerConfig;

    private ClusterManager<Address> clusterManager;

    private final ApplicationProps config;

    @Nullable private DatabaseFactory<?> databaseFactory;
    private final HikariDatabaseConfig   dbConfig;

    private Database          defaultScopeDatabase;
    private final Environment env;

    private IndexManager         esManager;
    private boolean              isRunning;
    private boolean              isShuttingDown;
    private final ServiceManager serviceManager;
    private final boolean        webEnvironment;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public SuiGenerisInstance(boolean webEnvironment) {
        this(new DatabaseEnvironment(), webEnvironment);
    }

    /** Construct from an Environment. */
    public SuiGenerisInstance(Environment env, boolean webEnvironment) {
        isRunning           = false;
        isShuttingDown      = false;
        this.webEnvironment = webEnvironment;

        // Initialize context environment
        this.env       = env;
        config         = env.get(ApplicationProps.class);
        dbConfig       = createDbConfiguration(env, Boolean.getBoolean(SUIGEN_DEVMODE));
        serviceManager = new ServiceManager(env);

        getContext().setSingleton(Environment.class, env);

        // Bind the default implementations
        bind(FilterFactory.class, FilterFactoryImpl.class);
        bind(ApplicationContext.class, ApplicationContextImpl.class);
        bind(Session.class, ShiroSession.class);
        bind(TypeConverters.class, TypeConvertersImpl.class);

        initElasticSearch();

        final ActionsImpl actions = new ActionsImpl();
        getContext().setSingleton(Actions.class, actions);
        NavigationFactory.setFactory(new NavigationFactory(actions, env));

        getContext().setSingleton(HtmlBuilder.class, new HtmlBuilderImpl());

        // Not sure...
        LogConfig.start();

        if (config.noCluster) noCluster();
    }

    //~ Methods ......................................................................................................................................

    /** Execute sql statements. */
    public void execSql(Reader reader) {
        LogConfig.start();

        final HikariDatabaseFactory dbFactory = new HikariDatabaseFactory(env, new JDBCTransactionManager());

        defaultScopeDatabase = dbFactory.openDefault();
        defaultScopeDatabase.getTransactionManager().runInTransaction(t -> defaultScopeDatabase.sqlStatement(reader).executeScript());
        dbFactory.shutdown();
    }

    /** No cluster configuration. */
    public SuiGenerisInstance noCluster() {
        clusterManager     = new LocalClusterManager();
        cacheManagerConfig = new NoCluster();
        return this;
    }

    /** Shutdown the application. */
    @SuppressWarnings("WeakerAccess")
    public void shutdown() {
        shutdown(false);
    }

    /** Shutdown the application silently, without notifying listeners. */
    @SuppressWarnings("WeakerAccess")
    public void shutdownSilently() {
        shutdown(true);
    }

    /** Startup the application instance with full functionality. */
    @SuppressWarnings("WeakerAccess")
    public void startup() {
        isShuttingDown = false;

        LogConfig.start();
        if (webEnvironment) WebResourceManager.start();

        startupDatabase();

        ((BaseEnvironment) env).refresh();

        @SuppressWarnings("DuplicateStringLiteralInspection")
        final String cmdLineSafeMode = System.getProperty("server.safeMode");

        if (cmdLineSafeMode != null) {
            final ServerProps serverProps = env.get(ServerProps.class);
            serverProps.safeMode = parseBoolean(cmdLineSafeMode);
            env.put(serverProps);
        }

        isRunning = false;
        initializeShiro(webEnvironment);
        isRunning = true;

        getContext().setSingleton(ServiceManager.class, serviceManager);

        serviceManager.start(clusterManager);

        notifyStarted();

        configureClusterNode();

        final SSEProps props = env.get(SSEProps.class);
        if (props.type != SSEType.NONE) getContext().setSingleton(SSEService.class, new RedisSSEService(props));

        // Not sure
        LogConfig.bridgeJul();

        Transaction.getCurrent().ifPresent(Transaction::close);
    }  // end method startup

    /**
     * Startup the application instance with database. This state does not have tasks,
     * authentication and consumers.
     */
    public void startupDatabase() {
        isShuttingDown = false;
        isRunning      = false;

        LogConfig.start();
        initializeCluster();

        final HikariDatabaseFactory dbFactory = new HikariDatabaseFactory(env, new JDBCTransactionManager());
        final ContextImpl           ctx       = getContext();
        ctx.setSingleton(DatabaseFactory.class, dbFactory);
        ctx.setSingleton(TransactionManager.class, dbFactory.getTransactionManager());
        defaultScopeDatabase = dbFactory.initialize(DEFAULT_SCOPE, dbConfig, config.resetDB);

        ctx.setSingleton(ResourceHandler.class, new DbResourceHandler(env, defaultScopeDatabase));
        ctx.setSingleton(IndexManager.class, esManager);
        initializeRepository();
        initializeTableFactoryAndCacheManager(dbFactory);

        if (config.seedDB)
            ImporterService.seedDatabase(env,
                dbFactory.getTransactionManager(),
                getSingleton(ResourceHandler.class),
                config.getSeedDir(),
                config.seedDirOnly);

        databaseFactory = dbFactory;
        isRunning       = true;

        // Not sure
        LogConfig.bridgeJul();
    }

    /**  */
    public SuiGenerisInstance withAllServices() {
        withImporter();
        withJmx();
        withMailService();
        withWebProxy();
        withElasticSearch();
        withTasks();
        withMetrics();
        return this;
    }

    /** Add elastic search service to instance. */
    public SuiGenerisInstance withElasticSearch() {
        new ElasticSearchService(serviceManager);
        return this;
    }

    /** Importer Service. */
    public SuiGenerisInstance withImporter() {
        new tekgenesis.app.service.ImporterService(serviceManager);
        return this;
    }

    /** Expose instance as Jmx. */
    public SuiGenerisInstance withJmx() {
        new JmxService(serviceManager);
        return this;
    }

    /** Email Service. */
    public SuiGenerisInstance withMailService() {
        new EmailService(serviceManager);
        return this;
    }

    /** Metrics. */
    public SuiGenerisInstance withMetrics() {
        new MetricService(serviceManager);
        return this;
    }

    /**
     * Add Task Services.
     *
     * @param  initRemoveViews  initialize remote views
     */
    public SuiGenerisInstance withTasks() {
        new TaskService(serviceManager);
        return this;
    }

    /** Web Proxy. */
    public SuiGenerisInstance withWebProxy() {
        new WebProxyService(serviceManager);
        return this;
    }

    /** Returns true if the application is running. */
    @SuppressWarnings("WeakerAccess")
    protected boolean isRunning() {
        return isRunning;
    }

    /** Returns true if the application is shutting down. */
    @SuppressWarnings("WeakerAccess")
    protected boolean isShuttingDown() {
        return isShuttingDown;
    }
    /** Run the database initialization in dry mode, generate sql files and check change level. */
    SchemaDefinition.ChangeLevel checkDatabase(@NotNull Writer writer) {
        final HikariDatabaseFactory dbFactory = new HikariDatabaseFactory(env, new JDBCTransactionManager());
        return dbFactory.dryInitialize(DEFAULT_SCOPE, dbConfig, config.resetDB, writer);
    }

    void purgeResources() {
        new ResourcesGC(env, Databases.openDefault()).purge();
    }

    private void closeCache() {
        if (getContext().hasBinding(InfinispanCacheManager.class)) {
            final CacheManager cacheManager = getContext().getSingleton(InfinispanCacheManager.class);
            cacheManager.close();
        }
    }

    private void closeCluster() {
        if (clusterManager != null) clusterManager.stop();
    }

    private void configCaches(InfinispanCacheManager manager) {
        manager.getCache(SUIGENERIS_LIFE_CYCLE_MAP, CacheType.DEFAULT.withLifespan(SECONDS_HOUR).withMaxIdle(SECONDS_HOUR).withSyncReplication());
        manager.getCache(SUIGENERIS_FORM_INSTANCES_MAP,
            CacheType.DEFAULT.withLifespan(SECONDS_HOUR).withMaxIdle(10 * SECONDS_MINUTE).withSyncReplication());

        manager.configureCache(SUIGENERIS_XHTML_CACHE, CacheType.LOCAL.withLifespan(XHTML_TTL));

        if (config.handlersCacheSize > 0) manager.configureCache(SUIGENERIS_HANDLERS_CACHE, CacheType.LOCAL.withSize(config.handlersCacheSize));

        final ShiroProps shiroProps = Context.getProperties(ShiroProps.class);

        manager.getCache(SHIRO_SESSION_CACHE,
            CacheType.DEFAULT.withMaxIdle(Math.max(shiroProps.maxSessionTimeout, shiroProps.sessionTimeout))
                             .withLifespan(shiroProps.sessionTimeout + SECONDS_DAY)
                             .withSize(shiroProps.sessionCacheSize)
                             .withAsyncReplication());

        manager.configureCache(SUIGENERIS_AUTHORIZATION_CACHE, CacheType.DEFAULT.withLifespan(AUTH_CACHE_EXPIRATION));
    }  // end method configCaches

    private void configureClusterNode() {
        final Clusters clusters = new Clusters();
        getContext().setSingleton(Clusters.class, clusters);

        final MemberStatus status  = MemberStatus.READY;
        final InetAddress  address = clusterManager.getPhysicalAddress(clusterManager.getMember());
        final String       node    = address == null ? Constants.LOCALHOST : address.toString();

        runInTransaction(() -> NodeEntry.create(node).setStatus(status).persist());
    }

    private void initElasticSearch() {
        final IndexProperties props = env.get(IndexProperties.class);
        esManager = new IndexManager(props);
        if (props.isEmbedded()) esManager.embeddedElasticSearch(config.runDir);
        else esManager.jestElasticSearch();
    }

    private void initializeCluster() {
        if (clusterManager == null) {
            clusterManager = new JGroupClusterManager(env);
            final ClusterProps properties = env.get(ClusterProps.class);
            cacheManagerConfig = new ClusterCacheManagerConfig(properties.clusterName, clusterManager);
        }

        getContext().setSingleton(CacheManagerConfig.class, cacheManagerConfig);
        getContext().setSingleton(ClusterManager.class, clusterManager);
        registerMessageHandlers();
        registerRpcDispatchers();
    }

    private void initializeRepository() {
        final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
        getContext().setSingleton(ModelRepository.class, loader.build());
    }
    private void initializeShiro(boolean web) {
        final ShiroConfiguration shiro = new ShiroConfiguration(web, env);
        getContext().setSingleton(ShiroConfiguration.class, shiro);
    }

    /** Initialize the table Factory. */
    private void initializeTableFactoryAndCacheManager(DatabaseFactory<?> dbFactory) {
        final SqlStoreHandlerFactory sqlStoreHandlerFactory = new SqlStoreHandlerFactory(dbFactory);
        final DbStoreHandlerFactory  storeHandlerFactory    = new DbStoreHandlerFactory(sqlStoreHandlerFactory);

        storeHandlerFactory.register(IX_STORE_TYPE, new IxStoreHandleFactory());

        final TransactionManager tm = dbFactory.getTransactionManager();

        final InfinispanCacheManager iCm = new InfinispanCacheManager(DEFAULT_SCOPE,
                env,
                tm,
                storeHandlerFactory,
                clusterManager,
                cacheManagerConfig);
        configCaches(iCm);
        final SessionCacheFactory sc = new SessionCacheFactory(dbConfig.sessionCacheSize, tm, iCm);

        TableFactory.setFactory(new TableFactory(new ElasticSearchIndexer(defaultScopeDatabase, sc, esManager)));

        getContext().setSingleton(InfinispanCacheManager.class, iCm);
    }

    private void notifyShutdown() {
        if (config.lifecycleTaskEnabled) serviceManager.onService(TaskService.class, TaskService::stopLifecycleTasks);
    }

    private void notifyStarted() {
        if (config.lifecycleTaskEnabled) serviceManager.onService(TaskService.class, TaskService::startLifecycleTasks);
    }

    private void registerMessageHandlers() {
        try {
            clusterManager.registerMessageHandler(new RebuildCacheMessageHandler());
            clusterManager.registerMessageHandler(new ClearCacheMessageHandler());

            final ServiceLoader<MessageHandler<?>> handlers = cast(ServiceLoader.load(MessageHandler.class));
            for (final MessageHandler<?> handler : handlers)
                clusterManager.registerMessageHandler(handler);

            clusterManager.start();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void registerRpcDispatchers() {
        try {
            clusterManager.registerRpcDispatcher(RemoteApp.class, new AppObject(serviceManager));
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void shutdown(boolean silent) {
        if (!isShuttingDown) {
            if (!silent) notifyShutdown();
            isShuttingDown = true;

            ShiroSession.clear();

            serviceManager.shutdown();

            if (esManager != null) esManager.shutdown();
            closeCache();
            closeCluster();
            if (databaseFactory != null) databaseFactory.shutdown();

            getContext().clean();
            serviceManager.clean();

            EventBus.shutdown();
            LogConfig.stop();
            WebResourceManager.stop();
            isRunning = false;
        }
    }  // end method shutdown

    //~ Methods ......................................................................................................................................

    private static HikariDatabaseConfig createDbConfiguration(Environment env, boolean devMode) {
        final HikariDatabaseConfig cfg = env.get(HikariDatabaseConfig.class);
        if (devMode) cfg.enforceVersion = false;
        return cfg;
    }

    //~ Static Fields ................................................................................................................................

    private static final int XHTML_TTL             = 30;
    private static final int AUTH_CACHE_EXPIRATION = 300;

    private static final Logger logger = getLogger(SuiGenerisInstance.class);
}  // end class SuiGenerisInstance
