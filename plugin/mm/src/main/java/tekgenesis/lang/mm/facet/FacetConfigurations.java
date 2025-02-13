
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.HyperlinkEvent;

import com.intellij.compiler.CompilerWorkspaceConfiguration;
import com.intellij.compiler.options.CompileStepBeforeRun;
import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.*;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.runner.MMConfigurationType;
import tekgenesis.lang.mm.runner.MMRunConfiguration;

import static java.io.File.separatorChar;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Facet Configuration utility class.
 */
class FacetConfigurations {

    //~ Constructors .................................................................................................................................

    private FacetConfigurations() {}

    //~ Methods ......................................................................................................................................

    /** Guess distribution installation directory based on module root. */
    @NotNull public static String guessInstallationDir(File parent) {
        File       installation        = new File(System.getProperty("user.home"), "/tekgen/distribution");
        final File epiphaniaSuigeneris = new File(parent.getPath(), "../../../genesis/suigeneris");
        if (epiphaniaSuigeneris.exists()) installation = epiphaniaSuigeneris;
        final File gulliverSuigeneris = new File(parent.getPath(), "../../../suigeneris");
        if (gulliverSuigeneris.exists()) installation = gulliverSuigeneris;
        final File genesisSuigeneris = new File(parent.getPath(), "../../suigeneris");
        if (genesisSuigeneris.exists()) installation = genesisSuigeneris;
        final File suigeneris = new File(parent.getPath(), "../suigeneris");
        if (suigeneris.exists()) installation = suigeneris;
        try {
            return installation.getCanonicalPath();
        }
        catch (final IOException e) {
            return installation.getAbsolutePath();
        }
    }

    static boolean createGeneratedAndSourceRoots(@NotNull MMFacet facet, @NotNull ModifiableRootModel model, boolean tests) {
        final Module       module           = facet.getModule();
        final Ref<Boolean> modelChangedFlag = Ref.create(false);

        // Initialize "src/main/mm" or "src/test/mm" and mark as source root.

        final String moduleRoot     = new File(module.getModuleFilePath()).getParent().replace(IDEA_MODULES, "");
        final String sourceRootPath = moduleRoot + separatorChar + "src" + separatorChar + (tests ? "test" : "main");
        initializeSourceRoot(model, sourceRootPath + separatorChar + "mm", SourceRootType.JAVA.asTest(tests), modelChangedFlag);
        initializeSourceRoot(model, sourceRootPath + separatorChar + "java", SourceRootType.JAVA.asTest(tests), modelChangedFlag);
        initializeSourceRoot(model, sourceRootPath + separatorChar + Constants.RESOURCES, SourceRootType.RESOURCES.asTest(tests), modelChangedFlag);

        // Initialize configurable generated source directory as generated source root.
        final String configurationGenSourcesDir = facet.getConfiguration().getGenSourcesDir();
        if (!isEmpty(configurationGenSourcesDir))
            initializeSourceRoot(model, configurationGenSourcesDir + separatorChar + "mm", SourceRootType.GENERATED.asTest(tests), modelChangedFlag);

        return modelChangedFlag.get();
    }

    static boolean createMetaModelRunConfiguration(MMFacet mmFacet, ModifiableRootModel modifiable) {
        final Module       module          = modifiable.getModule();
        final RunManagerEx runManager      = RunManagerEx.getInstanceEx(module.getProject());
        final boolean      hasNotMMConfigs = runManager.getConfigurationsList(MMConfigurationType.getInstance()).isEmpty();
        boolean            modified        = false;
        if (hasNotMMConfigs) {
            final RunnerAndConfigurationSettings runConfiguration = runManager.createRunConfiguration(module.getName(),
                    MMConfigurationType.getFactory());

            final MMRunConfiguration mmRunConfiguration = (MMRunConfiguration) runConfiguration.getConfiguration();
            mmRunConfiguration.setModule(module);

            @SuppressWarnings("rawtypes")
            final List<BeforeRunTask> tasks = new ArrayList<>();
            tasks.add(new CompileStepBeforeRun(module.getProject()).createTask(mmRunConfiguration));

            runManager.addConfiguration(runConfiguration, false, tasks, false);
            runManager.setSelectedConfiguration(runConfiguration);
            modified = true;
        }
        return modified;
    }

