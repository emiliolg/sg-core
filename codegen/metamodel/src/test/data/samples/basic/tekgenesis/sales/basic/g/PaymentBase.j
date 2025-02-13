package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.sales.basic.Invoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Payment;
import tekgenesis.sales.basic.PaymentType;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.InvoiceTable.INVOICE;
import static tekgenesis.sales.basic.g.PaymentTable.PAYMENT;
import static tekgenesis.sales.basic.g.PaymentTypeTable.PAYMENT_TYPE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Payment.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class PaymentBase
    extends EntityInstanceImpl<Payment,Tuple<Integer,Integer>>
    implements InnerInstance<Payment,Tuple<Integer,Integer>,Invoice,Integer>
{

    //~ Fields ...................................................................................................................

    int invoiceIdKey = 0;
    @NotNull EntityRef<Invoice,Integer> invoice = new EntityRef<>(INVOICE, Invoice::getPayments);
    int seqId = 0;
    int paymentId = 0;
    @NotNull EntityRef<PaymentType,Integer> payment = new EntityRef<>(PAYMENT_TYPE);
    @NotNull BigDecimal amount = BigDecimal.ZERO;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Invoice Id Key. */
    public int getInvoiceIdKey() { return this.invoiceIdKey; }

    /** Returns the Invoice. */
    @NotNull public Invoice getInvoice() { return invoice.solveOrFail(this.invoiceIdKey); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Invoice,Integer> parent() { return invoice; }

    @Override @NotNull public InnerEntitySeq<Payment> siblings() { return getInvoice().getPayments(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Payment Id. */
    public int getPaymentId() { return this.paymentId; }

    /** Returns the Payment. */
    @NotNull public PaymentType getPayment() { return payment.solveOrFail(this.paymentId); }

    /** Sets the value of the Payment Id. */
    @NotNull public Payment setPaymentId(int paymentId) {
        payment.invalidate();
        this.paymentId = paymentId;
        return (Payment) this;
    }

    /** Sets the value of the Payment. */
    @NotNull public Payment setPayment(@NotNull PaymentType payment) {
        this.payment.set(payment);
        this.paymentId = payment.getId();
        return (Payment) this;
    }

    /** Returns the Amount. */
    @NotNull public BigDecimal getAmount() { return this.amount; }

    /** Sets the value of the Amount. */
    @NotNull public Payment setAmount(@NotNull BigDecimal amount) {
        markAsModified();
        this.amount = Decimals.scaleAndCheck("amount", amount, false, 10, 2);
        return (Payment) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Payment,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(PAYMENT); }

    @NotNull public EntityTable<Payment,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Payment,Tuple<Integer,Integer>> table() { return PAYMENT; }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Payment find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Payment findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Payment findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Payment findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Payment find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Payment find(int invoiceIdKey, int seqId) { return find(Tuple.tuple2(invoiceIdKey, seqId)); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Payment find(@NotNull String invoice, int seqId) { return find(Conversions.toInt(invoice), seqId); }

    /** 
     * Try to finds an Object of type 'Payment' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Payment findWhere(@NotNull Criteria... condition) { return selectFrom(PAYMENT).where(condition).get(); }

    /** Create a selectFrom(PAYMENT). */
    @NotNull public static Select<Payment> list() { return selectFrom(PAYMENT); }

    /** Performs the given action for each Payment */
    public static void forEach(@Nullable Consumer<Payment> consumer) { selectFrom(PAYMENT).forEach(consumer); }

    /** List instances of 'Payment' with the specified keys. */
    @NotNull public static ImmutableList<Payment> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Payment' with the specified keys. */
    @NotNull public static ImmutableList<Payment> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Payment' that verify the specified condition. */
    @NotNull public static Select<Payment> listWhere(@NotNull Criteria condition) { return selectFrom(PAYMENT).where(condition); }

    @Override @NotNull public final Payment update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Payment insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Payment> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Payment> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return invoiceIdKey + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(invoiceIdKey, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getInvoice(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getInvoice() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Payment> rowMapper() { return PAYMENT.metadata().getRowMapper(); }

    @Override public void invalidate() {
        payment.invalidate();
        invoice.invalidate();
    }

}
