package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.OptionalWidget;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: BinaryNode.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class BinaryNodeBase
    extends WidgetInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Returns the value of the internal(value). */
    @NotNull public String getValue() { return f.get(Field.VALUE, String.class); }

    /** Sets the value of the internal(value). */
    @NotNull public BinaryNode setValue(@NotNull String value) {
        f.set(Field.VALUE, value);
        return (BinaryNode) this;
    }

    /** Invoked when toggle_button(button) value ui changes */
    @NotNull public abstract Action toggle();

    /** Returns the value of the toggle_button(button). */
    public boolean isButton() { return f.get(Field.BUTTON, Boolean.class); }

    /** Sets the value of the toggle_button(button). */
    @NotNull public BinaryNode setButton(boolean button) {
        f.set(Field.BUTTON, button);
        return (BinaryNode) this;
    }

    /** Define {@link BinaryNode instance} to be used during interaction. */
    @NotNull abstract BinaryNode defineLeft();

    /** Get widget {@link BinaryNode}. */
    @NotNull public OptionalWidget<BinaryNode> getLeft() { return f.optionalWidget(Field.LEFT, BinaryNode.class); }

    /** Define {@link BinaryNode instance} to be used during interaction. */
    @NotNull abstract BinaryNode defineRight();

    /** Get widget {@link BinaryNode}. */
    @NotNull public OptionalWidget<BinaryNode> getRight() { return f.optionalWidget(Field.RIGHT, BinaryNode.class); }

    /** Returns the value of the display(inorder). */
    @NotNull public String getInorder() { return f.get(Field.INORDER, String.class); }

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

    @NotNull private static final Logger logger = Logger.getLogger(BinaryNode.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $V0("$V0"),
        $V1("$V1"),
        VALUE("value"),
        BUTTON("button"),
        $H2("$H2"),
        LEFT("left"),
        RIGHT("right"),
        INORDER("inorder");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
