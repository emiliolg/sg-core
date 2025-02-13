
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.Writer;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.io.CsvOutput;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableField;

class EntityCsvOutput<T extends EntityInstance<T, K>, K> extends CsvOutput implements EntityEtl.Output<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean header;

    //~ Constructors .................................................................................................................................

    EntityCsvOutput(Writer writer, boolean header, String fieldSeparator, String recordSeparator, String nullString, char quoteChar) {
        super(writer, fieldSeparator, recordSeparator, nullString, quoteChar);
        this.header = header;
    }

    //~ Methods ......................................................................................................................................

    @Override public void writeInstance(T instance, Seq<TableField<?>> fields) {
        for (final TableField<?> field : fields) {
            final String value = field.getValueAsString(instance);
            outField(value, field.getType().equals(String.class));
        }
        writeLine();
    }

    @Override public void writePreamble(DbTable<T, K> table, Seq<TableField<?>> fields) {
        if (header) {
            for (final TableField<?> field : fields)
                outField(field.getFieldName(), false);
            writeLine();
        }
    }

    @Override public void writePrologue() {}
}
