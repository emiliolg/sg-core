package tekgenesis.sales.cart.g;

import java.math.BigDecimal;
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
import tekgenesis.sales.cart.ProductlView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import static tekgenesis.sales.cart.g.ProductlViewTable.PRODUCTL_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductlView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductlViewBase
    extends EntityInstanceImpl<ProductlView,Integer>
    implements EntityInstance<ProductlView,Integer>
{

    //~ Fields ...................................................................................................................

    int id = 0;
    @NotNull String model = "";
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    /** Returns the Model. */
    @NotNull public String getModel() { return this.model; }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductlView} instance. */
    @NotNull protected static ProductlView create(int id) {
        final ProductlView result = new ProductlView();
        ((ProductlViewBase) result).id = Integers.checkSignedLength("id", id, false, 9);
        return result;
    }

    @NotNull private static EntityTable<ProductlView,Integer> myEntityTable() { return EntityTable.forTable(PRODUCTL_VIEW); }

    @NotNull public EntityTable<ProductlView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductlView,Integer> table() { return PRODUCTL_VIEW; }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductlView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductlView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductlView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductlView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductlView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductlView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductlView findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCTL_VIEW).where(condition).get(); }

    /** Create a selectFrom(PRODUCTL_VIEW). */
    @NotNull public static Select<ProductlView> list() { return selectFrom(PRODUCTL_VIEW); }

    /** Performs the given action for each ProductlView */
    public static void forEach(@Nullable Consumer<ProductlView> consumer) { selectFrom(PRODUCTL_VIEW).forEach(consumer); }

    /** List instances of 'ProductlView' with the specified keys. */
    @NotNull public static ImmutableList<ProductlView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductlView' with the specified keys. */
    @NotNull public static ImmutableList<ProductlView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductlView' that verify the specified condition. */
    @NotNull public static Select<ProductlView> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCTL_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductlView> rowMapper() { return PRODUCTL_VIEW.metadata().getRowMapper(); }

}
