
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.json.JsonMapping;
import tekgenesis.persistence.IxInstance;
import tekgenesis.persistence.TableField;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.persistence.ix.IxRemoteStoreHandler.INC_STR;

/**
 * Batch Operation.
 */
public abstract class BatchOperation<T extends IxInstance<T, ?>> {

    //~ Instance Fields ..............................................................................................................................

    private final String schemaName;
    private final String tableName;

    //~ Constructors .................................................................................................................................

    private BatchOperation(@NotNull String schemaName, @NotNull String tableName) {
        this.schemaName = schemaName;
        this.tableName  = tableName;
    }

    //~ Methods ......................................................................................................................................

    abstract JsonNode _getJson(final ObjectMapper mapper);

    @Nullable String getHeader() {
        return null;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    JsonNode getJson() {
        final ObjectMapper mapper     = JsonMapping.shared();
        final ObjectNode   objectNode = mapper.createObjectNode();
        objectNode.put("operation", getOperation());
        objectNode.put("schema", schemaName);
        objectNode.put("table", tableName);
        final String header = getHeader();
        if (isNotEmpty(header)) objectNode.put("header", header);
        objectNode.set("content", _getJson(mapper));

        return objectNode;
    }

    Class<T> getObjectClass() {
        return null;
    }
    abstract String getOperation();

    //~ Methods ......................................................................................................................................

    /**
     * Delete batch operation.
     *
     * @param   schemaName  schema name
     * @param   tableName   table name
     * @param   key         key
     *
     * @return  BatchOperation
     */
    public static <T extends IxInstance<T, ?>> BatchOperation<T> delete(@NotNull final String schemaName, @NotNull final String tableName,
                                                                        @NotNull final String key) {
        return new BatchOperation<T>(schemaName, tableName) {
            @Override JsonNode _getJson(final ObjectMapper mapper) {
                return TextNode.valueOf(key);
            }

            @Override String getOperation() {  // noinspection DuplicateStringLiteralInspection
                return "DELETE";
            }
        };
    }

    /** incr operation.* */
    public static <T extends IxInstance<T, ?>> BatchOperation<T> incr(@NotNull final String schemaName, @NotNull final String tableName,
                                                                      @NotNull final T instance, @NotNull final TableField<?> field,
                                                                      final double value, final String mDateFieldName) {
        return new BatchOperation<T>(schemaName, tableName) {
            @Override Class<T> getObjectClass() {
                return cast(instance.getClass());
            }

            @Override JsonNode _getJson(ObjectMapper mapper) {
                return new POJONode(instance);
            }

            @Override String getOperation() {
                return "POST";
            }

            @Override String getHeader() {
                final String valueStr = Double.toString(value);

                return format(INC_STR, field.getFieldName(), valueStr, mDateFieldName);
            }
        };
    }

    /**
     * Post operation.
     *
     * @param   schemaName  schema name
     * @param   tableName   table name
     * @param   instance    object to post
     * @param   <T>         Object Type
     *
     * @return  BatchOperation
     */
    public static <T extends IxInstance<T, ?>> BatchOperation<T> post(@NotNull final String schemaName, @NotNull final String tableName,
                                                                      @NotNull final T instance) {
        return new BatchOperation<T>(schemaName, tableName) {
            @Override Class<T> getObjectClass() {
                return cast(instance.getClass());
            }

            @Override JsonNode _getJson(final ObjectMapper mapper) {
                return new POJONode(instance);
            }
            String getOperation() {
                return "POST";
            }
        };
    }
}  // end class BatchOperation
