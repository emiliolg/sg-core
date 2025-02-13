
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Resource;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.OrderSpec;
import tekgenesis.persistence.TableField;

import static tekgenesis.persistence.Sql.selectFrom;

/**
 * A Generic Interface to Export Entity data into different formats.
 */
public class EntityExporter<T extends EntityInstance<T, K>, K> extends EntityEtl<T, K, EntityExporter<T, K>> {

    //~ Constructors .................................................................................................................................

    private EntityExporter(@NotNull DbTable<T, K> dbTable) {
        super(dbTable);
    }

    //~ Methods ......................................................................................................................................

    /** Dump the elements into the specified file. */
    public void into(File file) {
        try {
            final Output<T, K> output = builder.createOutput(new OutputStreamWriter(new FileOutputStream(file), charset), charset);
            writeTo(output, true);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Dump the elements into the specified stream. Given stream is not closed. */
    public void into(OutputStream stream) {
        try {
            final OutputStreamWriter writer = new OutputStreamWriter(stream, charset);
            final Output<T, K>       output = builder.createOutput(writer, charset);
            writeTo(output, false);
            writer.flush();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Returns a list with all the resources associated to this entity. */
    public List<Resource> resources() {
        final ArrayList<Resource> resources = new ArrayList<>();
        metadata.getFields().filter(TableField.Res.class).forEach(r ->
                selectFrom(table).forEach(instance -> {
                    final Resource resource = r.getValue(instance);
                    if (resource != null) resources.add(resource);
                }));
        return resources;
    }

    //J-
    private void writeTo(Output<T, K> out, boolean close) {
        final ImmutableCollection<TableField<?>> fs = getTableFields();
        out.writePreamble(table, fs);


        final OrderSpec<?>[] orderSpecs = metadata.getPrimaryKey().toArray(OrderSpec<?>[]::new);
        try {
            selectFrom(table).orderBy(orderSpecs).forEach(instance -> out.writeInstance(instance, fs));
        }
        catch (final Exception e) {
            logger.error("Error exporting entity " + metadata.getTypeName() + ": " + e.getMessage());

        }
        out.writePrologue();
        if (close) out.close();
    }
    //J+

    //~ Methods ......................................................................................................................................

    /** Creates an Exporter for the specified MetaModel. */
    public static <T extends EntityInstance<T, K>, K> EntityExporter<T, K> export(QName metaModel) {
        return export(metaModel.getFullName());
    }

    /** Creates an Exporter for the specified Entity. */
    public static <T extends EntityInstance<T, K>, K> EntityExporter<T, K> export(DbTable<T, K> table) {
        return new EntityExporter<>(table);
    }

    /** Creates an Exporter for the specified Entity. */
    public static <T extends EntityInstance<T, K>, K> EntityExporter<T, K> export(String entityName) {
        final DbTable<T, K> dbTable = DbTable.forName(entityName);
        return new EntityExporter<>(dbTable);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(EntityExporter.class);
}  // end class EntityExporter
