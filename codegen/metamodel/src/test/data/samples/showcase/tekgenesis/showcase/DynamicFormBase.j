package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
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
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.showcase.DynamicForm.WidgetsRow;

/** 
 * Generated base class for form: DynamicForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DynamicFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DynamicForm");
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

    /** Returns a {@link FormTable<WidgetsRow>} instance to handle Widgets manipulation */
    @NotNull public final FormTable<WidgetsRow> getWidgets() { return table(Field.WIDGETS, WidgetsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(property). */
    public void setPropertyOptions(@NotNull Iterable<? extends Property> items) { f.opts(Field.PROPERTY, items); }

    /** Sets the options of the combo_box(property) with the given KeyMap. */
    public void setPropertyOptions(@NotNull KeyMap items) { f.opts(Field.PROPERTY, items); }

    /** Sets the options of the dynamic(value) with the given KeyMap. */
    public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

    /** Returns the value of the dynamic(mail). */
    @NotNull public Object getMail() { return f.get(Field.MAIL, Object.class); }

    /** Sets the value of the dynamic(mail). */
    @NotNull public DynamicForm setMail(@NotNull Object mail) {
        f.set(Field.MAIL, mail);
        return (DynamicForm) this;
    }

    /** Sets the options of the dynamic(mail) with the given KeyMap. */
    public void setMailOptions(@NotNull KeyMap items) { f.opts(Field.MAIL, items); }

    /** Returns the value of the internal(mailInternal). */
    public boolean isMailInternal() { return f.get(Field.MAIL_INTERNAL, Boolean.class); }

    /** Sets the value of the internal(mailInternal). */
    @NotNull public DynamicForm setMailInternal(boolean mailInternal) {
        f.set(Field.MAIL_INTERNAL, mailInternal);
        return (DynamicForm) this;
    }

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action changeMailType();

    /** Invoked when dynamic(range) value changes */
    @NotNull public abstract Action rangeChanged();

    /** Returns the value of the dynamic(range). */
    @NotNull public Seq<Object> getRange() { return f.getArray(Field.RANGE, Object.class); }

    /** Sets the value of the dynamic(range). */
    @NotNull public DynamicForm setRange(@NotNull Iterable<Object> range) {
        f.setArray(Field.RANGE, range);
        return (DynamicForm) this;
    }

    /** Sets the options of the dynamic(range) with the given KeyMap. */
    public void setRangeOptions(@NotNull KeyMap items) { f.opts(Field.RANGE, items); }

    /** Returns the value of the dynamic(rangeValue). */
    @NotNull public Object getRangeValue() { return f.get(Field.RANGE_VALUE, Object.class); }

    /** Sets the value of the dynamic(rangeValue). */
    @NotNull public DynamicForm setRangeValue(@NotNull Object rangeValue) {
        f.set(Field.RANGE_VALUE, rangeValue);
        return (DynamicForm) this;
    }

    /** Sets the options of the dynamic(rangeValue) with the given KeyMap. */
    public void setRangeValueOptions(@NotNull KeyMap items) { f.opts(Field.RANGE_VALUE, items); }

    /** Returns the value of the dynamic(radioDynamic). */
    @NotNull public Object getRadioDynamic() { return f.get(Field.RADIO_DYNAMIC, Object.class); }

    /** Sets the value of the dynamic(radioDynamic). */
    @NotNull public DynamicForm setRadioDynamic(@NotNull Object radioDynamic) {
        f.set(Field.RADIO_DYNAMIC, radioDynamic);
        return (DynamicForm) this;
    }

    /** Sets the options of the dynamic(radioDynamic) with the given KeyMap. */
    public void setRadioDynamicOptions(@NotNull KeyMap items) { f.opts(Field.RADIO_DYNAMIC, items); }

    /** Invoked when combo_box(combo) value changes */
    @NotNull public abstract Action updateDyn();

    /** Returns the value of the combo_box(combo). */
    @NotNull public DynOptions getCombo() { return DynOptions.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public DynamicForm setCombo(@NotNull DynOptions combo) {
        f.set(Field.COMBO, combo);
        return (DynamicForm) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<DynOptions> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Returns the value of the dynamic(dynimicValue). */
    @NotNull public Object getDynimicValue() { return f.get(Field.DYNIMIC_VALUE, Object.class); }

    /** Sets the value of the dynamic(dynimicValue). */
    @NotNull public DynamicForm setDynimicValue(@NotNull Object dynimicValue) {
        f.set(Field.DYNIMIC_VALUE, dynimicValue);
        return (DynamicForm) this;
    }

    /** Sets the options of the dynamic(dynimicValue) with the given KeyMap. */
    public void setDynimicValueOptions(@NotNull KeyMap items) { f.opts(Field.DYNIMIC_VALUE, items); }

    /** Invoked when button($B7) is clicked */
    @NotNull public abstract Action onValidate();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DynamicForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DynamicForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DynamicForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DynamicForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class WidgetsRowBase
        implements FormRowInstance<WidgetsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(id). */
        @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the internal(id). */
        @NotNull public WidgetsRow setId(@Nullable Integer id) {
            f.set(Field.ID, id);
            return (WidgetsRow) this;
        }

        /** Returns the value of the check_box(mandatory). */
        public boolean isMandatory() { return f.get(Field.MANDATORY, Boolean.class); }

        /** Sets the value of the check_box(mandatory). */
        @NotNull public WidgetsRow setMandatory(boolean mandatory) {
            f.set(Field.MANDATORY, mandatory);
            return (WidgetsRow) this;
        }

        /** Invoked when combo_box(property) value changes */
        @NotNull public abstract Action updateDynamic();

        /** Returns the value of the combo_box(property). */
        @NotNull public Property getProperty() {
            return Predefined.ensureNotNull(Property.find(getPropertyKey()), "'property' not found");
        }

        /** Returns the key value of the combo_box(property). */
        @NotNull public String getPropertyKey() { return f.get(Field.PROPERTY, String.class); }

        /** Sets the value of the combo_box(property). */
        @NotNull public WidgetsRow setProperty(@NotNull Property property) {
            f.set(Field.PROPERTY, property);
            return (WidgetsRow) this;
        }

        /** Sets the options of the combo_box(property). */
        public void setPropertyOptions(@NotNull Iterable<? extends Property> items) { f.opts(Field.PROPERTY, items); }

        /** Sets the options of the combo_box(property) with the given KeyMap. */
        public void setPropertyOptions(@NotNull KeyMap items) { f.opts(Field.PROPERTY, items); }

        /** Returns the value of the dynamic(value). */
        @NotNull public Seq<Object> getValue() { return f.getArray(Field.VALUE, Object.class); }

        /** Sets the value of the dynamic(value). */
        @NotNull public WidgetsRow setValue(@NotNull Iterable<Object> value) {
            f.setArray(Field.VALUE, value);
            return (WidgetsRow) this;
        }

        /** Sets the options of the dynamic(value) with the given KeyMap. */
        public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

        /** Invoked when button(clear) is clicked */
        @NotNull public abstract Action clear();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<WidgetsRow> table() { return getWidgets(); }

        /** Remove row from table and delete associated DynamicProperty instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final DynamicProperty instance = isDefined(Field.ID) ? DynamicProperty.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((WidgetsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given dynamicProperty instance. */
        public void populate(@NotNull DynamicProperty dynamicProperty) {
            setId(dynamicProperty.getId())
            	.setProperty(dynamicProperty.getProperty());
        }

        /** Copies field values to given dynamicProperty instance. */
        public void copyTo(@NotNull DynamicProperty dynamicProperty) { dynamicProperty.setProperty(getProperty()); }

        /** Return primary key of bound {@link DynamicProperty} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        WIDGETS("widgets"),
        ID("id"),
        MANDATORY("mandatory"),
        PROPERTY("property"),
        VALUE("value"),
        $T3("$T3"),
        CLEAR("clear"),
        $H4("$H4"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
        MAIL("mail"),
        MAIL_INTERNAL("mailInternal"),
        BUTTON("button"),
        RANGE("range"),
        RANGE_VALUE("rangeValue"),
        RADIO_DYNAMIC("radioDynamic"),
        COMBO("combo"),
        DYNIMIC_VALUE("dynimicValue"),
        $F5("$F5"),
        $B6("$B6"),
        $B7("$B7");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
