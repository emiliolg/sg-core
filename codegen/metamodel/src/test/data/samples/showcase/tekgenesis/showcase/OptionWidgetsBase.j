package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
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
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: OptionWidgets.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class OptionWidgetsBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.OptionWidgets");
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

    /** Returns the value of the tags(tags). */
    @NotNull public Seq<String> getTags() { return f.getArray(Field.TAGS, String.class); }

    /** Sets the value of the tags(tags). */
    @NotNull public OptionWidgets setTags(@NotNull Iterable<String> tags) {
        f.setArray(Field.TAGS, tags);
        return (OptionWidgets) this;
    }

    /** Returns the value of the combo_box(realCombo). */
    public double getRealCombo() { return f.get(Field.REAL_COMBO, Double.class); }

    /** Sets the value of the combo_box(realCombo). */
    @NotNull public OptionWidgets setRealCombo(double realCombo) {
        f.set(Field.REAL_COMBO, realCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the combo_box(realCombo). */
    public void setRealComboOptions(@NotNull Iterable<Double> items) { f.opts(Field.REAL_COMBO, items); }

    /** Sets the options of the combo_box(realCombo) with the given KeyMap. */
    public void setRealComboOptions(@NotNull KeyMap items) { f.opts(Field.REAL_COMBO, items); }

    /** Returns the value of the tags_combo_box(realTagsCombo). */
    @NotNull public Seq<Double> getRealTagsCombo() { return f.getArray(Field.REAL_TAGS_COMBO, Double.class); }

    /** Sets the value of the tags_combo_box(realTagsCombo). */
    @NotNull public OptionWidgets setRealTagsCombo(@NotNull Iterable<Double> realTagsCombo) {
        f.setArray(Field.REAL_TAGS_COMBO, realTagsCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the tags_combo_box(realTagsCombo). */
    public void setRealTagsComboOptions(@NotNull Iterable<Double> items) { f.opts(Field.REAL_TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(realTagsCombo) with the given KeyMap. */
    public void setRealTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.REAL_TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(realCheckBoxGroup). */
    @NotNull public Seq<Double> getRealCheckBoxGroup() { return f.getArray(Field.REAL_CHECK_BOX_GROUP, Double.class); }

    /** Sets the value of the check_box_group(realCheckBoxGroup). */
    @NotNull public OptionWidgets setRealCheckBoxGroup(@NotNull Iterable<Double> realCheckBoxGroup) {
        f.setArray(Field.REAL_CHECK_BOX_GROUP, realCheckBoxGroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the check_box_group(realCheckBoxGroup). */
    public void setRealCheckBoxGroupOptions(@NotNull Iterable<Double> items) { f.opts(Field.REAL_CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(realCheckBoxGroup) with the given KeyMap. */
    public void setRealCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.REAL_CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(realRadiogroup). */
    public double getRealRadiogroup() { return f.get(Field.REAL_RADIOGROUP, Double.class); }

    /** Sets the value of the radio_group(realRadiogroup). */
    @NotNull public OptionWidgets setRealRadiogroup(double realRadiogroup) {
        f.set(Field.REAL_RADIOGROUP, realRadiogroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the radio_group(realRadiogroup). */
    public void setRealRadiogroupOptions(@NotNull Iterable<Double> items) { f.opts(Field.REAL_RADIOGROUP, items); }

    /** Sets the options of the radio_group(realRadiogroup) with the given KeyMap. */
    public void setRealRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.REAL_RADIOGROUP, items); }

    /** Returns the value of the list_box(realListbox). */
    public double getRealListbox() { return f.get(Field.REAL_LISTBOX, Double.class); }

    /** Sets the value of the list_box(realListbox). */
    @NotNull public OptionWidgets setRealListbox(double realListbox) {
        f.set(Field.REAL_LISTBOX, realListbox);
        return (OptionWidgets) this;
    }

    /** Sets the options of the list_box(realListbox). */
    public void setRealListboxOptions(@NotNull Iterable<Double> items) { f.opts(Field.REAL_LISTBOX, items); }

    /** Sets the options of the list_box(realListbox) with the given KeyMap. */
    public void setRealListboxOptions(@NotNull KeyMap items) { f.opts(Field.REAL_LISTBOX, items); }

    /** Returns the value of the combo_box(decimalCombo). */
    @NotNull public BigDecimal getDecimalCombo() { return f.get(Field.DECIMAL_COMBO, BigDecimal.class); }

    /** Sets the value of the combo_box(decimalCombo). */
    @NotNull public OptionWidgets setDecimalCombo(@NotNull BigDecimal decimalCombo) {
        f.set(Field.DECIMAL_COMBO, Decimals.scaleAndCheck("decimalCombo", decimalCombo, false, 10, 3));
        return (OptionWidgets) this;
    }

    /** Sets the options of the combo_box(decimalCombo). */
    public void setDecimalComboOptions(@NotNull Iterable<BigDecimal> items) { f.opts(Field.DECIMAL_COMBO, items); }

    /** Sets the options of the combo_box(decimalCombo) with the given KeyMap. */
    public void setDecimalComboOptions(@NotNull KeyMap items) { f.opts(Field.DECIMAL_COMBO, items); }

    /** Returns the value of the tags_combo_box(decimalTagsCombo). */
    @NotNull public Seq<BigDecimal> getDecimalTagsCombo() {
        return f.getArray(Field.DECIMAL_TAGS_COMBO, BigDecimal.class);
    }

    /** Sets the value of the tags_combo_box(decimalTagsCombo). */
    @NotNull public OptionWidgets setDecimalTagsCombo(@NotNull Iterable<BigDecimal> decimalTagsCombo) {
        f.setArray(Field.DECIMAL_TAGS_COMBO, decimalTagsCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the tags_combo_box(decimalTagsCombo). */
    public void setDecimalTagsComboOptions(@NotNull Iterable<BigDecimal> items) { f.opts(Field.DECIMAL_TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(decimalTagsCombo) with the given KeyMap. */
    public void setDecimalTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.DECIMAL_TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(decimalCheckBoxGroup). */
    @NotNull public Seq<BigDecimal> getDecimalCheckBoxGroup() {
        return f.getArray(Field.DECIMAL_CHECK_BOX_GROUP, BigDecimal.class);
    }

    /** Sets the value of the check_box_group(decimalCheckBoxGroup). */
    @NotNull public OptionWidgets setDecimalCheckBoxGroup(@NotNull Iterable<BigDecimal> decimalCheckBoxGroup) {
        f.setArray(Field.DECIMAL_CHECK_BOX_GROUP, decimalCheckBoxGroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the check_box_group(decimalCheckBoxGroup). */
    public void setDecimalCheckBoxGroupOptions(@NotNull Iterable<BigDecimal> items) { f.opts(Field.DECIMAL_CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(decimalCheckBoxGroup) with the given KeyMap. */
    public void setDecimalCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.DECIMAL_CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(decimalRadiogroup). */
    @NotNull public BigDecimal getDecimalRadiogroup() { return f.get(Field.DECIMAL_RADIOGROUP, BigDecimal.class); }

    /** Sets the value of the radio_group(decimalRadiogroup). */
    @NotNull public OptionWidgets setDecimalRadiogroup(@NotNull BigDecimal decimalRadiogroup) {
        f.set(Field.DECIMAL_RADIOGROUP, Decimals.scaleAndCheck("decimalRadiogroup", decimalRadiogroup, false, 10, 3));
        return (OptionWidgets) this;
    }

    /** Sets the options of the radio_group(decimalRadiogroup). */
    public void setDecimalRadiogroupOptions(@NotNull Iterable<BigDecimal> items) { f.opts(Field.DECIMAL_RADIOGROUP, items); }

    /** Sets the options of the radio_group(decimalRadiogroup) with the given KeyMap. */
    public void setDecimalRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.DECIMAL_RADIOGROUP, items); }

    /** Returns the value of the list_box(decimalListbox). */
    @NotNull public BigDecimal getDecimalListbox() { return f.get(Field.DECIMAL_LISTBOX, BigDecimal.class); }

    /** Sets the value of the list_box(decimalListbox). */
    @NotNull public OptionWidgets setDecimalListbox(@NotNull BigDecimal decimalListbox) {
        f.set(Field.DECIMAL_LISTBOX, Decimals.scaleAndCheck("decimalListbox", decimalListbox, false, 10, 3));
        return (OptionWidgets) this;
    }

    /** Sets the options of the list_box(decimalListbox). */
    public void setDecimalListboxOptions(@NotNull Iterable<BigDecimal> items) { f.opts(Field.DECIMAL_LISTBOX, items); }

    /** Sets the options of the list_box(decimalListbox) with the given KeyMap. */
    public void setDecimalListboxOptions(@NotNull KeyMap items) { f.opts(Field.DECIMAL_LISTBOX, items); }

    /** Returns the value of the combo_box(stringCombo). */
    @NotNull public String getStringCombo() { return f.get(Field.STRING_COMBO, String.class); }

    /** Sets the value of the combo_box(stringCombo). */
    @NotNull public OptionWidgets setStringCombo(@NotNull String stringCombo) {
        f.set(Field.STRING_COMBO, stringCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the combo_box(stringCombo). */
    public void setStringComboOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_COMBO, items); }

    /** Sets the options of the combo_box(stringCombo) with the given KeyMap. */
    public void setStringComboOptions(@NotNull KeyMap items) { f.opts(Field.STRING_COMBO, items); }

    /** Returns the value of the tags_combo_box(stringTagsCombo). */
    @NotNull public Seq<String> getStringTagsCombo() { return f.getArray(Field.STRING_TAGS_COMBO, String.class); }

    /** Sets the value of the tags_combo_box(stringTagsCombo). */
    @NotNull public OptionWidgets setStringTagsCombo(@NotNull Iterable<String> stringTagsCombo) {
        f.setArray(Field.STRING_TAGS_COMBO, stringTagsCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the tags_combo_box(stringTagsCombo). */
    public void setStringTagsComboOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(stringTagsCombo) with the given KeyMap. */
    public void setStringTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.STRING_TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(stringCheckBoxGroup). */
    @NotNull public Seq<String> getStringCheckBoxGroup() {
        return f.getArray(Field.STRING_CHECK_BOX_GROUP, String.class);
    }

    /** Sets the value of the check_box_group(stringCheckBoxGroup). */
    @NotNull public OptionWidgets setStringCheckBoxGroup(@NotNull Iterable<String> stringCheckBoxGroup) {
        f.setArray(Field.STRING_CHECK_BOX_GROUP, stringCheckBoxGroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the check_box_group(stringCheckBoxGroup). */
    public void setStringCheckBoxGroupOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(stringCheckBoxGroup) with the given KeyMap. */
    public void setStringCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.STRING_CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(stringRadiogroup). */
    @NotNull public String getStringRadiogroup() { return f.get(Field.STRING_RADIOGROUP, String.class); }

    /** Sets the value of the radio_group(stringRadiogroup). */
    @NotNull public OptionWidgets setStringRadiogroup(@NotNull String stringRadiogroup) {
        f.set(Field.STRING_RADIOGROUP, stringRadiogroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the radio_group(stringRadiogroup). */
    public void setStringRadiogroupOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_RADIOGROUP, items); }

    /** Sets the options of the radio_group(stringRadiogroup) with the given KeyMap. */
    public void setStringRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.STRING_RADIOGROUP, items); }

    /** Returns the value of the list_box(stringListbox). */
    @NotNull public String getStringListbox() { return f.get(Field.STRING_LISTBOX, String.class); }

    /** Sets the value of the list_box(stringListbox). */
    @NotNull public OptionWidgets setStringListbox(@NotNull String stringListbox) {
        f.set(Field.STRING_LISTBOX, stringListbox);
        return (OptionWidgets) this;
    }

    /** Sets the options of the list_box(stringListbox). */
    public void setStringListboxOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_LISTBOX, items); }

    /** Sets the options of the list_box(stringListbox) with the given KeyMap. */
    public void setStringListboxOptions(@NotNull KeyMap items) { f.opts(Field.STRING_LISTBOX, items); }

    /** Returns the value of the combo_box(enumCombo). */
    @NotNull public Options getEnumCombo() {
        return Options.valueOf(f.get(Field.ENUM_COMBO, String.class));
    }

    /** Sets the value of the combo_box(enumCombo). */
    @NotNull public OptionWidgets setEnumCombo(@NotNull Options enumCombo) {
        f.set(Field.ENUM_COMBO, enumCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the combo_box(enumCombo). */
    public void setEnumComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.ENUM_COMBO, items); }

    /** Sets the options of the combo_box(enumCombo) with the given KeyMap. */
    public void setEnumComboOptions(@NotNull KeyMap items) { f.opts(Field.ENUM_COMBO, items); }

    /** Returns the value of the tags_combo_box(enumTagsCombo). */
    @NotNull public Seq<Options> getEnumTagsCombo() { return f.getArray(Field.ENUM_TAGS_COMBO, Options.class); }

    /** Sets the value of the tags_combo_box(enumTagsCombo). */
    @NotNull public OptionWidgets setEnumTagsCombo(@NotNull Iterable<Options> enumTagsCombo) {
        f.setArray(Field.ENUM_TAGS_COMBO, enumTagsCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the tags_combo_box(enumTagsCombo). */
    public void setEnumTagsComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.ENUM_TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(enumTagsCombo) with the given KeyMap. */
    public void setEnumTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.ENUM_TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(enumCheckBoxGroup). */
    @NotNull public Seq<Options> getEnumCheckBoxGroup() {
        return f.getArray(Field.ENUM_CHECK_BOX_GROUP, Options.class);
    }

    /** Sets the value of the check_box_group(enumCheckBoxGroup). */
    @NotNull public OptionWidgets setEnumCheckBoxGroup(@NotNull Iterable<Options> enumCheckBoxGroup) {
        f.setArray(Field.ENUM_CHECK_BOX_GROUP, enumCheckBoxGroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the check_box_group(enumCheckBoxGroup). */
    public void setEnumCheckBoxGroupOptions(@NotNull Iterable<Options> items) { f.opts(Field.ENUM_CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(enumCheckBoxGroup) with the given KeyMap. */
    public void setEnumCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.ENUM_CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(enumRadiogroup). */
    @NotNull public Options getEnumRadiogroup() {
        return Options.valueOf(f.get(Field.ENUM_RADIOGROUP, String.class));
    }

    /** Sets the value of the radio_group(enumRadiogroup). */
    @NotNull public OptionWidgets setEnumRadiogroup(@NotNull Options enumRadiogroup) {
        f.set(Field.ENUM_RADIOGROUP, enumRadiogroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the radio_group(enumRadiogroup). */
    public void setEnumRadiogroupOptions(@NotNull Iterable<Options> items) { f.opts(Field.ENUM_RADIOGROUP, items); }

    /** Sets the options of the radio_group(enumRadiogroup) with the given KeyMap. */
    public void setEnumRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.ENUM_RADIOGROUP, items); }

    /** Returns the value of the list_box(enumListbox). */
    @NotNull public Options getEnumListbox() {
        return Options.valueOf(f.get(Field.ENUM_LISTBOX, String.class));
    }

    /** Sets the value of the list_box(enumListbox). */
    @NotNull public OptionWidgets setEnumListbox(@NotNull Options enumListbox) {
        f.set(Field.ENUM_LISTBOX, enumListbox);
        return (OptionWidgets) this;
    }

    /** Sets the options of the list_box(enumListbox). */
    public void setEnumListboxOptions(@NotNull Iterable<Options> items) { f.opts(Field.ENUM_LISTBOX, items); }

    /** Sets the options of the list_box(enumListbox) with the given KeyMap. */
    public void setEnumListboxOptions(@NotNull KeyMap items) { f.opts(Field.ENUM_LISTBOX, items); }

    /** Returns the value of the combo_box(entityCombo). */
    @NotNull public Make getEntityCombo() {
        return Predefined.ensureNotNull(Make.find(getEntityComboKey()), "'entityCombo' not found");
    }

    /** Returns the key value of the combo_box(entityCombo). */
    @NotNull public String getEntityComboKey() { return f.get(Field.ENTITY_COMBO, String.class); }

    /** Sets the value of the combo_box(entityCombo). */
    @NotNull public OptionWidgets setEntityCombo(@NotNull Make entityCombo) {
        f.set(Field.ENTITY_COMBO, entityCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the combo_box(entityCombo). */
    public void setEntityComboOptions(@NotNull Iterable<? extends Make> items) { f.opts(Field.ENTITY_COMBO, items); }

    /** Sets the options of the combo_box(entityCombo) with the given KeyMap. */
    public void setEntityComboOptions(@NotNull KeyMap items) { f.opts(Field.ENTITY_COMBO, items); }

    /** Returns the value of the tags_combo_box(entityTagsCombo). */
    @NotNull public Seq<Make> getEntityTagsCombo() { return f.getArray(Field.ENTITY_TAGS_COMBO, Make.class); }

    /** Sets the value of the tags_combo_box(entityTagsCombo). */
    @NotNull public OptionWidgets setEntityTagsCombo(@NotNull Iterable<Make> entityTagsCombo) {
        f.setArray(Field.ENTITY_TAGS_COMBO, entityTagsCombo);
        return (OptionWidgets) this;
    }

    /** Sets the options of the tags_combo_box(entityTagsCombo). */
    public void setEntityTagsComboOptions(@NotNull Iterable<? extends Make> items) { f.opts(Field.ENTITY_TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(entityTagsCombo) with the given KeyMap. */
    public void setEntityTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.ENTITY_TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(entityCheckBoxGroup). */
    @NotNull public Seq<Make> getEntityCheckBoxGroup() { return f.getArray(Field.ENTITY_CHECK_BOX_GROUP, Make.class); }

    /** Sets the value of the check_box_group(entityCheckBoxGroup). */
    @NotNull public OptionWidgets setEntityCheckBoxGroup(@NotNull Iterable<Make> entityCheckBoxGroup) {
        f.setArray(Field.ENTITY_CHECK_BOX_GROUP, entityCheckBoxGroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the check_box_group(entityCheckBoxGroup). */
    public void setEntityCheckBoxGroupOptions(@NotNull Iterable<? extends Make> items) { f.opts(Field.ENTITY_CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(entityCheckBoxGroup) with the given KeyMap. */
    public void setEntityCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.ENTITY_CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(entityRadiogroup). */
    @NotNull public Make getEntityRadiogroup() {
        return Predefined.ensureNotNull(Make.find(getEntityRadiogroupKey()), "'entityRadiogroup' not found");
    }

    /** Returns the key value of the radio_group(entityRadiogroup). */
    @NotNull public String getEntityRadiogroupKey() { return f.get(Field.ENTITY_RADIOGROUP, String.class); }

    /** Sets the value of the radio_group(entityRadiogroup). */
    @NotNull public OptionWidgets setEntityRadiogroup(@NotNull Make entityRadiogroup) {
        f.set(Field.ENTITY_RADIOGROUP, entityRadiogroup);
        return (OptionWidgets) this;
    }

    /** Sets the options of the radio_group(entityRadiogroup). */
    public void setEntityRadiogroupOptions(@NotNull Iterable<? extends Make> items) { f.opts(Field.ENTITY_RADIOGROUP, items); }

    /** Sets the options of the radio_group(entityRadiogroup) with the given KeyMap. */
    public void setEntityRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.ENTITY_RADIOGROUP, items); }

    /** Returns the value of the list_box(entityListbox). */
    @NotNull public Make getEntityListbox() {
        return Predefined.ensureNotNull(Make.find(getEntityListboxKey()), "'entityListbox' not found");
    }

    /** Returns the key value of the list_box(entityListbox). */
    @NotNull public String getEntityListboxKey() { return f.get(Field.ENTITY_LISTBOX, String.class); }

    /** Sets the value of the list_box(entityListbox). */
    @NotNull public OptionWidgets setEntityListbox(@NotNull Make entityListbox) {
        f.set(Field.ENTITY_LISTBOX, entityListbox);
        return (OptionWidgets) this;
    }

    /** Sets the options of the list_box(entityListbox). */
    public void setEntityListboxOptions(@NotNull Iterable<? extends Make> items) { f.opts(Field.ENTITY_LISTBOX, items); }

    /** Sets the options of the list_box(entityListbox) with the given KeyMap. */
    public void setEntityListboxOptions(@NotNull KeyMap items) { f.opts(Field.ENTITY_LISTBOX, items); }

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action some();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<OptionWidgets> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<OptionWidgets> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<OptionWidgets> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(OptionWidgets.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        RAW_OPTIONS("rawOptions"),
        TAGS("tags"),
        REAL_OPTIONS("realOptions"),
        REAL_COMBO("realCombo"),
        REAL_TAGS_COMBO("realTagsCombo"),
        REAL_CHECK_BOX_GROUP("realCheckBoxGroup"),
        REAL_RADIOGROUP("realRadiogroup"),
        REAL_LISTBOX("realListbox"),
        DECIMAL_OPTIONS("decimalOptions"),
        DECIMAL_COMBO("decimalCombo"),
        DECIMAL_TAGS_COMBO("decimalTagsCombo"),
        DECIMAL_CHECK_BOX_GROUP("decimalCheckBoxGroup"),
        DECIMAL_RADIOGROUP("decimalRadiogroup"),
        DECIMAL_LISTBOX("decimalListbox"),
        STRING_OPTIONS("stringOptions"),
        STRING_COMBO("stringCombo"),
        STRING_TAGS_COMBO("stringTagsCombo"),
        STRING_CHECK_BOX_GROUP("stringCheckBoxGroup"),
        STRING_RADIOGROUP("stringRadiogroup"),
        STRING_LISTBOX("stringListbox"),
        ENUM_OPTIONS("enumOptions"),
        ENUM_COMBO("enumCombo"),
        ENUM_TAGS_COMBO("enumTagsCombo"),
        ENUM_CHECK_BOX_GROUP("enumCheckBoxGroup"),
        ENUM_RADIOGROUP("enumRadiogroup"),
        ENUM_LISTBOX("enumListbox"),
        ENTITY_OPTIONS("entityOptions"),
        ENTITY_COMBO("entityCombo"),
        ENTITY_TAGS_COMBO("entityTagsCombo"),
        ENTITY_CHECK_BOX_GROUP("entityCheckBoxGroup"),
        ENTITY_RADIOGROUP("entityRadiogroup"),
        ENTITY_LISTBOX("entityListbox"),
        BUTTON("button");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
