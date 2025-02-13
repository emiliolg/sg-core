
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.BuilderCategory;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.FSOperations;
import org.jetbrains.jps.incremental.ModuleBuildTarget;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.fs.CompilationRound;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.JpsNamedElement;
import org.jetbrains.jps.model.JpsSimpleElement;
import org.jetbrains.jps.model.java.JpsJavaClasspathKind;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.jetbrains.jps.model.library.JpsLibrary;
import org.jetbrains.jps.model.library.JpsLibraryRoot;
import org.jetbrains.jps.model.library.JpsOrderRootType;
import org.jetbrains.jps.model.library.sdk.JpsSdk;
import org.jetbrains.jps.model.module.JpsDependencyElement;
import org.jetbrains.jps.model.module.JpsLibraryDependency;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsModuleDependency;
import org.jetbrains.jps.model.module.JpsModuleSourceRoot;
import org.jetbrains.jps.model.module.JpsSdkDependency;
import org.jetbrains.jps.model.module.impl.JpsModuleDependencyImpl;
import org.jetbrains.jps.model.module.impl.JpsSdkDependencyImpl;
import org.jetbrains.jps.util.JpsPathUtil;

import tekgenesis.codegen.project.ProjectBuilder;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.exception.ViewsInvalidArgumentTypeException;
import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.MetaModelJpsUtils;
import tekgenesis.lang.mm.compiler.model.MetaModelFacetSettings;
import tekgenesis.lang.mm.compiler.model.SuiGenerisJpsSdkProperties;
import tekgenesis.lang.mm.compiler.model.SuiGenerisJpsSdkType;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.parser.Position;
import tekgenesis.repository.ModelRepository;
import tekgenesis.repository.MultiModuleRepository;

import static org.jetbrains.jps.builders.java.JavaBuilderUtil.isForcedRecompilationAllJavaModules;
import static org.jetbrains.jps.incremental.messages.BuildMessage.Kind.ERROR;
import static org.jetbrains.jps.incremental.messages.BuildMessage.Kind.INFO;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.exists;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Constants.PLUGIN_MM;
import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_SOURCES_PATH;
import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_TEST_PATH;
import static tekgenesis.lang.mm.compiler.CompilerConstants.JAVA_EXT;
import static tekgenesis.lang.mm.compiler.CompilerConstants.META_MODEL_EXT;
import static tekgenesis.lang.mm.compiler.CompilerConstants.META_MODEL_FACET_TYPE_ID;
import static tekgenesis.parser.Position.LineColumnPosition;

