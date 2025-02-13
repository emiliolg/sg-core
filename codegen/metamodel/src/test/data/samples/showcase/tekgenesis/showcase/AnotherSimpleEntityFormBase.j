package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.common.core.enumeration.Enumerations;
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
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: AnotherSimpleEntityForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class AnotherSimpleEntityFormBase
    extends FormInstance<AnotherSimpleEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final AnotherSimpleEntity anotherSimpleEntity = AnotherSimpleEntity.create();
        copyTo(anotherSimpleEntity);
        anotherSimpleEntity.insert();
        setId(anotherSimpleEntity.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final AnotherSimpleEntity anotherSimpleEntity = find();
        copyTo(anotherSimpleEntity);
        anotherSimpleEntity.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public AnotherSimpleEntity find() {
        final AnotherSimpleEntity value = AnotherSimpleEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public AnotherSimpleEntity populate() {
        final AnotherSimpleEntity anotherSimpleEntity = find();

        setName(anotherSimpleEntity.getName())
        	.setOptions(anotherSimpleEntity.getOptions());

        return anotherSimpleEntity;
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

    /** Copies field values to given anotherSimpleEntity instance. */
    public void copyTo(@NotNull AnotherSimpleEntity anotherSimpleEntity) {
        anotherSimpleEntity.setName(getName())
        	.setOptions(Enumerations.enumSet(Options.class, getOptions().toList()));
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public AnotherSimpleEntityForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (AnotherSimpleEntityForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public AnotherSimpleEntityForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (AnotherSimpleEntityForm) this;
    }

    /** Returns the value of the tags_combo_box(options). */
    @NotNull public Seq<Options> getOptions() { return f.getArray(Field.OPTIONS, Options.class); }

    /** Sets the value of the tags_combo_box(options). */
    @NotNull public AnotherSimpleEntityForm setOptions(@NotNull Iterable<Options> options) {
        f.setArray(Field.OPTIONS, options);
        return (AnotherSimpleEntityForm) this;
    }

    /** Sets the options of the tags_combo_box(options). */
    public void setOptionsOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTIONS, items); }

    /** Sets the options of the tags_combo_box(options) with the given KeyMap. */
    public void setOptionsOptions(@NotNull KeyMap items) { f.opts(Field.OPTIONS, items); }

    /** Returns the value of the tags_suggest_box(simpleEntities). */
    @NotNull public Seq<SimpleEntity> getSimpleEntities() {
        return f.getArray(Field.SIMPLE_ENTITIES, SimpleEntity.class);
    }

    /** Sets the value of the tags_suggest_box(simpleEntities). */
    @NotNull public AnotherSimpleEntityForm setSimpleEntities(@NotNull Iterable<SimpleEntity> simpleEntities) {
        f.setArray(Field.SIMPLE_ENTITIES, simpleEntities);
        return (AnotherSimpleEntityForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AnotherSimpleEntityForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AnotherSimpleEntityForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<AnotherSimpleEntityForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(AnotherSimpleEntityForm.class);

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
        OPTIONS("options"),
        SIMPLE_ENTITIES("simpleEntities"),
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
