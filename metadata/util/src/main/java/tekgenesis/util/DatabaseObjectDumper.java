
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
import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.SearchField;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Dumper for Database Objects.
 */
abstract class DatabaseObjectDumper<T extends DbObject> extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    final T databaseObject;

    //~ Constructors .................................................................................................................................

    DatabaseObjectDumper(MetaModel model, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences, T databaseObject) {
        super(model, repository, writer, preferences);
        this.databaseObject = databaseObject;
    }

    //~ Methods ......................................................................................................................................

    void dumpDescribedBy() {
        if (!databaseObject.describes().isEmpty() && !isDefaultDescribedBy()) {
            if (databaseObject.isInner()) space();
            else newLine();
            print(DESCRIBED_BY).space().print(fieldNames(databaseObject.describes()).mkString(", "));
        }
    }

    void dumpImage() {
        if (databaseObject.hasImage()) {
            if (databaseObject.isInner()) space();
            else newLine();
            print(IMAGE).space().print(databaseObject.image());
        }
    }

    void dumpIndexes() {
        for (final String indexName : databaseObject.getIndexNames())
            dumpIndex(indexName, databaseObject.getIndexByName(indexName), INDEX);
        for (final String indexName : databaseObject.getUniqueIndexNames())
            dumpIndex(indexName, databaseObject.getUniqueIndexByName(indexName), UNIQUE);
    }

    void dumpOptimistic() {
        if (databaseObject.isOptimistic()) newLine().print(OPTIMISTIC);
    }

    void dumpPrimaryKey() {
        if (!databaseObject.hasDefaultPrimaryKey())
            newLine().print(PRIMARY_KEY).space().print(fieldNames(databaseObject.getPrimaryKey()).mkString(", "));
    }
    void dumpRemotable() {
        if (databaseObject.isRemotable()) newLine().print(REMOTABLE);
    }

    void dumpSearcheable() {
        if (databaseObject.isSearchable()) {
            newLine().print(SEARCHABLE).space().print("by").space().print(LEFT_BRACE).indent();
            databaseObject.searchByFields().forEach(this::dumpSearchField);
            unIndent();
            newLine().print(RIGHT_BRACE);
        }
    }

    private void dumpIndex(String indexName, Seq<Attribute> index, MMToken indexToken) {
        newLine().print(indexToken).space();
        final String attrList = fieldNames(index).mkString(", ");
        if (isFull() || !indexName.equals(index.getFirst().get().getName())) print(indexName).print("(").print(attrList).print(")");
        else print(attrList);
    }

    private void dumpSearchField(SearchField sf) {
        newLine().print(sf.getId()).print(COLON).space().print(sf.getFieldName());
        comma().space().print("boost").space().print(sf.getBoost());
        if (sf.isSearchFilter()) comma().space().print("filter_only");
        semicolon();
    }

    private boolean isDefaultDescribedBy() {
        final Seq<Attribute> describes = databaseObject.describes().filter(Attribute.class);
        return databaseObject.getPrimaryKey().equals(describes);
    }
}  // end class DatabaseObjectDumper
