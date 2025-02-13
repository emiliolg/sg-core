
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.repository;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.collections.Colls.immutable;

/**
 * Model Repository with dependencies to other Repositories.
 */
public class MultiModuleRepository extends ModelRepository {

    //~ Instance Fields ..............................................................................................................................

    private final Set<ModelRepository> dependencies = new HashSet<>();

    //~ Methods ......................................................................................................................................

    /** Add dependency to other repository. */
    public void dependsOn(ModelRepository modelRepository) {
        dependencies.add(modelRepository);
    }

    /** get dependent ModelRepositories for module. */
    public Collection<ModelRepository> getDependencies() {
        return dependencies;
    }

    @Override public ImmutableList<String> getDomains() {
        return getDomains(true);
    }

    @NotNull @Override public Option<MetaModel> getModel(QName key) {
        if (key.isEmpty()) return Option.empty();
        Option<MetaModel> model = super.getModel(key);
        if (!model.isPresent()) {
            for (final ModelRepository dependency : dependencies) {
                model = dependency.getModel(key);
                if (model.isPresent()) break;
            }
        }
        return model;
    }

    @Override public ImmutableCollection<MetaModel> getModels(boolean includeDependencies) {
        return ImmutableList.build(builder -> {
            builder.addAll(getModels());
            if (includeDependencies) {
                for (final ModelRepository dependency : dependencies)
                    builder.addAll(dependency.getModels());
            }
        });
    }

    /** Return all the models with a given domain (Package). */
    @Override public <T extends MetaModel> Seq<T> getModels(String domain, Class<T> c) {
        Seq<T> models = Colls.filter(modelsByDomain(domain), c);
        if (models.isEmpty()) {
            for (final ModelRepository dependency : dependencies) {
                models = dependency.getModels(domain, c);
                if (!models.isEmpty()) break;
            }
        }
        return models;
    }
    /** returns Domains, boolean is to return depend domains or not. */
    private ImmutableList<String> getDomains(boolean getDependentDomains) {
        final TreeSet<String> domains = new TreeSet<>();
        domains.addAll(super.getDomains());
        if (getDependentDomains) {
            for (final ModelRepository dependency : dependencies)
                domains.addAll(dependency.getDomains());
        }
        return immutable(domains).toList();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8066980172328092803L;
}  // end class MultiModuleRepository
