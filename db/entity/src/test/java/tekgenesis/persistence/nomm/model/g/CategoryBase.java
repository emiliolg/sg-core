
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.*;
import tekgenesis.persistence.nomm.model.Category;
import tekgenesis.persistence.nomm.model.Product;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.nomm.model.CategoryTable.CATEGORY;
import static tekgenesis.persistence.nomm.model.ProductTable.PRODUCT;

/**
 * Test Class.
 */
public class CategoryBase implements EntityInstance<Category, String> {

    //~ Instance Fields ..............................................................................................................................

    EntitySeq<Category> children = createEntitySeq(CATEGORY, (Category) this, c -> ((CategoryBase) c).parent, listOf(CATEGORY.PARENT_CODE));

    String code = "";

    long                        instanceVersion = 0;
    String                      name            = "";
    EntityRef<Category, String> parent          = new EntityRef<>(CATEGORY);
    String                      parentCode      = "";
    EntitySeq<Product>          products        = createEntitySeq(PRODUCT,
            (Category) this,
            p -> ((ProductBase) p).mainCategory,
            listOf(PRODUCT.MAIN_CATEGORY_CODE));
    DateTime                    updateTime      = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<Category, String> et() {
        return myEntityTable();
    }

    @NotNull @Override public String keyAsString() {
        return code;
    }
    @NotNull @Override public String keyObject() {
        return code;
    }

    @Override public DbTable<Category, String> table() {
        return CATEGORY;
    }

    public EntitySeq<Category> getChildren() {
        return children;
    }

    public String getCode() {
        return code;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    public String getName() {
        return name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public EntitySeq<Product> getProducts() {
        return products;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    <T extends CategoryBase> T copyTo(T to) {
        to.code       = code;
        to.updateTime = updateTime;
        to.children   = children;
        to.parent     = parent;
        to.parentCode = parentCode;
        to.products   = products;
        return to;
    }

    //~ Methods ......................................................................................................................................

    /** Javadoc. */
    @Nullable public static Category find(String key) {
        return myEntityTable().find(key);
    }
    @NotNull public static Category findOrFail(String key) {
        return myEntityTable().findOrFail(key);
    }

    public static Select<Category> query() {
        return selectFrom(CATEGORY);
    }

    private static EntityTable<Category, String> myEntityTable() {
        return EntityTable.forTable(CATEGORY);
    }
}  // end class CategoryBase
