package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.showcase.DeprecableEntity;
import tekgenesis.showcase.DeprecableEntitySearcher;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.DEPRECABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DeprecableEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DeprecableEntityTable
    extends DbTable<DeprecableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str SIMPLE_ENTITY_NAME;
    @NotNull public final Int ANOTHER_DEPRECABLE_ENTITY_ID;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime DEPRECATION_TIME;
    @NotNull public final Str DEPRECATION_USER;

    //~ Constructors .............................................................................................................

    private DeprecableEntityTable() {
        super(DeprecableEntity.class,"SHOWCASE","DEPRECABLE_ENTITY","DEPRECABLE_ENTITY_SEQ",EnumSet.of(DEPRECABLE, DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 20);
        SIMPLE_ENTITY_NAME = strField("simpleEntityName", "SIMPLE_ENTITY_NAME", 255);
        ANOTHER_DEPRECABLE_ENTITY_ID = intField("anotherDeprecableEntityId", "ANOTHER_DEPRECABLE_ENTITY_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        DEPRECATION_TIME = dTimeField("deprecationTime", "DEPRECATION_TIME");
        DEPRECATION_USER = strInternField("deprecationUser", "DEPRECATION_USER", 100);
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<DeprecableEntitySearcher> searcher() {
        return Option.of(DeprecableEntitySearcher.DEPRECABLE_ENTITY_SEARCHER);
    }

    @Override @NotNull public final DeprecableEntityTable as(@NotNull String alias) { return createAlias(new DeprecableEntityTable(), alias); }

    @Override @NotNull protected final EntityTable<DeprecableEntity,Integer> createEntityTable() { return new EntityTable<>(DEPRECABLE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DeprecableEntityTable DEPRECABLE_ENTITY = new DeprecableEntityTable();

}
