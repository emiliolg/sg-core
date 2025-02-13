package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntitySeq;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.HasChildren;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.showcase.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.NodeTable.NODE;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Node.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class NodeBase
    extends EntityInstanceImpl<Node,Integer>
    implements PersistableInstance<Node,Integer>, HasChildren<Node>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @Nullable Integer parentId = null;
    @NotNull EntityRef<Node,Integer> parent = new EntityRef<>(NODE);
    @NotNull private EntitySeq<Node> children = createEntitySeq(NODE, (Node) this, c -> ((NodeBase)c).parent, listOf(NODE.PARENT_ID));
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Node setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (Node) this;
    }

    /** Returns the Parent Id. */
    @Nullable public Integer getParentId() { return this.parentId; }

    /** Returns the Parent. */
    @Nullable public Node getParent() { return parent.solve(this.parentId); }

    /** Sets the value of the Parent Id. */
    @NotNull public Node setParentId(@Nullable Integer parentId) {
        parent.invalidate();
        this.parentId = parentId;
        return (Node) this;
    }

    /** Sets the value of the Parent. */
    @SuppressWarnings("AssignmentToNull") @NotNull public Node setParent(@Nullable Node parent) {
        this.parent.set(parent);
        if (parent == null) {
        	this.parentId = null;
        }
        else {
        	this.parentId = parent.getId();
        }
        return (Node) this;
    }

    /** Returns the Children. */
    @NotNull public EntitySeq<Node> getChildren() { return children; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Node} instance. */
    @NotNull public static Node create() { return new Node(); }

    @NotNull private static EntityTable<Node,Integer> myEntityTable() { return EntityTable.forTable(NODE); }

    @NotNull public EntityTable<Node,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Node,Integer> table() { return NODE; }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Node find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Node findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Node findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Node findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Node find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Node' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Node findWhere(@NotNull Criteria... condition) { return selectFrom(NODE).where(condition).get(); }

    /** Create a selectFrom(NODE). */
    @NotNull public static Select<Node> list() { return selectFrom(NODE); }

    /** Performs the given action for each Node */
    public static void forEach(@Nullable Consumer<Node> consumer) { selectFrom(NODE).forEach(consumer); }

    /** List instances of 'Node' with the specified keys. */
    @NotNull public static ImmutableList<Node> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Node' with the specified keys. */
    @NotNull public static ImmutableList<Node> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Node' that verify the specified condition. */
    @NotNull public static Select<Node> listWhere(@NotNull Criteria condition) { return selectFrom(NODE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Node) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Node> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Node> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    @NotNull public Seq<Node> children() { return getChildren(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Node> rowMapper() { return NODE.metadata().getRowMapper(); }

    @Override public void invalidate() {
        parent.invalidate();
        children.invalidate();
    }

}
