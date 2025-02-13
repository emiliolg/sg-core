
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.database.MetaDataTable;
import tekgenesis.database.introspect.SchemaInfo;
import tekgenesis.database.introspect.SqlType;
import tekgenesis.database.introspect.TableInfo;
import tekgenesis.metadata.entity.AttributeBuilder;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.InvalidFieldNameException;
import tekgenesis.metadata.exception.InvalidTypeException;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.IntType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.core.Strings.toCamelCase;
import static tekgenesis.md.MdConstants.isVersionField;
import static tekgenesis.metadata.entity.EntityBuilder.entityFromDatabase;

/**
 * MM Code Generator from Sql.
 */
public class MetaModelFromSql {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String enclosingPackage;

    //~ Constructors .................................................................................................................................

    /** Create a MetaModel Generator based on Sql definitions. */
    public MetaModelFromSql(@NotNull String enclosingPackage) {
        this.enclosingPackage = enclosingPackage;
    }

    //~ Methods ......................................................................................................................................

    /** Create an Entity based on {@link TableInfo}. */
    public Entity buildEntity(TableInfo t) {
        final EntityBuilder        b  = entityFromDatabase(enclosingPackage, t.getSchema().getPlainName(), t.getName());
        final TableInfo.PrimaryKey pk = t.getPrimaryKey();
        if (!pk.isDefault()) b.primaryKeyFromColumnNames(pk.getColumnNames());

        for (final TableInfo.Column c : t.getColumns()) {
            final String name = deCapitalizeFirst(toCamelCase(c.getName()));
            final Type   type = typeFor(c.getType());
            if (type != null && !isVersionField(name) && (!pk.isDefault() || !"id".equals(name))) {
                final AttributeBuilder a = new AttributeBuilder(name, type);
                try {
                    b.addField(a);
                }
                catch (DuplicateFieldException | InvalidFieldNameException | InvalidTypeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return b.build();
        }
        catch (final BuilderException e) {
            throw new RuntimeException(e);
        }
    }

    /** Create a ModelRepository with all the tables in the specified Schema. */
    @NotNull public ModelRepository createRepository(SchemaInfo schema) {
        final ModelRepository mr = new ModelRepository();
        for (final TableInfo ti : schema.getTables())
            if (!ti.getName().equals(MetaDataTable.NAME)) mr.add(buildEntity(ti));
        return mr;
    }
    /** Create a ModelRepository with the specified tables in the specified Schema. */
    @NotNull public ModelRepository createRepository(SchemaInfo schema, Iterable<String> tables) {
        final ModelRepository mr = new ModelRepository();
        for (final String t : tables) {
            final TableInfo ti = schema.getTable(t).get();
            if (ti != null) mr.add(buildEntity(ti));
        }
        return mr;
    }

    //~ Methods ......................................................................................................................................

    /** Get Type From Basic Type. */
    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static Type typeFor(SqlType type) {
        final int size      = type.getSize();
        final int precision = type.getPrecision();
        switch (type.getSqlKind()) {
        case VARCHAR:
        case NVARCHAR:
            return Types.stringType(size);
        case DECIMAL:
            return Types.decimalType(size, precision);
        case DATE:
            return Types.dateType();
        case DATETIME:
            return precision == -1 ? Types.dateTimeType() : Types.dateTimeType(precision);
        case CLOB:
            return Types.stringType(CLOB_DEFAULT_LENGTH);
        case BOOLEAN:
            return Types.booleanType();
        case INT:
            return size == 0 ? Types.intType() : Types.intType(size);
        case BIGINT:
            return Types.intType(size == 0 ? IntType.MAX_LONG_LENGTH - 1 : size);
        case DOUBLE:
            return Types.realType();
        default:
            return null;
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final int CLOB_DEFAULT_LENGTH = 1_000_000;
}  // end class MetaModelFromSql
