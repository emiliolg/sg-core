package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.TextFieldShowcase;
import tekgenesis.showcase.TextFieldShowcaseSearcher;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TextFieldShowcase */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TextFieldShowcaseTable
    extends DbTable<TextFieldShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str F1;
    @NotNull public final Str F2;
    @NotNull public final Str F3;
    @NotNull public final Str F4;
    @NotNull public final Str PATENTE;
    @NotNull public final Decimal MONEY;
    @NotNull public final Decimal T1;
    @NotNull public final Decimal T2;
    @NotNull public final Decimal T3;
    @NotNull public final Decimal T4;
    @NotNull public final Int A1;
    @NotNull public final Int A2;
    @NotNull public final Int A3;
    @NotNull public final Int A4;
    @NotNull public final Str HTML;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TextFieldShowcaseTable() {
        super(TextFieldShowcase.class,"SHOWCASE","TEXT_FIELD_SHOWCASE","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        F1 = strField("f1", "F1", 50);
        F2 = strField("f2", "F2", 50);
        F3 = strField("f3", "F3", 50);
        F4 = strField("f4", "F4", 50);
        PATENTE = strField("patente", "PATENTE", 6);
        MONEY = decimalField("money", "MONEY", false, 10, 2);
        T1 = decimalField("t1", "T1", false, 4, 2);
        T2 = decimalField("t2", "T2", false, 4, 2);
        T3 = decimalField("t3", "T3", false, 4, 2);
        T4 = decimalField("t4", "T4", false, 4, 2);
        A1 = intField("a1", "A1", false, 9);
        A2 = intField("a2", "A2", false, 9);
        A3 = intField("a3", "A3", false, 9);
        A4 = intField("a4", "A4", false, 9);
        HTML = strField("html", "HTML", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<TextFieldShowcaseSearcher> searcher() {
        return Option.of(TextFieldShowcaseSearcher.TEXT_FIELD_SHOWCASE_SEARCHER);
    }

    @Override @NotNull public final TextFieldShowcaseTable as(@NotNull String alias) { return createAlias(new TextFieldShowcaseTable(), alias); }

    @Override @NotNull protected final EntityTable<TextFieldShowcase,Integer> createEntityTable() { return new EntityTable<>(TEXT_FIELD_SHOWCASE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TextFieldShowcaseTable TEXT_FIELD_SHOWCASE = new TextFieldShowcaseTable();

}
