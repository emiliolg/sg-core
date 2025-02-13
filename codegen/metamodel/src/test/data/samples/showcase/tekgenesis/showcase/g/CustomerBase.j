package tekgenesis.showcase.g;

import tekgenesis.showcase.Address;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.showcase.Customer;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AddressTable.ADDRESS;
import static tekgenesis.showcase.g.CustomerTable.CUSTOMER;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Customer.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CustomerBase
    extends EntityInstanceImpl<Customer,Integer>
    implements PersistableInstance<Customer,Integer>
{

    //~ Fields ...................................................................................................................

    int document = 0;
    @NotNull String firstName = "";
    @NotNull String lastName = "";
    int homeAddressId = 0;
    @NotNull EntityRef<Address,Integer> homeAddress = new EntityRef<>(ADDRESS);
    @Nullable Integer workAddressId = null;
    @NotNull EntityRef<Address,Integer> workAddress = new EntityRef<>(ADDRESS);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Document. */
    public int getDocument() { return this.document; }

    /** Returns the First Name. */
    @NotNull public String getFirstName() { return this.firstName; }

    /** Sets the value of the First Name. */
    @NotNull public Customer setFirstName(@NotNull String firstName) {
        markAsModified();
        this.firstName = Strings.truncate(firstName, 255);
        return (Customer) this;
    }

    /** Returns the Last Name. */
    @NotNull public String getLastName() { return this.lastName; }

    /** Sets the value of the Last Name. */
    @NotNull public Customer setLastName(@NotNull String lastName) {
        markAsModified();
        this.lastName = Strings.truncate(lastName, 255);
        return (Customer) this;
    }

    /** Returns the Home Address Id. */
    public int getHomeAddressId() { return this.homeAddressId; }

    /** Returns the Home Address. */
    @NotNull public Address getHomeAddress() { return homeAddress.solveOrFail(this.homeAddressId); }

    /** Sets the value of the Home Address Id. */
    @NotNull public Customer setHomeAddressId(int homeAddressId) {
        homeAddress.invalidate();
        this.homeAddressId = homeAddressId;
        return (Customer) this;
    }

    /** Sets the value of the Home Address. */
    @NotNull public Customer setHomeAddress(@NotNull Address homeAddress) {
        this.homeAddress.set(homeAddress);
        this.homeAddressId = homeAddress.getId();
        return (Customer) this;
    }

    /** Returns the Work Address Id. */
    @Nullable public Integer getWorkAddressId() { return this.workAddressId; }

    /** Returns the Work Address. */
    @Nullable public Address getWorkAddress() { return workAddress.solve(this.workAddressId); }

    /** Sets the value of the Work Address Id. */
    @NotNull public Customer setWorkAddressId(@Nullable Integer workAddressId) {
        workAddress.invalidate();
        this.workAddressId = workAddressId;
        return (Customer) this;
    }

    /** Sets the value of the Work Address. */
    @SuppressWarnings("AssignmentToNull") @NotNull public Customer setWorkAddress(@Nullable Address workAddress) {
        this.workAddress.set(workAddress);
        if (workAddress == null) {
        	this.workAddressId = null;
        }
        else {
        	this.workAddressId = workAddress.getId();
        }
        return (Customer) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Customer} instance. */
    @NotNull public static Customer create(int document) {
        final Customer result = new Customer();
        ((CustomerBase) result).document = Integers.checkSignedLength("document", document, false, 8);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(int document) { return myEntityTable().findOrCreate(document); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<Customer,Integer> myEntityTable() { return EntityTable.forTable(CUSTOMER); }

    @NotNull public EntityTable<Customer,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Customer,Integer> table() { return CUSTOMER; }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(int document) { return myEntityTable().find(document); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findOrFail(int document) { return myEntityTable().findOrFail(document); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer findPersisted(int document) { return myEntityTable().findPersisted(document); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findPersistedOrFail(int document) { return myEntityTable().findPersistedOrFail(document); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer findWhere(@NotNull Criteria... condition) { return selectFrom(CUSTOMER).where(condition).get(); }

    /** Create a selectFrom(CUSTOMER). */
    @NotNull public static Select<Customer> list() { return selectFrom(CUSTOMER); }

    /** Performs the given action for each Customer */
    public static void forEach(@Nullable Consumer<Customer> consumer) { selectFrom(CUSTOMER).forEach(consumer); }

    /** List instances of 'Customer' with the specified keys. */
    @NotNull public static ImmutableList<Customer> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Customer' with the specified keys. */
    @NotNull public static ImmutableList<Customer> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Customer' that verify the specified condition. */
    @NotNull public static Select<Customer> listWhere(@NotNull Criteria condition) { return selectFrom(CUSTOMER).where(condition); }

    @Override @NotNull public final Customer update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Customer insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Customer> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Customer> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(document); }

    @NotNull public Integer keyObject() { return document; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDocument()); }

    @Override @NotNull public String toString() { return "" + getDocument(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Customer> rowMapper() { return CUSTOMER.metadata().getRowMapper(); }

    @Override public void invalidate() {
        workAddress.invalidate();
        homeAddress.invalidate();
    }

}
