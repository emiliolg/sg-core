package tekgenesis.showcase.g;

import java.util.function.Consumer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.showcase.Property;
import tekgenesis.showcase.PropertyType;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.DynamicPropertyTable.DYNAMIC_PROPERTY;
import static tekgenesis.showcase.g.DynamicValueTable.DYNAMIC_VALUE;
import static tekgenesis.showcase.g.PropertyTable.PROPERTY;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DynamicProperty.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DynamicPropertyBase
    extends EntityInstanceImpl<DynamicProperty,Integer>
    implements PersistableInstance<DynamicProperty,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String propertyName = "";
    @NotNull PropertyType propertyType = PropertyType.STRING;
    @NotNull EntityRef<Property,Tuple<String,PropertyType>> property = new EntityRef<>(PROPERTY);
    @NotNull private InnerEntitySeq<DynamicValue> values = createInnerEntitySeq(DYNAMIC_VALUE, (DynamicProperty) this, c -> ((DynamicValueBase)c).dynamicProperty);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Property Name. */
    @NotNull public String getPropertyName() { return this.propertyName; }

    /** Returns the Property Type. */
    @NotNull public PropertyType getPropertyType() { return this.propertyType; }

    /** Returns the Property. */
    @NotNull public Property getProperty() {
        return property.solveOrFail(Tuple.tuple2(this.propertyName, this.propertyType));
    }

    /** Sets the value of the Property Name. */
    @NotNull public DynamicProperty setPropertyName(@NotNull String propertyName) {
        property.invalidate();
        this.propertyName = propertyName;
        return (DynamicProperty) this;
    }

    /** Sets the value of the Property Type. */
    @NotNull public DynamicProperty setPropertyType(@NotNull PropertyType propertyType) {
        property.invalidate();
        this.propertyType = propertyType;
        return (DynamicProperty) this;
    }

    /** Sets the value of the Property. */
    @NotNull public DynamicProperty setProperty(@NotNull Property property) {
        this.property.set(property);
        this.propertyName = property.getName();
        this.propertyType = property.getType();
        return (DynamicProperty) this;
    }

    /** Returns the Values. */
    @NotNull public InnerEntitySeq<DynamicValue> getValues() { return values; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link DynamicProperty} instance. */
    @NotNull public static DynamicProperty create() { return new DynamicProperty(); }

    @NotNull private static EntityTable<DynamicProperty,Integer> myEntityTable() { return EntityTable.forTable(DYNAMIC_PROPERTY); }

    @NotNull public EntityTable<DynamicProperty,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DynamicProperty,Integer> table() { return DYNAMIC_PROPERTY; }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicProperty find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DynamicProperty findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicProperty findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DynamicProperty findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicProperty find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DynamicProperty' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DynamicProperty findWhere(@NotNull Criteria... condition) { return selectFrom(DYNAMIC_PROPERTY).where(condition).get(); }

    /** Create a selectFrom(DYNAMIC_PROPERTY). */
    @NotNull public static Select<DynamicProperty> list() { return selectFrom(DYNAMIC_PROPERTY); }

    /** Performs the given action for each DynamicProperty */
    public static void forEach(@Nullable Consumer<DynamicProperty> consumer) { selectFrom(DYNAMIC_PROPERTY).forEach(consumer); }

    /** List instances of 'DynamicProperty' with the specified keys. */
    @NotNull public static ImmutableList<DynamicProperty> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DynamicProperty' with the specified keys. */
    @NotNull public static ImmutableList<DynamicProperty> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DynamicProperty' that verify the specified condition. */
    @NotNull public static Select<DynamicProperty> listWhere(@NotNull Criteria condition) { return selectFrom(DYNAMIC_PROPERTY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((DynamicProperty) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DynamicProperty> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DynamicProperty> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getProperty()); }

    @Override @NotNull public String toString() { return "" + getProperty(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DynamicProperty> rowMapper() { return DYNAMIC_PROPERTY.metadata().getRowMapper(); }

    @Override public void invalidate() {
        values.invalidate();
        property.invalidate();
    }

}
