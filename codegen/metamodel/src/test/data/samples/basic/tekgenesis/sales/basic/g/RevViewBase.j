package tekgenesis.sales.basic.g;

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
import tekgenesis.sales.basic.ProductDefaultView;
import tekgenesis.sales.basic.RevView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import static tekgenesis.sales.basic.g.ProductDefaultViewTable.PRODUCT_DEFAULT_VIEW;
import static tekgenesis.sales.basic.g.RevViewTable.REV_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: RevView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class RevViewBase
    extends EntityInstanceImpl<RevView,Integer>
    implements EntityInstance<RevView,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String rev = "";
    int prodId = 0;
    @NotNull EntityRef<ProductDefaultView,Integer> prod = new EntityRef<>(PRODUCT_DEFAULT_VIEW, ProductDefaultView::getRevs);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Rev. */
    @NotNull public String getRev() { return this.rev; }

    /** Returns the Prod Id. */
    public int getProdId() { return this.prodId; }

    /** Returns the Prod. */
    @NotNull public ProductDefaultView getProd() { return prod.solveOrFail(this.prodId); }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link RevView} instance. */
    @NotNull protected static RevView create() { return new RevView(); }

    @NotNull private static EntityTable<RevView,Integer> myEntityTable() { return EntityTable.forTable(REV_VIEW); }

    @NotNull public EntityTable<RevView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<RevView,Integer> table() { return REV_VIEW; }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static RevView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static RevView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'RevView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevView findWhere(@NotNull Criteria... condition) { return selectFrom(REV_VIEW).where(condition).get(); }

    /** Create a selectFrom(REV_VIEW). */
    @NotNull public static Select<RevView> list() { return selectFrom(REV_VIEW); }

    /** Performs the given action for each RevView */
    public static void forEach(@Nullable Consumer<RevView> consumer) { selectFrom(REV_VIEW).forEach(consumer); }

    /** List instances of 'RevView' with the specified keys. */
    @NotNull public static ImmutableList<RevView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'RevView' with the specified keys. */
    @NotNull public static ImmutableList<RevView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'RevView' that verify the specified condition. */
    @NotNull public static Select<RevView> listWhere(@NotNull Criteria condition) { return selectFrom(REV_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<RevView> rowMapper() { return REV_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { prod.invalidate(); }

}
