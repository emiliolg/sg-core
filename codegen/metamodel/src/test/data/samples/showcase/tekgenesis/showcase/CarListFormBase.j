package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.showcase.CarListForm.CarsRow;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CarListForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CarListFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CarListForm");
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

    /** Returns a {@link FormTable<CarsRow>} instance to handle Cars manipulation */
    @NotNull public final FormTable<CarsRow> getCars() { return table(Field.CARS, CarsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(engine). */
    public void setEngineOptions(@NotNull Iterable<Engine> items) { f.opts(Field.ENGINE, items); }

    /** Sets the options of the combo_box(engine) with the given KeyMap. */
    public void setEngineOptions(@NotNull KeyMap items) { f.opts(Field.ENGINE, items); }

    /** Sets the options of the combo_box(transmission). */
    public void setTransmissionOptions(@NotNull Iterable<Transmission> items) { f.opts(Field.TRANSMISSION, items); }

    /** Sets the options of the combo_box(transmission) with the given KeyMap. */
    public void setTransmissionOptions(@NotNull KeyMap items) { f.opts(Field.TRANSMISSION, items); }

    /** Called on loading table(cars). */
    public void loadCars() {
        final FormTable<CarsRow> table = getCars();
        Car.forEach(currentCar -> table.add().populate(currentCar));
    }

    /** Called each time table(cars) changes. */
    @NotNull public Action saveCar() {
        if (forms.hasPermission(PredefinedPermission.CREATE)) {
        	final CarsRow row = getCars().getCurrent();
        	final Integer primaryKey = row.getId();
        	final Car currentCar = primaryKey != null ? Predefined.ensureNotNull(Car.find(primaryKey), "'row.getId()' not found") : Car.create();
        	row.copyTo(currentCar);
        	currentCar.persist();
        	row.setId(currentCar.getId());
        }
        return actions().getDefault();
    }

    /** Called on removing cars row. */
    @NotNull public Action removeCar() { return getCars().getCurrent().remove(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarListForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarListForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CarListForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CarListForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class CarsRowBase
        implements FormRowInstance<CarsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(id). */
        @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the internal(id). */
        @NotNull public CarsRow setId(@Nullable Integer id) {
            f.set(Field.ID, id);
            return (CarsRow) this;
        }

        /** Returns the value of the suggest_box(make). */
        @NotNull public Make getMake() {
            return Predefined.ensureNotNull(Make.find(getMakeKey()), "'make' not found");
        }

        /** Returns the key value of the suggest_box(make). */
        @NotNull public String getMakeKey() { return f.get(Field.MAKE, String.class); }

        /** Sets the value of the suggest_box(make). */
        @NotNull public CarsRow setMake(@NotNull Make make) {
            f.set(Field.MAKE, make);
            return (CarsRow) this;
        }

        /** Returns the value of the suggest_box(model). */
        @NotNull public Model getModel() {
            return Predefined.ensureNotNull(Model.find(getModelKey()), "'model' not found");
        }

        /** Returns the key value of the suggest_box(model). */
        @NotNull public String getModelKey() { return f.get(Field.MODEL, String.class); }

        /** Sets the value of the suggest_box(model). */
        @NotNull public CarsRow setModel(@NotNull Model model) {
            f.set(Field.MODEL, model);
            return (CarsRow) this;
        }

        /** Returns the value of the text_field(year). */
        public int getYear() { return f.get(Field.YEAR, Integer.class); }

        /** Sets the value of the text_field(year). */
        @NotNull public CarsRow setYear(int year) {
            f.set(Field.YEAR, year);
            return (CarsRow) this;
        }

        /** Returns the value of the combo_box(engine). */
        @NotNull public Engine getEngine() { return Engine.valueOf(f.get(Field.ENGINE, String.class)); }

        /** Sets the value of the combo_box(engine). */
        @NotNull public CarsRow setEngine(@NotNull Engine engine) {
            f.set(Field.ENGINE, engine);
            return (CarsRow) this;
        }

        /** Sets the options of the combo_box(engine). */
        public void setEngineOptions(@NotNull Iterable<Engine> items) { f.opts(Field.ENGINE, items); }

        /** Sets the options of the combo_box(engine) with the given KeyMap. */
        public void setEngineOptions(@NotNull KeyMap items) { f.opts(Field.ENGINE, items); }

        /** Returns the value of the text_field(price). */
        @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

        /** Sets the value of the text_field(price). */
        @NotNull public CarsRow setPrice(@NotNull BigDecimal price) {
            f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 7, 0));
            return (CarsRow) this;
        }

        /** Returns the value of the text_field(mileage). */
        public int getMileage() { return f.get(Field.MILEAGE, Integer.class); }

        /** Sets the value of the text_field(mileage). */
        @NotNull public CarsRow setMileage(int mileage) {
            f.set(Field.MILEAGE, mileage);
            return (CarsRow) this;
        }

        /** Returns the value of the combo_box(transmission). */
        @NotNull public Transmission getTransmission() {
            return Transmission.valueOf(f.get(Field.TRANSMISSION, String.class));
        }

        /** Sets the value of the combo_box(transmission). */
        @NotNull public CarsRow setTransmission(@NotNull Transmission transmission) {
            f.set(Field.TRANSMISSION, transmission);
            return (CarsRow) this;
        }

        /** Sets the options of the combo_box(transmission). */
        public void setTransmissionOptions(@NotNull Iterable<Transmission> items) { f.opts(Field.TRANSMISSION, items); }

        /** Sets the options of the combo_box(transmission) with the given KeyMap. */
        public void setTransmissionOptions(@NotNull KeyMap items) { f.opts(Field.TRANSMISSION, items); }

        /** Returns the value of the text_field(color). */
        @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

        /** Sets the value of the text_field(color). */
        @NotNull public CarsRow setColor(@NotNull String color) {
            f.set(Field.COLOR, color);
            return (CarsRow) this;
        }

        /** Returns the value of the check_box(air). */
        public boolean isAir() { return f.get(Field.AIR, Boolean.class); }

        /** Sets the value of the check_box(air). */
        @NotNull public CarsRow setAir(boolean air) {
            f.set(Field.AIR, air);
            return (CarsRow) this;
        }

        /** Returns the value of the check_box(bluetooth). */
        public boolean isBluetooth() { return f.get(Field.BLUETOOTH, Boolean.class); }

        /** Sets the value of the check_box(bluetooth). */
        @NotNull public CarsRow setBluetooth(boolean bluetooth) {
            f.set(Field.BLUETOOTH, bluetooth);
            return (CarsRow) this;
        }

        /** Returns the value of the check_box(cruise). */
        public boolean isCruise() { return f.get(Field.CRUISE, Boolean.class); }

        /** Sets the value of the check_box(cruise). */
        @NotNull public CarsRow setCruise(boolean cruise) {
            f.set(Field.CRUISE, cruise);
            return (CarsRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<CarsRow> table() { return getCars(); }

        /** Remove row from table and delete associated Car instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Car instance = isDefined(Field.ID) ? Car.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((CarsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given car instance. */
        public void populate(@NotNull Car car) {
            setId(car.getId())
            	.setMake(car.getMake())
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
        }

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

        /** Return primary key of bound {@link Car} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        CARS("cars"),
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
        $H3("$H3"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
