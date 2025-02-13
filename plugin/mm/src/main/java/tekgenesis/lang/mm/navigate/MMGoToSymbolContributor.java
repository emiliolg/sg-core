
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.navigate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.ModelField;
import tekgenesis.lang.mm.ProjectUtils;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiModelField;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.lang.mm.navigate.MMGoToClassContributor.getProjectMetaModels;

/**
 * Go to Class contributor for MMFiles.
 */
class MMGoToSymbolContributor implements ChooseByNameContributor {

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("OverlyNestedMethod")
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        final List<NavigationItem> items = new ArrayList<>();
        for (final Module module : ProjectUtils.getAllModules(project)) {
            for (final MetaModel mm : PsiUtils.getModelRepository(module).getModels()) {
                for (final ModelField f : mm.getChildren()) {
                    if (f.getName().equals(name)) {
                        final Option<PsiMetaModel<?>> metamodel = PsiUtils.getNavigationItemForMetaModel(project, module, mm, includeNonProjectItems);
                        if (metamodel.isPresent()) {
                            final PsiModelField field = metamodel.get().getFieldNullable(name);
                            if (field != null) items.add(field);
                        }
                    }
                }
            }
        }
        return items.toArray(new NavigationItem[items.size()]);
    }

    @NotNull @Override public String[] getNames(Project project, boolean includeNonProjectItems) {
        final Set<String> names = new LinkedHashSet<>();
        for (final MetaModel metaModel : getProjectMetaModels(project, includeNonProjectItems)) {
            for (final ModelField field : metaModel.getChildren()) {
                if (includeInSearch(field.getName())) names.add(field.getName());
            }
        }
        return names.toArray(new String[names.size()]);
    }

    private boolean includeInSearch(String name) {
        return isNotEmpty(name) && name.charAt(0) != FormConstants.AUTO_ID_PREFIX;
    }
}
