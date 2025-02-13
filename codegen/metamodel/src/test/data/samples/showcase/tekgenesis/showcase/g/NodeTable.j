package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.Node;
import tekgenesis.showcase.NodeSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Node */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class NodeTable
    extends DbTable<Node,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Int PARENT_ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private NodeTable() {
        super(Node.class,"SHOWCASE","NODE","NODE_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 255);
        PARENT_ID = intField("parentId", "PARENT_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<NodeSearcher> searcher() { return Option.of(NodeSearcher.NODE_SEARCHER); }

    @Override @NotNull public final NodeTable as(@NotNull String alias) { return createAlias(new NodeTable(), alias); }

    @Override @NotNull protected final EntityTable<Node,Integer> createEntityTable() { return new EntityTable<>(NODE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final NodeTable NODE = new NodeTable();

}
