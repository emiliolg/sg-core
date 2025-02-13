
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect.delta;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Tuple;
import tekgenesis.database.introspect.SchemaInfo;
import tekgenesis.database.introspect.SqlKind;
import tekgenesis.database.introspect.SqlType;
import tekgenesis.database.introspect.TableInfo;
import tekgenesis.database.introspect.TableInfo.Element;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.JavaReservedWords.FALSE;
import static tekgenesis.common.util.JavaReservedWords.TRUE;
import static tekgenesis.database.DbMacro.*;
import static tekgenesis.database.SqlConstants.CURRENT_DATE;
import static tekgenesis.database.SqlConstants.CURRENT_TIMESTAMP;
import static tekgenesis.database.introspect.TableInfo.Element.*;
import static tekgenesis.database.introspect.TableInfo.dropConstraint;

/**
 * Generate Sql DDL statements to evolve from a given schema version to a new one.
 */
class TableDiffer extends MdDiffer<TableInfo> implements TableDeltas {

    //~ Instance Fields ..............................................................................................................................

    private final Map<Tuple<String, Element>, MdDelta> differs;

    private final SchemaInfo from;
    private final SchemaInfo to;

    //~ Constructors .................................................................................................................................

    /** Create the generator. */
    public TableDiffer(final SchemaInfo from, final SchemaInfo to) {
        super(to.getPlainName());
        this.from = from;
        this.to   = to;
        differs   = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public MdDelta diff(final String tableName, Element element) {
        final Tuple<String, Element> key = tuple(tableName, element);
        return differs.computeIfAbsent(key, k -> hasChanged(tableName) ? createDiffer(tableName, element).diff() : MdDelta.EMPTY);
    }

    @Override public void generate(final PrintWriter pw) {
        diff();

        for (final String name : getFromOnly())
            getFrom(name).dropForeignKeys(pw);

        // Drop foreign keys
        for (final String name : getChanged())
            diff(name, FOREIGN_KEY).dropElements(pw);

        super.generate(pw);

        // Create foreign keys
        for (final String name : getChanged())
            diff(name, FOREIGN_KEY).createElements(pw);

        for (final String name : getToOnly())
            getTo(name).dumpForeignKeys(pw);
    }

    @Override public boolean primaryKeyChange(final String name) {
        return !getFrom(name).getPrimaryKey().sameAs(getRenamedTo(name).getPrimaryKey());
    }

    @Override public boolean isMinor() {
        if (!getFromOnly().isEmpty() || !getRenamed().isEmpty()) return false;

        for (final String name : getChanged()) {
            for (final Element element : Element.values()) {
                if (!diff(name, element).isMinor()) return false;
            }
            if (primaryKeyChange(name)) return false;
        }
        return true;
    }

    @Override TableDeltas diff() {
        return (TableDeltas) super.diff();
    }

    @Override int diffWeight(TableInfo a, TableInfo b) {
        int diff = 0;
        for (final TableInfo.Column ac : a.getColumns()) {
            final TableInfo.Column bc = b.getColumn(ac.getName());
            if (bc == null || !ac.getType().equals(bc.getType())) ++diff;
        }
        for (final TableInfo.Column bc : b.getColumns()) {
            if (a.getColumn(bc.getName()) == null) ++diff;
        }
        return diff;
    }

    @Override void generateAlter(final PrintWriter pw, final String name) {
        generatePrimaryDropKeyChanges(pw, name);
        diff(name, COLUMN).generate(pw);
        diff(name, CHECK).generate(pw);
        diff(name, INDEX).generate(pw);
        diff(name, UNIQUE).generate(pw);
        generatePrimaryKeyAddChanges(pw, name);
    }

    void generateAlterTable(final PrintWriter pw, String name) {
        TableInfo.generateAlterTable(pw, name, getSchemaName());
    }

    @Override void generateCreate(final PrintWriter pw, final TableInfo table) {
        table.dumpSql(pw, false);
        pw.println();
        table.dumpTableIndices(pw);
    }

    @Override void generateDrop(final PrintWriter pw, final String name) {
        pw.printf("drop   table %s;;%n%n", getQName(name));
    }

    @Override void generateRename(final PrintWriter pw, final String tableFrom, final String tableTo) {
        generateAlterTable(pw, tableFrom);
        pw.printf("rename to %s;;%n%n", tableTo);
    }

    @Override int getDiffThreshold() {
        return TABLE_THRESHOLD;
    }

    @Nullable @Override TableInfo getFrom(String nm) {
        return from.getTable(nm).getOrNull();
    }

    @Override Iterable<TableInfo> getFromElements() {
        return from.getTables();
    }

    @Nullable @Override TableInfo getTo(String nm) {
        return to.getTable(nm).getOrNull();
    }

    @Override Iterable<TableInfo> getToElements() {
        return to.getTables();
    }

    private MdDiffer<?> createDiffer(final String tableName, final Element element) {
        switch (element) {
        case CHECK:
            return new CheckDiffer(tableName);
        case INDEX:
            return new IndexDiffer(tableName);
        case COLUMN:
            return new ColumnDiffer(tableName);
        case FOREIGN_KEY:
            return new ForeignKeyDiffer(tableName);
        case UNIQUE:
            return new UniqueDiffer(tableName);
        }
        throw unreachable();
    }

    private void generatePrimaryDropKeyChanges(final PrintWriter pw, final String name) {
        if (primaryKeyChange(name)) {
            final TableInfo tt     = getRenamedTo(name);
            final String    toName = tt.getName();
            generateAlterTable(pw, toName);
            dropConstraint(pw, getFrom(name).getPrimaryKey().getName());
        }
    }
    private void generatePrimaryKeyAddChanges(final PrintWriter pw, final String name) {
        if (primaryKeyChange(name)) {
            final TableInfo tt     = getRenamedTo(name);
            final String    toName = tt.getName();
            generateAlterTable(pw, toName);
            pw.printf("add %s;;%n%n", tt.getPrimaryKey().generateSql());
        }
    }

    private boolean hasChanged(final String name) {
        return getChanged().contains(name);
    }

    private String getQName(final String name) {
        return TableInfo.getQName(getSchemaName(), name);
    }

    //~ Methods ......................................................................................................................................

    private static String normalizeDefault(@Nullable final String v) {
        if (v == null) return "null";
        switch (v.toLowerCase()) {
        case "''":
            return EmptyString.name();
        case CURRENT_DATE:
            return CurrentDate.name();
        case CURRENT_TIMESTAMP:
            return CurrentTime.name();
        case FALSE:
            return False.name();
        case TRUE:
            return True.name();
        default:
            return v;
        }
    }

    //~ Static Fields ................................................................................................................................

    /** Number of modification that make table different. */
    private static final int TABLE_THRESHOLD = 4;

    //~ Inner Classes ................................................................................................................................

    private class CheckDiffer extends ElementDiffer<TableInfo.Check> {
        public CheckDiffer(final String tableName) {
            super(tableName, Element.CHECK);
        }

        @Override void generateAlter(final PrintWriter pw, final String name) {}
        @Override void generateCreate(final PrintWriter pw, final TableInfo.Check element) {}
        @Override void generateDrop(final PrintWriter pw, final String name) {}
        @Override void generateRename(final PrintWriter pw, final String fc, final String tc) {}
    }

    /**
     * Calculate differences between Table columns.
     */
    class ColumnDiffer extends ElementDiffer<TableInfo.Column> {
        ColumnDiffer(final String tableName) {
            super(tableName, COLUMN);
        }

        @Override public boolean isMinor() {
            if (!getFromOnly().isEmpty() || !getRenamed().isEmpty()) return false;

            for (final String name : getChanged()) {
                final TableInfo.Column f = super.getFrom(name);
                final TableInfo.Column t = getRenamedTo(name);

                if (majorTypeChange(f, t)) return false;
                // Now is required and does not have a default value
                if (f.isOptional() && !t.isOptional() && t.getDefaultValue() == null) return false;
            }
            return true;
        }

        @Override int diffWeight(TableInfo.Column f, TableInfo.Column t) {
            int           diff  = 0;
            final SqlType fType = f.getType();
            final SqlType tType = t.getType();
            if (fType.getSqlKind() != tType.getSqlKind()) diff += 10;
            if (f.isSerial() != t.isSerial()) diff += 10;
            if (fType.getPrecision() != tType.getPrecision()) diff++;
            if (fType.getSize() != tType.getSize()) diff++;
            if (f.isOptional() != t.isOptional()) diff++;
            if (!equal(f.getDefaultValue(), t.getDefaultValue())) diff++;
            return diff;
        }

        @Override void generateAlter(final PrintWriter pw, final String name) {
            final TableInfo.Column cf = super.getFrom(name);
            final TableInfo.Column ct = getRenamedTo(name);

            if (!cf.getType().sameAs(ct.getType())) {
                alterTable(pw).printf("%s(%s, %s);;%n%n", AlterColumnType, ct.getName(), ct.formatType());
                if (cf.getType().getSqlKind() == SqlKind.BOOLEAN) {
                    final String constraintName = TableInfo.getConstrainedName(cf.getTable().getName() + "_" + cf.getName(), "B");
                    dropConstraint(pw, constraintName);
                }
            }

            if (!equal(cf.getDefaultValue(), ct.getDefaultValue()))
                alterTable(pw).printf("%s(%s, %s);;%n%n", SetDefault, ct.getName(), normalizeDefault(ct.getDefaultValue()));

            if (cf.isOptional() != ct.isOptional()) alterTable(pw).printf("%s(%s);;%n%n", ct.isOptional() ? DropNotNull : SetNotNull, ct.getName());
        }

        @Override void generateCreate(final PrintWriter pw, final TableInfo.Column element) {
            alterTable(pw).printf("AddColumn(%s);;%n%n", element);
        }

        @Override void generateDrop(final PrintWriter pw, final String name) {
            alterTable(pw).printf("drop column %s;;%n%n", name);
        }

        @Override void generateRename(final PrintWriter pw, final String cf, final String ct) {
            alterTable(pw).printf("%s(%s, %s);;%n%n", RenameColumn, cf, ct);
        }

        @Override int getDiffThreshold() {
            return COLUMN_THRESHOLD;
        }

        private PrintWriter alterTable(final PrintWriter pw) {
            generateAlterTable(pw, getTableName());
            return pw;
        }

        private boolean majorTypeChange(final TableInfo.Column f, final TableInfo.Column t) {
            final SqlType fType = f.getType();
            final SqlType tType = t.getType();
            return fType.getSqlKind() != tType.getSqlKind() || fType.getPrecision() > tType.getPrecision() || fType.getSize() > tType.getSize();
        }

        /** Number of modification that make columns different. */
        private static final int COLUMN_THRESHOLD = 3;
    }  // end class ColumnDiffer

    /**
     * Calculate differences between Table elements.
     */
    abstract class ElementDiffer<T extends TableInfo.TableObject<T>> extends MdDiffer<T> {
        private final Element   element;
        private final TableInfo tf;
        private final TableInfo tt;

        ElementDiffer(final String name, Element element) {
            this(element, TableDiffer.this.getFrom(name), TableDiffer.this.getRenamedTo(name));
        }

        private ElementDiffer(final Element element, final TableInfo tf, final TableInfo tt) {
            super(tt.getSchema().getPlainName());
            this.tf      = tf;
            this.tt      = tt;
            this.element = element;
        }

        @Override T getFrom(final String nm) {
            return cast(tf.getElement(nm, element));
        }

        @Override Iterable<T> getFromElements() {
            return tf.getElements(element);
        }

        String getTableName() {
            return tt.getName();
        }

        // String getTableQName() { return tt.asQName(); }
        //
        @Override T getTo(final String nm) {
            return cast(tt.getElement(nm, element));
        }

        @Override Iterable<T> getToElements() {
            return tt.getElements(element);
        }
    }  // end class ElementDiffer

    private class ForeignKeyDiffer extends ElementDiffer<TableInfo.ForeignKey> {
        public ForeignKeyDiffer(final String tableName) {
            super(tableName, FOREIGN_KEY);
        }

        @Override public void createElements(PrintWriter pw) {
            super.createElements(pw);

            for (final String name : getChanged())
                generateCreate(pw, getTo(name));
        }

        @Override public void dropElements(PrintWriter pw) {
            super.dropElements(pw);

            for (final String name : getChanged())
                generateDrop(pw, name);
        }

        @Override void generateCreate(final PrintWriter pw, final TableInfo.ForeignKey fk) {
            fk.dumpSql(pw);
        }

        @Override void generateDrop(final PrintWriter pw, final String name) {
            generateAlterTable(pw, getTableName());
            dropConstraint(pw, name);
        }
    }

    private class IndexDiffer extends ElementDiffer<TableInfo.Index> {
        public IndexDiffer(final String tableName) {
            super(tableName, Element.INDEX);
        }

        @Override void generateCreate(final PrintWriter pw, final TableInfo.Index index) {
            index.dumpSql(pw);
        }

        @Override void generateDrop(final PrintWriter pw, final String name) {
            pw.printf("drop   index IndexName(%s, %s);;%n%n", getSchemaName(), name);
        }

        @Override void generateRename(final PrintWriter pw, final String fi, final String ti) {
            pw.printf("alter  index IndexName(%s, %s)%n\trename to %s;;%n%n", getSchemaName(), fi, ti);
        }
    }

    private class UniqueDiffer extends ElementDiffer<TableInfo.Index> {
        public UniqueDiffer(final String tableName) {
            super(tableName, UNIQUE);
        }

        @Override void generateCreate(final PrintWriter pw, final TableInfo.Index unique) {
            generateAlterTable(pw, getTableName());
            pw.printf("add %s;;%n%n", unique.generateUniqueSql());
        }

        @Override void generateDrop(final PrintWriter pw, final String name) {
            generateAlterTable(pw, getTableName());
            dropConstraint(pw, name);
        }
    }
}  // end class TableDiffer
