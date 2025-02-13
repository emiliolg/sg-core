
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.entity.DbObject;

import static tekgenesis.codegen.common.MMCodeGenConstants.TABLE_CLASS_NAME;
import static tekgenesis.codegen.entity.EntityBaseCodeGenerator.makePrimaryKeyType;
import static tekgenesis.common.core.Strings.quoted;

/**
 * IX DatabaseObjectTable Generator.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "SpellCheckingInspection" })
public class IxEntityTableCodeGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Constructor constructor;

    //~ Constructors .................................................................................................................................

    IxEntityTableCodeGenerator(IxEntityBaseCodeGenerator parent, DbObject dbObject, String baseExt) {
        super(parent, TABLE_CLASS_NAME + baseExt);
        asProtected().asStatic();
        final String keyType     = makePrimaryKeyType(this, dbObject);
        final String entityClass = dbObject.getImplementationClassName();

        withSuperclass(generic("tekgenesis.persistence.IxEntityTable", entityClass, keyType));
        constructor = constructor().invokeSuper(parent.getTableSingleton());

        constructor.statement(invoke("", "setMDateFieldName", "MDATE_FIELD_NAME"));
        constructor.statement(invoke("", "setMTimeFieldName", "MTIME_FIELD_NAME"));
        constructor.statement(invoke("", "setIxTableName", "TABLE_NAME"));

        dbObject.getUniqueIndexNames().forEach(i -> addToIndex(i, dbObject.getUniqueIndexByName(i).map(t -> quoted(t.getName()))));

        dbObject.getIndexNames().forEach(i -> addToIndex(i, dbObject.getIndexByName(i).map(t -> quoted(t.getName()))));

        addToIndex("pk", dbObject.getPrimaryKey().map(t -> quoted(t.getName())));
    }

    //~ Methods ......................................................................................................................................

    private Constructor addToIndex(String indexName, Seq<String> fields) {
        return constructor.statement(invoke("", "addIndex", quoted(indexName), fields.mkString(",")));
    }
}
