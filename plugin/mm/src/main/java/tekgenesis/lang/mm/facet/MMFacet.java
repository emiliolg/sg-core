
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import java.io.File;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.ProjectUtils;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * MM Facet.
 */
public class MMFacet extends Facet<MMFacetConfiguration> {

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives module, name and configuration */
    public MMFacet(@NotNull Module module, String name, @NotNull MMFacetConfiguration configuration) {
        super(getFacetType(), module, name, configuration, null);
    }

    //~ Methods ......................................................................................................................................

    @Override public void initFacet() {
        StartupManager.getInstance(getModule().getProject()).runWhenProjectIsInitialized(this::initializeFacet);
    }  // end method initFacet

    private void commitDirectoriesAndConfigurationsSettings(final Module module, final boolean tests) {
        ApplicationManager.getApplication().invokeAndWait(() -> {
                final ModifiableRootModel modifiable = ModuleRootManager.getInstance(module).getModifiableModel();
                ApplicationManager.getApplication().runWriteAction(() -> {
                    final boolean generatedAndSourceRoots = FacetConfigurations.createGeneratedAndSourceRoots(this, modifiable, tests);
                    boolean       runConfigAdded          = false;
                    if (!ProjectUtils.isSuiGenerisProject(module.getProject()))
                        runConfigAdded = FacetConfigurations.createMetaModelRunConfiguration(this, modifiable);
                    if (generatedAndSourceRoots || runConfigAdded) modifiable.commit();
                });
            },
            ModalityState.defaultModalityState());
    }

    private void initializeFacet() {
        // AndroidResourceFilesListener.notifyFacetInitialized(AndroidFacet.this);

        if (ApplicationManager.getApplication().isUnitTestMode()) return;

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            final Module  module  = getModule();
            final Project project = module.getProject();

            if (project.isDisposed()) return;

            final String parent = new File(module.getModuleFilePath()).getParent().replace(FacetConfigurations.IDEA_MODULES, "");

            if (parent != null) {
                final boolean tests = new File(parent, "src/test/mm").exists();

                final MMFacetConfiguration configuration = getConfiguration();

                if (isEmpty(configuration.getGenSourcesDir())) {
                    if (tests) configuration.setGenSourcesDir(parent + "/src_managed/test");
                    else configuration.setGenSourcesDir(parent + "/src_managed/main");
                }

                commitDirectoriesAndConfigurationsSettings(module, tests);
            }
        });
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if given module has an MM Facet. */
    public static boolean hasFacet(Module module) {
        return FacetManager.getInstance(module).getFacetByType(ID) != null;
    }

    /** Returns the generated content entry. */
    @Nullable public static ContentEntry getGeneratedContentEntry(ModifiableRootModel modifiableRootModel) {
        final String generatedSourcesPath = getGeneratedSourcesPath(modifiableRootModel.getModule());
        return generatedSourcesPath == null ? null : getContentEntry(modifiableRootModel, generatedSourcesPath);
    }

    /** Returns generated sources path. */
    @Nullable public static String getGeneratedSourcesPath(@NotNull Module module) {
        final MMFacet mmFacet = getInstance(module);
        return mmFacet != null ? mmFacet.getConfiguration().getGenSourcesDir() : null;
    }

    /** Sets generated sources path for the given module. */
    public static void setGeneratedSourcesPath(Module module, String path) {
        final MMFacet instance = getInstance(module);
        if (instance != null) instance.getConfiguration().setGenSourcesDir(path);
    }

    /** Refreshes generated output directory. */
    static void refreshGeneratedOutputDir(String oldGeneratedSourcesPath, ModuleRootModel modifiableRootModel) {
        final String generatedSourcesPath = getGeneratedSourcesPath(modifiableRootModel.getModule());
        if (generatedSourcesPath != null && !generatedSourcesPath.equals(oldGeneratedSourcesPath)) {
            final File newGeneratedSourcesFile = new File(generatedSourcesPath);
            if (!newGeneratedSourcesFile.exists()) newGeneratedSourcesFile.mkdirs();
            final File mmFolder = new File(newGeneratedSourcesFile, "mm");
            if (!mmFolder.exists()) mmFolder.mkdir();
            final ContentEntry generatedRoot = getContentEntry(modifiableRootModel.getContentEntries(), generatedSourcesPath);
            if (generatedRoot != null) {
                final VirtualFile sourceFolder = FileUtils.refreshAndFindVFile(mmFolder.getPath());
                if (sourceFolder != null) generatedRoot.addSourceFolder(sourceFolder, false);
                if (!isEmpty(oldGeneratedSourcesPath)) {
                    final ContentEntry oldContentEntry = getContentEntry(modifiableRootModel.getContentEntries(), oldGeneratedSourcesPath);
                    if (oldContentEntry != null) {
                        removeSourceFolder(oldContentEntry, oldGeneratedSourcesPath + "/mm");
                        Files.remove(new File(oldGeneratedSourcesPath));
                    }
                }
            }
        }
    }

    @Nullable static ContentEntry getContentEntry(ModifiableRootModel modifiableRootModel, String contentPath) {
        return getContentEntry(modifiableRootModel.getContentEntries(), contentPath);
    }
    @Nullable static ContentEntry getContentEntry(ContentEntry[] entries, String contentPath) {
        for (final ContentEntry contentEntry : entries) {
            final VirtualFile file = contentEntry.getFile();
            if (file != null && contentPath.startsWith(file.getPath())) return contentEntry;
        }
        return null;
    }

    /** Return facet type. */
    static MMFacetType getFacetType() {
        return (MMFacetType) FacetTypeRegistry.getInstance().findFacetType(ID);
    }

    @Nullable static MMFacet getInstance(Module module) {
        return FacetManager.getInstance(module).getFacetByType(getFacetType().getId());
    }

    private static void removeSourceFolder(ContentEntry oldContentEntry, String oldGeneratedSourcesPath) {
        SourceFolder toRemove = null;
        for (final SourceFolder sourceFolder : oldContentEntry.getSourceFolders()) {
            if (sourceFolder.getUrl().endsWith(oldGeneratedSourcesPath)) {
                toRemove = sourceFolder;
                break;
            }
        }
        if (toRemove != null) oldContentEntry.removeSourceFolder(toRemove);
    }

    //~ Static Fields ................................................................................................................................

    public static final FacetTypeId<MMFacet> ID = new FacetTypeId<>("mm");
}  // end class MMFacet
