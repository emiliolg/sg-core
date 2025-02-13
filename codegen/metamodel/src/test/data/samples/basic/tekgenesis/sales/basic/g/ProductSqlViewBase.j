package tekgenesis.sales.basic.g;

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
import tekgenesis.sales.basic.ProductSqlView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import static tekgenesis.sales.basic.g.ProductSqlViewTable.PRODUCT_SQL_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductSqlView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductSqlViewBase
    extends EntityInstanceImpl<ProductSqlView,Integer>
    implements EntityInstance<ProductSqlView,Integer>
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

    /** Creates a new {@link ProductSqlView} instance. */
    @NotNull protected static ProductSqlView create(int id) {
        final ProductSqlView result = new ProductSqlView();
        ((ProductSqlViewBase) result).id = Integers.checkSignedLength("id", id, false, 9);
        return result;
    }

    @NotNull private static EntityTable<ProductSqlView,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_SQL_VIEW); }

    @NotNull public EntityTable<ProductSqlView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductSqlView,Integer> table() { return PRODUCT_SQL_VIEW; }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductSqlView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductSqlView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductSqlView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductSqlView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductSqlView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductSqlView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductSqlView findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT_SQL_VIEW).where(condition).get(); }

    /** Create a selectFrom(PRODUCT_SQL_VIEW). */
    @NotNull public static Select<ProductSqlView> list() { return selectFrom(PRODUCT_SQL_VIEW); }

    /** Performs the given action for each ProductSqlView */
    public static void forEach(@Nullable Consumer<ProductSqlView> consumer) { selectFrom(PRODUCT_SQL_VIEW).forEach(consumer); }

    /** List instances of 'ProductSqlView' with the specified keys. */
    @NotNull public static ImmutableList<ProductSqlView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductSqlView' with the specified keys. */
    @NotNull public static ImmutableList<ProductSqlView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductSqlView' that verify the specified condition. */
    @NotNull public static Select<ProductSqlView> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_SQL_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductSqlView> rowMapper() { return PRODUCT_SQL_VIEW.metadata().getRowMapper(); }

}
