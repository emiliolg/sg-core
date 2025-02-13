
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

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Files;
import tekgenesis.database.DbIntrospector;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.database.DbMacro.*;
import static tekgenesis.database.introspect.MdColumn.*;

/**
 * It represents an introspected schema.
 */
public final class SchemaInfo extends MetadataObject<SchemaInfo> {

    //~ Instance Fields ..............................................................................................................................

    private final String         catalogName;
    private boolean              current;
    private final DbIntrospector introspector;

    private final String                 plainName;
    private final String                 qualification;
    private Map<String, SchemaObject<?>> schemaObjects;
    private Map<String, SequenceInfo>    sequences;

    //~ Constructors .................................................................................................................................

    /** Create SchemaInfo. */
    public SchemaInfo(@NotNull DbIntrospector introspector, @NotNull final String catalogName, @NotNull final String name) {
        super(name);
        this.introspector = introspector;
        this.catalogName  = catalogName.isEmpty() ? introspector.getDefaultCatalog() : catalogName;
        qualification     = introspector.getDefaultCatalog().equals(catalogName) ? "" : catalogName;
        schemaObjects     = null;
        sequences         = null;
        final String schemaPrefix = introspector.getSchemaPrefix();
        plainName = name.startsWith(schemaPrefix) ? name.substring(schemaPrefix.length()) : name;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Write the Sql DDL statements to the specified writer. If lexSorted == false then the order
     * for columns inside a table will be based on the column position in the table If lexSorted ==
     * true then columns will be sorted in lexicographical order.
     */
    public void dumpSql(final Writer writer, boolean lexSorted) {
        final PrintWriter                       w       = Files.printWriter(writer);
        final ImmutableCollection<SequenceInfo> ss      = getSequences();
        final ImmutableList<TableInfo.Column>   serials = collectSerials();

        if (!ss.isEmpty()) generateSequences(w, ss);
        else if (!serials.isEmpty()) generateSequences(w, serials.map(SchemaInfo::asSequence));

        for (final TableInfo t : getTables()) {
            t.dumpSql(w, lexSorted);
            w.println();
        }
        for (final ViewInfo t : getViews()) {
            t.dumpSql(w, true);
            w.println();
        }

        for (final TableInfo t : getTables())
            t.dumpForeignKeys(w);

        for (final TableInfo t : getTables())
            t.dumpTableIndices(w);

        if (!serials.isEmpty()) dumpSerials(serials, w);
    }

    /** Load all introspected data data. */
    public void loadAll() {
        getSequences();
        for (final TableInfo t : getTables())
            t.loadAll();
        for (final ViewInfo v : getViews())
            v.loadAll();
    }

    /** Mark schema as current one. */
    public void markCurrent() {
        current = true;
    }

    // todo implement....
    @Override public boolean sameAs(SchemaInfo to) {
        return false;
    }

    /** Get Catalog Name. */
    public String getCatalogName() {
        return catalogName;
    }

    /** Get Schema Plain Name (Without schema Prefix). */
    public String getPlainName() {
        return plainName;
    }

    /** Return the tables for this schema. */
    public Map<String, SchemaObject<?>> getSchemaObjects() {
        if (schemaObjects == null) schemaObjects = retrieveSchemaObjects("%", EnumSet.allOf(TableType.class));
        return schemaObjects;
    }

    /** Return the sequence with the specified name. */
    public SequenceInfo getSequence(final String name) {
        getSequences();
        return sequences.get(name);
    }
    /** Return the sequences for this schema. */
    public ImmutableCollection<SequenceInfo> getSequences() {
        if (sequences == null) sequences = retrieveSequences();
        return immutable(sequences.values());
    }

    /** Is schema current. */
    public boolean isCurrent() {
        return current;
    }

    /** Return the Metadata for the table with the specified name. */
    public Option<TableInfo> getTable(String name) {
        final SchemaObject<?> schemaObject = getSchemaObjects().get(name);
        return (schemaObject instanceof TableInfo) ? some(cast(schemaObject)) : Option.empty();
    }

    /** Return the tables for this schema. */
    public Seq<TableInfo> getTables() {
        return Colls.immutable(getSchemaObjects().values()).filter(s -> s instanceof TableInfo).map(Predefined::cast);
    }

    /** Return the Metadata for the view with the specified name. */
    public Option<ViewInfo> getView(String name) {
        final SchemaObject<?> schemaObject = getSchemaObjects().get(name);
        return (schemaObject instanceof ViewInfo) ? some(cast(schemaObject)) : Option.empty();
    }

    /** Return the views for this schema. */
    public Seq<ViewInfo> getViews() {
        return Colls.immutable(getSchemaObjects().values()).filter(s -> s instanceof ViewInfo).map(Predefined::cast);
    }

    DbIntrospector getIntrospector() {
        return introspector;
    }

    @NotNull @Override String getQualification() {
        return qualification;
    }

    private ImmutableList<TableInfo.Column> collectSerials() {
        return getTables()                   //
               .flatMap(TableInfo::getColumns)  //
               .filter(TableInfo.Column::isSerial)  //
               .toList();
    }

    private void dumpSerials(final List<TableInfo.Column> serials, final PrintWriter w) {
        w.println("-- if NeedsSerialComment");
        for (final TableInfo.Column c : serials) {
            final String s = (c.getType().getSqlKind() == SqlKind.INT ? Serial : BigSerial).name();
            w.printf("comment on column %s.%s is '%s(%d,%s)';;%n", c.getTable().asQName(), c.getName(), s, c.getSequenceStart(), c.getSequenceName());
        }
        w.println("-- end");
    }

    private Map<String, SchemaObject<?>> retrieveSchemaObjects(final String tableNamePattern, final EnumSet<TableType> tableTypes) {
        final Map<String, SchemaObject<?>> result = new TreeMap<>();

        for (final MdEntry e : introspector.getRetriever().getTables(getCatalogName(), getName(), tableNamePattern, tableTypes)) {
            final String tableName = e.getString(T_NAME);
            if (tableName != null) {
                final TableType type = TableType.valueOf(e.getString(T_TYPE, TableName.name()));

                // todo Views, etc
                final SchemaObject<?> t = type.isTable() ? new TableInfo(this, tableName, e.getString(T_REMARKS), type)
                                                         : type.isView() ? new ViewInfo(this, tableName, e.getString(T_REMARKS)) : null;
                if (t != null) result.put(t.getName(), t);
            }
        }
        return result;
    }

    private Map<String, SequenceInfo> retrieveSequences() {
        final Map<String, SequenceInfo> result = new TreeMap<>();

        for (final MdEntry e : introspector.getRetriever().getSequences(getName())) {
            final String name = e.getString(SEQ_NAME);
            if (name != null) {
                final SequenceInfo s = new SequenceInfo(this,
                        name,
                        e.getDecimal(SEQ_START),
                        e.getDecimal(SEQ_MIN),
                        e.getDecimal(SEQ_MAX),
                        e.getInt(SEQ_INC),
                        e.getYesOrNo(SEQ_CYCLE),
                        e.getInt(SEQ_CACHE),
                        e.getDecimal(SEQ_LAST));
                result.put(name, s);
            }
        }
        return result;
    }

    //~ Methods ......................................................................................................................................

    /** Generate Sequence. */
    public static void generateSequences(final PrintWriter w, Runnable r) {
        w.println("-- if NeedsCreateSequence");
        w.println();
        r.run();
        w.println("-- end");
    }

    @NotNull private static SequenceInfo asSequence(final TableInfo.Column column) {
        return new SequenceInfo(column.getTable().getSchema(), column.getSequenceName());
    }

    private static void generateSequences(final PrintWriter w, final Seq<SequenceInfo> ss) {
        generateSequences(w, () -> {
                for (final SequenceInfo s : ss) {
                    s.dumpSql(w);
                    w.println();
                }
            });
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5309848447599233878L;
}  // end class SchemaInfo
