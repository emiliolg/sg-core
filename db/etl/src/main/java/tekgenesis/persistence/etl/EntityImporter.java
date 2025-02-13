
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jetbrains.annotations.NotNull;

import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;

/**
 * A Generic Interface to Export Entity data into different formats.
 */
public class EntityImporter<T extends EntityInstance<T, K>, K> extends EntityEtl<T, K, EntityImporter<T, K>> {

    //~ Instance Fields ..............................................................................................................................

    private boolean batch = false;

    @NotNull private Mode mode;

    //~ Constructors .................................................................................................................................

    private EntityImporter(@NotNull DbTable<T, K> dbTable) {
        super(dbTable);
        mode = Mode.INSERT;
    }

    //~ Methods ......................................................................................................................................

    /** Use batch mode. */
    public EntityImporter<T, K> batch(boolean b) {
        batch = b;
        return this;
    }

    /** Defines that records will be deleted from the table. */
    public EntityImporter<T, K> delete() {
        mode = Mode.DELETE;
        return this;
    }

    /** Load the elements from the specified file. */
    public void from(File file) {
        try {
            from(new FileInputStream(file));
        }
        catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Load the elements from the specified stream. */
    public void from(InputStream stream) {
        readFrom(builder.createInput(new InputStreamReader(stream, charset), table, getTableFields()));
    }

    /** Defines that records will be inserted into the table. */
    public EntityImporter<T, K> insert() {
        mode = Mode.INSERT;
        return this;
    }

    /** Defines that records will be updated from the table. */
    public EntityImporter<T, K> update() {
        mode = Mode.UPDATE;
        return this;
    }

    /** Defines that records will be updated from the table and inserted if they do not exists. */
    @SuppressWarnings("WeakerAccess")
    public EntityImporter<T, K> updateOrInsert() {
        mode = Mode.UPDATE_OR_INSERT;
        return this;
    }

    /** Defines the type of Input to be used. */
    public EntityImporter<T, K> using(Builder i) {
        builder = i;
        return this;
    }

    private void readFrom(Input input) {
        input.load(mode, batch);
        input.close();
    }

    //~ Methods ......................................................................................................................................

    /** Creates an Importer for the specified Entity. */
    public static <T extends EntityInstance<T, K>, K> EntityImporter<T, K> load(String entityName) {
        final DbTable<T, K> dbTable = DbTable.forName(entityName);
        return new EntityImporter<>(dbTable);
    }

    /** Creates an Importer for the specified Entity. */
    public static <T extends EntityInstance<T, K>, K> EntityImporter<T, K> load(DbTable<T, K> table) {
        return new EntityImporter<>(table);
    }
}  // end class EntityImporter
