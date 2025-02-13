
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.persistence.*;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.index.SearchResult.result;
import static tekgenesis.persistence.EntityTable.forName;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Searcher that searches the database instead of using indexes.
 */
public abstract class DatabaseSearcher extends Searcher {

    //~ Constructors .................................................................................................................................

    protected DatabaseSearcher(Class<? extends EntityInstance<?, ?>> entity) {
        super(entity);
    }

    //~ Methods ......................................................................................................................................

    @Override public List<SearchResult> search(String query) {
        return search(query, topHits);
    }

    /** Search overriding query limit. */
    @NotNull public final List<SearchResult> search(String query, int hits) {
        final EntityTable<?, ?> table = forName(entity);

        final Criteria c = fromArray(query.toLowerCase().split(" ")).foldLeft(Criteria.EMPTY,
                (criteria, term) -> criteria.and(collectTerm(table, term)));

        return invokeInTransaction(() -> selectFrom(table.getDbTable()).where(c).limit(hits).map(this::processResult).toList());
    }

    private Criteria collectTerm(EntityTable<?, ?> table, @NotNull final String term) {
        return getSearchFields().foldLeft(Criteria.EMPTY, (criteria, field) -> criteria.or(likeCriteria(term, table, criteria, field)));
    }

    private Criteria likeCriteria(String query, EntityTable<?, ?> table, Criteria criteria, SearchableFieldImpl<?, ?> field) {
        return getTableField(table, field).like("%" + query.toUpperCase() + "%");
    }

    @NotNull private SearchResult processResult(EntityInstance<?, ?> hit) {
        final Option<String> imageOption = Option.ofNullable(hit instanceof HasImage ? ((HasImage) hit).imagePath() : null);
        // TODO score? extraFields?
        return result(hit.keyAsString(), hit.describe().mkString("\t"), hit.toString(), imageOption, 1);
    }

    /** Get table field or fail. If field is of type Str return it as a lowercase Str field. */
    @NotNull private Expr<?> getTableField(EntityTable<?, ?> table, SearchableFieldImpl<?, ?> field) {
        final TableField<?> tableField = table.getMetadata().getFieldOrFail(field.getEntityFieldName());

        return tableField instanceof Str ? ((Str) tableField).upper() : tableField;
    }
}  // end class DatabaseSearcher
