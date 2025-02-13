package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateTime;
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
import tekgenesis.common.Predefined;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.showcase.SynchronousList.SynchronousesRow;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SynchronousList.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SynchronousListBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SynchronousList");
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

    /** Returns a {@link FormTable<SynchronousesRow>} instance to handle Synchronouses manipulation */
    @NotNull public final FormTable<SynchronousesRow> getSynchronouses() { return table(Field.SYNCHRONOUSES, SynchronousesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on loading table(synchronouses). */
    public void loadSynchronouses() {
        final FormTable<SynchronousesRow> table = getSynchronouses();
        Synchronous.forEach(currentSynchronous -> table.add().populate(currentSynchronous));
    }

    /** Called each time table(synchronouses) changes. */
    @NotNull public Action saveSynchronous() {
        if (forms.hasPermission(PredefinedPermission.CREATE)) {
        	final SynchronousesRow row = getSynchronouses().getCurrent();
        	final Integer primaryKey = row.getId();
        	final Synchronous currentSynchronous = primaryKey != null ? Predefined.ensureNotNull(Synchronous.find(primaryKey), "'row.getId()' not found") : Synchronous.create();
        	row.copyTo(currentSynchronous);
        	currentSynchronous.persist();
        	row.setId(currentSynchronous.getId());
        }
        return actions().getDefault();
    }

    /** Called on removing synchronouses row. */
    @NotNull public Action removeSynchronous() { return getSynchronouses().getCurrent().remove(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SynchronousList> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SynchronousList> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SynchronousList> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SynchronousList.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SynchronousesRowBase
        implements FormRowInstance<SynchronousesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(id). */
        public int getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the display(id). */
        @NotNull public SynchronousesRow setId(int id) {
            f.set(Field.ID, id);
            return (SynchronousesRow) this;
        }

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public SynchronousesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (SynchronousesRow) this;
        }

        /** Returns the value of the display(creationTime). */
        @NotNull public DateTime getCreationTime() {
            return DateTime.fromMilliseconds(f.get(Field.CREATION_TIME, Long.class));
        }

        /** Sets the value of the display(creationTime). */
        @NotNull public SynchronousesRow setCreationTime(@NotNull DateTime creationTime) {
            f.set(Field.CREATION_TIME, creationTime);
            return (SynchronousesRow) this;
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
        @NotNull public final FormTable<SynchronousesRow> table() { return getSynchronouses(); }

        /** Remove row from table and delete associated Synchronous instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Synchronous instance = isDefined(Field.ID) ? Synchronous.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((SynchronousesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given synchronous instance. */
        public void populate(@NotNull Synchronous synchronous) {
            setId(synchronous.getId())
            	.setName(synchronous.getName())
            	.setCreationTime(synchronous.getCreationTime());
        }

        /** Copies field values to given synchronous instance. */
        public void copyTo(@NotNull Synchronous synchronous) { synchronous.setName(getName()); }

        /** Return primary key of bound {@link Synchronous} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SYNCHRONOUSES("synchronouses"),
        ID("id"),
        NAME("name"),
        CREATION_TIME("creationTime"),
        $H2("$H2"),
        $B3("$B3"),
        $B4("$B4");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
