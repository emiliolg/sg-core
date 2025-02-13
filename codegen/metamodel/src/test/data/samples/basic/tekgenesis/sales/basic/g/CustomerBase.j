package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.sales.basic.Customer;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.common.core.Resource;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.Sex;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.sales.basic.g.CustomerTable.CUSTOMER;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Customer.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CustomerBase
    extends EntityInstanceImpl<Customer,Tuple3<DocType,BigDecimal,Sex>>
    implements PersistableInstance<Customer,Tuple3<DocType,BigDecimal,Sex>>
{

    //~ Fields ...................................................................................................................

    @NotNull DocType documentType = DocType.DNI;
    @NotNull BigDecimal documentId = BigDecimal.ZERO;
    @NotNull Sex sex = Sex.F;
    @NotNull String firstName = "";
    @NotNull String lastName = "";
    @NotNull String nickname = "";
    @Nullable Resource photo = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Document Type. */
    @NotNull public DocType getDocumentType() { return this.documentType; }

    /** Returns the Document Id. */
    @NotNull public BigDecimal getDocumentId() { return this.documentId; }

    /** Returns the Sex. */
    @NotNull public Sex getSex() { return this.sex; }

    /** Returns the First Name. */
    @NotNull public String getFirstName() { return this.firstName; }

    /** Sets the value of the First Name. */
    @NotNull public Customer setFirstName(@NotNull String firstName) {
        markAsModified();
        this.firstName = Strings.truncate(firstName, 50);
        return (Customer) this;
    }

    /** Returns the Last Name. */
    @NotNull public String getLastName() { return this.lastName; }

    /** Sets the value of the Last Name. */
    @NotNull public Customer setLastName(@NotNull String lastName) {
        markAsModified();
        this.lastName = Strings.truncate(lastName, 50);
        return (Customer) this;
    }

    /** Returns the Nickname. */
    @NotNull public String getNickname() { return this.nickname; }

    /** Sets the value of the Nickname. */
    @NotNull public Customer setNickname(@NotNull String nickname) {
        markAsModified();
        this.nickname = Strings.truncate(nickname, 50);
        return (Customer) this;
    }

    /** Returns the Photo. */
    @Nullable public Resource getPhoto() { return this.photo; }

    /** Sets the value of the Photo. */
    @NotNull public Customer setPhoto(@Nullable Resource photo) {
        markAsModified();
        this.photo = photo;
        return (Customer) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Customer} instance. */
    @NotNull public static Customer create(@NotNull DocType documentType, @NotNull BigDecimal documentId, @NotNull Sex sex) {
        final Customer result = new Customer();
        ((CustomerBase) result).documentType = documentType;
        ((CustomerBase) result).documentId = Decimals.scaleAndCheck("documentId", documentId, false, 10, 0);
        ((CustomerBase) result).sex = sex;
        return result;
    }

    /** 
     * Creates a new {@link Customer} instance.
     * Based on the primary key object
     */
    @NotNull public static Customer create(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return create(key._1(), key._2(), key._3()); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull DocType documentType, @NotNull BigDecimal documentId, @NotNull Sex sex) {
        return findOrCreate(Tuple.tuple(documentType, documentId, sex));
    }

    @NotNull private static EntityTable<Customer,Tuple3<DocType,BigDecimal,Sex>> myEntityTable() { return EntityTable.forTable(CUSTOMER); }

    @NotNull public EntityTable<Customer,Tuple3<DocType,BigDecimal,Sex>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Customer,Tuple3<DocType,BigDecimal,Sex>> table() { return CUSTOMER; }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findOrFail(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer findPersisted(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findPersistedOrFail(@NotNull Tuple3<DocType,BigDecimal,Sex> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(@NotNull DocType documentType, @NotNull BigDecimal documentId, @NotNull Sex sex) { return find(Tuple.tuple(documentType, documentId, sex)); }

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
    @NotNull public static ImmutableList<Customer> list(@Nullable Set<Tuple3<DocType,BigDecimal,Sex>> keys) { return myEntityTable().list(keys); }

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

    /** Finds the instance */
    @Nullable public static Customer findByNick(@NotNull String nickname) { return myEntityTable().findByKey(0, nickname); }

    /** List the instances of 'Customer' that matches the given parameters. */
    @NotNull public static ImmutableList<Customer> listByLast(@NotNull String lastName, @NotNull String firstName) {
        return selectFrom(CUSTOMER).where(CUSTOMER.LAST_NAME.eq(lastName), CUSTOMER.FIRST_NAME.eq(firstName)).list();
    }

    @NotNull public String keyAsString() { return documentType + ":" + documentId + ":" + sex; }

    @NotNull public Tuple3<DocType,BigDecimal,Sex> keyObject() { return Tuple.tuple(documentType, documentId, sex); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getFirstName(), getLastName()); }

    @Override @NotNull public String toString() { return "" + getFirstName() + " " + getLastName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Customer> rowMapper() { return CUSTOMER.metadata().getRowMapper(); }

}
