
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.sql;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Types;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.util.Files;
import tekgenesis.database.DbMacro;
import tekgenesis.database.introspect.ViewInfo;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.*;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.UnresolvedTypeReference;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static java.lang.String.format;

import static tekgenesis.codegen.sql.SqlExpressionGenerator.defaultFor;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.database.DbConstants.SQL_INDEX_TABLE_SPACE;
import static tekgenesis.database.DbMacro.CommentOnView;
import static tekgenesis.database.MetaDataTable.*;
import static tekgenesis.database.SchemaDefinition.DB_CURRENT;
import static tekgenesis.type.Types.*;

/**
 * Class to manage the Sql Code generation.
 */
public class SchemaCreationGenerator implements AutoCloseable {

    //~ Instance Fields ..............................................................................................................................

    private final Set<String> grantedReferences;

    private final File   outputFile;
    private final String schema;

    private final PrintWriter writer;

    //~ Constructors .................................................................................................................................

    SchemaCreationGenerator(final File dir, final String schema)
        throws IOException
    {
        final String fileName = schema.toLowerCase() + ".sql";
        final File   dbDir    = new File(dir, DB_CURRENT);
        Files.ensureDirExists(dbDir);
        outputFile        = new File(dbDir, fileName);
        writer            = new PrintWriter(outputFile);
        grantedReferences = new HashSet<>();
        this.schema       = schema;
    }

    //~ Methods ......................................................................................................................................

    @Override public void close()
        throws IOException
    {
        writer.close();
    }

    /** Generates the Sql to the given repository. */
    void createSchema(@NotNull Iterable<MetaModel> models) {
        printf("-- SQL for Schema %s --\n\n", schema);

        final List<Entity>    entities = filter(models, Entity.class).toList();
        final List<Attribute> serials  = collectSerials(entities);
        printSequences(serials);

        entities.forEach(this::createTable);

        printMetadataTable();

        // ENTITY VIEWS
        for (final View view : filter(models, View.class)) {
            if (!view.getAsQuery().isEmpty()) printSqlView(view);
            else if (view.isRemote()) createTable(view);
            else printView(view);
        }

        printIndexes(filter(models, DbObject.class));

        // printGrants(schema);

        // Foreign keys
        entities.forEach(this::createForeignKeys);

        // Comments for Serials
        printSerialComments(serials);

        writer.flush();
    }  // end method createSchema

    File getOutputFile() {
        return outputFile;
    }

    private MultiMap<DbObject, String> collectSelectReferences(View view) {
        final MultiMap<DbObject, String> result = MultiMap.createSortedMultiMap();
        for (@Nullable MetaModel entity : view.entities()) {
            if (entity instanceof UnresolvedTypeReference) throw new UnresolvedTypeReferenceException(entity.getFullName());
            if (entity instanceof SimpleType) {
                final Type finalType = ((SimpleType) entity).getFinalType();
                entity = finalType instanceof DbObject ? (DbObject) finalType : null;
            }
            if (entity instanceof DbObject && !entity.getSchema().equals(schema)) result.put((DbObject) entity, schema);
        }
        return result;
    }  // end method collectSelectReferences

    private List<Attribute> collectSerials(final List<Entity> entities) {
        final List<Attribute> sequences = new ArrayList<>();
        for (final Entity e : entities) {
            for (final Attribute a : e.attributes()) {
                if (a.isSerial()) sequences.add(a);
            }
        }
        return sequences;
    }

    private String columns(Iterable<Attribute> attributes) {
        final StrBuilder columns = new StrBuilder();
        for (final Attribute attribute : attributes) {
            for (final TypeField field : attribute.retrieveSimpleFields())
                columns.appendElement(field.getColumnName());
        }
        return columns.toString();
    }

    private void createForeignKeys(Entity entity) {
        for (final Attribute attribute : entity.attributes()) {
            if (attribute.hasColumn() && attribute.isEntity() && !attribute.asEntity().get().getFullName().equals(entity.getFullName()))
                foreignKeyConstraint(attribute);
        }
    }

    /** Generates the Sql to creates the entity table. */

    private void createTable(@NotNull Entity entity) {
        createTable(entity.getTableName());

        printColumnDefinitions(entity.getTableName(), entity.attributes(), false);
        println();

        printConstraint("PK_" + entity.getTableName().getName().toUpperCase(), PRIMARY_KEY, entity.getPrimaryKey());
        for (final String index : entity.getUniqueIndexNames()) {
            println(",");
            printConstraint(indexName(entity, index, true), UNIQUE, entity.getUniqueIndexByName(index));
        }
        printf("\n);;\n\n");
    }

    /** Generates the Sql to creates the entity table. */
    private void createTable(@NotNull View view) {
        createTable(view.getTableName());

        printColumnDefinitions(view.getTableName(), view.allAttributes(), view.isRemote());
        println();

        printConstraint("PK_" + view.getTableName().getName().toUpperCase(), PRIMARY_KEY, view.getPrimaryKey());
        printf("\n);;\n\n");
    }

