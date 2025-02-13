package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.SimpleEntitiesList.EntitiesRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SimpleEntitiesList.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SimpleEntitiesListBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SimpleEntitiesList");
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

    /** Returns a {@link FormTable<EntitiesRow>} instance to handle Entities manipulation */
    @NotNull public final FormTable<EntitiesRow> getEntities() { return table(Field.ENTITIES, EntitiesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on loading table(entities). */
    public void load() {
        final FormTable<EntitiesRow> table = getEntities();
        SimpleEntity.forEach(currentSimpleEntity -> table.add().populate(currentSimpleEntity));
    }

    /** Invoked when button(add) is clicked */
    @NotNull public abstract Action createNewOne();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleEntitiesList> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleEntitiesList> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SimpleEntitiesList> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SimpleEntitiesList.class);

    //~ Inner Classes ............................................................................................................

    public abstract class EntitiesRowBase
        implements FormRowInstance<EntitiesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the display(name). */
        @NotNull public EntitiesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (EntitiesRow) this;
        }

        /** Returns the value of the display(description). */
        @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the display(description). */
        @NotNull public EntitiesRow setDescription(@NotNull String description) {
            f.set(Field.DESCRIPTION, description);
            return (EntitiesRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<EntitiesRow> table() { return getEntities(); }

        /** Remove row from table and delete associated SimpleEntity instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final SimpleEntity instance = isDefined(Field.NAME) ? SimpleEntity.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((EntitiesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given simpleEntity instance. */
        public void populate(@NotNull SimpleEntity simpleEntity) {
            setName(simpleEntity.getName())
            	.setDescription(simpleEntity.getDescription());
        }

        /** Copies field values to given simpleEntity instance. */
        public void copyTo(@NotNull SimpleEntity simpleEntity) { simpleEntity.setDescription(getDescription()); }

        /** Return primary key of bound {@link SimpleEntity} */
        @NotNull public String keyAsString() { return "" + getName(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        ENTITIES("entities"),
        NAME("name"),
        DESCRIPTION("description"),
        ADD("add");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
