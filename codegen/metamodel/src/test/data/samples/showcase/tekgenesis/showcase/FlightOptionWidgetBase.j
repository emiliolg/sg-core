package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightOptionWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightOptionWidgetBase
    extends WidgetInstance<FlightOption>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightOption instance. */
    public void populate(@NotNull FlightOption flightOption) {

        getOut().populate(flightOption.getOut());

        getIn().populate(flightOption.getIn());

        getPrice().populate(flightOption.getPrice());
    }

    /** Copies field values to given flightOption instance. */
    public void copyTo(@NotNull FlightOption flightOption) {

        getOut().copyTo(flightOption.getOut());

        getIn().copyTo(flightOption.getIn());

        getPrice().copyTo(flightOption.getPrice());
    }

    /** Define {@link FlightRouteWidget instance} to be used during interaction. */
    @NotNull FlightRouteWidget defineOut() { return new FlightRouteWidget(); }

    /** Get widget {@link FlightRouteWidget}. */
    @NotNull public FlightRouteWidget getOut() { return f.widget(Field.OUT, FlightRouteWidget.class); }

    /** Define {@link FlightRouteWidget instance} to be used during interaction. */
    @NotNull FlightRouteWidget defineIn() { return new FlightRouteWidget(); }

    /** Get widget {@link FlightRouteWidget}. */
    @NotNull public FlightRouteWidget getIn() { return f.widget(Field.IN, FlightRouteWidget.class); }

    /** Define {@link FlightPriceWidget instance} to be used during interaction. */
    @NotNull FlightPriceWidget definePrice() { return new FlightPriceWidget(); }

    /** Get widget {@link FlightPriceWidget}. */
    @NotNull public FlightPriceWidget getPrice() { return f.widget(Field.PRICE, FlightPriceWidget.class); }

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

    @NotNull private static final Logger logger = Logger.getLogger(FlightOptionWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $V1("$V1"),
        OUT("out"),
        IN("in"),
        $V2("$V2"),
        PRICE("price");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
