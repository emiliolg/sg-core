package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.ImageResource;
import tekgenesis.showcase.ImageResources;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.common.core.Resource;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import static tekgenesis.showcase.g.ImageResourceTable.IMAGE_RESOURCE;
import static tekgenesis.showcase.g.ImageResourcesTable.IMAGE_RESOURCES;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ImageResource.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ImageResourceBase
    extends EntityInstanceImpl<ImageResource,Integer>
    implements PersistableInstance<ImageResource,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @Nullable Resource img = null;
    @NotNull private InnerEntitySeq<ImageResources> imgs = createInnerEntitySeq(IMAGE_RESOURCES, (ImageResource) this, c -> ((ImageResourcesBase)c).imageResource);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Img. */
    @Nullable public Resource getImg() { return this.img; }

    /** Sets the value of the Img. */
    @NotNull public ImageResource setImg(@Nullable Resource img) {
        markAsModified();
        this.img = img;
        return (ImageResource) this;
    }

    /** Returns the Imgs. */
    @NotNull public InnerEntitySeq<ImageResources> getImgs() { return imgs; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ImageResource} instance. */
    @NotNull public static ImageResource create(int idKey) {
        final ImageResource result = new ImageResource();
        ((ImageResourceBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'ImageResource' in the database.
     * Identified by the primary key.
     */
    @NotNull public static ImageResource findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'ImageResource' in the database.
     * Identified by the primary key.
     */
    @NotNull public static ImageResource findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<ImageResource,Integer> myEntityTable() { return EntityTable.forTable(IMAGE_RESOURCE); }

    @NotNull public EntityTable<ImageResource,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ImageResource,Integer> table() { return IMAGE_RESOURCE; }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResource find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ImageResource findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResource findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ImageResource findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResource find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ImageResource' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResource findWhere(@NotNull Criteria... condition) { return selectFrom(IMAGE_RESOURCE).where(condition).get(); }

    /** Create a selectFrom(IMAGE_RESOURCE). */
    @NotNull public static Select<ImageResource> list() { return selectFrom(IMAGE_RESOURCE); }

    /** Performs the given action for each ImageResource */
    public static void forEach(@Nullable Consumer<ImageResource> consumer) { selectFrom(IMAGE_RESOURCE).forEach(consumer); }

    /** List instances of 'ImageResource' with the specified keys. */
    @NotNull public static ImmutableList<ImageResource> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ImageResource' with the specified keys. */
    @NotNull public static ImmutableList<ImageResource> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ImageResource' that verify the specified condition. */
    @NotNull public static Select<ImageResource> listWhere(@NotNull Criteria condition) { return selectFrom(IMAGE_RESOURCE).where(condition); }

    @Override @NotNull public final ImageResource update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final ImageResource insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ImageResource> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ImageResource> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ImageResource> rowMapper() { return IMAGE_RESOURCE.metadata().getRowMapper(); }

    @Override public void invalidate() { imgs.invalidate(); }

}
