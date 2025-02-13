package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.FlightChooserWidget.OptionsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightChooserWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightChooserWidgetBase
    extends WidgetInstance<FlightChooser>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightChooser instance. */
    public void populate(@NotNull FlightChooser flightChooser) {
        setOrigin(flightChooser.getOrigin())
        	.setDestiny(flightChooser.getDestiny())
        	.setFrom(flightChooser.getFrom())
        	.setTo(flightChooser.getTo());

        getOptions().populate(flightChooser.getOptions(), OptionsRow::populate);
    }

    /** Copies field values to given flightChooser instance. */
    public void copyTo(@NotNull FlightChooser flightChooser) {
        flightChooser.setOrigin(getOrigin())
        	.setDestiny(getDestiny())
        	.setFrom(getFrom())
        	.setTo(getTo());

        getOptions().mergeInto(flightChooser.getOptions(), OptionsRow::copyTo, FlightOption::new);
    }

    /** Returns the value of the text_field(origin). */
    @NotNull public String getOrigin() { return f.get(Field.ORIGIN, String.class); }

    /** Sets the value of the text_field(origin). */
    @NotNull public FlightChooserWidget setOrigin(@NotNull String origin) {
        f.set(Field.ORIGIN, origin);
        return (FlightChooserWidget) this;
    }

    /** Returns the value of the text_field(destiny). */
    @NotNull public String getDestiny() { return f.get(Field.DESTINY, String.class); }

    /** Sets the value of the text_field(destiny). */
    @NotNull public FlightChooserWidget setDestiny(@NotNull String destiny) {
        f.set(Field.DESTINY, destiny);
        return (FlightChooserWidget) this;
    }

    /** Returns the value of the date_box(from). */
    @NotNull public DateOnly getFrom() {
        return DateOnly.fromMilliseconds(f.get(Field.FROM, Long.class));
    }

    /** Sets the value of the date_box(from). */
    @NotNull public FlightChooserWidget setFrom(@NotNull DateOnly from) {
        f.set(Field.FROM, from);
        return (FlightChooserWidget) this;
    }

    /** Returns the value of the date_box(to). */
    @NotNull public DateOnly getTo() {
        return DateOnly.fromMilliseconds(f.get(Field.TO, Long.class));
    }

    /** Sets the value of the date_box(to). */
    @NotNull public FlightChooserWidget setTo(@NotNull DateOnly to) {
        f.set(Field.TO, to);
        return (FlightChooserWidget) this;
    }

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action search();

    /** Returns a {@link FormTable<OptionsRow>} instance to handle Options manipulation */
    @NotNull public final FormTable<OptionsRow> getOptions() { return table(Field.OPTIONS, OptionsRow.class); }

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

    @NotNull private static final Logger logger = Logger.getLogger(FlightChooserWidget.class);

    //~ Inner Classes ............................................................................................................

    public abstract class OptionsRowBase
        implements FormRowInstance<OptionsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Define {@link FlightOptionWidget instance} to be used during interaction. */
        @NotNull FlightOptionWidget defineOption() { return new FlightOptionWidget(); }

        /** Get widget {@link FlightOptionWidget}. */
        @NotNull public FlightOptionWidget getOption() { return f.widget(Field.OPTION, FlightOptionWidget.class); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<OptionsRow> table() { return getOptions(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((OptionsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given flightOption instance. */
        public void populate(@NotNull FlightOption flightOption) {

            getOption().populate(flightOption);
        }

        /** Copies field values to given flightOption instance. */
        public void copyTo(@NotNull FlightOption flightOption) {

            getOption().copyTo(flightOption);
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $V0("$V0"),
        ORIGIN("origin"),
        DESTINY("destiny"),
        FROM("from"),
        TO("to"),
        BUTTON("button"),
        $V1("$V1"),
        OPTIONS("options"),
        OPTION("option");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
