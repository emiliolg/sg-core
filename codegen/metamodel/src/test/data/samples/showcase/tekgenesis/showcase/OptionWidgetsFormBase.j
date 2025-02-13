package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
 * Generated base class for form: OptionWidgetsForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class OptionWidgetsFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.OptionWidgetsForm");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Returns the value of the combo_box(simpleE). */
    @NotNull public SimpleEntity getSimpleE() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSimpleEKey()), "'simpleE' not found");
    }

    /** Returns the key value of the combo_box(simpleE). */
    @NotNull public String getSimpleEKey() { return f.get(Field.SIMPLE_E, String.class); }

    /** Sets the value of the combo_box(simpleE). */
    @NotNull public OptionWidgetsForm setSimpleE(@NotNull SimpleEntity simpleE) {
        f.set(Field.SIMPLE_E, simpleE);
        return (OptionWidgetsForm) this;
    }

    /** Sets the options of the combo_box(simpleE). */
    public void setSimpleEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.SIMPLE_E, items); }

    /** Sets the options of the combo_box(simpleE) with the given KeyMap. */
    public void setSimpleEOptions(@NotNull KeyMap items) { f.opts(Field.SIMPLE_E, items); }

    /** Invoked when button(simpleButton) is clicked */
    @NotNull public abstract Action setSimpleE();

    /** Invoked when button(simpleOptionButton) is clicked */
    @NotNull public abstract Action setSimpleEOptions();

    /** Returns the value of the tags_combo_box(tagsSimpE). */
    @NotNull public Seq<SimpleEntity> getTagsSimpE() { return f.getArray(Field.TAGS_SIMP_E, SimpleEntity.class); }

    /** Sets the value of the tags_combo_box(tagsSimpE). */
    @NotNull public OptionWidgetsForm setTagsSimpE(@NotNull Iterable<SimpleEntity> tagsSimpE) {
        f.setArray(Field.TAGS_SIMP_E, tagsSimpE);
        return (OptionWidgetsForm) this;
    }

    /** Sets the options of the tags_combo_box(tagsSimpE). */
    public void setTagsSimpEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.TAGS_SIMP_E, items); }

    /** Sets the options of the tags_combo_box(tagsSimpE) with the given KeyMap. */
    public void setTagsSimpEOptions(@NotNull KeyMap items) { f.opts(Field.TAGS_SIMP_E, items); }

    /** Invoked when button(tagsButton) is clicked */
    @NotNull public abstract Action setTagsE();

    /** Invoked when button(tagsOptionButton) is clicked */
    @NotNull public abstract Action setTagsEOptions();

    /** Returns the value of the pick_list(pickSimpE). */
    @NotNull public Seq<SimpleEntity> getPickSimpE() { return f.getArray(Field.PICK_SIMP_E, SimpleEntity.class); }

    /** Sets the value of the pick_list(pickSimpE). */
    @NotNull public OptionWidgetsForm setPickSimpE(@NotNull Iterable<SimpleEntity> pickSimpE) {
        f.setArray(Field.PICK_SIMP_E, pickSimpE);
        return (OptionWidgetsForm) this;
    }

    /** Sets the options of the pick_list(pickSimpE). */
    public void setPickSimpEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.PICK_SIMP_E, items); }

    /** Sets the options of the pick_list(pickSimpE) with the given KeyMap. */
    public void setPickSimpEOptions(@NotNull KeyMap items) { f.opts(Field.PICK_SIMP_E, items); }

    /** Invoked when button(pickButton) is clicked */
    @NotNull public abstract Action setPicklistE();

    /** Invoked when button(pickOptionButton) is clicked */
    @NotNull public abstract Action setPicklistEOptions();

    /** Returns the value of the breadcrumb(breadcrumbSimpE). */
    @NotNull public SimpleEntity getBreadcrumbSimpE() {
        return Predefined.ensureNotNull(SimpleEntity.find(getBreadcrumbSimpEKey()), "'breadcrumbSimpE' not found");
    }

    /** Returns the key value of the breadcrumb(breadcrumbSimpE). */
    @NotNull public String getBreadcrumbSimpEKey() { return f.get(Field.BREADCRUMB_SIMP_E, String.class); }

    /** Sets the options of the breadcrumb(breadcrumbSimpE). */
    public void setBreadcrumbSimpEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.BREADCRUMB_SIMP_E, items); }

    /** Sets the options of the breadcrumb(breadcrumbSimpE) with the given KeyMap. */
    public void setBreadcrumbSimpEOptions(@NotNull KeyMap items) { f.opts(Field.BREADCRUMB_SIMP_E, items); }

    /** Invoked when button(breadcrumbButton) is clicked */
    @NotNull public abstract Action setBreadcrumbE();

    /** Invoked when button(breadcrumbOptionButton) is clicked */
    @NotNull public abstract Action setBreadcrumbEOptions();

    /** Returns the value of the check_box_group(checkSimpE). */
    @NotNull public Seq<SimpleEntity> getCheckSimpE() { return f.getArray(Field.CHECK_SIMP_E, SimpleEntity.class); }

    /** Sets the value of the check_box_group(checkSimpE). */
    @NotNull public OptionWidgetsForm setCheckSimpE(@NotNull Iterable<SimpleEntity> checkSimpE) {
        f.setArray(Field.CHECK_SIMP_E, checkSimpE);
        return (OptionWidgetsForm) this;
    }

    /** Sets the options of the check_box_group(checkSimpE). */
    public void setCheckSimpEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.CHECK_SIMP_E, items); }

    /** Sets the options of the check_box_group(checkSimpE) with the given KeyMap. */
    public void setCheckSimpEOptions(@NotNull KeyMap items) { f.opts(Field.CHECK_SIMP_E, items); }

    /** Invoked when button(checkButton) is clicked */
    @NotNull public abstract Action setCheckGroupE();

    /** Invoked when button(checkOptionButton) is clicked */
    @NotNull public abstract Action setCheckGroupEOptions();

    /** Returns the value of the radio_group(radioSimpE). */
    @NotNull public SimpleEntity getRadioSimpE() {
        return Predefined.ensureNotNull(SimpleEntity.find(getRadioSimpEKey()), "'radioSimpE' not found");
    }

    /** Returns the key value of the radio_group(radioSimpE). */
    @NotNull public String getRadioSimpEKey() { return f.get(Field.RADIO_SIMP_E, String.class); }

    /** Sets the value of the radio_group(radioSimpE). */
    @NotNull public OptionWidgetsForm setRadioSimpE(@NotNull SimpleEntity radioSimpE) {
        f.set(Field.RADIO_SIMP_E, radioSimpE);
        return (OptionWidgetsForm) this;
    }

    /** Sets the options of the radio_group(radioSimpE). */
    public void setRadioSimpEOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.RADIO_SIMP_E, items); }

    /** Sets the options of the radio_group(radioSimpE) with the given KeyMap. */
    public void setRadioSimpEOptions(@NotNull KeyMap items) { f.opts(Field.RADIO_SIMP_E, items); }

    /** Invoked when button(radioSButton) is clicked */
    @NotNull public abstract Action setRadioGroupE();

    /** Invoked when button(radioOptionButton) is clicked */
    @NotNull public abstract Action setRadioGroupEOptions();

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action validate();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<OptionWidgetsForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<OptionWidgetsForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<OptionWidgetsForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(OptionWidgetsForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        SIMPLE_E("simpleE"),
        SIMPLE_BUTTON("simpleButton"),
        SIMPLE_OPTION_BUTTON("simpleOptionButton"),
        $H1("$H1"),
        TAGS_SIMP_E("tagsSimpE"),
        TAGS_BUTTON("tagsButton"),
        TAGS_OPTION_BUTTON("tagsOptionButton"),
        $H2("$H2"),
        PICK_SIMP_E("pickSimpE"),
        PICK_BUTTON("pickButton"),
        PICK_OPTION_BUTTON("pickOptionButton"),
        $H3("$H3"),
        BREADCRUMB_SIMP_E("breadcrumbSimpE"),
        BREADCRUMB_BUTTON("breadcrumbButton"),
        BREADCRUMB_OPTION_BUTTON("breadcrumbOptionButton"),
        $H4("$H4"),
        CHECK_SIMP_E("checkSimpE"),
        CHECK_BUTTON("checkButton"),
        CHECK_OPTION_BUTTON("checkOptionButton"),
        $H5("$H5"),
        RADIO_SIMP_E("radioSimpE"),
        RADIO_SBUTTON("radioSButton"),
        RADIO_OPTION_BUTTON("radioOptionButton"),
        BUTTON("button");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
