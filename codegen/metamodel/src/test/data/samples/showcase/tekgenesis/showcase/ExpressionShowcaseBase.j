package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
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
 * Generated base class for form: ExpressionShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ExpressionShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ExpressionShowcase");
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

    /** Returns the value of the text_field(ignoredItem). */
    public int getIgnoredItem() { return f.get(Field.IGNORED_ITEM, Integer.class); }

    /** Sets the value of the text_field(ignoredItem). */
    @NotNull public ExpressionShowcase setIgnoredItem(int ignoredItem) {
        f.set(Field.IGNORED_ITEM, ignoredItem);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(item1). */
    public int getItem1() { return f.get(Field.ITEM1, Integer.class); }

    /** Sets the value of the text_field(item1). */
    @NotNull public ExpressionShowcase setItem1(int item1) {
        f.set(Field.ITEM1, item1);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(item2). */
    public int getItem2() { return f.get(Field.ITEM2, Integer.class); }

    /** Sets the value of the text_field(item2). */
    @NotNull public ExpressionShowcase setItem2(int item2) {
        f.set(Field.ITEM2, item2);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(item3). */
    public int getItem3() { return f.get(Field.ITEM3, Integer.class); }

    /** Sets the value of the text_field(item3). */
    @NotNull public ExpressionShowcase setItem3(int item3) {
        f.set(Field.ITEM3, item3);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(subtotal). */
    public int getSubtotal() { return f.get(Field.SUBTOTAL, Integer.class); }

    /** Returns the value of the text_field(iva). */
    @NotNull public BigDecimal getIva() { return f.get(Field.IVA, BigDecimal.class); }

    /** Returns the value of the text_field(total). */
    @NotNull public BigDecimal getTotal() { return f.get(Field.TOTAL, BigDecimal.class); }

    /** Returns the value of the text_field(first). */
    @NotNull public String getFirst() { return f.get(Field.FIRST, String.class); }

    /** Sets the value of the text_field(first). */
    @NotNull public ExpressionShowcase setFirst(@NotNull String first) {
        f.set(Field.FIRST, first);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(last). */
    @NotNull public String getLast() { return f.get(Field.LAST, String.class); }

    /** Sets the value of the text_field(last). */
    @NotNull public ExpressionShowcase setLast(@NotNull String last) {
        f.set(Field.LAST, last);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the internal(available). */
    public boolean isAvailable() { return f.get(Field.AVAILABLE, Boolean.class); }

    /** Sets the value of the internal(available). */
    @NotNull public ExpressionShowcase setAvailable(boolean available) {
        f.set(Field.AVAILABLE, available);
        return (ExpressionShowcase) this;
    }

    /** Invoked when text_field(nick) value changes */
    @NotNull public abstract Action isNickAvailable();

    /** Returns the value of the text_field(nick). */
    @NotNull public String getNick() { return f.get(Field.NICK, String.class); }

    /** Sets the value of the text_field(nick). */
    @NotNull public ExpressionShowcase setNick(@NotNull String nick) {
        f.set(Field.NICK, nick);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(target). */
    @NotNull public String getTarget() { return f.get(Field.TARGET, String.class); }

    /** Sets the value of the text_field(target). */
    @NotNull public ExpressionShowcase setTarget(@NotNull String target) {
        f.set(Field.TARGET, target);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(function). */
    @NotNull public String getFunction() { return f.get(Field.FUNCTION, String.class); }

    /** Sets the value of the text_field(function). */
    @NotNull public ExpressionShowcase setFunction(@NotNull String function) {
        f.set(Field.FUNCTION, function);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(arg1). */
    @Nullable public String getArg1() { return f.get(Field.ARG1, String.class); }

    /** Sets the value of the text_field(arg1). */
    @NotNull public ExpressionShowcase setArg1(@Nullable String arg1) {
        f.set(Field.ARG1, arg1);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(arg2). */
    @Nullable public String getArg2() { return f.get(Field.ARG2, String.class); }

    /** Sets the value of the text_field(arg2). */
    @NotNull public ExpressionShowcase setArg2(@Nullable String arg2) {
        f.set(Field.ARG2, arg2);
        return (ExpressionShowcase) this;
    }

    /** Returns the value of the text_field(arg3). */
    @Nullable public Integer getArg3() { return f.get(Field.ARG3, Integer.class); }

    /** Sets the value of the text_field(arg3). */
    @NotNull public ExpressionShowcase setArg3(@Nullable Integer arg3) {
        f.set(Field.ARG3, arg3);
        return (ExpressionShowcase) this;
    }

    /** Invoked when button($B5) is clicked */
    @NotNull public abstract Action invoke();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExpressionShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExpressionShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ExpressionShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ExpressionShowcase.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        $H3("$H3"),
        ITEM_GROUP("itemGroup"),
        IGNORED_ITEM("ignoredItem"),
        ITEM1("item1"),
        ITEM2("item2"),
        ITEM3("item3"),
        SUBTOTAL("subtotal"),
        IVA("iva"),
        TOTAL("total"),
        USERNAME("username"),
        FIRST("first"),
        LAST("last"),
        AVAILABLE("available"),
        NICK("nick"),
        $V4("$V4"),
        TARGET("target"),
        FUNCTION("function"),
        ARG1("arg1"),
        ARG2("arg2"),
        ARG3("arg3"),
        $B5("$B5"),
        $F6("$F6"),
        $B7("$B7");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
