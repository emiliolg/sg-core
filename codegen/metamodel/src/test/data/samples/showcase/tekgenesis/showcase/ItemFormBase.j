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
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ItemForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ItemFormBase
    extends FormInstance<NamedItem>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final NamedItem namedItem = NamedItem.create(getId());
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

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

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

    /** Returns the value of the internal(prev). */
    public int getPrev() { return f.get(Field.PREV, Integer.class); }

    /** Sets the value of the internal(prev). */
    @NotNull public ItemForm setPrev(int prev) {
        f.set(Field.PREV, prev);
        return (ItemForm) this;
    }

    /** Returns the value of the internal(next). */
    public int getNext() { return f.get(Field.NEXT, Integer.class); }

    /** Sets the value of the internal(next). */
    @NotNull public ItemForm setNext(int next) {
        f.set(Field.NEXT, next);
        return (ItemForm) this;
    }

    /** Returns the value of the internal(data). */
    @NotNull public ViewData getData() {
        return Predefined.ensureNotNull(ViewData.find(getDataKey()), "'data' not found");
    }

    /** Returns the key value of the internal(data). */
    @NotNull public String getDataKey() { return f.get(Field.DATA, String.class); }

    /** Sets the value of the internal(data). */
    @NotNull public ItemForm setData(@NotNull ViewData data) {
        f.set(Field.DATA, data);
        return (ItemForm) this;
    }

    /** Returns the value of the text_field(id). */
    public int getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the text_field(id). */
    @NotNull public ItemForm setId(int id) {
        f.set(Field.ID, id);
        return (ItemForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public ItemForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (ItemForm) this;
    }

    /** Returns the value of the text_field(color). */
    @NotNull public String getColor() { return f.get(Field.COLOR, String.class); }

    /** Sets the value of the text_field(color). */
    @NotNull public ItemForm setColor(@NotNull String color) {
        f.set(Field.COLOR, color);
        return (ItemForm) this;
    }

    /** Invoked when button($B4) is clicked */
    @NotNull public abstract Action prev();

    /** Invoked when button($B6) is clicked */
    @NotNull public abstract Action next();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ItemForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ItemForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ItemForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ItemForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        PREV("prev"),
        NEXT("next"),
        DATA("data"),
        ID("id"),
        NAME("name"),
        COLOR("color"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
