package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.showcase.SimpleEntitySearcher;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.SimpleEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class SimpleEntityTable
    extends DbTable<SimpleEntity,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private SimpleEntityTable() {
        super(SimpleEntity.class,"SHOWCASE","SIMPLE_ENTITY","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        NAME = strField("name", "NAME", 255);
        DESCRIPTION = strField("description", "DESCRIPTION", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(NAME));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<SimpleEntitySearcher> searcher() {
        return Option.of(SimpleEntitySearcher.SIMPLE_ENTITY_SEARCHER);
    }

    @Override @NotNull public final SimpleEntityTable as(@NotNull String alias) { return createAlias(new SimpleEntityTable(), alias); }

    @Override @NotNull protected final EntityTable<SimpleEntity,String> createEntityTable() { return new EntityTable<>(SIMPLE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final SimpleEntityTable SIMPLE_ENTITY = new SimpleEntityTable();

}
