package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.sales.basic.Customer;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.sales.basic.MailDigest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.Preferences;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.Sex;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.sales.basic.g.CustomerTable.CUSTOMER;
import static tekgenesis.sales.basic.g.PreferencesTable.PREFERENCES;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Preferences.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class PreferencesBase
    extends EntityInstanceImpl<Preferences,Integer>
    implements PersistableInstance<Preferences,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull DocType customerDocumentType = DocType.DNI;
    @NotNull BigDecimal customerDocumentId = BigDecimal.ZERO;
    @NotNull Sex customerSex = Sex.F;
    @NotNull EntityRef<Customer,Tuple3<DocType,BigDecimal,Sex>> customer = new EntityRef<>(CUSTOMER);
    @NotNull String mail = "";
    @NotNull String twitter = "";
    @NotNull MailDigest digest = MailDigest.DAILY;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

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
    @NotNull public Preferences setCustomerDocumentType(@NotNull DocType customerDocumentType) {
        customer.invalidate();
        this.customerDocumentType = customerDocumentType;
        return (Preferences) this;
    }

    /** Sets the value of the Customer Document Id. */
    @NotNull public Preferences setCustomerDocumentId(@NotNull BigDecimal customerDocumentId) {
        customer.invalidate();
        this.customerDocumentId = customerDocumentId;
        return (Preferences) this;
    }

    /** Sets the value of the Customer Sex. */
    @NotNull public Preferences setCustomerSex(@NotNull Sex customerSex) {
        customer.invalidate();
        this.customerSex = customerSex;
        return (Preferences) this;
    }

    /** Sets the value of the Customer. */
    @NotNull public Preferences setCustomer(@NotNull Customer customer) {
        this.customer.set(customer);
        this.customerDocumentType = customer.getDocumentType();
        this.customerDocumentId = customer.getDocumentId();
        this.customerSex = customer.getSex();
        return (Preferences) this;
    }

    /** Returns the Mail. */
    @NotNull public String getMail() { return this.mail; }

    /** Sets the value of the Mail. */
    @NotNull public Preferences setMail(@NotNull String mail) {
        markAsModified();
        this.mail = Strings.truncate(mail, 60);
        return (Preferences) this;
    }

    /** Returns the Twitter. */
    @NotNull public String getTwitter() { return this.twitter; }

    /** Sets the value of the Twitter. */
    @NotNull public Preferences setTwitter(@NotNull String twitter) {
        markAsModified();
        this.twitter = Strings.truncate(twitter, 60);
        return (Preferences) this;
    }

    /** Returns the Digest. */
    @NotNull public MailDigest getDigest() { return this.digest; }

    /** Sets the value of the Digest. */
    @NotNull public Preferences setDigest(@NotNull MailDigest digest) {
        markAsModified();
        this.digest = digest;
        return (Preferences) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Preferences} instance. */
    @NotNull public static Preferences create() { return new Preferences(); }

    @NotNull private static EntityTable<Preferences,Integer> myEntityTable() { return EntityTable.forTable(PREFERENCES); }

    @NotNull public EntityTable<Preferences,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Preferences,Integer> table() { return PREFERENCES; }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Preferences find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Preferences findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Preferences findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Preferences findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Preferences find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Preferences' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Preferences findWhere(@NotNull Criteria... condition) { return selectFrom(PREFERENCES).where(condition).get(); }

    /** Create a selectFrom(PREFERENCES). */
    @NotNull public static Select<Preferences> list() { return selectFrom(PREFERENCES); }

    /** Performs the given action for each Preferences */
    public static void forEach(@Nullable Consumer<Preferences> consumer) { selectFrom(PREFERENCES).forEach(consumer); }

    /** List instances of 'Preferences' with the specified keys. */
    @NotNull public static ImmutableList<Preferences> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Preferences' with the specified keys. */
    @NotNull public static ImmutableList<Preferences> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Preferences' that verify the specified condition. */
    @NotNull public static Select<Preferences> listWhere(@NotNull Criteria condition) { return selectFrom(PREFERENCES).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Preferences) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Preferences> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Preferences> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** Finds the instance */
    @Nullable public static Preferences findByCustomer(@NotNull DocType customerDocumentType, @NotNull BigDecimal customerDocumentId, @NotNull Sex customerSex) {
        return myEntityTable().findByKey(0, Tuple.tuple(customerDocumentType, customerDocumentId, customerSex));
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCustomer()); }

    @Override @NotNull public String toString() { return "" + getCustomer(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Preferences> rowMapper() { return PREFERENCES.metadata().getRowMapper(); }

    @Override public void invalidate() { customer.invalidate(); }

}
