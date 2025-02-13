package tekgenesis.showcase.g;

import tekgenesis.persistence.TableField.Bool;
import tekgenesis.cache.CacheType;
import tekgenesis.showcase.Car;
import tekgenesis.showcase.CarSearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.showcase.Engine;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.showcase.Transmission;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Car */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CarTable
    extends DbTable<Car,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Int MAKE_ID;
    @NotNull public final Int MODEL_MAKE_ID;
    @NotNull public final Int MODEL_SEQ_ID;
    @NotNull public final Int YEAR;
    @NotNull public final Enum<Engine,String> ENGINE;
    @NotNull public final Decimal PRICE;
    @NotNull public final Int MILEAGE;
    @NotNull public final Enum<Transmission,String> TRANSMISSION;
    @NotNull public final Str COLOR;
    @NotNull public final Bool AIR;
    @NotNull public final Bool BLUETOOTH;
    @NotNull public final Bool CRUISE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CarTable() {
        super(Car.class,"SHOWCASE","CAR","CAR_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MAKE_ID = intField("makeId", "MAKE_ID", false, 9);
        MODEL_MAKE_ID = intField("modelMakeId", "MODEL_MAKE_ID", false, 9);
        MODEL_SEQ_ID = intField("modelSeqId", "MODEL_SEQ_ID", false, 9);
        YEAR = intField("year", "YEAR", false, 9);
        ENGINE = enumField("engine", "ENGINE", Engine.class);
        PRICE = decimalField("price", "PRICE", false, 7, 0);
        MILEAGE = intField("mileage", "MILEAGE", false, 9);
        TRANSMISSION = enumField("transmission", "TRANSMISSION", Transmission.class);
        COLOR = strField("color", "COLOR", 30);
        AIR = boolField("air", "AIR");
        BLUETOOTH = boolField("bluetooth", "BLUETOOTH");
        CRUISE = boolField("cruise", "CRUISE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<CarSearcher> searcher() { return Option.of(CarSearcher.CAR_SEARCHER); }

    @Override @NotNull public final CarTable as(@NotNull String alias) { return createAlias(new CarTable(), alias); }

    @Override @NotNull protected final EntityTable<Car,Integer> createEntityTable() { return new EntityTable<>(CAR); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CarTable CAR = new CarTable();

}
