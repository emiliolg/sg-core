package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.DateOnly;
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
 * Generated base class for form: DateEntityForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DateEntityFormBase
    extends FormInstance<DateEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final DateEntity dateEntity = DateEntity.create();
        copyTo(dateEntity);
        dateEntity.insert();
        setId(dateEntity.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final DateEntity dateEntity = find();
        copyTo(dateEntity);
        dateEntity.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public DateEntity find() {
        final DateEntity value = DateEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public DateEntity populate() {
        final DateEntity dateEntity = find();

        setDate(dateEntity.getDate());

        return dateEntity;
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

    /** Copies field values to given dateEntity instance. */
    public void copyTo(@NotNull DateEntity dateEntity) { dateEntity.setDate(getDate()); }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public DateEntityForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (DateEntityForm) this;
    }

    /** Returns the value of the date_box(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the date_box(date). */
    @NotNull public DateEntityForm setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (DateEntityForm) this;
    }

    /** Invoked when double_date_box(leaveDate) value ui changes */
    @NotNull public abstract Action changeReturnDate();

    /** Returns the value of the double_date_box(leaveDate). */
    @Nullable public DateOnly getLeaveDate() {
        final Long val = f.get(Field.LEAVE_DATE, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the double_date_box(leaveDate). */
    @NotNull public DateEntityForm setLeaveDate(@Nullable DateOnly leaveDate) {
        f.set(Field.LEAVE_DATE, leaveDate);
        return (DateEntityForm) this;
    }

    /** Returns the value of the double_date_box(returnDate). */
    @Nullable public DateOnly getReturnDate() {
        final Long val = f.get(Field.RETURN_DATE, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the double_date_box(returnDate). */
    @NotNull public DateEntityForm setReturnDate(@Nullable DateOnly returnDate) {
        f.set(Field.RETURN_DATE, returnDate);
        return (DateEntityForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DateEntityForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DateEntityForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DateEntityForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DateEntityForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        DATE("date"),
        LEAVE_DATE("leaveDate"),
        RETURN_DATE("returnDate"),
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
