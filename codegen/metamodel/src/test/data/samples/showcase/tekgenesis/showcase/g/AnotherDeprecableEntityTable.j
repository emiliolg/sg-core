package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherDeprecableEntity;
import tekgenesis.showcase.AnotherDeprecableEntitySearcher;
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
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.DEPRECABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.AnotherDeprecableEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class AnotherDeprecableEntityTable
    extends DbTable<AnotherDeprecableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime DEPRECATION_TIME;
    @NotNull public final Str DEPRECATION_USER;

    //~ Constructors .............................................................................................................

    private AnotherDeprecableEntityTable() {
        super(AnotherDeprecableEntity.class,"SHOWCASE","ANOTHER_DEPRECABLE_ENTITY","ANOTHER_DEPRECABLE_ENTITY_SEQ",EnumSet.of(DEPRECABLE, DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 20);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        DEPRECATION_TIME = dTimeField("deprecationTime", "DEPRECATION_TIME");
        DEPRECATION_USER = strInternField("deprecationUser", "DEPRECATION_USER", 100);
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<AnotherDeprecableEntitySearcher> searcher() {
        return Option.of(AnotherDeprecableEntitySearcher.ANOTHER_DEPRECABLE_ENTITY_SEARCHER);
    }

    @Override @NotNull public final AnotherDeprecableEntityTable as(@NotNull String alias) {
        return createAlias(new AnotherDeprecableEntityTable(), alias);
    }

    @Override @NotNull protected final EntityTable<AnotherDeprecableEntity,Integer> createEntityTable() { return new EntityTable<>(ANOTHER_DEPRECABLE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final AnotherDeprecableEntityTable ANOTHER_DEPRECABLE_ENTITY = new AnotherDeprecableEntityTable();

}
