package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.ListingSimpleEntities.EntitiesRow;
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
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ListingSimpleEntities.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ListingSimpleEntitiesBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ListingSimpleEntities");
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

    /** Invoked when button(reload) is clicked */
    @NotNull public abstract Action reload();

    /** Returns a {@link FormTable<EntitiesRow>} instance to handle Entities manipulation */
    @NotNull public final FormTable<EntitiesRow> getEntities() { return table(Field.ENTITIES, EntitiesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on loading table(entities). */
    public void loadSimpleEntities() {
        final FormTable<EntitiesRow> table = getEntities();
        SimpleEntity.forEach(currentSimpleEntity -> table.add().populate(currentSimpleEntity));
    }

    /** Called each time table(entities) changes. */
    @NotNull public Action saveSimpleEntity() {
        if (forms.hasPermission(PredefinedPermission.CREATE)) {
        	final EntitiesRow row = getEntities().getCurrent();
        	if (!row.isDefined(Field.NAME)) throw new IllegalArgumentException("Trying to use a null key.");
        	final SimpleEntity currentSimpleEntity = SimpleEntity.findOrCreate(row.keyAsString());
        	row.copyTo(currentSimpleEntity);
        	currentSimpleEntity.persist();
        }
        return actions().getDefault();
    }

    /** Called on removing entities row. */
    @NotNull public Action removeSimpleEntity() { return getEntities().getCurrent().remove(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ListingSimpleEntities> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ListingSimpleEntities> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ListingSimpleEntities> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ListingSimpleEntities.class);

    //~ Inner Classes ............................................................................................................

    public abstract class EntitiesRowBase
        implements FormRowInstance<EntitiesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public EntitiesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (EntitiesRow) this;
        }

        /** Returns the value of the text_field(desc). */
        @NotNull public String getDesc() { return f.get(Field.DESC, String.class); }

        /** Sets the value of the text_field(desc). */
        @NotNull public EntitiesRow setDesc(@NotNull String desc) {
            f.set(Field.DESC, desc);
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
            	.setDesc(simpleEntity.getDescription());
        }

        /** Copies field values to given simpleEntity instance. */
        public void copyTo(@NotNull SimpleEntity simpleEntity) { simpleEntity.setDescription(getDesc()); }

        /** Return primary key of bound {@link SimpleEntity} */
        @NotNull public String keyAsString() { return "" + getName(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        RELOAD("reload"),
        ENTITIES("entities"),
        NAME("name"),
        DESC("desc"),
        $H2("$H2"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
