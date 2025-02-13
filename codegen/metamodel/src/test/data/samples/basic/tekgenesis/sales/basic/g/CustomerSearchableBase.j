package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.sales.basic.CustomerSearchable;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.Sex;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CustomerSearchableTable.CUSTOMER_SEARCHABLE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CustomerSearchable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CustomerSearchableBase
    extends EntityInstanceImpl<CustomerSearchable,Integer>
    implements PersistableInstance<CustomerSearchable,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String firstName = "";
    @NotNull String lastName = "";
    @NotNull BigDecimal document = BigDecimal.ZERO;
    @NotNull DateTime birthDate = DateTime.EPOCH;
    @NotNull Sex sex = Sex.F;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the First Name. */
    @NotNull public String getFirstName() { return this.firstName; }

    /** Sets the value of the First Name. */
    @NotNull public CustomerSearchable setFirstName(@NotNull String firstName) {
        markAsModified();
        this.firstName = Strings.truncate(firstName, 255);
        return (CustomerSearchable) this;
    }

    /** Returns the Last Name. */
    @NotNull public String getLastName() { return this.lastName; }

    /** Sets the value of the Last Name. */
    @NotNull public CustomerSearchable setLastName(@NotNull String lastName) {
        markAsModified();
        this.lastName = Strings.truncate(lastName, 255);
        return (CustomerSearchable) this;
    }

    /** Returns the Document. */
    @NotNull public BigDecimal getDocument() { return this.document; }

    /** Sets the value of the Document. */
    @NotNull public CustomerSearchable setDocument(@NotNull BigDecimal document) {
        markAsModified();
        this.document = Decimals.scaleAndCheck("document", document, false, 10, 0);
        return (CustomerSearchable) this;
    }

    /** Returns the Birth Date. */
    @NotNull public DateTime getBirthDate() { return this.birthDate; }

    /** Sets the value of the Birth Date. */
    @NotNull public CustomerSearchable setBirthDate(@NotNull DateTime birthDate) {
        markAsModified();
        this.birthDate = birthDate;
        return (CustomerSearchable) this;
    }

    /** Returns the Sex. */
    @NotNull public Sex getSex() { return this.sex; }

    /** Sets the value of the Sex. */
    @NotNull public CustomerSearchable setSex(@NotNull Sex sex) {
        markAsModified();
        this.sex = sex;
        return (CustomerSearchable) this;
    }

    /** Returns the Some Extra Field. */
    @NotNull public abstract String getSomeExtraField();

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CustomerSearchable} instance. */
    @NotNull public static CustomerSearchable create() { return new CustomerSearchable(); }

    @NotNull private static EntityTable<CustomerSearchable,Integer> myEntityTable() { return EntityTable.forTable(CUSTOMER_SEARCHABLE); }

    @NotNull public EntityTable<CustomerSearchable,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CustomerSearchable,Integer> table() { return CUSTOMER_SEARCHABLE; }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CustomerSearchable find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CustomerSearchable findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CustomerSearchable findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CustomerSearchable findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CustomerSearchable find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CustomerSearchable' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CustomerSearchable findWhere(@NotNull Criteria... condition) {
        return selectFrom(CUSTOMER_SEARCHABLE).where(condition).get();
    }

    /** Create a selectFrom(CUSTOMER_SEARCHABLE). */
    @NotNull public static Select<CustomerSearchable> list() { return selectFrom(CUSTOMER_SEARCHABLE); }

    /** Performs the given action for each CustomerSearchable */
    public static void forEach(@Nullable Consumer<CustomerSearchable> consumer) { selectFrom(CUSTOMER_SEARCHABLE).forEach(consumer); }

    /** List instances of 'CustomerSearchable' with the specified keys. */
    @NotNull public static ImmutableList<CustomerSearchable> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CustomerSearchable' with the specified keys. */
    @NotNull public static ImmutableList<CustomerSearchable> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CustomerSearchable' that verify the specified condition. */
    @NotNull public static Select<CustomerSearchable> listWhere(@NotNull Criteria condition) { return selectFrom(CUSTOMER_SEARCHABLE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((CustomerSearchable) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CustomerSearchable> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CustomerSearchable> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getFirstName(), getLastName()); }

    @Override @NotNull public String toString() { return "" + getFirstName() + " " + getLastName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CustomerSearchable> rowMapper() { return CUSTOMER_SEARCHABLE.metadata().getRowMapper(); }

}
