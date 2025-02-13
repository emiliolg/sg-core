package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.sales.basic.Item;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Item */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ItemTable
    extends DbTable<Item,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int INVOICE_ID_KEY;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str PRODUCT_PRODUCT_ID;
    @NotNull public final Int QUANTITY;
    @NotNull public final Int DISCOUNT;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ItemTable() {
        super(Item.class,"BASIC","ITEM","",Modifier.NONE,CacheType.NONE);
        INVOICE_ID_KEY = intField("invoiceIdKey", "INVOICE_ID_KEY", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        PRODUCT_PRODUCT_ID = strField("productProductId", "PRODUCT_PRODUCT_ID", 8);
        QUANTITY = intField("quantity", "QUANTITY", false, 9);
        DISCOUNT = intField("discount", "DISCOUNT", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(INVOICE_ID_KEY, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ItemTable as(@NotNull String alias) { return createAlias(new ItemTable(), alias); }

    @Override @NotNull protected final EntityTable<Item,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(ITEM); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ItemTable ITEM = new ItemTable();

}
