package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.ClientShowcase.AddressesRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.ClientShowcase.IndicatorsRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ClientShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ClientShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ClientShowcase");
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

    /** Returns a {@link FormTable<IndicatorsRow>} instance to handle Indicators manipulation */
    @NotNull public final FormTable<IndicatorsRow> getIndicators() { return table(Field.INDICATORS, IndicatorsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<AddressesRow>} instance to handle Addresses manipulation */
    @NotNull public final FormTable<AddressesRow> getAddresses() { return table(Field.ADDRESSES, AddressesRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClientShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClientShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ClientShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ClientShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class IndicatorsRowBase
        implements FormRowInstance<IndicatorsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(indicatorIcon). */
        @Nullable public String getIndicatorIcon() { return f.get(Field.INDICATOR_ICON, String.class); }

        /** Sets the value of the internal(indicatorIcon). */
        @NotNull public IndicatorsRow setIndicatorIcon(@Nullable String indicatorIcon) {
            f.set(Field.INDICATOR_ICON, indicatorIcon);
            return (IndicatorsRow) this;
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
        @NotNull public final FormTable<IndicatorsRow> table() { return getIndicators(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((IndicatorsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class AddressesRowBase
        implements FormRowInstance<AddressesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(addressName). */
        @NotNull public String getAddressName() { return f.get(Field.ADDRESS_NAME, String.class); }

        /** Sets the value of the display(addressName). */
        @NotNull public AddressesRow setAddressName(@NotNull String addressName) {
            f.set(Field.ADDRESS_NAME, addressName);
            return (AddressesRow) this;
        }

        /** Returns the value of the display(addressLine). */
        @Nullable public String getAddressLine() { return f.get(Field.ADDRESS_LINE, String.class); }

        /** Sets the value of the display(addressLine). */
        @NotNull public AddressesRow setAddressLine(@Nullable String addressLine) {
            f.set(Field.ADDRESS_LINE, addressLine);
            return (AddressesRow) this;
        }

        /** Returns the value of the display(addressCity). */
        @Nullable public String getAddressCity() { return f.get(Field.ADDRESS_CITY, String.class); }

        /** Sets the value of the display(addressCity). */
        @NotNull public AddressesRow setAddressCity(@Nullable String addressCity) {
            f.set(Field.ADDRESS_CITY, addressCity);
            return (AddressesRow) this;
        }

        /** Returns the value of the display(addressStateCountry). */
        @Nullable public String getAddressStateCountry() { return f.get(Field.ADDRESS_STATE_COUNTRY, String.class); }

        /** Sets the value of the display(addressStateCountry). */
        @NotNull public AddressesRow setAddressStateCountry(@Nullable String addressStateCountry) {
            f.set(Field.ADDRESS_STATE_COUNTRY, addressStateCountry);
            return (AddressesRow) this;
        }

        /** Returns the value of the toggle_button(mainAddress). */
        public boolean isMainAddress() { return f.get(Field.MAIN_ADDRESS, Boolean.class); }

        /** Sets the value of the toggle_button(mainAddress). */
        @NotNull public AddressesRow setMainAddress(boolean mainAddress) {
            f.set(Field.MAIN_ADDRESS, mainAddress);
            return (AddressesRow) this;
        }

        /** Returns the value of the toggle_button(deprecateAddress). */
        public boolean isDeprecateAddress() { return f.get(Field.DEPRECATE_ADDRESS, Boolean.class); }

        /** Sets the value of the toggle_button(deprecateAddress). */
        @NotNull public AddressesRow setDeprecateAddress(boolean deprecateAddress) {
            f.set(Field.DEPRECATE_ADDRESS, deprecateAddress);
            return (AddressesRow) this;
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
        @NotNull public final FormTable<AddressesRow> table() { return getAddresses(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((AddressesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $M2("$M2"),
        $H3("$H3"),
        INDICATORS("indicators"),
        INDICATOR_ICON("indicatorIcon"),
        INDICATOR("indicator"),
        REFERENCE("reference"),
        GOLD_ICON("goldIcon"),
        SILVER_ICON("silverIcon"),
        BRONZE_ICON("bronzeIcon"),
        $H4("$H4"),
        $V5("$V5"),
        $I6("$I6"),
        $C7("$C7"),
        $T8("$T8"),
        $I9("$I9"),
        $T10("$T10"),
        $T11("$T11"),
        $I12("$I12"),
        $T13("$T13"),
        $T14("$T14"),
        $I15("$I15"),
        $C16("$C16"),
        $D17("$D17"),
        $T18("$T18"),
        $V19("$V19"),
        $D20("$D20"),
        $D21("$D21"),
        $H22("$H22"),
        ADDRESSES("addresses"),
        $H23("$H23"),
        $V24("$V24"),
        ADDRESS_NAME("addressName"),
        ADDRESS_LINE("addressLine"),
        ADDRESS_CITY("addressCity"),
        ADDRESS_STATE_COUNTRY("addressStateCountry"),
        $V25("$V25"),
        MAIN_ADDRESS("mainAddress"),
        DEPRECATE_ADDRESS("deprecateAddress"),
        ADD_ADDRESS_BUTTON("addAddressButton");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
