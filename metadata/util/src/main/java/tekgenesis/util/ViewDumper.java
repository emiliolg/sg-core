
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Colls;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.entity.ViewAttribute;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Views dumper.
 */
class ViewDumper extends DatabaseObjectDumper<View> {

    //~ Constructors .................................................................................................................................

    ViewDumper(View view, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(view, repository, writer, preferences, view);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void dumpModelOptions() {
        indent();
        dumpUpdatable();
        dumpPrimaryKey();
        dumpDescribedBy();
        dumpSearcheable();
        dumpRemotable();
        dumpOptimistic();
        dumpIndexes();

        unIndent().newLine();
    }

    @Override protected boolean mustDumpField(ModelField field) {
        return !(((Attribute) field).isSynthesized()) && (!databaseObject.isInner() || !databaseObject.isPrimaryKey((Attribute) field));
    }

    @Override ModelDumper beginModel() {
        super.beginModel();
        dumpEntities();
        dumpAsQuery();
        return this;
    }

    void dumpBatchSize() {
        if (databaseObject.getBatchSize() != null) newLine().print(BATCH_SIZE).space().print(databaseObject.getBatchSize());
    }
    @Override void dumpPrimaryKey() {
        if (!databaseObject.isRemote() && !databaseObject.getPrimaryKey().isEmpty())
            newLine().print(PRIMARY_KEY).space().print(fieldNames(databaseObject.getPrimaryKey()).mkString(", "));
    }
    @Override void dumpType(ModelField field) {
        if (field instanceof ViewAttribute) {
            final ViewAttribute attribute = (ViewAttribute) field;
            printElement(attribute.getBaseAttribute().getName());
        }
        else super.dumpType(field);
    }  // end method dumpType
    private void dumpAsQuery() {
        final String asQuery = databaseObject.getAsQuery();
        if (!isEmpty(asQuery)) {
            final String quotes = asQuery.contains("\n") ? "\"\"\"" : "\"";
            newLine().indent().print(AS).space().print(quotes + asQuery + quotes).unIndent();
        }
    }

    private void dumpEntities() {
        if (!databaseObject.entities().isEmpty()) {
            newLine().indent().print(OF).space();
            print(Colls.immutable(databaseObject.entities()).mkString(","));
            unIndent();
        }
    }

    private void dumpUpdatable() {
        if (databaseObject.asView().isUpdatable()) newLine().print(UPDATABLE);
    }
}  // end class ViewDumper
