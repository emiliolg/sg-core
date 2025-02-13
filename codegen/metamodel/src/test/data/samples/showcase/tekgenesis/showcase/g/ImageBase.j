package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.HasImage;
import tekgenesis.showcase.Image;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.common.core.Resource;
import tekgenesis.common.util.Resources;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.ImageTable.IMAGE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Image.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ImageBase
    extends EntityInstanceImpl<Image,Integer>
    implements PersistableInstance<Image,Integer>, HasImage
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull Resource resource = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Image setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (Image) this;
    }

    /** Returns the Resource. */
    @SuppressWarnings("NullableProblems") @NotNull public Resource getResource() { return this.resource; }

    /** Sets the value of the Resource. */
    @NotNull public Image setResource(@NotNull Resource resource) {
        markAsModified();
        this.resource = resource;
        return (Image) this;
    }

    /** Returns the Sec. */
    @NotNull public abstract Resource getSec();

    /** Sets the value of the Sec. */
    @NotNull public abstract Image setSec(@NotNull Resource sec);

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Image} instance. */
    @NotNull public static Image create() { return new Image(); }

    @NotNull private static EntityTable<Image,Integer> myEntityTable() { return EntityTable.forTable(IMAGE); }

    @NotNull public EntityTable<Image,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Image,Integer> table() { return IMAGE; }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Image findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Image findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image findWhere(@NotNull Criteria... condition) { return selectFrom(IMAGE).where(condition).get(); }

    /** Create a selectFrom(IMAGE). */
    @NotNull public static Select<Image> list() { return selectFrom(IMAGE); }

    /** Performs the given action for each Image */
    public static void forEach(@Nullable Consumer<Image> consumer) { selectFrom(IMAGE).forEach(consumer); }

    /** List instances of 'Image' with the specified keys. */
    @NotNull public static ImmutableList<Image> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Image' with the specified keys. */
    @NotNull public static ImmutableList<Image> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Image' that verify the specified condition. */
    @NotNull public static Select<Image> listWhere(@NotNull Criteria condition) { return selectFrom(IMAGE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Image) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Image> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Image> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    /** Returns the image path build using the image resource field. */
    @NotNull public String imagePath() { return Resources.imagePath(getSec()); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Image> rowMapper() { return IMAGE.metadata().getRowMapper(); }

}
