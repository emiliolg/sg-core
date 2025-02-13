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
import tekgenesis.showcase.SuggestedPerson;
import tekgenesis.showcase.SuggestedPersonSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.SuggestedPerson */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class SuggestedPersonTable
    extends DbTable<SuggestedPerson,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private SuggestedPersonTable() {
        super(SuggestedPerson.class,"SHOWCASE","SUGGESTED_PERSON","SUGGESTED_PERSON_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 30);
        LAST_NAME = strField("lastName", "LAST_NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<SuggestedPersonSearcher> searcher() {
        return Option.of(SuggestedPersonSearcher.SUGGESTED_PERSON_SEARCHER);
    }

    @Override @NotNull public final SuggestedPersonTable as(@NotNull String alias) { return createAlias(new SuggestedPersonTable(), alias); }

    @Override @NotNull protected final EntityTable<SuggestedPerson,Integer> createEntityTable() { return new EntityTable<>(SUGGESTED_PERSON); }

    //~ Fields ...................................................................................................................

    @NotNull public static final SuggestedPersonTable SUGGESTED_PERSON = new SuggestedPersonTable();

}
