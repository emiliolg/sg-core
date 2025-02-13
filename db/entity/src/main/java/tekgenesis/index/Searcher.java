
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Enumeration;
import tekgenesis.index.SearchableFieldImpl.*;
import tekgenesis.persistence.EntityInstance;

import static tekgenesis.common.collections.ImmutableList.fromIterable;

/**
 * Searcher class.
 */
public abstract class Searcher {

    //~ Instance Fields ..............................................................................................................................

    protected final String                    entity;
    @NotNull protected final SearchableFields searchFields;
    protected int                             topHits;

    //~ Constructors .................................................................................................................................

    protected Searcher(Class<? extends EntityInstance<?, ?>> entity) {
        this.entity  = entity.getName();
        searchFields = new SearchableFieldsImpl();
        topHits      = 10;
    }

    //~ Methods ......................................................................................................................................

    /** Search. */
    public abstract List<SearchResult> search(String query);

    /** Returns entity fqn corresponding to this searcher. */
    public final String getEntityFqn() {
        return entity;
    }

    /** Suggestions. */
    public final List<SearchResult> getSuggestions() {
        return search("");
    }

    @NotNull protected final SearchableFields fields() {
        return searchFields;
    }

    /** Get search fields. */
    @NotNull final ImmutableList<SearchableFieldImpl<?, ?>> getSearchFields() {
        return ((SearchableFieldsImpl) searchFields).getFields();
    }

    //~ Inner Classes ................................................................................................................................

    private class SearchableFieldsImpl implements SearchableFields {
        private final List<SearchableFieldImpl<?, ?>> fields = new ArrayList<>();

        @Override public BoolField boolField(String id, String name) {
            return add(new BoolField(id, name));
        }

        @Override public DateField dateField(String id, String name) {
            return add(new DateField(id, name));
        }

        @Override public DateTimeField dateTimeField(String id, String name) {
            return add(new DateTimeField(id, name));
        }

        @Override public DecimalField decimalField(String id, String name) {
            return add(new DecimalField(id, name));
        }

        @Override public <T extends EntityInstance<T, K>, K> EntityField<T, K> entityField(String id, String name, Class<T> entityClass) {
            return add(new EntityField<T, K>(id, name, entityClass));
        }

        @Override public <E extends Enum<E> & Enumeration<E, ?>> EnumField<E> enumField(String id, String name, Class<E> enumClass) {
            return add(new EnumField<E>(id, name, enumClass));
        }

        @Override public IntField intField(String id, String name) {
            return add(new IntField(id, name));
        }

        @Override public LongField longField(String id, String name) {
            return add(new LongField(id, name));
        }

        @Override public <C extends EntityInstance<?, ?>> ManyEntField<C> manyEntField(SearchableField<C> field) {
            return new ManyEntField<C>(field);
        }

        @Override public <C> ManyField<C> manyField(SearchableField<C> field) {
            return new ManyField<C>(field);
        }

        @Override public RealField realField(String id, String name) {
            return add(new RealField(id, name));
        }

        @Override public StrField strField(String id, String name) {
            return add(new StrField(id, name));
        }

        public ImmutableList<SearchableFieldImpl<?, ?>> getFields() {
            return fromIterable(fields);
        }

        @NotNull private <F extends SearchableFieldImpl<F, ?>> F add(F field) {
            fields.add(field);
            return field;
        }
    }  // end class SearchableFieldsImpl
}  // end class Searcher
