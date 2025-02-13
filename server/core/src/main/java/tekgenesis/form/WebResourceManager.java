
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.cluster.ClusterProps;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.LruCache;
import tekgenesis.common.util.Sha;
import tekgenesis.metadata.handler.Routes;

import static java.lang.Boolean.getBoolean;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.util.Files.close;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.common.util.Files.remove;

/**
 * Manage Web Resources.
 */
public class WebResourceManager {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, String>      pathToSha     = new HashMap<>();
    private final LruCache<String, byte[]> resourcesMap  = LruCache.createLruCache(SIZE);
    private ShaRepository                  shaRepository;
    private final Map<String, byte[]>      shaResources  = new HashMap<>(SIZE);
    private Thread                         thread        = null;

    //~ Constructors .................................................................................................................................

    private WebResourceManager() {
        configureShaRepository();
    }

    //~ Methods ......................................................................................................................................

    /** Clear resources cache. */
    public void clearCache() {
        resourcesMap.evictAll();
    }

    /**
     * @param  source  less file
     * @param  target  final css file
     */
    public void compileLessResources(File source, File target)
        throws IOException, LessException
    {
        // Instantiate the LESS compiler
        final LessCompiler lessCompiler = new LessCompiler();
        lessCompiler.setCompress(isProductionMode());

        // Compile LESS input file to CSS output file
        lessCompiler.compile(source, target);
    }

    /** Load resource again. */
    public void processResourceChange(final String resourcePath) {
        final File   source = new File(resourcePath);
        final String path   = toRelative(source);
        processResource(source, getCompiledResource(path), path);
    }

    /** Read resource with sha. If sha not found, use ShaResourceRepository */
    public InputStream readShaResource(String sha, String path) {
        final byte[] bytes = shaResources.get(getKey(path, sha));

        return bytes == null ? shaRepository.get(path, sha) : new ByteArrayInputStream(bytes);
    }

    /** Find all Resources filtering by the specified Predicate. */
    @Nullable public byte[] readWebResource(@NotNull final String uri) {
        final String normalized = Routes.normalize(uri);

        // try to read form compiled resources folder
        byte[] resource = readCompiledResource(normalized);

        // fallback to jar resources
        if (resource == null) {
            copyJarResource(normalized);
            resource = readCompiledResource(normalized);
        }

        return resource;
    }

    /** Replace resource with sha. */
    public void replaceShaPath(String path, File file)
        throws IOException
    {
        final FileInputStream resource     = new FileInputStream(file);
        final Sha             shaGenerator = new Sha();
        shaGenerator.process(resource);
        final String sha = shaGenerator.getDigestAsString();
        close(resource);
        // cache sha
        pathToSha.put(path, sha);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        shaResources.put(getKey(path, sha), bytes);
        shaRepository.put(path, sha, bytes);
    }

    /** Return path with sha. */
    public String shaPath(String path) {
        final String shaPath = shaForPath(path);
        final String sha     = shaPath.substring(shaPath.lastIndexOf("/") + 1);
        if (!shaResources.containsKey(getKey(path, sha))) {
            final byte[] bytes = readWebResource(path);
            if (bytes != null) {
                shaResources.put(getKey(path, sha), bytes);
                shaRepository.put(path, sha, bytes);
            }
        }
        return shaPath;
    }

    private void attachDirectory(WatchService watchService, File rootDir, File resourcesDir)
        throws IOException
    {
        Paths.get(resourcesDir.getAbsolutePath()).register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        final File[] dirs = resourcesDir.listFiles(File::isDirectory);
        if (dirs != null) {
            for (final File directory : dirs)
                attachDirectory(watchService, rootDir, directory);
        }

        final File[] files = resourcesDir.listFiles(File::isFile);
        if (files != null) {
            for (final File source : files) {
                final String path = source.getAbsolutePath().replace(rootDir.getAbsolutePath(), "");
                processResource(source, getCompiledResource(path), path);
            }
        }
    }

