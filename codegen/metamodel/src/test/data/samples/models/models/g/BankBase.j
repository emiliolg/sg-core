package models.g;

import tekgenesis.persistence.AuditableInstance;
import models.Bank;
import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import models.Customer;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import models.DocType;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static models.g.BankTable.BANK;
import static models.g.CustomerTable.CUSTOMER;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Bank.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class BankBase
    extends EntityInstanceImpl<Bank,Tuple3<DocType,BigDecimal,Integer>>
    implements InnerInstance<Bank,Tuple3<DocType,BigDecimal,Integer>,Customer,Tuple<DocType,BigDecimal>>, AuditableInstance
{

    //~ Fields ...................................................................................................................

    @NotNull DocType customerDocumentType = DocType.PASSPORT;
    @NotNull BigDecimal customerDocumentId = BigDecimal.ZERO;
    @NotNull EntityRef<Customer,Tuple<DocType,BigDecimal>> customer = new EntityRef<>(CUSTOMER, Customer::getBanks);
    int seqId = 0;
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @NotNull DateTime creationTime = DateTime.EPOCH;
    @Nullable String creationUser = null;
    @Nullable String updateUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Customer Document Type. */
    @NotNull public DocType getCustomerDocumentType() { return this.customerDocumentType; }

    /** Returns the Customer Document Id. */
    @NotNull public BigDecimal getCustomerDocumentId() { return this.customerDocumentId; }

    /** Returns the Customer. */
    @NotNull public Customer getCustomer() {
        return customer.solveOrFail(Tuple.tuple2(this.customerDocumentType, this.customerDocumentId));
    }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Customer,Tuple<DocType,BigDecimal>> parent() { return customer; }

    @Override @NotNull public InnerEntitySeq<Bank> siblings() { return getCustomer().getBanks(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Bank setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (Bank) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Creation Time. */
    @NotNull public DateTime getCreationTime() { return this.creationTime; }

    /** Returns the Creation User. */
    @Nullable public String getCreationUser() { return this.creationUser; }

    /** Returns the Update User. */
    @Nullable public String getUpdateUser() { return this.updateUser; }

    @NotNull private static EntityTable<Bank,Tuple3<DocType,BigDecimal,Integer>> myEntityTable() { return EntityTable.forTable(BANK); }

    @NotNull public EntityTable<Bank,Tuple3<DocType,BigDecimal,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Bank,Tuple3<DocType,BigDecimal,Integer>> table() { return BANK; }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Bank find(@NotNull Tuple3<DocType,BigDecimal,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Bank findOrFail(@NotNull Tuple3<DocType,BigDecimal,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Bank findPersisted(@NotNull Tuple3<DocType,BigDecimal,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Bank findPersistedOrFail(@NotNull Tuple3<DocType,BigDecimal,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Bank find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Bank find(@NotNull DocType customerDocumentType, @NotNull BigDecimal customerDocumentId, int seqId) {
        return find(Tuple.tuple(customerDocumentType, customerDocumentId, seqId));
    }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Bank find(@NotNull String customer, int seqId) {
        final String[] customerParts = Strings.splitToArray(customer, 2);
        return find(DocType.valueOf(customerParts[0]), Conversions.toDecimal(customerParts[1]), seqId);
    }

    /** 
     * Try to finds an Object of type 'Bank' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Bank findWhere(@NotNull Criteria... condition) { return selectFrom(BANK).where(condition).get(); }

    /** Create a selectFrom(BANK). */
    @NotNull public static Select<Bank> list() { return selectFrom(BANK); }

    /** Performs the given action for each Bank */
    public static void forEach(@Nullable Consumer<Bank> consumer) { selectFrom(BANK).forEach(consumer); }

    /** List instances of 'Bank' with the specified keys. */
    @NotNull public static ImmutableList<Bank> list(@Nullable Set<Tuple3<DocType,BigDecimal,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Bank' with the specified keys. */
    @NotNull public static ImmutableList<Bank> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Bank' that verify the specified condition. */
    @NotNull public static Select<Bank> listWhere(@NotNull Criteria condition) { return selectFrom(BANK).where(condition); }

    @Override @NotNull public final Bank update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Bank insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Bank> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Bank> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return customerDocumentType + ":" + customerDocumentId + ":" + seqId;
    }

    @NotNull public Tuple3<DocType,BigDecimal,Integer> keyObject() {
        return Tuple.tuple(customerDocumentType, customerDocumentId, seqId);
    }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCustomer(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getCustomer() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Bank> rowMapper() { return BANK.metadata().getRowMapper(); }

    @Override public void invalidate() { customer.invalidate(); }

}
