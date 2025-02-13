package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.PaymentOption;
import tekgenesis.sales.basic.PaymentType;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.PaymentTypeTable.PAYMENT_TYPE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: PaymentType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class PaymentTypeBase
    extends EntityInstanceImpl<PaymentType,Integer>
    implements PersistableInstance<PaymentType,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull PaymentOption type = PaymentOption.CASH;
    @NotNull String subtype = "";
    @NotNull String description = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Type. */
    @NotNull public PaymentOption getType() { return this.type; }

    /** Sets the value of the Type. */
    @NotNull public PaymentType setType(@NotNull PaymentOption type) {
        markAsModified();
        this.type = type;
        return (PaymentType) this;
    }

    /** Returns the Subtype. */
    @NotNull public String getSubtype() { return this.subtype; }

    /** Sets the value of the Subtype. */
    @NotNull public PaymentType setSubtype(@NotNull String subtype) {
        markAsModified();
        this.subtype = Strings.truncate(subtype, 30);
        return (PaymentType) this;
    }

    /** Returns the Description. */
    @NotNull public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public PaymentType setDescription(@NotNull String description) {
        markAsModified();
        this.description = Strings.truncate(description, 160);
        return (PaymentType) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link PaymentType} instance. */
    @NotNull public static PaymentType create() { return new PaymentType(); }

    @NotNull private static EntityTable<PaymentType,Integer> myEntityTable() { return EntityTable.forTable(PAYMENT_TYPE); }

    @NotNull public EntityTable<PaymentType,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<PaymentType,Integer> table() { return PAYMENT_TYPE; }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PaymentType find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static PaymentType findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PaymentType findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static PaymentType findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PaymentType find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'PaymentType' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PaymentType findWhere(@NotNull Criteria... condition) { return selectFrom(PAYMENT_TYPE).where(condition).get(); }

    /** Create a selectFrom(PAYMENT_TYPE). */
    @NotNull public static Select<PaymentType> list() { return selectFrom(PAYMENT_TYPE); }

    /** Performs the given action for each PaymentType */
    public static void forEach(@Nullable Consumer<PaymentType> consumer) { selectFrom(PAYMENT_TYPE).forEach(consumer); }

    /** List instances of 'PaymentType' with the specified keys. */
    @NotNull public static ImmutableList<PaymentType> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'PaymentType' with the specified keys. */
    @NotNull public static ImmutableList<PaymentType> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'PaymentType' that verify the specified condition. */
    @NotNull public static Select<PaymentType> listWhere(@NotNull Criteria condition) { return selectFrom(PAYMENT_TYPE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((PaymentType) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<PaymentType> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<PaymentType> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** List the instances of 'PaymentType' that matches the given parameters. */
    @NotNull public static ImmutableList<PaymentType> listByType(@NotNull PaymentOption type) {
        return selectFrom(PAYMENT_TYPE).where(PAYMENT_TYPE.TYPE.eq(type)).list();
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getSubtype()); }

    @Override @NotNull public String toString() { return "" + getSubtype(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<PaymentType> rowMapper() { return PAYMENT_TYPE.metadata().getRowMapper(); }

}
