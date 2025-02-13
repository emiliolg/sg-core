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
import tekgenesis.common.core.Integers;
import tekgenesis.showcase.NamedItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.NamedItemTable.NAMED_ITEM;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: NamedItem.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class NamedItemBase
    extends EntityInstanceImpl<NamedItem,Integer>
    implements PersistableInstance<NamedItem,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull String name = "";
    @NotNull String color = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public NamedItem setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 20);
        return (NamedItem) this;
    }

    /** Returns the Color. */
    @NotNull public String getColor() { return this.color; }

    /** Sets the value of the Color. */
    @NotNull public NamedItem setColor(@NotNull String color) {
        markAsModified();
        this.color = Strings.truncate(color, 30);
        return (NamedItem) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link NamedItem} instance. */
    @NotNull public static NamedItem create(int idKey) {
        final NamedItem result = new NamedItem();
        ((NamedItemBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'NamedItem' in the database.
     * Identified by the primary key.
     */
    @NotNull public static NamedItem findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'NamedItem' in the database.
     * Identified by the primary key.
     */
    @NotNull public static NamedItem findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<NamedItem,Integer> myEntityTable() { return EntityTable.forTable(NAMED_ITEM); }

    @NotNull public EntityTable<NamedItem,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<NamedItem,Integer> table() { return NAMED_ITEM; }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static NamedItem find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static NamedItem findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static NamedItem findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static NamedItem findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static NamedItem find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'NamedItem' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static NamedItem findWhere(@NotNull Criteria... condition) { return selectFrom(NAMED_ITEM).where(condition).get(); }

    /** Create a selectFrom(NAMED_ITEM). */
    @NotNull public static Select<NamedItem> list() { return selectFrom(NAMED_ITEM); }

    /** Performs the given action for each NamedItem */
    public static void forEach(@Nullable Consumer<NamedItem> consumer) { selectFrom(NAMED_ITEM).forEach(consumer); }

    /** List instances of 'NamedItem' with the specified keys. */
    @NotNull public static ImmutableList<NamedItem> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'NamedItem' with the specified keys. */
    @NotNull public static ImmutableList<NamedItem> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'NamedItem' that verify the specified condition. */
    @NotNull public static Select<NamedItem> listWhere(@NotNull Criteria condition) { return selectFrom(NAMED_ITEM).where(condition); }

    @Override @NotNull public final NamedItem update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final NamedItem insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<NamedItem> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<NamedItem> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<NamedItem> rowMapper() { return NAMED_ITEM.metadata().getRowMapper(); }

}
