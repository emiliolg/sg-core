package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductData;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductData */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDataTable
    extends DbTable<ProductData,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str ENTITY_PRODUCT_ID;
    @NotNull public final DTime CREATION;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDataTable() {
        super(ProductData.class,"BASIC","PRODUCT_DATA","PRODUCT_DATA_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        ENTITY_PRODUCT_ID = strField("entityProductId", "ENTITY_PRODUCT_ID", 8);
        CREATION = dTimeField("creation", "CREATION");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ProductDataTable as(@NotNull String alias) { return createAlias(new ProductDataTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductData,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DATA); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDataTable PRODUCT_DATA = new ProductDataTable();

}
