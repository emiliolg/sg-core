package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.showcase.DynamicProperty;
import tekgenesis.showcase.DynamicValue;
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
import static tekgenesis.showcase.g.DynamicPropertyTable.DYNAMIC_PROPERTY;
import static tekgenesis.showcase.g.DynamicValueTable.DYNAMIC_VALUE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DynamicValue.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DynamicValueBase
    extends EntityInstanceImpl<DynamicValue,Tuple<Integer,Integer>>
    implements InnerInstance<DynamicValue,Tuple<Integer,Integer>,DynamicProperty,Integer>
{

    //~ Fields ...................................................................................................................

    int dynamicPropertyId = 0;
    @NotNull EntityRef<DynamicProperty,Integer> dynamicProperty = new EntityRef<>(DYNAMIC_PROPERTY, DynamicProperty::getValues);
    int seqId = 0;
    @NotNull String value = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Dynamic Property Id. */
    public int getDynamicPropertyId() { return this.dynamicPropertyId; }

    /** Returns the Dynamic Property. */
    @NotNull public DynamicProperty getDynamicProperty() { return dynamicProperty.solveOrFail(this.dynamicPropertyId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<DynamicProperty,Integer> parent() { return dynamicProperty; }

    @Override @NotNull public InnerEntitySeq<DynamicValue> siblings() { return getDynamicProperty().getValues(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Value. */
    @NotNull public String getValue() { return this.value; }

    /** Sets the value of the Value. */
    @NotNull public DynamicValue setValue(@NotNull String value) {
        markAsModified();
        this.value = Strings.truncate(value, 100);
        return (DynamicValue) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<DynamicValue,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(DYNAMIC_VALUE); }

    @NotNull public EntityTable<DynamicValue,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DynamicValue,Tuple<Integer,Integer>> table() { return DYNAMIC_VALUE; }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicValue find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DynamicValue findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicValue findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DynamicValue findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicValue find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicValue find(int dynamicPropertyId, int seqId) { return find(Tuple.tuple2(dynamicPropertyId, seqId)); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static DynamicValue find(@NotNull String dynamicProperty, int seqId) { return find(Conversions.toInt(dynamicProperty), seqId); }

    /** 
     * Try to finds an Object of type 'DynamicValue' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicValue findWhere(@NotNull Criteria... condition) { return selectFrom(DYNAMIC_VALUE).where(condition).get(); }

    /** Create a selectFrom(DYNAMIC_VALUE). */
    @NotNull public static Select<DynamicValue> list() { return selectFrom(DYNAMIC_VALUE); }

    /** Performs the given action for each DynamicValue */
    public static void forEach(@Nullable Consumer<DynamicValue> consumer) { selectFrom(DYNAMIC_VALUE).forEach(consumer); }

    /** List instances of 'DynamicValue' with the specified keys. */
    @NotNull public static ImmutableList<DynamicValue> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DynamicValue' with the specified keys. */
    @NotNull public static ImmutableList<DynamicValue> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DynamicValue' that verify the specified condition. */
    @NotNull public static Select<DynamicValue> listWhere(@NotNull Criteria condition) { return selectFrom(DYNAMIC_VALUE).where(condition); }

    @Override @NotNull public final DynamicValue update() { return InnerInstance.super.update(); }

    @Override @NotNull public final DynamicValue insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DynamicValue> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DynamicValue> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return dynamicPropertyId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(dynamicPropertyId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getValue()); }

    @Override @NotNull public String toString() { return "" + getValue(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DynamicValue> rowMapper() { return DYNAMIC_VALUE.metadata().getRowMapper(); }

    @Override public void invalidate() { dynamicProperty.invalidate(); }

}
