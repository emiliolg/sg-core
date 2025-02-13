
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.es.IndexProperties;
import tekgenesis.index.SearchableExpr.FieldExpr;
import tekgenesis.index.SearchableField.Bool;
import tekgenesis.index.SearchableFieldImpl.*;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.type.assignment.AssignmentType;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.valueOf;

import static io.searchbox.client.JestResult.ES_METADATA_ID;

import static org.elasticsearch.common.unit.Fuzziness.fromEdits;
import static org.elasticsearch.index.query.QueryBuilders.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.ImmutableList.*;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.es.ESConstants.REBUILD;
import static tekgenesis.es.ElasticSearch.indexName;
import static tekgenesis.index.IndexConstants.*;
import static tekgenesis.index.IndexSearcher.Container.fromExpr;
import static tekgenesis.index.SearchResult.result;
import static tekgenesis.index.SearchableExpr.EMPTY_EXPR;
import static tekgenesis.md.MdConstants.DEPRECATED_FIELD;

/**
 * InstanceSearch class that provides methods for indexing and for searching. Can be overridden by
 * an entity with the "searchable using <class name>" syntax
 */
public abstract class IndexSearcher extends Searcher {

    //~ Instance Fields ..............................................................................................................................

    protected SearchableExpr       filter;
    protected QueryMode            mode;
    @Nullable private final String namePrefix;

    //~ Constructors .................................................................................................................................

    /** Searcher default constructor. */
    protected IndexSearcher(Class<? extends EntityInstance<?, ?>> entity) {
        super(entity);
        filter     = EMPTY_EXPR;
        mode       = QueryMode.PREFIX;
        namePrefix = Context.getProperties(IndexProperties.class).prefix;
    }

    //~ Methods ......................................................................................................................................

    /** Return a criteria to check which instances should be included in the index. */
    @NotNull public Criteria allow() {
        return Criteria.TRUE;
    }

    /** Deprecated field for queries. */
    @NotNull public final Bool deprecated() {
        return new BoolField(DEPRECATED_FIELD, DEPRECATED_FIELD);
    }

    /** Search instance. */
    @NotNull @Override public List<SearchResult> search(@Nullable String query) {
        return search(query, filter, mode, topHits);
    }

    /** Custom search! */
    @NotNull public final List<SearchResult> search(@NotNull final SearchableExpr expr) {
        return search("", expr, mode, topHits);
    }

    /** Search instance. */
    @NotNull public final List<SearchResult> search(@Nullable String query, SearchableExpr expr) {
        return search(query, expr, mode, topHits);
    }

    /** Custom Search! */
    @NotNull public final List<SearchResult> search(@NotNull final SearchableExpr expr, int hits) {
        return search("", expr, mode, hits);
    }

    /** Search instance. */
    @NotNull public final List<SearchResult> search(@Nullable String query, SearchableExpr expr, int hits) {
        return search(query, expr, mode, hits);
    }

    /** Search instance. */
    @NotNull public final List<SearchResult> search(@Nullable String query, @NotNull SearchableExpr expr, QueryMode queryMode, int hits) {
        final IndexManager                   indexManager = getSingleton(IndexManager.class);
        final io.searchbox.core.SearchResult response     = indexManager.search(getAlias(), createSearchQuery(query, queryMode, expr), hits);

        return processResults(response.getHits(JsonObject.class));
    }

    /** Get alias name for elastic search. */
    @NotNull public final String getAlias() {
        return getAlias(false);
    }

    /** Get alias name for elastic search, specify if reindex alias is what you want. */
    @NotNull public final String getAlias(boolean reindex) {
        return getPrefix() + indexName(entity) + "-" + getIndexId() + (reindex ? REBUILD : "");
    }

    /** Get unique index ID, pre calculated from ElasticSearch mapping. */
    @NotNull public abstract String getIndexId();

    @NotNull String getPrefix() {
        return isNotEmpty(namePrefix) ? indexName(namePrefix) + "-" : "";
    }

    private void addToQuery(QueryMode m, BoolQueryBuilder innerQuery, SearchableFieldImpl<?, ?> field, String searchTerm) {
        final String              fieldId = field.getId();
        ConstantScoreQueryBuilder q       = null;
        switch (m) {
        case STRICT:
            q = constantScoreQuery(termQuery(fieldId, searchTerm));
            break;
        case CONTAINS:
            q = constantScoreQuery(wildcardQuery(fieldId, "*" + searchTerm + "*"));
            break;
        case FUZZY:
            innerQuery.should(constantScoreQuery(prefixQuery(fieldId, searchTerm)));
            q = constantScoreQuery(fuzzyQuery(fieldId, searchTerm).fuzziness(Fuzziness.AUTO));
            break;
        case PREFIX:
            q = constantScoreQuery(prefixQuery(fieldId, searchTerm));
            break;
        }

        innerQuery.should(q.boost(field.getBoost()));
    }

