package tekgenesis.showcase.g;

import tekgenesis.persistence.TableField.Bool;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.TableField.Date;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.showcase.Options;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.TextShowcase;
import tekgenesis.showcase.TextShowcaseSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TextShowcase */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TextShowcaseTable
    extends DbTable<TextShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str TXT;
    @NotNull public final Date DATE;
    @NotNull public final Bool BOOL;
    @NotNull public final Enum<Options,String> OPTION;
    @NotNull public final Str ENTITY_NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TextShowcaseTable() {
        super(TextShowcase.class,"SHOWCASE","TEXT_SHOWCASE","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        TXT = strField("txt", "TXT", 20);
        DATE = dateField("date", "DATE");
        BOOL = boolField("bool", "BOOL");
        OPTION = enumField("option", "OPTION", Options.class);
        ENTITY_NAME = strField("entityName", "ENTITY_NAME", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<TextShowcaseSearcher> searcher() {
        return Option.of(TextShowcaseSearcher.TEXT_SHOWCASE_SEARCHER);
    }

    @Override @NotNull public final TextShowcaseTable as(@NotNull String alias) { return createAlias(new TextShowcaseTable(), alias); }

    @Override @NotNull protected final EntityTable<TextShowcase,Integer> createEntityTable() { return new EntityTable<>(TEXT_SHOWCASE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TextShowcaseTable TEXT_SHOWCASE = new TextShowcaseTable();

}
