package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.showcase.FlightInfo.FlightsTableRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.FlightInfo.PriceTableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FlightInfo.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightInfoBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FlightInfo");
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

    /** Invoked when internal(tax) value changes */
    @NotNull public abstract Action taxChange();

    /** Returns the value of the internal(tax). */
    @NotNull public BigDecimal getTax() { return f.get(Field.TAX, BigDecimal.class); }

    /** Sets the value of the internal(tax). */
    @NotNull public FlightInfo setTax(@NotNull BigDecimal tax) {
        f.set(Field.TAX, Decimals.scaleAndCheck("tax", tax, false, 10, 2));
        return (FlightInfo) this;
    }

    /** Returns a {@link FormTable<FlightsTableRow>} instance to handle FlightsTable manipulation */
    @NotNull public final FormTable<FlightsTableRow> getFlightsTable() { return table(Field.FLIGHTS_TABLE, FlightsTableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the display(initialPrice). */
    @NotNull public BigDecimal getInitialPrice() { return f.get(Field.INITIAL_PRICE, BigDecimal.class); }

    /** Sets the value of the display(initialPrice). */
    @NotNull public FlightInfo setInitialPrice(@NotNull BigDecimal initialPrice) {
        f.set(Field.INITIAL_PRICE, Decimals.scaleAndCheck("initialPrice", initialPrice, false, 10, 2));
        return (FlightInfo) this;
    }

    /** Returns the value of the display(infoText). */
    @NotNull public String getInfoText() { return f.get(Field.INFO_TEXT, String.class); }

    /** Returns a {@link FormTable<PriceTableRow>} instance to handle PriceTable manipulation */
    @NotNull public final FormTable<PriceTableRow> getPriceTable() { return table(Field.PRICE_TABLE, PriceTableRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FlightInfo> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FlightInfo> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FlightInfo> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FlightInfo.class);

    //~ Inner Classes ............................................................................................................

    public abstract class FlightsTableRowBase
        implements FormRowInstance<FlightsTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(rowType). */
        @NotNull public RowType getRowType() { return RowType.valueOf(f.get(Field.ROW_TYPE, String.class)); }

        /** Sets the value of the internal(rowType). */
        @NotNull public FlightsTableRow setRowType(@NotNull RowType rowType) {
            f.set(Field.ROW_TYPE, rowType);
            return (FlightsTableRow) this;
        }

        /** Returns the value of the display(leaveTime). */
        @NotNull public String getLeaveTime() { return f.get(Field.LEAVE_TIME, String.class); }

        /** Sets the value of the display(leaveTime). */
        @NotNull public FlightsTableRow setLeaveTime(@NotNull String leaveTime) {
            f.set(Field.LEAVE_TIME, leaveTime);
            return (FlightsTableRow) this;
        }

        /** Returns the value of the display(arriveTime). */
        @NotNull public String getArriveTime() { return f.get(Field.ARRIVE_TIME, String.class); }

        /** Sets the value of the display(arriveTime). */
        @NotNull public FlightsTableRow setArriveTime(@NotNull String arriveTime) {
            f.set(Field.ARRIVE_TIME, arriveTime);
            return (FlightsTableRow) this;
        }

        /** Returns the value of the display(flightDuration). */
        @NotNull public String getFlightDuration() { return f.get(Field.FLIGHT_DURATION, String.class); }

        /** Sets the value of the display(flightDuration). */
        @NotNull public FlightsTableRow setFlightDuration(@NotNull String flightDuration) {
            f.set(Field.FLIGHT_DURATION, flightDuration);
            return (FlightsTableRow) this;
        }

        /** Returns the value of the display(airline). */
        @NotNull public String getAirline() { return f.get(Field.AIRLINE, String.class); }

        /** Sets the value of the display(airline). */
        @NotNull public FlightsTableRow setAirline(@NotNull String airline) {
            f.set(Field.AIRLINE, airline);
            return (FlightsTableRow) this;
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
        @NotNull public final FormTable<FlightsTableRow> table() { return getFlightsTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((FlightsTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class PriceTableRowBase
        implements FormRowInstance<PriceTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(description). */
        @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the display(description). */
        @NotNull public PriceTableRow setDescription(@NotNull String description) {
            f.set(Field.DESCRIPTION, description);
            return (PriceTableRow) this;
        }

        /** Returns the value of the display(price). */
        @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

        /** Sets the value of the display(price). */
        @NotNull public PriceTableRow setPrice(@NotNull BigDecimal price) {
            f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
            return (PriceTableRow) this;
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
        @NotNull public final FormTable<PriceTableRow> table() { return getPriceTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PriceTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        TAX("tax"),
        $H0("$H0"),
        FLIGHTS_TABLE("flightsTable"),
        ROW_TYPE("rowType"),
        LEAVE_TIME("leaveTime"),
        ARRIVE_TIME("arriveTime"),
        FLIGHT_DURATION("flightDuration"),
        AIRLINE("airline"),
        $V1("$V1"),
        $H2("$H2"),
        INITIAL_PRICE("initialPrice"),
        $H3("$H3"),
        INFO_TEXT("infoText"),
        PRICE_TABLE("priceTable"),
        DESCRIPTION("description"),
        PRICE("price");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
