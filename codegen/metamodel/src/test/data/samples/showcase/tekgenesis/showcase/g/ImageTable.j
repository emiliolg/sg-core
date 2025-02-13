package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.showcase.Image;
import tekgenesis.showcase.ImageSearcher;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Image */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ImageTable
    extends DbTable<Image,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Res RESOURCE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ImageTable() {
        super(Image.class,"SHOWCASE","IMAGE","IMAGE_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 255);
        RESOURCE = resourceField("resource", "RESOURCE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ImageSearcher> searcher() { return Option.of(ImageSearcher.IMAGE_SEARCHER); }

    @Override @NotNull public final ImageTable as(@NotNull String alias) { return createAlias(new ImageTable(), alias); }

    @Override @NotNull protected final EntityTable<Image,Integer> createEntityTable() { return new EntityTable<>(IMAGE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ImageTable IMAGE = new ImageTable();

}
