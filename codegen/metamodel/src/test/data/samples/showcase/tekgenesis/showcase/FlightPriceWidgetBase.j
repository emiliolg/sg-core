package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: FlightPriceWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FlightPriceWidgetBase
    extends WidgetInstance<FlightPrice>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Populate field values with given flightPrice instance. */
    public void populate(@NotNull FlightPrice flightPrice) {
        setPrice(flightPrice.getPrice())
        	.setTax(flightPrice.getTax());
    }

    /** Copies field values to given flightPrice instance. */
    public void copyTo(@NotNull FlightPrice flightPrice) {
        flightPrice.setPrice(getPrice())
        	.setTax(getTax());
    }

    /** Returns the value of the display(price). */
    @NotNull public BigDecimal getPrice() { return f.get(Field.PRICE, BigDecimal.class); }

    /** Sets the value of the display(price). */
    @NotNull public FlightPriceWidget setPrice(@NotNull BigDecimal price) {
        f.set(Field.PRICE, Decimals.scaleAndCheck("price", price, false, 10, 2));
        return (FlightPriceWidget) this;
    }

    /** Returns the value of the display(tax). */
    @NotNull public BigDecimal getTax() { return f.get(Field.TAX, BigDecimal.class); }

    /** Sets the value of the display(tax). */
    @NotNull public FlightPriceWidget setTax(@NotNull BigDecimal tax) {
        f.set(Field.TAX, Decimals.scaleAndCheck("tax", tax, false, 10, 2));
        return (FlightPriceWidget) this;
    }

    /** Invoked when button(reserve) is clicked */
    @NotNull public abstract Action reserve();

    /** Invoked when button(buy) is clicked */
    @NotNull public abstract Action buy();

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

    @NotNull private static final Logger logger = Logger.getLogger(FlightPriceWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $D1("$D1"),
        $V2("$V2"),
        PRICE("price"),
        TAX("tax"),
        $H3("$H3"),
        RESERVE("reserve"),
        BUY("buy");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
