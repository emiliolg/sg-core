package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
 * Generated base class for form: TitleEntityForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TitleEntityFormBase
    extends FormInstance<NamedItem>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final NamedItem namedItem = NamedItem.create(getIdKey());
        copyTo(namedItem);
        namedItem.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final NamedItem namedItem = find();
        copyTo(namedItem);
        namedItem.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public NamedItem find() {
        final NamedItem value = NamedItem.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID_KEY, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getIdKey(); }

    /** Invoked when populating a form instance */
    @NotNull public NamedItem populate() {
        final NamedItem namedItem = find();

        setName(namedItem.getName())
        	.setColor(namedItem.getColor());

        return namedItem;
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

    /** Copies field values to given namedItem instance. */
    public void copyTo(@NotNull NamedItem namedItem) {
        namedItem.setName(getName())
        	.setColor(getColor());
    }

    /** Returns the value of the message(title). */
    @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

    /** Returns the value of the internal(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the internal(idKey). */
    @NotNull public TitleEntityForm setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (TitleEntityForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public TitleEntityForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (TitleEntityForm) this;
    }

    /** Returns the value of the text_field(color). */
    @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

    /** Sets the value of the text_field(color). */
    @NotNull public TitleEntityForm setColor(@NotNull String color) {
        f.set(Field.COLOR, color);
        return (TitleEntityForm) this;
    }

    /** Invoked when button(submit) is clicked */
    @NotNull public abstract Action saveAndRedirect();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TitleEntityForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TitleEntityForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TitleEntityForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TitleEntityForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        TITLE("title"),
        SEARCH("search"),
        ID_KEY("idKey"),
        NAME("name"),
        COLOR("color"),
        $F1("$F1"),
        SUBMIT("submit"),
        $B2("$B2"),
        $B3("$B3");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
