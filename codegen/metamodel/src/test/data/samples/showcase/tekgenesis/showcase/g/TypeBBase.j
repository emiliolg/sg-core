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
import tekgenesis.showcase.TypeB;
import static tekgenesis.showcase.g.TypeBTable.TYPE_B;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TypeB.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TypeBBase
    extends EntityInstanceImpl<TypeB,Integer>
    implements PersistableInstance<TypeB,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String s = "";
    @NotNull DateTime t = DateTime.EPOCH;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the S. */
    @NotNull public String getS() { return this.s; }

    /** Sets the value of the S. */
    @NotNull public TypeB setS(@NotNull String s) {
        markAsModified();
        this.s = Strings.truncate(s, 60);
        return (TypeB) this;
    }

    /** Returns the T. */
    @NotNull public DateTime getT() { return this.t; }

    /** Sets the value of the T. */
    @NotNull public TypeB setT(@NotNull DateTime t) {
        markAsModified();
        this.t = t;
        return (TypeB) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TypeB} instance. */
    @NotNull public static TypeB create() { return new TypeB(); }

    @NotNull private static EntityTable<TypeB,Integer> myEntityTable() { return EntityTable.forTable(TYPE_B); }

    @NotNull public EntityTable<TypeB,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TypeB,Integer> table() { return TYPE_B; }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeB find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeB findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeB findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeB findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeB find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TypeB' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeB findWhere(@NotNull Criteria... condition) { return selectFrom(TYPE_B).where(condition).get(); }

    /** Create a selectFrom(TYPE_B). */
    @NotNull public static Select<TypeB> list() { return selectFrom(TYPE_B); }

    /** Performs the given action for each TypeB */
    public static void forEach(@Nullable Consumer<TypeB> consumer) { selectFrom(TYPE_B).forEach(consumer); }

    /** List instances of 'TypeB' with the specified keys. */
    @NotNull public static ImmutableList<TypeB> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TypeB' with the specified keys. */
    @NotNull public static ImmutableList<TypeB> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TypeB' that verify the specified condition. */
    @NotNull public static Select<TypeB> listWhere(@NotNull Criteria condition) { return selectFrom(TYPE_B).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((TypeB) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeB> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeB> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getS()); }

    @Override @NotNull public String toString() { return "" + getS(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TypeB> rowMapper() { return TYPE_B.metadata().getRowMapper(); }

}
