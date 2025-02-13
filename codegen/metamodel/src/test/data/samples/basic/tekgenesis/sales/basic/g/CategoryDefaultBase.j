package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryDefault;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.DeprecableInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntitySeq;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.ProductDefault;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CategoryDefaultTable.CATEGORY_DEFAULT;
import static tekgenesis.sales.basic.g.ProductDefaultTable.PRODUCT_DEFAULT;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategoryDefault.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryDefaultBase
    extends EntityInstanceImpl<CategoryDefault,Integer>
    implements PersistableInstance<CategoryDefault,Integer>, DeprecableInstance<CategoryDefault,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String descr = "";
    @NotNull private EntitySeq<ProductDefault> products = createEntitySeq(PRODUCT_DEFAULT, (CategoryDefault) this, c -> ((ProductDefaultBase)c).mainCategory, listOf(PRODUCT_DEFAULT.MAIN_CATEGORY_ID));
    @Nullable Integer parentId = null;
    @NotNull EntityRef<CategoryDefault,Integer> parent = new EntityRef<>(CATEGORY_DEFAULT);
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @Nullable DateTime deprecationTime = null;
    @Nullable String deprecationUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public CategoryDefault setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (CategoryDefault) this;
    }

    /** Returns the Descr. */
    @NotNull public String getDescr() { return this.descr; }

    /** Sets the value of the Descr. */
    @NotNull public CategoryDefault setDescr(@NotNull String descr) {
        markAsModified();
        this.descr = Strings.truncate(descr, 120);
        return (CategoryDefault) this;
    }

    /** Returns the Products. */
    @NotNull public EntitySeq<ProductDefault> getProducts() { return products; }

    /** Returns the Parent Id. */
    @Nullable public Integer getParentId() { return this.parentId; }

    /** Returns the Parent. */
    @Nullable public CategoryDefault getParent() { return parent.solve(this.parentId); }

    /** Sets the value of the Parent Id. */
    @NotNull public CategoryDefault setParentId(@Nullable Integer parentId) {
        parent.invalidate();
        this.parentId = parentId;
        return (CategoryDefault) this;
    }

    /** Sets the value of the Parent. */
    @SuppressWarnings("AssignmentToNull") @NotNull public CategoryDefault setParent(@Nullable CategoryDefault parent) {
        this.parent.set(parent);
        if (parent == null) {
        	this.parentId = null;
        }
        else {
        	this.parentId = parent.getId();
        }
        return (CategoryDefault) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Deprecation Time. */
    @Nullable public DateTime getDeprecationTime() { return this.deprecationTime; }

    /** Returns the Deprecation User. */
    @Nullable public String getDeprecationUser() { return this.deprecationUser; }

    /** Creates a new {@link CategoryDefault} instance. */
    @NotNull public static CategoryDefault create() { return new CategoryDefault(); }

    @NotNull private static EntityTable<CategoryDefault,Integer> myEntityTable() { return EntityTable.forTable(CATEGORY_DEFAULT); }

    @NotNull public EntityTable<CategoryDefault,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategoryDefault,Integer> table() { return CATEGORY_DEFAULT; }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefault find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryDefault findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefault findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryDefault findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefault find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategoryDefault' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryDefault findWhere(@NotNull Criteria... condition) { return selectFrom(CATEGORY_DEFAULT).where(condition).get(); }

    /** Create a selectFrom(CATEGORY_DEFAULT). */
    @NotNull public static Select<CategoryDefault> list() { return selectFrom(CATEGORY_DEFAULT); }

    /** Performs the given action for each CategoryDefault */
    public static void forEach(@Nullable Consumer<CategoryDefault> consumer) { selectFrom(CATEGORY_DEFAULT).forEach(consumer); }

    /** List instances of 'CategoryDefault' with the specified keys. */
    @NotNull public static ImmutableList<CategoryDefault> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategoryDefault' with the specified keys. */
    @NotNull public static ImmutableList<CategoryDefault> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategoryDefault' that verify the specified condition. */
    @NotNull public static Select<CategoryDefault> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_DEFAULT).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((CategoryDefault) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CategoryDefault> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CategoryDefault> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** List the instances of 'CategoryDefault' that matches the given parameters. */
    @NotNull public static ImmutableList<CategoryDefault> listByDeprecationTime(@NotNull DateTime deprecationTime) {
        return selectFrom(CATEGORY_DEFAULT).where(CATEGORY_DEFAULT.DEPRECATION_TIME.eq(deprecationTime)).list();
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescr()); }

    @Override @NotNull public String toString() { return "" + getDescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategoryDefault> rowMapper() { return CATEGORY_DEFAULT.metadata().getRowMapper(); }

    @Override public void invalidate() {
        parent.invalidate();
        products.invalidate();
    }

}
