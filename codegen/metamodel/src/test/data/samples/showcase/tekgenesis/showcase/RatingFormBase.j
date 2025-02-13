package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: RatingForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class RatingFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.RatingForm");
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

    /** Returns the value of the text_field(text1). */
    @NotNull public String getText1() { return f.get(Field.TEXT1, String.class); }

    /** Sets the value of the text_field(text1). */
    @NotNull public RatingForm setText1(@NotNull String text1) {
        f.set(Field.TEXT1, text1);
        return (RatingForm) this;
    }

    /** Returns the value of the rating(stars). */
    @Nullable public Integer getStars() { return f.get(Field.STARS, Integer.class); }

    /** Sets the value of the rating(stars). */
    @NotNull public RatingForm setStars(@Nullable Integer stars) {
        f.set(Field.STARS, stars);
        return (RatingForm) this;
    }

    /** Sets the options of the rating(stars). */
    public void setStarsOptions(@NotNull Iterable<Integer> items) { f.opts(Field.STARS, items); }

    /** Sets the options of the rating(stars) with the given KeyMap. */
    public void setStarsOptions(@NotNull KeyMap items) { f.opts(Field.STARS, items); }

    /** 
     * Invoked when rating(hearts) value changes
     * Invoked when text_field(text2) value changes
     * Invoked when button(but) is clicked
     */
    @NotNull public abstract Action sync();

    /** Returns the value of the rating(hearts). */
    public int getHearts() { return f.get(Field.HEARTS, Integer.class); }

    /** Sets the value of the rating(hearts). */
    @NotNull public RatingForm setHearts(int hearts) {
        f.set(Field.HEARTS, hearts);
        return (RatingForm) this;
    }

    /** Sets the options of the rating(hearts). */
    public void setHeartsOptions(@NotNull Iterable<Integer> items) { f.opts(Field.HEARTS, items); }

    /** Sets the options of the rating(hearts) with the given KeyMap. */
    public void setHeartsOptions(@NotNull KeyMap items) { f.opts(Field.HEARTS, items); }

    /** Returns the value of the text_field(text2). */
    @NotNull public String getText2() { return f.get(Field.TEXT2, String.class); }

    /** Sets the value of the text_field(text2). */
    @NotNull public RatingForm setText2(@NotNull String text2) {
        f.set(Field.TEXT2, text2);
        return (RatingForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<RatingForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<RatingForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<RatingForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(RatingForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        TEXT1("text1"),
        STARS("stars"),
        HEARTS("hearts"),
        TEXT2("text2"),
        BUT("but");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
