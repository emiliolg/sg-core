
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.util.List;

import tekgenesis.form.FormTable;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;

/**
 * User class for Form: CategoriesService
 */
public class CategoriesService extends CategoriesServiceBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<CategoriesRow> rows = getCategories();

        final Long           from       = getFrom();
        final List<Category> categories = selectFrom(CATEGORY).where(from == null ? null : CATEGORY.ID_KEY.ge(from))
                                          .limit(getLimit())
                                          .orderBy(CATEGORY.ID_KEY)
                                          .list();

        for (final Category category : categories)
            rows.add().populate(category);

        setNext(null);

        if (!rows.isEmpty()) {
            final Category last = categories.get(categories.size() - 1);
            final Category next = selectFrom(CATEGORY).where(CATEGORY.ID_KEY.gt(last.getIdKey())).get();
            if (next != null) setNext(next.getIdKey());
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class CategoriesRow extends CategoriesRowBase {}
}