    /** Guess generated sources based on module root. */
    @NotNull
    @SuppressWarnings("DuplicateStringLiteralInspection")
    static File guessGeneratedSourcesDir(File parent) {
        return new File(parent, "src_managed" + File.separatorChar + "main");
    }

    @Nullable private static SourceFolder addSourceRoot(final ModifiableRootModel model, @NotNull final VirtualFile root,
                                                        @NotNull final SourceRootType type) {
        final ContentEntry entry = findContentEntryForRoot(model, root);

        if (entry == null) {
            final Project project = model.getProject();
            final String  message = "Cannot mark directory '" + FileUtil.toSystemDependentName(root.getPath()) +
                                    "' as source root, because it is not located under content root of module '" + model.getModule().getName() +
                                    "'\n<a href='fix'>Open Project Structure</a>";

            final Notification notification = new Notification("metamodel.configuration.notification.group",
                    "Configuration Error",
                    message,
                    NotificationType.ERROR,
                    new NotificationListener.Adapter() {
                        @Override protected void hyperlinkActivated(@NotNull Notification n, @NotNull HyperlinkEvent e) {
                            n.expire();
                            final ProjectStructureConfigurable configurable = ProjectStructureConfigurable.getInstance(project);

                            ShowSettingsUtil.getInstance()
                                .editConfigurable(project,
                                    configurable,
                                    () -> {
                                        final Module  module = model.getModule();
                                        final MMFacet facet  = MMFacet.getInstance(module);

                                        if (facet != null) configurable.select(facet, true);
                                    });
                        }
                    });
            Notifications.Bus.notify(notification, project);
            return null;
        }
        else return type.addSourceFolder(entry, root);
    }  // end method addSourceRoot

    private static boolean annotationsOnly(VirtualFile file) {
        return file != null && file.getName().startsWith("annotations-");
    }

    @Nullable private static VirtualFile createSourceRootIfNotExist(@NotNull final String path, @NotNull final ModifiableRootModel model,
                                                                    @NotNull final SourceRootType type, @NotNull Ref<Boolean> modelChangedFlag) {
        ApplicationManager.getApplication().assertIsDispatchThread();

        final File rootFile = new File(path);

        final boolean created;

        if (!rootFile.exists()) {
            if (!rootFile.mkdirs()) return null;
            created = true;
        }
        else created = false;

        final Module  module  = model.getModule();
        final Project project = module.getProject();

        if (project.isDisposed() || module.isDisposed()) return null;

        final VirtualFile root;

        if (created) root = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(rootFile);
        else root = LocalFileSystem.getInstance().findFileByIoFile(rootFile);

        if (root != null) {
            final String parent = root.getParent().getUrl();

            for (final ContentEntry entry : model.getContentEntries()) {
                for (final SourceFolder source : entry.getSourceFolders()) {
                    final String sourcePath = source.getUrl();
                    if (parent.contains(sourcePath)) entry.removeSourceFolder(source);
                }
            }

            boolean markedAsSource = false;

            for (final VirtualFile existingRoot : model.getSourceRoots()) {
                if (Comparing.equal(existingRoot, root)) markedAsSource = true;
            }

            if (markedAsSource) markedAsSource = ensureCorrectlyMarked(model, root, type, modelChangedFlag);

            if (!markedAsSource) {
                addSourceRoot(model, root, type);
                modelChangedFlag.set(true);
            }
        }
        return root;
    }  // end method createSourceRootIfNotExist

