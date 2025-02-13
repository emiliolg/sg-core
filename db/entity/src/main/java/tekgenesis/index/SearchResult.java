
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.index.SearchableField.Ent;
import tekgenesis.index.SearchableField.Many;
import tekgenesis.index.SearchableField.ManyEnt;
import tekgenesis.index.SearchableFieldImpl.EntityField;
import tekgenesis.index.SearchableFieldImpl.ManyEntField;
import tekgenesis.index.SearchableFieldImpl.ManyField;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.HasImage;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.persistence.EntityTable.forName;

/**
 * Contains data of ElasticSearch index search result.
 */
public class SearchResult {

    //~ Instance Fields ..............................................................................................................................

    private final String              describedBy;
    private final Map<String, Object> extraFields;
    private final Option<String>      image;

    private final String key;
    private final double score;
    private final String toString;

    //~ Constructors .................................................................................................................................

    /** SearchResult constructor. */
    SearchResult(String key, String describedBy, String toString, Option<String> image, double score) {
        this(key, describedBy, toString, image, score, new HashMap<>());
    }

    /** SearchResult constructor. */
    private SearchResult(String key, String describedBy, String toString, Option<String> image, double score, Map<String, Object> extraFields) {
        this.key         = key;
        this.describedBy = describedBy;
        this.toString    = toString;
        this.image       = image;
        this.score       = score;
        this.extraFields = extraFields;
    }

    //~ Methods ......................................................................................................................................

    /** Returns result describedBy. */
    public String getDescribedBy() {
        return describedBy;
    }

    /** Returns extra field value. */
    @Nullable public Object getField(String fieldName) {
        return extraFields.get(fieldName);
    }

    /** Returns extra field value. */
    @Nullable public <V> V getField(@NotNull final SearchableField<V> field) {
        final SearchableFieldImpl<?, V> fieldImpl = cast(field);
        return cast(extraFields.get(fieldImpl.getId()));
    }

    /** Returns extra field value. */
    @NotNull public <V> ImmutableList<V> getField(@NotNull final Many<V> field) {
        final ManyField<V> fieldImpl = cast(field);

        return cast(extraFields.get(fieldImpl.getId()));
    }

    /** Returns extra field value. */
    @NotNull public <V extends EntityInstance<?, ?>> ImmutableList<V> getField(@NotNull final ManyEnt<V> field) {
        final ManyEntField<V> fieldImpl = cast(field);

        final String                entityClass = fieldImpl.asEntity().getEntityClass();
        final ImmutableList<String> keys        = cast(extraFields.get(fieldImpl.getId()));
        return cast(keys.map(k -> getEntity(entityClass, k)).toList());
    }

    /** Returns extra field value. */
    @Nullable public <V extends EntityInstance<V, K>, K> V getField(@NotNull final Ent<V> field) {
        final EntityField<V, K> fieldImpl   = cast(field);
        final String            entityClass = fieldImpl.getEntityClass();
        final String            keyAsString = (String) extraFields.get(fieldImpl.getId());
        return getEntity(entityClass, keyAsString);
    }

    /** Returns result image. */
    public Option<String> getImage() {
        return image;
    }

    /** Returns result key. */
    public String getKey() {
        return key;
    }

    /** Return result score. */
    public double getScore() {
        return score;
    }

    /** Returns result toString. */
    public String getToString() {
        return toString;
    }

    @Nullable private <V extends EntityInstance<V, K>, K> V getEntity(String entityClass, String keyAsString) {
        return cast(forName(entityClass).findByString(keyAsString));
    }

    //~ Methods ......................................................................................................................................

    /** Builder initializer from entity instance. */
    public static Builder builder(EntityInstance<?, ?> instance) {
        final Builder b = new Builder(instance.keyAsString(), instance.describe().mkString("\t")).toString(instance.toString());
        if (instance instanceof HasImage) b.image(((HasImage) instance).imagePath());
        return b;
    }

    /** Builder for Search Result. */
    @NotNull public static Builder builder(String key, String description) {
        return new Builder(key, description);
    }

    /** Shorter method to build SearchResult from entity instance. */
    @NotNull public static SearchResult result(EntityInstance<?, ?> instance) {
        return builder(instance).build();
    }

    /** Shorter method to build SearchResult from key and description. */
    @NotNull public static SearchResult result(String key, String description) {
        return builder(key, description).build();
    }

    /** Shorter method to build SearchResult from key and description as a Seq. */
    @NotNull public static SearchResult result(String key, Seq<String> description) {
        return builder(key, description.mkString("\t")).build();
    }

    @NotNull static SearchResult result(String key, String describedBy, String toString, Option<String> image, float score) {
        return result(key, describedBy, toString, image, score, new HashMap<>());
    }

    @NotNull static SearchResult result(String key, String describedBy, String toString, Option<String> image, double score,
                                        Map<String, Object> extraFields) {
        return new SearchResult(key, describedBy, toString, image, score, extraFields);
    }

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("ParameterHidesMemberVariable")
    public static class Builder {
        private String                    describedBy;
        private final Map<String, Object> extraFields = new HashMap<>();
        private Option<String>            image;

        private String key;
        private float  score;
        private String toString;

        private Builder(String key, String description) {
            this.key    = key;
            image       = Option.empty();
            describedBy = description;
            toString    = description;
            score       = 1;
        }

        /** Build search result. */
        public SearchResult build() {
            return new SearchResult(key, describedBy, toString, image, score, extraFields);
        }

        /** Set described by to search result. */
        public Builder describedBy(String describedBy) {
            this.describedBy = describedBy;
            return this;
        }

        /** Add one extra field to map. */
        public Builder extraField(String key, Object value) {
            extraFields.put(key, value);
            return this;
        }

        /** Add extra fields to map. */
        public Builder extraFields(Map<String, Object> extras) {
            extras.forEach(extraFields::put);
            return this;
        }

        /** Set image to search result. */
        public Builder image(@NotNull String image) {
            this.image = some(image);
            return this;
        }

        /** Set key to search result. */
        public Builder key(String key) {
            this.key = key;
            return this;
        }

        /** Set score to search result. */
        public Builder score(float score) {
            this.score = score;
            return this;
        }

        /** Set toString to search result. */
        public Builder toString(String toString) {
            this.toString = toString;
            return this;
        }
    }  // end class Builder
}  // end class SearchResult
