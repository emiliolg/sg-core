
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.InputStream;
import java.io.Reader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.AbstractIterator;
import tekgenesis.common.collections.BaseSeq;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.*;

import static tekgenesis.persistence.etl.EntityEtl.wrongRecord;

/**
 * A class that allows to read Entities from an Xml Input Stream.
 */
public class EntityXmlReader<T extends EntityInstance<T, K>, K> implements EntityEtl.Input {

    //~ Instance Fields ..............................................................................................................................

    private final EntityTable<T, K>   dao;
    private final TableMetadata<T, K> metadata;

    @NotNull private final DbTable<T, K> table;

    private final XmlStreamParser xp;

    //~ Constructors .................................................................................................................................

    /** Create an EntityXmlWriter. */
    private EntityXmlReader(XmlStreamParser xp, @Nullable DbTable<T, K> table) {
        this.xp = xp;
        if (!xp.current(XmlStreamParser.Event.START_ELEMENT)) throw new IllegalStateException("Not root element");
        this.table = table == null ? DbTable.forName(xp.getName()) : table;
        metadata   = this.table.metadata();
        dao        = EntityTable.forTable(this.table);
    }

    //~ Methods ......................................................................................................................................

    /** Close the writer. */
    public void close() {
        xp.close();
    }

    @Override
    @SuppressWarnings("RedundantCast")
    public void load(EntityImporter.Mode mode, final boolean batch) {
        for (final T instance : readAll()) {
            try {
                switch (mode) {
                case INSERT:
                    final K keyValue = metadata.hasGeneratedKey() ? instance.keyObject() : null;
                    if (keyValue != null && !instance.hasEmptyKey()) dao.insertDoNotGenerate(instance);
                    else dao.insert(instance);
                    break;
                case UPDATE:
                    dao.update(instance);
                    break;
                case UPDATE_OR_INSERT:
                    if (instance instanceof PersistableInstance) dao.forcePersist(instance);
                    else {
                        final K key = instance.keyObject();
                        if (metadata.hasGeneratedKey() ? instance.hasEmptyKey() : dao.find(key) == null) dao.insert(instance);
                        else dao.forceUpdate(instance);
                    }
                    break;
                case DELETE:
                    dao.delete(instance);
                    break;
                }
            }
            catch (final Exception e) {
                logger.error(wrongRecord(instance.toString(), e.getMessage()));
            }
        }
    }  // end method load

    /** Reads a Seq of instances. */
    @SuppressWarnings("WeakerAccess")
    public Seq<T> readAll() {
        return new BaseSeq<T>() {
            @NotNull @Override public ImmutableIterator<T> iterator() {
                return new AbstractIterator<T>() {
                    @Override protected boolean advance() {
                        next = XmlUtils.read(table, xp, metadata.hasGeneratedKey());
                        return next != null;
                    }
                };
            }
        };
    }

    //~ Methods ......................................................................................................................................

    /** Create a Writer to a File, using UTF8 encoding. */
    @SuppressWarnings("WeakerAccess")
    public static <T extends EntityInstance<T, K>, K> EntityXmlReader<T, K> create(InputStream stream, @Nullable DbTable<T, K> table) {
        return new EntityXmlReader<>(new XmlStreamParser(stream), table);
    }
    /** Create a Writer to a File, using UTF8 encoding. */
    public static <T extends EntityInstance<T, K>, K> EntityXmlReader<T, K> create(Reader reader, DbTable<T, K> table) {
        return new EntityXmlReader<>(new XmlStreamParser(reader), table);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(EntityXmlReader.class);
}  // end class EntityXmlReader
