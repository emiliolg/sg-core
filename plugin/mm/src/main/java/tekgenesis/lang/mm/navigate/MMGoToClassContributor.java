
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.navigate;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.type.MetaModel;

import static tekgenesis.lang.mm.ProjectUtils.getAllModules;
import static tekgenesis.lang.mm.psi.PsiUtils.getModelRepository;
import static tekgenesis.lang.mm.psi.PsiUtils.getNavigationItemForMetaModel;

/**
 * Go to Class contributor for MMFiles.
 */
class MMGoToClassContributor implements ChooseByNameContributor {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        //J-
        return getAllModules(project)  //
               .flatMap(module -> getModelRepository(module).getModels(includeNonProjectItems)
                                  .filter(mm -> mm.getFullName().equals(name))
                                  .flatMap(mm -> getNavigationItemForMetaModel(project, module, mm, includeNonProjectItems)))
               .toArray(NavigationItem[]::new);
        //J+
    }

    @NotNull @Override public String[] getNames(Project project, boolean includeNonProjectItems) {
        return getProjectMetaModels(project, includeNonProjectItems).map(MetaModel::getFullName).toSet().toArray(String[]::new);
    }

    //~ Methods ......................................................................................................................................

    static Seq<MetaModel> getProjectMetaModels(@NotNull Project project, boolean includeNonProjectItems) {
        return getAllModules(project)  //
               .flatMap(module -> getModelRepository(module).getModels(includeNonProjectItems));
    }
}
