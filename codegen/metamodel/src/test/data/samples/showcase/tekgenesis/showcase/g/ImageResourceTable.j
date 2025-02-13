package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.ImageResource;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Res;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.ImageResource */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ImageResourceTable
    extends DbTable<ImageResource,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Res IMG;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ImageResourceTable() {
        super(ImageResource.class,"SHOWCASE","IMAGE_RESOURCE","",Modifier.NONE,CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        IMG = resourceField("img", "IMG");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ImageResourceTable as(@NotNull String alias) { return createAlias(new ImageResourceTable(), alias); }

    @Override @NotNull protected final EntityTable<ImageResource,Integer> createEntityTable() { return new EntityTable<>(IMAGE_RESOURCE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ImageResourceTable IMAGE_RESOURCE = new ImageResourceTable();

}
