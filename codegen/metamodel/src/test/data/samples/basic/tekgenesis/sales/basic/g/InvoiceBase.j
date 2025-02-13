package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.sales.basic.Customer;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.common.core.Integers;
import tekgenesis.sales.basic.Invoice;
import tekgenesis.sales.basic.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Payment;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.Sex;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.sales.basic.g.CustomerTable.CUSTOMER;
import static tekgenesis.sales.basic.g.InvoiceTable.INVOICE;
import static tekgenesis.sales.basic.g.ItemTable.ITEM;
import static tekgenesis.sales.basic.g.PaymentTable.PAYMENT;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Invoice.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class InvoiceBase
    extends EntityInstanceImpl<Invoice,Integer>
    implements PersistableInstance<Invoice,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull DateOnly invoiceDate = DateOnly.EPOCH;
    @NotNull DocType customerDocumentType = DocType.DNI;
    @NotNull BigDecimal customerDocumentId = BigDecimal.ZERO;
    @NotNull Sex customerSex = Sex.F;
    @NotNull EntityRef<Customer,Tuple3<DocType,BigDecimal,Sex>> customer = new EntityRef<>(CUSTOMER);
    @NotNull private InnerEntitySeq<Item> items = createInnerEntitySeq(ITEM, (Invoice) this, c -> ((ItemBase)c).invoice);
    @NotNull private InnerEntitySeq<Payment> payments = createInnerEntitySeq(PAYMENT, (Invoice) this, c -> ((PaymentBase)c).invoice);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    protected int getIdKey() { return this.idKey; }

    /** Returns the Invoice Date. */
    @NotNull public DateOnly getInvoiceDate() { return this.invoiceDate; }

    /** Sets the value of the Invoice Date. */
    @NotNull public Invoice setInvoiceDate(@NotNull DateOnly invoiceDate) {
        markAsModified();
        this.invoiceDate = invoiceDate;
        return (Invoice) this;
    }

    /** Returns the Customer Document Type. */
    @NotNull public DocType getCustomerDocumentType() { return this.customerDocumentType; }

    /** Returns the Customer Document Id. */
    @NotNull public BigDecimal getCustomerDocumentId() { return this.customerDocumentId; }

    /** Returns the Customer Sex. */
    @NotNull public Sex getCustomerSex() { return this.customerSex; }

    /** Returns the Customer. */
    @NotNull public Customer getCustomer() {
        return customer.solveOrFail(Tuple.tuple(this.customerDocumentType, this.customerDocumentId, this.customerSex));
    }

    /** Sets the value of the Customer Document Type. */
    @NotNull public Invoice setCustomerDocumentType(@NotNull DocType customerDocumentType) {
        customer.invalidate();
        this.customerDocumentType = customerDocumentType;
        return (Invoice) this;
    }

    /** Sets the value of the Customer Document Id. */
    @NotNull public Invoice setCustomerDocumentId(@NotNull BigDecimal customerDocumentId) {
        customer.invalidate();
        this.customerDocumentId = customerDocumentId;
        return (Invoice) this;
    }

    /** Sets the value of the Customer Sex. */
    @NotNull public Invoice setCustomerSex(@NotNull Sex customerSex) {
        customer.invalidate();
        this.customerSex = customerSex;
        return (Invoice) this;
    }

    /** Sets the value of the Customer. */
    @NotNull public Invoice setCustomer(@NotNull Customer customer) {
        this.customer.set(customer);
        this.customerDocumentType = customer.getDocumentType();
        this.customerDocumentId = customer.getDocumentId();
        this.customerSex = customer.getSex();
        return (Invoice) this;
    }

    /** Returns the Items. */
    @NotNull public InnerEntitySeq<Item> getItems() { return items; }

    /** Returns the Payments. */
    @NotNull public InnerEntitySeq<Payment> getPayments() { return payments; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Invoice} instance. */
    @NotNull public static Invoice create(int idKey) {
        final Invoice result = new Invoice();
        ((InvoiceBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Invoice' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Invoice findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'Invoice' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Invoice findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<Invoice,Integer> myEntityTable() { return EntityTable.forTable(INVOICE); }

    @NotNull public EntityTable<Invoice,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Invoice,Integer> table() { return INVOICE; }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Invoice find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Invoice findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Invoice findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Invoice findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Invoice find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Invoice' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Invoice findWhere(@NotNull Criteria... condition) { return selectFrom(INVOICE).where(condition).get(); }

    /** Create a selectFrom(INVOICE). */
    @NotNull public static Select<Invoice> list() { return selectFrom(INVOICE); }

    /** Performs the given action for each Invoice */
    public static void forEach(@Nullable Consumer<Invoice> consumer) { selectFrom(INVOICE).forEach(consumer); }

    /** List instances of 'Invoice' with the specified keys. */
    @NotNull public static ImmutableList<Invoice> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Invoice' with the specified keys. */
    @NotNull public static ImmutableList<Invoice> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Invoice' that verify the specified condition. */
    @NotNull public static Select<Invoice> listWhere(@NotNull Criteria condition) { return selectFrom(INVOICE).where(condition); }

    @Override @NotNull public final Invoice update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Invoice insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Invoice> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Invoice> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Invoice> rowMapper() { return INVOICE.metadata().getRowMapper(); }

    @Override public void invalidate() {
        payments.invalidate();
        items.invalidate();
        customer.invalidate();
    }

}
