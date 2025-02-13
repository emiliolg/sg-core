package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SearchFilterForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SearchFilterFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.basic.SearchFilterForm");
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

    /** Returns the value of the check_box(someBool). */
    public boolean isSomeBool() { return f.get(Field.SOME_BOOL, Boolean.class); }

    /** Sets the value of the check_box(someBool). */
    @NotNull public SearchFilterForm setSomeBool(boolean someBool) {
        f.set(Field.SOME_BOOL, someBool);
        return (SearchFilterForm) this;
    }

    /** Returns the value of the text_field(someLastName). */
    @NotNull public String getSomeLastName() { return f.get(Field.SOME_LAST_NAME, String.class); }

    /** Sets the value of the text_field(someLastName). */
    @NotNull public SearchFilterForm setSomeLastName(@NotNull String someLastName) {
        f.set(Field.SOME_LAST_NAME, someLastName);
        return (SearchFilterForm) this;
    }

    /** Returns the value of the suggest_box(someFilter). */
    @NotNull public CustomerSearchable getSomeFilter() {
        return Predefined.ensureNotNull(CustomerSearchable.find(getSomeFilterKey()), "'someFilter' not found");
    }

    /** Returns the key value of the suggest_box(someFilter). */
    @NotNull public String getSomeFilterKey() { return f.get(Field.SOME_FILTER, String.class); }

    /** Sets the value of the suggest_box(someFilter). */
    @NotNull public SearchFilterForm setSomeFilter(@NotNull CustomerSearchable someFilter) {
        f.set(Field.SOME_FILTER, someFilter);
        return (SearchFilterForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SearchFilterForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SearchFilterForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SearchFilterForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SearchFilterForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SOME_BOOL("someBool"),
        SOME_LAST_NAME("someLastName"),
        SOME_FILTER("someFilter");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
