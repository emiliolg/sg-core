
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
public class ProductTable extends DbTable<Product, String> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str                  CODE;
    public final TableField.Enum<Color, Integer> COLOR;
    public final TableField.Str                  MAIN_CATEGORY_CODE;
    public final TableField.Str                  NAME;
    public final TableField.Str                  OPT_CATEGORY_CODE;
    public final TableField.DTime                UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    ProductTable() {
        super(Product.class, "MODEL", "PRODUCT", "", Modifier.NONE, CacheType.NONE);
        CODE        = strField("code", "CODE", 30);
        NAME        = strField("name", "NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        COLOR       = enumField("color", "COLOR", Color.class);

        MAIN_CATEGORY_CODE = strField("mainCategoryCode", "MAIN_CATEGORY_CODE", 30);
        OPT_CATEGORY_CODE  = strField("optCategoryCode", "OPT_CATEGORY_CODE", 30);
        primaryKey(listOf(CODE));
    }

    //~ Methods ......................................................................................................................................

    public ProductTable as(final String alias) {
        return createAlias(new ProductTable(), alias);
    }

    @Override protected EntityTable<Product, String> createEntityTable() {
        return new EntityTable<>(PRODUCT);
    }

    @Override protected String strToKey(@NotNull String key) {
        return key;
    }

    //~ Static Fields ................................................................................................................................

    public static final ProductTable PRODUCT = new ProductTable();
}  // end class ProductTable
