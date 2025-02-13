package tekgenesis.showcase.g;

import tekgenesis.showcase.Classroom;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Student;
import static tekgenesis.showcase.g.ClassroomTable.CLASSROOM;
import static tekgenesis.showcase.g.StudentTable.STUDENT;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Classroom.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ClassroomBase
    extends EntityInstanceImpl<Classroom,Integer>
    implements PersistableInstance<Classroom,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull String room = "";
    @NotNull private InnerEntitySeq<Student> students = createInnerEntitySeq(STUDENT, (Classroom) this, c -> ((StudentBase)c).classroom);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Room. */
    @NotNull public String getRoom() { return this.room; }

    /** Sets the value of the Room. */
    @NotNull public Classroom setRoom(@NotNull String room) {
        markAsModified();
        this.room = Strings.truncate(room, 4);
        return (Classroom) this;
    }

    /** Returns the Students. */
    @NotNull public InnerEntitySeq<Student> getStudents() { return students; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Classroom} instance. */
    @NotNull public static Classroom create(int idKey) {
        final Classroom result = new Classroom();
        ((ClassroomBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Classroom' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Classroom findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'Classroom' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Classroom findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<Classroom,Integer> myEntityTable() { return EntityTable.forTable(CLASSROOM); }

    @NotNull public EntityTable<Classroom,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Classroom,Integer> table() { return CLASSROOM; }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Classroom find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Classroom findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Classroom findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Classroom findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Classroom find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Classroom' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Classroom findWhere(@NotNull Criteria... condition) { return selectFrom(CLASSROOM).where(condition).get(); }

    /** Create a selectFrom(CLASSROOM). */
    @NotNull public static Select<Classroom> list() { return selectFrom(CLASSROOM); }

    /** Performs the given action for each Classroom */
    public static void forEach(@Nullable Consumer<Classroom> consumer) { selectFrom(CLASSROOM).forEach(consumer); }

    /** List instances of 'Classroom' with the specified keys. */
    @NotNull public static ImmutableList<Classroom> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Classroom' with the specified keys. */
    @NotNull public static ImmutableList<Classroom> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Classroom' that verify the specified condition. */
    @NotNull public static Select<Classroom> listWhere(@NotNull Criteria condition) { return selectFrom(CLASSROOM).where(condition); }

    @Override @NotNull public final Classroom update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Classroom insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Classroom> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Classroom> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Classroom> rowMapper() { return CLASSROOM.metadata().getRowMapper(); }

    @Override public void invalidate() { students.invalidate(); }

}
