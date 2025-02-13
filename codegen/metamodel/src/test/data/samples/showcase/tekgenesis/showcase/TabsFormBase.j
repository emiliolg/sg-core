package tekgenesis.showcase;

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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TabsForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TabsFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TabsForm");
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

    /** Returns the value of the check_box(hideFirst). */
    public boolean isHideFirst() { return f.get(Field.HIDE_FIRST, Boolean.class); }

    /** Sets the value of the check_box(hideFirst). */
    @NotNull public TabsForm setHideFirst(boolean hideFirst) {
        f.set(Field.HIDE_FIRST, hideFirst);
        return (TabsForm) this;
    }

    /** Returns the value of the check_box(hideSecond). */
    public boolean isHideSecond() { return f.get(Field.HIDE_SECOND, Boolean.class); }

    /** Sets the value of the check_box(hideSecond). */
    @NotNull public TabsForm setHideSecond(boolean hideSecond) {
        f.set(Field.HIDE_SECOND, hideSecond);
        return (TabsForm) this;
    }

    /** Returns the value of the tabs(tabs). */
    public int getTabs() { return f.get(Field.TABS, Integer.class); }

    /** Sets the value of the tabs(tabs). */
    @NotNull public TabsForm setTabs(int tabs) {
        f.set(Field.TABS, tabs);
        return (TabsForm) this;
    }

    /** Returns the value of the text_field(str). */
    @NotNull public String getStr() { return f.get(Field.STR, String.class); }

    /** Sets the value of the text_field(str). */
    @NotNull public TabsForm setStr(@NotNull String str) {
        f.set(Field.STR, str);
        return (TabsForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TabsForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TabsForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TabsForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TabsForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        HIDE_FIRST("hideFirst"),
        HIDE_SECOND("hideSecond"),
        TABS("tabs"),
        $H2("$H2"),
        $M3("$M3"),
        $M4("$M4"),
        $H5("$H5"),
        $M6("$M6"),
        $M7("$M7"),
        THIRD("third"),
        STR("str");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
