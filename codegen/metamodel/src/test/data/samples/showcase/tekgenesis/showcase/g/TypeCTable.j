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
import tekgenesis.showcase.TypeC;
import tekgenesis.showcase.TypeCSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TypeC */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TypeCTable
    extends DbTable<TypeC,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str A;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TypeCTable() {
        super(TypeC.class,"SHOWCASE","TYPE_C","TYPE_C_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        A = strField("a", "A", 60);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<TypeCSearcher> searcher() { return Option.of(TypeCSearcher.TYPE_CSEARCHER); }

    @Override @NotNull public final TypeCTable as(@NotNull String alias) { return createAlias(new TypeCTable(), alias); }

    @Override @NotNull protected final EntityTable<TypeC,Integer> createEntityTable() { return new EntityTable<>(TYPE_C); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TypeCTable TYPE_C = new TypeCTable();

}
