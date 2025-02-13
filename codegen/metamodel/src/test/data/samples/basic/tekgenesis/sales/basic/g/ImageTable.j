package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.sales.basic.Image;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Image */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ImageTable
    extends DbTable<Image,Tuple<String,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str PRODUCT_PRODUCT_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Res IMAGE_ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ImageTable() {
        super(Image.class,"BASIC","IMAGE","",Modifier.NONE,CacheType.NONE);
        PRODUCT_PRODUCT_ID = strField("productProductId", "PRODUCT_PRODUCT_ID", 8);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        IMAGE_ID = resourceField("imageId", "IMAGE_ID");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(PRODUCT_PRODUCT_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ImageTable as(@NotNull String alias) { return createAlias(new ImageTable(), alias); }

    @Override @NotNull protected final EntityTable<Image,Tuple<String,Integer>> createEntityTable() { return new InnerEntityTable<>(IMAGE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ImageTable IMAGE = new ImageTable();

}
