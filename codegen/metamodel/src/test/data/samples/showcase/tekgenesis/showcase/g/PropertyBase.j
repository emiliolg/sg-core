package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.showcase.Property;
import tekgenesis.showcase.PropertyType;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.showcase.ValidValue;
import static tekgenesis.showcase.g.PropertyTable.PROPERTY;
import static tekgenesis.showcase.g.ValidValueTable.VALID_VALUE;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Property.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class PropertyBase
    extends EntityInstanceImpl<Property,Tuple<String,PropertyType>>
    implements PersistableInstance<Property,Tuple<String,PropertyType>>
{

    //~ Fields ...................................................................................................................

    @NotNull String name = "";
    @NotNull PropertyType type = PropertyType.STRING;
    boolean multiple = false;
    @NotNull private InnerEntitySeq<ValidValue> values = createInnerEntitySeq(VALID_VALUE, (Property) this, c -> ((ValidValueBase)c).property);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Type. */
    @NotNull public PropertyType getType() { return this.type; }

    /** Returns true if it is Multiple. */
    public boolean isMultiple() { return this.multiple; }

    /** Sets the value of the Multiple. */
    @NotNull public Property setMultiple(boolean multiple) {
        markAsModified();
        this.multiple = multiple;
        return (Property) this;
    }

    /** Returns the Values. */
    @NotNull public InnerEntitySeq<ValidValue> getValues() { return values; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Property} instance. */
    @NotNull public static Property create(@NotNull String name, @NotNull PropertyType type) {
        final Property result = new Property();
        ((PropertyBase) result).name = Strings.truncate(name, 20);
        ((PropertyBase) result).type = type;
        return result;
    }

    /** 
     * Creates a new {@link Property} instance.
     * Based on the primary key object
     */
    @NotNull public static Property create(@NotNull Tuple<String,PropertyType> key) { return create(key._1(), key._2()); }

    /** 
     * Find (or create if not present) a 'Property' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Property findOrCreate(@NotNull Tuple<String,PropertyType> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'Property' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Property findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'Property' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Property findOrCreate(@NotNull String name, @NotNull PropertyType type) { return findOrCreate(Tuple.tuple2(name, type)); }

    @NotNull private static EntityTable<Property,Tuple<String,PropertyType>> myEntityTable() { return EntityTable.forTable(PROPERTY); }

    @NotNull public EntityTable<Property,Tuple<String,PropertyType>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Property,Tuple<String,PropertyType>> table() { return PROPERTY; }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Property find(@NotNull Tuple<String,PropertyType> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Property findOrFail(@NotNull Tuple<String,PropertyType> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Property findPersisted(@NotNull Tuple<String,PropertyType> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Property findPersistedOrFail(@NotNull Tuple<String,PropertyType> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Property find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Property find(@NotNull String name, @NotNull PropertyType type) { return find(Tuple.tuple2(name, type)); }

    /** 
     * Try to finds an Object of type 'Property' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Property findWhere(@NotNull Criteria... condition) { return selectFrom(PROPERTY).where(condition).get(); }

    /** Create a selectFrom(PROPERTY). */
    @NotNull public static Select<Property> list() { return selectFrom(PROPERTY); }

    /** Performs the given action for each Property */
    public static void forEach(@Nullable Consumer<Property> consumer) { selectFrom(PROPERTY).forEach(consumer); }

    /** List instances of 'Property' with the specified keys. */
    @NotNull public static ImmutableList<Property> list(@Nullable Set<Tuple<String,PropertyType>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Property' with the specified keys. */
    @NotNull public static ImmutableList<Property> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Property' that verify the specified condition. */
    @NotNull public static Select<Property> listWhere(@NotNull Criteria condition) { return selectFrom(PROPERTY).where(condition); }

    @Override @NotNull public final Property update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Property insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Property> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Property> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return Strings.escapeCharOn(name, ':') + ":" + type; }

    @NotNull public Tuple<String,PropertyType> keyObject() { return Tuple.tuple2(name, type); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Property> rowMapper() { return PROPERTY.metadata().getRowMapper(); }

    @Override public void invalidate() { values.invalidate(); }

}
