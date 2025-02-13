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
import tekgenesis.common.core.Integers;
import tekgenesis.showcase.Listing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.showcase.ViewData;
import static tekgenesis.showcase.g.ListingTable.LISTING;
import static tekgenesis.showcase.g.ViewDataTable.VIEW_DATA;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ViewData.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ViewDataBase
    extends EntityInstanceImpl<ViewData,Integer>
    implements PersistableInstance<ViewData,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    int current = 0;
    @NotNull private InnerEntitySeq<Listing> items = createInnerEntitySeq(LISTING, (ViewData) this, c -> ((ListingBase)c).viewData);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Current. */
    public int getCurrent() { return this.current; }

    /** Sets the value of the Current. */
    @NotNull public ViewData setCurrent(int current) {
        markAsModified();
        this.current = Integers.checkSignedLength("current", current, false, 9);
        return (ViewData) this;
    }

    /** Returns the Items. */
    @NotNull public InnerEntitySeq<Listing> getItems() { return items; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ViewData} instance. */
    @NotNull public static ViewData create() { return new ViewData(); }

    @NotNull private static EntityTable<ViewData,Integer> myEntityTable() { return EntityTable.forTable(VIEW_DATA); }

    @NotNull public EntityTable<ViewData,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ViewData,Integer> table() { return VIEW_DATA; }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ViewData find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ViewData findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ViewData findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ViewData findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ViewData find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ViewData' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ViewData findWhere(@NotNull Criteria... condition) { return selectFrom(VIEW_DATA).where(condition).get(); }

    /** Create a selectFrom(VIEW_DATA). */
    @NotNull public static Select<ViewData> list() { return selectFrom(VIEW_DATA); }

    /** Performs the given action for each ViewData */
    public static void forEach(@Nullable Consumer<ViewData> consumer) { selectFrom(VIEW_DATA).forEach(consumer); }

    /** List instances of 'ViewData' with the specified keys. */
    @NotNull public static ImmutableList<ViewData> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ViewData' with the specified keys. */
    @NotNull public static ImmutableList<ViewData> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ViewData' that verify the specified condition. */
    @NotNull public static Select<ViewData> listWhere(@NotNull Criteria condition) { return selectFrom(VIEW_DATA).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((ViewData) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ViewData> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ViewData> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ViewData> rowMapper() { return VIEW_DATA.metadata().getRowMapper(); }

    @Override public void invalidate() { items.invalidate(); }

}
