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
import tekgenesis.showcase.Label;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.LabelTable.LABEL;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Label.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class LabelBase
    extends EntityInstanceImpl<Label,Integer>
    implements PersistableInstance<Label,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String withLabel1 = "";
    @NotNull String withLabel2 = "";
    @NotNull String noLabel1 = "";
    @NotNull String noLabel2 = "";
    @NotNull String noLabel3 = "";
    @NotNull String noLabel4 = "";
    @NotNull String some = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the With Label1. */
    @NotNull public String getWithLabel1() { return this.withLabel1; }

    /** Sets the value of the With Label1. */
    @NotNull public Label setWithLabel1(@NotNull String withLabel1) {
        markAsModified();
        this.withLabel1 = Strings.truncate(withLabel1, 255);
        return (Label) this;
    }

    /** Returns the With Label2. */
    @NotNull public String getWithLabel2() { return this.withLabel2; }

    /** Sets the value of the With Label2. */
    @NotNull public Label setWithLabel2(@NotNull String withLabel2) {
        markAsModified();
        this.withLabel2 = Strings.truncate(withLabel2, 255);
        return (Label) this;
    }

    /** Returns the No Label1. */
    @NotNull public String getNoLabel1() { return this.noLabel1; }

    /** Sets the value of the No Label1. */
    @NotNull public Label setNoLabel1(@NotNull String noLabel1) {
        markAsModified();
        this.noLabel1 = Strings.truncate(noLabel1, 255);
        return (Label) this;
    }

    /** Returns the No Label2. */
    @NotNull public String getNoLabel2() { return this.noLabel2; }

    /** Sets the value of the No Label2. */
    @NotNull public Label setNoLabel2(@NotNull String noLabel2) {
        markAsModified();
        this.noLabel2 = Strings.truncate(noLabel2, 255);
        return (Label) this;
    }

    /** Returns the No Label3. */
    @NotNull public String getNoLabel3() { return this.noLabel3; }

    /** Sets the value of the No Label3. */
    @NotNull public Label setNoLabel3(@NotNull String noLabel3) {
        markAsModified();
        this.noLabel3 = Strings.truncate(noLabel3, 255);
        return (Label) this;
    }

    /** Returns the No Label4. */
    @NotNull public String getNoLabel4() { return this.noLabel4; }

    /** Sets the value of the No Label4. */
    @NotNull public Label setNoLabel4(@NotNull String noLabel4) {
        markAsModified();
        this.noLabel4 = Strings.truncate(noLabel4, 255);
        return (Label) this;
    }

    /** Returns the Some. */
    @NotNull public String getSome() { return this.some; }

    /** Sets the value of the Some. */
    @NotNull public Label setSome(@NotNull String some) {
        markAsModified();
        this.some = Strings.truncate(some, 255);
        return (Label) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Label} instance. */
    @NotNull public static Label create() { return new Label(); }

    @NotNull private static EntityTable<Label,Integer> myEntityTable() { return EntityTable.forTable(LABEL); }

    @NotNull public EntityTable<Label,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Label,Integer> table() { return LABEL; }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Label find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Label findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Label findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Label findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Label find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Label' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Label findWhere(@NotNull Criteria... condition) { return selectFrom(LABEL).where(condition).get(); }

    /** Create a selectFrom(LABEL). */
    @NotNull public static Select<Label> list() { return selectFrom(LABEL); }

    /** Performs the given action for each Label */
    public static void forEach(@Nullable Consumer<Label> consumer) { selectFrom(LABEL).forEach(consumer); }

    /** List instances of 'Label' with the specified keys. */
    @NotNull public static ImmutableList<Label> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Label' with the specified keys. */
    @NotNull public static ImmutableList<Label> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Label' that verify the specified condition. */
    @NotNull public static Select<Label> listWhere(@NotNull Criteria condition) { return selectFrom(LABEL).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Label) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Label> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Label> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Label> rowMapper() { return LABEL.metadata().getRowMapper(); }

}
