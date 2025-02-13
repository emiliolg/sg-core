
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import tekgenesis.common.collections.Seq;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableField;

/**
 * A Builder for Csv Input/Output.
 */
public class CsvBuilder implements EntityEtl.Builder {

    //~ Instance Fields ..............................................................................................................................

    private String  fieldSeparator  = null;
    private boolean header          = true;
    private String  nullString      = null;
    private char    quoteChar       = '"';
    private String  recordSeparator = null;

    //~ Methods ......................................................................................................................................

    @Override public <T extends EntityInstance<T, K>, K> EntityImporter.Input createInput(Reader reader, DbTable<T, K> table,
                                                                                          Seq<TableField<?>> tableFields) {
        return new EntityCsvInput<>(reader, table, tableFields, header, fieldSeparator, recordSeparator, nullString, quoteChar);
    }

    @Override public <T extends EntityInstance<T, K>, K> EntityEtl.Output<T, K> createOutput(Writer writer, Charset charset) {
        return new EntityCsvOutput<>(writer, header, fieldSeparator, recordSeparator, nullString, quoteChar);
    }

    /** Defines the string used to separate fields (Default ","). */
    @SuppressWarnings("WeakerAccess")
    public CsvBuilder withFieldSeparator(String separator) {
        fieldSeparator = separator;
        if ("\t".equals(separator)) quoteChar = 0;
        return this;
    }

    /** Defines the string used to indicate a null value (Default "NULL"). */
    public CsvBuilder withNull(String string) {
        nullString = string;
        return this;
    }

    /** Do not include header line in the CSV Output. */
    public CsvBuilder withoutHeader() {
        header = false;
        return this;
    }
    /** Defines the character used to quote Strings fields (Default "). Use 0 to disable */
    public CsvBuilder withQuote(char chr) {
        quoteChar = chr;
        return this;
    }

    /** Defines the string used to separate record (Default Newline). */
    public CsvBuilder withRecordSeparator(String separator) {
        recordSeparator = separator;
        return this;
    }

    @Override public String getExtension() {
        return "csv";
    }
}  // end class CsvBuilder
