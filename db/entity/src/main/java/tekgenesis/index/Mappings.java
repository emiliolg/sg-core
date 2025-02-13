
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Reflection;
import tekgenesis.persistence.DeprecableInstance;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.HasImage;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.common.core.Strings.notNullValueOf;
import static tekgenesis.es.ESConstants.*;
import static tekgenesis.index.IndexConstants.*;
import static tekgenesis.index.IndexConstants.INDEX_DEPRECATED;
import static tekgenesis.index.IndexConstants.INDEX_UPDATE_TIME;
import static tekgenesis.index.IndexMetadata.defaultMetadata;

/**
 * Takes care of json mappings.
 */
class Mappings {

    //~ Constructors .................................................................................................................................

    private Mappings() {}

    //~ Methods ......................................................................................................................................

    @NotNull static String createMapping(IndexSearcher searcher, String type, String alias, boolean embedded)
        throws IOException
    {
        final XContentBuilder mapping = jsonBuilder().startObject().startObject(MAPPINGS).startObject(type).startObject(PROPERTIES);

        for (final SearchableFieldImpl<?, ?> field : searcher.getSearchFields()) {
            mapping.startObject(field.getId());
            mapping.field("type", field.typeAsString());

            mapping.endObject();
        }

        mapStringFields(mapping, DESCRIBED_BY_FIELD, TO_STRING_FIELD, IMAGE_PATH_FIELD, DEPRECATED_FIELD);

        metaField(mapping.endObject().field(DYNAMIC, "strict"), defaultMetadata());
        mapping.endObject().endObject();

        mapping.startObject("aliases").startObject(alias).endObject().endObject();

        mapping.startObject("settings");

        if (embedded) {
            mapping.field("number_of_shards", 1);
            mapping.field("number_of_replicas", 0);
        }

        mapping.startObject("analysis");

        mapping.startObject(FILTER).startObject(ASCIIFOLDING).field("type", ASCIIFOLDING).field("preserveOriginal", true).endObject().endObject();

        mapping.startObject("analyzer")
            .startObject(DEFAULT)
            .field("type", "custom")
            .field("tokenizer", "whitespace")
            .field(FILTER, ASCIIFOLDING, "lowercase", "standard")
            .endObject()
            .endObject();

        mapping.endObject();

        mapping.endObject();

        return mapping.endObject().string();
    }  // end method createMapping

    @NotNull static String entityJson(@NotNull EntityInstance<?, ?> instance, IndexSearcher searcher)
        throws IOException
    {
        final XContentBuilder json = jsonBuilder().startObject();

        for (final SearchableFieldImpl<?, ?> searchField : searcher.getSearchFields()) {
            final Object value = getFromInstanceField(instance, searchField);

            addToJson(json, searchField.getId(), value);
        }

        json.field(DESCRIBED_BY_FIELD, instance.describe().mkString("\t"));
        json.field(TO_STRING_FIELD, instance.toString());

        if (instance instanceof HasImage) json.field(IMAGE_PATH_FIELD, ((HasImage) instance).imagePath());
        if (instance instanceof DeprecableInstance) json.field(DEPRECATED_FIELD, ((DeprecableInstance<?, ?>) instance).isDeprecated());
        return json.endObject().string();
    }

    @NotNull static XContentBuilder updateMetadataObject(IndexMetadata metadata)
        throws IOException
    {
        final XContentBuilder metaUpdate = jsonBuilder().startObject();
        metaField(metaUpdate, metadata);
        return metaUpdate.endObject();
    }

    //J-
    private static void addToJson(XContentBuilder json, String id, @Nullable Object value)
        throws IOException
    {
        if (value instanceof Iterable<?>) {
            json.array(id,
                    fromIterable((Iterable<?>) value).map(o ->
                                    o instanceof EntityInstance<?, ?> ?
                                            ((EntityInstance<?, ?>) o).keyAsString() :
                                            notNullValueOf(o)).toList().toArray());
        } else if (value instanceof EntityInstance<?, ?>) addToJson(json, id, ((EntityInstance<?, ?>) value).keyAsString());
        else json.field(id, notNullValueOf(value));
    }
    //J+

    @NotNull private static Object invoke(EntityInstance<?, ?> instance, SearchableFieldImpl<?, ?> searchField, String name) {
        try {
            return notNull(Reflection.invoke(instance, getterName(name, searchField.isBoolean())), "");
        }
        catch (final IllegalArgumentException e) {
            return notNull(Reflection.invoke(instance, getterName(name, false)), "");
        }
    }

    private static void mapStringField(@NotNull final XContentBuilder mapping, @NotNull final String fieldName)
        throws IOException
    {
        mapping.startObject(fieldName).field("type", STRING_TYPE).endObject();
    }

    private static void mapStringFields(@NotNull final XContentBuilder mapping, final String... fields)
        throws IOException
    {
        for (final String field : fields)
            mapStringField(mapping, field);
    }

    private static void metaField(XContentBuilder json, IndexMetadata metadata)
        throws IOException
    {
        json.startObject(META_FIELD).field(INDEX_DEPRECATED, metadata.isDeprecated()).field(INDEX_UPDATE_TIME, metadata.getUpdateTime()).endObject();
    }

    /** Get field value from entity instance stored in event. */
    @Nullable private static Object getFromInstanceField(EntityInstance<?, ?> instance, SearchableFieldImpl<?, ?> searchField) {
        final String name = searchField.getEntityFieldName();
        if (name.equals(DEPRECATED_FIELD)) return instance instanceof DeprecableInstance && ((DeprecableInstance<?, ?>) instance).isDeprecated();
        else return invoke(instance, searchField, name);
    }  // end method getFromInstanceField
}  // end class Mappings
