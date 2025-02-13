
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;

/**
 * Listener in charge of managing Events when roots in ModuleChanged.
 */
class ModuleDependencyListener extends ModuleRootAdapter {

    //~ Methods ......................................................................................................................................

    @Override public void beforeRootsChange(ModuleRootEvent moduleRootEvent) {}

    @Override public void rootsChanged(ModuleRootEvent moduleRootEvent) {
        final Object source = moduleRootEvent.getSource();
        if (source instanceof Project) {
            final MMProjectComponent component = ((Project) source).getComponent(MMProjectComponent.class);
            component.buildModuleRepositories();
        }
    }
}
