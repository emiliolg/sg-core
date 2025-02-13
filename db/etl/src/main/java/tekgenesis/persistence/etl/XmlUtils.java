
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.persistence.*;
import tekgenesis.persistence.InnerEntitySeq;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.util.Reflection.getNotNullFieldValue;
import static tekgenesis.persistence.etl.XmlStreamParser.Event.*;

/**
 * Static methods to read and write from Xml.
 */
class XmlUtils {

    //~ Constructors .................................................................................................................................

    private XmlUtils() {}

    //~ Methods ......................................................................................................................................

    /**
     * Reads an Instance from the {@link XMLStreamReader} the reader tries to position itself on the
     * next start element with the name of the instance.
     */
    @Nullable static <T extends EntityInstance<T, K>, K> T read(DbTable<T, K> table, XmlStreamParser xp, boolean defaultKey) {
        final TableMetadata<T, K> md = table.metadata();
        xp.syncToStartElement(md.getTypeQName().getName());
        if (xp.eof()) return null;

        final T      instance;
        final String idValue = xp.getAttrValue(ID);
        if (defaultKey) {
            instance = md.createInstance();
            if (idValue != null) md.getPrimaryKey().get(0).setValue(instance, idValue);
        }
        else instance = md.createInstance(md.keyFromString(idValue));

        read(xp, instance);
        return instance;
    }

    static <T extends EntityInstance<T, K>, K> void write(XMLStreamWriter w, T instance, Seq<TableField<?>> fields) {
        final TableMetadata<T, K> md = instance.metadata();
        write(w, md, instance, instance.keyAsString(), fields);
    }

    private static <T extends EntityInstance<T, K>, K> void read(XmlStreamParser xp, T instance) {
        final TableMetadata<T, K> md = instance.metadata();
        while (xp.advanceTo(START_ELEMENT, END_ELEMENT) == START_ELEMENT) {
            final String fieldName = xp.getName();
            xp.advanceTo(CHARACTERS);

            md.getField(fieldName).ifPresentOrElse(field -> {
                    field.setValue(instance, xp.getText());
                    xp.advanceTo(END_ELEMENT);
                },
                () -> readInners(xp, md.getInner(instance, fieldName)));
        }
    }

    private static void readInners(final XmlStreamParser xp, final InnerEntitySeq<?> innerEntitySeq) {
        innerEntitySeq.merge(xp.iterable(START_ELEMENT, END_ELEMENT), (i, e) -> {
                xp.advanceTo(CHARACTERS);
                read(xp, cast(i));
            });
    }

    private static <T extends EntityInstance<T, K>, K> void write(XMLStreamWriter w, TableMetadata<T, K> md, T instance, String id,
                                                                  Seq<TableField<?>> fields) {
        try {
            final String nm = md.getTypeQName().getName();
            writeStart(w, nm);

            w.writeAttribute(ID, id);
            for (final TableField<?> f : fields) {
                if (!f.isPrimaryKey()) {
                    if (f.getValue(instance) != null) {
                        writeStart(w, f.getFieldName());
                        w.writeCharacters(f.getValueAsString(instance));
                        writeEnd(w);
                    }
                }
            }
            // Inner Elements
            md.getInnerFields().forEach(f -> writeInner(w, f.getName(), getNotNullFieldValue(instance, f)));
            writeEnd(w);
        }
        catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeEnd(XMLStreamWriter w) {
        try {
            w.writeEndElement();
        }
        catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static <E extends InnerInstance<E, EK, ?, ?>, EK> void writeInner(final XMLStreamWriter w, String name, InnerEntitySeq<E> seq) {
        if (!seq.isEmpty()) {
            writeStart(w, name);
            int i = 1;
            for (final E innerInstance : seq) {
                final TableMetadata<E, EK> md = innerInstance.metadata();
                write(w, md, innerInstance, String.valueOf(i++), md.getFields());
            }
            writeEnd(w);
        }
    }

    private static void writeStart(XMLStreamWriter w, String nm) {
        try {
            w.writeStartElement(nm);
        }
        catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String ID = "id";
}  // end class XmlUtils
