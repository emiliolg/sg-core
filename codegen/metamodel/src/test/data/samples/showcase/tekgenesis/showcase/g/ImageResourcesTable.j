package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.ImageResources;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.ImageResources */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ImageResourcesTable
    extends DbTable<ImageResources,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int IMAGE_RESOURCE_ID_KEY;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str NAME;
    @NotNull public final Res IMG;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ImageResourcesTable() {
        super(ImageResources.class,"SHOWCASE","IMAGE_RESOURCES","",Modifier.NONE,CacheType.NONE);
        IMAGE_RESOURCE_ID_KEY = intField("imageResourceIdKey", "IMAGE_RESOURCE_ID_KEY", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        NAME = strField("name", "NAME", 255);
        IMG = resourceField("img", "IMG");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(IMAGE_RESOURCE_ID_KEY, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ImageResourcesTable as(@NotNull String alias) { return createAlias(new ImageResourcesTable(), alias); }

    @Override @NotNull protected final EntityTable<ImageResources,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(IMAGE_RESOURCES); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ImageResourcesTable IMAGE_RESOURCES = new ImageResourcesTable();

}
