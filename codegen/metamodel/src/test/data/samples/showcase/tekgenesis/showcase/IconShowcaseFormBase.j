package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
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
 * Generated base class for form: IconShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class IconShowcaseFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.IconShowcaseForm");
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

    /** Returns the value of the message(title). */
    @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

    /** Sets the value of the message(title). */
    @NotNull public IconShowcaseForm setTitle(@NotNull String title) {
        f.set(Field.TITLE, title);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the internal(change). */
    public boolean isChange() { return f.get(Field.CHANGE, Boolean.class); }

    /** Sets the value of the internal(change). */
    @NotNull public IconShowcaseForm setChange(boolean change) {
        f.set(Field.CHANGE, change);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the message(m1). */
    @NotNull public String getM1() { return f.get(Field.M1, String.class); }

    /** Returns the value of the message(m2). */
    @NotNull public String getM2() { return f.get(Field.M2, String.class); }

    /** Returns the value of the message(m3). */
    @NotNull public String getM3() { return f.get(Field.M3, String.class); }

    /** Returns the value of the message(pepe). */
    @NotNull public String getPepe() { return f.get(Field.PEPE, String.class); }

    /** Sets the value of the message(pepe). */
    @NotNull public IconShowcaseForm setPepe(@NotNull String pepe) {
        f.set(Field.PEPE, pepe);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the message(defaultPlain). */
    @NotNull public String getDefaultPlain() { return f.get(Field.DEFAULT_PLAIN, String.class); }

    /** Sets the value of the message(defaultPlain). */
    @NotNull public IconShowcaseForm setDefaultPlain(@NotNull String defaultPlain) {
        f.set(Field.DEFAULT_PLAIN, defaultPlain);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the text_field(tf1). */
    @NotNull public String getTf1() { return f.get(Field.TF1, String.class); }

    /** Sets the value of the text_field(tf1). */
    @NotNull public IconShowcaseForm setTf1(@NotNull String tf1) {
        f.set(Field.TF1, tf1);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the text_field(tf). */
    @NotNull public String getTf() { return f.get(Field.TF, String.class); }

    /** Sets the value of the text_field(tf). */
    @NotNull public IconShowcaseForm setTf(@NotNull String tf) {
        f.set(Field.TF, tf);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the date_box(db). */
    @NotNull public DateOnly getDb() {
        return DateOnly.fromMilliseconds(f.get(Field.DB, Long.class));
    }

    /** Sets the value of the date_box(db). */
    @NotNull public IconShowcaseForm setDb(@NotNull DateOnly db) {
        f.set(Field.DB, db);
        return (IconShowcaseForm) this;
    }

    /** Returns the value of the toggle_button(tb). */
    public boolean isTb() { return f.get(Field.TB, Boolean.class); }

    /** Sets the value of the toggle_button(tb). */
    @NotNull public IconShowcaseForm setTb(boolean tb) {
        f.set(Field.TB, tb);
        return (IconShowcaseForm) this;
    }

    /** Invoked when button(changeMe) is clicked */
    @NotNull public abstract Action changeAll();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<IconShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<IconShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<IconShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(IconShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        TITLE("title"),
        CHANGE("change"),
        VERTICAL("vertical"),
        M1("m1"),
        $M1("$M1"),
        HORIZONTAL("horizontal"),
        $M2("$M2"),
        M2("m2"),
        $H3("$H3"),
        M3("m3"),
        $M4("$M4"),
        $V5("$V5"),
        $M6("$M6"),
        PEPE("pepe"),
        PLAIN_HORIZONTAL("plainHorizontal"),
        $M7("$M7"),
        DEFAULT_PLAIN("defaultPlain"),
        ICONS("icons"),
        ICONS1("icons1"),
        BUT("but"),
        TF1("tf1"),
        $L8("$L8"),
        $D9("$D9"),
        ICONS2("icons2"),
        TF("tf"),
        DB("db"),
        BU("bu"),
        TB("tb"),
        $H10("$H10"),
        $L11("$L11"),
        $L12("$L12"),
        $L13("$L13"),
        $L14("$L14"),
        CHANGE_ME("changeMe");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
