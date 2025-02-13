package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.Category;
import tekgenesis.sales.basic.CategorySearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.LongFld;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Category */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryTable
    extends DbTable<Category,Long>
{

    //~ Fields ...................................................................................................................

    @NotNull public final LongFld ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str DESCR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategoryTable() {
        super(Category.class,"BASIC","CATEGORY","",Modifier.NONE,CacheType.NONE);
        ID_KEY = longField("idKey", "ID_KEY", false, 18);
        NAME = strField("name", "NAME", 30);
        DESCR = strField("descr", "DESCR", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Long strToKey(@NotNull String key) { return Conversions.toLong(key); }

    @Override @NotNull protected Option<CategorySearcher> searcher() { return Option.of(CategorySearcher.CATEGORY_SEARCHER); }

    @Override @NotNull public final CategoryTable as(@NotNull String alias) { return createAlias(new CategoryTable(), alias); }

    @Override @NotNull protected final EntityTable<Category,Long> createEntityTable() { return new EntityTable<>(CATEGORY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryTable CATEGORY = new CategoryTable();

}
