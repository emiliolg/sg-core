
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.math.BigDecimal;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.index.SearchableExpr.FieldExpr;
import tekgenesis.persistence.EntityInstance;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.util.JavaReservedWords.FALSE;
import static tekgenesis.common.util.JavaReservedWords.TRUE;
import static tekgenesis.index.IndexConstants.INTEGER_TYPE;
import static tekgenesis.index.IndexConstants.STRING_TYPE;
import static tekgenesis.index.IndexSearcher.FieldExprImpl.fromValue;
import static tekgenesis.index.IndexSearcher.FieldExprImpl.fromValues;

/**
 * Searchable field impl.
 */
abstract class SearchableFieldImpl<T extends SearchableFieldImpl<T, V>, V> implements SearchableField<V> {

    //~ Instance Fields ..............................................................................................................................

    private float        boost        = 1;
    private final String entityField;
    private final String id;
    private boolean      primary;
    private boolean      searchFilter;

    //~ Constructors .................................................................................................................................

    private SearchableFieldImpl(SearchableField<V> field) {
        final SearchableFieldImpl<?, ?> impl = ((SearchableFieldImpl<?, ?>) field);
        id          = impl.id;
        entityField = impl.entityField;
    }

    private SearchableFieldImpl(String id, String entityField) {
        this.id          = id;
        this.entityField = entityField;
    }

    //~ Methods ......................................................................................................................................

    public FieldExpr eq(@NotNull V value) {
        return fromValue(getId(), value).boost(boost);
    }

    /**
     * Sets this field as not searchable in the default query (to be used inside a filter
     * expression, for example).
     */
    public T filterOnly() {
        searchFilter = true;
        return cast(this);
    }

    public FieldExpr in(@NotNull Seq<V> value) {
        return fromValues(getId(), value).boost(boost);
    }

    /** Sets a boost to this field when indexing a document. */
    public T withBoost(float f) {
        boost = f;
        return cast(this);
    }

    /** Get field id. */
    public String getId() {
        return id;
    }

    boolean canParse(String s) {
        return true;
    }

    String typeAsString() {
        return STRING_TYPE;
    }

    float getBoost() {
        return boost;
    }

    /** Get field name (references entity field). */
    String getEntityFieldName() {
        return entityField;
    }

    @Nullable Object getFromJson(JsonElement e) {
        return e.getAsString();
    }

    boolean isBoolean() {
        return false;
    }

    /** Get value from json element. */
    @Nullable Object getValue(JsonElement json) {
        if (json.isJsonNull()) return null;
        if (json.isJsonArray()) return fromIterable(json.getAsJsonArray()).map(this::getFromJson).toList();

        return getFromJson(json);
    }

    boolean isFilterOnly() {
        return searchFilter;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6195562744245910959L;

    //~ Inner Classes ................................................................................................................................

    public static class BoolField extends SearchableFieldImpl<BoolField, Boolean> implements Bool {
        BoolField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            return TRUE.equalsIgnoreCase(s) || FALSE.equalsIgnoreCase(s);
        }

        @Override String typeAsString() {
            return Constants.BOOLEAN;
        }

        @Override Object getFromJson(JsonElement e) {
            return e.getAsBoolean();
        }

        @Override boolean isBoolean() {
            return true;
        }

        private static final long serialVersionUID = -5214539078046575450L;
    }

    public static class DateField extends SearchableFieldImpl<DateField, DateOnly> implements Date {
        DateField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            try {
                DateOnly.fromString(s);
                return true;
            }
            catch (final Exception e) {
                return false;
            }
        }

        @Override String typeAsString() {
            return "date";
        }

        @Override Object getFromJson(JsonElement e) {
            return DateOnly.fromString(e.getAsString());
        }

