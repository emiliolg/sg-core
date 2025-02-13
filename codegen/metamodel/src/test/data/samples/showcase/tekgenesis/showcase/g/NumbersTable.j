package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.Numbers;
import tekgenesis.showcase.NumbersSearcher;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Real;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Numbers */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class NumbersTable
    extends DbTable<Numbers,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Int UNSIGNED_INT4;
    @NotNull public final Int SIGNED_INT5;
    @NotNull public final Int SIGNED_TO_BE_UNSIGNED;
    @NotNull public final Int UNSIGNED_INTEGER;
    @NotNull public final Int SIGNED_INTEGER;
    @NotNull public final Decimal UNSIGNED_DECIMAL52;
    @NotNull public final Decimal SIGNED_DECIMAL52;
    @NotNull public final Real UNSIGNED_REAL;
    @NotNull public final Real SIGNED_REAL;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private NumbersTable() {
        super(Numbers.class,"SHOWCASE","NUMBERS","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        NAME = strField("name", "NAME", 255);
        UNSIGNED_INT4 = intField("unsignedInt4", "UNSIGNED_INT4", false, 4);
        SIGNED_INT5 = intField("signedInt5", "SIGNED_INT5", true, 5);
        SIGNED_TO_BE_UNSIGNED = intField("signedToBeUnsigned", "SIGNED_TO_BE_UNSIGNED", true, 9);
        UNSIGNED_INTEGER = intField("unsignedInteger", "UNSIGNED_INTEGER", false, 8);
        SIGNED_INTEGER = intField("signedInteger", "SIGNED_INTEGER", true, 9);
        UNSIGNED_DECIMAL52 = decimalField("unsignedDecimal52", "UNSIGNED_DECIMAL52", false, 5, 2);
        SIGNED_DECIMAL52 = decimalField("signedDecimal52", "SIGNED_DECIMAL52", true, 5, 2);
        UNSIGNED_REAL = realField("unsignedReal", "UNSIGNED_REAL", false);
        SIGNED_REAL = realField("signedReal", "SIGNED_REAL", true);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(NAME));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<NumbersSearcher> searcher() { return Option.of(NumbersSearcher.NUMBERS_SEARCHER); }

    @Override @NotNull public final NumbersTable as(@NotNull String alias) { return createAlias(new NumbersTable(), alias); }

    @Override @NotNull protected final EntityTable<Numbers,String> createEntityTable() { return new EntityTable<>(NUMBERS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final NumbersTable NUMBERS = new NumbersTable();

}
