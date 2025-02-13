package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Real;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.TableEntity;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TableEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TableEntityTable
    extends DbTable<TableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MOE;
    @NotNull public final Int LARRY;
    @NotNull public final Real CURLY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TableEntityTable() {
        super(TableEntity.class,"SHOWCASE","TABLE_ENTITY","TABLE_ENTITY_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MOE = strField("moe", "MOE", 255);
        LARRY = intField("larry", "LARRY", false, 9);
        CURLY = realField("curly", "CURLY", false);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final TableEntityTable as(@NotNull String alias) { return createAlias(new TableEntityTable(), alias); }

    @Override @NotNull protected final EntityTable<TableEntity,Integer> createEntityTable() { return new EntityTable<>(TABLE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TableEntityTable TABLE_ENTITY = new TableEntityTable();

}
