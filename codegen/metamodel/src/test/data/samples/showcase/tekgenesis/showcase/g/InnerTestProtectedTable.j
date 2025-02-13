package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.showcase.InnerTestProtected;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.InnerTestProtected */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class InnerTestProtectedTable
    extends DbTable<InnerTestProtected,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int TEST_PROTECTED_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str DESC;
    @NotNull public final Int LENGTH;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private InnerTestProtectedTable() {
        super(InnerTestProtected.class,"SHOWCASE","INNER_TEST_PROTECTED","",Modifier.NONE,CacheType.NONE);
        TEST_PROTECTED_ID = intField("testProtectedId", "TEST_PROTECTED_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        DESC = strField("desc", "DESC", 255);
        LENGTH = intField("length", "LENGTH", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(TEST_PROTECTED_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final InnerTestProtectedTable as(@NotNull String alias) { return createAlias(new InnerTestProtectedTable(), alias); }

    @Override @NotNull protected final EntityTable<InnerTestProtected,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(INNER_TEST_PROTECTED); }

    //~ Fields ...................................................................................................................

    @NotNull public static final InnerTestProtectedTable INNER_TEST_PROTECTED = new InnerTestProtectedTable();

}
