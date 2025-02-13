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
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Tag;
import static tekgenesis.showcase.g.TagTable.TAG;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Tag.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TagBase
    extends EntityInstanceImpl<Tag,String>
    implements PersistableInstance<Tag,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Tag} instance. */
    @NotNull public static Tag create(@NotNull String name) {
        final Tag result = new Tag();
        ((TagBase) result).name = Strings.truncate(name, 20);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Tag' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Tag findOrCreate(@NotNull String name) { return myEntityTable().findOrCreate(name); }

    @NotNull private static EntityTable<Tag,String> myEntityTable() { return EntityTable.forTable(TAG); }

    @NotNull public EntityTable<Tag,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Tag,String> table() { return TAG; }

    /** 
     * Try to finds an Object of type 'Tag' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Tag find(@NotNull String name) { return myEntityTable().find(name); }

    /** 
     * Try to finds an Object of type 'Tag' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Tag findOrFail(@NotNull String name) { return myEntityTable().findOrFail(name); }

    /** 
     * Try to finds an Object of type 'Tag' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Tag findPersisted(@NotNull String name) { return myEntityTable().findPersisted(name); }

    /** 
     * Try to finds an Object of type 'Tag' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Tag findPersistedOrFail(@NotNull String name) { return myEntityTable().findPersistedOrFail(name); }

    /** 
     * Try to finds an Object of type 'Tag' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Tag findWhere(@NotNull Criteria... condition) { return selectFrom(TAG).where(condition).get(); }

    /** Create a selectFrom(TAG). */
    @NotNull public static Select<Tag> list() { return selectFrom(TAG); }

    /** Performs the given action for each Tag */
    public static void forEach(@Nullable Consumer<Tag> consumer) { selectFrom(TAG).forEach(consumer); }

    /** List instances of 'Tag' with the specified keys. */
    @NotNull public static ImmutableList<Tag> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Tag' that verify the specified condition. */
    @NotNull public static Select<Tag> listWhere(@NotNull Criteria condition) { return selectFrom(TAG).where(condition); }

    @Override @NotNull public final Tag update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Tag insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Tag> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Tag> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return name; }

    @NotNull public String keyObject() { return name; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Tag> rowMapper() { return TAG.metadata().getRowMapper(); }

}
