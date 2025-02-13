
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.util.Files;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.DbConstants;
import tekgenesis.database.DbIntrospector;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.core.Constants.HASH_SALT;
import static tekgenesis.database.DatabaseType.HSQLDB;
import static tekgenesis.database.DbMacro.*;
import static tekgenesis.database.introspect.TableInfo.Element.*;

/**
 * Metadata Information about a database table.
 */
public class TableInfo extends SchemaObject<TableInfo> {

    //~ Instance Fields ..............................................................................................................................

    private final EnumMap<Element, Map<String, ? extends TableObject<?>>> elements;
    private transient boolean                                             notLoaded;
    private PrimaryKey                                                    primaryKey;
    private final TableType                                               tableType;

    //~ Constructors .................................................................................................................................

    TableInfo(final SchemaInfo schema, final String tableName, @Nullable String remarks, TableType type) {
        super(schema, tableName, remarks);
        tableType  = type;
        primaryKey = null;
        notLoaded  = true;
        elements   = new EnumMap<>(Element.class);
    }

    //~ Methods ......................................................................................................................................

    /** The name as a a QName. */
    public String asQName() {
        return getQName(getSchema().getPlainName(), getName());
    }

    /** Write the sql statements to create the foreign keys for the specified table. */
    public void dropForeignKeys(final PrintWriter w) {
        for (final ForeignKey fk : getForeignKeys())
            fk.dropSql(w);
    }

    /** Write the sql statements to create the foreign keys for the specified table. */
    public void dumpForeignKeys(final PrintWriter w) {
        for (final ForeignKey fk : getForeignKeys()) {
            fk.dumpSql(w);
            w.println();
        }
    }
    /**
     * Write the Sql DDL statements to the specified writer. If lexSorted == false then the order
     * for columns inside a table will be based on the column position in the table If lexSorted ==
     * true then columns will be sorted in lexicographical order.
     */
    public void dumpSql(final Writer writer, final boolean lexSorted) {
        final PrintWriter w = Files.printWriter(writer);
        w.printf("create table %s (%n", asQName());
        final PrimaryKey pk = getPrimaryKey();

        boolean nl = false;
        for (final Column column : lexSorted ? getColumns().sorted(Comparator.comparing(Column::getName)) : getColumns()) {
            if (nl) w.println(",");
            nl = true;
            w.print("\t");
            if (!lexSorted) w.print(column.toString());
            else {
                // Hack until defaults are generated
                final String def = column.defaultValue;
                column.defaultValue = null;
                w.print(column.toString());
                column.defaultValue = def;
            }
        }
        final ImmutableCollection<Index> uniques = getUniques();

        if (!pk.isUndefined()) w.printf(",%n%n\t%s", pk.generateSql());
        for (final Index unique : uniques)
            w.printf(",%n\t%s", unique.generateUniqueSql());

        w.println();
        w.println(");;");
    }  // end method dumpSql

    /** DumpTableIndices. */
    public void dumpTableIndices(final PrintWriter w) {
        for (final Index ix : getIndices()) {
            if (!ix.isSystem()) {
                ix.dumpSql(w);
                w.println();
            }
        }
    }

    /** Load all introspected data data. */
    public void loadAll() {
        if (notLoaded) {
            getChecks();
            getUniques();
            getColumns();
            getForeignKeys();
            getPrimaryKey();
            getIndices();
            notLoaded = false;
        }
    }

    /** Returns true if the tables are equivalent. */
    public boolean sameAs(TableInfo tt) {
        loadAll();
        tt.loadAll();
        for (final Element e : Element.values()) {
            if (elementsDiffer(tt, e)) return false;
        }
        return primaryKey.sameAs(tt.primaryKey);
    }

    /** Returns the check with the specified name. */
    public Check getCheck(final String name) {
        return cast(elementMap(CHECK).get(name));
    }

    /** Return the checks for this table. */
    public ImmutableCollection<Check> getChecks() {
        return getElements(CHECK);
    }

