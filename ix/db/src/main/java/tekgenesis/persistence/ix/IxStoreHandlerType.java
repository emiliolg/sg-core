
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import org.jetbrains.annotations.NotNull;

import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.IxInstance;
import tekgenesis.persistence.StoreHandler;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.MOCK_PROTOCOL;

/**
 * Ix Store Handler Types.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public enum IxStoreHandlerType {

    //~ Enum constants ...............................................................................................................................

    NULL, MOCKED, STANDARD;

    //~ Methods ......................................................................................................................................

    @NotNull <T extends IxInstance<T, K>, K> StoreHandler<T, K> createHandler(DbTable<?, K> dbTable) {
        final DbTable<T, K> dt = cast(dbTable);
        switch (this) {
        case MOCKED:
            return new IxMockHandler<>(dt);
        case STANDARD:
            return new IxBatchRemoteStoreHandler<>(dt);
        default:
            return new IxNullStoreHandler<>(dt);
        }
    }

    //~ Methods ......................................................................................................................................

    /**
     * @param   url  The server url
     *
     * @return  The IxStoreHandlerType
     */
    public static IxStoreHandlerType get(String url) {
        return isEmpty(url) ? NULL : url.startsWith(MOCK_PROTOCOL) ? MOCKED : STANDARD;
    }
}
