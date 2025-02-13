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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.SuggestedPerson;
import static tekgenesis.showcase.g.SuggestedPersonTable.SUGGESTED_PERSON;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: SuggestedPerson.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class SuggestedPersonBase
    extends EntityInstanceImpl<SuggestedPerson,Integer>
    implements PersistableInstance<SuggestedPerson,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @Nullable String lastName = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public SuggestedPerson setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (SuggestedPerson) this;
    }

    /** Returns the Last Name. */
    @Nullable public String getLastName() { return this.lastName; }

    /** Sets the value of the Last Name. */
    @NotNull public SuggestedPerson setLastName(@Nullable String lastName) {
        markAsModified();
        this.lastName = Strings.truncate(lastName, 30);
        return (SuggestedPerson) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link SuggestedPerson} instance. */
    @NotNull public static SuggestedPerson create() { return new SuggestedPerson(); }

    @NotNull private static EntityTable<SuggestedPerson,Integer> myEntityTable() { return EntityTable.forTable(SUGGESTED_PERSON); }

    @NotNull public EntityTable<SuggestedPerson,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<SuggestedPerson,Integer> table() { return SUGGESTED_PERSON; }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SuggestedPerson find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SuggestedPerson findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SuggestedPerson findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SuggestedPerson findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SuggestedPerson find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'SuggestedPerson' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SuggestedPerson findWhere(@NotNull Criteria... condition) { return selectFrom(SUGGESTED_PERSON).where(condition).get(); }

    /** Create a selectFrom(SUGGESTED_PERSON). */
    @NotNull public static Select<SuggestedPerson> list() { return selectFrom(SUGGESTED_PERSON); }

    /** Performs the given action for each SuggestedPerson */
    public static void forEach(@Nullable Consumer<SuggestedPerson> consumer) { selectFrom(SUGGESTED_PERSON).forEach(consumer); }

    /** List instances of 'SuggestedPerson' with the specified keys. */
    @NotNull public static ImmutableList<SuggestedPerson> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'SuggestedPerson' with the specified keys. */
    @NotNull public static ImmutableList<SuggestedPerson> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'SuggestedPerson' that verify the specified condition. */
    @NotNull public static Select<SuggestedPerson> listWhere(@NotNull Criteria condition) { return selectFrom(SUGGESTED_PERSON).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((SuggestedPerson) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SuggestedPerson> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SuggestedPerson> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName(), getLastName()); }

    @Override @NotNull public String toString() {
        return "" + getName() + (getLastName()==null ? "" : " " + getLastName());
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<SuggestedPerson> rowMapper() { return SUGGESTED_PERSON.metadata().getRowMapper(); }

}
