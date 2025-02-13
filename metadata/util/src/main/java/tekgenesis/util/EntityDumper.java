
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.cache.CacheType;
import tekgenesis.common.IndentedWriter;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ArrayType;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * A visitor to generate the Database Schema.
 */
class EntityDumper extends DatabaseObjectDumper<Entity> {

    //~ Constructors .................................................................................................................................

    EntityDumper(Entity entity, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(entity, repository, writer, preferences, entity);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void dumpModelOptions() {
        if (databaseObject.isInner()) {
            dumpDeprecable();
            dumpDescribedBy();
            dumpRemotable();
            super.dumpModelOptions();
        }
        else {
            indent();
            dumpPrimaryKey();
            dumpDeprecable();
            dumpAuditable();
            dumpRemotable();
            dumpOptimistic();
            dumpForm();
            dumpDescribedBy();
            dumpImage();
            dumpSearcheable();
            dumpIndexes();
            dumpCached();
            dumpTableName();
            unIndent().newLine();
        }
    }

    @Override protected boolean mustDumpField(ModelField field) {
        return !(((Attribute) field).isSynthesized()) && (!databaseObject.isInner() || !databaseObject.isPrimaryKey((Attribute) field));
    }

    @Override void dumpType(ModelField field) {
        final Attribute attribute = (Attribute) field;

        final Type type = attribute.getType();

        if (attribute.isInner() && type instanceof MetaModel) {
            createModelDumper((MetaModel) type).multiple(attribute.isMultiple()).dump();
            advanceMargin();
        }
        else {
            final StringBuilder s = new StringBuilder(asString(type.isArray() ? ((ArrayType) type).getElementType() : type));
            if (attribute.isMultiple() || type.isArray()) s.append("*");

            final String reverseReference = attribute.getReverseReference();
            if (!reverseReference.isEmpty()) {
                int count = 0;
                if (type instanceof Entity) {
                    for (final Attribute attr : ((Entity) type).attributes()) {
                        if (attr.getType().equivalent(attribute.getDbObject())) count++;
                    }
                }
                if (count > 1 || isFull()) s.append(" ").append(USING.getText()).append(" ").append(reverseReference);
            }
            printElement(s.toString());
        }
    }  // end method dumpType

    private void dumpAuditable() {
        if (databaseObject.isAuditable()) newLine().print(AUDITABLE);
    }

    private void dumpCached() {
        final CacheType cacheType = databaseObject.getCacheType();
        if (cacheType.isDefined()) {
            newLine().print(CACHE);
            if (cacheType.isFull()) {
                space();
                print(ALL);
            }
            else if (cacheType.getSize() != CacheType.DEFAULT.getSize()) {
                space();
                print(cacheType.getSize());
            }
        }
    }

    private void dumpDeprecable() {
        if (databaseObject.isDeprecable()) newLine().print(DEPRECABLE);
    }

    private void dumpForm() {
        final String defaultForm = databaseObject.getDefaultForm();
        if (!defaultForm.isEmpty()) newLine().print(FORM).space().print(defaultForm);
    }

    private void dumpTableName() {
        if (!databaseObject.isTableNameGenerated()) newLine().print(TABLE).space().print(databaseObject.getTableName().toString());
    }
}  // end class EntityDumper
