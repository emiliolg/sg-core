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
 * Generated base class for form: DeprecableEntityForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DeprecableEntityFormBase
    extends FormInstance<DeprecableEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final DeprecableEntity deprecableEntity = DeprecableEntity.create();
        copyTo(deprecableEntity);
        deprecableEntity.insert();
        setId(deprecableEntity.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final DeprecableEntity deprecableEntity = find();
        copyTo(deprecableEntity);
        deprecableEntity.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to change the deprecation status of a form instance */
    @NotNull public Action deprecate(boolean status) {
        final DeprecableEntity deprecable = find();
        if (deprecable.isDeprecated() != status) {
        	deprecable.deprecate(status);
        	deprecable.update();
        }
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public DeprecableEntity find() {
        final DeprecableEntity value = DeprecableEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public DeprecableEntity populate() {
        final DeprecableEntity deprecableEntity = find();

        setName(deprecableEntity.getName())
        	.setSimpleEntity(deprecableEntity.getSimpleEntity())
        	.setAnotherDeprecableEntity(deprecableEntity.getAnotherDeprecableEntity());

        return deprecableEntity;
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

    /** Copies field values to given deprecableEntity instance. */
    public void copyTo(@NotNull DeprecableEntity deprecableEntity) {
        deprecableEntity.setName(getName())
        	.setSimpleEntity(getSimpleEntity())
        	.setAnotherDeprecableEntity(getAnotherDeprecableEntity());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public DeprecableEntityForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (DeprecableEntityForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public DeprecableEntityForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (DeprecableEntityForm) this;
    }

    /** Returns the value of the suggest_box(simpleEntity). */
    @NotNull public SimpleEntity getSimpleEntity() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSimpleEntityKey()), "'simpleEntity' not found");
    }

    /** Returns the key value of the suggest_box(simpleEntity). */
    @NotNull public String getSimpleEntityKey() { return f.get(Field.SIMPLE_ENTITY, String.class); }

    /** Sets the value of the suggest_box(simpleEntity). */
    @NotNull public DeprecableEntityForm setSimpleEntity(@NotNull SimpleEntity simpleEntity) {
        f.set(Field.SIMPLE_ENTITY, simpleEntity);
        return (DeprecableEntityForm) this;
    }

    /** Returns the value of the suggest_box(anotherDeprecableEntity). */
    @NotNull public AnotherDeprecableEntity getAnotherDeprecableEntity() {
        return Predefined.ensureNotNull(AnotherDeprecableEntity.find(getAnotherDeprecableEntityKey()), "'anotherDeprecableEntity' not found");
    }

    /** Returns the key value of the suggest_box(anotherDeprecableEntity). */
    @NotNull public String getAnotherDeprecableEntityKey() { return f.get(Field.ANOTHER_DEPRECABLE_ENTITY, String.class); }

    /** Sets the value of the suggest_box(anotherDeprecableEntity). */
    @NotNull public DeprecableEntityForm setAnotherDeprecableEntity(@NotNull AnotherDeprecableEntity anotherDeprecableEntity) {
        f.set(Field.ANOTHER_DEPRECABLE_ENTITY, anotherDeprecableEntity);
        return (DeprecableEntityForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DeprecableEntityForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DeprecableEntityForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DeprecableEntityForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DeprecableEntityForm.class);

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
        SIMPLE_ENTITY("simpleEntity"),
        ANOTHER_DEPRECABLE_ENTITY("anotherDeprecableEntity"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6"),
        $T7("$T7");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
