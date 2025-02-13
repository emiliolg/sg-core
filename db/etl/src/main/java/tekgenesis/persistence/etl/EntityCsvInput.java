
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.io.CsvInput;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.*;

import static java.lang.Character.isUnicodeIdentifierPart;
import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.persistence.etl.EntityEtl.Mode.INSERT;
import static tekgenesis.persistence.etl.EntityEtl.Mode.UPDATE_OR_INSERT;
import static tekgenesis.persistence.etl.EntityEtl.wrongRecord;

@SuppressWarnings({ "rawtypes", "unchecked" })
class EntityCsvInput<T extends EntityInstance<T, K>, K> extends CsvInput<List<String>> implements EntityImporter.Input {

    //~ Instance Fields ..............................................................................................................................

    private final EntityTable<T, K> dao;

    private final List<TableField<?>> fields;

    private final TableMetadata<T, K> metadata;
    private final List<Integer>       primaryKeyPos;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    EntityCsvInput(@NotNull Reader reader, @Nullable DbTable<T, K> table, @NotNull Seq<TableField<?>> tableFields, boolean header,
                   @Nullable String fieldSeparator, @Nullable String recordSeparator, @Nullable String nullString, char quoteChar) {
        super(reader, Function.identity(), fieldSeparator, recordSeparator, nullString, quoteChar);
        if (table == null) throw new IllegalArgumentException("CSV does not support discovery of table name");
        dao           = EntityTable.forTable(table);
        metadata      = table.metadata();
        fields        = header ? readHeader() : tableFields.toList();
        primaryKeyPos = metadata.getPrimaryKey().map(fields::indexOf).toList();
    }

    //~ Methods ......................................................................................................................................

    @Override public void load(EntityImporter.Mode mode, final boolean batch) {
        for (final List<String> line : this) {
            final K key = loadPrimaryKey(line);
            try {
                loadRecord(mode, key, line);
            }
            catch (final Exception e) {
                logger.error(wrongRecord(line.toString(), e.getMessage()));
            }
        }
    }

    @Override protected boolean isValid(List<String> line) {
        if (line.size() >= fields.size()) return true;
        logger.error("Insufficient number of fields in line: " + line);
        return false;
    }

    private void addField(StringBuilder current, List<TableField<?>> tableFields) {
        if (current.length() != 0) {
            final String fieldName = current.toString();
            current.setLength(0);
            tableFields.add(metadata.getFieldOrFail(fieldName));
        }
    }

    private void errorProcessingField(TableField<?> field, String message) {
        logger.error(format("Error while processing field '%s'\n%s", field.getFieldName(), message));
    }

    private void errorProcessingField(TableField<?> field, String value, Exception e) {
        logger.error(format("Error while processing field '%s' value '%s'\n%s", field.getFieldName(), value, e));
    }

    private boolean hasDefaultKey() {
        return metadata.hasGeneratedKey();
    }

    private void insertRecord(@NotNull K key, List<String> line) {
        final T instance = metadata.createInstance();
        if (updateAllFields(instance, line)) dao.insertDoNotGenerate(instance);
    }

    @Nullable private K loadPrimaryKey(List<String> line) {
        final List<Object> elements = new ArrayList<>(primaryKeyPos.size());
        for (final int pos : primaryKeyPos) {
            if (pos == -1) {
                if (!hasDefaultKey()) logger.error("Null value for primary key and " + metadata.getType() + " has not default key");
                return null;
            }
            final TableField<?> field = fields.get(pos);
            final String        value = getValue(line, pos);
            if (isEmpty(value)) {
                if (!hasDefaultKey()) errorProcessingField(field, "Null value for primary key field ");
                return null;
            }
            try {
                elements.add(field.fromString(value));
            }
            catch (final Exception e) {
                errorProcessingField(field, value, e);
                return null;
            }
        }
        return cast(Tuple.tupleFromList(elements));
    }

    private void loadRecord(final EntityEtl.Mode mode, @Nullable final K key, final List<String> line) {
        @Nullable final T instance;
        if (key == null) {
            if (!hasDefaultKey() || (mode != INSERT && mode != UPDATE_OR_INSERT)) {
                logger.error(wrongRecord(line.toString(), "Empty Key"));
                return;
            }
            instance = metadata.createInstance();
            if (updateFields(instance, line)) dao.insert(instance);
        }
        else {
            switch (mode) {
            case INSERT:
                insertRecord(key, line);
                break;
            case UPDATE:
                instance = dao.find(key);
                if (instance != null && updateFields(instance, line)) dao.forceUpdate(instance);
                break;
            case UPDATE_OR_INSERT:
                instance = dao.find(key);
                if (instance == null) insertRecord(key, line);
                else if (updateFields(instance, line)) dao.forceUpdate(instance);
                break;

            case DELETE:
                instance = dao.find(key);
                if (instance != null) dao.delete(instance);
                break;
            }
        }
    }  // end method loadRecord

    /** Read the header line trying to detect the field separator if null. */
    private List<TableField<?>> readHeader() {
        try {
            final List<TableField<?>> tableFields = new ArrayList<>();
            final StringBuilder       current     = new StringBuilder();

            int c;
            while ((c = reader.read()) != -1 && !isEndRecord(c)) {
                final char chr = (char) c;
                if (isEndHeaderField(chr)) addField(current, tableFields);
                else current.append(chr);
            }
            addField(current, tableFields);
            if (!tableFields.isEmpty()) return tableFields;
        }
        catch (final IOException e) {
            // Fall trough
        }
        throw new IllegalStateException("Cannot read Header line");
    }

    private boolean updateAllFields(T instance, List<String> line) {
        for (int i = 0; i < fields.size(); i++) {
            if (!updateField(instance, fields.get(i), line.get(i))) return false;
        }
        return true;
    }

    private boolean updateField(T instance, TableField<?> field, String value) {
        try {
            field.setValue(instance, value);
            return true;
        }
        catch (final Exception e) {
            errorProcessingField(field, value, e);
            return false;
        }
    }

    private boolean updateFields(T instance, List<String> line) {
        for (int i = 0; i < fields.size(); i++) {
            final TableField<?> field = fields.get(i);
            if (!field.isPrimaryKey() && !updateField(instance, field, line.get(i))) return false;
        }
        return true;
    }

    private boolean isEndHeaderField(char chr)
        throws IOException
    {
        if (fieldSeparator != null) return isEndField(chr);
        if (isUnicodeIdentifierPart(chr)) return false;
        fieldSeparator = String.valueOf(chr);
        return true;
    }

    @Nullable private String getValue(List<String> line, int pos) {
        return pos < line.size() ? line.get(pos) : null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(EntityCsvInput.class);
}  // end class EntityCsvInput
