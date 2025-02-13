package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
import tekgenesis.test.BoundTableDifferNameForm.SomeOtherEntities5Row;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.escapeCharOn;
import static tekgenesis.common.core.Strings.splitToArray;

/** 
 * Generated base class for form: BoundTableDifferNameForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class BoundTableDifferNameFormBase
    extends FormInstance<SomeEntity>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final SomeEntity someEntity = SomeEntity.create(getAltoInt(), getMartinsString());
        copyTo(someEntity);
        someEntity.insert();
        createOrUpdateSomeOtherEntities5();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final SomeEntity someEntity = find();
        copyTo(someEntity);
        someEntity.update();
        createOrUpdateSomeOtherEntities5();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public SomeEntity find() {
        final SomeEntity value = SomeEntity.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public final void setPrimaryKey(@NotNull String key) {
        final String[] parts = splitToArray(key, 2);
        f.set(Field.ALTO_INT, Conversions.toInt(parts[0]));
        f.set(Field.MARTINS_STRING, parts[1]);
    }

    @NotNull public String keyAsString() {
        return "" + getAltoInt() + ":" + escapeCharOn(getMartinsString(), ':');
    }

    /** Invoked when populating a form instance */
    @NotNull public SomeEntity populate() {
        final SomeEntity someEntity = find();

        setMgBool(someEntity.isSomeBool());

        getSomeOtherEntities5().populate(someEntity.getSomeOtherEntities(), SomeOtherEntities5Row::populate);

        return someEntity;
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

    /** Copies field values to given someEntity instance. */
    public void copyTo(@NotNull SomeEntity someEntity) { someEntity.setSomeBool(isMgBool()); }

    /** Updates external references to someOtherEntities. */
    public void createOrUpdateSomeOtherEntities5() {
        for (final SomeOtherEntities5Row r : getSomeOtherEntities5()) {
        	final SomeOtherEntity someOtherEntity = SomeOtherEntity.find(keyAsString());
        	if (someOtherEntity == null) {
        		final SomeOtherEntity newSomeOtherEntity = SomeOtherEntity.create(keyAsString());
        		r.copyTo(newSomeOtherEntity);
        		newSomeOtherEntity.insert();
        	}
        	else {
        		r.copyTo(someOtherEntity);
        		someOtherEntity.update();
        	}
        }
    }

    /** Returns the value of the text_field(altoInt). */
    public int getAltoInt() { return f.get(Field.ALTO_INT, Integer.class); }

    /** Sets the value of the text_field(altoInt). */
    @NotNull public BoundTableDifferNameForm setAltoInt(int altoInt) {
        f.set(Field.ALTO_INT, altoInt);
        return (BoundTableDifferNameForm) this;
    }

    /** Returns the value of the check_box(mgBool). */
    public boolean isMgBool() { return f.get(Field.MG_BOOL, Boolean.class); }

    /** Sets the value of the check_box(mgBool). */
    @NotNull public BoundTableDifferNameForm setMgBool(boolean mgBool) {
        f.set(Field.MG_BOOL, mgBool);
        return (BoundTableDifferNameForm) this;
    }

    /** Returns the value of the text_field(martinsString). */
    @NotNull public String getMartinsString() { return f.get(Field.MARTINS_STRING, String.class); }

    /** Sets the value of the text_field(martinsString). */
    @NotNull public BoundTableDifferNameForm setMartinsString(@NotNull String martinsString) {
        f.set(Field.MARTINS_STRING, martinsString);
        return (BoundTableDifferNameForm) this;
    }

    /** Returns a {@link FormTable<SomeOtherEntities5Row>} instance to handle SomeOtherEntities5 manipulation */
    @NotNull public final FormTable<SomeOtherEntities5Row> getSomeOtherEntities5() {
        return table(Field.SOME_OTHER_ENTITIES5, SomeOtherEntities5Row.class);
    }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<BoundTableDifferNameForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<BoundTableDifferNameForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<BoundTableDifferNameForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(BoundTableDifferNameForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SomeOtherEntities5RowBase
        implements FormRowInstance<SomeOtherEntities5Row>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(quantity). */
        public int getQuantity() { return f.get(Field.QUANTITY, Integer.class); }

        /** Sets the value of the text_field(quantity). */
        @NotNull public SomeOtherEntities5Row setQuantity(int quantity) {
            f.set(Field.QUANTITY, quantity);
            return (SomeOtherEntities5Row) this;
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
        @NotNull public final FormTable<SomeOtherEntities5Row> table() { return getSomeOtherEntities5(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SomeOtherEntities5Row) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given someOtherEntity instance. */
        public void populate(@NotNull SomeOtherEntity someOtherEntity) { setQuantity(someOtherEntity.getQuantity()); }

        /** Copies field values to given someOtherEntity instance. */
        public void copyTo(@NotNull SomeOtherEntity someOtherEntity) { someOtherEntity.setQuantity(getQuantity()); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        ALTO_INT("altoInt"),
        MG_BOOL("mgBool"),
        MARTINS_STRING("martinsString"),
        SOME_OTHER_ENTITIES5("someOtherEntities5"),
        QUANTITY("quantity");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
