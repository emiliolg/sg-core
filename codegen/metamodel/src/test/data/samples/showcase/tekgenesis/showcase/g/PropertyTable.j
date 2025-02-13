package tekgenesis.showcase.g;

import tekgenesis.persistence.TableField.Bool;
import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.showcase.Property;
import tekgenesis.showcase.PropertySearcher;
import tekgenesis.showcase.PropertyType;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Property */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class PropertyTable
    extends DbTable<Property,Tuple<String,PropertyType>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Enum<PropertyType,String> TYPE;
    @NotNull public final Bool MULTIPLE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private PropertyTable() {
        super(Property.class,"SHOWCASE","PROPERTY","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        NAME = strField("name", "NAME", 20);
        TYPE = enumField("type", "TYPE", PropertyType.class);
        MULTIPLE = boolField("multiple", "MULTIPLE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(NAME, TYPE));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,PropertyType> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], PropertyType.valueOf(parts[1]));
    }

    @Override @NotNull protected Option<PropertySearcher> searcher() { return Option.of(PropertySearcher.PROPERTY_SEARCHER); }

    @Override @NotNull public final PropertyTable as(@NotNull String alias) { return createAlias(new PropertyTable(), alias); }

    @Override @NotNull protected final EntityTable<Property,Tuple<String,PropertyType>> createEntityTable() { return new EntityTable<>(PROPERTY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final PropertyTable PROPERTY = new PropertyTable();

}
