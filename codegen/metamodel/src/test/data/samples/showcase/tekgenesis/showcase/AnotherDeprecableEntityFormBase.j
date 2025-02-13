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
 * Generated base class for form: AnotherDeprecableEntityForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class AnotherDeprecableEntityFormBase
    extends FormInstance<AnotherDeprecableEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final AnotherDeprecableEntity anotherDeprecableEntity = AnotherDeprecableEntity.create();
        copyTo(anotherDeprecableEntity);
        anotherDeprecableEntity.insert();
        setId(anotherDeprecableEntity.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final AnotherDeprecableEntity anotherDeprecableEntity = find();
        copyTo(anotherDeprecableEntity);
        anotherDeprecableEntity.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to change the deprecation status of a form instance */
    @NotNull public Action deprecate(boolean status) {
        final AnotherDeprecableEntity deprecable = find();
        if (deprecable.isDeprecated() != status) {
        	deprecable.deprecate(status);
        	deprecable.update();
        }
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public AnotherDeprecableEntity find() {
        final AnotherDeprecableEntity value = AnotherDeprecableEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public AnotherDeprecableEntity populate() {
        final AnotherDeprecableEntity anotherDeprecableEntity = find();

        setName(anotherDeprecableEntity.getName());

        return anotherDeprecableEntity;
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

    /** Copies field values to given anotherDeprecableEntity instance. */
    public void copyTo(@NotNull AnotherDeprecableEntity anotherDeprecableEntity) { anotherDeprecableEntity.setName(getName()); }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public AnotherDeprecableEntityForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (AnotherDeprecableEntityForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public AnotherDeprecableEntityForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (AnotherDeprecableEntityForm) this;
    }

    /** Returns the value of the toggle_button(deprecate). */
    public boolean isDeprecate() { return f.get(Field.DEPRECATE, Boolean.class); }

    /** Sets the value of the toggle_button(deprecate). */
    @NotNull public AnotherDeprecableEntityForm setDeprecate(boolean deprecate) {
        f.set(Field.DEPRECATE, deprecate);
        return (AnotherDeprecableEntityForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AnotherDeprecableEntityForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AnotherDeprecableEntityForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<AnotherDeprecableEntityForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(AnotherDeprecableEntityForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        NAME("name"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6"),
        DEPRECATE("deprecate");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
