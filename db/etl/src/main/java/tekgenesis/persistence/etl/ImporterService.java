
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.common.util.ProgressMeter;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.task.ImporterTask;
import tekgenesis.task.Task;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.type.resource.AbstractResource;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.core.Constants.JAVA_IO_TMPDIR;
import static tekgenesis.common.media.Mimes.isText;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Class that polls a directory for entity files to import. If several related files need to be
 * processed, a file import.lock should be created before copying the files and removed after all
 * the files are complete
 */
@SuppressWarnings("OverlyComplexClass")
public class ImporterService implements Importer {

    //~ Instance Fields ..............................................................................................................................

    private final Environment env;

    private final Seq<ImporterTask> importers;
    private ProgressMeter           pm = null;

    private boolean               rebuildSequence;
    private final ResourceHandler rh;
    private boolean               running = false;

    private final File uploadDir;

    //~ Constructors .................................................................................................................................

    /** Constructor with upload folder. */
    public ImporterService(Environment env, ResourceHandler rh) {
        this(env, rh, getUploadDir());
    }

    /** .* */
    ImporterService(final Environment env, ResourceHandler rh, @Nullable File uploadDir) {
        this.uploadDir = uploadDir;
        this.rh        = rh;
        importers      = Task.listTasks(TaskType.IMPORTER).map(ImporterTask::new);
        running        = false;
        this.env       = env;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean accepts(File file) {
        return false;
    }

    @Override public boolean accepts(QName model, File file) {
        return IMPORT_FILTER.accept(file.getParentFile(), file.getName());
    }

    /** Import files from specified dir. */
    @SuppressWarnings({ "WeakerAccess", "DuplicateStringLiteralInspection" })
    public void importFiles(String inputDirName) {
        if (uploadDir == null) {
            logger.error("Upload directory does not set");
            return;
        }

        if (!uploadDir.exists()) {
            logger.error("Upload directory '" + uploadDir.getAbsolutePath() + "' does not exist");
            return;
        }
        SecurityUtils.unbindContext();

        initializeProgressMeter();

        pm.beginPhase(1);
        final File processDir = new File(uploadDir, Constants.PROCESS);
        // noinspection ResultOfMethodCallIgnored
        processDir.mkdir();

        final File inputDir      = new File(uploadDir, inputDirName);
        final File processingDir = new File(uploadDir, Constants.PROCESSING);
        inputDir.renameTo(processingDir);

        final String[] files = processingDir.list((dir, name) -> new File(dir, name).isFile());

        final List<String> sourceFiles;
        if (files != null) {
            sourceFiles = Arrays.asList(files);
            Collections.sort(sourceFiles);
        }
        else sourceFiles = emptyList();

        final Seq<String> entities = TableMetadata.localEntities(env);
        pm.endPhase();

        final File resourcesFolder = new File(processingDir, RESOURCES_FOLDER);
        try {
            pm.beginPhase(2);
            if (resourcesFolder.exists()) importResources(rh, resourcesFolder);
            pm.endPhase();

            pm.beginPhase(3);
            final Tuple<List<File>, Map<String, List<File>>> toProcess = getFilesToProcess(processingDir, processDir, sourceFiles, entities);
            pm.endPhase();

            pm.beginPhase(4);
            importFiles(toProcess.first());

            pm.beginPhase(5);
            importEntities(toProcess.second(), entities);
            pm.endPhase();
        }
        catch (final Throwable e) {
            logger.error("Error importing files", e);
            final File errorDir = new File(uploadDir, getCurrentDateFileName());

            try {
                Files.copyDirectory(processDir, errorDir);
                Files.copyDirectory(resourcesFolder, errorDir);
            }
            catch (final IOException e1) {
                logger.error(e1);
            }
        }
        pm.beginPhase(5);
        clean(processingDir, processDir, files, resourcesFolder);
        pm.endPhase();
    }  // end method importFiles

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void process(File file) {
        throw new IllegalStateException("Should not be called");
    }

    @Override public void process(QName model, File file) {
        try(FileInputStream is = new FileInputStream(file)) {
            EntityImporter.load(model.getFullName()).updateOrInsert().using(getBuilder(file)).from(is);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Start service.* */
    @SuppressWarnings("OverlyNestedMethod")
    public void start() {
        if (uploadDir == null) {
            logger.warning("No upload directory set. Importer service not initialized");
            return;
        }

        // bootstrap import...
        // for each in* file run a first import
        File[] files = uploadDir.listFiles((dir, name) -> isInputDirectory(name));
        do {
            if (isNotEmpty(files)) {
                for (final File file : files)
                    doImportFiles(file.getName());
                files = uploadDir.listFiles((dir, name) -> isInputDirectory(name));
            }
        }
        while (isNotEmpty(files));

        running = true;
        try {
            final Path         uploadPath = Paths.get(uploadDir.getCanonicalPath());
            final WatchService watcher    = uploadPath.getFileSystem().newWatchService();
            uploadPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                // Watch lock file when it is removed
                WatchKey key = null;
                // noinspection NestedTryStatement
                try {
                    key = watcher.take();
                }
                catch (final InterruptedException e) {
                    logger.error("Unable to watch import directory", e);
                }

                if (key == null) logger.error("Unable to retrieve a watcher for upload dir");
                else {
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        final WatchEvent.Kind<?> kind = event.kind();

                        // We register just delete event..so we check that this is not an Overflow
                        // From Java Documentation
                        // This key is registered only for ENTRY_CREATE events,
                        // but an OVERFLOW event can occur regardless if events are lost or discarded.
                        if (kind != StandardWatchEventKinds.OVERFLOW) {
                            final WatchEvent<Path> ev       = cast(event);
                            final Path             filename = ev.context();
                            // Double check..just process the import when the import.lock is deleted

                            final String inputDir = filename.toFile().getName();

                            if (isInputDirectory(inputDir)) doImportFiles(inputDir);
                        }
                    }

                    // From Java documentation
                    // Reset the key -- this step is critical if you want to
                    // receive further watch events.  If the key is no longer valid,
                    // the directory is inaccessible so exit the loop.
                    key.reset();
                }
            }
        }
        catch (final IOException e) {
            logger.error("I/O Error trying to watch upload dir. Import service not started", e);
        }
    }  // end method start

    /** Stop service.* */
    public void stop() {
        running = false;
    }

    /** Set boolean to rebuild sequence after import. */
    public void setRebuildSequence(boolean b) {
        rebuildSequence = b;
    }

    ProgressMeter getProgressMeter() {
        return pm;
    }

    private void clean(@NotNull File processingDir, @NotNull File processDir, @Nullable String[] files, @NotNull File resourcesFolder) {
        if (files != null) pm.setItemsToProcess(files.length + 2);
        Files.remove(processDir);
        pm.advance();
        Files.remove(resourcesFolder);
        pm.advance();
        Files.remove(processingDir);
    }

    private void doImportFiles(String inputDirName) {
        try {
            // Try to get lock file (this is not the import.lock used by te user to put file in the uploadDir)
            // this lock file is used to avoid multiple node trying to upload the same file at the same time IF
            // the uploadDir is a share folder between nodes.
            final File file = new File(uploadDir, "processFiles.lock");

            file.createNewFile();
            final FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
            // get an exclusive lock on this channel

            final FileLock lock = fileChannel.lock();

            if (lock != null) {
                importFiles(inputDirName);
                lock.release();
            }
        }
        catch (final IOException e) {
            logger.warning("Unable to create/lock processFiles.lock", e);
        }
    }

    // Iterate over entities based on the dependency order defined in EntityTable#entities()
    private void importEntities(final Map<String, List<File>> toProcess, final Seq<String> entities) {
        int n = 0;
        for (final String entityName : entities) {
            final List<File> files = toProcess.get(entityName);
            if (files != null) {
                n++;
                for (final File file : files) {
                    logger.info("Importing model " + entityName);
                    load(QName.createQName(entityName), file);
                    if (rebuildSequence) rebuildSequence(EntityTable.forName(entityName));
                }
            }
        }
        if (n > 0) logger.info("Imported " + n + " models");
        else logger.debug("No models to import");
    }

    private boolean importFile(final File file) {
        for (final ImporterTask importer : importers) {
            if (importer.processFile(file)) return true;
        }
        return false;
    }

    private void importFiles(final List<File> filesToProcess) {
        for (final File file : filesToProcess) {
            if (!importFile(file)) logger.warning("No Importer found for file: " + file);
        }
    }

    @SuppressWarnings("MagicNumber")
    private void initializeProgressMeter() {
        pm = new ProgressMeter.Builder(getClass().getName()).addPhase(1, "Getting Files", 200)
             .addPhase(2, "Importing Resources", 1000)
             .addPhase(3, "Processing Files", 100)
             .addPhase(4, "Importing Files", 1000)
             .addPhase(5, "Importing Entities", 1000)
             .addPhase(6, "Removing Files", 100)
             .withLogger(LOGGER)
             .build();
    }

    private void load(@NotNull QName model, File file) {  //
        runInTransaction(() -> {
            if (importFile(file)) return;

            for (final ImporterTask task : importers) {
                if (task.processFile(model, file)) return;
            }
            process(model, file);
        });
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private String getCurrentDateFileName() {
        final DateTime current = DateTime.current();
        final String   dateStr = current.getYear() + "-" + current.getMonth() + "-" + current.getDay() + "_" + current.getHours() +
                                 current.getMinutes() + current.getSeconds();
        return "error/" + dateStr;
    }

    private Tuple<List<File>, Map<String, List<File>>> getFilesToProcess(@NotNull File processingDir, @NotNull File processDir,
                                                                         @NotNull List<String> sourceFiles, @NotNull Seq<String> entities)
        throws IOException
    {
        pm.setItemsToProcess(sourceFiles.size());

        final List<File>              filesToProcess      = new ArrayList<>();
        final Map<String, List<File>> modelFilesToProcess = new HashMap<>();

        for (final String sourceFile : sourceFiles) {
            final File   source     = new File(processingDir, sourceFile);
            final int    index      = sourceFile.lastIndexOf(".");
            final String entityName = index > 0 ? sourceFile.substring(0, index) : "";

            if (entityName.isEmpty() || !entities.contains(entityName)) filesToProcess.add(source);
            else {
                Files.copyToDir(source, processDir);

                List<File> fileList = modelFilesToProcess.get(entityName);
                if (fileList == null) fileList = new ArrayList<>();

                final File file = new File(processDir, sourceFile);
                fileList.add(file);
                modelFilesToProcess.put(entityName, fileList);
            }
            pm.advance();
        }

        return Tuple.tuple(filesToProcess, modelFilesToProcess);
    }  // end method getFilesToProcess

    private boolean isInputDirectory(@NotNull String name) {
        return name.startsWith(Constants.INPUT_PREFIX);
    }

    //~ Methods ......................................................................................................................................

    /** Seed database from files in the specified directory. */
    public static void seedDatabase(Environment env, TransactionManager tm, @NotNull ResourceHandler resourceHandler, @Nullable File seedDir,
                                    boolean seedDirOnly) {
        try {
            SecurityUtils.setSystemSurrogate("seed");
            runInTransaction(() -> doSeed(env, resourceHandler, seedDir, seedDirOnly));
        }
        finally {
            SecurityUtils.clearSystemSurrogate();
        }
    }

    private static void doSeed(Environment env, @NotNull ResourceHandler resourceHandler, @Nullable File seedDir, boolean seedDirOnly) {
        // Iterate over entities based on the dependency order defined in EntityTable#entities()
        logger.info("Seeding database ...");
        logger.info("Seeding resources ...");
        seedResources(resourceHandler);
        logger.info("Seeding entities ...");
        for (final String entityName : TableMetadata.localEntities(env)) {
            final boolean sed = seed(entityName, seedDir, seedDirOnly);
            if (sed) rebuildSequence(EntityTable.forName(entityName));
        }

        if (seedDir != null) importResources(resourceHandler, new File(seedDir, RESOURCES_FOLDER));
        logger.info("Seed Done.");
    }

    private static InputStream findResource(String fullName, EntityEtl.Builder builder) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("seed/" + fullName + "." + builder.getExtension());
    }

    private static void importContents(File variant, Resource resource, String uuid)
        throws IOException
    {
        final File[] files = variant.listFiles();
        if (files != null) {
            if (files.length > 2) throw new IOException("More than one resource content in folder " + variant);

            String mimeType = "";
            for (final File file : files) {
                final String name = file.getName();
                if (name.endsWith(".info")) mimeType = readContent(file);
                else {
                    if (mimeType.isEmpty()) {
                        final File infoFile = new File(variant, file.getName() + ".info");
                        mimeType = readContent(infoFile);
                    }
                    final Resource.Factory factory = resource.addVariant(variant.getName());
                    if (mimeType.isEmpty()) factory.upload(file);
                    else if (EXTERNAL.equals(mimeType)) factory.upload(file.getName(), readContent(file));
                    else uploadResource(mimeType, file, factory, uuid);
                }
            }
        }
    }  // end method importContents

    private static Option<Resource> importMaster(ResourceHandler rh, File uuidDir)
        throws IOException
    {
        final File master = new File(uuidDir, AbstractResource.MASTER);
        if (!master.exists()) return rh.findResource(uuidDir.getName());

        final Resource.Factory factory  = rh.create(uuidDir.getName());
        final File[]           files    = master.listFiles();
        Option<Resource>       resource = Option.empty();

        if (files == null) return resource;

        String mimeType = "";
        for (final File file : files) {
            final String name = file.getName();
            if (name.endsWith(".info")) mimeType = readContent(file);
            else {
                if (mimeType.isEmpty()) {
                    final File infoFile = new File(master, file.getName() + ".info");
                    mimeType = readContent(infoFile);
                }
                if (mimeType.isEmpty()) resource = Option.some(factory.upload(file));
                else {
                    if (mimeType.isEmpty()) {
                        final File infoFile = new File(master, file.getName() + ".info");
                        mimeType = readContent(infoFile);
                    }
                    if (mimeType.isEmpty()) resource = Option.some(factory.upload(file));
                    else if (EXTERNAL.equals(mimeType)) resource = Option.some(factory.upload(file.getName(), readContent(file)));
                    else {
                        final Resource upload = uploadResource(mimeType, file, factory, uuidDir.getName());
                        if (upload == null) resource = rh.findResource(uuidDir.getName());
                        else resource = Option.option(upload);
                    }
                }
            }
        }
        return resource;
    }  // end method importMaster

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static void importResources(ResourceHandler rh, File resourcesFolder) {
        final File[] list = resourcesFolder.listFiles(DIRECTORY_FILTER);

        if (list != null) {
            for (final File uuidDir : list) {
                try {
                    final Option<Resource> resource = importMaster(rh, uuidDir);

                    if (resource.isPresent()) {
                        final File[] files = uuidDir.listFiles(DIRECTORY_FILTER);
                        if (files != null) {
                            for (final File variant : files)
                                importContents(variant, resource.get(), uuidDir.getName());
                        }
                    }
                    else logger.error("Cannot find master variant for resource " + uuidDir.getName());
                }
                catch (final IOException e) {
                    logger.error("Error importing resource " + uuidDir, e);
                }
            }
        }
    }

    private static String readContent(File file)
        throws IOException
    {
        String mimeType = "";
        if (file.exists()) mimeType = Files.readInput(new FileReader(file));
        return mimeType.trim();
    }  // end method readContent

    private static void rebuildSequence(final EntityTable<?, ?> table) {
        logger.info("Rebuilding sequence for " + table.getEntityName());
        table.resetIdentitySequence();
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static boolean seed(String fullName, @Nullable File seedDirFile, boolean seedDirOnly) {
        boolean sed = false;
        for (final EntityEtl.Builder builder : getBuilders()) {
            if (!seedDirOnly) {
                final InputStream is = findResource(fullName, builder);
                if (is != null) {
                    logger.info(SEEDING + fullName + " from classLoader.");
                    EntityImporter.load(fullName).using(builder).from(is);
                    sed = true;
                }
            }
            if (seedDirFile != null) {
                final File file = new File(seedDirFile, fullName + "." + builder.getExtension());
                if (file.exists()) {
                    logger.info(SEEDING + fullName + " from " + file.getAbsolutePath());
                    EntityImporter.load(fullName).insert().using(builder).from(file);
                    sed = true;
                }
            }
        }
        return sed;
    }

    private static void seedFromJar(ResourceHandler rh, URL resource)
        throws IOException
    {
        final String path        = resource.getPath();
        final String jarName     = path.substring(5, path.indexOf('!'));
        final File   tempSeedDir = new File(System.getProperty(JAVA_IO_TMPDIR), "seed" + jarName.substring(jarName.lastIndexOf('/')));
        Files.remove(tempSeedDir);
        final JarFile               jar     = new JarFile(jarName);
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();
            final String   name     = jarEntry.getName();
            if (name.startsWith(SEED_RESOURCES_PATH) && !jarEntry.isDirectory()) {
                final File file = new File(tempSeedDir, name.substring(SEED_RESOURCES_PATH.length()));
                file.getParentFile().mkdirs();
                final InputStream      is  = jar.getInputStream(jarEntry);
                final FileOutputStream fos = new FileOutputStream(file);
                while (is.available() > 0)
                    fos.write(is.read());
                fos.close();
                is.close();
            }
        }

        importResources(rh, tempSeedDir);
    }

    private static void seedResources(ResourceHandler rh) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL         resource    = classLoader.getResource(SEED_RESOURCES_PATH);
        if (resource != null) {
            try {
                final String protocol = resource.getProtocol();
                if ("jar".equals(protocol)) seedFromJar(rh, resource);
                else if ("file".equals(protocol)) importResources(rh, new File(resource.toURI()));
            }
            catch (IOException | URISyntaxException e) {
                logger.error(e);
            }
        }
    }

