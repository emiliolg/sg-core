package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Review;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.AUDITABLE;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Review */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ReviewTable
    extends DbTable<Review,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Int PRODUCT_ID;
    @NotNull public final Str REVIEW_;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime CREATION_TIME;
    @NotNull public final Str CREATION_USER;
    @NotNull public final Str UPDATE_USER;

    //~ Constructors .............................................................................................................

    private ReviewTable() {
        super(Review.class,"BASIC","REVIEW","REVIEW_SEQ",EnumSet.of(AUDITABLE, REMOTABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        PRODUCT_ID = intField("productId", "PRODUCT_ID", false, 9);
        REVIEW_ = strField("review", "REVIEW", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        CREATION_TIME = dTimeField("creationTime", "CREATION_TIME");
        CREATION_USER = strInternField("creationUser", "CREATION_USER", 100);
        UPDATE_USER = strInternField("updateUser", "UPDATE_USER", 100);
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ReviewTable as(@NotNull String alias) { return createAlias(new ReviewTable(), alias); }

    @Override @NotNull protected final EntityTable<Review,Integer> createEntityTable() { return new EntityTable<>(REVIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ReviewTable REVIEW = new ReviewTable();

}