        private static final long serialVersionUID = -4491873316660495184L;
    }

    public static class DateTimeField extends SearchableFieldImpl<DateTimeField, DateTime> implements DTime {
        DateTimeField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            return false;
        }

        @Override String typeAsString() {
            return "date";
        }

        @Override Object getFromJson(JsonElement e) {
            return DateTime.fromString(e.getAsString());
        }

        private static final long serialVersionUID = 8346046651959702311L;
    }

    public static class DecimalField extends SearchableFieldImpl<DecimalField, BigDecimal> implements Decimal {
        DecimalField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            return false;
        }

        @Override String typeAsString() {
            return Constants.DOUBLE;
        }

        @Override Object getFromJson(JsonElement e) {
            return e.getAsBigDecimal();
        }

        private static final long serialVersionUID = 4452136659317232204L;
    }

    public static class EntityField<T extends EntityInstance<T, K>, K> extends SearchableFieldImpl<EntityField<T, K>, T> implements Ent<T> {
        private final String entityClass;

        EntityField(String id, String name, Class<T> entityClass) {
            super(id, name);
            this.entityClass = entityClass.getName();
        }

        @Override public FieldExpr eq(@NotNull T value) {
            return primitive().eq(value.keyAsString());
        }

        @Override public FieldExpr in(@NotNull Seq<T> value) {
            return primitive().in(fromIterable(value).map(T::keyAsString));
        }

        @Override public Str primitive() {
            return new StrField(getId(), getEntityFieldName());
        }

        String getEntityClass() {
            return entityClass;
        }

        private static final long serialVersionUID = -7420465486846915465L;
    }

    public static class EnumField<E extends java.lang.Enum<E> & Enumeration<E, ?>> extends SearchableFieldImpl<EnumField<E>, E> implements Enum<E> {
        private final Class<E> enumClass;

        EnumField(String id, String name, Class<E> enumClass) {
            super(id, name);
            this.enumClass = enumClass;
        }

        @Override Object getFromJson(JsonElement e) {
            if (isEmpty(e.getAsString())) return null;
            return java.lang.Enum.valueOf(enumClass, e.getAsString());
        }

        private static final long serialVersionUID = -8524476100113038802L;
    }

    public static class IntField extends SearchableFieldImpl<IntField, Integer> implements Int {
        IntField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            try {
                Integer.parseInt(s);
                return true;
            }
            catch (final Exception e) {
                return false;
            }
        }

        @Override String typeAsString() {
            return INTEGER_TYPE;
        }

        @Override Object getFromJson(JsonElement e) {
            return e.getAsInt();
        }

        private static final long serialVersionUID = 36920574328871697L;
    }

    public static class LongField extends SearchableFieldImpl<LongField, Long> implements LongFld {
        LongField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            try {
                Long.parseLong(s);
                return true;
            }
            catch (final Exception e) {
                return false;
            }
        }

        @Override String typeAsString() {
            return "long";
        }

        @Override Object getFromJson(JsonElement e) {
            return e.getAsLong();
        }

        private static final long serialVersionUID = -746777877315833781L;
    }

    public static class ManyEntField<C extends EntityInstance<?, ?>> extends SearchableFieldImpl<ManyEntField<C>, C> implements ManyEnt<C> {
        private final SearchableFieldImpl<?, C> field;

        ManyEntField(SearchableField<C> field) {
            super(field);
            this.field = (SearchableFieldImpl<?, C>) field;
        }

        @Override public FieldExpr eq(@NotNull C value) {
            return primitive().eq(value.keyAsString());
        }

        @Override public FieldExpr in(@NotNull Seq<C> value) {
            return primitive().in(fromIterable(value).map(C::keyAsString));
        }

        @Override public Many<String> primitive() {
            return new ManyField<>(new StrField(getId(), getEntityFieldName()));
        }

        EntityField<?, ?> asEntity() {
            return (EntityField<?, ?>) field;
        }

        @Override boolean canParse(String s) {
            return field.canParse(s);
        }

        @Override String typeAsString() {
            return field.typeAsString();
        }

        @Override Object getFromJson(JsonElement e) {
            return field.getFromJson(e);
        }

        private static final long serialVersionUID = 4383424342848594543L;
    }

    public static class ManyField<C> extends SearchableFieldImpl<ManyField<C>, C> implements Many<C> {
        private final SearchableFieldImpl<?, C> field;

        ManyField(SearchableField<C> field) {
            super(field);
            this.field = (SearchableFieldImpl<?, C>) field;
        }

        @Override boolean canParse(String s) {
            return field.canParse(s);
        }

        @Override String typeAsString() {
            return field.typeAsString();
        }

        @Override Object getFromJson(JsonElement e) {
            return field.getFromJson(e);
        }

        private static final long serialVersionUID = 7685923234211044543L;
    }

    public static class RealField extends SearchableFieldImpl<RealField, Double> implements Real {
        RealField(String id, String name) {
            super(id, name);
        }

        @Override boolean canParse(String s) {
            try {
                Double.parseDouble(s);
                return true;
            }
            catch (final Exception e) {
                return false;
            }
        }

        @Override String typeAsString() {
            return Constants.DOUBLE;
        }

        @Override Object getFromJson(JsonElement e) {
            return e.getAsDouble();
        }

        private static final long serialVersionUID = 6685974397311042843L;
    }

    public static class StrField extends SearchableFieldImpl<StrField, String> implements Str {
        StrField(String id, String name) {
            super(id, name);
        }

        private static final long serialVersionUID = 2535504568170301405L;
    }
}  // end class SearchableFieldImpl
