
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
 * An Input Builder for XML.
 */
public class XmlBuilder implements EntityEtl.Builder {

    //~ Methods ......................................................................................................................................

    @Override public <T extends EntityInstance<T, K>, K> EntityImporter.Input createInput(Reader reader, DbTable<T, K> table,
                                                                                          Seq<TableField<?>> tableFields) {
        return EntityXmlReader.create(reader, table);
    }

    @Override public <T extends EntityInstance<T, K>, K> EntityEtl.Output<T, K> createOutput(Writer writer, Charset charset) {
        return EntityXmlWriter.create(writer, charset);
    }

    @Override public String getExtension() {
        return "xml";
    }
}