    private Option<WatchService> attachWatchService(final Seq<File> resourcesDirs) {
        try {
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            for (final File resourcesDir : resourcesDirs)
                attachDirectory(watchService, resourcesDir, resourcesDir);
            return some(watchService);
        }
        catch (final IOException e) {
            logger.error("Resource listener error", e);
            return Option.empty();
        }
    }

    private void compileJs(File source, File target)
        throws IOException
    {
        final com.google.javascript.jscomp.Compiler compiler = new com.google.javascript.jscomp.Compiler();

        final CompilerOptions options = new CompilerOptions();
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        // compile() returns a Result, but it is not needed here.
        compiler.compile(Colls.emptyList(), ImmutableList.of(SourceFile.fromFile(source)), options);

        // The compiler is responsible for generating the compiled code; it is not accessible via the Result.
        final String result = compiler.toSource();

        target.getParentFile().mkdirs();
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(target), UTF8);
        out.write(result);
        close(out);
    }

    private void configureShaRepository() {
        if (Context.getContext().hasBinding(Environment.class)) {
            final ClusterProps clusterProps = Context.getProperties(ClusterProps.class);
            if (isNotEmpty(clusterProps.awsAccessKey) && isNotEmpty(clusterProps.awsSecretKey) && isNotEmpty(clusterProps.awsBucket)) {
                shaRepository = new S3ShaRepository(clusterProps.awsAccessKey, clusterProps.awsSecretKey, clusterProps.awsBucket);
                return;
            }
        }
        shaRepository = new DefaultShaRepository();
    }

    private void copyJarResource(String normalized) {
        final File resource = getCompiledResource(normalized);
        try {
            final String      path = normalized.startsWith(SEP) ? normalized.substring(1) : normalized;
            final InputStream in   = currentThread().getContextClassLoader().getResourceAsStream(path);
            if (in != null) {
                // noinspection ResultOfMethodCallIgnored
                resource.getParentFile().mkdirs();
                final FileOutputStream out = new FileOutputStream(resource);

                // copy to resource out folder
                copy(in, out);
                // process resource overriding it self
                processResource(resource, resource, normalized);
            }
        }
        catch (final FileNotFoundException e) {
            logger.error("Failed to copy static resource: " + normalized, e);
        }
    }

    @Nullable private byte[] loadResource(@NotNull final String normalized) {
        return toBytes(getCompiledResource(normalized));
    }

    private void processResource(final File source, final File target, @NotNull String path) {
        final String normalized = Routes.normalize(path);
        final String name       = source.getName();
        boolean      copy       = true;

        // process special resources (js, less)
        try {
            // compile less files
            if (name.endsWith(LESS) && !name.startsWith("_")) {
                compileLessResources(source, target);
                copy = false;
            }

            // compile js files (skip in dev mode)
            else if (name.endsWith(JS) && isProductionMode()) {
                compileJs(source, target);
                copy = false;
            }
        }
        catch (IOException | LessException e) {
            logger.error("Error compiling resource: " + name, e);
        }

        // fallback to a simple copy
        if (copy) {
            try {
                copy(source, target, false);
            }
            catch (final IOException e) {
                logger.error("Error copying resource: " + name, e);
            }
        }

        final byte[] value = toBytes(target);
        if (value != null) resourcesMap.put(normalized, value);
    }

    private void processResourceChange(WatchKey watchKey, WatchEvent<?> event) {
        // returns the event type
        final WatchEvent.Kind<?> eventKind = event.kind();
        if (eventKind == OVERFLOW) return;

        // WatchKey watchable returns the calling Path object of Path.register
        final Path watchedPath = (Path) watchKey.watchable();

        // returns the context of the event
        final Path context = (Path) event.context();

        final File source = new File(watchedPath.toFile(), context.toString());

        if (source.isFile()) {
            final String path = toRelative(source);
            pathToSha.remove(path);
            if (!ENTRY_DELETE.equals(eventKind)) processResource(source, getCompiledResource(path), path);
        }

        if (!watchKey.reset()) logger.info(format("Resource listener reset. Path: %s - Kind: %s - Target: %s", watchedPath, eventKind, source));
    }

    @Nullable private byte[] readCompiledResource(@NotNull final String normalized) {
        return resourcesMap.find(normalized, this::loadResource);
    }

    @NotNull private String sha(@NotNull String relativePath) {
        final String normalized = Routes.normalize(relativePath);

        String sha = pathToSha.get(normalized);
        if (sha == null) {
            // look for resource
            final byte[] resource = readWebResource(normalized);
            if (resource != null) {
                // generate sha
                final Sha shaGenerator = new Sha();
                shaGenerator.process(resource);
                sha = shaGenerator.getDigestAsString();
                // cache sha
                pathToSha.put(normalized, sha);
            }
        }
        return notNull(sha);
    }

    @NotNull private String shaForPath(@NotNull String path) {
        final String normalized = Routes.normalize(path);

        final String sha = sha(normalized);
        // build the url to serve the static resource, ex: /sha/img/image.png/###SHA###
        return sha.isEmpty() ? normalized : SHA_SERVLET_PATH + normalized + SEP + sha.substring(0, 10);
    }

    private void startListening() {
        final Seq<File> resourceSrcDirs = Context.getEnvironment().get(ApplicationProps.class).getResourceSrcDir();
        if (!resourceSrcDirs.isEmpty()) {
            final Option<WatchService> watchServices = attachWatchService(resourceSrcDirs);
            if (watchServices.isPresent()) {
                thread = new Thread(() -> {
                        // noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                // wait for key to be signaled
                                final WatchKey key = watchServices.get().take();

                                if (key != null) {
                                    for (final WatchEvent<?> event : key.pollEvents())
                                        processResourceChange(key, event);
                                }
                            }
                            catch (final InterruptedException e) {
                                return;
                            }
                        }
                    });
                thread.start();
            }
        }
    }

    private void stopListening() {
        if (thread != null) thread.interrupt();
    }

    private String toRelative(final File source) {
        final String absolutePath = source.getAbsolutePath();
        for (final File resourceDir : Context.getEnvironment().get(ApplicationProps.class).getResourceSrcDir()) {
            if (absolutePath.contains(resourceDir.getAbsolutePath())) return absolutePath.replace(resourceDir.getAbsolutePath(), "");
        }
        return absolutePath;
    }

    private boolean isProductionMode() {
        return !getBoolean(SUIGEN_DEVMODE);
    }

    private String getKey(String path, String sha) {
        return Routes.normalize(path) + "-" + sha;
    }

    //~ Methods ......................................................................................................................................

    /** Start the service. */
    public static void start() {
        // empty out content
        final File resourceOutDir = Context.getEnvironment().get(ApplicationProps.class).getResourceOutDir();
        remove(resourceOutDir);
        // noinspection ResultOfMethodCallIgnored
        resourceOutDir.mkdir();

        getInstance().startListening();
    }

    /** Stop the service. */
    public static void stop() {
        getInstance().stopListening();
    }

    /** Return the path for the compiled resources. */
    public static File getCompiledResource(String path) {
        return new File(Context.getEnvironment().get(ApplicationProps.class).getResourceOutDir(), path);
    }

    /** Singleton return method. */
    public static WebResourceManager getInstance() {
        return INSTANCE;
    }

    @Nullable private static byte[] toBytes(@NotNull final File resource) {
        if (resource.exists()) {
            try {
                final byte[] bytes = new byte[(int) resource.length()];

                final FileInputStream fileInputStream = new FileInputStream(resource);
                fileInputStream.read(bytes, 0, bytes.length);
                fileInputStream.close();
                return bytes;
            }
            catch (final FileNotFoundException e) {
                // ignore
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final int SIZE = 500;

    private static final String         SEP              = "/";
    public static final String          LESS             = ".less";
    private static final String         JS               = ".js";
    @NonNls private static final String SHA_SERVLET_PATH = "/sha";

    private static final WebResourceManager INSTANCE = new WebResourceManager();

    private static final Logger logger = getLogger(WebResourceManager.class);
}  // end class WebResourceManager
