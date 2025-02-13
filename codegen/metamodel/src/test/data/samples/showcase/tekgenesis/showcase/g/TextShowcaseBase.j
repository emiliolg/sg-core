package tekgenesis.showcase.g;

import java.util.function.Consumer;
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
import tekgenesis.common.core.Integers;
import tekgenesis.showcase.MyProp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.Options;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TextShowcase;
import static tekgenesis.showcase.g.MyPropTable.MY_PROP;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;
import static tekgenesis.showcase.g.TextShowcaseTable.TEXT_SHOWCASE;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TextShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TextShowcaseBase
    extends EntityInstanceImpl<TextShowcase,Integer>
    implements PersistableInstance<TextShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull String txt = "";
    @NotNull DateOnly date = DateOnly.EPOCH;
    boolean bool = false;
    @NotNull Options option = Options.OPTION1;
    @NotNull String entityName = "";
    @NotNull EntityRef<SimpleEntity,String> entity = new EntityRef<>(SIMPLE_ENTITY);
    @NotNull private InnerEntitySeq<MyProp> prop = createInnerEntitySeq(MY_PROP, (TextShowcase) this, c -> ((MyPropBase)c).textShowcase);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Txt. */
    @NotNull public String getTxt() { return this.txt; }

    /** Sets the value of the Txt. */
    @NotNull public TextShowcase setTxt(@NotNull String txt) {
        markAsModified();
        this.txt = Strings.truncate(txt, 20);
        return (TextShowcase) this;
    }

    /** Returns the Date. */
    @NotNull public DateOnly getDate() { return this.date; }

    /** Sets the value of the Date. */
    @NotNull public TextShowcase setDate(@NotNull DateOnly date) {
        markAsModified();
        this.date = date;
        return (TextShowcase) this;
    }

    /** Returns true if it is Bool. */
    public boolean isBool() { return this.bool; }

    /** Sets the value of the Bool. */
    @NotNull public TextShowcase setBool(boolean bool) {
        markAsModified();
        this.bool = bool;
        return (TextShowcase) this;
    }

    /** Returns the Option. */
    @NotNull public Options getOption() { return this.option; }

    /** Sets the value of the Option. */
    @NotNull public TextShowcase setOption(@NotNull Options option) {
        markAsModified();
        this.option = option;
        return (TextShowcase) this;
    }

    /** Returns the Entity Name. */
    @NotNull public String getEntityName() { return this.entityName; }

    /** Returns the Entity. */
    @NotNull public SimpleEntity getEntity() { return entity.solveOrFail(this.entityName); }

    /** Sets the value of the Entity Name. */
    @NotNull public TextShowcase setEntityName(@NotNull String entityName) {
        entity.invalidate();
        this.entityName = entityName;
        return (TextShowcase) this;
    }

    /** Sets the value of the Entity. */
    @NotNull public TextShowcase setEntity(@NotNull SimpleEntity entity) {
        this.entity.set(entity);
        this.entityName = entity.getName();
        return (TextShowcase) this;
    }

    /** Returns the Prop. */
    @NotNull public InnerEntitySeq<MyProp> getProp() { return prop; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TextShowcase} instance. */
    @NotNull public static TextShowcase create(int idKey) {
        final TextShowcase result = new TextShowcase();
        ((TextShowcaseBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'TextShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static TextShowcase findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'TextShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static TextShowcase findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<TextShowcase,Integer> myEntityTable() { return EntityTable.forTable(TEXT_SHOWCASE); }

    @NotNull public EntityTable<TextShowcase,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TextShowcase,Integer> table() { return TEXT_SHOWCASE; }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextShowcase find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TextShowcase findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextShowcase findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TextShowcase findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextShowcase find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TextShowcase' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextShowcase findWhere(@NotNull Criteria... condition) { return selectFrom(TEXT_SHOWCASE).where(condition).get(); }

    /** Create a selectFrom(TEXT_SHOWCASE). */
    @NotNull public static Select<TextShowcase> list() { return selectFrom(TEXT_SHOWCASE); }

    /** Performs the given action for each TextShowcase */
    public static void forEach(@Nullable Consumer<TextShowcase> consumer) { selectFrom(TEXT_SHOWCASE).forEach(consumer); }

    /** List instances of 'TextShowcase' with the specified keys. */
    @NotNull public static ImmutableList<TextShowcase> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TextShowcase' with the specified keys. */
    @NotNull public static ImmutableList<TextShowcase> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TextShowcase' that verify the specified condition. */
    @NotNull public static Select<TextShowcase> listWhere(@NotNull Criteria condition) { return selectFrom(TEXT_SHOWCASE).where(condition); }

    @Override @NotNull public final TextShowcase update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final TextShowcase insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TextShowcase> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TextShowcase> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TextShowcase> rowMapper() { return TEXT_SHOWCASE.metadata().getRowMapper(); }

    @Override public void invalidate() {
        prop.invalidate();
        entity.invalidate();
    }

}
