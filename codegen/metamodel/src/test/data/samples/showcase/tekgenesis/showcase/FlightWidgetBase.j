package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.FlightWidget.SectionsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightWidgetBase
    extends WidgetInstance<FlightType>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightType instance. */
    public void populate(@NotNull FlightType flightType) {
        setIdKey(flightType.getIdKey())
        	.setName(flightType.getName())
        	.setFrom(flightType.getFrom())
        	.setTo(flightType.getTo())
        	.setPrice(flightType.getPrice());

        getSections().populate(flightType.getSchedules(), SectionsRow::populate);
    }

    /** Copies field values to given flightType instance. */
    public void copyTo(@NotNull FlightType flightType) {
        flightType.setIdKey(getIdKey())
        	.setName(getName())
        	.setFrom(getFrom())
        	.setTo(getTo())
        	.setPrice(getPrice());

        getSections().mergeInto(flightType.getSchedules(), SectionsRow::copyTo, FlightSchedule::new);
    }

    /** Returns the value of the internal(displaying). */
    public boolean isDisplaying() { return f.get(Field.DISPLAYING, Boolean.class); }

    /** Sets the value of the internal(displaying). */
    @NotNull public FlightWidget setDisplaying(boolean displaying) {
        f.set(Field.DISPLAYING, displaying);
        return (FlightWidget) this;
    }

    /** Returns the value of the internal(flightNr). */
    public int getFlightNr() { return f.get(Field.FLIGHT_NR, Integer.class); }

    /** Sets the value of the internal(flightNr). */
    @NotNull public FlightWidget setFlightNr(int flightNr) {
        f.set(Field.FLIGHT_NR, flightNr);
        return (FlightWidget) this;
    }

    /** Returns the value of the text_field(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the text_field(idKey). */
    @NotNull public FlightWidget setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (FlightWidget) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public FlightWidget setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (FlightWidget) this;
    }

    /** Returns the value of the text_field(from). */
    @NotNull public String getFrom() { return f.get(Field.FROM, String.class); }

    /** Sets the value of the text_field(from). */
    @NotNull public FlightWidget setFrom(@NotNull String from) {
        f.set(Field.FROM, from);
        return (FlightWidget) this;
    }

    /** Returns the value of the text_field(to). */
    @NotNull public String getTo() { return f.get(Field.TO, String.class); }

    /** Sets the value of the text_field(to). */
    @NotNull public FlightWidget setTo(@NotNull String to) {
        f.set(Field.TO, to);
        return (FlightWidget) this;
    }

    /** Returns the value of the text_field(price). */
    public int getPrice() { return f.get(Field.PRICE, Integer.class); }

    /** Sets the value of the text_field(price). */
    @NotNull public FlightWidget setPrice(int price) {
        f.set(Field.PRICE, price);
        return (FlightWidget) this;
    }

    /** Invoked when button(add) is clicked */
    @NotNull public abstract Action addRow();

    /** Returns a {@link FormTable<SectionsRow>} instance to handle Sections manipulation */
    @NotNull public final FormTable<SectionsRow> getSections() { return table(Field.SECTIONS, SectionsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

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

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(FlightWidget.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SectionsRowBase
        implements FormRowInstance<SectionsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(fromSchedule) value changes */
        @NotNull public abstract Action fromChanged();

        /** Returns the value of the text_field(fromSchedule). */
        @NotNull public String getFromSchedule() { return f.get(Field.FROM_SCHEDULE, String.class); }

        /** Sets the value of the text_field(fromSchedule). */
        @NotNull public SectionsRow setFromSchedule(@NotNull String fromSchedule) {
            f.set(Field.FROM_SCHEDULE, fromSchedule);
            return (SectionsRow) this;
        }

        /** Returns the value of the text_field(toSchedule). */
        @Nullable public String getToSchedule() { return f.get(Field.TO_SCHEDULE, String.class); }

        /** Sets the value of the text_field(toSchedule). */
        @NotNull public SectionsRow setToSchedule(@Nullable String toSchedule) {
            f.set(Field.TO_SCHEDULE, toSchedule);
            return (SectionsRow) this;
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
        @NotNull public final FormTable<SectionsRow> table() { return getSections(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SectionsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given flightSchedule instance. */
        public void populate(@NotNull FlightSchedule flightSchedule) {
            setFromSchedule(flightSchedule.getFromSchedule())
            	.setToSchedule(flightSchedule.getToSchedule());
        }

        /** Copies field values to given flightSchedule instance. */
        public void copyTo(@NotNull FlightSchedule flightSchedule) {
            flightSchedule.setFromSchedule(getFromSchedule())
            	.setToSchedule(getToSchedule());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        DISPLAYING("displaying"),
        FLIGHT_NR("flightNr"),
        INFO("info"),
        ID_KEY("idKey"),
        NAME("name"),
        FROM("from"),
        TO("to"),
        PRICE("price"),
        SCHEDULE("schedule"),
        ADD("add"),
        SECTIONS("sections"),
        FROM_SCHEDULE("fromSchedule"),
        TO_SCHEDULE("toSchedule");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