    private void collectTerm(String term, Seq<SearchableFieldImpl<?, ?>> fields, BoolQueryBuilder defaultQuery, QueryMode m) {
        final BoolQueryBuilder innerQuery = boolQuery();
        fields.filter(field -> field.canParse(term)).forEach(field -> addToQuery(m, innerQuery, field, term));
        if (innerQuery.hasClauses()) defaultQuery.must(innerQuery);
    }

    private void collectTerms(String[] searchTerms, Seq<SearchableFieldImpl<?, ?>> fields, BoolQueryBuilder defaultQuery, QueryMode m) {
        fromArray(searchTerms).forEach(term -> collectTerm(term, fields, defaultQuery, m));
    }

    /** Create search query based in query mode and filter. */
    private QueryBuilder createSearchQuery(@Nullable final String queryString, QueryMode m, SearchableExpr expr) {
        final BoolQueryBuilder query = boolQuery();

        // query
        if (!isEmpty(queryString)) {
            final BoolQueryBuilder defaultQuery = boolQuery();
            final String[]         searchTerms  = queryString.toLowerCase().split(" ");

            final Seq<SearchableFieldImpl<?, ?>> searchableFields = getSearchFields().filter(field -> !field.isFilterOnly());

            collectTerms(searchTerms, searchableFields, defaultQuery, m);
            if (defaultQuery.hasClauses()) query.must(defaultQuery);

            if (m != QueryMode.STRICT) {
                final BoolQueryBuilder extraScoreQuery = boolQuery();

                collectTerms(searchTerms, searchableFields, extraScoreQuery, QueryMode.STRICT);
                if (extraScoreQuery.hasClauses()) query.should(extraScoreQuery);
            }
        }

        // filter
        if (expr != EMPTY_EXPR) {
            final SearchableExprImpl<?> impl = ((SearchableExprImpl<?>) expr);
            if (impl.occur == Occur.MUST) query.must(filterQuery(impl));
            else if (impl.occur == Occur.MUST_NOT) query.mustNot(filterQuery(impl));
            else query.should(filterQuery(impl));
        }

        return query.hasClauses() ? query : matchAllQuery();
    }  // end method createSearchQuery

    private void exprToQuery(SearchableExprImpl<?> expr, BoolQueryBuilder query) {
        final BoolQueryBuilder innerQuery = boolQuery();

        if (expr.isContainer()) expr.asContainer().expressions().forEach(innerExpr -> exprToQuery(((SearchableExprImpl<?>) innerExpr), innerQuery));
        else expr.asField().values().forEach(value -> innerQuery.should(fuzzyOrWildcard(expr.asField(), valueOf(value))));

        if (expr.hasBoost()) innerQuery.boost(expr.getBoost());

        if (expr.occur == Occur.MUST) query.must(innerQuery);
        else if (expr.occur == Occur.MUST_NOT) query.mustNot(innerQuery);
        else query.should(innerQuery);
    }  // end method exprToQuery

    private BoolQueryBuilder filterQuery(SearchableExprImpl<?> expr) {
        final BoolQueryBuilder query = boolQuery();

        exprToQuery(expr, query);

        return query;
    }

    @NotNull private FieldExpr fromAssignment(AssignmentType at) {
        final FieldExprImpl expr = new FieldExprImpl(at.getField(), at.getFilteredValue());

        if (!at.isEquals()) return expr.not();
        return expr;
    }

    /** Check if query is a wildcard or a fuzzy query. */
    @NotNull private QueryBuilder fuzzyOrWildcard(FieldExprImpl expr, String value) {
        final String analyzedValue = value.trim().toLowerCase();
        if (expr.isFuzzy()) return fuzzyQuery(expr.getName(), analyzedValue).fuzziness(fromEdits(expr.getFuzzy()));
        else if (analyzedValue.contains("*")) return wildcardQuery(expr.getName(), analyzedValue);
        else return matchPhraseQuery(expr.getName(), analyzedValue);
    }

    private Map<String, Object> processExtraFields(@NotNull final JsonObject source) {
        final Map<String, Object> extraFields = new HashMap<>();
        for (final SearchableFieldImpl<?, ?> field : getSearchFields()) {
            final JsonElement json = source.get(field.getId());
            extraFields.put(field.getId(), field.getValue(json));
        }
        return extraFields;
    }

    private List<SearchResult> processResults(List<io.searchbox.core.SearchResult.Hit<JsonObject, Void>> hits) {
        final List<SearchResult> l = new ArrayList<>();

        for (final io.searchbox.core.SearchResult.Hit<JsonObject, Void> doc : hits) {
            final JsonObject source = doc.source;
            final String     key    = source.get(ES_METADATA_ID).getAsString();

            final JsonElement image       = source.get(IMAGE_PATH_FIELD);
            Option<String>    imageOption = Option.empty();
            if (image != null) imageOption = option(image.getAsString());

            l.add(
                result(key,
                    source.get(DESCRIBED_BY_FIELD).getAsString(),
                    source.get(TO_STRING_FIELD).getAsString(),
                    imageOption,
                    doc.score,
                    processExtraFields(source)));
        }

        return l;
    }

