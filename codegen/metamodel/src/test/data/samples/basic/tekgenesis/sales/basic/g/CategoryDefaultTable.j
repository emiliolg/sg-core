package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategoryDefault;
import tekgenesis.sales.basic.CategoryDefaultSearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEPRECABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategoryDefault */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryDefaultTable
    extends DbTable<CategoryDefault,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str DESCR;
    @NotNull public final Int PARENT_ID;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime DEPRECATION_TIME;
    @NotNull public final Str DEPRECATION_USER;

    //~ Constructors .............................................................................................................

    private CategoryDefaultTable() {
        super(CategoryDefault.class,"BASIC","CATEGORY_DEFAULT","CATEGORY_DEFAULT_SEQ",EnumSet.of(DEPRECABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 30);
        DESCR = strField("descr", "DESCR", 120);
        PARENT_ID = intField("parentId", "PARENT_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        DEPRECATION_TIME = dTimeField("deprecationTime", "DEPRECATION_TIME");
        DEPRECATION_USER = strInternField("deprecationUser", "DEPRECATION_USER", 100);
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<CategoryDefaultSearcher> searcher() {
        return Option.of(CategoryDefaultSearcher.CATEGORY_DEFAULT_SEARCHER);
    }

    @Override @NotNull public final CategoryDefaultTable as(@NotNull String alias) { return createAlias(new CategoryDefaultTable(), alias); }

    @Override @NotNull protected final EntityTable<CategoryDefault,Integer> createEntityTable() { return new EntityTable<>(CATEGORY_DEFAULT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryDefaultTable CATEGORY_DEFAULT = new CategoryDefaultTable();

}