    /** Return the column with the specified name. */
    public Column getColumn(String name) {
        return cast(elementMap(COLUMN).get(name));
    }

    /** Return the columns for this table. */
    public ImmutableCollection<Column> getColumns() {
        return getElements(COLUMN);
    }

    /** Returns the element with the specified name and type. */
    public <T extends TableObject<T>> T getElement(final String name, Element element) {
        return cast(elementMap(element).get(name));
    }
    /** Returns the elements of this type. */
    public <T extends TableObject<T>> ImmutableCollection<T> getElements(Element element) {
        return cast(immutable(elementMap(element).values()));
    }

    /** Returns the table Foreign Key with the specified name. */
    public ForeignKey getForeignKey(final String name) {
        return cast(elementMap(FOREIGN_KEY).get(name));
    }

    /** Returns the table Foreign Keys. */
    public ImmutableCollection<ForeignKey> getForeignKeys() {
        return getElements(FOREIGN_KEY);
    }

    /** Returns the index with the specified name. */
    public Index getIndex(final String name) {
        return cast(elementMap(INDEX).get(name));
    }

    /** Returns the table indices. */
    public ImmutableCollection<Index> getIndices() {
        return getElements(INDEX);
    }

    /** Returns the table primary key. */
    @NotNull public PrimaryKey getPrimaryKey() {
        if (primaryKey == null) primaryKey = getRetriever().retrievePrimaryKey(this);
        return primaryKey;
    }
    /** Returns the table type. */
    public TableType getTableType() {
        return tableType;
    }

    /** Return the unique constraint with the specified name. */
    public Index getUnique(final String name) {
        return cast(elementMap(UNIQUE).get(name));
    }

    /** Return the unique constraints for this table. */
    public ImmutableCollection<Index> getUniques() {
        return getElements(UNIQUE);
    }

    void addFk(final Map<String, ForeignKey> result, final String name, QName references, final Map<Integer, FkColumn> fkColumns) {
        if (!name.isEmpty()) result.put(name, new ForeignKey(name, references, fkColumns.values()));
        fkColumns.clear();
    }

    void addIndex(final List<Index> result, final String name, final boolean unique, final Map<Integer, IndexColumn> idxColumns) {
        if (!name.isEmpty()) result.add(new Index(name, unique, idxColumns.values()));
        idxColumns.clear();
    }

    DbIntrospector getIntrospector() {
        return getSchema().getIntrospector();
    }

    private <T extends TableObject<T>> Map<String, T> elementMap(Element element) {
        Map<String, T> map = cast(elements.get(element));
        if (map == null) {
            map = cast(getRetriever().retrieve(this, element));
            elements.put(element, map);
        }
        return map;
    }

    private <T extends TableObject<T>> boolean elementsDiffer(final TableInfo tt, final Element e) {
        final Map<String, T> froms = elementMap(e);
        final Map<String, T> tos   = tt.elementMap(e);

        if (froms.size() != tos.size()) return true;

        for (final T from : froms.values()) {
            final T to = tos.get(from.getName());
            if (to == null || !from.sameAs(to)) return true;
        }
        return false;
    }

    private DatabaseType getDatabaseType() {
        return getSchema().getIntrospector().getDatabaseType().getType();
    }

    private MetadataRetriever getRetriever() {
        return getSchema().getIntrospector().getRetriever();
    }

    //~ Methods ......................................................................................................................................

    public static PrintWriter dropConstraint(final PrintWriter pw, final String name) {
        return pw.printf("drop constraint %s;;%n%n", name);
    }
    public static void generateAlterTable(final PrintWriter pw, String name, String schema) {
        pw.printf("alter  table %s%n\t", getQName(schema, name));
    }

    public static String getConstrainedName(String name, String suffix) {
        return Strings.truncate(name, suffix, "_", MAX_DB_ID_LENGHT).toUpperCase();
    }

