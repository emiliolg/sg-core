
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
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.persistence.*;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Base class for Entity Export/Import.
 */
@SuppressWarnings("WeakerAccess")
public class EntityEtl<T extends EntityInstance<T, K>, K, This extends EntityEtl<T, K, This>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected Builder                   builder;
    @NotNull protected Charset                   charset;
    @NotNull protected final List<TableField<?>> fields;
    protected final TableMetadata<T, K>          metadata;
    @NotNull protected final DbTable<T, K>       table;

    //~ Constructors .................................................................................................................................

    protected EntityEtl(@NotNull DbTable<T, K> dbTable) {
        builder  = new JsonBuilder();
        charset  = Charset.forName(Constants.UTF8);
        fields   = new ArrayList<>();
        table    = dbTable;
        metadata = table.metadata();
    }

    //~ Methods ......................................................................................................................................

    /** Defines the the input/output encoding charset. */
    @SuppressWarnings("UnusedReturnValue")
    public This encoding(@NotNull String encoding) {
        charset = Charset.forName(encoding);
        return cast(this);
    }

    /** Specified the fields. */
    public This fields(TableField<?>... fs) {
        for (final TableField<?> f : fs) {
            final TableField<Object> field = cast(f);
            fields.add(field);
        }
        return cast(this);
    }

    /** Defines the type of input/output to be used. */
    public This using(Builder b) {
        builder = b;
        return cast(this);
    }

    protected ImmutableCollection<TableField<?>> getTableFields() {
        return fields.isEmpty() ? metadata.getFields() : immutable(fields);
    }

    //~ Methods ......................................................................................................................................

    /** Use a CSV Output type. */
    public static CsvBuilder csv() {
        return new CsvBuilder();
    }

    /** Use a Json Output type. */
    public static JsonBuilder json() {
        return new JsonBuilder();
    }
    /** Use a XML Output type. */
    public static XmlBuilder xml() {
        return new XmlBuilder();
    }

    static String wrongRecord(String record, String errorMessage) {
        return "Cannot load record '" + record + "' cause: " + errorMessage;
    }

    //~ Enums ........................................................................................................................................

    enum Mode { INSERT, UPDATE, UPDATE_OR_INSERT, DELETE }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Input Builder used when exporting.
     */
    public interface Builder {
        /** Create the Input. */
        <T extends EntityInstance<T, K>, K> Input createInput(Reader reader, DbTable<T, K> table, Seq<TableField<?>> tableFields);

        /** Create the Output. */
        <T extends EntityInstance<T, K>, K> Output<T, K> createOutput(Writer writer, Charset charset);

        /** Returns the file extension for builder. */
        String getExtension();
    }

    /**
     * Actual Input used when exporting.
     */
    public interface Input {
        /** Close the Input. */
        void close();
        /** Load data from the Input into the specified table. */
        void load(Mode mode, final boolean batch);
    }

    /**
     * Actual Output used when exporting.
     */
    public interface Output<T extends EntityInstance<T, K>, K> {
        /** Close the Output. */
        void close();
        /** Write an instance. */
        void writeInstance(T instance, Seq<TableField<?>> tableFields);
        /** Write the Output preamble. */
        void writePreamble(DbTable<T, K> table, Seq<TableField<?>> tableFields);
        /** Write the Output prologue. */
        void writePrologue();
    }
}  // end class EntityEtl
