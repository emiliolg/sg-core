package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryDefaultView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import static tekgenesis.sales.basic.g.CategoryDefaultViewTable.CATEGORY_DEFAULT_VIEW;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategoryDefaultView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryDefaultViewBase
    extends EntityInstanceImpl<CategoryDefaultView,Integer>
    implements EntityInstance<CategoryDefaultView,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String vname = "";
    @NotNull String vdescr = "";
    @Nullable Integer vparentId = null;
    @NotNull EntityRef<CategoryDefaultView,Integer> vparent = new EntityRef<>(CATEGORY_DEFAULT_VIEW);
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @Nullable DateTime deprecationTime = null;
    @Nullable String deprecationUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Vname. */
    @NotNull public String getVname() { return this.vname; }

    /** Returns the Vdescr. */
    @NotNull public String getVdescr() { return this.vdescr; }

    /** Returns the Vparent Id. */
    @Nullable public Integer getVparentId() { return this.vparentId; }

    /** Returns the Vparent. */
    @Nullable public CategoryDefaultView getVparent() { return vparent.solve(this.vparentId); }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Deprecation Time. */
    @Nullable public DateTime getDeprecationTime() { return this.deprecationTime; }

    /** Returns the Deprecation User. */
    @Nullable public String getDeprecationUser() { return this.deprecationUser; }

    /** Creates a new {@link CategoryDefaultView} instance. */
    @NotNull protected static CategoryDefaultView create() { return new CategoryDefaultView(); }

    @NotNull private static EntityTable<CategoryDefaultView,Integer> myEntityTable() { return EntityTable.forTable(CATEGORY_DEFAULT_VIEW); }

    @NotNull public EntityTable<CategoryDefaultView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategoryDefaultView,Integer> table() { return CATEGORY_DEFAULT_VIEW; }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefaultView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryDefaultView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefaultView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryDefaultView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefaultView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategoryDefaultView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefaultView findWhere(@NotNull Criteria... condition) {
        return selectFrom(CATEGORY_DEFAULT_VIEW).where(condition).get();
    }

    /** Create a selectFrom(CATEGORY_DEFAULT_VIEW). */
    @NotNull public static Select<CategoryDefaultView> list() { return selectFrom(CATEGORY_DEFAULT_VIEW); }

    /** Performs the given action for each CategoryDefaultView */
    public static void forEach(@Nullable Consumer<CategoryDefaultView> consumer) { selectFrom(CATEGORY_DEFAULT_VIEW).forEach(consumer); }

    /** List instances of 'CategoryDefaultView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryDefaultView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategoryDefaultView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryDefaultView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategoryDefaultView' that verify the specified condition. */
    @NotNull public static Select<CategoryDefaultView> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_DEFAULT_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + getVdescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategoryDefaultView> rowMapper() { return CATEGORY_DEFAULT_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { vparent.invalidate(); }

}
