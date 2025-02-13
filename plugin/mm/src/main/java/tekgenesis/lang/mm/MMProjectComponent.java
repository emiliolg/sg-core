
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import javax.swing.*;

import com.intellij.ProjectTopics;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.messages.MessageBusConnection;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.facet.MMFacet;

import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.filter;

/**
 * Component of Project used by plugin to add Listener when the Project is being loaded, may do more
 * in future.
 */
public class MMProjectComponent implements ProjectComponent {

    //~ Instance Fields ..............................................................................................................................

    private MessageBusConnection busConnection = null;

    private final Project               project;
    private final LibraryTable.Listener listener = new LibraryTable.Listener() {
            @Override public void afterLibraryAdded(Library library) {
                buildModuleRepositories();
            }

            @Override public void afterLibraryRenamed(Library library) {
                buildModuleRepositories();
            }

            @Override public void beforeLibraryRemoved(Library library) {}

            @Override public void afterLibraryRemoved(Library library) {
                buildModuleRepositories();
            }
        };

    private MMProjectListener     projectListener = null;
    private final MMGraphSettings settings        = new MMGraphSettings();

    //~ Constructors .................................................................................................................................

    /**
     * Component of Project used by plugin to add Listener when the Project is being loaded, may do
     * more in future.
     */
    public MMProjectComponent(Project project) {
        this.project = project;
    }

    //~ Methods ......................................................................................................................................

    /** Build all module repositories. USE WITH CAUTION. */
    public synchronized void buildModuleRepositories() {
        final Seq<Module> modules = filter(asList(ModuleManager.getInstance(project).getSortedModules()), MMFacet::hasFacet);

        for (final Module module : modules) {
            final MMModuleComponent component = module.getComponent(MMModuleComponent.class);
            component.initializeModelRepository();
        }
    }

    public void disposeComponent() {
        ToolWindowManager.getInstance(project).unregisterToolWindow(MM_GRAPH);
        deRegisterVirtualFileListener();
        // Disconnect bus
        if (busConnection != null) busConnection.disconnect();
    }

    public void initComponent() {
        final StartupManager manager = StartupManager.getInstance(project);

        manager.registerPostStartupActivity(() -> registerWindow(MM_GRAPH, ToolWindowAnchor.TOP, MMFileType.ENTITY_ICON));

        // Connect bus
        busConnection = project.getMessageBus().connect(project);
        busConnection.subscribe(VirtualFileManager.VFS_CHANGES, new MMResourceEditorListener(project));
        busConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new MMFileEditorListener());
        busConnection.subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleDependencyListener());
        busConnection.subscribe(ProjectTopics.MODULES, new MMModuleListener());

        manager.runWhenProjectIsInitialized(this::buildModuleRepositories);
    }

    public void projectClosed() {
        deRegisterVirtualFileListener();
    }

    public void projectOpened() {
        LibraryTablesRegistrar.getInstance().getLibraryTable(project).addListener(listener);
        registerVirtualFileListener();
    }

    @NotNull public String getComponentName() {
        return "MMProjectComponent";
    }

    private void deRegisterVirtualFileListener() {
        final VirtualFile projectFile = project.getProjectFile();
        if (projectListener != null && projectFile != null) projectFile.getFileSystem().removeVirtualFileListener(projectListener);
    }

    private void registerVirtualFileListener() {
        final VirtualFile projectFile = project.getProjectFile();
        if (projectFile != null) {
            projectListener = new MMProjectListener(project);
            projectFile.getFileSystem().addVirtualFileListener(projectListener);
        }
    }

    private void registerWindow(String name, ToolWindowAnchor anchor, Icon icon) {
        final ToolWindow window = ToolWindowManager.getInstance(project).registerToolWindow(name, true, anchor);
        window.hide(null);
        window.setAvailable(false, null);
        window.setIcon(icon);
    }
    /** Project Settings. */
    private MMGraphSettings getSettings() {
        return settings;
    }

    //~ Methods ......................................................................................................................................

    /** Gets settings for the project. */
    public static MMGraphSettings getSettings(Project project) {
        return getInstance(project).getSettings();
    }
    /** Gets ProjectComponent for the project. */
    private static MMProjectComponent getInstance(Project project) {
        return project.getComponent(MMProjectComponent.class);
    }

    //~ Static Fields ................................................................................................................................

    public static final String MM_GRAPH = "MMGraph";
}  // end class MMProjectComponent
