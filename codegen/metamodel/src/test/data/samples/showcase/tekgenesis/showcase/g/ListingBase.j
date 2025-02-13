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
import tekgenesis.common.core.Integers;
import tekgenesis.showcase.Listing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import tekgenesis.showcase.ViewData;
import static tekgenesis.showcase.g.ListingTable.LISTING;
import static tekgenesis.showcase.g.ViewDataTable.VIEW_DATA;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Listing.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ListingBase
    extends EntityInstanceImpl<Listing,Tuple<Integer,Integer>>
    implements InnerInstance<Listing,Tuple<Integer,Integer>,ViewData,Integer>
{

    //~ Fields ...................................................................................................................

    int viewDataId = 0;
    @NotNull EntityRef<ViewData,Integer> viewData = new EntityRef<>(VIEW_DATA, ViewData::getItems);
    int seqId = 0;
    int pk = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the View Data Id. */
    public int getViewDataId() { return this.viewDataId; }

    /** Returns the View Data. */
    @NotNull public ViewData getViewData() { return viewData.solveOrFail(this.viewDataId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<ViewData,Integer> parent() { return viewData; }

    @Override @NotNull public InnerEntitySeq<Listing> siblings() { return getViewData().getItems(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Pk. */
    public int getPk() { return this.pk; }

    /** Sets the value of the Pk. */
    @NotNull public Listing setPk(int pk) {
        markAsModified();
        this.pk = Integers.checkSignedLength("pk", pk, false, 9);
        return (Listing) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Listing,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(LISTING); }

    @NotNull public EntityTable<Listing,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Listing,Tuple<Integer,Integer>> table() { return LISTING; }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Listing find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Listing findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Listing findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Listing findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Listing find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Listing find(int viewDataId, int seqId) { return find(Tuple.tuple2(viewDataId, seqId)); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Listing find(@NotNull String viewData, int seqId) { return find(Conversions.toInt(viewData), seqId); }

    /** 
     * Try to finds an Object of type 'Listing' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Listing findWhere(@NotNull Criteria... condition) { return selectFrom(LISTING).where(condition).get(); }

    /** Create a selectFrom(LISTING). */
    @NotNull public static Select<Listing> list() { return selectFrom(LISTING); }

    /** Performs the given action for each Listing */
    public static void forEach(@Nullable Consumer<Listing> consumer) { selectFrom(LISTING).forEach(consumer); }

    /** List instances of 'Listing' with the specified keys. */
    @NotNull public static ImmutableList<Listing> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Listing' with the specified keys. */
    @NotNull public static ImmutableList<Listing> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Listing' that verify the specified condition. */
    @NotNull public static Select<Listing> listWhere(@NotNull Criteria condition) { return selectFrom(LISTING).where(condition); }

    @Override @NotNull public final Listing update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Listing insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Listing> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Listing> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return viewDataId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(viewDataId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getViewData(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getViewData() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Listing> rowMapper() { return LISTING.metadata().getRowMapper(); }

    @Override public void invalidate() { viewData.invalidate(); }

}
