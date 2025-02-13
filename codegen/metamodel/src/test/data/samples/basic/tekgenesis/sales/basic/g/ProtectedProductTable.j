package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProtectedProduct;
import tekgenesis.sales.basic.ProtectedProductSearcher;
import tekgenesis.sales.basic.State;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.PROTECTED;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProtectedProduct */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProtectedProductTable
    extends DbTable<ProtectedProduct,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str PRODUCT_ID;
    @NotNull public final Str MODEL;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Decimal PRICE;
    @NotNull public final Enum<State,String> STATE;
    @NotNull public final LongFld CATEGORY_ID_KEY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProtectedProductTable() {
        super(ProtectedProduct.class,"BASIC","PROTECTED_PRODUCT","",EnumSet.of(PROTECTED, DEFAULT_SEARCHABLE),CacheType.NONE);
        PRODUCT_ID = strField("productId", "PRODUCT_ID", 8);
        MODEL = strField("model", "MODEL", 30);
        DESCRIPTION = strField("description", "DESCRIPTION", 100);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        STATE = enumField("state", "STATE", State.class);
        CATEGORY_ID_KEY = longField("categoryIdKey", "CATEGORY_ID_KEY", false, 18);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(PRODUCT_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<ProtectedProductSearcher> searcher() {
        return Option.of(ProtectedProductSearcher.PROTECTED_PRODUCT_SEARCHER);
    }

    @Override @NotNull public final ProtectedProductTable as(@NotNull String alias) { return createAlias(new ProtectedProductTable(), alias); }

    @Override @NotNull protected final EntityTable<ProtectedProduct,String> createEntityTable() { return new EntityTable<>(PROTECTED_PRODUCT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProtectedProductTable PROTECTED_PRODUCT = new ProtectedProductTable();

}
