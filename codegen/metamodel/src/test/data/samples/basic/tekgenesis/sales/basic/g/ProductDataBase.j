package tekgenesis.sales.basic.g;

import tekgenesis.workflow.CaseInstance;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductData;
import tekgenesis.sales.basic.ProductDataWorkItem;
import tekgenesis.sales.basic.ProductDescription;
import tekgenesis.sales.basic.ProductImages;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.io.Serializable;
import java.util.Set;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.sales.basic.g.ProductDataTable.PRODUCT_DATA;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductData.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDataBase
    extends EntityInstanceImpl<ProductData,Integer>
    implements PersistableInstance<ProductData,Integer>, Serializable, CaseInstance<ProductData,Integer,Product,String,ProductDataWorkItem,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String entityProductId = "";
    @NotNull EntityRef<Product,String> entity = new EntityRef<>(PRODUCT);
    @NotNull DateTime creation = DateTime.EPOCH;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Task to create a work item with {@link ProductDescription} as associated form. */
    @NotNull public ProductDataWorkItem details() {
        final ProductDataWorkItem item = new ProductDataWorkItem();
        item.setTask("details");
        item.setParentCaseId(getId());
        item.setCreation(DateTime.current());
        return item;
    }

    /** Task to create a work item with {@link ProductImages} as associated form. */
    @NotNull public ProductDataWorkItem images() {
        final ProductDataWorkItem item = new ProductDataWorkItem();
        item.setTask("images");
        item.setParentCaseId(getId());
        item.setCreation(DateTime.current());
        return item;
    }

    public void process(@NotNull ProductDataWorkItem item, @NotNull String result) { }

    /** Returns the Entity Product Id. */
    @NotNull public String getEntityProductId() { return this.entityProductId; }

    /** Returns the Entity. */
    @NotNull public Product getEntity() { return entity.solveOrFail(this.entityProductId); }

    /** Sets the value of the Entity Product Id. */
    @NotNull public ProductData setEntityProductId(@NotNull String entityProductId) {
        entity.invalidate();
        this.entityProductId = entityProductId;
        return (ProductData) this;
    }

    /** Sets the value of the Entity. */
    @NotNull public ProductData setEntity(@NotNull Product entity) {
        this.entity.set(entity);
        this.entityProductId = entity.getProductId();
        return (ProductData) this;
    }

    /** Returns the Creation. */
    @NotNull public DateTime getCreation() { return this.creation; }

    /** Sets the value of the Creation. */
    @NotNull public ProductData setCreation(@NotNull DateTime creation) {
        markAsModified();
        this.creation = creation;
        return (ProductData) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductData} instance. */
    @NotNull public static ProductData create() { return new ProductData(); }

    /** Creates a new {@link ProductData} instance. */
    @NotNull public static ProductData create(@NotNull Product product) {
        final ProductData result = new ProductData();
        result.setEntity(product);
        result.setCreation(DateTime.current());
        return result;
    }

    @NotNull private static EntityTable<ProductData,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DATA); }

    @NotNull public EntityTable<ProductData,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductData,Integer> table() { return PRODUCT_DATA; }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductData find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductData findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductData findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductData findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductData find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductData' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductData findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT_DATA).where(condition).get(); }

    /** Create a selectFrom(PRODUCT_DATA). */
    @NotNull public static Select<ProductData> list() { return selectFrom(PRODUCT_DATA); }

    /** Performs the given action for each ProductData */
    public static void forEach(@Nullable Consumer<ProductData> consumer) { selectFrom(PRODUCT_DATA).forEach(consumer); }

    /** List instances of 'ProductData' with the specified keys. */
    @NotNull public static ImmutableList<ProductData> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductData' with the specified keys. */
    @NotNull public static ImmutableList<ProductData> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductData' that verify the specified condition. */
    @NotNull public static Select<ProductData> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_DATA).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((ProductData) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductData> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductData> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getEntity()); }

    @Override @NotNull public String toString() { return "" + getEntity(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductData> rowMapper() { return PRODUCT_DATA.metadata().getRowMapper(); }

    @Override public void invalidate() { entity.invalidate(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 241086860477884613L;

}