    private void setFilter(List<AssignmentType> f) {
        final SearchableExpr searchableExpr = fromIterable(f).filter(AssignmentType::canAddAssignment)
                                              .foldLeft(EMPTY_EXPR, (expr, assignment) ->
                    expr.and(fromAssignment(assignment)));
        filter = searchableExpr == EMPTY_EXPR ? searchableExpr : searchableExpr.must();
    }

    //~ Methods ......................................................................................................................................

    /**
     * This method is used to convert the old similarity float used for fuzzy to the new maxEdits
     * int.
     */
    public static int floatToEdits(float minimumSimilarity, int termLen) {
        if (minimumSimilarity >= 1f) return (int) Math.min(minimumSimilarity, MAX_FUZZY_EDIT);
        else if (minimumSimilarity == 0.0f) return 0;  // 0 means exact, not infinite # of edits!
        else return Math.min((int) ((1D - minimumSimilarity) * termLen), MAX_FUZZY_EDIT);
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_FUZZY_EDIT = 2;
    public static final int DEFAULT_FUZZY  = 0;
    public static final int DEFAULT_BOOST  = 1;

    //~ Enums ........................................................................................................................................

    private enum Occur { MUST, SHOULD, MUST_NOT, EMPTY }

    //~ Inner Classes ................................................................................................................................

    static class Container extends SearchableExprImpl<Container> {
        private final ImmutableList<SearchableExpr> es;

        private Container(Iterable<SearchableExpr> es, SearchableExpr expr) {
            this.es = ImmutableList.<SearchableExpr>builder().addAll(es).add(expr).build();
        }

        @NotNull @Override public SearchableExpr also(@NotNull SearchableExpr other) {
            final SearchableExprImpl<?> impl = cast(other);
            return impl.isContainer() ? fromExpr(this, other) : fromIterable(es, other);
        }

        @NotNull @Override public SearchableExpr and(@NotNull SearchableExpr other) {
            final SearchableExprImpl<?> impl = cast(other);
            return impl.isContainer() ? fromExpr(must(), other.must()) : fromIterable(es, other.must());
        }

        /** Get inner expressions. */
        ImmutableList<SearchableExpr> expressions() {
            return es;
        }

        @Override boolean isContainer() {
            return true;
        }

        @NotNull static Container fromExpr(SearchableExpr expr, SearchableExpr other) {
            return new Container(of(expr), other);
        }

        @NotNull static Container fromIterable(Iterable<SearchableExpr> es, SearchableExpr other) {
            return new Container(es, other);
        }
    }

    static class FieldExprImpl extends SearchableExprImpl<FieldExprImpl> implements FieldExpr {
        private int                         fuzzy  = DEFAULT_FUZZY;
        private final String                name;
        private final ImmutableList<Object> values;

        private <T> FieldExprImpl(String name, Iterable<T> values) {
            this.name   = name;
            this.values = builder().addAll(values).build();
        }

        @NotNull @Override public SearchableExpr also(@NotNull SearchableExpr other) {
            return fromExpr(this, other);
        }

        @NotNull @Override public SearchableExpr and(@NotNull SearchableExpr other) {
            return fromExpr(must(), other.must());
        }

        @NotNull @Override public FieldExpr boost(float boost) {
            return (FieldExpr) super.boost(boost);
        }

        @NotNull @Override public FieldExpr fuzzy(int f) {
            fuzzy = min(MAX_FUZZY_EDIT, max(f, 0));
            return this;
        }

        public String getName() {
            return name;
        }

        ImmutableList<Object> values() {
            return values;
        }

        int getFuzzy() {
            return fuzzy;
        }

        @Override boolean isContainer() {
            return false;
        }

        boolean isFuzzy() {
            return fuzzy != DEFAULT_FUZZY;
        }

        @NotNull static FieldExpr fromValue(String name, Object value) {
            return new FieldExprImpl(name, of(value));
        }

        @NotNull static <T> FieldExpr fromValues(String name, Iterable<T> value) {
            return new FieldExprImpl(name, value);
        }
    }  // end class FieldExprImpl

    abstract static class SearchableExprImpl<This extends SearchableExprImpl<This>> implements SearchableExpr {
        private float boost = DEFAULT_BOOST;
        private Occur occur = Occur.EMPTY;

        @NotNull @Override public SearchableExpr boost(float b) {
            boost = b;
            return this;
        }

        @NotNull @Override public This must() {
            if (occur == Occur.EMPTY) occur = Occur.MUST;
            return cast(this);
        }

        @NotNull @Override public This not() {
            if (occur == Occur.EMPTY) occur = Occur.MUST_NOT;
            return cast(this);
        }

        Container asContainer() {
            return (Container) this;
        }

        FieldExprImpl asField() {
            return (FieldExprImpl) this;
        }

        boolean hasBoost() {
            return boost != DEFAULT_BOOST;
        }

        float getBoost() {
            return boost;
        }

        abstract boolean isContainer();
    }  // end class SearchableExprImpl
}  // end class IndexSearcher
