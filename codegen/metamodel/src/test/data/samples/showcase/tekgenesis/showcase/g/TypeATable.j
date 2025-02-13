package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Real;
import tekgenesis.showcase.TypeA;
import tekgenesis.showcase.TypeASearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TypeA */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TypeATable
    extends DbTable<TypeA,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Decimal D;
    @NotNull public final Int I;
    @NotNull public final Real R;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TypeATable() {
        super(TypeA.class,"SHOWCASE","TYPE_A","TYPE_A_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        D = decimalField("d", "D", false, 10, 2);
        I = intField("i", "I", false, 9);
        R = realField("r", "R", false);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<TypeASearcher> searcher() { return Option.of(TypeASearcher.TYPE_ASEARCHER); }

    @Override @NotNull public final TypeATable as(@NotNull String alias) { return createAlias(new TypeATable(), alias); }

    @Override @NotNull protected final EntityTable<TypeA,Integer> createEntityTable() { return new EntityTable<>(TYPE_A); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TypeATable TYPE_A = new TypeATable();

}
