
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Test class.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class CategoryTable extends DbTable<Category, String> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str   CODE;
    public final TableField.Str   NAME;
    public final TableField.Str   PARENT_CODE;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    private CategoryTable() {
        super(Category.class, "MODEL", "CATEGORY", "", Modifier.NONE, CacheType.NONE);
        CODE        = strField("code", "CODE", 30);
        NAME        = strField("name", "NAME", 30);
        PARENT_CODE = strField("parentCode", "PARENT_CODE", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(CODE));
    }

    //~ Methods ......................................................................................................................................

    public CategoryTable as(final String alias) {
        return createAlias(new CategoryTable(), alias);
    }

    @Override protected EntityTable<Category, String> createEntityTable() {
        return new EntityTable<>(CATEGORY);
    }
    @Override protected String strToKey(@NotNull String key) {
        return key;
    }

    //~ Static Fields ................................................................................................................................

    public static final CategoryTable CATEGORY = new CategoryTable();
}
