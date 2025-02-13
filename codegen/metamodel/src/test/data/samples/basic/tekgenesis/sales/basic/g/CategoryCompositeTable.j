package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategoryComposite;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategoryComposite */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryCompositeTable
    extends DbTable<CategoryComposite,Tuple3<Long,String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final LongFld ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str DESCR;
    @NotNull public final Str SHORT_DESC;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategoryCompositeTable() {
        super(CategoryComposite.class,"BASIC","CATEGORY_COMPOSITE","",EnumSet.of(REMOTABLE),CacheType.NONE);
        ID_KEY = longField("idKey", "ID_KEY", false, 18);
        NAME = strField("name", "NAME", 30);
        DESCR = strField("descr", "DESCR", 120);
        SHORT_DESC = strField("shortDesc", "SHORT_DESC", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY, DESCR, SHORT_DESC));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple3<Long,String,String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(Conversions.toLong(parts[0]), parts[1], parts[2]);
    }

    @Override @NotNull public final CategoryCompositeTable as(@NotNull String alias) { return createAlias(new CategoryCompositeTable(), alias); }

    @Override @NotNull protected final EntityTable<CategoryComposite,Tuple3<Long,String,String>> createEntityTable() { return new EntityTable<>(CATEGORY_COMPOSITE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryCompositeTable CATEGORY_COMPOSITE = new CategoryCompositeTable();

}
