
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
import java.nio.charset.Charset;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.xml.DelegatingXMLStreamWriter;
import tekgenesis.common.xml.IndentingXMLStreamWriter;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableField;

/**
 * A XMLWriter that provides additional methods for writing {@link EntityInstance} objects.
 */
class EntityXmlWriter<T extends EntityInstance<T, K>, K> extends DelegatingXMLStreamWriter implements EntityExporter.Output<T, K> {

    //~ Constructors .................................................................................................................................

    /** Create an EntityXmlWriter. */
    private EntityXmlWriter(Writer w) {
        super(createWriter(w));
        createWriter(w);
    }

    //~ Methods ......................................................................................................................................

    /** Close the writer. */
    public void close() {
        super.writeEndDocument();
        super.flush();
        super.close();
    }

    /** Write an instance. */
    @Override public void writeInstance(T instance, Seq<TableField<?>> fields) {
        XmlUtils.write(this, instance, fields);
    }

    /** Write a set of instances. */
    public void writeInstances(Seq<T> instances, Seq<TableField<?>> fields) {
        for (final T instance : instances)
            XmlUtils.write(this, instance, fields);
    }

    @Override public void writePreamble(DbTable<T, K> table, Seq<TableField<?>> tableFields) {
        writeStartElement(table.metadata().getTypeName());
    }

    @Override public void writePrologue() {
        writeEndDocument();
    }

    //~ Methods ......................................................................................................................................

    /** Create a Writer to a File, using charset encoding. */
    @SuppressWarnings("WeakerAccess")
    public static <T extends EntityInstance<T, K>, K> EntityXmlWriter<T, K> create(Writer writer, Charset charset) {
        final EntityXmlWriter<T, K> w = new EntityXmlWriter<>(writer);
        w.writeStartDocument(charset.name(), "");
        return w;
    }

    private static IndentingXMLStreamWriter createWriter(Writer w) {
        final XMLOutputFactory xf = XMLOutputFactory.newInstance();

        try {
            return new IndentingXMLStreamWriter(xf.createXMLStreamWriter(w));
        }
        catch (final XMLStreamException e) {
            throw new XMLException(e);
        }
    }
}  // end class EntityXmlWriter
