package tekgenesis.sales.basic.g;

import tekgenesis.metadata.authorization.Assignee;
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
import tekgenesis.persistence.Initialize;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductData;
import tekgenesis.sales.basic.ProductDataWorkItem;
import tekgenesis.metadata.authorization.RoleAssignment;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.io.Serializable;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.metadata.authorization.User;
import tekgenesis.workflow.WorkItemInstance;
import tekgenesis.notification.push.workflow.WorkItemNotifier;
import tekgenesis.workflow.WorkItemPriority;
import static tekgenesis.sales.basic.g.ProductDataTable.PRODUCT_DATA;
import static tekgenesis.sales.basic.g.ProductDataWorkItemTable.PRODUCT_DATA_WORK_ITEM;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductDataWorkItem.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDataWorkItemBase
    extends EntityInstanceImpl<ProductDataWorkItem,Integer>
    implements PersistableInstance<ProductDataWorkItem,Integer>, Serializable, WorkItemInstance<ProductDataWorkItem,Integer,ProductData,Integer,Product,String>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String task = "";
    int parentCaseId = 0;
    @NotNull EntityRef<ProductData,Integer> parentCase = new EntityRef<>(PRODUCT_DATA);
    @NotNull DateTime creation = DateTime.EPOCH;
    @NotNull String assignee = "";
    @Nullable String reporter = null;
    @Nullable String ouName = null;
    boolean closed = false;
    @NotNull String description = "";
    @Nullable String title = null;
    @Nullable Integer priorityCode = 3;
    @Nullable String businessKey = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** List matching work items for given user and organization. */
    @NotNull public static Select<ProductDataWorkItem> listByAssignees(@NotNull User user, @NotNull OrganizationalUnit orgUnit, @NotNull Set<String> assignees) {
        Criteria condition = PRODUCT_DATA_WORK_ITEM.OU_NAME.isNull().and(PRODUCT_DATA_WORK_ITEM.ASSIGNEE.in(assignees));
        for (final RoleAssignment assignment : user.getRoleAssignmentsForOrganization(orgUnit)) {
        	condition = condition.or(PRODUCT_DATA_WORK_ITEM.OU_NAME.in(assignment.getOu().getHierarchy()).and(PRODUCT_DATA_WORK_ITEM.ASSIGNEE.eq(user.asString()).or(PRODUCT_DATA_WORK_ITEM.ASSIGNEE.eq(assignment.getRole().asString()))));
        }
        return selectFrom(PRODUCT_DATA_WORK_ITEM).where(PRODUCT_DATA_WORK_ITEM.CLOSED.eq(false), condition);
    }

    public void setAssignee(@NotNull Assignee assignee) { setAssignee(assignee.asString()); }

    public void setOrganizationalUnit(@NotNull OrganizationalUnit orgUnit) { setOuName(orgUnit.getName()); }

    @NotNull public WorkItemPriority getPriority() { return WorkItemPriority.find(this.priorityCode); }

    @Initialize public static void initialize() {
        addListener(EntityListenerType.AFTER_PERSIST, w -> { WorkItemNotifier.notify(w, w.getId(), w.getUpdateTime()); return true; });
    }

    /** Returns the Task. */
    @NotNull public String getTask() { return this.task; }

    /** Sets the value of the Task. */
    @NotNull public ProductDataWorkItem setTask(@NotNull String task) {
        markAsModified();
        this.task = Strings.truncate(task, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Parent Case Id. */
    public int getParentCaseId() { return this.parentCaseId; }

    /** Returns the Parent Case. */
    @NotNull public ProductData getParentCase() { return parentCase.solveOrFail(this.parentCaseId); }

    /** Sets the value of the Parent Case Id. */
    @NotNull public ProductDataWorkItem setParentCaseId(int parentCaseId) {
        parentCase.invalidate();
        this.parentCaseId = parentCaseId;
        return (ProductDataWorkItem) this;
    }

    /** Sets the value of the Parent Case. */
    @NotNull public ProductDataWorkItem setParentCase(@NotNull ProductData parentCase) {
        this.parentCase.set(parentCase);
        this.parentCaseId = parentCase.getId();
        return (ProductDataWorkItem) this;
    }

    /** Returns the Creation. */
    @NotNull public DateTime getCreation() { return this.creation; }

    /** Sets the value of the Creation. */
    @NotNull public ProductDataWorkItem setCreation(@NotNull DateTime creation) {
        markAsModified();
        this.creation = creation;
        return (ProductDataWorkItem) this;
    }

    /** Returns the Assignee. */
    @NotNull public String getAssignee() { return this.assignee; }

    /** Sets the value of the Assignee. */
    @NotNull public ProductDataWorkItem setAssignee(@NotNull String assignee) {
        markAsModified();
        this.assignee = Strings.truncate(assignee, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Reporter. */
    @Nullable public String getReporter() { return this.reporter; }

    /** Sets the value of the Reporter. */
    @NotNull public ProductDataWorkItem setReporter(@Nullable String reporter) {
        markAsModified();
        this.reporter = Strings.truncate(reporter, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Ou Name. */
    @Nullable public String getOuName() { return this.ouName; }

    /** Sets the value of the Ou Name. */
    @NotNull public ProductDataWorkItem setOuName(@Nullable String ouName) {
        markAsModified();
        this.ouName = Strings.truncate(ouName, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns true if it is Closed. */
    public boolean isClosed() { return this.closed; }

    /** Sets the value of the Closed. */
    @NotNull public ProductDataWorkItem setClosed(boolean closed) {
        markAsModified();
        this.closed = closed;
        return (ProductDataWorkItem) this;
    }

    /** Returns the Description. */
    @NotNull public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public ProductDataWorkItem setDescription(@NotNull String description) {
        markAsModified();
        this.description = Strings.truncate(description, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Title. */
    @Nullable public String getTitle() { return this.title; }

    /** Sets the value of the Title. */
    @NotNull public ProductDataWorkItem setTitle(@Nullable String title) {
        markAsModified();
        this.title = Strings.truncate(title, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Priority Code. */
    @Nullable public Integer getPriorityCode() { return this.priorityCode; }

    /** Sets the value of the Priority Code. */
    @NotNull public ProductDataWorkItem setPriorityCode(@Nullable Integer priorityCode) {
        markAsModified();
        this.priorityCode = Integers.checkSignedLength("priorityCode", priorityCode, false, 9);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Business Key. */
    @Nullable public String getBusinessKey() { return this.businessKey; }

    /** Sets the value of the Business Key. */
    @NotNull public ProductDataWorkItem setBusinessKey(@Nullable String businessKey) {
        markAsModified();
        this.businessKey = Strings.truncate(businessKey, 256);
        return (ProductDataWorkItem) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductDataWorkItem} instance. */
    @NotNull public static ProductDataWorkItem create() { return new ProductDataWorkItem(); }

    @NotNull private static EntityTable<ProductDataWorkItem,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DATA_WORK_ITEM); }

    @NotNull public EntityTable<ProductDataWorkItem,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductDataWorkItem,Integer> table() { return PRODUCT_DATA_WORK_ITEM; }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDataWorkItem find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDataWorkItem findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDataWorkItem findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDataWorkItem findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDataWorkItem find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductDataWorkItem' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDataWorkItem findWhere(@NotNull Criteria... condition) {
        return selectFrom(PRODUCT_DATA_WORK_ITEM).where(condition).get();
    }

    /** Create a selectFrom(PRODUCT_DATA_WORK_ITEM). */
    @NotNull public static Select<ProductDataWorkItem> list() { return selectFrom(PRODUCT_DATA_WORK_ITEM); }

    /** Performs the given action for each ProductDataWorkItem */
    public static void forEach(@Nullable Consumer<ProductDataWorkItem> consumer) { selectFrom(PRODUCT_DATA_WORK_ITEM).forEach(consumer); }

    /** List instances of 'ProductDataWorkItem' with the specified keys. */
    @NotNull public static ImmutableList<ProductDataWorkItem> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductDataWorkItem' with the specified keys. */
    @NotNull public static ImmutableList<ProductDataWorkItem> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductDataWorkItem' that verify the specified condition. */
    @NotNull public static Select<ProductDataWorkItem> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_DATA_WORK_ITEM).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((ProductDataWorkItem) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDataWorkItem> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDataWorkItem> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** List the instances of 'ProductDataWorkItem' that matches the given parameters. */
    @NotNull public static ImmutableList<ProductDataWorkItem> listByClosed(boolean closed) {
        return selectFrom(PRODUCT_DATA_WORK_ITEM).where(PRODUCT_DATA_WORK_ITEM.CLOSED.eq(closed)).list();
    }

    /** List the instances of 'ProductDataWorkItem' that matches the given parameters. */
    @NotNull public static ImmutableList<ProductDataWorkItem> listByUpdateTime(@NotNull DateTime updateTime) {
        return selectFrom(PRODUCT_DATA_WORK_ITEM).where(PRODUCT_DATA_WORK_ITEM.UPDATE_TIME.eq(updateTime)).list();
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescription()); }

    @Override @NotNull public String toString() { return "" + getDescription(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductDataWorkItem> rowMapper() { return PRODUCT_DATA_WORK_ITEM.metadata().getRowMapper(); }

    @Override public void invalidate() { parentCase.invalidate(); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 7528205897507709987L;

}
