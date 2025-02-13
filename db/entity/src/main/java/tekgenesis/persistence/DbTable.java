
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
import java.util.Collection;
import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Reflection;
import tekgenesis.index.Searcher;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.type.IntType;
import tekgenesis.type.Modifier;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.util.Reflection.findFieldOrFail;

/**
 * A Database Table reference.
 */
public abstract class DbTable<I extends EntityInstance<I, K>, K> extends TableLike<I> implements Cloneable {

    //~ Instance Fields ..............................................................................................................................

    private String            alias;
    private EntityTable<I, K> impl;

    // private final QName tableQName;

    private final TableMetadata<I, K>                   metadata;
    private ImmutableList<TableField<?>>                primaryKey;
    private ImmutableList<ImmutableList<TableField<?>>> secondaryKeys;

    //~ Constructors .................................................................................................................................

    protected DbTable(final Class<I> entityClass, String schemaName, String tableName, String sequenceName, EnumSet<Modifier> modifiers,
                      CacheType cacheType) {
        impl          = null;
        primaryKey    = ImmutableList.empty();
        secondaryKeys = ImmutableList.empty();
        alias         = "";
        //J-
        metadata         =
            new TableMetadata<I, K>(entityClass, schemaName, tableName, sequenceName, modifiers, cacheType) {
                @NotNull @Override public ImmutableList<ImmutableList<TableField<?>>> getSecondaryKeys()      { return secondaryKeys; }
                @NotNull @Override public ImmutableList<TableField<?>>                getPrimaryKey()         { return primaryKey; }
                @NotNull @Override public Option<? extends Searcher>                  getSearcher()           { return searcher(); }
                @NotNull @Override public K                                           keyFromString(String s) { return strToKey(s); }
            };
        //J+
    }

    //~ Methods ......................................................................................................................................

    /** Generate an Alias for this table. */
    public abstract DbTable<I, K> as(String alias);

    @Override public String asTableExpression() {
        return "QName(" + metadata.getSchemaName() + ", " + metadata.getTableName() + ")" + (alias.isEmpty() ? "" : " " + alias);
    }

    /** Expose Table Metadata. */
    public TableMetadata<I, K> metadata() {
        return metadata;
    }

    public String toString() {
        return metadata.getTableName();
    }

    protected TableField.Bool boolField(String fieldName, String columnName) {
        return metadata.addField(new TableField.Bool(this, fieldFor(fieldName), columnName));
    }

    protected TableField.Clob clobField(String fieldName, String columnName, int length) {
        return metadata.addField(new TableField.Clob(this, fieldFor(fieldName), columnName, length));
    }

    /** Generate an Alias for this table. */
    @SuppressWarnings("UnnecessaryLocalVariable")
    protected <T extends DbTable<I, K>> T createAlias(final T t, final String aliasName) {
        final DbTable<I, K> dbTable = t;
        dbTable.alias = aliasName;
        dbTable.impl  = entityTable();
        return t;
    }

    protected abstract EntityTable<I, K> createEntityTable();

    protected TableField.Date dateField(String fieldName, String columnName) {
        return metadata.addField(new TableField.Date(this, fieldFor(fieldName), columnName));
    }

    protected TableField.Decimal decimalField(String fieldName, String columnName, boolean signed, int precision, int length) {
        return metadata.addField(new TableField.Decimal(this, fieldFor(fieldName), columnName, signed, precision, length));
    }
    protected TableField.DTime dTimeField(String fieldName, String columnName) {
        return metadata.addField(new TableField.DTime(this, fieldFor(fieldName), columnName));
    }

    /** Returns the entityTable related to this DbTable. */
    @NotNull protected final EntityTable<I, K> entityTable() {
        return impl != null ? impl : bindTable();
    }

    protected <E extends Enum<E> & Enumeration<E, ID>, ID> TableField.Enum<E, ID> enumField(String fieldName, String columnName, Class<E> clazz) {
        return metadata.addField(new TableField.Enum<>(this, fieldFor(fieldName), columnName, clazz));
    }

    protected <E extends Enum<E> & Enumeration<E, ID>, ID> TableField.EnumerationSet<E, ID> enumSetField(String fieldName, String columnName,
                                                                                                         Class<E> clazz) {
        return metadata.addField(new TableField.EnumerationSet<>(this, fieldFor(fieldName), columnName, clazz));
    }

    protected TableField.Int intField(String fieldName, String columnName) {
        return intField(fieldName, columnName, false, IntType.MAX_INT_LENGTH);
    }

    protected TableField.Int intField(String fieldName, String columnName, boolean signed, int length) {
        return metadata.addField(new TableField.Int(this, fieldFor(fieldName), columnName, signed, length));
    }

    protected TableField.LongFld longField(String fieldName, String columnName, boolean signed, int length) {
        return metadata.addField(new TableField.LongFld(this, fieldFor(fieldName), columnName, signed, length));
    }

    protected final void primaryKey(ImmutableList<TableField<?>> pkFields) {
        pkFields.forEach(TableField::primaryKey);
        primaryKey = pkFields;
    }

    protected TableField.Real realField(String fieldName, String columnName, boolean signed) {
        return metadata.addField(new TableField.Real(this, fieldFor(fieldName), columnName, signed));
    }

    protected TableField.Res resourceField(String fieldName, String columnName) {
        return metadata.addField(new TableField.Res(this, fieldFor(fieldName), columnName));
    }

    @NotNull protected Option<? extends Searcher> searcher() {
        return Option.empty();
    }

    protected final void secondaryKeys(final ImmutableList<ImmutableList<TableField<?>>> keys) {
        secondaryKeys = keys;
    }

    protected TableField.Str strField(String fieldName, String columnName, int length) {
        return metadata.addField(new TableField.Str(this, fieldFor(fieldName), columnName, length));
    }
    protected TableField.Str strInternField(String fieldName, String columnName, int length) {
        return metadata.addField(new TableField.StrIntern(this, fieldFor(fieldName), columnName, length));
    }

    protected abstract K strToKey(String s);

    String alias() {
        return alias;
    }

    void doBind() {
        impl = createEntityTable();
        TableFactory.bind(this);
    }

    @Override Collection<TableField<?>> fields() {
        return metadata.fieldMap().values();
    }

    @Override <I1 extends EntityInstance<I1, K1>, K1> DbTable<I1, K1> getDbTable() {
        return cast(this);
    }

    @Override boolean isSingleTable() {
        return true;
    }

    @Override Expr<?>[] getExpressions() {
        return Select.EMPTY_EXPR;
    }

    @Override Class<I> getType() {
        return metadata.getType();
    }

    private synchronized EntityTable<I, K> bindTable() {
        if (impl == null) doBind();
        return impl;
    }
    private Field fieldFor(final String fieldName) {
        return findFieldOrFail(metadata.getDataClass(), fieldName);
    }

    //~ Methods ......................................................................................................................................

    /** Get the DbTable for the given instance class name. */
    public static <I extends EntityInstance<I, K>, K> DbTable<I, K> forName(String instanceClassName) {
        return Reflection.<I>construct(instanceClassName).table();
    }
}  // end class DbTable
