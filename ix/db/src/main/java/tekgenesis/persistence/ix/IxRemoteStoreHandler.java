
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.util.Map;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.rest.exception.NotFoundRestException;
import tekgenesis.persistence.*;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.service.HeaderNames.*;
import static tekgenesis.persistence.IxUtil.utf8Encode;
import static tekgenesis.persistence.ix.IxStoreHandler.retrieveFields;

/**
 * Implementation of an StoreHandler for IxInstances.
 */
class IxRemoteStoreHandler<I extends IxInstance<I, K>, K> extends IxNullStoreHandler<I, K> {

    //~ Constructors .................................................................................................................................

    /** Create an IxEntityTable. */
    IxRemoteStoreHandler(@NotNull DbTable<I, K> dbTable) {
        super(dbTable);
    }

    //~ Methods ......................................................................................................................................

    public boolean checkLock(@NotNull K key) {
        final String          path     = getPath(createInstance(key));
        final PathResource<?> resource = createInvoker().resource(path);
        resource.header(X_OPER_DEF, path);

        final Map<String, Boolean> map = resource.get(new GenericType<Map<String, Boolean>>() {});
        // noinspection DuplicateStringLiteralInspection
        return map.get("locked");
    }

    /** Delete instance. */
    public void delete(I instance) {
        createInvoker().resource(getPath(instance)).delete();
    }

    @Nullable @Override public I find(@NotNull K key) {
        try {
            return createInvoker().resource(getPath(createInstance(key))).get(getType());
        }
        catch (final NotFoundRestException e) {
            return null;
        }
    }

    @Nullable @Override public I findPersisted(K key) {
        return find(key);
    }

    public void incr(@NotNull I instance, @NotNull TableField<?> field, double value, String mDateFieldName) {
        final PathResource<?> resource = createInvoker().resource(getPath());
        resource.header(X_OPER_DEF, format(INC_STR, field.getFieldName(), Double.toString(value), getIxEntityTable().getMDateFieldName()));
        resource.post(instance);
    }

    @Override public void insert(I instance, boolean generateKey) {
        createInvoker().resource(getPath()).post(instance);
    }

    @Override public <E> Select.Handler<E> select(final Select<E> select) {
        return new IxBaseSelectHandler<E, I, K>(select) {
            @Override protected Seq<I> values() {
                final String parameters = new IxCriteriaSerializer().accept(getWhere());

                validateQueryParameters(getIxEntityTable(), parameters);

                final String   path     = getPath() + (parameters.isEmpty() ? "" : "?" + parameters);
                final int      pageSize = select instanceof IxSelect ? ((IxSelect<?>) select).getPageSize() : 0;
                final DateTime newer    = select instanceof IxSelect ? ((IxSelect<?>) select).getNewer() : null;

                if (getLimit() != Long.MAX_VALUE || getOffset() > 0) throw new UnsupportedOperationException("Limit and offset");

                return new IxSeq<>(createInvoker(),
                    path,
                    buildHeaders(pageSize, newer, retrieveFields(getExpressions())),
                    IxRemoteStoreHandler.this.getType());
            }
        };
    }

    @Override public void update(I instance) {
        insert(instance, false);
    }

    String getIxTableName() {
        return getIxEntityTable().getIxTableName();
    }

    private Map<String, String> buildHeaders(int pageSize, @Nullable DateTime newer, @Nullable final TableField<?>[] fields) {
        final Map<String, String> headers = new TreeMap<>();

        if (pageSize > 0) headers.put(X_PAGE_SIZE, String.valueOf(pageSize));

        if (newer != null) {
            final IxEntityTable<I, K> t = getIxEntityTable();

            final String str;

            if (t.getGetMTimeFieldName() == null) str = format(NEWER_DATE_ONLY_STR, t.getMDateFieldName(), newer.toDateOnly());
            else str = format(NEWER_STR, t.getMDateFieldName(), newer.toDateOnly(), t.getGetMTimeFieldName(), newer.getDaySeconds());

            headers.put(X_NEWER, str);
        }
        if (fields != null && fields.length > 0) {
            final StrBuilder names = new StrBuilder();
            for (final TableField<?> f : fields)
                names.appendElement(f.getFieldName());
            headers.put(X_FIELDS, names.toString());
        }
        return headers;
    }

    private I createInstance(@NotNull K key) {
        return getEntityTable().getMetadata().createInstance(key);
    }

    private HttpInvoker createInvoker() {
        return IxUtil.createInvoker(getType());
    }

    private String keyAsString(@NotNull I instance) {
        final ImmutableList<TableField<?>> primaryKeyFields = getMetadata().getPrimaryKey();
        final String[]                     keyParts         = instance.keyAsString().split(":");

        for (int i = 0; i < primaryKeyFields.size(); i++) {
            final TableField<?> primaryKeyField = primaryKeyFields.get(i);
            final Class<?>      type            = primaryKeyField.getType();

            if ((type.equals(Integer.class) && Integer.parseInt(keyParts[i]) == Integer.MAX_VALUE) ||
                (type.equals(Integer.class) && Double.parseDouble(keyParts[i]) == Double.MAX_VALUE)) keyParts[i] = "null";
        }

        return ImmutableList.fromArray(keyParts).mkString(":");
    }

    @NotNull private IxEntityTable<I, K> getIxEntityTable()
    {
        return cast(getEntityTable());
    }

    private String getPath() {
        return '/' + getSchemaName() + '.' + getIxTableName() + '/';
    }

    private String getPath(I instance) {
        return getPath() + utf8Encode(keyAsString(instance));
    }

    //~ Static Fields ................................................................................................................................

    private static final String NEWER_STR           = "%s=%s,%s=%d";
    private static final String NEWER_DATE_ONLY_STR = "%s=%s";
    static final String         INC_STR             = "%s,%s;%s";
}  // end class IxRemoteStoreHandler
