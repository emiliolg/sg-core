package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.ListingAnotherDeprecable.AnotherDeprecableEntitiesRow;
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
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ListingAnotherDeprecable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ListingAnotherDeprecableBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ListingAnotherDeprecable");
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

    /** Returns the value of the check_box(showDeprecableInfo). */
    public boolean isShowDeprecableInfo() { return f.get(Field.SHOW_DEPRECABLE_INFO, Boolean.class); }

    /** Sets the value of the check_box(showDeprecableInfo). */
    @NotNull public ListingAnotherDeprecable setShowDeprecableInfo(boolean showDeprecableInfo) {
        f.set(Field.SHOW_DEPRECABLE_INFO, showDeprecableInfo);
        return (ListingAnotherDeprecable) this;
    }

    /** Returns a {@link FormTable<AnotherDeprecableEntitiesRow>} instance to handle AnotherDeprecableEntities manipulation */
    @NotNull public final FormTable<AnotherDeprecableEntitiesRow> getAnotherDeprecableEntities() {
        return table(Field.ANOTHER_DEPRECABLE_ENTITIES, AnotherDeprecableEntitiesRow.class);
    }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on loading table(anotherDeprecableEntities). */
    public void loadAnotherDeprecableEntities() {
        final FormTable<AnotherDeprecableEntitiesRow> table = getAnotherDeprecableEntities();
        AnotherDeprecableEntity.forEach(currentAnotherDeprecableEntity -> table.add().populate(currentAnotherDeprecableEntity));
    }

    /** Called each time table(anotherDeprecableEntities) changes. */
    @NotNull public Action saveAnotherDeprecableEntity() {
        if (forms.hasPermission(PredefinedPermission.CREATE)) {
        	final AnotherDeprecableEntitiesRow row = getAnotherDeprecableEntities().getCurrent();
        	final Integer primaryKey = row.getId();
        	final AnotherDeprecableEntity currentAnotherDeprecableEntity = primaryKey != null ? Predefined.ensureNotNull(AnotherDeprecableEntity.find(primaryKey), "'row.getId()' not found") : AnotherDeprecableEntity.create();
        	row.copyTo(currentAnotherDeprecableEntity);
        	currentAnotherDeprecableEntity.persist();
        	row.setId(currentAnotherDeprecableEntity.getId());
        }
        return actions().getDefault();
    }

    /** Called on removing anotherDeprecableEntities row. */
    @NotNull public Action removeAnotherDeprecableEntity() { return getAnotherDeprecableEntities().getCurrent().remove(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ListingAnotherDeprecable> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ListingAnotherDeprecable> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ListingAnotherDeprecable> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ListingAnotherDeprecable.class);

    //~ Inner Classes ............................................................................................................

    public abstract class AnotherDeprecableEntitiesRowBase
        implements FormRowInstance<AnotherDeprecableEntitiesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(id). */
        @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the internal(id). */
        @NotNull public AnotherDeprecableEntitiesRow setId(@Nullable Integer id) {
            f.set(Field.ID, id);
            return (AnotherDeprecableEntitiesRow) this;
        }

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public AnotherDeprecableEntitiesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (AnotherDeprecableEntitiesRow) this;
        }

        /** Returns the value of the display(deprecationTime). */
        @Nullable public DateTime getDeprecationTime() {
            final Long val = f.get(Field.DEPRECATION_TIME, Long.class);
            return val == null ? null : DateTime.fromMilliseconds(val);
        }

        /** Sets the value of the display(deprecationTime). */
        @NotNull public AnotherDeprecableEntitiesRow setDeprecationTime(@Nullable DateTime deprecationTime) {
            f.set(Field.DEPRECATION_TIME, deprecationTime);
            return (AnotherDeprecableEntitiesRow) this;
        }

        /** Returns the value of the display(deprecationUser). */
        @Nullable public String getDeprecationUser() { return f.get(Field.DEPRECATION_USER, String.class); }

        /** Sets the value of the display(deprecationUser). */
        @NotNull public AnotherDeprecableEntitiesRow setDeprecationUser(@Nullable String deprecationUser) {
            f.set(Field.DEPRECATION_USER, deprecationUser);
            return (AnotherDeprecableEntitiesRow) this;
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
        @NotNull public final FormTable<AnotherDeprecableEntitiesRow> table() { return getAnotherDeprecableEntities(); }

        /** Remove row from table and delete associated AnotherDeprecableEntity instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final AnotherDeprecableEntity instance = isDefined(Field.ID) ? AnotherDeprecableEntity.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((AnotherDeprecableEntitiesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given anotherDeprecableEntity instance. */
        public void populate(@NotNull AnotherDeprecableEntity anotherDeprecableEntity) {
            setId(anotherDeprecableEntity.getId())
            	.setName(anotherDeprecableEntity.getName())
            	.setDeprecationTime(anotherDeprecableEntity.getDeprecationTime())
            	.setDeprecationUser(anotherDeprecableEntity.getDeprecationUser());
        }

        /** Copies field values to given anotherDeprecableEntity instance. */
        public void copyTo(@NotNull AnotherDeprecableEntity anotherDeprecableEntity) { anotherDeprecableEntity.setName(getName()); }

        /** Return primary key of bound {@link AnotherDeprecableEntity} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SHOW_DEPRECABLE_INFO("showDeprecableInfo"),
        ANOTHER_DEPRECABLE_ENTITIES("anotherDeprecableEntities"),
        ID("id"),
        NAME("name"),
        DEPRECATION_TIME("deprecationTime"),
        DEPRECATION_USER("deprecationUser"),
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
