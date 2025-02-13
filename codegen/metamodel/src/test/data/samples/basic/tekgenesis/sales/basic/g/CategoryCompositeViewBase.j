package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryCompositeView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.sales.basic.g.CategoryCompositeViewTable.CATEGORY_COMPOSITE_VIEW;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CategoryCompositeView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryCompositeViewBase
    extends EntityInstanceImpl<CategoryCompositeView,Tuple3<Long,String,String>>
    implements EntityInstance<CategoryCompositeView,Tuple3<Long,String,String>>
{

    //~ Fields ...................................................................................................................

    long vid = 0;
    @NotNull String vdescr = "";
    @NotNull String vshort = "";
    @NotNull String vname = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Vid. */
    public long getVid() { return this.vid; }

    /** Returns the Vdescr. */
    @NotNull public String getVdescr() { return this.vdescr; }

    /** Returns the Vshort. */
    @NotNull public String getVshort() { return this.vshort; }

    /** Returns the Vname. */
    @NotNull public String getVname() { return this.vname; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CategoryCompositeView} instance. */
    @NotNull protected static CategoryCompositeView create(long vid, @NotNull String vdescr, @NotNull String vshort) {
        final CategoryCompositeView result = new CategoryCompositeView();
        ((CategoryCompositeViewBase) result).vid = Integers.checkSignedLength("vid", vid, false, 18);
        ((CategoryCompositeViewBase) result).vdescr = Strings.truncate(vdescr, 120);
        ((CategoryCompositeViewBase) result).vshort = Strings.truncate(vshort, 120);
        return result;
    }

    /** 
     * Creates a new {@link CategoryCompositeView} instance.
     * Based on the primary key object
     */
    @NotNull protected static CategoryCompositeView create(@NotNull Tuple3<Long,String,String> key) { return create(key._1(), key._2(), key._3()); }

    @NotNull private static EntityTable<CategoryCompositeView,Tuple3<Long,String,String>> myEntityTable() { return EntityTable.forTable(CATEGORY_COMPOSITE_VIEW); }

    @NotNull public EntityTable<CategoryCompositeView,Tuple3<Long,String,String>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategoryCompositeView,Tuple3<Long,String,String>> table() { return CATEGORY_COMPOSITE_VIEW; }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryCompositeView find(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryCompositeView findOrFail(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryCompositeView findPersisted(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryCompositeView findPersistedOrFail(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryCompositeView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryCompositeView find(long vid, @NotNull String vdescr, @NotNull String vshort) { return find(Tuple.tuple(vid, vdescr, vshort)); }

    /** 
     * Try to finds an Object of type 'CategoryCompositeView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryCompositeView findWhere(@NotNull Criteria... condition) {
        return selectFrom(CATEGORY_COMPOSITE_VIEW).where(condition).get();
    }

    /** Create a selectFrom(CATEGORY_COMPOSITE_VIEW). */
    @NotNull public static Select<CategoryCompositeView> list() { return selectFrom(CATEGORY_COMPOSITE_VIEW); }

    /** Performs the given action for each CategoryCompositeView */
    public static void forEach(@Nullable Consumer<CategoryCompositeView> consumer) { selectFrom(CATEGORY_COMPOSITE_VIEW).forEach(consumer); }

    /** List instances of 'CategoryCompositeView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryCompositeView> list(@Nullable Set<Tuple3<Long,String,String>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategoryCompositeView' with the specified keys. */
    @NotNull public static ImmutableList<CategoryCompositeView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategoryCompositeView' that verify the specified condition. */
    @NotNull public static Select<CategoryCompositeView> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_COMPOSITE_VIEW).where(condition); }

    @NotNull public String keyAsString() {
        return vid + ":" + Strings.escapeCharOn(vdescr, ':') + ":" + Strings.escapeCharOn(vshort, ':');
    }

    @NotNull public Tuple3<Long,String,String> keyObject() { return Tuple.tuple(vid, vdescr, vshort); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + getVdescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategoryCompositeView> rowMapper() { return CATEGORY_COMPOSITE_VIEW.metadata().getRowMapper(); }

}
