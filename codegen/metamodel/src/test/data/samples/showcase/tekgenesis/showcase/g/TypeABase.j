package tekgenesis.showcase.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
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
import tekgenesis.showcase.TypeA;
import static tekgenesis.showcase.g.TypeATable.TYPE_A;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TypeA.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TypeABase
    extends EntityInstanceImpl<TypeA,Integer>
    implements PersistableInstance<TypeA,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull BigDecimal d = BigDecimal.ZERO;
    int i = 0;
    double r = 0.0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the D. */
    @NotNull public BigDecimal getD() { return this.d; }

    /** Sets the value of the D. */
    @NotNull public TypeA setD(@NotNull BigDecimal d) {
        markAsModified();
        this.d = Decimals.scaleAndCheck("d", d, false, 10, 2);
        return (TypeA) this;
    }

    /** Returns the I. */
    public int getI() { return this.i; }

    /** Sets the value of the I. */
    @NotNull public TypeA setI(int i) {
        markAsModified();
        this.i = Integers.checkSignedLength("i", i, false, 9);
        return (TypeA) this;
    }

    /** Returns the R. */
    public double getR() { return this.r; }

    /** Sets the value of the R. */
    @NotNull public TypeA setR(double r) {
        markAsModified();
        this.r = Reals.checkSigned("r", r, false);
        return (TypeA) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TypeA} instance. */
    @NotNull public static TypeA create() { return new TypeA(); }

    @NotNull private static EntityTable<TypeA,Integer> myEntityTable() { return EntityTable.forTable(TYPE_A); }

    @NotNull public EntityTable<TypeA,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TypeA,Integer> table() { return TYPE_A; }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeA find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeA findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeA findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TypeA findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeA find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TypeA' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TypeA findWhere(@NotNull Criteria... condition) { return selectFrom(TYPE_A).where(condition).get(); }

    /** Create a selectFrom(TYPE_A). */
    @NotNull public static Select<TypeA> list() { return selectFrom(TYPE_A); }

    /** Performs the given action for each TypeA */
    public static void forEach(@Nullable Consumer<TypeA> consumer) { selectFrom(TYPE_A).forEach(consumer); }

    /** List instances of 'TypeA' with the specified keys. */
    @NotNull public static ImmutableList<TypeA> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TypeA' with the specified keys. */
    @NotNull public static ImmutableList<TypeA> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TypeA' that verify the specified condition. */
    @NotNull public static Select<TypeA> listWhere(@NotNull Criteria condition) { return selectFrom(TYPE_A).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((TypeA) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeA> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TypeA> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getD()); }

    @Override @NotNull public String toString() { return "" + getD(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TypeA> rowMapper() { return TYPE_A.metadata().getRowMapper(); }

}
