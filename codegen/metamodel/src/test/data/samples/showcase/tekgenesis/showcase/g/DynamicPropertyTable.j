package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.showcase.DynamicProperty;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.PropertyType;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DynamicProperty */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DynamicPropertyTable
    extends DbTable<DynamicProperty,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str PROPERTY_NAME;
    @NotNull public final Enum<PropertyType,String> PROPERTY_TYPE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private DynamicPropertyTable() {
        super(DynamicProperty.class,"SHOWCASE","DYNAMIC_PROPERTY","DYNAMIC_PROPERTY_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        PROPERTY_NAME = strField("propertyName", "PROPERTY_NAME", 20);
        PROPERTY_TYPE = enumField("propertyType", "PROPERTY_TYPE", PropertyType.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final DynamicPropertyTable as(@NotNull String alias) { return createAlias(new DynamicPropertyTable(), alias); }

    @Override @NotNull protected final EntityTable<DynamicProperty,Integer> createEntityTable() { return new EntityTable<>(DYNAMIC_PROPERTY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DynamicPropertyTable DYNAMIC_PROPERTY = new DynamicPropertyTable();

}
