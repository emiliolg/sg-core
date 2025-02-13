
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.*;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;

import static java.util.Collections.binarySearch;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.MAX_STRING;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.isQualified;
import static tekgenesis.common.util.Files.toSystemIndependentName;

/**
 * A repository containing {@link MetaModel} objects.
 */

public class ModelRepository implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final List<String>                            domains        = new ArrayList<>();
    private final ConcurrentSkipListMap<QName, MetaModel> models         = new ConcurrentSkipListMap<>();
    private final MultiMap<String, MetaModel>             modelsByFile   = MultiMap.createSortedMultiMap();
    private final MultiMap<String, MetaModel>             modelsBySchema = MultiMap.createSortedMultiMap();

    //~ Methods ......................................................................................................................................

    /** Add a MetaModel to the repository. */
    public void add(@NotNull MetaModel model) {
        models.put(model.getKey(), model);
        modelsByFile.put(toSystemIndependentName(model.getSourceName()), model);
        addModelBySchema(model);
        addDomain(model.getDomain());
    }

    /** Delete a MetaModel from the repository. */
    public void delete(@NotNull QName key) {
        final MetaModel model = remove(key);
        modelsByFile.remove(toSystemIndependentName(model.getSourceName()), model);
        modelsBySchema.remove(model.getSchema(), model);
    }

    /**
     * Delete all models contained in the specified sourceFile. Return the list of models deleted
     */
    public ImmutableCollection<MetaModel> deleteAll(String sourceName) {
        final ImmutableCollection<MetaModel> ms = getModelsByFile(sourceName);
        for (final MetaModel m : ms) {
            remove(m.getKey());
            if (!Predefined.isEmpty(m.getSchema())) modelsBySchema.remove(m.getSchema(), m);
        }
        modelsByFile.removeAll(toSystemIndependentName(sourceName));
        return ms;
    }

    /** Get all packages . */
    public ImmutableList<String> getDomains() {
        return immutable(domains);
    }

    /** Retrieve a MetaModel with the given Key. */
    @NotNull public Option<MetaModel> getModel(QName key) {
        return key.isEmpty() ? Option.empty() : option(models.get(key));
    }

    /** Get a MetaModel with the given name and type. */
    @NotNull public <T extends MetaModel> Option<T> getModel(String name, Class<T> c) {
        return getModel("", name).castTo(c);
    }

    /** Get a MetaModel with the given key and type. */
    @NotNull public <T extends MetaModel> Option<T> getModel(QName key, Class<T> c) {
        return getModel(key).castTo(c);
    }

    /** Retrieve a MetaModel with the given default Domain and (optionally fully qualified) name. */
    @NotNull public Option<MetaModel> getModel(String defaultDomain, String name) {
        return getModel(isQualified(name) ? createQName(name) : createQName(defaultDomain, name));
    }

    /**
     * Get a MetaModel with the given default Domain, (optionally fully qualified) name and type.
     */
    @NotNull public <T extends MetaModel> Option<T> getModel(String defaultDomain, String name, Class<T> c) {
        return getModel(defaultDomain, name).castTo(c);
    }

    /** Return all Repository models. */
    public ImmutableCollection<MetaModel> getModels() {
        return immutable(models.values());
    }

    /** Return all Repository models (specify true to include dependencies if available). */
    public ImmutableCollection<MetaModel> getModels(boolean includeDependencies) {
        return immutable(models.values());
    }

    /** Return all the models with a given domain (Package). */
    public ImmutableCollection<MetaModel> getModels(String domain) {
        return Colls.immutable(modelsByDomain(domain));
    }

    /** Return all the models with a given domain (Package). */
    public <T extends MetaModel> Seq<T> getModels(String domain, Class<T> c) {
        return filter(modelsByDomain(domain), c);
    }

    // Todo check normalization (canonisation??) of source name
    /** Get all the Models for a given file. */
    public ImmutableCollection<MetaModel> getModelsByFile(String sourceName) {
        return modelsByFile.get(toSystemIndependentName(sourceName));
    }

    /** Returns a collection of all the MetaModels for the specified schema. */
    @NotNull public Collection<MetaModel> getModelsBySchema(@NotNull String schema) {
        return modelsBySchema.get(schema);
    }

    /** Returns a collection of all the schemas. */
    @NotNull public Collection<String> getSchemas() {
        return modelsBySchema.keys();
    }

    /** Adds all models to repository. */
    void addAll(Iterable<MetaModel> metaModels) {
        for (final MetaModel model : metaModels)
            add(model);
    }

    Collection<MetaModel> modelsByDomain(String domain) {
        return models.subMap(createQName(domain, ""), createQName(domain, MAX_STRING)).values();
    }

    private void addDomain(String domain) {
        final int n = binarySearch(domains, domain);
        if (n < 0) domains.add(-n - 1, domain);
    }

    private void addModelBySchema(@NotNull MetaModel model) {
        final String schema = model.getSchema();
        if (!schema.isEmpty()) {
            final ImmutableCollection<MetaModel> metaModels = modelsBySchema.get(schema);
            for (final MetaModel mm : metaModels) {
                if (mm.getName().equals(model.getName()))
                    throw new RepositoryException("A Metamodel named '" + model.getName() + "' already exists in schema '" + schema + "'.");
            }
            modelsBySchema.put(schema, model);
        }
    }

    private MetaModel remove(QName key) {
        final MetaModel model  = models.remove(key);
        final String    domain = key.getQualification();
        if (modelsByDomain(domain).isEmpty()) removeDomain(domain);
        return model;
    }

    private void removeDomain(String domain) {
        final int n = binarySearch(domains, domain);
        if (n >= 0) domains.remove(n);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 210800012824049389L;
}  // end class ModelRepository
