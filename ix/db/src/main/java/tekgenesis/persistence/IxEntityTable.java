
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.ix.IxStoreHandler;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.mkString;

/**
 * IxEntityTable.
 */
public abstract class IxEntityTable<T extends IxInstance<T, K>, K> extends EntityTable<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private final List<Tuple<String, List<String>>> ixIndexesFields = new ArrayList<>();

    private String ixTableName    = null;
    private String mDateFieldName = null;
    private String mTimeFieldName = null;

    //~ Constructors .................................................................................................................................

    protected IxEntityTable(@NotNull DbTable<T, K> type) {
        super(type);
    }

    //~ Methods ......................................................................................................................................

    /** Add index/unique/pk definition. */
    public void addIndex(String name, String... fields) {
        ixIndexesFields.add(Tuple.tuple(name, ImmutableList.fromArray(fields)));
    }

    /** Check if the current fields match with any defined IX index/unique/pk. */
    public boolean applyForIndex(String... fields) {
        final Set<String> fieldSet = Colls.set(fields);

        Option<Tuple<String, List<String>>> match = Colls.first(ixIndexesFields,
                p -> p != null && Colls.forAll(fieldSet, field -> p.second().contains(field)));

        if (match.isEmpty())
            logger.warning(format("Unable to found an index to match '%s'. (Available indexes: %s )", fieldSet, mkString(ixIndexesFields)));
        else {
            // Check gaps in selected index
            final Tuple<String, List<String>> tuple       = match.get();
            final List<String>                indexFields = tuple.second();
            int                               i           = 0;
            for (; i < indexFields.size(); i++) {
                final String idxName = indexFields.get(i);

                if (!fieldSet.contains(idxName)) break;
            }
            if (fieldSet.size() > i) {
                logger.warning(
                    format("Index found for '%s', but the are gaps in the field's index ( indexes: %s )", mkString(fieldSet), mkString(indexFields)));
                match = Option.empty();
            }
        }

        return match.isPresent();
    }  // end method applyForIndex

    /** Check if the following instance is locked in IX. */
    public boolean checkLock(T instance) {
        StoreHandler<T, K> currentStoreHandler = getStoreHandler();
        if (!(currentStoreHandler instanceof IxStoreHandler))
            currentStoreHandler = ((DelegatingStoreHandler<T, K>) currentStoreHandler).getDelegate();
        return ((IxStoreHandler<T, K>) currentStoreHandler).checkLock(instance.keyObject());
    }

    /** Inc operation.* */
    @SuppressWarnings("SpellCheckingInspection")
    public void incr(@NotNull T instance, @NotNull TableField<?> field, double value) {
        StoreHandler<T, K> currentStoreHandler = getStoreHandler();
        if (!(currentStoreHandler instanceof IxStoreHandler))
            currentStoreHandler = ((DelegatingStoreHandler<T, K>) currentStoreHandler).getDelegate();
        ((IxStoreHandler<T, K>) currentStoreHandler).incr(instance, field, value);
    }

    /** Update the specified Object (all fields). */
    public T update(@NotNull final T instance) {
        getStoreHandler().update(instance);
        return instance;
    }

    /** Returns the field name user as mtime.* */
    @SuppressWarnings("SpellCheckingInspection")
    public String getGetMTimeFieldName() {
        return mTimeFieldName;
    }

    /**  */
    public ImmutableList<Tuple<String, List<String>>> getIxIndexes() {
        return Colls.immutable(ixIndexesFields);
    }

    /**  */
    public String getIxTableName() {
        return ixTableName;
    }

    @SuppressWarnings("JavaDoc")
    public void setIxTableName(String name) {
        ixTableName = name;
    }

    /** Returns the fieldname user as mdate.* */
    @SuppressWarnings("SpellCheckingInspection")
    public String getMDateFieldName() {
        return mDateFieldName;
    }

    @SuppressWarnings("JavaDoc")
    public void setMDateFieldName(String name) {
        mDateFieldName = name;
    }

    @SuppressWarnings("JavaDoc")
    public void setMTimeFieldName(String name) {
        if (isNotEmpty(name.trim())) mTimeFieldName = name;
    }

    @Override String storeType() {
        return IX_STORE_TYPE;
    }

    //~ Static Fields ................................................................................................................................

    public static final String IX_STORE_TYPE = "IX";

    private static final Logger logger = Logger.getLogger(IxEntityTable.class);
}  // end class IxEntityTable
