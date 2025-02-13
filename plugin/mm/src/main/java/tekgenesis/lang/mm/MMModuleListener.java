
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.Function;

import org.jetbrains.annotations.NotNull;

/**
 * Listener in charge of managing Events when roots in ModuleChanged.
 */
class MMModuleListener implements ModuleListener {

    //~ Methods ......................................................................................................................................

    @Override public void beforeModuleRemoved(@NotNull Project project, @NotNull Module module) {}

    @Override public void moduleAdded(@NotNull Project project, @NotNull Module module) {
        if (project.isInitialized()) project.getComponent(MMProjectComponent.class).buildModuleRepositories();
    }

    @Override public void moduleRemoved(@NotNull Project project, @NotNull Module module) {
        if (project.isInitialized()) project.getComponent(MMProjectComponent.class).buildModuleRepositories();
    }

    @Override public void modulesRenamed(@NotNull Project project, @NotNull List<Module> modules, @NotNull Function<Module, String> fn) {}
}
