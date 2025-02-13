package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.TableField.Date;
import tekgenesis.showcase.DateEntity;
import tekgenesis.showcase.DateEntitySearcher;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DateEntity */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DateEntityTable
    extends DbTable<DateEntity,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Date DATE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private DateEntityTable() {
        super(DateEntity.class,"SHOWCASE","DATE_ENTITY","DATE_ENTITY_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        DATE = dateField("date", "DATE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<DateEntitySearcher> searcher() { return Option.of(DateEntitySearcher.DATE_ENTITY_SEARCHER); }

    @Override @NotNull public final DateEntityTable as(@NotNull String alias) { return createAlias(new DateEntityTable(), alias); }

    @Override @NotNull protected final EntityTable<DateEntity,Integer> createEntityTable() { return new EntityTable<>(DATE_ENTITY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DateEntityTable DATE_ENTITY = new DateEntityTable();

}