    private void createTable(QName tableName) {
        printf("create table QName(%s, %s) (\n", tableName.getQualification(), tableName.getName());
    }

    private void foreignKeyConstraint(final Attribute attribute) {
        final DbObject base   = attribute.getDbObject();
        final Entity   target = attribute.asEntity().get();

        final String name = getConstrainedName(attribute.getColumnName() + "_" + base.getTableName().getName(), FOREIGN_KEY_SUFFIX);

        final StrBuilder baseColumns   = new StrBuilder().startCollection(", ");
        final StrBuilder targetColumns = new StrBuilder().startCollection(", ");
        for (final TypeField column : attribute.retrieveSimpleFields()) {
            baseColumns.appendElement(column.getColumnName());
            targetColumns.appendElement(column.getTargetColumnName());
        }

        foreignKeyConstraintPrint(base.getTableName().getName(), target, name, baseColumns.toString(), targetColumns.toString());
    }

    private void foreignKeyConstraintPrint(String baseTableName, Entity target, String name, String baseColumns, String targetColumns) {
        grantReferences(target);

        printf("alter table QName(%s, %s) add constraint %s\n", schema, baseTableName, name);
        printf("\tforeign key (%s)\n", baseColumns);
        printf("\treferences QName(%s, %s) (%s);;\n\n", target.getSchema(), target.getTableName().getName(), targetColumns);
    }

    private void grantReferences(final Entity target) {
        if (target.getSchema().equals(schema)) return;

        final String e = schema + ":" + target.getFullName();
        if (grantedReferences.contains(e)) return;

        printf("-- if %s%n", DbMacro.NeedsGrantReference);
        printf("grant references on QName(%s,%s) to SchemaOrUser(%s);;%n", target.getSchema(), target.getTableName().getName(), schema);
        println("-- end");
        grantedReferences.add(e);
    }

    private String indexName(DbObject entity, String name, boolean unique) {
        final String suffix = unique ? UNIQUE_SUFFIX : INDEX_SUFFIX;
        return getConstrainedName(entity.getTableName().getName() + "_" + fromCamelCase(name), suffix);
    }

    private void printColumnDefinition(final String name, String sqlType, final boolean required, String defaultValue, String checkExpr) {
        printf("\t%s,%n",
            format("%-33s %-16s %-24s %s",
                name,
                sqlType,
                (defaultValue.isEmpty() ? "" : "default " + defaultValue) + " " + checkExpr,
                required ? "not null" : "").trim());
    }

    private void printColumnDefinition(QName tableName, String columnName, Type type, boolean required, boolean multiple, String defaultValue) {
        final String constraintName = getConstrainedName(tableName.getName() + "_" + columnName, "B");
        printColumnDefinition(columnName,
            type.getSqlImplementationType(multiple).toLowerCase(),
            required,
            defaultValue,
            type.isBoolean() ? DbMacro.CheckBoolConstraint.name() + "(" + constraintName + ", " + columnName + ")" : "");
    }

    private void printColumnDefinitions(QName tableName, @NotNull Seq<Attribute> attributes, boolean remote) {
        for (final Attribute a : attributes) {
            if (a.isSerial() && !remote) printIdentityColumn(a);
            else if (a.hasColumn()) {
                for (final TypeField f : a.retrieveSimpleFields())
                    printColumnDefinition(tableName,
                        f.getColumnName(),
                        f.getType(),
                        f.isRequired(),
                        f.isMultiple(),
                        a.isEntity() ? "" : defaultFor(f, f.isMultiple()));
            }
        }
    }  // end method printColumnDefinitions

    private void printConstraint(String name, String type, Seq<Attribute> columns) {
        printf("\tconstraint %-22.30s %-11s (%s)", name, type, columns(columns));
    }

    private void printf(String str, Object... args) {
        writer.printf(str, args);
    }

    private void printGrantSelectReferences(MultiMap<DbObject, String> grants) {
        printf("-- if %s\n\n", DbMacro.NeedsGrantReference);
        for (final Map.Entry<DbObject, Collection<String>> entry : grants.asMap().entrySet()) {
            for (final String u : entry.getValue()) {
                final DbObject e = entry.getKey();
                printf("grant select on QName(%s,%s) to SchemaOrUser(%s) with grant option;;\n", e.getSchema(), e.getTableName().getName(), u);
            }
        }
        printf("\n-- end\n\n");
    }

    private void printIdentityColumn(final Attribute a) {
        printf("\t%-33s %-41s not null,%n", a.getColumnName(), serialType(a));
    }

    private void printIndexes(Seq<DbObject> entities) {
        for (final DbObject entity : entities)
            for (final String indexName : entity.getIndexNames()) {
                final String index        = indexName(entity, indexName, false);
                final String indexColumns = columns(entity.getIndexByName(indexName));

                printf("create index IndexName(%s, %s)\n", schema, index);
                printf("\ton QName(%s, %s) (%s) %s;;\n\n", schema, entity.getTableName().getName(), indexColumns, SQL_INDEX_TABLE_SPACE);
            }
    }

