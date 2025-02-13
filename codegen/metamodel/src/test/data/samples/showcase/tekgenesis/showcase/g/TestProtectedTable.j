package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.TestProtected;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.TestProtected */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class TestProtectedTable
    extends DbTable<TestProtected,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str BLA;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private TestProtectedTable() {
        super(TestProtected.class,"SHOWCASE","TEST_PROTECTED","TEST_PROTECTED_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 255);
        BLA = strField("bla", "BLA", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final TestProtectedTable as(@NotNull String alias) { return createAlias(new TestProtectedTable(), alias); }

    @Override @NotNull protected final EntityTable<TestProtected,Integer> createEntityTable() { return new EntityTable<>(TEST_PROTECTED); }

    //~ Fields ...................................................................................................................

    @NotNull public static final TestProtectedTable TEST_PROTECTED = new TestProtectedTable();

}
