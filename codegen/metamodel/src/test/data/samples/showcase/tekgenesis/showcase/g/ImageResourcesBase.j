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
import tekgenesis.showcase.ImageResource;
import tekgenesis.showcase.ImageResources;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Resource;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.ImageResourceTable.IMAGE_RESOURCE;
import static tekgenesis.showcase.g.ImageResourcesTable.IMAGE_RESOURCES;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ImageResources.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ImageResourcesBase
    extends EntityInstanceImpl<ImageResources,Tuple<Integer,Integer>>
    implements InnerInstance<ImageResources,Tuple<Integer,Integer>,ImageResource,Integer>
{

    //~ Fields ...................................................................................................................

    int imageResourceIdKey = 0;
    @NotNull EntityRef<ImageResource,Integer> imageResource = new EntityRef<>(IMAGE_RESOURCE, ImageResource::getImgs);
    int seqId = 0;
    @NotNull String name = "";
    @Nullable Resource img = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Image Resource Id Key. */
    public int getImageResourceIdKey() { return this.imageResourceIdKey; }

    /** Returns the Image Resource. */
    @NotNull public ImageResource getImageResource() { return imageResource.solveOrFail(this.imageResourceIdKey); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<ImageResource,Integer> parent() { return imageResource; }

    @Override @NotNull public InnerEntitySeq<ImageResources> siblings() { return getImageResource().getImgs(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public ImageResources setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (ImageResources) this;
    }

    /** Returns the Img. */
    @Nullable public Resource getImg() { return this.img; }

    /** Sets the value of the Img. */
    @NotNull public ImageResources setImg(@Nullable Resource img) {
        markAsModified();
        this.img = img;
        return (ImageResources) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<ImageResources,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(IMAGE_RESOURCES); }

    @NotNull public EntityTable<ImageResources,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ImageResources,Tuple<Integer,Integer>> table() { return IMAGE_RESOURCES; }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResources find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ImageResources findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResources findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ImageResources findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResources find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResources find(int imageResourceIdKey, int seqId) { return find(Tuple.tuple2(imageResourceIdKey, seqId)); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static ImageResources find(@NotNull String imageResource, int seqId) { return find(Conversions.toInt(imageResource), seqId); }

    /** 
     * Try to finds an Object of type 'ImageResources' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ImageResources findWhere(@NotNull Criteria... condition) { return selectFrom(IMAGE_RESOURCES).where(condition).get(); }

    /** Create a selectFrom(IMAGE_RESOURCES). */
    @NotNull public static Select<ImageResources> list() { return selectFrom(IMAGE_RESOURCES); }

    /** Performs the given action for each ImageResources */
    public static void forEach(@Nullable Consumer<ImageResources> consumer) { selectFrom(IMAGE_RESOURCES).forEach(consumer); }

    /** List instances of 'ImageResources' with the specified keys. */
    @NotNull public static ImmutableList<ImageResources> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ImageResources' with the specified keys. */
    @NotNull public static ImmutableList<ImageResources> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ImageResources' that verify the specified condition. */
    @NotNull public static Select<ImageResources> listWhere(@NotNull Criteria condition) { return selectFrom(IMAGE_RESOURCES).where(condition); }

    @Override @NotNull public final ImageResources update() { return InnerInstance.super.update(); }

    @Override @NotNull public final ImageResources insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ImageResources> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ImageResources> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return imageResourceIdKey + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(imageResourceIdKey, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getImageResource(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getImageResource() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ImageResources> rowMapper() { return IMAGE_RESOURCES.metadata().getRowMapper(); }

    @Override public void invalidate() { imageResource.invalidate(); }

}
