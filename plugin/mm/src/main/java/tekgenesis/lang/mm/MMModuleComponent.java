
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.roots.libraries.LibraryUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.repository.ModelRepository;
import tekgenesis.repository.MultiModuleRepository;

import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_SOURCES_PATH;
import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_TEST_PATH;
import static tekgenesis.lang.mm.psi.PsiUtils.getDependentModules;

/**
 * Component of Module used by plugin for managing the module's ModelRepository and dependencies.
 */
public class MMModuleComponent implements ModuleComponent {

    //~ Instance Fields ..............................................................................................................................

    private boolean hasErrors;
    private long    lastBuilt;

    private ModelRepository libraries;

    private final Module module;

    private MultiModuleRepository repository;

    //~ Constructors .................................................................................................................................

    /** Constructor of ModuleRepositoryComponent. */
    public MMModuleComponent(@NotNull Module module) {
        this.module = module;
        repository  = null;
        libraries   = null;
    }

    //~ Methods ......................................................................................................................................

    public void disposeComponent() {}

    /** Returns true if has been built with errors. */
    public boolean hasErrors() {
        return hasErrors;
    }

    public void initComponent() {}

    public void moduleAdded() {}

    public void projectClosed() {}

    public void projectOpened() {}

    /** Update build time. */
    public void updateLastBuilt(boolean errors) {
        lastBuilt = System.currentTimeMillis();
        hasErrors = errors;
    }

    /** Return true if has been built after timeStamp. */
    public boolean upToDate(long timeStamp) {
        return lastBuilt > timeStamp;
    }

    @NotNull public String getComponentName() {
        return "ModuleRepositoryComponent";
    }
    /** repository for external files. */
    public ModelRepository getLibrariesRepository() {
        return libraries;
    }

    /** Gets LibraryRoots. */
    public VirtualFile[] getLibraryRoots() {
        return LibraryUtil.getLibraryRoots(new Module[] { module }, false, true);
    }

    /** Get associated ModelRepository. */
    @NotNull public ModelRepository getRepository() {
        return repository == null ? new ModelRepository() : repository;
    }

    /**
     * Initialize module repository: - Create empty repository - Add dependent modules - Add
     * dependent libraries - Build repository models
     */
    void initializeModelRepository() {
        repository = new MultiModuleRepository();
        addDependentModules();
        addDependentLibraries();
        buildRepository();
    }

    private void addDependentLibraries() {
        final ClassLoader loader = findOuterProjectDependencies();
        libraries = new ModelRepositoryLoader(loader).build();
        if (!libraries.getModels().isEmpty()) repository.dependsOn(libraries);
    }

    private void addDependentModules() {
        for (final Module dependent : getDependentModules(module)) {
            final MMModuleComponent component = dependent.getComponent(MMModuleComponent.class);
            final ModelRepository   repo      = component.getRepository();
            if (!repo.getModels().isEmpty()) repository.dependsOn(repo);
        }
    }

    /** Build repository: parse all files from the mm directory. */
    private void buildRepository() {
        final VirtualFile sources = getMMDir(module);
        if (sources != null) {
            final ModelRepositoryLoader b = new ModelRepositoryLoader(new File(sources.getPath()), repository);
            b.withErrorListener(new BuilderErrorListener.Silent());
            b.build();
        }
    }

    private ClassLoader findOuterProjectDependencies() {
        final URL[] urls = toUrl(getLibraryRoots());
        return new URLClassLoader(urls);
    }

    private URL[] toUrl(VirtualFile[] libraryRoots) {
        final URL[] urls = new URL[libraryRoots.length];
        int         i    = 0;
        for (final VirtualFile virtualFile : libraryRoots) {
            try {
                urls[i++] = new URL(VirtualFileManager.constructUrl("file", virtualFile.getPresentableUrl()));
            }
            catch (final MalformedURLException ignored) {}
        }
        return urls;
    }

    //~ Methods ......................................................................................................................................

    /** return the if the module or any of its dependencies has a mm directory. */
    public static boolean hasMMDir(Module module) {
        final VirtualFile mmDir = getMMDir(module);
        if (mmDir != null) return true;

        for (final Module dependency : PsiUtils.getDependentModules(module))
            if (hasMMDir(dependency)) return true;

        return false;
    }

    /** Returns 'mm' dir for module. */
    @Nullable public static VirtualFile getMMDir(@NotNull final Module module) {
        for (final VirtualFile sourceRoot : FileUtils.getSourceRoots(module)) {
            if (sourceRoot.getPath().endsWith(META_MODEL_SOURCES_PATH) || sourceRoot.getPath().endsWith(META_MODEL_TEST_PATH)) return sourceRoot;
        }
        return null;
    }
}  // end class MMModuleComponent
