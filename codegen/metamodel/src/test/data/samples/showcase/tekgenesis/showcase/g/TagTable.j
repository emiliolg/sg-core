package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.Tag;
import tekgenesis.showcase.TagSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Tag */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TagTable
    extends DbTable<Tag,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TagTable() {
        super(Tag.class,"SHOWCASE","TAG","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        NAME = strField("name", "NAME", 20);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(NAME));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<TagSearcher> searcher() { return Option.of(TagSearcher.TAG_SEARCHER); }

    @Override @NotNull public final TagTable as(@NotNull String alias) { return createAlias(new TagTable(), alias); }

    @Override @NotNull protected final EntityTable<Tag,String> createEntityTable() { return new EntityTable<>(TAG); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TagTable TAG = new TagTable();

}
