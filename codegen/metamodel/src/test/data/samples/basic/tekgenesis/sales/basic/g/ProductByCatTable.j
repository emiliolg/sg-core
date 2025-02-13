package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductByCat;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.AUDITABLE;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductByCat */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductByCatTable
    extends DbTable<ProductByCat,Tuple<String,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str PRODUCT_PRODUCT_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final LongFld SECONDARY_CATEGORY_ID_KEY;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime CREATION_TIME;
    @NotNull public final Str CREATION_USER;
    @NotNull public final Str UPDATE_USER;

    //~ Constructors .............................................................................................................

    private ProductByCatTable() {
        super(ProductByCat.class,"BASIC","PRODUCT_BY_CAT","",EnumSet.of(AUDITABLE, REMOTABLE),CacheType.NONE);
        PRODUCT_PRODUCT_ID = strField("productProductId", "PRODUCT_PRODUCT_ID", 8);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        SECONDARY_CATEGORY_ID_KEY = longField("secondaryCategoryIdKey", "SECONDARY_CATEGORY_ID_KEY", false, 18);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        CREATION_TIME = dTimeField("creationTime", "CREATION_TIME");
        CREATION_USER = strInternField("creationUser", "CREATION_USER", 100);
        UPDATE_USER = strInternField("updateUser", "UPDATE_USER", 100);
        primaryKey(listOf(PRODUCT_PRODUCT_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ProductByCatTable as(@NotNull String alias) { return createAlias(new ProductByCatTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductByCat,Tuple<String,Integer>> createEntityTable() { return new InnerEntityTable<>(PRODUCT_BY_CAT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductByCatTable PRODUCT_BY_CAT = new ProductByCatTable();

}
