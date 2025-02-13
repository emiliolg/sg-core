package tekgenesis.showcase.g;

import java.math.BigDecimal;
import tekgenesis.showcase.Car;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.showcase.Engine;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.Transmission;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.CarTable.CAR;
import static tekgenesis.showcase.g.MakeTable.MAKE;
import static tekgenesis.showcase.g.ModelTable.MODEL;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Car.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CarBase
    extends EntityInstanceImpl<Car,Integer>
    implements PersistableInstance<Car,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    int makeId = 0;
    @NotNull EntityRef<Make,Integer> make = new EntityRef<>(MAKE);
    int modelMakeId = 0;
    int modelSeqId = 0;
    @NotNull EntityRef<Model,Tuple<Integer,Integer>> model = new EntityRef<>(MODEL);
    int year = 0;
    @NotNull Engine engine = Engine.GAS;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    int mileage = 0;
    @NotNull Transmission transmission = Transmission.MANUAL;
    @NotNull String color = "";
    boolean air = false;
    boolean bluetooth = false;
    boolean cruise = false;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Make Id. */
    public int getMakeId() { return this.makeId; }

    /** Returns the Make. */
    @NotNull public Make getMake() { return make.solveOrFail(this.makeId); }

    /** Sets the value of the Make Id. */
    @NotNull public Car setMakeId(int makeId) {
        make.invalidate();
        this.makeId = makeId;
        return (Car) this;
    }

    /** Sets the value of the Make. */
    @NotNull public Car setMake(@NotNull Make make) {
        this.make.set(make);
        this.makeId = make.getId();
        return (Car) this;
    }

    /** Returns the Model Make Id. */
    public int getModelMakeId() { return this.modelMakeId; }

    /** Returns the Model Seq Id. */
    public int getModelSeqId() { return this.modelSeqId; }

    /** Returns the Model. */
    @NotNull public Model getModel() {
        return model.solveOrFail(Tuple.tuple2(this.modelMakeId, this.modelSeqId));
    }

    /** Sets the value of the Model Make Id. */
    @NotNull public Car setModelMakeId(int modelMakeId) {
        model.invalidate();
        this.modelMakeId = modelMakeId;
        return (Car) this;
    }

    /** Sets the value of the Model Seq Id. */
    @NotNull public Car setModelSeqId(int modelSeqId) {
        model.invalidate();
        this.modelSeqId = modelSeqId;
        return (Car) this;
    }

    /** Sets the value of the Model. */
    @NotNull public Car setModel(@NotNull Model model) {
        this.model.set(model);
        this.modelMakeId = model.getMakeId();
        this.modelSeqId = model.getSeqId();
        return (Car) this;
    }

    /** Returns the Year. */
    public int getYear() { return this.year; }

    /** Sets the value of the Year. */
    @NotNull public Car setYear(int year) {
        markAsModified();
        this.year = Integers.checkSignedLength("year", year, false, 9);
        return (Car) this;
    }

    /** Returns the Engine. */
    @NotNull public Engine getEngine() { return this.engine; }

    /** Sets the value of the Engine. */
    @NotNull public Car setEngine(@NotNull Engine engine) {
        markAsModified();
        this.engine = engine;
        return (Car) this;
    }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull public Car setPrice(@NotNull BigDecimal price) {
        markAsModified();
        this.price = Decimals.scaleAndCheck("price", price, false, 7, 0);
        return (Car) this;
    }

    /** Returns the Mileage. */
    public int getMileage() { return this.mileage; }

    /** Sets the value of the Mileage. */
    @NotNull public Car setMileage(int mileage) {
        markAsModified();
        this.mileage = Integers.checkSignedLength("mileage", mileage, false, 9);
        return (Car) this;
    }

    /** Returns the Transmission. */
    @NotNull public Transmission getTransmission() { return this.transmission; }

    /** Sets the value of the Transmission. */
    @NotNull public Car setTransmission(@NotNull Transmission transmission) {
        markAsModified();
        this.transmission = transmission;
        return (Car) this;
    }

    /** Returns the Color. */
    @NotNull public String getColor() { return this.color; }

    /** Sets the value of the Color. */
    @NotNull public Car setColor(@NotNull String color) {
        markAsModified();
        this.color = Strings.truncate(color, 30);
        return (Car) this;
    }

    /** Returns true if it is Air. */
    public boolean isAir() { return this.air; }

    /** Sets the value of the Air. */
    @NotNull public Car setAir(boolean air) {
        markAsModified();
        this.air = air;
        return (Car) this;
    }

    /** Returns true if it is Bluetooth. */
    public boolean isBluetooth() { return this.bluetooth; }

    /** Sets the value of the Bluetooth. */
    @NotNull public Car setBluetooth(boolean bluetooth) {
        markAsModified();
        this.bluetooth = bluetooth;
        return (Car) this;
    }

    /** Returns true if it is Cruise. */
    public boolean isCruise() { return this.cruise; }

    /** Sets the value of the Cruise. */
    @NotNull public Car setCruise(boolean cruise) {
        markAsModified();
        this.cruise = cruise;
        return (Car) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Car} instance. */
    @NotNull public static Car create() { return new Car(); }

    @NotNull private static EntityTable<Car,Integer> myEntityTable() { return EntityTable.forTable(CAR); }

    @NotNull public EntityTable<Car,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Car,Integer> table() { return CAR; }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Car find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Car findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Car findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Car findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Car find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Car' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Car findWhere(@NotNull Criteria... condition) { return selectFrom(CAR).where(condition).get(); }

    /** Create a selectFrom(CAR). */
    @NotNull public static Select<Car> list() { return selectFrom(CAR); }

    /** Performs the given action for each Car */
    public static void forEach(@Nullable Consumer<Car> consumer) { selectFrom(CAR).forEach(consumer); }

    /** List instances of 'Car' with the specified keys. */
    @NotNull public static ImmutableList<Car> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Car' with the specified keys. */
    @NotNull public static ImmutableList<Car> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Car' that verify the specified condition. */
    @NotNull public static Select<Car> listWhere(@NotNull Criteria condition) { return selectFrom(CAR).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Car) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Car> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Car> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getMake(), getModel()); }

    @Override @NotNull public String toString() { return "" + getMake() + " " + getModel(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Car> rowMapper() { return CAR.metadata().getRowMapper(); }

    @Override public void invalidate() {
        model.invalidate();
        make.invalidate();
    }

}
