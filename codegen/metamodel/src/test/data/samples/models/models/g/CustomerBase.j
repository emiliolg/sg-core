package models.g;

import models.Bank;
import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import models.Customer;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import models.DocType;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import models.Sex;
import models.Sport;
import models.State;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static models.g.BankTable.BANK;
import static models.g.CustomerTable.CUSTOMER;
import static models.g.StateTable.STATE;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Customer.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CustomerBase
    extends EntityInstanceImpl<Customer,Tuple<DocType,BigDecimal>>
    implements PersistableInstance<Customer,Tuple<DocType,BigDecimal>>
{

    //~ Fields ...................................................................................................................

    @NotNull DocType documentType = DocType.PASSPORT;
    @NotNull BigDecimal documentId = BigDecimal.ZERO;
    @NotNull String firstName = "";
    @NotNull String lastName = "";
    @NotNull String nickname = "";
    @NotNull Sex sex = Sex.MALE;
    @NotNull String stateCountryCode = "";
    @NotNull String stateCode = "";
    @NotNull EntityRef<State,Tuple<String,String>> state = new EntityRef<>(STATE);
    @NotNull EnumSet<Sport> sports = EnumSet.of(Sport.FOOTBALL);
    @NotNull private InnerEntitySeq<Bank> banks = createInnerEntitySeq(BANK, (Customer) this, c -> ((BankBase)c).customer);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Document Type. */
    @NotNull public DocType getDocumentType() { return this.documentType; }

    /** Returns the Document Id. */
    @NotNull public BigDecimal getDocumentId() { return this.documentId; }

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

    /** Returns the Sex. */
    @NotNull public Sex getSex() { return this.sex; }

    /** Sets the value of the Sex. */
    @NotNull public Customer setSex(@NotNull Sex sex) {
        markAsModified();
        this.sex = sex;
        return (Customer) this;
    }

    /** Returns the State Country Code. */
    @NotNull public String getStateCountryCode() { return this.stateCountryCode; }

    /** Returns the State Code. */
    @NotNull public String getStateCode() { return this.stateCode; }

    /** Returns the State. */
    @NotNull public State getState() {
        return state.solveOrFail(Tuple.tuple2(this.stateCountryCode, this.stateCode));
    }

    /** Sets the value of the State Country Code. */
    @NotNull public Customer setStateCountryCode(@NotNull String stateCountryCode) {
        state.invalidate();
        this.stateCountryCode = stateCountryCode;
        return (Customer) this;
    }

    /** Sets the value of the State Code. */
    @NotNull public Customer setStateCode(@NotNull String stateCode) {
        state.invalidate();
        this.stateCode = stateCode;
        return (Customer) this;
    }

    /** Sets the value of the State. */
    @NotNull public Customer setState(@NotNull State state) {
        this.state.set(state);
        this.stateCountryCode = state.getCountryCode();
        this.stateCode = state.getCode();
        return (Customer) this;
    }

    /** Returns the Sports. */
    @NotNull public EnumSet<Sport> getSports() { return this.sports; }

    /** Sets the value of the Sports. */
    @NotNull public Customer setSports(@NotNull EnumSet<Sport> sports) {
        markAsModified();
        this.sports = sports;
        return (Customer) this;
    }

    /** Returns the Banks. */
    @NotNull public InnerEntitySeq<Bank> getBanks() { return banks; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Customer} instance. */
    @NotNull public static Customer create(@NotNull DocType documentType, @NotNull BigDecimal documentId) {
        final Customer result = new Customer();
        ((CustomerBase) result).documentType = documentType;
        ((CustomerBase) result).documentId = Decimals.scaleAndCheck("documentId", documentId, false, 10, 0);
        return result;
    }

    /** 
     * Creates a new {@link Customer} instance.
     * Based on the primary key object
     */
    @NotNull public static Customer create(@NotNull Tuple<DocType,BigDecimal> key) { return create(key._1(), key._2()); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull Tuple<DocType,BigDecimal> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'Customer' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Customer findOrCreate(@NotNull DocType documentType, @NotNull BigDecimal documentId) { return findOrCreate(Tuple.tuple2(documentType, documentId)); }

    @NotNull private static EntityTable<Customer,Tuple<DocType,BigDecimal>> myEntityTable() { return EntityTable.forTable(CUSTOMER); }

    @NotNull public EntityTable<Customer,Tuple<DocType,BigDecimal>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Customer,Tuple<DocType,BigDecimal>> table() { return CUSTOMER; }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer find(@NotNull Tuple<DocType,BigDecimal> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findOrFail(@NotNull Tuple<DocType,BigDecimal> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Customer findPersisted(@NotNull Tuple<DocType,BigDecimal> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Customer' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Customer findPersistedOrFail(@NotNull Tuple<DocType,BigDecimal> key) { return myEntityTable().findPersistedOrFail(key); }

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
    @Nullable public static Customer find(@NotNull DocType documentType, @NotNull BigDecimal documentId) { return find(Tuple.tuple2(documentType, documentId)); }

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
    @NotNull public static ImmutableList<Customer> list(@Nullable Set<Tuple<DocType,BigDecimal>> keys) { return myEntityTable().list(keys); }

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

    @NotNull public String keyAsString() { return documentType + ":" + documentId; }

    @NotNull public Tuple<DocType,BigDecimal> keyObject() { return Tuple.tuple2(documentType, documentId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getFirstName(), getLastName()); }

    @Override @NotNull public String toString() { return "" + getFirstName() + " " + getLastName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Customer> rowMapper() { return CUSTOMER.metadata().getRowMapper(); }

    @Override public void invalidate() {
        banks.invalidate();
        state.invalidate();
    }

}
