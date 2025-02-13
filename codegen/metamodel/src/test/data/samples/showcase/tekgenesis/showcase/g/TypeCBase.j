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
import tekgenesis.showcase.TypeC;
import static tekgenesis.showcase.g.TypeCTable.TYPE_C;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TypeC.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TypeCBase
    extends EntityInstanceImpl<TypeC,Integer>
    implements PersistableInstance<TypeC,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String a = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the A. */
    @NotNull public String getA() { return this.a; }

    /** Sets the value of the A. */
    @NotNull public TypeC setA(@NotNull String a) {
        markAsModified();
        this.a = Strings.truncate(a, 60);
        return (TypeC) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TypeC} instance. */
    @NotNull public static TypeC create() { return new TypeC(); }

    @NotNull private static EntityTable<TypeC,Integer> myEntityTable() { return EntityTable.forTable(TYPE_C); }

    @NotNull public EntityTable<TypeC,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TypeC,Integer> table() { return TYPE_C; }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeC find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeC findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeC findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeC findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeC find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TypeC' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeC findWhere(@NotNull Criteria... condition) { return selectFrom(TYPE_C).where(condition).get(); }

    /** Create a selectFrom(TYPE_C). */
    @NotNull public static Select<TypeC> list() { return selectFrom(TYPE_C); }

    /** Performs the given action for each TypeC */
    public static void forEach(@Nullable Consumer<TypeC> consumer) { selectFrom(TYPE_C).forEach(consumer); }

    /** List instances of 'TypeC' with the specified keys. */
    @NotNull public static ImmutableList<TypeC> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TypeC' with the specified keys. */
    @NotNull public static ImmutableList<TypeC> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TypeC' that verify the specified condition. */
    @NotNull public static Select<TypeC> listWhere(@NotNull Criteria condition) { return selectFrom(TYPE_C).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((TypeC) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeC> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeC> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getA()); }

    @Override @NotNull public String toString() { return "" + getA(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TypeC> rowMapper() { return TYPE_C.metadata().getRowMapper(); }

}
