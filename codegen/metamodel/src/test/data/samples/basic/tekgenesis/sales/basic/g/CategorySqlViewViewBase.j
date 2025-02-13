package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategorySqlViewView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import static tekgenesis.sales.basic.g.CategorySqlViewViewTable.CATEGORY_SQL_VIEW_VIEW;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategorySqlViewView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategorySqlViewViewBase
    extends EntityInstanceImpl<CategorySqlViewView,Integer>
    implements EntityInstance<CategorySqlViewView,Integer>
{

    //~ Fields ...................................................................................................................

    int vid = 0;
    @NotNull String vname = "";
    @NotNull String vdescr = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Vid. */
    public int getVid() { return this.vid; }

    /** Returns the Vname. */
    @NotNull public String getVname() { return this.vname; }

    /** Returns the Vdescr. */
    @NotNull public String getVdescr() { return this.vdescr; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CategorySqlViewView} instance. */
    @NotNull protected static CategorySqlViewView create(int vid) {
        final CategorySqlViewView result = new CategorySqlViewView();
        ((CategorySqlViewViewBase) result).vid = Integers.checkSignedLength("vid", vid, false, 9);
        return result;
    }

    @NotNull private static EntityTable<CategorySqlViewView,Integer> myEntityTable() { return EntityTable.forTable(CATEGORY_SQL_VIEW_VIEW); }

    @NotNull public EntityTable<CategorySqlViewView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategorySqlViewView,Integer> table() { return CATEGORY_SQL_VIEW_VIEW; }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlViewView find(int vid) { return myEntityTable().find(vid); }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategorySqlViewView findOrFail(int vid) { return myEntityTable().findOrFail(vid); }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlViewView findPersisted(int vid) { return myEntityTable().findPersisted(vid); }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategorySqlViewView findPersistedOrFail(int vid) { return myEntityTable().findPersistedOrFail(vid); }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlViewView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategorySqlViewView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlViewView findWhere(@NotNull Criteria... condition) {
        return selectFrom(CATEGORY_SQL_VIEW_VIEW).where(condition).get();
    }

    /** Create a selectFrom(CATEGORY_SQL_VIEW_VIEW). */
    @NotNull public static Select<CategorySqlViewView> list() { return selectFrom(CATEGORY_SQL_VIEW_VIEW); }

    /** Performs the given action for each CategorySqlViewView */
    public static void forEach(@Nullable Consumer<CategorySqlViewView> consumer) { selectFrom(CATEGORY_SQL_VIEW_VIEW).forEach(consumer); }

    /** List instances of 'CategorySqlViewView' with the specified keys. */
    @NotNull public static ImmutableList<CategorySqlViewView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategorySqlViewView' with the specified keys. */
    @NotNull public static ImmutableList<CategorySqlViewView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategorySqlViewView' that verify the specified condition. */
    @NotNull public static Select<CategorySqlViewView> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_SQL_VIEW_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(vid); }

    @NotNull public Integer keyObject() { return vid; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + getVdescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategorySqlViewView> rowMapper() { return CATEGORY_SQL_VIEW_VIEW.metadata().getRowMapper(); }

}
