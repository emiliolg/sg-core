package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.Label;
import tekgenesis.showcase.LabelSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Label */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class LabelTable
    extends DbTable<Label,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str WITH_LABEL1;
    @NotNull public final Str WITH_LABEL2;
    @NotNull public final Str NO_LABEL1;
    @NotNull public final Str NO_LABEL2;
    @NotNull public final Str NO_LABEL3;
    @NotNull public final Str NO_LABEL4;
    @NotNull public final Str SOME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private LabelTable() {
        super(Label.class,"SHOWCASE","LABEL","LABEL_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        WITH_LABEL1 = strField("withLabel1", "WITH_LABEL1", 255);
        WITH_LABEL2 = strField("withLabel2", "WITH_LABEL2", 255);
        NO_LABEL1 = strField("noLabel1", "NO_LABEL1", 255);
        NO_LABEL2 = strField("noLabel2", "NO_LABEL2", 255);
        NO_LABEL3 = strField("noLabel3", "NO_LABEL3", 255);
        NO_LABEL4 = strField("noLabel4", "NO_LABEL4", 255);
        SOME = strField("some", "SOME", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<LabelSearcher> searcher() { return Option.of(LabelSearcher.LABEL_SEARCHER); }

    @Override @NotNull public final LabelTable as(@NotNull String alias) { return createAlias(new LabelTable(), alias); }

    @Override @NotNull protected final EntityTable<Label,Integer> createEntityTable() { return new EntityTable<>(LABEL); }

    //~ Fields ...................................................................................................................

    @NotNull public static final LabelTable LABEL = new LabelTable();

}
