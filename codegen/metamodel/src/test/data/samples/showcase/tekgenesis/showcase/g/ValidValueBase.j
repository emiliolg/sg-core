package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
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
import tekgenesis.showcase.Property;
import tekgenesis.showcase.PropertyType;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import tekgenesis.showcase.ValidValue;
import static tekgenesis.showcase.g.PropertyTable.PROPERTY;
import static tekgenesis.showcase.g.ValidValueTable.VALID_VALUE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ValidValue.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ValidValueBase
    extends EntityInstanceImpl<ValidValue,Tuple3<String,PropertyType,Integer>>
    implements InnerInstance<ValidValue,Tuple3<String,PropertyType,Integer>,Property,Tuple<String,PropertyType>>
{

    //~ Fields ...................................................................................................................

    @NotNull String propertyName = "";
    @NotNull PropertyType propertyType = PropertyType.STRING;
    @NotNull EntityRef<Property,Tuple<String,PropertyType>> property = new EntityRef<>(PROPERTY, Property::getValues);
    int seqId = 0;
    @NotNull String value = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Property Name. */
    @NotNull public String getPropertyName() { return this.propertyName; }

    /** Returns the Property Type. */
    @NotNull public PropertyType getPropertyType() { return this.propertyType; }

    /** Returns the Property. */
    @NotNull public Property getProperty() {
        return property.solveOrFail(Tuple.tuple2(this.propertyName, this.propertyType));
    }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Property,Tuple<String,PropertyType>> parent() { return property; }

    @Override @NotNull public InnerEntitySeq<ValidValue> siblings() { return getProperty().getValues(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Value. */
    @NotNull public String getValue() { return this.value; }

    /** Sets the value of the Value. */
    @NotNull public ValidValue setValue(@NotNull String value) {
        markAsModified();
        this.value = Strings.truncate(value, 100);
        return (ValidValue) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<ValidValue,Tuple3<String,PropertyType,Integer>> myEntityTable() { return EntityTable.forTable(VALID_VALUE); }

    @NotNull public EntityTable<ValidValue,Tuple3<String,PropertyType,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ValidValue,Tuple3<String,PropertyType,Integer>> table() { return VALID_VALUE; }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ValidValue find(@NotNull Tuple3<String,PropertyType,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ValidValue findOrFail(@NotNull Tuple3<String,PropertyType,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ValidValue findPersisted(@NotNull Tuple3<String,PropertyType,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ValidValue findPersistedOrFail(@NotNull Tuple3<String,PropertyType,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ValidValue find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ValidValue find(@NotNull String propertyName, @NotNull PropertyType propertyType, int seqId) { return find(Tuple.tuple(propertyName, propertyType, seqId)); }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static ValidValue find(@NotNull String property, int seqId) {
        final String[] propertyParts = Strings.splitToArray(property, 2);
        return find(propertyParts[0], PropertyType.valueOf(propertyParts[1]), seqId);
    }

    /** 
     * Try to finds an Object of type 'ValidValue' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ValidValue findWhere(@NotNull Criteria... condition) { return selectFrom(VALID_VALUE).where(condition).get(); }

    /** Create a selectFrom(VALID_VALUE). */
    @NotNull public static Select<ValidValue> list() { return selectFrom(VALID_VALUE); }

    /** Performs the given action for each ValidValue */
    public static void forEach(@Nullable Consumer<ValidValue> consumer) { selectFrom(VALID_VALUE).forEach(consumer); }

    /** List instances of 'ValidValue' with the specified keys. */
    @NotNull public static ImmutableList<ValidValue> list(@Nullable Set<Tuple3<String,PropertyType,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ValidValue' with the specified keys. */
    @NotNull public static ImmutableList<ValidValue> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ValidValue' that verify the specified condition. */
    @NotNull public static Select<ValidValue> listWhere(@NotNull Criteria condition) { return selectFrom(VALID_VALUE).where(condition); }

    @Override @NotNull public final ValidValue update() { return InnerInstance.super.update(); }

    @Override @NotNull public final ValidValue insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ValidValue> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ValidValue> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(propertyName, ':') + ":" + propertyType + ":" + seqId;
    }

    @NotNull public Tuple3<String,PropertyType,Integer> keyObject() { return Tuple.tuple(propertyName, propertyType, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getValue()); }

    @Override @NotNull public String toString() { return "" + getValue(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ValidValue> rowMapper() { return VALID_VALUE.metadata().getRowMapper(); }

    @Override public void invalidate() { property.invalidate(); }

}
