package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.MakeTable.MAKE;
import static tekgenesis.showcase.g.ModelTable.MODEL;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Model.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ModelBase
    extends EntityInstanceImpl<Model,Tuple<Integer,Integer>>
    implements InnerInstance<Model,Tuple<Integer,Integer>,Make,Integer>
{

    //~ Fields ...................................................................................................................

    int makeId = 0;
    @NotNull EntityRef<Make,Integer> make = new EntityRef<>(MAKE, Make::getModels);
    int seqId = 0;
    @NotNull String model = "";
    @NotNull DateOnly released = DateOnly.EPOCH;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Make Id. */
    public int getMakeId() { return this.makeId; }

    /** Returns the Make. */
    @NotNull public Make getMake() { return make.solveOrFail(this.makeId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Make,Integer> parent() { return make; }

    @Override @NotNull public InnerEntitySeq<Model> siblings() { return getMake().getModels(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Model. */
    @NotNull public String getModel() { return this.model; }

    /** Sets the value of the Model. */
    @NotNull public Model setModel(@NotNull String model) {
        markAsModified();
        this.model = Strings.truncate(model, 30);
        return (Model) this;
    }

    /** Returns the Released. */
    @NotNull public DateOnly getReleased() { return this.released; }

    /** Sets the value of the Released. */
    @NotNull public Model setReleased(@NotNull DateOnly released) {
        markAsModified();
        this.released = released;
        return (Model) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Model,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(MODEL); }

    @NotNull public EntityTable<Model,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Model,Tuple<Integer,Integer>> table() { return MODEL; }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Model find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Model findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Model findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Model findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Model find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Model find(int makeId, int seqId) { return find(Tuple.tuple2(makeId, seqId)); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Model find(@NotNull String make, int seqId) { return find(Conversions.toInt(make), seqId); }

    /** 
     * Try to finds an Object of type 'Model' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Model findWhere(@NotNull Criteria... condition) { return selectFrom(MODEL).where(condition).get(); }

    /** Create a selectFrom(MODEL). */
    @NotNull public static Select<Model> list() { return selectFrom(MODEL); }

    /** Performs the given action for each Model */
    public static void forEach(@Nullable Consumer<Model> consumer) { selectFrom(MODEL).forEach(consumer); }

    /** List instances of 'Model' with the specified keys. */
    @NotNull public static ImmutableList<Model> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Model' with the specified keys. */
    @NotNull public static ImmutableList<Model> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Model' that verify the specified condition. */
    @NotNull public static Select<Model> listWhere(@NotNull Criteria condition) { return selectFrom(MODEL).where(condition); }

    @Override @NotNull public final Model update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Model insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Model> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Model> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return makeId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(makeId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getModel()); }

    @Override @NotNull public String toString() { return "" + getModel(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Model> rowMapper() { return MODEL.metadata().getRowMapper(); }

    @Override public void invalidate() { make.invalidate(); }

}
