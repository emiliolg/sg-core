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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.common.core.Reals;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TableEntity;
import static tekgenesis.showcase.g.TableEntityTable.TABLE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TableEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TableEntityBase
    extends EntityInstanceImpl<TableEntity,Integer>
    implements PersistableInstance<TableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String moe = "";
    int larry = 0;
    double curly = 0.0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Moe. */
    @NotNull public String getMoe() { return this.moe; }

    /** Sets the value of the Moe. */
    @NotNull public TableEntity setMoe(@NotNull String moe) {
        markAsModified();
        this.moe = Strings.truncate(moe, 255);
        return (TableEntity) this;
    }

    /** Returns the Larry. */
    public int getLarry() { return this.larry; }

    /** Sets the value of the Larry. */
    @NotNull public TableEntity setLarry(int larry) {
        markAsModified();
        this.larry = Integers.checkSignedLength("larry", larry, false, 9);
        return (TableEntity) this;
    }

    /** Returns the Curly. */
    public double getCurly() { return this.curly; }

    /** Sets the value of the Curly. */
    @NotNull public TableEntity setCurly(double curly) {
        markAsModified();
        this.curly = Reals.checkSigned("curly", curly, false);
        return (TableEntity) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TableEntity} instance. */
    @NotNull public static TableEntity create() { return new TableEntity(); }

    @NotNull private static EntityTable<TableEntity,Integer> myEntityTable() { return EntityTable.forTable(TABLE_ENTITY); }

    @NotNull public EntityTable<TableEntity,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TableEntity,Integer> table() { return TABLE_ENTITY; }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TableEntity find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TableEntity findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TableEntity findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TableEntity findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TableEntity find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TableEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TableEntity findWhere(@NotNull Criteria... condition) { return selectFrom(TABLE_ENTITY).where(condition).get(); }

    /** Create a selectFrom(TABLE_ENTITY). */
    @NotNull public static Select<TableEntity> list() { return selectFrom(TABLE_ENTITY); }

    /** Performs the given action for each TableEntity */
    public static void forEach(@Nullable Consumer<TableEntity> consumer) { selectFrom(TABLE_ENTITY).forEach(consumer); }

    /** List instances of 'TableEntity' with the specified keys. */
    @NotNull public static ImmutableList<TableEntity> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TableEntity' with the specified keys. */
    @NotNull public static ImmutableList<TableEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TableEntity' that verify the specified condition. */
    @NotNull public static Select<TableEntity> listWhere(@NotNull Criteria condition) { return selectFrom(TABLE_ENTITY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((TableEntity) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TableEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TableEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TableEntity> rowMapper() { return TABLE_ENTITY.metadata().getRowMapper(); }

}