    @Nullable private static Resource uploadResource(String mimeType, File file, Resource.Factory factory, String uuid)
        throws IOException
    {
        try {
            final String name = file.getName();
            if (isText(mimeType)) {
                final FileReader reader = new FileReader(file);
                final Resource   upload = factory.upload(name, mimeType, reader);
                reader.close();
                return upload;
            }
            else {
                final FileInputStream is     = new FileInputStream(file);
                final Resource        upload = factory.upload(name, mimeType, is);
                is.close();
                return upload;
            }
        }
        catch (final UniqueViolationException e) {
            logger.info("ImporterService : Resource " + uuid + " already exist.");
            return null;
        }
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static EntityEtl.Builder getBuilder(File file)
        throws IOException
    {
        final String extension = file.getName().substring(file.getName().lastIndexOf("."));
        if (".csv".equalsIgnoreCase(extension)) return EntityEtl.csv();
        if (".xml".equalsIgnoreCase(extension)) return EntityEtl.xml();
        throw new IOException("wrong file extension for file " + file);
    }

    private static EntityEtl.Builder[] getBuilders() {
        return new EntityEtl.Builder[] { EntityEtl.csv(), EntityEtl.xml() };
    }

    @Nullable private static File getUploadDir() {
        File                dir        = null;
        final ImporterProps properties = Context.getProperties(ImporterProps.class);
        if (properties.dir != null) {
            final File importDir = new File(properties.dir);
            if (importDir.exists() && importDir.isDirectory()) dir = importDir;
        }
        return dir;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(ImporterService.class);

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String EXTERNAL = "EXTERNAL";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SEEDING = "Seeding ";

    public static final String RESOURCES_FOLDER = Constants.RESOURCES;

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SEED_RESOURCES_PATH = "seed/resources/";

    private static final FileFilter DIRECTORY_FILTER = path -> path.isDirectory() && !AbstractResource.MASTER.equals(path.getName());

    private static final FilenameFilter IMPORT_FILTER = (file, name) -> name.endsWith(".csv") || name.endsWith(".xml");

    private static final Logger logger = Logger.getLogger(ImporterService.class);

    //~ Inner Classes ................................................................................................................................

    public static class Default implements Importer {
        @Override public boolean accepts(File file) {
            return false;
        }

        @Override public boolean accepts(QName model, File file) {
            return false;
        }

        @Override public void process(File file) {}

        @Override public void process(QName model, File file) {}
    }
}  // end class ImporterService
