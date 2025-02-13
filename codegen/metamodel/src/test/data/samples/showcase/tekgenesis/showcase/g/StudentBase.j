package tekgenesis.showcase.g;

import tekgenesis.showcase.Classroom;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.Gender;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Student;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.ClassroomTable.CLASSROOM;
import static tekgenesis.showcase.g.StudentTable.STUDENT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Student.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class StudentBase
    extends EntityInstanceImpl<Student,Tuple<Integer,Integer>>
    implements InnerInstance<Student,Tuple<Integer,Integer>,Classroom,Integer>
{

    //~ Fields ...................................................................................................................

    int classroomIdKey = 0;
    @NotNull EntityRef<Classroom,Integer> classroom = new EntityRef<>(CLASSROOM, Classroom::getStudents);
    int seqId = 0;
    int dni = 0;
    @NotNull String firstName = "";
    @NotNull String lastName = "";
    int age = 0;
    @NotNull Gender gender = Gender.MALE;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Classroom Id Key. */
    public int getClassroomIdKey() { return this.classroomIdKey; }

    /** Returns the Classroom. */
    @NotNull public Classroom getClassroom() { return classroom.solveOrFail(this.classroomIdKey); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Classroom,Integer> parent() { return classroom; }

    @Override @NotNull public InnerEntitySeq<Student> siblings() { return getClassroom().getStudents(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Dni. */
    public int getDni() { return this.dni; }

    /** Sets the value of the Dni. */
    @NotNull public Student setDni(int dni) {
        markAsModified();
        this.dni = Integers.checkSignedLength("dni", dni, false, 9);
        return (Student) this;
    }

    /** Returns the First Name. */
    @NotNull public String getFirstName() { return this.firstName; }

    /** Sets the value of the First Name. */
    @NotNull public Student setFirstName(@NotNull String firstName) {
        markAsModified();
        this.firstName = Strings.truncate(firstName, 20);
        return (Student) this;
    }

    /** Returns the Last Name. */
    @NotNull public String getLastName() { return this.lastName; }

    /** Sets the value of the Last Name. */
    @NotNull public Student setLastName(@NotNull String lastName) {
        markAsModified();
        this.lastName = Strings.truncate(lastName, 20);
        return (Student) this;
    }

    /** Returns the Age. */
    public int getAge() { return this.age; }

    /** Sets the value of the Age. */
    @NotNull public Student setAge(int age) {
        markAsModified();
        this.age = Integers.checkSignedLength("age", age, false, 9);
        return (Student) this;
    }

    /** Returns the Gender. */
    @NotNull public Gender getGender() { return this.gender; }

    /** Sets the value of the Gender. */
    @NotNull public Student setGender(@NotNull Gender gender) {
        markAsModified();
        this.gender = gender;
        return (Student) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Student,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(STUDENT); }

    @NotNull public EntityTable<Student,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Student,Tuple<Integer,Integer>> table() { return STUDENT; }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Student find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Student findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Student findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Student findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Student find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Student find(int classroomIdKey, int seqId) { return find(Tuple.tuple2(classroomIdKey, seqId)); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Student find(@NotNull String classroom, int seqId) { return find(Conversions.toInt(classroom), seqId); }

    /** 
     * Try to finds an Object of type 'Student' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Student findWhere(@NotNull Criteria... condition) { return selectFrom(STUDENT).where(condition).get(); }

    /** Create a selectFrom(STUDENT). */
    @NotNull public static Select<Student> list() { return selectFrom(STUDENT); }

    /** Performs the given action for each Student */
    public static void forEach(@Nullable Consumer<Student> consumer) { selectFrom(STUDENT).forEach(consumer); }

    /** List instances of 'Student' with the specified keys. */
    @NotNull public static ImmutableList<Student> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Student' with the specified keys. */
    @NotNull public static ImmutableList<Student> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Student' that verify the specified condition. */
    @NotNull public static Select<Student> listWhere(@NotNull Criteria condition) { return selectFrom(STUDENT).where(condition); }

    @Override @NotNull public final Student update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Student insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Student> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Student> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return classroomIdKey + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(classroomIdKey, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getClassroom(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getClassroom() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Student> rowMapper() { return STUDENT.metadata().getRowMapper(); }

    @Override public void invalidate() { classroom.invalidate(); }

}
