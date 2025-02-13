
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.*;
import tekgenesis.common.core.*;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.util.Reflection;
import tekgenesis.database.RowMapper;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.Searcher;
import tekgenesis.properties.SchemaProps;
import tekgenesis.type.Modifier;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Seq.fromTo;
import static tekgenesis.common.core.Constants.ENTITY_LIST_FILE;
import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.common.util.Reflection.*;
import static tekgenesis.common.util.Resources.readResources;
import static tekgenesis.md.MdConstants.*;
import static tekgenesis.persistence.Criteria.*;
import static tekgenesis.persistence.expr.Expr.constant;
import static tekgenesis.persistence.expr.ExprOperator.GT;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * Expose Metadata for a DatabaseTable.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class TableMetadata<I extends EntityInstance<I, K>, K> {

    //~ Instance Fields ..............................................................................................................................

    private final CacheType                  cacheType;
    private final Class<?>                   dataClass;
    private final Map<String, TableField<?>> fieldMap;
    private final ImmutableList<Field>       innerFields;
    private final EnumSet<Modifier>          modifiers;
    private final String                     sequenceName;
    private final QName                      tableQName;
    private final Class<I>                   type;

    //J- Special fields
    @Nullable private TableField.DTime       creationTimeField;
    @Nullable private TableField.Str         creationUserField;
    @Nullable private TableField.DTime       deprecationTimeField;
    @Nullable private TableField.Str         deprecationUserField;
    @Nullable private TableField.DTime       updateTimeField;
    @Nullable private TableField.Str         updateUserField;
    @Nullable private TableField.LongFld     versionField;
    //J+

    //~ Constructors .................................................................................................................................

    TableMetadata(Class<I> type, String schemaName, String tableName, String sequenceName, EnumSet<Modifier> modifiers, CacheType cacheType) {
        this.type            = type;
        dataClass            = findField(type, DATA_FIELD_NAME).<Class<?>>map(Field::getType).orElse(type);
        tableQName           = QName.createQName(schemaName, tableName);
        this.cacheType       = cacheType;
        this.sequenceName    = sequenceName;
        this.modifiers       = modifiers;
        fieldMap             = new LinkedHashMap<>();
        innerFields          = solveInners(type);
        creationTimeField    = null;
        creationUserField    = null;
        deprecationTimeField = null;
        deprecationUserField = null;
        updateTimeField      = null;
        updateUserField      = null;
        versionField         = null;
    }

    //~ Methods ......................................................................................................................................

    /** Build Criteria to compare to the key by equality. */
    public Criteria buildKeyCriteria(K key) {
        return buildKeyCriteria(Tuple.asList(key));
    }

    /**
     * Creates a criteria to get instances whose primary key is greater than the argument.
     *
     * <p>e.g: key = (k1,k2,k3)</p>
     *
     * <p>returns K1 > k1 or ( K1 = k1 and K2 > k2) or ( K1 = k1 and K2 = k2 and K3 > k3)</p>
     */
    public Criteria buildKeyCriteriaGT(K key) {
        //J-
        final ImmutableList<?>             values = Tuple.asList(key);
        final ImmutableList<TableField<?>> keys   = getPrimaryKey();
        return anyOf(                                                                             // OR all parts
                     fromTo(0, keys.size() - 1)                                                   // one part for each key
                     .map(n -> keys.get(n).bool(GT, constant(values.get(n)))                      // always Kn > Vn
                               .and(n == 0 ? EMPTY                                                // AND all of Ki = Vi for i < n
                                           : allOf(keys.slice(0, n).zipWith(this::eq, values.slice(0, n))))));
        //J+
    }

    /** Creates an instance. */
    public I createInstance() {
        return construct(getType());
    }

    /** Creates an instance. with the specified key */
    public I createInstance(K key) {
        return setKey(createInstance(), key);
    }

    /** Returns the true if the Entity has an auto-generated key. */
    public boolean hasGeneratedKey() {
        return !sequenceName.isEmpty();
    }

    /** Check for a given modifier. */
    public boolean hasModifier(Modifier mod) {
        return modifiers.contains(mod);
    }

    /** Make key field based on an String. */
    public abstract K keyFromString(String keyAsString);

    /** Return the secondary key for the specified keyId. */
    @Nullable public <SK> SK keyObject(final I instance, final int keyId) {
        validateKeyId(keyId);

        final ImmutableList<TableField<?>> keyFields = getSecondaryKeys().get(keyId);
        if (keyFields.size() == 1) return cast(keyFields.get(0).getValue(instance));

        final ImmutableList<Object> kvs = keyFields.map(kf -> (Object) kf.getValue(instance)).toList();
        return kvs.size() == keyFields.size() ? Tuple.tupleFromList(kvs) : null;
    }

    /**
     * Update the special Audit fields That is Update,Creation,Deprecation Time and User Depending
     * on the flags creation and deprecation time will be updated. Restore means to clean up the
     * deprecation fields
     */
    public I updateAuditFields(@NotNull I instance, boolean create, boolean deprecate, boolean restore) {
        if (hasModifier(REMOTE)) return instance;
        if (restore) {
            if (deprecationTimeField != null) deprecationTimeField.setValue(instance, (DateTime) null);
            if (deprecationUserField != null) deprecationUserField.setValue(instance, null);
            return instance;
        }

        final DateTime now    = DateTime.current();
        final String   userId = updateUserField == null && deprecationUserField == null ? "" : SecurityUtils.getAuditUserId();

        if (updateTimeField != null) updateTimeField.setValue(instance, now);
        if (updateUserField != null) updateUserField.setValue(instance, userId);
        if (create) {
            if (creationTimeField != null) creationTimeField.setValue(instance, now);
            if (creationUserField != null) creationUserField.setValue(instance, userId);
        }
        else if (deprecate) {
            if (deprecationTimeField != null) deprecationTimeField.setValue(instance, now);
            if (deprecationUserField != null) deprecationUserField.setValue(instance, userId);
        }
        return instance;
    }
    /** Return the cache type. */
    @NotNull public CacheType getCacheType() {
        return cacheType;
    }

    /** Returns the creationTime Field. */
    @Nullable public TableField.DTime getCreationTimeField() {
        return creationTimeField;
    }

    /** Get The Data class (The inner class that holds the actual values). */
    public Class<?> getDataClass() {
        return dataClass;
    }

    /** Returns true if the entity can be used for remote views. */
    public boolean isRemotable() {
        return hasModifier(Modifier.REMOTABLE) || hasModifier(Modifier.DEFAULT_SEARCHABLE) || getSearcher().isPresent();
    }

    /** Gets a field by name. */
    @NotNull public <F extends TableField<?>> Option<F> getField(@NotNull String fieldName) {
        return Option.ofNullable(cast(fieldMap.get(fieldName)));
    }

    /** Gets a field of an specific type by name. */
    @NotNull public <F extends TableField<?>> Option<F> getField(@NotNull String fieldName, Class<F> fieldType) {
        return getField(fieldName).castTo(fieldType);
    }

    /** Get The specified Field or fail. */
    @NotNull public <F extends TableField<?>> F getFieldOrFail(String fieldName) {
        return this.<F>getField(fieldName).orElseThrow(() -> new IllegalStateException("Invalid field: " + fieldName));
    }

    /** Return the Table Fields. */
    @NotNull public ImmutableCollection<TableField<?>> getFields() {
        return immutable(fieldMap.values());
    }

    /** Returns the InstanceSearcher. */
    @NotNull public Option<IndexSearcher> getIndexSearcher() {
        return getSearcher().castTo(IndexSearcher.class);
    }

    /** Return the value of the field for the given instance value. */
    public <E extends InnerInstance<E, EK, ?, ?>, EK> InnerEntitySeq<E> getInner(I instance, String fieldName) {
        for (final Field inner : innerFields) {
            if (inner.getName().equals(fieldName)) return getNotNullFieldValue(instance, inner);
        }
        throw new IllegalArgumentException("Inner field not found: " + fieldName);
    }

    /** Get the fields that reference inner entities. */
    public ImmutableList<Field> getInnerFields() {
        return innerFields;
    }

    /** Sets the key fields of the instance. */
    public I setKey(I instance, K key) {
        getPrimaryKey().zip(Tuple.asList(key)).forEach(t -> t._1().setRawValue(instance, t._2()));
        return instance;
    }
    /** Returns the Primary Key Fields. */
    @NotNull public abstract ImmutableList<TableField<?>> getPrimaryKey();

    /** Returns a {@link RowMapper} for this table. */
    public RowMapper<I> getRowMapper() {
        return rs -> {
                   final I result = createInstance();
                   for (final TableField<?> f : fieldMap.values())
                       f.setFromResultSet(result, rs, rs.findColumn(f.getName()));
                   return result;
               };
    }

    /** Returns the schema name. */
    @NotNull public String getSchemaName() {
        return tableQName.getQualification();
    }

    /** Returns the InstanceSearcher. */
    @NotNull public abstract Option<? extends Searcher> getSearcher();

    /** Returns the Secondary Keys Fields. */
    @NotNull public abstract ImmutableList<ImmutableList<TableField<?>>> getSecondaryKeys();

    /** Return the sequence name. */
    @NotNull public String getSequenceName() {
        return sequenceName;
    }

    /** Returns the table name. */
    @NotNull public String getTableName() {
        return tableQName.getName();
    }

    /** Returns the Qualified table name. */
    @NotNull public QName getTableQName() {
        return tableQName;
    }

    /** the type of the Instance. */
    @NotNull public final Class<I> getType() {
        return type;
    }
    /** Returns the Type name. */
    @NotNull public String getTypeName() {
        return getType().getName();
    }

    /** Returns the Type name as a Qualified name. */
    @NotNull public QName getTypeQName() {
        return QName.createQName(getTypeName());
    }

    /** Returns the updateTime Field. */
    @Nullable public TableField.DTime getUpdateTimeField() {
        return updateTimeField;
    }

    /** Returns the version Field. */
    @Nullable public TableField.LongFld getVersionField() {
        return versionField;
    }

    @NotNull <F extends TableField<?>> F addField(F tableField) {
        final String fieldName = tableField.getFieldName();
        fieldMap.put(fieldName, tableField);
        switch (fieldName) {
        case UPDATE_TIME:
            updateTimeField = (TableField.DTime) tableField;
            break;
        case UPDATE_USER:
            updateUserField = (TableField.Str) tableField;
            break;
        case CREATION_TIME:
            creationTimeField = (TableField.DTime) tableField;
            break;
        case CREATION_USER:
            creationUserField = (TableField.Str) tableField;
            break;
        case VERSION_FIELD:
            versionField = (TableField.LongFld) tableField;
            break;
        case DEPRECATION_TIME:
            deprecationTimeField = (TableField.DTime) tableField;
            break;
        case DEPRECATION_USER:
            deprecationUserField = (TableField.Str) tableField;
            break;
        }
        return tableField;
    }

    Criteria buildKeyCriteria(ImmutableList<?> that) {
        return allOf(getPrimaryKey().zipWith(this::eq, that));
    }

    /** Return the map with the fields. */
    @NotNull final Map<String, TableField<?>> fieldMap() {
        return fieldMap;
    }

    void validateKeyId(final int keyId) {
        if (keyId < 0 || keyId >= getSecondaryKeys().size())
            throw new IllegalArgumentException(format("Illegal key Id %d for entity %s", keyId, getTypeName()));
    }

    private Criteria eq(TableField<?> f, Object k) {
        return f.eq(constant(k));
    }

    //~ Methods ......................................................................................................................................

    /** Copy all fields (Except the primary key) from one instance to a another. */
    public static <T extends EntityInstance<T, K>, K> void copyFields(T from, T to) {
        for (final TableField<?> f : from.metadata().getFields()) {
            final TableField<Object> field = cast(f);
            if (!field.isPrimaryKey()) field.setValue(to, field.getValue(from));
        }
    }

    /** Copy all Inner Instances from one instance to a another. */
    public static <T extends EntityInstance<T, K>, K> void copyInners(T from, T to) {
        from.metadata().getInnerFields().forEach(field -> copyInnerFields(getNotNullFieldValue(from, field), getNotNullFieldValue(to, field)));
    }

    /** Return all entity names topological sort by dependency. */
    public static ImmutableCollection<String> entities() {
        return immutable(TablesHolder.tables.keySet());
    }
    /** Get the Metadata for the given instance class name. */
    public static <I extends EntityInstance<I, K>, K> TableMetadata<I, K> forName(String instanceClassName) {
        return DbTable.<I, K>forName(instanceClassName).metadata();
    }

    /** Return all entity names excluding remote ones. */
    @NotNull public static Seq<String> localEntities(final Environment env) {
        return entities().filter(s -> !env.get(TablesHolder.schemaFor(s), SchemaProps.class).remote);
    }

    /** Return all schema names excluding remote ones. */
    @NotNull public static ImmutableSet<String> localSchemas(final Environment env) {
        return localEntities(env).map(TablesHolder::schemaFor).toSet();
    }

    /** Return the searchable Entities. */
    public static ImmutableList<String> searchableEntities(Environment env) {
        return localEntities(env).filter(entityName -> forName(entityName).getSearcher().isPresent()).toList();
    }

    /** Return the entities with their associated table names. */
    public static Map<String, String> tablesByEntity() {
        return TablesHolder.tables;
    }

    private static <T extends InnerInstance<T, K, ?, ?>, K> void copyInnerFields(final InnerEntitySeq<T> fromInner, final InnerEntitySeq<T> toInner) {
        for (final T from : fromInner) {
            final T to = toInner.add();
            copyFields(from, to);
            copyInners(from, to);
        }
    }
    private static ImmutableList<Field> solveInners(Class<?> type) {
        return ImmutableList.build(b -> {
            for (final Field field : Reflection.getFields(type)) {
                if (!isStatic(field.getModifiers()) && EntitySeq.Inner.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    b.add(field);
                }
            }
        });
    }

    //~ Inner Interfaces .............................................................................................................................

    // Lazy initialization holder class idiom for static fields
    private interface TablesHolder {
        Map<String, String> tables = Maps.map(readResources(ENTITY_LIST_FILE), value -> Strings.splitToTuple(value, " "));

        static String schemaFor(String entityName) {
            return extractQualification(tableFor(entityName));
        }
        static String tableFor(String entityName) {
            return tables.getOrDefault(entityName, "");
        }
    }
}  // end interface TableMetadata
