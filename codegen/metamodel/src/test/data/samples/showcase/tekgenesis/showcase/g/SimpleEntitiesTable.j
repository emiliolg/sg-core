package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.SimpleEntities;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.SimpleEntities */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class SimpleEntitiesTable
    extends DbTable<SimpleEntities,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ANOTHER_SIMPLE_ENTITY_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str SIMPLE_ENTITY_NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private SimpleEntitiesTable() {
        super(SimpleEntities.class,"SHOWCASE","SIMPLE_ENTITIES","",Modifier.NONE,CacheType.NONE);
        ANOTHER_SIMPLE_ENTITY_ID = intField("anotherSimpleEntityId", "ANOTHER_SIMPLE_ENTITY_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        SIMPLE_ENTITY_NAME = strField("simpleEntityName", "SIMPLE_ENTITY_NAME", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ANOTHER_SIMPLE_ENTITY_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final SimpleEntitiesTable as(@NotNull String alias) { return createAlias(new SimpleEntitiesTable(), alias); }

    @Override @NotNull protected final EntityTable<SimpleEntities,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(SIMPLE_ENTITIES); }

    //~ Fields ...................................................................................................................

    @NotNull public static final SimpleEntitiesTable SIMPLE_ENTITIES = new SimpleEntitiesTable();

}
