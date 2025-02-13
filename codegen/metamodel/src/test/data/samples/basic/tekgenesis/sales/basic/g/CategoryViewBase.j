package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryView;
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
import static tekgenesis.sales.basic.g.CategoryViewTable.CATEGORY_VIEW;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategoryView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryViewBase
    extends EntityInstanceImpl<CategoryView,Long>
    implements EntityInstance<CategoryView,Long>
{

    //~ Fields ...................................................................................................................

    long vid = 0;
    @NotNull String vname = "";
    @NotNull String vdescr = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Vid. */
    public long getVid() { return this.vid; }

    /** Returns the Vname. */
    @NotNull public String getVname() { return this.vname; }

    /** Returns the Vdescr. */
    @NotNull public String getVdescr() { return this.vdescr; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CategoryView} instance. */
    @NotNull protected static CategoryView create(long vid) {
        final CategoryView result = new CategoryView();
        ((CategoryViewBase) result).vid = Integers.checkSignedLength("vid", vid, false, 18);
        return result;
    }

    @NotNull private static EntityTable<CategoryView,Long> myEntityTable() { return EntityTable.forTable(CATEGORY_VIEW); }

    @NotNull public EntityTable<CategoryView,Long> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategoryView,Long> table() { return CATEGORY_VIEW; }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryView find(long vid) { return myEntityTable().find(vid); }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryView findOrFail(long vid) { return myEntityTable().findOrFail(vid); }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryView findPersisted(long vid) { return myEntityTable().findPersisted(vid); }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryView findPersistedOrFail(long vid) { return myEntityTable().findPersistedOrFail(vid); }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategoryView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryView findWhere(@NotNull Criteria... condition) { return selectFrom(CATEGORY_VIEW).where(condition).get(); }

    /** Create a selectFrom(CATEGORY_VIEW). */
    @NotNull public static Select<CategoryView> list() { return selectFrom(CATEGORY_VIEW); }

    /** Performs the given action for each CategoryView */
    public static void forEach(@Nullable Consumer<CategoryView> consumer) { selectFrom(CATEGORY_VIEW).forEach(consumer); }

    /** List instances of 'CategoryView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryView> list(@Nullable Set<Long> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategoryView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategoryView' that verify the specified condition. */
    @NotNull public static Select<CategoryView> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(vid); }

    @NotNull public Long keyObject() { return vid; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + getVdescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategoryView> rowMapper() { return CATEGORY_VIEW.metadata().getRowMapper(); }

}
