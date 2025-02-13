package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherSimpleEntity;
import tekgenesis.showcase.AnotherSimpleEntitySearcher;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.EnumerationSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.showcase.Options;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.AnotherSimpleEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class AnotherSimpleEntityTable
    extends DbTable<AnotherSimpleEntity,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final EnumerationSet<Options,String> OPTIONS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private AnotherSimpleEntityTable() {
        super(AnotherSimpleEntity.class,"SHOWCASE","ANOTHER_SIMPLE_ENTITY","ANOTHER_SIMPLE_ENTITY_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 30);
        OPTIONS = enumSetField("options", "OPTIONS", Options.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<AnotherSimpleEntitySearcher> searcher() {
        return Option.of(AnotherSimpleEntitySearcher.ANOTHER_SIMPLE_ENTITY_SEARCHER);
    }

    @Override @NotNull public final AnotherSimpleEntityTable as(@NotNull String alias) { return createAlias(new AnotherSimpleEntityTable(), alias); }

    @Override @NotNull protected final EntityTable<AnotherSimpleEntity,Integer> createEntityTable() { return new EntityTable<>(ANOTHER_SIMPLE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final AnotherSimpleEntityTable ANOTHER_SIMPLE_ENTITY = new AnotherSimpleEntityTable();

}