    private static boolean ensureCorrectlyMarked(ModifiableRootModel model, VirtualFile root, SourceRootType type, Ref<Boolean> modelChangedFlag) {
        final ContentEntry entry = findContentEntryForRoot(model, root);

        if (entry == null) return false;

        for (final SourceFolder sourceFolder : entry.getSourceFolders()) {
            if (root.equals(sourceFolder.getFile())) {
                if (!Comparing.equal(sourceFolder.getRootType(), type.type)) {
                    entry.removeSourceFolder(sourceFolder);
                    modelChangedFlag.set(true);
                    return false;
                }
                if (type.isGenerated()) {
                    final JavaSourceRootProperties props = sourceFolder.getJpsElement().getProperties(JavaModuleSourceRootTypes.SOURCES);
                    if (props != null && !props.isForGeneratedSources()) {
                        props.setForGeneratedSources(true);
                        modelChangedFlag.set(true);
                        return true;
                    }
                }
                break;
            }
        }
        return true;
    }

    @Nullable private static ContentEntry findContentEntryForRoot(@NotNull ModifiableRootModel model, @NotNull VirtualFile root) {
        ContentEntry entry = null;
        for (final ContentEntry candidate : model.getContentEntries()) {
            final VirtualFile contentRoot = candidate.getFile();
            if (contentRoot != null && VfsUtilCore.isAncestor(contentRoot, root, false)) entry = candidate;
        }
        return entry;
    }

    @Nullable private static VirtualFile initializeSourceRoot(@NotNull ModifiableRootModel model, @NotNull String sourceRootPath,
                                                              @NotNull SourceRootType type, @NotNull Ref<Boolean> modelChangedFlag) {
        VirtualFile sourceRoot = null;

        final VirtualFile root = createSourceRootIfNotExist(sourceRootPath, model, type, modelChangedFlag);
        if (root != null) sourceRoot = root;

        if (sourceRoot == null) sourceRoot = LocalFileSystem.getInstance().findFileByPath(sourceRootPath);

        if (sourceRoot != null) CompilerWorkspaceConfiguration.getInstance(model.getModule().getProject());

        return sourceRoot;
    }

    //~ Static Fields ................................................................................................................................

    static final String IDEA_MODULES = "/.idea/modules";

    private static final String SOURCES = "/sources";

    //~ Enums ........................................................................................................................................

    private enum SourceRootType {
        JAVA_TEST(JavaSourceRootType.TEST_SOURCE), GENERATED_TEST(JavaSourceRootType.TEST_SOURCE), RESOURCES_TEST(JavaResourceRootType.TEST_RESOURCE),
        JAVA(JavaSourceRootType.SOURCE, JAVA_TEST), GENERATED(JavaSourceRootType.SOURCE, GENERATED_TEST),
        RESOURCES(JavaResourceRootType.RESOURCE, RESOURCES_TEST);
        private final SourceRootType test;

        private final JpsModuleSourceRootType<?> type;

        SourceRootType(JpsModuleSourceRootType<?> type) {
            this(type, null);
        }

        SourceRootType(JpsModuleSourceRootType<?> type, @Nullable SourceRootType test) {
            this.type = type;
            this.test = test;
        }

        @SuppressWarnings("unchecked")
        public SourceFolder addSourceFolder(ContentEntry entry, VirtualFile root) {
            SourceFolder result = null;
            switch (this) {
            case JAVA:
            case JAVA_TEST:
            case RESOURCES:
            case RESOURCES_TEST:
                result = entry.addSourceFolder(root, type);
                break;
            case GENERATED:
            case GENERATED_TEST:
                result = entry.addSourceFolder(root,
                        (JpsModuleSourceRootType<JavaSourceRootProperties>) type,
                        JpsJavaExtensionService.getInstance().createSourceRootProperties("", true));
                break;
            }
            return result;
        }

        boolean isGenerated() {
            return this == GENERATED || this == GENERATED_TEST;
        }

        private SourceRootType asTest(boolean asTest) {
            return asTest && test != null ? test : this;
        }
    }
}  // end class FacetConfigurations