    /** Get QName from elements. */
    public static String getQName(final String schema, final String name) {
        return format(DbConstants.QNAME, schema, name);
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_DB_ID_LENGHT = 30;

    private static final long serialVersionUID = 3257290248802284852L;

    //~ Enums ........................................................................................................................................

    public enum Element { COLUMN, INDEX, CHECK, FOREIGN_KEY, UNIQUE }

    //~ Inner Classes ................................................................................................................................

    /**
     * Check constraints for a Table.
     */
    public class Check extends TableObject<Check> {
        final List<String>    cols;
        private final String  condition;
        private final boolean enabled;

        Check(final String name, final String condition, final boolean enabled) {
            super(name);
            this.condition = condition;
            this.enabled   = enabled;
            cols           = new ArrayList<>();
        }

        /** Returns true if the check affects this column. */
        public boolean hasColumn(String name) {
            return cols.contains(name);
        }

        @Override public boolean sameAs(Check to) {
            return condition.equals(to.condition) && getName().equals(to.getName());
        }

        @Override public String toString() {
            return getCondition();
        }

        /** returns the columns this check affects. */
        public ImmutableList<String> getColumns() {
            return immutable(cols);
        }

        /** Return check Condition. */
        public String getCondition() {
            return condition;
        }

        /** Returns true if the check is enabled. */
        public boolean isEnabled() {
            return enabled;
        }

        private static final long serialVersionUID = -8135833449976813163L;
    }  // end class Check

    public class Column extends TableObject<Column> {
        private String        defaultValue;
        private final boolean nullable;
        private final int     ordinal;
        private final String  sequenceName;
        private final int     sequenceStart;
        private boolean       serial;
        private final SqlType sqlType;

        @SuppressWarnings("ConstructorWithTooManyParameters")
        Column(final String name, SqlType sqlType, final int ordinal, final boolean nullable, boolean serial, final String sequenceName,
               @Nullable final String defaultValue) {
            super(name);
            this.sqlType      = sqlType;
            this.ordinal      = ordinal;
            this.sequenceName = sequenceName;
            this.defaultValue = defaultValue;
            this.serial       = serial;
            this.nullable     = nullable && !serial;
            sequenceStart     = 1;
        }

        /** formats the type. */
        public String formatType() {
            return serial ? format("%s(%d,%s)", sqlType.getSqlKind() == SqlKind.INT ? Serial : BigSerial, getSequenceStart(), getSequenceName())
                          : sqlType.format();
        }

        /** Returns true if the columns are equivalent. */
        public boolean sameAs(Column tc) {
            return sqlType == tc.sqlType && nullable == tc.nullable && equal(defaultValue, tc.defaultValue) && serial == tc.serial;
        }

        @Override public String toString() {
            final StringBuilder r = new StringBuilder();
            r.append(format("%-33s ", getName()));
            if (nullable && defaultValue == null) r.append(formatType());
            else {
                r.append(format("%-16s", formatType()));
                if (defaultValue != null) r.append(" default ").append(defaultValue);
                if (sqlType.getSqlKind() == SqlKind.BOOLEAN)
                    r.append(" CheckBoolConstraint(")
                        .append(getConstrainedName(getTable().getName() + "_" + getName(), "B"))
                        .append(", ")
                        .append(getName())
                        .append(")");
                if (!nullable) r.append(" not null");
            }
            return r.toString();
        }  // end method toString

        /** Returns the defaultValue or null if none present. */
        @Nullable public String getDefaultValue() {
            return defaultValue;
        }

        /** Returns true if the column is optional. */
        public boolean isOptional() {
            return nullable;
        }

        /** Returns true if the field is an identity one. */
        public boolean isSerial() {
            return serial;
        }

        /** Return ordinal (position) number for column. */
        public int getOrdinal() {
            return ordinal;
        }

        /** Returns the name of the sequence for Serial fields. */
        public String getSequenceName() {
            return sequenceName.isEmpty() && serial ? getTable().getName() + "_SEQ" : sequenceName;
        }

        /** Return the start of the sequence for serial columns. */
        public int getSequenceStart() {
            return sequenceStart;
        }

        /** Returns the {@link SqlType} for the column). */
        public SqlType getType() {
            return sqlType;
        }

        private static final long serialVersionUID = -6855385762977224678L;
    }  // end class Column

    public static class FkColumn {
        @NotNull private final String name;
        @NotNull private final String pkName;

        FkColumn(@NotNull final String name, @NotNull final String pkName) {
            this.name   = name;
            this.pkName = pkName;
        }

        @Override public boolean equals(Object o) {
            return this == o || o instanceof FkColumn && name.equals(((FkColumn) o).name) && pkName.equals(((FkColumn) o).pkName);
        }

        @Override public int hashCode() {
            return name.hashCode() + pkName.hashCode() * HASH_SALT;
        }

        @Override public String toString() {
            return pkName + "=" + name;
        }

        /** Return column name. */
        @NotNull public String getName() {
            return name;
        }

        /** Return referenced column name. */
        @NotNull public String getPkName() {
            return pkName;
        }
    }

    public class ForeignKey extends TableObject<ForeignKey> {
        private final ImmutableList<FkColumn> fkColumns;
        private final QName                   references;

        private ForeignKey(final String name, final QName referencedTable, final Collection<FkColumn> cols) {
            super(name);
            references = referencedTable;
            fkColumns  = fromIterable(cols);
        }

        /** Drop foreign key. */
        public void dropSql(final PrintWriter w) {
            generateAlterTable(w, getTable().getName(), getTable().getSchema().getPlainName());
            dropConstraint(w, getName());
        }

        /** Generate foreign key. */
        public void dumpSql(final PrintWriter w) {
            final TableInfo referencedTable = getReferencedTableInfo();

            final String schema = getSchema().getName();
            if (!schema.equals(referencedTable.getSchema().getName())) {
                w.printf("-- if %s%n", NeedsGrantReference);
                w.printf("grant references on %s to SchemaOrUser(%s);;%n", referencedTable.asQName(), schema);
                w.println("-- end");
            }

            w.printf("alter table %s add constraint %s%n", asQName(), getName());
            w.printf("\tforeign key(%s)%n", getFkColumns().mkString(", "));
            w.printf("\treferences %s(%s);;%n%n", referencedTable.asQName(), getPkColumns().mkString(", "));
        }

        @Override public boolean sameAs(ForeignKey to) {
            return references.equals(to.references) && fkColumns.equals(to.fkColumns);
        }

        /** Returns Index Columns. */
        public ImmutableList<FkColumn> getColumns() {
            return fkColumns;
        }

        /** Returns the name of the Foreign key columns. */
        public Seq<String> getFkColumns() {
            return fkColumns.map(FkColumn::getName);
        }

        /** Returns the name of the primary key columns. */
        public Seq<String> getPkColumns() {
            return fkColumns.map(FkColumn::getPkName);
        }

        /** Return referenced table. */
        public QName getReferencedTable() {
            return references;
        }

        /** Return referenced table. */
        public TableInfo getReferencedTableInfo() {
            final SchemaInfo refS = getSchema().getIntrospector().getSchema(references.getQualification());
            return refS.getTable(references.getName()).get();
        }

        private static final long serialVersionUID = -8740103403688021348L;
    }  // end class ForeignKey

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public class Index extends TableObject<Index> {
        private final ImmutableList<IndexColumn> ixColumns;

        private final boolean unique;

        Index(final String name, final boolean unique, final Collection<IndexColumn> cols) {
            super(name);
            ixColumns   = fromIterable(cols);
            this.unique = unique;
        }

        /** Dump Sql to generate Index. */
        public void dumpSql(final PrintWriter w) {
            w.printf("create %s IndexName(%s, %s)%n", unique ? "unique index" : "index", getSchema().getPlainName(), getName());
            w.printf("\ton %s (%s);;%n%n", asQName(), getColumns().toStrings().mkString(", "));
        }

        /** sql to generate unique constraint key. */
        public String generateUniqueSql() {
            return format("constraint %s unique (%s)", getName(), getColumns().mkString(", "));
        }

        @Override public boolean sameAs(Index to) {
            return (unique == to.unique && ixColumns.equals(to.ixColumns));
        }

        /** Returns Index Columns. */
        public ImmutableList<IndexColumn> getColumns() {
            return ixColumns;
        }

        /** Returns true if the index is unique. */
        public boolean isUnique() {
            return unique;
        }

        /** Returns true if the index is a system one. */
        public boolean isSystem() {
            return getName().contains("SYS_IDX");
        }

        private static final long serialVersionUID = -8740103403688021348L;
    }  // end class Index

    public static class IndexColumn {
        private final boolean descending;
        private final String  name;

        IndexColumn(final String columnName, final boolean descending) {
            name            = columnName;
            this.descending = descending;
        }

        @Override public boolean equals(Object obj) {
            return this == obj || obj instanceof IndexColumn && descending == ((IndexColumn) obj).descending && name.equals(((IndexColumn) obj).name);
        }

        @Override public int hashCode() {
            return name.hashCode() + (descending ? HASH_SALT : 0);
        }

        @Override public String toString() {
            return name + (descending ? "desc" : "");
        }

        /** Returns true if is order in descending way. */
        public boolean isDescending() {
            return descending;
        }

        /** Returns column name. */
        public String getName() {
            return name;
        }
    }

    public class PrimaryKey extends TableObject<PrimaryKey> {
        private final ImmutableList<Column> pkColumns;

        PrimaryKey(final String name, final Collection<Column> cols) {
            super(name);
            pkColumns = fromIterable(cols);
            if (pkColumns.size() == 1) {
                final Column first = pkColumns.get(0);
                if (!getSchema().isCurrent() && "ID".equals(first.getName()) && first.getType().getSqlKind() == SqlKind.INT && !first.isSerial() &&
                    !first.isOptional()) first.serial = true;
            }
        }

        /** sql to generate primary key. */
        public String generateSql()
        {
            return format("constraint %s primary key(%s)", getName(), getColumnNames().mkString(", "));
        }

        /** Returns true if the Primary keys are equivalent. */
        public boolean sameAs(PrimaryKey that) {
            return getName().equals(that.getName()) && getColumnNames().equals(that.getColumnNames());
        }

        /** Return column names. */
        public Seq<String> getColumnNames() {
            return pkColumns.map(Column::getName);
        }

        /** Get primary key pkColumns. */
        public ImmutableList<Column> getColumns() {
            return pkColumns;
        }

        /** Returns true if this refers to an undefined primary key. */
        public boolean isUndefined() {
            return getName().isEmpty() && pkColumns.isEmpty();
        }

        /** Returns true if the primary key is the default one. */
        public boolean isDefault() {
            if (pkColumns.size() != 1) return false;
            final Column column = pkColumns.get(0);
            return column.isSerial() && "id".equalsIgnoreCase(column.getName()) && column.getType().getSqlKind() == SqlKind.INT;
        }

        boolean isPrimaryKey(String indexName) {
            return indexName.equals(getName()) || getDatabaseType() == HSQLDB && indexName.startsWith("SYS_IDX_" + getName());
        }

        private static final long serialVersionUID = -8189328053606044845L;
    }  // end class PrimaryKey

    public abstract class TableObject<This extends MetadataObject<This>> extends MetadataObject<This> {
        protected TableObject(@NotNull final String name) {
            super(name);
        }

        /** Return the Table for this column. */
        @NotNull public final TableInfo getTable() {
            return TableInfo.this;
        }

        @NotNull @Override String getQualification() {
            return TableInfo.this.getFullName();
        }

        private static final long serialVersionUID = -6635747157443366253L;
    }
}  // end class TableInfo
