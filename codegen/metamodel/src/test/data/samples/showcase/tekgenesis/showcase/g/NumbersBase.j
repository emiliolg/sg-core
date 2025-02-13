package tekgenesis.showcase.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.Numbers;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.common.core.Reals;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.NumbersTable.NUMBERS;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Numbers.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class NumbersBase
    extends EntityInstanceImpl<Numbers,String>
    implements PersistableInstance<Numbers,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String name = "";
    int unsignedInt4 = 0;
    int signedInt5 = 0;
    int signedToBeUnsigned = 0;
    int unsignedInteger = 0;
    int signedInteger = 0;
    @NotNull BigDecimal unsignedDecimal52 = BigDecimal.ZERO;
    @NotNull BigDecimal signedDecimal52 = BigDecimal.ZERO;
    double unsignedReal = 0.0;
    double signedReal = 0.0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Unsigned Int4. */
    public int getUnsignedInt4() { return this.unsignedInt4; }

    /** Sets the value of the Unsigned Int4. */
    @NotNull public Numbers setUnsignedInt4(int unsignedInt4) {
        markAsModified();
        this.unsignedInt4 = Integers.checkSignedLength("unsignedInt4", unsignedInt4, false, 4);
        return (Numbers) this;
    }

    /** Returns the Signed Int5. */
    public int getSignedInt5() { return this.signedInt5; }

    /** Sets the value of the Signed Int5. */
    @NotNull public Numbers setSignedInt5(int signedInt5) {
        markAsModified();
        this.signedInt5 = Integers.checkSignedLength("signedInt5", signedInt5, true, 5);
        return (Numbers) this;
    }

    /** Returns the Signed To Be Unsigned. */
    public int getSignedToBeUnsigned() { return this.signedToBeUnsigned; }

    /** Sets the value of the Signed To Be Unsigned. */
    @NotNull public Numbers setSignedToBeUnsigned(int signedToBeUnsigned) {
        markAsModified();
        this.signedToBeUnsigned = Integers.checkSignedLength("signedToBeUnsigned", signedToBeUnsigned, true, 9);
        return (Numbers) this;
    }

    /** Returns the Unsigned Integer. */
    public int getUnsignedInteger() { return this.unsignedInteger; }

    /** Sets the value of the Unsigned Integer. */
    @NotNull public Numbers setUnsignedInteger(int unsignedInteger) {
        markAsModified();
        this.unsignedInteger = Integers.checkSignedLength("unsignedInteger", unsignedInteger, false, 8);
        return (Numbers) this;
    }

    /** Returns the Signed Integer. */
    public int getSignedInteger() { return this.signedInteger; }

    /** Sets the value of the Signed Integer. */
    @NotNull public Numbers setSignedInteger(int signedInteger) {
        markAsModified();
        this.signedInteger = Integers.checkSignedLength("signedInteger", signedInteger, true, 9);
        return (Numbers) this;
    }

    /** Returns the Unsigned Decimal52. */
    @NotNull public BigDecimal getUnsignedDecimal52() { return this.unsignedDecimal52; }

    /** Sets the value of the Unsigned Decimal52. */
    @NotNull public Numbers setUnsignedDecimal52(@NotNull BigDecimal unsignedDecimal52) {
        markAsModified();
        this.unsignedDecimal52 = Decimals.scaleAndCheck("unsignedDecimal52", unsignedDecimal52, false, 5, 2);
        return (Numbers) this;
    }

    /** Returns the Signed Decimal52. */
    @NotNull public BigDecimal getSignedDecimal52() { return this.signedDecimal52; }

    /** Sets the value of the Signed Decimal52. */
    @NotNull public Numbers setSignedDecimal52(@NotNull BigDecimal signedDecimal52) {
        markAsModified();
        this.signedDecimal52 = Decimals.scaleAndCheck("signedDecimal52", signedDecimal52, true, 5, 2);
        return (Numbers) this;
    }

    /** Returns the Unsigned Real. */
    public double getUnsignedReal() { return this.unsignedReal; }

    /** Sets the value of the Unsigned Real. */
    @NotNull public Numbers setUnsignedReal(double unsignedReal) {
        markAsModified();
        this.unsignedReal = Reals.checkSigned("unsignedReal", unsignedReal, false);
        return (Numbers) this;
    }

    /** Returns the Signed Real. */
    public double getSignedReal() { return this.signedReal; }

    /** Sets the value of the Signed Real. */
    @NotNull public Numbers setSignedReal(double signedReal) {
        markAsModified();
        this.signedReal = Reals.checkSigned("signedReal", signedReal, true);
        return (Numbers) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Numbers} instance. */
    @NotNull public static Numbers create(@NotNull String name) {
        final Numbers result = new Numbers();
        ((NumbersBase) result).name = Strings.truncate(name, 255);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Numbers' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Numbers findOrCreate(@NotNull String name) { return myEntityTable().findOrCreate(name); }

    @NotNull private static EntityTable<Numbers,String> myEntityTable() { return EntityTable.forTable(NUMBERS); }

    @NotNull public EntityTable<Numbers,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Numbers,String> table() { return NUMBERS; }

    /** 
     * Try to finds an Object of type 'Numbers' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Numbers find(@NotNull String name) { return myEntityTable().find(name); }

    /** 
     * Try to finds an Object of type 'Numbers' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Numbers findOrFail(@NotNull String name) { return myEntityTable().findOrFail(name); }

    /** 
     * Try to finds an Object of type 'Numbers' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Numbers findPersisted(@NotNull String name) { return myEntityTable().findPersisted(name); }

    /** 
     * Try to finds an Object of type 'Numbers' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Numbers findPersistedOrFail(@NotNull String name) { return myEntityTable().findPersistedOrFail(name); }

    /** 
     * Try to finds an Object of type 'Numbers' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Numbers findWhere(@NotNull Criteria... condition) { return selectFrom(NUMBERS).where(condition).get(); }

    /** Create a selectFrom(NUMBERS). */
    @NotNull public static Select<Numbers> list() { return selectFrom(NUMBERS); }

    /** Performs the given action for each Numbers */
    public static void forEach(@Nullable Consumer<Numbers> consumer) { selectFrom(NUMBERS).forEach(consumer); }

    /** List instances of 'Numbers' with the specified keys. */
    @NotNull public static ImmutableList<Numbers> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Numbers' that verify the specified condition. */
    @NotNull public static Select<Numbers> listWhere(@NotNull Criteria condition) { return selectFrom(NUMBERS).where(condition); }

    @Override @NotNull public final Numbers update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Numbers insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Numbers> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Numbers> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return name; }

    @NotNull public String keyObject() { return name; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Numbers> rowMapper() { return NUMBERS.metadata().getRowMapper(); }

}
