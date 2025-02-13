
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.index.IndexManager;
import tekgenesis.index.IndexSearcher;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;

import static tekgenesis.common.core.Constants.WAIT_FOR_INDEX;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.persistence.TableMetadata.forName;
import static tekgenesis.persistence.TableMetadata.searchableEntities;

/**
 * Elastic Search Service.
 */
public class ElasticSearchService extends Service {

    //~ Constructors .................................................................................................................................

    /** Service constructor. */
    public ElasticSearchService(ServiceManager manager) {
        super(manager, SERVICE_NAME, SERVICE_START_ORDER);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void doShutdown() {
        Context.getContext().getSingleton(IndexManager.class).shutdown();
    }

    @Override protected void doStart() {
        checkIndex(getEnvironment());
        if (Boolean.getBoolean(WAIT_FOR_INDEX))
            try {
                Context.getContext().getSingleton(IndexManager.class).waitForIndexingThreads();
            }
            catch (final InterruptedException e) {
                logger.error("Error starting ES.", e);
            }
    }

    //~ Methods ......................................................................................................................................

    /** Check indexes initial status. */
    public static void checkIndex(Environment env) {
        final IndexManager       indexManager = Context.getContext().getSingleton(IndexManager.class);
        final Seq<IndexSearcher> entities     = entities(env);

        indexManager.createIndicesForEntities(entities);
        indexManager.indexEntities(entities);
    }

    /** Get searchable entities db tables. */
    @NotNull public static Seq<IndexSearcher> entities(Environment env) {
        return searchableEntities(env).map(e -> forName(e).getIndexSearcher()).filter(Option::isPresent).map(Option::get);
    }

    //~ Static Fields ................................................................................................................................

    private static final String SERVICE_NAME        = ElasticSearchService.class.getSimpleName();
    private static final int    SERVICE_START_ORDER = 5;
}  // end class ElasticSearchService
