package tekgenesis.showcase.g;

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
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.showcase.MyProp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.PropertyType;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TextShowcase;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.MyPropTable.MY_PROP;
import static tekgenesis.showcase.g.TextShowcaseTable.TEXT_SHOWCASE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: MyProp.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class MyPropBase
    extends EntityInstanceImpl<MyProp,Tuple<Integer,Integer>>
    implements InnerInstance<MyProp,Tuple<Integer,Integer>,TextShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    int textShowcaseIdKey = 0;
    @NotNull EntityRef<TextShowcase,Integer> textShowcase = new EntityRef<>(TEXT_SHOWCASE, TextShowcase::getProp);
    int seqId = 0;
    @NotNull PropertyType type = PropertyType.STRING;
    @NotNull String value = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Text Showcase Id Key. */
    public int getTextShowcaseIdKey() { return this.textShowcaseIdKey; }

    /** Returns the Text Showcase. */
    @NotNull public TextShowcase getTextShowcase() { return textShowcase.solveOrFail(this.textShowcaseIdKey); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<TextShowcase,Integer> parent() { return textShowcase; }

    @Override @NotNull public InnerEntitySeq<MyProp> siblings() { return getTextShowcase().getProp(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Type. */
    @NotNull public PropertyType getType() { return this.type; }

    /** Sets the value of the Type. */
    @NotNull public MyProp setType(@NotNull PropertyType type) {
        markAsModified();
        this.type = type;
        return (MyProp) this;
    }

    /** Returns the Value. */
    @NotNull public String getValue() { return this.value; }

    /** Sets the value of the Value. */
    @NotNull public MyProp setValue(@NotNull String value) {
        markAsModified();
        this.value = Strings.truncate(value, 20);
        return (MyProp) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<MyProp,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(MY_PROP); }

    @NotNull public EntityTable<MyProp,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<MyProp,Tuple<Integer,Integer>> table() { return MY_PROP; }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static MyProp find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static MyProp findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static MyProp findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static MyProp findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static MyProp find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static MyProp find(int textShowcaseIdKey, int seqId) { return find(Tuple.tuple2(textShowcaseIdKey, seqId)); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static MyProp find(@NotNull String textShowcase, int seqId) { return find(Conversions.toInt(textShowcase), seqId); }

    /** 
     * Try to finds an Object of type 'MyProp' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static MyProp findWhere(@NotNull Criteria... condition) { return selectFrom(MY_PROP).where(condition).get(); }

    /** Create a selectFrom(MY_PROP). */
    @NotNull public static Select<MyProp> list() { return selectFrom(MY_PROP); }

    /** Performs the given action for each MyProp */
    public static void forEach(@Nullable Consumer<MyProp> consumer) { selectFrom(MY_PROP).forEach(consumer); }

    /** List instances of 'MyProp' with the specified keys. */
    @NotNull public static ImmutableList<MyProp> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'MyProp' with the specified keys. */
    @NotNull public static ImmutableList<MyProp> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'MyProp' that verify the specified condition. */
    @NotNull public static Select<MyProp> listWhere(@NotNull Criteria condition) { return selectFrom(MY_PROP).where(condition); }

    @Override @NotNull public final MyProp update() { return InnerInstance.super.update(); }

    @Override @NotNull public final MyProp insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<MyProp> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<MyProp> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return textShowcaseIdKey + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(textShowcaseIdKey, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getTextShowcase(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getTextShowcase() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<MyProp> rowMapper() { return MY_PROP.metadata().getRowMapper(); }

    @Override public void invalidate() { textShowcase.invalidate(); }

}
