package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.showcase.NestedOnChangesWidgetForm.WidgetsRow;

/** 
 * Generated base class for form: NestedOnChangesWidgetForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class NestedOnChangesWidgetFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.NestedOnChangesWidgetForm");
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

    /** Returns a {@link FormTable<WidgetsRow>} instance to handle Widgets manipulation */
    @NotNull public final FormTable<WidgetsRow> getWidgets() { return table(Field.WIDGETS, WidgetsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Invoked when text_field(sum) value changes */
    @NotNull public abstract Action sumChanged();

    /** Returns the value of the text_field(sum). */
    public int getSum() { return f.get(Field.SUM, Integer.class); }

    /** Returns the value of the text_field(calls). */
    @NotNull public String getCalls() { return f.get(Field.CALLS, String.class); }

    /** Sets the value of the text_field(calls). */
    @NotNull public NestedOnChangesWidgetForm setCalls(@NotNull String calls) {
        f.set(Field.CALLS, calls);
        return (NestedOnChangesWidgetForm) this;
    }

    /** Returns the value of the combo_box(opts). */
    @NotNull public Options getOpts() { return Options.valueOf(f.get(Field.OPTS, String.class)); }

    /** Sets the value of the combo_box(opts). */
    @NotNull public NestedOnChangesWidgetForm setOpts(@NotNull Options opts) {
        f.set(Field.OPTS, opts);
        return (NestedOnChangesWidgetForm) this;
    }

    /** Sets the options of the combo_box(opts). */
    public void setOptsOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTS, items); }

    /** Sets the options of the combo_box(opts) with the given KeyMap. */
    public void setOptsOptions(@NotNull KeyMap items) { f.opts(Field.OPTS, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NestedOnChangesWidgetForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NestedOnChangesWidgetForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<NestedOnChangesWidgetForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(NestedOnChangesWidgetForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class WidgetsRowBase
        implements FormRowInstance<WidgetsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Define {@link NestedOnChangesWidget instance} to be used during interaction. */
        @NotNull abstract NestedOnChangesWidget defineChanges();

        /** Get widget {@link NestedOnChangesWidget}. */
        @NotNull public NestedOnChangesWidget getChanges() { return f.widget(Field.CHANGES, NestedOnChangesWidget.class); }

        /** Invoked when text_field(f) value changes */
        @NotNull public abstract Action fValueChanged();

        /** Returns the value of the text_field(f). */
        public int getF() { return f.get(Field.F, Integer.class); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<WidgetsRow> table() { return getWidgets(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((WidgetsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        WIDGETS("widgets"),
        CHANGES("changes"),
        F("f"),
        SUM("sum"),
        CALLS("calls"),
        OPTS("opts");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
