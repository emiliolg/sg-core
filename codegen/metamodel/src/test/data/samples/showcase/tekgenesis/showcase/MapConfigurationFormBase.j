package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.MapConfigurationForm.ArgentinaRow;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.showcase.MapConfigurationForm.EthiopiaRow;
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
import tekgenesis.showcase.MapConfigurationForm.PilarRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: MapConfigurationForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class MapConfigurationFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.MapConfigurationForm");
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

    /** Returns the value of the text_field(markerSize). */
    @NotNull public BigDecimal getMarkerSize() { return f.get(Field.MARKER_SIZE, BigDecimal.class); }

    /** Sets the value of the text_field(markerSize). */
    @NotNull public MapConfigurationForm setMarkerSize(@NotNull BigDecimal markerSize) {
        f.set(Field.MARKER_SIZE, Decimals.scaleAndCheck("markerSize", markerSize, false, 4, 2));
        return (MapConfigurationForm) this;
    }

    /** Returns the value of the combo_box(color). */
    @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

    /** Sets the value of the combo_box(color). */
    @NotNull public MapConfigurationForm setColor(@NotNull String color) {
        f.set(Field.COLOR, color);
        return (MapConfigurationForm) this;
    }

    /** Sets the options of the combo_box(color). */
    public void setColorOptions(@NotNull Iterable<String> items) { f.opts(Field.COLOR, items); }

    /** Sets the options of the combo_box(color) with the given KeyMap. */
    public void setColorOptions(@NotNull KeyMap items) { f.opts(Field.COLOR, items); }

    /** Invoked when button($B3) is clicked */
    @NotNull public abstract Action changeSize();

    /** Returns a {@link FormTable<ArgentinaRow>} instance to handle Argentina manipulation */
    @NotNull public final FormTable<ArgentinaRow> getArgentina() { return table(Field.ARGENTINA, ArgentinaRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<EthiopiaRow>} instance to handle Ethiopia manipulation */
    @NotNull public final FormTable<EthiopiaRow> getEthiopia() { return table(Field.ETHIOPIA, EthiopiaRow.class); }

    /** Returns a {@link FormTable<PilarRow>} instance to handle Pilar manipulation */
    @NotNull public final FormTable<PilarRow> getPilar() { return table(Field.PILAR, PilarRow.class); }

    /** Invoked when button(zoomIn) is clicked */
    @NotNull public abstract Action zoomIn();

    /** Invoked when button(zoomOut) is clicked */
    @NotNull public abstract Action zoomOut();

    /** Invoked when button(resolution3) is clicked */
    @NotNull public abstract Action size1280();

    /** Invoked when button(resolution2) is clicked */
    @NotNull public abstract Action size854();

    /** Invoked when button(resolution1) is clicked */
    @NotNull public abstract Action size640();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MapConfigurationForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MapConfigurationForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<MapConfigurationForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(MapConfigurationForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ArgentinaRowBase
        implements FormRowInstance<ArgentinaRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(argLat). */
        public double getArgLat() { return f.get(Field.ARG_LAT, Double.class); }

        /** Sets the value of the text_field(argLat). */
        @NotNull public ArgentinaRow setArgLat(double argLat) {
            f.set(Field.ARG_LAT, argLat);
            return (ArgentinaRow) this;
        }

        /** Returns the value of the text_field(argLng). */
        public double getArgLng() { return f.get(Field.ARG_LNG, Double.class); }

        /** Sets the value of the text_field(argLng). */
        @NotNull public ArgentinaRow setArgLng(double argLng) {
            f.set(Field.ARG_LNG, argLng);
            return (ArgentinaRow) this;
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
        @NotNull public final FormTable<ArgentinaRow> table() { return getArgentina(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ArgentinaRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class EthiopiaRowBase
        implements FormRowInstance<EthiopiaRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(etiLat). */
        public double getEtiLat() { return f.get(Field.ETI_LAT, Double.class); }

        /** Sets the value of the text_field(etiLat). */
        @NotNull public EthiopiaRow setEtiLat(double etiLat) {
            f.set(Field.ETI_LAT, etiLat);
            return (EthiopiaRow) this;
        }

        /** Returns the value of the text_field(etiLng). */
        public double getEtiLng() { return f.get(Field.ETI_LNG, Double.class); }

        /** Sets the value of the text_field(etiLng). */
        @NotNull public EthiopiaRow setEtiLng(double etiLng) {
            f.set(Field.ETI_LNG, etiLng);
            return (EthiopiaRow) this;
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
        @NotNull public final FormTable<EthiopiaRow> table() { return getEthiopia(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((EthiopiaRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class PilarRowBase
        implements FormRowInstance<PilarRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(pilLat). */
        public double getPilLat() { return f.get(Field.PIL_LAT, Double.class); }

        /** Sets the value of the text_field(pilLat). */
        @NotNull public PilarRow setPilLat(double pilLat) {
            f.set(Field.PIL_LAT, pilLat);
            return (PilarRow) this;
        }

        /** Returns the value of the text_field(pilLng). */
        public double getPilLng() { return f.get(Field.PIL_LNG, Double.class); }

        /** Sets the value of the text_field(pilLng). */
        @NotNull public PilarRow setPilLng(double pilLng) {
            f.set(Field.PIL_LNG, pilLng);
            return (PilarRow) this;
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
        @NotNull public final FormTable<PilarRow> table() { return getPilar(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PilarRow) this);
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
        $L2("$L2"),
        MARKER_SIZE("markerSize"),
        COLOR("color"),
        $B3("$B3"),
        ARGENTINA("argentina"),
        ARG_LAT("argLat"),
        ARG_LNG("argLng"),
        ETHIOPIA("ethiopia"),
        ETI_LAT("etiLat"),
        ETI_LNG("etiLng"),
        PILAR("pilar"),
        PIL_LAT("pilLat"),
        PIL_LNG("pilLng"),
        $H4("$H4"),
        ZOOM_IN("zoomIn"),
        ZOOM_OUT("zoomOut"),
        RESOLUTION3("resolution3"),
        RESOLUTION2("resolution2"),
        RESOLUTION1("resolution1");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
