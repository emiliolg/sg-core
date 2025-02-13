package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.showcase.CarFilterForm.CarsRow;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.filter.Filter.CustomOptions;
import tekgenesis.form.filter.Filter.DateFilter;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.filter.Filter.DateTimeFilter;
import tekgenesis.form.filter.Filter.DecimalFilter;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.filter.Filter.EntityFilter;
import tekgenesis.form.filter.Filter.EnumFilter;
import tekgenesis.form.filter.FilterFactory;
import tekgenesis.showcase.CarFilterForm.FiltersRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.form.filter.Filter.IntegerFilter;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.filter.Options;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CarFilterForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CarFilterFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CarFilterForm");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Invoked when button(filter) is clicked */
    @NotNull public abstract Action filter();

    /** Returns a {@link FormTable<FiltersRow>} instance to handle Filters manipulation */
    @NotNull public final FormTable<FiltersRow> getFilters() { return table(Field.FILTERS, FiltersRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the dynamic(value) with the given KeyMap. */
    public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

    /** Invoked when table(cars) is clicked */
    @NotNull public abstract Action rowClicked();

    /** Returns a {@link FormTable<CarsRow>} instance to handle Cars manipulation */
    @NotNull public final FormTable<CarsRow> getCars() { return table(Field.CARS, CarsRow.class); }

    /** Sets the options of the combo_box(engine). */
    public void setEngineOptions(@NotNull Iterable<Engine> items) { f.opts(Field.ENGINE, items); }

    /** Sets the options of the combo_box(engine) with the given KeyMap. */
    public void setEngineOptions(@NotNull KeyMap items) { f.opts(Field.ENGINE, items); }

    /** Invoked when button($B3) is clicked */
    @NotNull public abstract Action navigate();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarFilterForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CarFilterForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull public static final CarsFilters CARS_ROW_FILTERS = new CarsFilters();
    @NotNull private static FormListenerMap<CarFilterForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CarFilterForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class FiltersRowBase
        implements FormRowInstance<FiltersRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(title). */
        @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

        /** Sets the value of the display(title). */
        @NotNull public FiltersRow setTitle(@NotNull String title) {
            f.set(Field.TITLE, title);
            return (FiltersRow) this;
        }

        /** Returns the value of the dynamic(value). */
        @NotNull public Seq<Object> getValue() { return f.getArray(Field.VALUE, Object.class); }

        /** Sets the value of the dynamic(value). */
        @NotNull public FiltersRow setValue(@NotNull Iterable<Object> value) {
            f.setArray(Field.VALUE, value);
            return (FiltersRow) this;
        }

        /** Sets the options of the dynamic(value) with the given KeyMap. */
        public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<FiltersRow> table() { return getFilters(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((FiltersRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

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

        /** Returns the value of the internal(transmission). */
        @NotNull public Transmission getTransmission() {
            return Transmission.valueOf(f.get(Field.TRANSMISSION, String.class));
        }

        /** Sets the value of the internal(transmission). */
        @NotNull public CarsRow setTransmission(@NotNull Transmission transmission) {
            f.set(Field.TRANSMISSION, transmission);
            return (CarsRow) this;
        }

        /** Returns the value of the internal(publication). */
        @NotNull public DateOnly getPublication() {
            return DateOnly.fromMilliseconds(f.get(Field.PUBLICATION, Long.class));
        }

        /** Sets the value of the internal(publication). */
        @NotNull public CarsRow setPublication(@NotNull DateOnly publication) {
            f.set(Field.PUBLICATION, publication);
            return (CarsRow) this;
        }

        /** Returns the value of the internal(expiration). */
        @NotNull public DateTime getExpiration() {
            return DateTime.fromMilliseconds(f.get(Field.EXPIRATION, Long.class));
        }

        /** Sets the value of the internal(expiration). */
        @NotNull public CarsRow setExpiration(@NotNull DateTime expiration) {
            f.set(Field.EXPIRATION, expiration);
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
            	.setTransmission(car.getTransmission());
        }

        /** Copies field values to given car instance. */
        public void copyTo(@NotNull Car car) {
            car.setMake(getMake())
            	.setModel(getModel())
            	.setYear(getYear())
            	.setEngine(getEngine())
            	.setPrice(getPrice())
            	.setMileage(getMileage())
            	.setTransmission(getTransmission());
        }

        /** Return primary key of bound {@link Car} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    public static class CarsFilters
    {

        //~ Fields ...................................................................................................................

        @NotNull public final IntegerFilter id;
        @NotNull public final EntityFilter<Make> make;
        @NotNull public final EntityFilter<Model> model;
        @NotNull public final IntegerFilter year;
        @NotNull public final EnumFilter<Engine,String> engine;
        @NotNull public final DecimalFilter price;
        @NotNull public final IntegerFilter mileage;
        @NotNull public final EnumFilter<Transmission,String> transmission;
        @NotNull public final DateFilter publication;
        @NotNull public final DateTimeFilter expiration;
        @NotNull private final FilterFactory factory = Context.getSingleton(FilterFactory.class);

        //~ Constructors .............................................................................................................

        private CarsFilters() {
            id = factory.intFilter(Field.ID, "Id");
            make = factory.entityFilter(Field.MAKE, Make.class, "Make");
            model = factory.entityFilter(Field.MODEL, Model.class, "Model");
            year = factory.intFilter(Field.YEAR, "Year");
            engine = factory.enumFilter(Field.ENGINE, Engine.class, "Engine");
            price = factory.decimalFilter(Field.PRICE, "Price");
            mileage = factory.intFilter(Field.MILEAGE, "Mileage");
            transmission = factory.enumFilter(Field.TRANSMISSION, Transmission.class, "Transmission");
            publication = factory.dateFilter(Field.PUBLICATION, "Published");
            expiration = factory.dateTimeFilter(Field.EXPIRATION, "Expires");
        }

        //~ Methods ..................................................................................................................

        /** Return a new filter options implementation. */
        @SuppressWarnings("unchecked") @NotNull public <F> CustomOptions<CarsRow,F> createFilter(@NotNull final String title, @NotNull final Options<? super CarsRow,F> options) { return factory.customFilter(title, options); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        FILTER("filter"),
        MAIN("main"),
        FILTER_PANEL("filterPanel"),
        FILTERS("filters"),
        TITLE("title"),
        VALUE("value"),
        CARS_PANEL("carsPanel"),
        CARS("cars"),
        ID("id"),
        MAKE("make"),
        MODEL("model"),
        YEAR("year"),
        ENGINE("engine"),
        PRICE("price"),
        MILEAGE("mileage"),
        TRANSMISSION("transmission"),
        PUBLICATION("publication"),
        EXPIRATION("expiration"),
        $B3("$B3");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
