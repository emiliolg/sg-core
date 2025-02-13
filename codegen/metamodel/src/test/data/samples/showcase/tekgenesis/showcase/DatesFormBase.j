package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DatesForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DatesFormBase
    extends FormInstance<DateShowcase>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final DateShowcase dateShowcase = DateShowcase.create(getId());
        copyTo(dateShowcase);
        dateShowcase.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final DateShowcase dateShowcase = find();
        copyTo(dateShowcase);
        dateShowcase.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public DateShowcase find() {
        final DateShowcase value = DateShowcase.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public DateShowcase populate() {
        final DateShowcase dateShowcase = find();

        setDateFrom(dateShowcase.getDateFrom())
        	.setDateTo(dateShowcase.getDateTo())
        	.setTimeFrom(dateShowcase.getTimeFrom())
        	.setTimeTo(dateShowcase.getTimeTo())
        	.setDoubleDateFrom(dateShowcase.getDoubleDateFrom())
        	.setDoubleDateTo(dateShowcase.getDoubleDateTo())
        	.setDateCombo(dateShowcase.getDateCombo())
        	.setDateCombo1(dateShowcase.getDateCombo1());

        return dateShowcase;
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

    /** Copies field values to given dateShowcase instance. */
    public void copyTo(@NotNull DateShowcase dateShowcase) {
        dateShowcase.setDateFrom(getDateFrom())
        	.setDateTo(getDateTo())
        	.setTimeFrom(getTimeFrom())
        	.setTimeTo(getTimeTo())
        	.setDoubleDateFrom(getDoubleDateFrom())
        	.setDoubleDateTo(getDoubleDateTo())
        	.setDateCombo(getDateCombo())
        	.setDateCombo1(getDateCombo1());
    }

    /** Returns the value of the text_field(id). */
    public int getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the text_field(id). */
    @NotNull public DatesForm setId(int id) {
        f.set(Field.ID, id);
        return (DatesForm) this;
    }

    /** 
     * Invoked when date_box(dateFrom) value changes
     * Invoked when date_box(dateTo) value changes
     * Invoked when date_time_box(timeFrom) value changes
     * Invoked when date_time_box(timeTo) value changes
     * Invoked when double_date_box(doubleDateFrom) value changes
     * Invoked when double_date_box(doubleDateTo) value changes
     * Invoked when combo_date_box(dateCombo) value changes
     * Invoked when combo_date_box(dateCombo1) value changes
     */
    @NotNull public abstract Action show();

    /** Returns the value of the date_box(dateFrom). */
    @Nullable public DateOnly getDateFrom() {
        final Long val = f.get(Field.DATE_FROM, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the date_box(dateFrom). */
    @NotNull public DatesForm setDateFrom(@Nullable DateOnly dateFrom) {
        f.set(Field.DATE_FROM, dateFrom);
        return (DatesForm) this;
    }

    /** Returns the value of the date_box(dateTo). */
    @Nullable public DateOnly getDateTo() {
        final Long val = f.get(Field.DATE_TO, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the date_box(dateTo). */
    @NotNull public DatesForm setDateTo(@Nullable DateOnly dateTo) {
        f.set(Field.DATE_TO, dateTo);
        return (DatesForm) this;
    }

    /** Returns the value of the date_picker(datePickerFrom). */
    @NotNull public DateOnly getDatePickerFrom() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE_PICKER_FROM, Long.class));
    }

    /** Sets the value of the date_picker(datePickerFrom). */
    @NotNull public DatesForm setDatePickerFrom(@NotNull DateOnly datePickerFrom) {
        f.set(Field.DATE_PICKER_FROM, datePickerFrom);
        return (DatesForm) this;
    }

    /** Returns the value of the date_picker(datePickerTo). */
    @NotNull public DateOnly getDatePickerTo() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE_PICKER_TO, Long.class));
    }

    /** Sets the value of the date_picker(datePickerTo). */
    @NotNull public DatesForm setDatePickerTo(@NotNull DateOnly datePickerTo) {
        f.set(Field.DATE_PICKER_TO, datePickerTo);
        return (DatesForm) this;
    }

    /** Returns the value of the date_picker(datePickerDefault). */
    @NotNull public DateOnly getDatePickerDefault() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE_PICKER_DEFAULT, Long.class));
    }

    /** Sets the value of the date_picker(datePickerDefault). */
    @NotNull public DatesForm setDatePickerDefault(@NotNull DateOnly datePickerDefault) {
        f.set(Field.DATE_PICKER_DEFAULT, datePickerDefault);
        return (DatesForm) this;
    }

    /** Returns the value of the date_time_box(timeFrom). */
    @Nullable public DateTime getTimeFrom() {
        final Long val = f.get(Field.TIME_FROM, Long.class);
        return val == null ? null : DateTime.fromMilliseconds(val);
    }

    /** Sets the value of the date_time_box(timeFrom). */
    @NotNull public DatesForm setTimeFrom(@Nullable DateTime timeFrom) {
        f.set(Field.TIME_FROM, timeFrom);
        return (DatesForm) this;
    }

    /** Returns the value of the date_time_box(timeTo). */
    @Nullable public DateTime getTimeTo() {
        final Long val = f.get(Field.TIME_TO, Long.class);
        return val == null ? null : DateTime.fromMilliseconds(val);
    }

    /** Sets the value of the date_time_box(timeTo). */
    @NotNull public DatesForm setTimeTo(@Nullable DateTime timeTo) {
        f.set(Field.TIME_TO, timeTo);
        return (DatesForm) this;
    }

    /** Returns the value of the double_date_box(doubleDateFrom). */
    @Nullable public DateOnly getDoubleDateFrom() {
        final Long val = f.get(Field.DOUBLE_DATE_FROM, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the double_date_box(doubleDateFrom). */
    @NotNull public DatesForm setDoubleDateFrom(@Nullable DateOnly doubleDateFrom) {
        f.set(Field.DOUBLE_DATE_FROM, doubleDateFrom);
        return (DatesForm) this;
    }

    /** Returns the value of the double_date_box(doubleDateTo). */
    @Nullable public DateOnly getDoubleDateTo() {
        final Long val = f.get(Field.DOUBLE_DATE_TO, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the double_date_box(doubleDateTo). */
    @NotNull public DatesForm setDoubleDateTo(@Nullable DateOnly doubleDateTo) {
        f.set(Field.DOUBLE_DATE_TO, doubleDateTo);
        return (DatesForm) this;
    }

    /** Returns the value of the combo_date_box(dateCombo). */
    @Nullable public DateOnly getDateCombo() {
        final Long val = f.get(Field.DATE_COMBO, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the combo_date_box(dateCombo). */
    @NotNull public DatesForm setDateCombo(@Nullable DateOnly dateCombo) {
        f.set(Field.DATE_COMBO, dateCombo);
        return (DatesForm) this;
    }

    /** Returns the value of the combo_date_box(dateCombo1). */
    @Nullable public DateOnly getDateCombo1() {
        final Long val = f.get(Field.DATE_COMBO1, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the combo_date_box(dateCombo1). */
    @NotNull public DatesForm setDateCombo1(@Nullable DateOnly dateCombo1) {
        f.set(Field.DATE_COMBO1, dateCombo1);
        return (DatesForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DatesForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DatesForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DatesForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DatesForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        VIEW("view"),
        ID("id"),
        DATE_FROM("dateFrom"),
        DATE_TO("dateTo"),
        DATE_PICKER_FROM("datePickerFrom"),
        DATE_PICKER_TO("datePickerTo"),
        DATE_PICKER_DEFAULT("datePickerDefault"),
        TIME_FROM("timeFrom"),
        TIME_TO("timeTo"),
        DOUBLE_DATE_FROM("doubleDateFrom"),
        DOUBLE_DATE_TO("doubleDateTo"),
        DATE_COMBO("dateCombo"),
        DATE_COMBO1("dateCombo1"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
