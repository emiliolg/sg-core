
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import java.lang.reflect.Field;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.util.Reflection;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.nomm.model.Category;
import tekgenesis.persistence.nomm.model.Color;
import tekgenesis.persistence.nomm.model.Product;

import static tekgenesis.persistence.nomm.model.CategoryTable.CATEGORY;
import static tekgenesis.persistence.nomm.model.ProductTable.PRODUCT;

/**
 * Javadoc.
 */
public class ProductBase implements EntityInstance<Product, String> {

    //~ Instance Fields ..............................................................................................................................

    String code  = "";
    Color  color = Color.BLACK;

    long instanceVersion;

    EntityRef<Category, String> mainCategory     = new EntityRef<>(CATEGORY, Category::getProducts);
    String                      mainCategoryCode = "";
    String                      name             = "";

    EntityRef<Category, String> optCategory     = new EntityRef<>(CATEGORY);
    String                      optCategoryCode = null;

    DateTime updateTime = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof ProductBase && keyObject().equals(((ProductBase) obj).keyObject());
    }

    @NotNull public EntityTable<Product, String> et() {
        return myEntityTable();
    }

    @Override public int hashCode() {
        return keyObject().hashCode();
    }

    public long incrementVersion() {
        return instanceVersion++;
    }

    @NotNull @Override public String keyAsString() {
        return code;
    }
    @NotNull @Override public String keyObject() {
        return code;
    }

    @Override public DbTable<Product, String> table() {
        return PRODUCT;
    }

    @Override public String toString() {
        return code;
    }

    /** Javadoc. */
    public String getCode() {
        return code;
    }

    /** Javadoc. */
    public Color getColor() {
        return color;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    /** Javadoc. */
    @Nullable public Category getMainCategory() {
        return mainCategory.solve(mainCategoryCode);
    }

    /** Javadoc. */
    public String getName() {
        return name;
    }

    /** Javadoc. */
    @Nullable public Category getOptCategory() {
        return optCategory.solve(optCategoryCode);
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    <T extends ProductBase> T copyTo(T to) {
        to.code             = code;
        to.updateTime       = updateTime;
        to.color            = color;
        to.mainCategory     = mainCategory;
        to.name             = name;
        to.mainCategoryCode = mainCategoryCode;
        to.optCategoryCode  = optCategoryCode;
        to.optCategory      = optCategory;
        return to;
    }

    //~ Methods ......................................................................................................................................

    /** Javadoc. */
    @Nullable public static Product find(String code) {
        return myEntityTable().find(code);
    }

    static EntityTable<Product, String> myEntityTable() {
        return EntityTable.forTable(PRODUCT);
    }

    //~ Static Fields ................................................................................................................................

    private static final transient Field CATEGORY_PRODUCTS = Reflection.findFieldOrFail(Category.class, "products");
}  // end class ProductBase
