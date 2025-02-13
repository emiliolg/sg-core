package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.showcase.Classroom;
import tekgenesis.showcase.ClassroomSearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Classroom */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ClassroomTable
    extends DbTable<Classroom,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str ROOM;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ClassroomTable() {
        super(Classroom.class,"SHOWCASE","CLASSROOM","",Modifier.NONE,CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        ROOM = strField("room", "ROOM", 4);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ClassroomSearcher> searcher() { return Option.of(ClassroomSearcher.CLASSROOM_SEARCHER); }

    @Override @NotNull public final ClassroomTable as(@NotNull String alias) { return createAlias(new ClassroomTable(), alias); }

    @Override @NotNull protected final EntityTable<Classroom,Integer> createEntityTable() { return new EntityTable<>(CLASSROOM); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ClassroomTable CLASSROOM = new ClassroomTable();

}
