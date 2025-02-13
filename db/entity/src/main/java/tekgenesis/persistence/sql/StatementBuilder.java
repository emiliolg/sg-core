
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import java.lang.Long;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Lazy;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.DbMacro;
import tekgenesis.database.SqlStatement;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Alias;
import tekgenesis.persistence.expr.Expr;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.database.SqlConstants.*;
import static tekgenesis.persistence.Criteria.*;
import static tekgenesis.persistence.Select.Flag.*;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * Factory for statement suppliers.
 */
@SuppressWarnings("ClassWithTooManyMethods")
class StatementBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final Supplier<StatementCache.Proto> checkAndLock;
    private final Database                       db;
    private final Supplier<StatementCache.Proto> deleteProto;

    private final EntityTable<?, ?>              et;
    private final StatementCache.Proto[]         findByKeyProto;
    private final Supplier<StatementCache.Proto> findProto;
    private final Supplier<StatementCache.Proto> insertProto;
    private final Supplier<StatementCache.Proto> insertWithKeyProto;
    private final Supplier<StatementCache.Proto> mergeProto;
    private final TableMetadata<?, ?>            metadata;
    private final String                         tableExpression;
    private final Supplier<StatementCache.Proto> updateProto;
    private final Supplier<StatementCache.Proto> updateTime;
    private final TableField.DTime               updateTimeField;

    //~ Constructors .................................................................................................................................

    /** Create a builder. */
    StatementBuilder(final Database db, DbTable<?, ?> dbTable) {
        this.db  = db;
        et       = EntityTable.forTable(dbTable);
        metadata = dbTable.metadata();

        tableExpression = dbTable.asTableExpression();
        findProto       = new Lazy<>(new Find());
        checkAndLock    = new Lazy<>(new CheckAndLock());
        deleteProto     = new Lazy<>(new Delete());
        updateTimeField = metadata.getUpdateTimeField();
        updateTime      = new Lazy<>(new Find() {
                    @NotNull @Override String fieldList() {
                        return ensureNotNull(updateTimeField).asSql(false);
                    }
                });
        findByKeyProto  = new StatementCache.Proto[metadata.getSecondaryKeys().size()];

        insertProto        = new ConditionalSupplier(new Insert());
        insertWithKeyProto = new ConditionalSupplier(new InsertWithKey());
        updateProto        = new ConditionalSupplier(new Update());
        mergeProto         = new ConditionalSupplier(db.getDatabaseType() == DatabaseType.POSTGRES ? new PostgresMerge() : new Merge());
    }

    //~ Methods ......................................................................................................................................

    void delete(Criteria[] condition) {
        final String c = criteriaToSql(condition, false);
        if (c != null) db.sqlStatement(deleteWhere(c)).execute();
    }

    SqlStatement delete(List<?> keys) {
        return db.sqlStatement(deleteWhere(listByKeysCondition(keys)));
    }

    //J-
    Supplier<StatementCache.Proto> find() { return findProto; }
    Supplier<StatementCache.Proto> delete() { return deleteProto; }
    Supplier<StatementCache.Proto> insert() { return insertProto; }
    Supplier<StatementCache.Proto> insertWithKey() { return insertWithKeyProto; }
        Supplier<StatementCache.Proto> merge() { return mergeProto; }
    Supplier<StatementCache.Proto> locking(Long version) { return new ConditionalSupplier(new Update(version)); }
    Supplier<StatementCache.Proto> update() { return updateProto; }
    Supplier<StatementCache.Proto> updateTime() { return updateTime; }
    Supplier<StatementCache.Proto> checkAndLock() { return checkAndLock; }
    //J+

    StatementCache.Proto findByKey(int keyId) {
        final StatementCache.Proto proto = findByKeyProto[keyId];
        return proto != null ? proto : makeFindByKeyProto(keyId);
    }

    int insert(final List<SetClause<?>> setClauses) {
        return db.sqlStatement(
                format("insert into %s %s values %s",
                    tableExpression,
                    map(setClauses, SetClause::getFieldAsSql),
                    map(setClauses, SetClause::getValueAsSql))).executeDml();
    }

    int insertOrUpdate(List<SetClause<?>> insertSetClauses, TableField<?>[] keyColumns, List<SetClause<?>> setClauses, Criteria[] condition) {
        final StrBuilder aliasedValues = new StrBuilder();
        final StrBuilder allFields     = new StrBuilder();
        final StrBuilder allValues     = new StrBuilder();
        final StrBuilder values        = new StrBuilder();

        for (final SetClause<?> s : insertSetClauses) {
            final String nm = s.getFieldAsSql();
            aliasedValues.appendFormat("%s %s", s.getValueAsSql(), nm);
            allFields.appendElement(nm);
            allValues.appendFormat("V.%s", nm);
            values.appendElement(s.getValueAsSql());
        }

        final String result = db.getDatabaseType() != DatabaseType.POSTGRES
                              ? buildMergeStatement(matchCondition(keyColumns),
                aliasedValues.toString(),
                allFields.toString(),
                allValues.toString(),
                setClausesToSql(setClauses),
                criteriaToSql(condition, false))
                              : buildPostgresMergeStatement(ImmutableList.fromArray(keyColumns).map(f -> f.asSql(false)).mkString(","),
                allFields.toString(), values.toString(), setClausesToSql(setClauses), criteriaToSql(condition, true));

        return db.sqlStatement(result).executeDml();
    }

    SqlStatement listByKeys(List<?> keys) {
        return db.sqlStatement(selectFromWhere(tableExpression, fieldsToSql(et.getFields(), false), listByKeysCondition(keys)));
    }

    int update(final List<SetClause<?>> setClauses, final Criteria[] condition) {
        final String c = criteriaToSql(condition, false);
        if (c == null) return 0;
        return db.sqlStatement(format("update %s\nset\n%s\nwhere %s", tableExpression, setClausesToSql(setClauses), c)).executeDml();
    }

    EntityTable<?, ?> getTable() {
        return et;
    }

    @NotNull private String buildMergeStatement(String matchCondition, String values, String allFields, String allValues, String updateSet,
                                                @Nullable String updateCondition) {
        //J-
        return format(
                "merge into %s\n" +
                "using Values(V, %s) on (%s)\n" +
                "%s\n" +
                "when not matched then insert (%s) values (%s)",
            tableExpression,
            values,
            matchCondition,
            updateCondition == null || updateSet.isEmpty() ? ""
                               : updateCondition.isEmpty() ? format("when matched then update set %s", updateSet)
                                                           : format("when matched UpdateIf(\\(%s\\),\\(%s\\))", updateCondition, updateSet),
            allFields,
            allValues);
        //J+
    }

    @NotNull private String buildPostgresMergeStatement(String matchCondition, String allFields, String allValues, String setClauses,
                                                        @Nullable String updateCondition) {
        //J-
        return format(
                "insert into %s (%s) values (%s)\n" +
                "on conflict (%s)\n" +
                "do %s\n",
            tableExpression,
            allFields,
            allValues,
            matchCondition,
            updateCondition == null || setClauses.isEmpty() ? "nothing "
                                : updateCondition.isEmpty() ? format("update set %s", setClauses)
                                                            : format("update set %s where %s", setClauses, updateCondition));
        //J+
    }  // end method buildPostgresMergeStatement

    @NotNull private String deleteWhere(final String condition) {
        final StringBuilder s = new StringBuilder();
        append(s, DELETE, tableExpression);
        append(s, WHERE, condition);
        return s.toString();
    }

    @NotNull private String keyPredicate(final List<TableField<?>> keyFields) {
        final StrBuilder keyPredicate = new StrBuilder().startCollection(" and ");
        for (final TableField<?> f : keyFields)
            keyPredicate.appendFormat("%s = ?", f.getName());
        return keyPredicate.toString();
    }

    private String listByKeysCondition(List<?> keys) {
        final ImmutableList<TableField<?>> pks = metadata.getPrimaryKey();
        if (pks.size() == 1) {
            final TableField<Object> field  = cast(pks.get(0));
            final StrBuilder         values = new StrBuilder();
            for (final Object key : keys)
                values.appendElement(field.getValueAsSqlConstant(key));
            return format("%s in (%s)", field.getName(), values);
        }

        final StrBuilder condition = new StrBuilder().startCollection(" or ");
        for (final Object key : keys) {
            final StrBuilder       clause = new StrBuilder().startCollection(" and ");
            final ImmutableList<?> l      = ((Tuple<?, ?>) key).asList();

            for (int i = 0; i < pks.size(); i++) {
                final TableField<Object> field = cast(pks.get(i));
                clause.appendFormat("%s = %s", field.getName(), field.getValueAsSqlConstant(l.get(i)));
            }
            condition.appendElement(clause);
        }

        return condition.toString();
    }

    @NotNull private synchronized StatementCache.Proto makeFindByKeyProto(final int keyId) {
        final StatementCache.Proto p = findByKeyProto[keyId];
        if (p != null) return p;

        final ImmutableList<TableField<?>> keyFields = metadata.getSecondaryKeys().get(keyId);

        final String               statement = selectFromWhere(tableExpression, fieldsToSql(et.getFields(), false), keyPredicate(keyFields));
        final StatementCache.Proto findByKey = new StatementCache.Proto(db.preProcess(statement), keyFields);
        findByKeyProto[keyId] = findByKey;
        return findByKey;
    }

    @NotNull private String matchCondition(TableField<?>[] keyColumns) {
        final StrBuilder matchCondition = new StrBuilder().startCollection(" and ");
        for (final TableField<?> keyColumn : keyColumns) {
            final String name = keyColumn.getName();
            matchCondition.appendElement(format("%s = V.%s", keyColumn.asSql(true), name));
        }
        return matchCondition.toString();
    }

    @NotNull private String valueParameter(boolean useCurrentTime) {
        return useCurrentTime && !metadata.hasModifier(REMOTE) ? DbMacro.CurrentTime.name() : "?";
    }

    private boolean isCreateOrUpdateTime(TableField<?> f) {
        return f == updateTimeField || f == metadata.getCreationTimeField();
    }

    //~ Methods ......................................................................................................................................

    @Nullable static String buildNestedSelect(String selectItems, String sql) {
        return sql == null ? null : selectFromWhere("(" + sql + ")", selectItems, "");
    }

    @Nullable
    @SuppressWarnings("MethodWithTooManyParameters")
    static String buildSelect(String selectItems, String from, final Seq<Select.Join> joins, final Seq<Select.Union> unions, @Nullable String where,
                              final String groupBy, @Nullable String having, final String orderBy, EnumSet<Select.Flag> flags) {
        return where == null || having == null
               ? null
               : selectFromWhere(from,
            selectItems,
            createJoinConditions(joins),
            createUnionConditions(unions),
            where,
            groupBy,
            having,
            orderBy,
            flags);
    }

    static String convertToSql(final boolean qualify, final boolean createAlias, final Expr<?>... expressions) {
        final StrBuilder result = new StrBuilder().startCollection(", ");
        for (final Expr<?> e : expressions) {
            String item = e.asSql(qualify);
            if (createAlias && e instanceof Alias) item += " as " + e.getName();
            result.appendElement(item);
        }
        return result.toString();
    }

    @Nullable static String criteriaToSql(final Criteria[] conditionList, final boolean qualify) {
        final StrBuilder result = new StrBuilder().startCollection(' ' + AND + ' ');
        for (final Criteria c : conditionList) {
            if (c != null && c != EMPTY && c != TRUE) {
                if (c == FALSE) return null;
                result.appendElement(c.asSql(qualify));
            }
        }
        return result.toString();
    }

    static String fieldsToSql(Iterable<TableField<?>> fields, final boolean qualify) {
        final StrBuilder fieldsToSelect = new StrBuilder();
        for (final TableField<?> f : fields)
            fieldsToSelect.appendElement(f.asSql(qualify));
        return fieldsToSelect.toString();
    }

    static String orderSpecToSql(final OrderSpec<?>[] orderSpec, final boolean qualify) {
        final StrBuilder result = new StrBuilder();
        for (final OrderSpec<?> spec : orderSpec)
            result.appendElement(spec.asSql(qualify));
        return result.toString();
    }

    private static void append(final StringBuilder b, final String kw, final String condition) {
        if (!condition.isEmpty()) b.append(kw).append(condition).append('\n');
    }

    private static String createJoinConditions(Seq<Select.Join> joins) {
        final StrBuilder result = new StrBuilder().startCollection("\n");
        for (final Select.Join j : joins)
            result.appendElement(format("%s %s on (%s)", j.left ? LEFT_OUTER_JOIN : JOIN, j.getTarget(), criteriaToSql(j.criteria, true)));
        return result.toString();
    }

    private static String createUnionConditions(Seq<Select.Union> unions) {
        final StrBuilder result = new StrBuilder().startCollection("\n");
        for (final Select.Union u : unions)
            result.appendElement(format("%s %s", u.all ? UNION_ALL : UNION, u.getTarget()));
        return result.toString();
    }

    private static String selectFromWhere(String from, String tableFields, String condition) {
        final StringBuilder b = new StringBuilder();
        append(b, SELECT, tableFields);
        append(b, FROM, from);
        append(b, WHERE, condition);
        return b.toString();
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    private static String selectFromWhere(String from, String tableFields, final String joins, final String unions, String condition,
                                          final String groupBy, final String having, String orderBy, final EnumSet<Select.Flag> flags) {
        final StringBuilder b = new StringBuilder();
        append(b, SELECT + DISTINCT.asStringIfPresent(flags), tableFields);
        append(b, FROM, from);
        append(b, "", joins);
        append(b, WHERE, condition);
        append(b, GROUP_BY, groupBy);
        append(b, HAVING, having);
        append(b, "", unions);
        append(b, ORDER_BY, orderBy);
        final String forUpdate = FOR_UPDATE.asStringIfPresent(flags) + NO_WAIT.asStringIfPresent(flags) + SKIP_LOCKED.asStringIfPresent(flags);
        if (!forUpdate.isEmpty()) b.append(forUpdate).append('\n');
        return b.toString();
    }

    private static String setClausesToSql(final List<SetClause<?>> setClauseList) {
        final StrBuilder result = new StrBuilder().startCollection(",\n");
        for (final SetClause<?> s : setClauseList)
            result.appendElement(s.asSql());
        return result.toString();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A Builder for a Check Timestamp and lock Statement.
     */
    private class CheckAndLock extends StatementSupplier {
        @Override protected ImmutableList<TableField<?>> arguments() {
            final TableField<?> upd = ensureNotNull(updateTimeField);
            return listOf(upd, upd).append(metadata.getPrimaryKey()).toList();
        }

        @Override String statement() {
            return format("update %s set UPDATE_TIME = ? where cast(UPDATE_TIME as timestamp(3)) = ? and %s",
                tableExpression,
                keyPredicate(metadata.getPrimaryKey()));
        }
    }

    private static class ConditionalSupplier implements Supplier<StatementCache.Proto> {
        private final Lazy<StatementCache.Proto>     cachedSupplier;
        private final Supplier<StatementCache.Proto> statementSupplier;

        private ConditionalSupplier(Supplier<StatementCache.Proto> statementSupplier) {
            this.statementSupplier = statementSupplier;
            cachedSupplier         = new Lazy<>(statementSupplier);
        }

        @Nullable @Override public StatementCache.Proto get() {
            return Database.useClientTime() ? statementSupplier.get() : cachedSupplier.get();
        }
    }

    private class Delete extends StatementSupplier {
        @Override protected ImmutableList<TableField<?>> arguments() {
            return metadata.getPrimaryKey();
        }

        @Override String statement() {
            return deleteWhere(keyPredicate(metadata.getPrimaryKey()));
        }
    }

    private class Find extends StatementSupplier {
        @Override protected ImmutableList<TableField<?>> arguments() {
            return metadata.getPrimaryKey();
        }

        @NotNull String fieldList() {
            return fieldsToSql(et.getFields(), false);
        }

        @Override String statement() {
            return selectFromWhere(tableExpression, fieldList(), keyPredicate(metadata.getPrimaryKey()));
        }
    }

    private class Insert extends StatementSupplier {
        protected boolean includeField(final TableField<?> f) {
            return metadata.hasModifier(REMOTE) || !isCreateOrUpdateTime(f);
        }
        void addField(final TableField<?> f, final StrBuilder fieldNames, final StrBuilder fieldValues) {
            final String col = f.getName();
            fieldNames.appendElement(col);
            fieldValues.appendElement(value(f));
        }

        @Override ImmutableList<TableField<?>> arguments() {
            return et.getFields().filter(this::includeField).toList();
        }

        @Override String statement() {
            final StrBuilder fieldNames  = new StrBuilder();
            final StrBuilder fieldValues = new StrBuilder();

            for (final TableField<?> f : et.getFields())
                addField(f, fieldNames, fieldValues);
            return format("insert into %s (%s) values (%s)", tableExpression, fieldNames, fieldValues);
        }

        @NotNull String value(TableField<?> f) {
            return valueParameter(isCreateOrUpdateTime(f));
        }
    }

    private class InsertWithKey extends Insert {
        private String nextVal = "";

        @Override public StatementCache.Proto get() {
            if (db.getDatabaseType().has(DbMacro.SeqNextVal))
                nextVal = format("SeqNextVal(QName(%s,%s))", metadata.getSchemaName(), metadata.getSequenceName());
            return super.get();
        }
        protected boolean includeField(final TableField<?> f) {
            return !f.isPrimaryKey() && super.includeField(f);
        }

        @Override void addField(final TableField<?> f, final StrBuilder fieldNames, final StrBuilder fieldValues) {
            if (!f.isPrimaryKey()) super.addField(f, fieldNames, fieldValues);
            else if (!nextVal.isEmpty()) {
                fieldNames.appendElement(f.getName());
                fieldValues.appendElement(nextVal);
            }
        }
    }  // end class InsertWithKey

    /**
     * A Builder for a Merge Statement.
     */
    private class Merge extends Insert {
        @Override String statement() {
            final StrBuilder aliasedValues  = new StrBuilder();
            final StrBuilder fieldsToUpdate = new StrBuilder();
            final StrBuilder allFields      = new StrBuilder();
            final StrBuilder allValues      = new StrBuilder();
            final StrBuilder keyPredicate   = new StrBuilder().startCollection(" and ");

            for (final TableField<?> f : et.getFields()) {
                final String col = f.getName();
                aliasedValues.appendFormat("%s %s", value(f), col);
                if (f.isPrimaryKey()) keyPredicate.appendFormat("T.%s = V.%s", col, col);
                else fieldsToUpdate.appendFormat("T.%s = V.%s", col, col);
                allFields.appendElement(col);
                allValues.appendFormat("V.%s", col);
            }

            return format(
                "merge into %s T " +
                "using Values(V, %s)" +
                "on (%s) " +
                "when matched then" +
                "     update set %s " +
                "when not matched then" +
                "     insert (%s) values (%s)",
                tableExpression,
                aliasedValues,
                keyPredicate,
                fieldsToUpdate,
                allFields,
                allValues);
        }
    }  // end class Merge

    /**
     * A Merge for Postgres.
     */
    private class PostgresMerge extends Insert {
        @Override ImmutableList<TableField<?>> arguments() {               //
            return super.arguments()                                       //
                   .append(et.getFields().filter(f -> !f.isPrimaryKey()))  //
                   .toList();
        }

        @Override String statement() {
            final StrBuilder values         = new StrBuilder();
            final StrBuilder fieldsToUpdate = new StrBuilder();
            final StrBuilder allFields      = new StrBuilder();
            final StrBuilder keyFields      = new StrBuilder();

            for (final TableField<?> f : et.getFields()) {
                values.appendElement(value(f));
                final String col = f.getName();
                if (f.isPrimaryKey()) keyFields.appendElement(col);
                else fieldsToUpdate.appendFormat("%s = %s", col, valueParameter(f == updateTimeField));
                allFields.appendElement(col);
            }

            return format("insert into %s (%s) values (%s) on conflict (%s) do update set %s",
                tableExpression,
                allFields,
                values,
                keyFields,
                fieldsToUpdate);
        }
    }

    /**
     * An abstract Statement Supplier.
     */
    abstract class StatementSupplier implements Supplier<StatementCache.Proto> {
        @Override public StatementCache.Proto get() {
            return new StatementCache.Proto(db.preProcess(statement()), arguments());
        }

        ImmutableCollection<TableField<?>> arguments() {
            return et.getFields();
        }

        abstract String statement();
    }

    /**
     * A Builder for an Update Statement.
     */
    private class Update extends StatementSupplier {
        private final Long version;

        public Update() {
            version = null;
        }

        public Update(Long version) {
            this.version = version;
        }

        @Override protected ImmutableList<TableField<?>> arguments() {
            return et.getFields().filter(this::includeField).append(metadata.getPrimaryKey()).toList();
        }

        @Override String statement() {
            final StrBuilder         fieldsToUpdate = new StrBuilder();
            final TableField.LongFld versionFld     = metadata.getVersionField();
            for (final TableField<?> f : et.getFields())
                if (!f.isPrimaryKey()) fieldsToUpdate.appendFormat("%s = %s", f.getName(), valueParameter(f == updateTimeField));

            return format("update %s set %s where %s", tableExpression, fieldsToUpdate, keyPredicate(metadata.getPrimaryKey())) +
                   (version != null && versionFld != null ? " and INSTANCE_VERSION = " + versionFld.getValueAsSqlConstant(version) : "");
        }

        private boolean includeField(final TableField<?> f) {
            return !f.isPrimaryKey() && (metadata.hasModifier(REMOTE) || f != updateTimeField);
        }
    }
}