    // private void printGrants(String schema) {
    // if (!isEmpty(entityNames)) {
    // println();
    // println("-- if " + POSTGRES);
    // // println("GRANT USAGE ON SCHEMA " + sch + " TO " + user + ";");
    // final String sch  = quoted(schemaOf(schema));
    // final String user = quoted(SQL_CURRENT_USER);
    // println("GRANT USAGE ON ALL SEQUENCES IN SCHEMA " + sch + " TO " + user + ";");
    // println("-- end");
    // println();
    // }
    // }

    private void println() {
        writer.println();
    }

    private void println(String s) {
        writer.println(s);
    }

    private void printMetadataTable() {
        final QName tableName = QName.createQName(schema, NAME);
        createTable(tableName);

        // noinspection DuplicateStringLiteralInspection
        printColumnDefinition(tableName, "VERSION", stringType(VERSION_COL_LENGTH), true, false, "");
        printColumnDefinition(tableName, "SHA", stringType(SHA_COL_LENGTH), true, false, "");
        printColumnDefinition(tableName, "SHA_OVL", stringType(SHA_COL_LENGTH), false, false, "");
        // noinspection DuplicateStringLiteralInspection
        printColumnDefinition(tableName, "UPDATE_TIME", dateTimeType(), false, false, "");
        printColumnDefinition(tableName, SCHEMA_COL, textType(), false, false, "");
        printColumnDefinition(tableName, OVERLAY_COL, textType(), false, false, "");

        // noinspection DuplicateStringLiteralInspection
        printf("\n\tconstraint %-22s primary key (%s)\n", "PK" + NAME, "VERSION");
        printf(");;\n\n");
    }

    private void printSequences(final List<Attribute> serials) {
        if (serials.isEmpty()) return;

        printf("-- if %s\n\n", DbMacro.NeedsCreateSequence);
        for (final Attribute a : serials) {
            printf("create sequence QName(%s, %s)\n", schema, a.getSequenceName());
            printf("\tstart with %s(%d) increment by 1 %s;;\n\n", DbMacro.SequenceStartValue, a.getSequenceStart(), DbMacro.SequenceCache);
        }
        printf("-- end\n\n");
    }

    private void printSerialComments(final List<Attribute> serials) {
        if (serials.isEmpty()) return;

        printf("-- if %s%n", DbMacro.NeedsSerialComment);
        for (final Attribute a : serials) {
            final String col = format("QName(%s,%s).%s", schema, a.getDbObject().getTableName().getName(), a.getColumnName());
            printf("comment on column %-40s is '%s';;%n", col, serialType(a));
        }
        printf("-- end%n%n");
    }

    private void printSqlView(View view) {
        final MultiMap<DbObject, String> map = collectSelectReferences(view);
        if (!map.isEmpty()) printGrantSelectReferences(map);
        printf("create view QName(%s, %s) as\n", schema, view.getTableName().getName());
        printf("\t%s;;\n\n", view.getAsQuery());
        printf("%s  QName(%s, %s) is '%s';;\n\n", CommentOnView, schema, view.getTableName().getName(), ViewInfo.quoteViewSql(view.getAsQuery()));
    }

    private void printView(View view) {
        final Option<DbObject> baseEntity = view.getBaseEntity();
        final Seq<Attribute>   attributes = view.allAttributes();

        final List<String> columns = new ArrayList<>();
        for (final Attribute a : attributes) {
            final Attribute attr = a instanceof ViewAttribute ? ((ViewAttribute) a).getBaseAttribute() : a;
            for (final TypeField t : attr.retrieveSimpleFields())
                columns.add(t instanceof ViewAttribute ? ((ViewAttribute) t).getBaseAttribute().getColumnName() : t.getColumnName());
        }

        final MultiMap<DbObject, String> map = collectSelectReferences(view);
        if (!map.isEmpty()) printGrantSelectReferences(map);

        final QName tableName = baseEntity.get().getTableName();
        printf("create view QName(%s, %s) (%s) as\n", schema, view.getTableName().getName(), columns(attributes));
        printf("\tselect %s\n", mkString(columns, ", "));
        printf("\tfrom QName(%s, %s);;\n\n", tableName.getQualification(), tableName.getName());
        printf("%s  QName(%s, %s) is 'select %s from  QName(%s, %s)';;\n\n",
            CommentOnView,
            schema,
            view.getTableName().getName(),
            mkString(columns, ", "),
            tableName.getQualification(),
            tableName.getName());
    }

    //~ Methods ......................................................................................................................................

    private static String serialType(final Attribute a) {
        return format("%s(%d,%s)",
            (a.getFinalType().getSqlType() == Types.BIGINT ? DbMacro.BigSerial : DbMacro.Serial).name(),
            a.getSequenceStart(),
            a.getSequenceName());
    }

    //~ Static Fields ................................................................................................................................

    private static final String PRIMARY_KEY = "primary key";
}  // end class SchemaCreationGenerator
