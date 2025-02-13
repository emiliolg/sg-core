package tekgenesis.showcase.g;

import tekgenesis.persistence.AuditableInstance;
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
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Synchronous;
import static tekgenesis.showcase.g.SynchronousTable.SYNCHRONOUS;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Synchronous.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class SynchronousBase
    extends EntityInstanceImpl<Synchronous,Integer>
    implements PersistableInstance<Synchronous,Integer>, AuditableInstance
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @NotNull DateTime creationTime = DateTime.EPOCH;
    @Nullable String creationUser = null;
    @Nullable String updateUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Synchronous setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 40);
        return (Synchronous) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Creation Time. */
    @NotNull public DateTime getCreationTime() { return this.creationTime; }

    /** Returns the Creation User. */
    @Nullable public String getCreationUser() { return this.creationUser; }

    /** Returns the Update User. */
    @Nullable public String getUpdateUser() { return this.updateUser; }

    /** Creates a new {@link Synchronous} instance. */
    @NotNull public static Synchronous create() { return new Synchronous(); }

    @NotNull private static EntityTable<Synchronous,Integer> myEntityTable() { return EntityTable.forTable(SYNCHRONOUS); }

    @NotNull public EntityTable<Synchronous,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Synchronous,Integer> table() { return SYNCHRONOUS; }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Synchronous find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Synchronous findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Synchronous findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Synchronous findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Synchronous find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Synchronous' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Synchronous findWhere(@NotNull Criteria... condition) { return selectFrom(SYNCHRONOUS).where(condition).get(); }

    /** Create a selectFrom(SYNCHRONOUS). */
    @NotNull public static Select<Synchronous> list() { return selectFrom(SYNCHRONOUS); }

    /** Performs the given action for each Synchronous */
    public static void forEach(@Nullable Consumer<Synchronous> consumer) { selectFrom(SYNCHRONOUS).forEach(consumer); }

    /** List instances of 'Synchronous' with the specified keys. */
    @NotNull public static ImmutableList<Synchronous> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Synchronous' with the specified keys. */
    @NotNull public static ImmutableList<Synchronous> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Synchronous' that verify the specified condition. */
    @NotNull public static Select<Synchronous> listWhere(@NotNull Criteria condition) { return selectFrom(SYNCHRONOUS).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Synchronous) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Synchronous> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Synchronous> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** Finds the instance */
    @Nullable public static Synchronous findByName(@NotNull String name) { return myEntityTable().findByKey(0, name); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Synchronous> rowMapper() { return SYNCHRONOUS.metadata().getRowMapper(); }

}
