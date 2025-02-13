package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.TypeB;
import tekgenesis.showcase.TypeBSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TypeB */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TypeBTable
    extends DbTable<TypeB,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str S;
    @NotNull public final DTime T;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TypeBTable() {
        super(TypeB.class,"SHOWCASE","TYPE_B","TYPE_B_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        S = strField("s", "S", 60);
        T = dTimeField("t", "T");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<TypeBSearcher> searcher() { return Option.of(TypeBSearcher.TYPE_BSEARCHER); }

    @Override @NotNull public final TypeBTable as(@NotNull String alias) { return createAlias(new TypeBTable(), alias); }

    @Override @NotNull protected final EntityTable<TypeB,Integer> createEntityTable() { return new EntityTable<>(TYPE_B); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TypeBTable TYPE_B = new TypeBTable();

}
