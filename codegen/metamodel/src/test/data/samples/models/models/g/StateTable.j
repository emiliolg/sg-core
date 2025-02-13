package models.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import models.State;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity models.State */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class StateTable
    extends DbTable<State,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str COUNTRY_CODE;
    @NotNull public final Str CODE;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private StateTable() {
        super(State.class,"MODELS","STATE","",Modifier.NONE,CacheType.NONE);
        COUNTRY_CODE = strField("countryCode", "COUNTRY_CODE", 2);
        CODE = strField("code", "CODE", 2);
        DESCRIPTION = strField("description", "DESCRIPTION", 40);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(COUNTRY_CODE, CODE));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], parts[1]);
    }

    @Override @NotNull public final StateTable as(@NotNull String alias) { return createAlias(new StateTable(), alias); }

    @Override @NotNull protected final EntityTable<State,Tuple<String,String>> createEntityTable() { return new EntityTable<>(STATE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final StateTable STATE = new StateTable();

}
