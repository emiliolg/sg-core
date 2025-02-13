package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.Synchronous;
import static tekgenesis.type.Modifier.AUDITABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Synchronous */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class SynchronousTable
    extends DbTable<Synchronous,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime CREATION_TIME;
    @NotNull public final Str CREATION_USER;
    @NotNull public final Str UPDATE_USER;

    //~ Constructors .............................................................................................................

    private SynchronousTable() {
        super(Synchronous.class,"SHOWCASE","SYNCHRONOUS","SYNCHRONOUS_SEQ",EnumSet.of(AUDITABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 40);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        CREATION_TIME = dTimeField("creationTime", "CREATION_TIME");
        CREATION_USER = strInternField("creationUser", "CREATION_USER", 100);
        UPDATE_USER = strInternField("updateUser", "UPDATE_USER", 100);
        primaryKey(listOf(ID));
        secondaryKeys(listOf(listOf(NAME)));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final SynchronousTable as(@NotNull String alias) { return createAlias(new SynchronousTable(), alias); }

    @Override @NotNull protected final EntityTable<Synchronous,Integer> createEntityTable() { return new EntityTable<>(SYNCHRONOUS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final SynchronousTable SYNCHRONOUS = new SynchronousTable();

}
