package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.showcase.Country;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.MakeTable.MAKE;
import static tekgenesis.showcase.g.ModelTable.MODEL;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Make.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class MakeBase
    extends EntityInstanceImpl<Make,Integer>
    implements PersistableInstance<Make,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull Country origin = Country.ARGENTINA;
    @NotNull private InnerEntitySeq<Model> models = createInnerEntitySeq(MODEL, (Make) this, c -> ((ModelBase)c).make);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Make setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (Make) this;
    }

    /** Returns the Origin. */
    @NotNull public Country getOrigin() { return this.origin; }

    /** Sets the value of the Origin. */
    @NotNull public Make setOrigin(@NotNull Country origin) {
        markAsModified();
        this.origin = origin;
        return (Make) this;
    }

    /** Returns the Models. */
    @NotNull public InnerEntitySeq<Model> getModels() { return models; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Make} instance. */
    @NotNull public static Make create() { return new Make(); }

    @NotNull private static EntityTable<Make,Integer> myEntityTable() { return EntityTable.forTable(MAKE); }

    @NotNull public EntityTable<Make,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Make,Integer> table() { return MAKE; }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Make find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Make findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Make findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Make findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Make find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Make' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Make findWhere(@NotNull Criteria... condition) { return selectFrom(MAKE).where(condition).get(); }

    /** Create a selectFrom(MAKE). */
    @NotNull public static Select<Make> list() { return selectFrom(MAKE); }

    /** Performs the given action for each Make */
    public static void forEach(@Nullable Consumer<Make> consumer) { selectFrom(MAKE).forEach(consumer); }

    /** List instances of 'Make' with the specified keys. */
    @NotNull public static ImmutableList<Make> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Make' with the specified keys. */
    @NotNull public static ImmutableList<Make> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Make' that verify the specified condition. */
    @NotNull public static Select<Make> listWhere(@NotNull Criteria condition) { return selectFrom(MAKE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Make) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Make> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Make> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Make> rowMapper() { return MAKE.metadata().getRowMapper(); }

    @Override public void invalidate() { models.invalidate(); }

}
