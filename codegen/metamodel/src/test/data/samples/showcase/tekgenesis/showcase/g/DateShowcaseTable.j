package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.TableField.Date;
import tekgenesis.showcase.DateShowcase;
import tekgenesis.showcase.DateShowcaseSearcher;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DateShowcase */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DateShowcaseTable
    extends DbTable<DateShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Date DATE_FROM;
    @NotNull public final Date DATE_TO;
    @NotNull public final DTime TIME_FROM;
    @NotNull public final DTime TIME_TO;
    @NotNull public final Date DOUBLE_DATE_FROM;
    @NotNull public final Date DOUBLE_DATE_TO;
    @NotNull public final Date DATE_COMBO;
    @NotNull public final Date DATE_COMBO1;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private DateShowcaseTable() {
        super(DateShowcase.class,"SHOWCASE","DATE_SHOWCASE","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        DATE_FROM = dateField("dateFrom", "DATE_FROM");
        DATE_TO = dateField("dateTo", "DATE_TO");
        TIME_FROM = dTimeField("timeFrom", "TIME_FROM");
        TIME_TO = dTimeField("timeTo", "TIME_TO");
        DOUBLE_DATE_FROM = dateField("doubleDateFrom", "DOUBLE_DATE_FROM");
        DOUBLE_DATE_TO = dateField("doubleDateTo", "DOUBLE_DATE_TO");
        DATE_COMBO = dateField("dateCombo", "DATE_COMBO");
        DATE_COMBO1 = dateField("dateCombo1", "DATE_COMBO1");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<DateShowcaseSearcher> searcher() {
        return Option.of(DateShowcaseSearcher.DATE_SHOWCASE_SEARCHER);
    }

    @Override @NotNull public final DateShowcaseTable as(@NotNull String alias) { return createAlias(new DateShowcaseTable(), alias); }

    @Override @NotNull protected final EntityTable<DateShowcase,Integer> createEntityTable() { return new EntityTable<>(DATE_SHOWCASE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DateShowcaseTable DATE_SHOWCASE = new DateShowcaseTable();

}
