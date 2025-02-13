package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategorySqlView;
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
import java.util.Set;
import static tekgenesis.sales.basic.g.CategorySqlViewTable.CATEGORY_SQL_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategorySqlView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategorySqlViewBase
    extends EntityInstanceImpl<CategorySqlView,Integer>
    implements EntityInstance<CategorySqlView,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull String name = "";
    @NotNull String descr = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Descr. */
    @NotNull public String getDescr() { return this.descr; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CategorySqlView} instance. */
    @NotNull protected static CategorySqlView create(int idKey) {
        final CategorySqlView result = new CategorySqlView();
        ((CategorySqlViewBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    @NotNull private static EntityTable<CategorySqlView,Integer> myEntityTable() { return EntityTable.forTable(CATEGORY_SQL_VIEW); }

    @NotNull public EntityTable<CategorySqlView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategorySqlView,Integer> table() { return CATEGORY_SQL_VIEW; }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlView find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategorySqlView findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlView findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategorySqlView findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategorySqlView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategorySqlView findWhere(@NotNull Criteria... condition) { return selectFrom(CATEGORY_SQL_VIEW).where(condition).get(); }

    /** Create a selectFrom(CATEGORY_SQL_VIEW). */
    @NotNull public static Select<CategorySqlView> list() { return selectFrom(CATEGORY_SQL_VIEW); }

    /** Performs the given action for each CategorySqlView */
    public static void forEach(@Nullable Consumer<CategorySqlView> consumer) { selectFrom(CATEGORY_SQL_VIEW).forEach(consumer); }

    /** List instances of 'CategorySqlView' with the specified keys. */
    @NotNull public static ImmutableList<CategorySqlView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategorySqlView' with the specified keys. */
    @NotNull public static ImmutableList<CategorySqlView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategorySqlView' that verify the specified condition. */
    @NotNull public static Select<CategorySqlView> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_SQL_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategorySqlView> rowMapper() { return CATEGORY_SQL_VIEW.metadata().getRowMapper(); }

}