/**
 * MetaModel Jps sources generation builder.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MetaModelSourceGeneratorBuilder extends ModuleLevelBuilder {

    //~ Constructors .................................................................................................................................

    protected MetaModelSourceGeneratorBuilder() {
        super(BuilderCategory.SOURCE_GENERATOR);
    }

    //~ Methods ......................................................................................................................................

    @Override public ExitCode build(final CompileContext context, ModuleChunk chunk,
                                    DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirty, OutputConsumer output)
        throws ProjectBuildException, IOException
    {
        final Map<JpsModule, MetaModelFacetSettings> modules = computeModules(sortModules(chunk));

        if (modules.isEmpty()) return ExitCode.NOTHING_DONE;

        if (isNotSuiGenerisProject(modules)) {
            for (final JpsModule module : modules.keySet()) {
                final JpsSdk<JpsSimpleElement<SuiGenerisJpsSdkProperties>> sdk = module.getSdk(SuiGenerisJpsSdkType.INSTANCE);
                if (sdk == null) {
                    if (context != null)
                        processErrorMessage(context, String.format("Sui Generis SDK not specified for module '%s'", module.getName()));
                    return ExitCode.ABORT;
                }
            }
        }

        final boolean rebuild = isForcedRecompilationAllJavaModules(context);

        final MultiMap<ModuleBuildTarget, File> files = MultiMap.createMultiMap();
        computeDirtyFilesForBuild(dirty, files);
        computeRemovedFiles(chunk, dirty, files, ".*\\.mm");

        if (!rebuild && files.isEmpty()) return ExitCode.NOTHING_DONE;
        final Map<JpsNamedElement, ModelRepository> cache = getRepositoriesCache(context);

        final BuilderListener listener = new BuilderListener(context);

        final Set<File> resourcesDirs = new HashSet<>();
        for (final JpsModule module : modules.keySet()) {
            if (!cache.containsKey(module)) {
                final ModelRepository repository = buildModuleRepository(context, module, listener);
                collectResourcesDirs(context, module, resourcesDirs);
                if (listener.hasErrors()) {
                    processInfoMessage(context, String.format("Errors occurred while building module '%s' repository.", module.getName()));
                    return ExitCode.ABORT;
                }
                else registerModuleInCache(context, cache, module, repository);
            }
        }

        if (rebuild && !deleteGeneratedSourcesDir(context, chunk.getModules(), chunk.containsTests())) return ExitCode.ABORT;

        if (!files.isEmpty())
            return generateSourceForFiles(context, chunk, cache, files, modules, resourcesDirs, output) ? ExitCode.OK : ExitCode.ABORT;

        return ExitCode.NOTHING_DONE;
    }

    @Override public void buildFinished(CompileContext context) {
        getClassLoaderCache(context).clear();
        getRepositoriesCache(context).clear();
        super.buildFinished(context);
    }

    @Override public void buildStarted(CompileContext context) {
        context.putUserData(classLoaders_cache_key, new HashMap<>());
        context.putUserData(repositories_cache_key, new HashMap<>());
        super.buildStarted(context);
    }

    @Override public List<String> getCompilableFileExtensions() {
        return Collections.singletonList(META_MODEL_EXT);
    }

    @NotNull @Override public String getPresentableName() {
        return "MetaModel Builder";
    }

    private void addDependantModules(CompileContext context, MultiModuleRepository repository, Set<JpsModule> dependencies) {
        final Map<JpsNamedElement, ModelRepository> cache = getRepositoriesCache(context);
        for (final JpsModule module : dependencies) {
            final ModelRepository dependency = cache.get(module);
            if (dependency != null && !dependency.getModels().isEmpty()) repository.dependsOn(dependency);
        }
    }
    /**
     * Build module repository: - Create empty repository - Add dependent modules - Add dependent
     * libraries - Build repository models
     */
    private ModelRepository buildModuleRepository(@NotNull final CompileContext context, @NotNull final JpsModule module,
                                                  BuilderErrorListener listener) {
        final MultiModuleRepository repository = new MultiModuleRepository();

        final List<JpsDependencyElement> dependencies = getModuleDependencies(module);
        final Set<JpsLibrary>            libraries    = new LinkedHashSet<>();
        final Set<JpsModule>             modules      = new HashSet<>();
        computeDependencies(dependencies, libraries, modules);

        addDependantModules(context, repository, modules);
        final ModelRepository librariesRepository = createLibrariesRepository(context, libraries);
        repository.dependsOn(librariesRepository);
        buildRepository(module, listener, repository);

        return repository;
    }

    private void buildRepository(JpsModule module, BuilderErrorListener listener, ModelRepository repository) {
        for (final JpsModuleSourceRoot sourceRoot : module.getSourceRoots()) {
            if (isMetaModelSourceRoot(sourceRoot)) new ModelRepositoryLoader(sourceRoot.getFile(), repository).withErrorListener(listener).build();
        }
    }

    /**
     * Build module repository: - Create empty repository - Add dependent modules - Add dependent
     * libraries - Build repository models
     */
    private void collectResourcesDirs(@NotNull final CompileContext context, @NotNull final JpsModule module, Set<File> sqlDirs) {
        final List<JpsDependencyElement> dependencies = getModuleDependencies(module);
        final Map<JpsNamedElement, File> userData     = getResourcesDirMap(context);
        for (final JpsDependencyElement dependency : dependencies) {
            if (dependency instanceof JpsModuleDependency) {
                if (userData != null) {
                    final File e = userData.get(((JpsModuleDependency) dependency).getModule());
                    if (e != null) sqlDirs.add(e);
                }
            }
            else if (dependency instanceof JpsLibraryDependency)
                sqlDirs.addAll(getLibraryBinaries(((JpsLibraryDependency) dependency).getLibrary()).toList());
            else if (dependency instanceof JpsSdkDependencyImpl) {
                final JpsSdkDependencyImpl sdkDependency = (JpsSdkDependencyImpl) dependency;
                if (sdkDependency.getSdkType() == SuiGenerisJpsSdkType.INSTANCE)
                    sqlDirs.addAll(getLibraryBinaries(sdkDependency.resolveSdk()).toList());
            }
        }
    }

    private void computeDependencies(List<JpsDependencyElement> dependencies, Set<JpsLibrary> libraries, Set<JpsModule> modules) {
        for (final JpsDependencyElement element : dependencies) {
            if (element instanceof JpsLibraryDependency) {
                final JpsLibrary library = ((JpsLibraryDependency) element).getLibrary();
                if (library != null) libraries.add(library);
            }
            else if (element instanceof JpsModuleDependency) modules.add(((JpsModuleDependency) element).getModule());
            else if (element instanceof JpsSdkDependency) {
                final JpsSdkDependency sdk = (JpsSdkDependency) element;
                if (sdk.getSdkType() == SuiGenerisJpsSdkType.INSTANCE) libraries.add(sdk.resolveSdk());
            }
        }
    }

    private void computeDirtyFilesForBuild(DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirty,
                                           final MultiMap<ModuleBuildTarget, File>                       files)
        throws IOException
    {
        dirty.processDirtyFiles((target, file, descriptor) -> {
            if (FileUtilRt.extensionEquals(file.getName(), META_MODEL_EXT)) files.put(target, file);
            return true;
        });
    }

    @NotNull private CompilerMessage createDetailedMessage(String message, String path, int columnNumber, int lineNumber) {
        return new CompilerMessage(Files.extension(path), ERROR, message, path, -1, -1, -1, lineNumber, columnNumber);
    }

    @NotNull private ModelRepository createLibrariesRepository(CompileContext context, Set<JpsLibrary> libraries) {
        final ModelRepositoryLoader loader = new ModelRepositoryLoader(new ModelRepository());
        for (final JpsLibrary library : libraries)
            loadRepositoryForLibrary(context, library, loader);
        return loader.build();
    }
    private ClassLoader createLibraryClassLoader(JpsLibrary library) {
        final List<JpsLibraryRoot> roots = library.getRoots(JpsOrderRootType.COMPILED);
        final URL[]                urls  = new URL[roots.size()];
        for (int i = 0; i < roots.size(); i++)
            urls[i] = Files.toUrl(JpsPathUtil.urlToFile(roots.get(i).getUrl()));
        return new URLClassLoader(urls);
    }

    private boolean deleteAndMarkRecursively(@NotNull File dir, @NotNull CompileContext context)
        throws IOException
    {
        if (dir.exists()) {
            final List<File> filesToDelete = collectJavaFilesRecursively(dir);
            if (!FileUtil.delete(dir)) {
                processErrorMessage(context, String.format("Cannot delete directory %s", dir.getPath()));
                return false;
            }
            for (final File file : filesToDelete)
                FSOperations.markDeleted(context, file);
        }
        return true;
    }

    private boolean deleteGeneratedSourcesDir(CompileContext context, Set<JpsModule> modules, boolean tests)
        throws IOException
    {
        boolean success = true;
        for (final JpsModule module : modules) {
            final MetaModelFacetSettings facetSettings = MetaModelJpsUtils.getFacetSettings(module);
            if (facetSettings != null) {
                final File generated = facetSettings.getGeneratedSourcesDir();
                if (generated != null && generated.exists() && matchesScope(generated, tests) && !deleteAndMarkRecursively(generated, context))
                    success = false;
            }
        }
        return success;
    }

    private Iterable<String> filesToPaths(Iterable<File> files) {
        return map(files, File::getPath);
    }

    private boolean generateSourceForFiles(CompileContext context, ModuleChunk chunk, Map<JpsNamedElement, ModelRepository> cache,
                                           MultiMap<ModuleBuildTarget, File> dirty, Map<JpsModule, MetaModelFacetSettings> modules,
                                           Set<File> resourcesDirs, OutputConsumer output) {
        for (final Map.Entry<ModuleBuildTarget, Collection<File>> entry : dirty.asMap().entrySet()) {
            final Collection<File>  files  = entry.getValue();
            final ModuleBuildTarget target = entry.getKey();
            final JpsModule         module = target.getModule();

            final MetaModelFacetSettings settings = modules.get(module);
            if (settings == null) {
                processErrorMessage(context, String.format(NO_FACET_EXTENSION, module.getName()));
                return false;
            }

            final ModelRepository repository = cache.get(module);
            if (repository == null) {
                processErrorMessage(context, String.format("No registered repository for module '%s'", module.getName()));
                return false;
            }

            final JpsModuleSourceRoot mm = getMetaModelSourceDirectory(context, module);
            if (mm == null) {
                processErrorMessage(context, String.format(NO_METAMODEL_SOURCE_ROOT, module.getName()));
                return false;
            }

            final ProjectBuilder projectBuilder = new ProjectBuilder(repository,
                    mm.getFile(),
                    target.getOutputDir(),
                    new File(settings.getGeneratedSourcesDir(), "mm"),
                    resourcesDirs);

            projectBuilder.withForceBaseGeneration().withModels(filesToPaths(files));

            processInfoMessage(context, String.format("Generating sources for %s metamodel files on module '%s'.", files.size(), module.getName()));

            try {
                final List<File> updated = projectBuilder.buildProject();
                for (final File f : updated)
                    FSOperations.markDirty(context, CompilationRound.CURRENT, f);
                    // output.registerOutputFile(target, f, Colls.singleton(mm.getFile().getPath()).toList());
            }
            catch (final IOException e) {
                processErrorMessage(context, String.format(AN_ERROR_OCCURRED, module.getName(), e.getMessage()));
                return false;
            }
            catch (final ViewsInvalidArgumentTypeException e) {
                for (final String error : e.getErrors())
                    context.processMessage(createDetailedMessage(error, e.getFullPath(), e.getColumnNumber(), e.getLineNumber()));

                return false;
            }
        }

        return true;
    }  // end method generateSourceForFiles

    private void loadRepositoryForLibrary(CompileContext context, JpsLibrary library, ModelRepositoryLoader loader) {
        final Map<JpsNamedElement, ClassLoader> cache       = getClassLoaderCache(context);
        final ClassLoader                       classLoader = cache.containsKey(library) ? cache.get(library) : createLibraryClassLoader(library);
        loader.loadStreams(classLoader);
    }

    private boolean matchesScope(File generated, boolean tests) {
        return generated.getPath().endsWith("test") == tests;
    }

    private void processErrorMessage(CompileContext context, String message) {
        processMessage(context, message, ERROR);
    }

    private void processInfoMessage(CompileContext context, String message) {
        processMessage(context, message, INFO);
    }

    private void processMessage(CompileContext context, String message, BuildMessage.Kind kind) {
        context.processMessage(new CompilerMessage(META_MODEL_FACET_TYPE_ID, kind, message));
    }

    private void registerModuleInCache(CompileContext context, Map<JpsNamedElement, ModelRepository> cache, JpsModule module,
                                       ModelRepository repository) {
        processInfoMessage(context, String.format("Repository for module '%s' built successfully.", module.getName()));
        cache.put(module, repository);

        final JpsModuleSourceRoot mm = getMetaModelSourceDirectory(context, module);
        if (mm != null) getResourcesDirMap(context).put(module, new File(mm.getFile().getParent(), Constants.RESOURCES));
    }

    private Set<JpsModule> sortModules(ModuleChunk chunk) {
        return Colls.immutable(chunk.getModules()).topologicalSort(jpsModule -> {
            final List<JpsDependencyElement> dependencies = JpsJavaExtensionService.getInstance()
                                                                                   .getDependencies(jpsModule,
                                                                                       JpsJavaClasspathKind.PRODUCTION_COMPILE,
                                                                                       false);
            final ArrayList<JpsModule>       result       = new ArrayList<>();
            for (final JpsDependencyElement dependency : dependencies) {
                if (dependency instanceof JpsModuleDependencyImpl) result.add(((JpsModuleDependency) dependency).getModule());
            }
            return result;
        });
    }

    @NotNull private Map<JpsNamedElement, ClassLoader> getClassLoaderCache(CompileContext context) {
        final Map<JpsNamedElement, ClassLoader> cache = context.getUserData(classLoaders_cache_key);
        assert cache != null;
        return cache;
    }

    private Seq<File> getLibraryBinaries(@Nullable JpsLibrary library) {
        if (library == null) return emptyIterable();
        return map(library.getRoots(JpsOrderRootType.COMPILED), value -> JpsPathUtil.urlToFile(value.getUrl()));
    }

    @Nullable private JpsModuleSourceRoot getMetaModelSourceDirectory(CompileContext context, JpsModule module) {
        for (final JpsModuleSourceRoot sourceRoot : module.getSourceRoots()) {
            if (isMetaModelSourceRoot(sourceRoot)) return sourceRoot;
        }
        return null;
    }

    private List<JpsDependencyElement> getModuleDependencies(JpsModule module) {
        return JpsJavaExtensionService.getInstance().getDependencies(module, JpsJavaClasspathKind.PRODUCTION_COMPILE, false);
    }

    @NotNull private Map<JpsNamedElement, ModelRepository> getRepositoriesCache(CompileContext context) {
        final Map<JpsNamedElement, ModelRepository> cache = context.getUserData(repositories_cache_key);
        assert cache != null;
        return cache;
    }

    private Map<JpsNamedElement, File> getResourcesDirMap(CompileContext context) {
        Map<JpsNamedElement, File> userData = context.getUserData(module_sql_dirs_key);
        if (userData == null) {
            userData = new HashMap<>();
            context.putUserData(module_sql_dirs_key, userData);
        }

        return userData;
    }

    private boolean isMetaModelSourceRoot(JpsModuleSourceRoot sourceRoot) {
        return sourceRoot.getFile().exists() &&
               (sourceRoot.getUrl().endsWith(META_MODEL_SOURCES_PATH) || sourceRoot.getUrl().endsWith(META_MODEL_TEST_PATH));
    }

    private boolean isNotSuiGenerisProject(Map<JpsModule, MetaModelFacetSettings> modules) {
        final JpsModule module = modules.keySet().iterator().next();
        return !exists(module.getProject().getModules(), jpsModule -> jpsModule != null && PLUGIN_MM.equals(jpsModule.getName()));
    }

    //~ Methods ......................................................................................................................................

    static void computeRemovedFiles(ModuleChunk chunk, DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirty,
                                    MultiMap<ModuleBuildTarget, File> files, String pattern) {
        boolean missingMM = false;
        for (final String s : dirty.getRemovedFiles(chunk.representativeTarget())) {
            if (s.contains("/mm/")) {
                missingMM = true;
                break;
            }
        }
        if (missingMM) {
            for (final JpsModuleSourceRoot root : chunk.representativeTarget().getModule().getSourceRoots()) {
                if (root.getUrl().endsWith("/mm")) Files.list(root.getFile(), pattern).forEach(a ->
                        files.put(chunk.representativeTarget(), new File(a)));
            }
        }
    }

    @NotNull private static List<File> collectJavaFilesRecursively(@NotNull File dir) {
        final List<File> result = new ArrayList<>();

        FileUtil.processFilesRecursively(dir,
            file -> {
                if (file.isFile() && FileUtilRt.extensionEquals(file.getName(), JAVA_EXT)) result.add(file);
                return true;
            });
        return result;
    }

    @NotNull private static Map<JpsModule, MetaModelFacetSettings> computeModules(@NotNull Collection<JpsModule> modules) {
        final Map<JpsModule, MetaModelFacetSettings> result = new LinkedHashMap<>();

        for (final JpsModule module : modules) {
            final MetaModelFacetSettings extension = MetaModelJpsUtils.getFacetSettings(module);
            if (extension != null) result.put(module, extension);
        }

        return result;
    }

    //~ Static Fields ................................................................................................................................

    static final String AN_ERROR_OCCURRED = "An error occurred while generating sources for module '%s' (%s)";

    static final String NO_FACET_EXTENSION       = "No facet extension for module '%s'";
    static final String NO_METAMODEL_SOURCE_ROOT = "No metamodel source root for module '%s'";

    private static final ModelRepository empty_repository = new ModelRepository();

    private static final Key<Map<JpsNamedElement, ClassLoader>>     classLoaders_cache_key = Key.create("classloaders.cache");
    private static final Key<Map<JpsNamedElement, ModelRepository>> repositories_cache_key = Key.create("repositories.cache");
    private static final Key<Map<JpsNamedElement, File>>            module_sql_dirs_key    = Key.create("sqldirs");

    //~ Inner Classes ................................................................................................................................

    private static class BuilderListener implements BuilderErrorListener {
        private final CompileContext context;
        private boolean              hasErrors;

        private BuilderListener(CompileContext context) {
            this.context = context;
        }

        @Override public void error(BuilderError error) {
            hasErrors = true;
            context.processMessage(createSimpleMessage(error));
        }

        @Override public void error(MetaModelAST node, BuilderError error) {
            hasErrors = true;
            final Position position = node.getPosition();
            if (position instanceof LineColumnPosition) context.processMessage(createDetailedMessage(error, (LineColumnPosition) position));
            else context.processMessage(createSimpleMessage(error));
        }

        @Override public boolean hasErrors() {
            return hasErrors;
        }

        private CompilerMessage createDetailedMessage(BuilderError error, LineColumnPosition position) {
            return new CompilerMessage(META_MODEL_FACET_TYPE_ID,
                ERROR,
                error.getMessage(),
                position.getSourceName(),
                -1,
                -1,
                -1,
                position.getLine(),
                position.getColumn());
        }

        private CompilerMessage createSimpleMessage(BuilderError error) {
            return new CompilerMessage(META_MODEL_FACET_TYPE_ID, ERROR, error.getMessage());
        }
    }  // end class BuilderListener
}  // end class MetaModelSourceGeneratorBuilder
