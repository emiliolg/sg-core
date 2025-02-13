package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormParameters;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: NavigateWithParameters.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class NavigateWithParametersBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.NavigateWithParameters");
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

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public NavigateWithParameters setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the combo_box(oneOption). */
    @NotNull public Options getOneOption() {
        return Options.valueOf(f.get(Field.ONE_OPTION, String.class));
    }

    /** Sets the value of the combo_box(oneOption). */
    @NotNull public NavigateWithParameters setOneOption(@NotNull Options oneOption) {
        f.set(Field.ONE_OPTION, oneOption);
        return (NavigateWithParameters) this;
    }

    /** Sets the options of the combo_box(oneOption). */
    public void setOneOptionOptions(@NotNull Iterable<Options> items) { f.opts(Field.ONE_OPTION, items); }

    /** Sets the options of the combo_box(oneOption) with the given KeyMap. */
    public void setOneOptionOptions(@NotNull KeyMap items) { f.opts(Field.ONE_OPTION, items); }

    /** Returns the value of the tags_combo_box(someOptions). */
    @NotNull public Seq<Options> getSomeOptions() { return f.getArray(Field.SOME_OPTIONS, Options.class); }

    /** Sets the value of the tags_combo_box(someOptions). */
    @NotNull public NavigateWithParameters setSomeOptions(@NotNull Iterable<Options> someOptions) {
        f.setArray(Field.SOME_OPTIONS, someOptions);
        return (NavigateWithParameters) this;
    }

    /** Sets the options of the tags_combo_box(someOptions). */
    public void setSomeOptionsOptions(@NotNull Iterable<Options> items) { f.opts(Field.SOME_OPTIONS, items); }

    /** Sets the options of the tags_combo_box(someOptions) with the given KeyMap. */
    public void setSomeOptionsOptions(@NotNull KeyMap items) { f.opts(Field.SOME_OPTIONS, items); }

    /** Returns the value of the date_box(oneDateTime). */
    @NotNull public DateOnly getOneDateTime() {
        return DateOnly.fromMilliseconds(f.get(Field.ONE_DATE_TIME, Long.class));
    }

    /** Sets the value of the date_box(oneDateTime). */
    @NotNull public NavigateWithParameters setOneDateTime(@NotNull DateOnly oneDateTime) {
        f.set(Field.ONE_DATE_TIME, oneDateTime);
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the date_time_box(oneDate). */
    @NotNull public DateTime getOneDate() {
        return DateTime.fromMilliseconds(f.get(Field.ONE_DATE, Long.class));
    }

    /** Sets the value of the date_time_box(oneDate). */
    @NotNull public NavigateWithParameters setOneDate(@NotNull DateTime oneDate) {
        f.set(Field.ONE_DATE, oneDate);
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the text_field(decimal). */
    @NotNull public BigDecimal getDecimal() { return f.get(Field.DECIMAL, BigDecimal.class); }

    /** Sets the value of the text_field(decimal). */
    @NotNull public NavigateWithParameters setDecimal(@NotNull BigDecimal decimal) {
        f.set(Field.DECIMAL, Decimals.scaleAndCheck("decimal", decimal, false, 9, 3));
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the text_field(someInt). */
    public int getSomeInt() { return f.get(Field.SOME_INT, Integer.class); }

    /** Sets the value of the text_field(someInt). */
    @NotNull public NavigateWithParameters setSomeInt(int someInt) {
        f.set(Field.SOME_INT, someInt);
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the suggest_box(oneEntity). */
    @NotNull public NamedItem getOneEntity() {
        return Predefined.ensureNotNull(NamedItem.find(getOneEntityKey()), "'oneEntity' not found");
    }

    /** Returns the key value of the suggest_box(oneEntity). */
    @NotNull public String getOneEntityKey() { return f.get(Field.ONE_ENTITY, String.class); }

    /** Sets the value of the suggest_box(oneEntity). */
    @NotNull public NavigateWithParameters setOneEntity(@NotNull NamedItem oneEntity) {
        f.set(Field.ONE_ENTITY, oneEntity);
        return (NavigateWithParameters) this;
    }

    /** Returns the value of the tags_suggest_box(moreEntity). */
    @NotNull public Seq<NamedItem> getMoreEntity() { return f.getArray(Field.MORE_ENTITY, NamedItem.class); }

    /** Sets the value of the tags_suggest_box(moreEntity). */
    @NotNull public NavigateWithParameters setMoreEntity(@NotNull Iterable<NamedItem> moreEntity) {
        f.setArray(Field.MORE_ENTITY, moreEntity);
        return (NavigateWithParameters) this;
    }

    /** Invoked when button(navi) is clicked */
    @NotNull public abstract Action navigate();

    @NotNull public static NavigateWithParametersParameters parameters() { return new NavigateWithParametersParameters(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NavigateWithParameters> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NavigateWithParameters> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<NavigateWithParameters> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(NavigateWithParameters.class);

    //~ Inner Classes ............................................................................................................

    public static final class NavigateWithParametersParameters
        extends FormParameters<NavigateWithParameters>
    {

        //~ Methods ..................................................................................................................

        @NotNull public NavigateWithParametersParameters withName(@NotNull String name) { return put(Field.NAME, name); }

        @NotNull public NavigateWithParametersParameters withOneOption(@NotNull Options oneOption) { return put(Field.ONE_OPTION, oneOption); }

        @NotNull public NavigateWithParametersParameters withSomeOptions(@NotNull Iterable<Options> someOptions) { return putAll(Field.SOME_OPTIONS, someOptions); }

        @NotNull public NavigateWithParametersParameters withOneDateTime(@NotNull DateOnly oneDateTime) { return put(Field.ONE_DATE_TIME, oneDateTime); }

        @NotNull public NavigateWithParametersParameters withOneDate(@NotNull DateTime oneDate) { return put(Field.ONE_DATE, oneDate); }

        @NotNull public NavigateWithParametersParameters withDecimal(@NotNull BigDecimal decimal) {
            return put(Field.DECIMAL, Decimals.scaleAndCheck("decimal", decimal, false, 9, 3));
        }

        @NotNull public NavigateWithParametersParameters withSomeInt(int someInt) { return put(Field.SOME_INT, someInt); }

        @NotNull public NavigateWithParametersParameters withOneEntity(@NotNull NamedItem oneEntity) { return put(Field.ONE_ENTITY, oneEntity); }

        @NotNull public NavigateWithParametersParameters withOneEntityKey(@NotNull String oneEntity) { return put(Field.ONE_ENTITY, oneEntity); }

        @NotNull public NavigateWithParametersParameters withMoreEntity(@NotNull Iterable<NamedItem> moreEntity) { return putAll(Field.MORE_ENTITY, moreEntity); }

        @NotNull public NavigateWithParametersParameters withMoreEntityKey(@NotNull Iterable<String> moreEntity) { return putAll(Field.MORE_ENTITY, moreEntity); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        NAME("name"),
        ONE_OPTION("oneOption"),
        SOME_OPTIONS("someOptions"),
        ONE_DATE_TIME("oneDateTime"),
        ONE_DATE("oneDate"),
        DECIMAL("decimal"),
        SOME_INT("someInt"),
        ONE_ENTITY("oneEntity"),
        MORE_ENTITY("moreEntity"),
        $H3("$H3"),
        NAVI("navi"),
        BACK("back");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
