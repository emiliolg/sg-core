
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.lang.reflect.Method;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * A Factory for EntityTable objects.
 */
public class TableFactory {

    //~ Instance Fields ..............................................................................................................................

    private final MultiMap<Class<?>, EntityInitializer> initializer;

    private final StoreHandler.Factory storeHandlerFactory;

    //~ Constructors .................................................................................................................................

    /** Create the Factory. */
    public TableFactory(StoreHandler.Factory storeHandlerFactory) {
        this.storeHandlerFactory = storeHandlerFactory;
        initializer              = MultiMap.createMultiMap();
        for (final EntityInitializer i : ServiceLoader.load(EntityInitializer.class))
            initializer.put(i.getEntityType(), i);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create an EntityTable. Care may be taken when tables refers to other tables (as in the case
     * of Views). In the case of reinitialization of the factory the method will be called from the
     * {@link #setFactory(TableFactory)} one. This is not very tidy but the best that can be done
     * while we keep the static 'TABLE' field in Entities.
     */
    <T extends EntityTable<I, K>, I extends EntityInstance<I, K>, K> void createEntityTable(DbTable<I, K> t, String storeType) {
        final StoreHandler<I, K> handler = storeHandlerFactory.createHandler(storeType, t);
        t.entityTable().init(ensureNotNull(handler, "Cannot create Handler"));
        initialize(t.getType());
    }

    private void initialize(final Class<?> clazz) {
        try {
            for (final EntityInitializer i : initializer.get(clazz))
                i.initialize();
            for (final Method m : clazz.getDeclaredMethods()) {
                if (m.getDeclaredAnnotation(Initialize.class) != null) {
                    m.setAccessible(true);
                    m.invoke(null);
                }
            }
        }
        catch (final Exception e) {
            Logger.getLogger(TableFactory.class).error(e);
        }
    }

    //~ Methods ......................................................................................................................................

    /** Bind with the default (empty) store type. */
    @NotNull public static <I extends EntityInstance<I, K>, K> EntityTable<I, K> bind(DbTable<I, K> dbTable) {
        final EntityTable<I, K> entityTable = dbTable.entityTable();
        factory().createEntityTable(dbTable, entityTable.storeType());
        boundTables.add(dbTable);
        return entityTable;
    }

    /** Sets the singleton. */
    public static void setFactory(TableFactory factory) {
        instance = factory;
        boundTables.forEach(DbTable::doBind);
    }

    private static TableFactory factory() {
        if (instance == null) throw new IllegalStateException("Factory not initialized");
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static final Set<DbTable<?, ?>> boundTables = new LinkedHashSet<>();

    @Nullable private static TableFactory instance = null;
}  // end class TableFactory
