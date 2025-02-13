package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategorySqlView;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategorySqlView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategorySqlViewTable
    extends DbTable<CategorySqlView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str DESCR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategorySqlViewTable() {
        super(CategorySqlView.class,"BASIC","CATEGORY_SQL_VIEW","",EnumSet.of(REMOTABLE),CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        NAME = strField("name", "NAME", 30);
        DESCR = strField("descr", "DESCR", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final CategorySqlViewTable as(@NotNull String alias) { return createAlias(new CategorySqlViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CategorySqlView,Integer> createEntityTable() { return new EntityTable<>(CATEGORY_SQL_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategorySqlViewTable CATEGORY_SQL_VIEW = new CategorySqlViewTable();

}
