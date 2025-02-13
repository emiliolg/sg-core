package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormParameters;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CarForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CarFormBase
    extends FormInstance<Car>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Car car = Car.create();
        copyTo(car);
        car.insert();
        setId(car.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Car car = find();
        copyTo(car);
        car.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Car find() {
        final Car value = Car.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Car populate() {
        final Car car = find();

        setMake(car.getMake())
        	.setModel(car.getModel())
        	.setYear(car.getYear())
        	.setEngine(car.getEngine())
        	.setPrice(car.getPrice())
        	.setMileage(car.getMileage())
        	.setTransmission(car.getTransmission())
        	.setColor(car.getColor())
        	.setAir(car.isAir())
        	.setBluetooth(car.isBluetooth())
        	.setCruise(car.isCruise());

        return car;
    }

    /** Associates a new message to the specified field and returns it for any further configuration. */
    @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

    /** Returns true if the field value is not null. */
    public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

    /** Resets the given fields. */
    protected void reset(@NotNull Field... fields) { f.reset(fields); }

    /** Focuses given field. */
    protected void focus(@NotNull Field field) { f.focus(field); }

    /** Returns the label of the given field. */
    @NotNull protected String label(@NotNull Field field) { return f.label(field); }

    /** Returns a typed configuration for a given field. */
    @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    /** Copies field values to given car instance. */
    public void copyTo(@NotNull Car car) {
        car.setMake(getMake())
        	.setModel(getModel())
        	.setYear(getYear())
        	.setEngine(getEngine())
        	.setPrice(getPrice())
        	.setMileage(getMileage())
        	.setTransmission(getTransmission())
        	.setColor(getColor())
        	.setAir(isAir())
        	.setBluetooth(isBluetooth())
        	.setCruise(isCruise());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public CarForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (CarForm) this;
    }

    /** Returns the value of the suggest_box(make). */
    @NotNull public Make getMake() {
        return Predefined.ensureNotNull(Make.find(getMakeKey()), "'make' not found");
    }

    /** Returns the key value of the suggest_box(make). */
    @NotNull public String getMakeKey() { return f.get(Field.MAKE, String.class); }

    /** Sets the value of the suggest_box(make). */
    @NotNull public CarForm setMake(@NotNull Make make) {
        f.set(Field.MAKE, make);
        return (CarForm) this;
    }

    /** Returns the value of the suggest_box(model). */
    @NotNull public Model getModel() {
        return Predefined.ensureNotNull(Model.find(getModelKey()), "'model' not found");
    }

    /** Returns the key value of the suggest_box(model). */
    @NotNull public String getModelKey() { return f.get(Field.MODEL, String.class); }

    /** Sets the value of the suggest_box(model). */
    @NotNull public CarForm setModel(@NotNull Model model) {
        f.set(Field.MODEL, model);
        return (CarForm) this;
    }

    /** Returns the value of the text_field(year). */
    public int getYear() { return f.get(Field.YEAR, Integer.class); }

    /** Sets the value of the text_field(year). */
    @NotNull public CarForm setYear(int year) {
        f.set(Field.YEAR, year);
        return (CarForm) this;
    }

    /** Returns the value of the combo_box(engine). */
    @NotNull public Engine getEngine() { return Engine.valueOf(f.get(Field.ENGINE, String.class)); }

    /** Sets the value of the combo_box(engine). */
    @NotNull public CarForm setEngine(@NotNull Engine engine) {
        f.set(Field.ENGINE, engine);
        return (CarForm) this;
    }

    /** Sets the options of the combo_box(engine). */
    public void setEngineOptions(@NotNull Iterable<Engine> items) { f.opts(Field.ENGINE, items); }

    /** Sets the options of the combo_box(engine) with the given KeyMap. */
    public void setEngineOptions(@NotNull KeyMap items) { f.opts(Field.ENGINE, items); }

    /** Returns the value of the text_field(price). */
    @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

    /** Sets the value of the text_field(price). */
    @NotNull public CarForm setPrice(@NotNull BigDecimal price) {
        f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 7, 0));
        return (CarForm) this;
    }

    /** Returns the value of the text_field(mileage). */
    public int getMileage() { return f.get(Field.MILEAGE, Integer.class); }

    /** Sets the value of the text_field(mileage). */
    @NotNull public CarForm setMileage(int mileage) {
        f.set(Field.MILEAGE, mileage);
        return (CarForm) this;
    }

    /** Returns the value of the combo_box(transmission). */
    @NotNull public Transmission getTransmission() {
        return Transmission.valueOf(f.get(Field.TRANSMISSION, String.class));
    }

    /** Sets the value of the combo_box(transmission). */
    @NotNull public CarForm setTransmission(@NotNull Transmission transmission) {
        f.set(Field.TRANSMISSION, transmission);
        return (CarForm) this;
    }

    /** Sets the options of the combo_box(transmission). */
    public void setTransmissionOptions(@NotNull Iterable<Transmission> items) { f.opts(Field.TRANSMISSION, items); }

    /** Sets the options of the combo_box(transmission) with the given KeyMap. */
    public void setTransmissionOptions(@NotNull KeyMap items) { f.opts(Field.TRANSMISSION, items); }

    /** Returns the value of the text_field(color). */
    @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

    /** Sets the value of the text_field(color). */
    @NotNull public CarForm setColor(@NotNull String color) {
        f.set(Field.COLOR, color);
        return (CarForm) this;
    }

    /** Returns the value of the check_box(air). */
    public boolean isAir() { return f.get(Field.AIR, Boolean.class); }

    /** Sets the value of the check_box(air). */
    @NotNull public CarForm setAir(boolean air) {
        f.set(Field.AIR, air);
        return (CarForm) this;
    }

    /** Returns the value of the check_box(bluetooth). */
    public boolean isBluetooth() { return f.get(Field.BLUETOOTH, Boolean.class); }

    /** Sets the value of the check_box(bluetooth). */
    @NotNull public CarForm setBluetooth(boolean bluetooth) {
        f.set(Field.BLUETOOTH, bluetooth);
        return (CarForm) this;
    }

    /** Returns the value of the check_box(cruise). */
    public boolean isCruise() { return f.get(Field.CRUISE, Boolean.class); }

    /** Sets the value of the check_box(cruise). */
    @NotNull public CarForm setCruise(boolean cruise) {
        f.set(Field.CRUISE, cruise);
        return (CarForm) this;
    }

    @NotNull public static CarFormParameters parameters() { return new CarFormParameters(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CarForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CarForm.class);

    //~ Inner Classes ............................................................................................................

    public static final class CarFormParameters
        extends FormParameters<CarForm>
    {

        //~ Methods ..................................................................................................................

        @NotNull public CarFormParameters withYear(int year) { return put(Field.YEAR, year); }

        @NotNull public CarFormParameters withEngine(@NotNull Engine engine) { return put(Field.ENGINE, engine); }

        @NotNull public CarFormParameters withPrice(@NotNull BigDecimal price) {
            return put(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 7, 0));
        }

        @NotNull public CarFormParameters withAir(boolean air) { return put(Field.AIR, air); }

        @NotNull public CarFormParameters withBluetooth(boolean bluetooth) { return put(Field.BLUETOOTH, bluetooth); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        $L3("$L3"),
        ID("id"),
        MAKE("make"),
        MODEL("model"),
        YEAR("year"),
        ENGINE("engine"),
        PRICE("price"),
        MILEAGE("mileage"),
        TRANSMISSION("transmission"),
        COLOR("color"),
        AIR("air"),
        BLUETOOTH("bluetooth"),
        CRUISE("cruise"),
        $F4("$F4"),
        $B5("$B5"),
        $B6("$B6"),
        $B7("$B7");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
