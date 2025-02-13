package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.showcase.Gender;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Student;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Student */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class StudentTable
    extends DbTable<Student,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int CLASSROOM_ID_KEY;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Int DNI;
    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Int AGE;
    @NotNull public final Enum<Gender,String> GENDER;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private StudentTable() {
        super(Student.class,"SHOWCASE","STUDENT","",Modifier.NONE,CacheType.NONE);
        CLASSROOM_ID_KEY = intField("classroomIdKey", "CLASSROOM_ID_KEY", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        DNI = intField("dni", "DNI", false, 9);
        FIRST_NAME = strField("firstName", "FIRST_NAME", 20);
        LAST_NAME = strField("lastName", "LAST_NAME", 20);
        AGE = intField("age", "AGE", false, 9);
        GENDER = enumField("gender", "GENDER", Gender.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(CLASSROOM_ID_KEY, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final StudentTable as(@NotNull String alias) { return createAlias(new StudentTable(), alias); }

    @Override @NotNull protected final EntityTable<Student,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(STUDENT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final StudentTable STUDENT = new StudentTable();

}
