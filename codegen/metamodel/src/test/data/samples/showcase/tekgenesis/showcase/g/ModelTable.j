package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.TableField.Date;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.Model;
import tekgenesis.showcase.ModelSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Model */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ModelTable
    extends DbTable<Model,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int MAKE_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str MODEL_;
    @NotNull public final Date RELEASED;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ModelTable() {
        super(Model.class,"SHOWCASE","MODEL","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        MAKE_ID = intField("makeId", "MAKE_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        MODEL_ = strField("model", "MODEL", 30);
        RELEASED = dateField("released", "RELEASED");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(MAKE_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull protected Option<ModelSearcher> searcher() { return Option.of(ModelSearcher.MODEL_SEARCHER); }

    @Override @NotNull public final ModelTable as(@NotNull String alias) { return createAlias(new ModelTable(), alias); }

    @Override @NotNull protected final EntityTable<Model,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(MODEL); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ModelTable MODEL = new ModelTable();

}
