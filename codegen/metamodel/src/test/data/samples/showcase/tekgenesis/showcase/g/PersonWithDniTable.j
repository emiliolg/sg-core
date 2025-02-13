package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.showcase.PersonWithDni;
import tekgenesis.showcase.PersonWithDniSearcher;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.PersonWithDni */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class PersonWithDniTable
    extends DbTable<PersonWithDni,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int DNI_NUMBER;
    @NotNull public final Str NAME;
    @NotNull public final Str LASTNAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private PersonWithDniTable() {
        super(PersonWithDni.class,"SHOWCASE","PERSON_WITH_DNI","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        DNI_NUMBER = intField("dniNumber", "DNI_NUMBER", false, 9);
        NAME = strField("name", "NAME", 255);
        LASTNAME = strField("lastname", "LASTNAME", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DNI_NUMBER));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<PersonWithDniSearcher> searcher() {
        return Option.of(PersonWithDniSearcher.PERSON_WITH_DNI_SEARCHER);
    }

    @Override @NotNull public final PersonWithDniTable as(@NotNull String alias) { return createAlias(new PersonWithDniTable(), alias); }

    @Override @NotNull protected final EntityTable<PersonWithDni,Integer> createEntityTable() { return new EntityTable<>(PERSON_WITH_DNI); }

    //~ Fields ...................................................................................................................

    @NotNull public static final PersonWithDniTable PERSON_WITH_DNI = new PersonWithDniTable();

}
