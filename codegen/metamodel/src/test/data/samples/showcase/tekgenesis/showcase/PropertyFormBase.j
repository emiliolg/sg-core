package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.showcase.PropertyForm.ValuesRow;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.escapeCharOn;
import static tekgenesis.common.core.Strings.splitToArray;

/** 
 * Generated base class for form: PropertyForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PropertyFormBase
    extends FormInstance<Property>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Property property = Property.create(getName(), getType());
        copyTo(property);
        createOrUpdateValues(property);
        property.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Property property = find();
        copyTo(property);
        createOrUpdateValues(property);
        property.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Property find() {
        final Property value = Property.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public final void setPrimaryKey(@NotNull String key) {
        final String[] parts = splitToArray(key, 2);
        f.set(Field.NAME, parts[0]);
        f.set(Field.TYPE, PropertyType.valueOf(parts[1]));
    }

    @NotNull public String keyAsString() { return "" + escapeCharOn(getName(), ':') + ":" + getType(); }

    /** Invoked when populating a form instance */
    @NotNull public Property populate() {
        final Property property = find();

        setMultiple(property.isMultiple());

        getValues().populate(property.getValues(), ValuesRow::populate);

        return property;
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

    /** Copies field values to given property instance. */
    public void copyTo(@NotNull Property property) { property.setMultiple(isMultiple()); }

    /** Updates external references to values. */
    public void createOrUpdateValues(@NotNull Property property) {
        property.getValues().merge(getValues(), (validValue, row) -> row.copyTo(validValue));
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public PropertyForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (PropertyForm) this;
    }

    /** Returns the value of the combo_box(type). */
    @NotNull public PropertyType getType() {
        return PropertyType.valueOf(f.get(Field.TYPE, String.class));
    }

    /** Sets the value of the combo_box(type). */
    @NotNull public PropertyForm setType(@NotNull PropertyType type) {
        f.set(Field.TYPE, type);
        return (PropertyForm) this;
    }

    /** Sets the options of the combo_box(type). */
    public void setTypeOptions(@NotNull Iterable<PropertyType> items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the combo_box(type) with the given KeyMap. */
    public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

    /** Returns the value of the check_box(multiple). */
    public boolean isMultiple() { return f.get(Field.MULTIPLE, Boolean.class); }

    /** Sets the value of the check_box(multiple). */
    @NotNull public PropertyForm setMultiple(boolean multiple) {
        f.set(Field.MULTIPLE, multiple);
        return (PropertyForm) this;
    }

    /** Returns a {@link FormTable<ValuesRow>} instance to handle Values manipulation */
    @NotNull public final FormTable<ValuesRow> getValues() { return table(Field.VALUES, ValuesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PropertyForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PropertyForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<PropertyForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(PropertyForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ValuesRowBase
        implements FormRowInstance<ValuesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(value). */
        @NotNull public String getValue() { return f.get(Field.VALUE, String.class); }

        /** Sets the value of the text_field(value). */
        @NotNull public ValuesRow setValue(@NotNull String value) {
            f.set(Field.VALUE, value);
            return (ValuesRow) this;
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
        @NotNull public final FormTable<ValuesRow> table() { return getValues(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ValuesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given validValue instance. */
        public void populate(@NotNull ValidValue validValue) { setValue(validValue.getValue()); }

        /** Copies field values to given validValue instance. */
        public void copyTo(@NotNull ValidValue validValue) { validValue.setValue(getValue()); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        NAME("name"),
        TYPE("type"),
        MULTIPLE("multiple"),
        VALUES("values"),
        VALUE("value"),
        $H3("$H3"),
        $B4("$B4"),
        $B5("$B5"),
        $F6("$F6"),
        $B7("$B7"),
        $B8("$B8"),
        $B9("$B9");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
