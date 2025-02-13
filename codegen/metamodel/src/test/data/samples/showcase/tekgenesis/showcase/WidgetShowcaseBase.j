package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import java.util.Comparator;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.ExecutionFeedback;
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
import tekgenesis.common.core.Resource;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: WidgetShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class WidgetShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.WidgetShowcase");
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

    /** Returns the value of the display(text). */
    @NotNull public String getText() { return f.get(Field.TEXT, String.class); }

    /** Returns the value of the text_field(textfield). */
    @NotNull public String getTextfield() { return f.get(Field.TEXTFIELD, String.class); }

    /** Sets the value of the text_field(textfield). */
    @NotNull public WidgetShowcase setTextfield(@NotNull String textfield) {
        f.set(Field.TEXTFIELD, textfield);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the mail_field(mailFiled). */
    @NotNull public String getMailFiled() { return f.get(Field.MAIL_FILED, String.class); }

    /** Sets the value of the mail_field(mailFiled). */
    @NotNull public WidgetShowcase setMailFiled(@NotNull String mailFiled) {
        f.set(Field.MAIL_FILED, mailFiled);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the mail_field(mailFiled). */
    public void setMailFiledOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_FILED, items); }

    /** Returns the value of the text_field(iconField). */
    @NotNull public String getIconField() { return f.get(Field.ICON_FIELD, String.class); }

    /** Sets the value of the text_field(iconField). */
    @NotNull public WidgetShowcase setIconField(@NotNull String iconField) {
        f.set(Field.ICON_FIELD, iconField);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the text_field(noPasteTextfield). */
    @NotNull public String getNoPasteTextfield() { return f.get(Field.NO_PASTE_TEXTFIELD, String.class); }

    /** Sets the value of the text_field(noPasteTextfield). */
    @NotNull public WidgetShowcase setNoPasteTextfield(@NotNull String noPasteTextfield) {
        f.set(Field.NO_PASTE_TEXTFIELD, noPasteTextfield);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the password_field(password). */
    @NotNull public String getPassword() { return f.get(Field.PASSWORD, String.class); }

    /** Sets the value of the password_field(password). */
    @NotNull public WidgetShowcase setPassword(@NotNull String password) {
        f.set(Field.PASSWORD, password);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the text_area(textarea). */
    @NotNull public String getTextarea() { return f.get(Field.TEXTAREA, String.class); }

    /** Sets the value of the text_area(textarea). */
    @NotNull public WidgetShowcase setTextarea(@NotNull String textarea) {
        f.set(Field.TEXTAREA, textarea);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the rich_text_area(richText). */
    @NotNull public String getRichText() { return f.get(Field.RICH_TEXT, String.class); }

    /** Sets the value of the rich_text_area(richText). */
    @NotNull public WidgetShowcase setRichText(@NotNull String richText) {
        f.set(Field.RICH_TEXT, richText);
        return (WidgetShowcase) this;
    }

    /** Invoked when color_picker(colorpicker) value changes */
    @NotNull public abstract Action printColor();

    /** Returns the value of the color_picker(colorpicker). */
    @NotNull public String getColorpicker() { return f.get(Field.COLORPICKER, String.class); }

    /** Sets the value of the color_picker(colorpicker). */
    @NotNull public WidgetShowcase setColorpicker(@NotNull String colorpicker) {
        f.set(Field.COLORPICKER, colorpicker);
        return (WidgetShowcase) this;
    }

    /** Invoked when breadcrumb(breadcrumb) value changes */
    @NotNull public abstract Action breadCrumbNavigation();

    /** Returns the value of the breadcrumb(breadcrumb). */
    @NotNull public String getBreadcrumb() { return f.get(Field.BREADCRUMB, String.class); }

    /** Sets the options of the breadcrumb(breadcrumb). */
    public void setBreadcrumbOptions(@NotNull Iterable<String> items) { f.opts(Field.BREADCRUMB, items); }

    /** Sets the options of the breadcrumb(breadcrumb) with the given KeyMap. */
    public void setBreadcrumbOptions(@NotNull KeyMap items) { f.opts(Field.BREADCRUMB, items); }

    /** Invoked when suggest_box(suggest) value changes */
    @NotNull public abstract Action makeChanged();

    /** Returns the value of the suggest_box(suggest). */
    @NotNull public Make getSuggest() {
        return Predefined.ensureNotNull(Make.find(getSuggestKey()), "'suggest' not found");
    }

    /** Returns the key value of the suggest_box(suggest). */
    @NotNull public String getSuggestKey() { return f.get(Field.SUGGEST, String.class); }

    /** Sets the value of the suggest_box(suggest). */
    @NotNull public WidgetShowcase setSuggest(@NotNull Make suggest) {
        f.set(Field.SUGGEST, suggest);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the message(suggestInfo). */
    @NotNull public String getSuggestInfo() { return f.get(Field.SUGGEST_INFO, String.class); }

    /** Sets the value of the message(suggestInfo). */
    @NotNull public WidgetShowcase setSuggestInfo(@NotNull String suggestInfo) {
        f.set(Field.SUGGEST_INFO, suggestInfo);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the combo_box(combo). */
    @NotNull public Options getCombo() { return Options.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public WidgetShowcase setCombo(@NotNull Options combo) {
        f.set(Field.COMBO, combo);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Returns the value of the tags_combo_box(tagsCombo). */
    @NotNull public Seq<Options> getTagsCombo() { return f.getArray(Field.TAGS_COMBO, Options.class); }

    /** Sets the value of the tags_combo_box(tagsCombo). */
    @NotNull public WidgetShowcase setTagsCombo(@NotNull Iterable<Options> tagsCombo) {
        f.setArray(Field.TAGS_COMBO, tagsCombo);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the tags_combo_box(tagsCombo). */
    public void setTagsComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(tagsCombo) with the given KeyMap. */
    public void setTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.TAGS_COMBO, items); }

    /** Returns the value of the check_box_group(checkBoxGroup). */
    @NotNull public Seq<Options> getCheckBoxGroup() { return f.getArray(Field.CHECK_BOX_GROUP, Options.class); }

    /** Sets the value of the check_box_group(checkBoxGroup). */
    @NotNull public WidgetShowcase setCheckBoxGroup(@NotNull Iterable<Options> checkBoxGroup) {
        f.setArray(Field.CHECK_BOX_GROUP, checkBoxGroup);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the check_box_group(checkBoxGroup). */
    public void setCheckBoxGroupOptions(@NotNull Iterable<Options> items) { f.opts(Field.CHECK_BOX_GROUP, items); }

    /** Sets the options of the check_box_group(checkBoxGroup) with the given KeyMap. */
    public void setCheckBoxGroupOptions(@NotNull KeyMap items) { f.opts(Field.CHECK_BOX_GROUP, items); }

    /** Returns the value of the radio_group(radiogroup). */
    @NotNull public Options getRadiogroup() {
        return Options.valueOf(f.get(Field.RADIOGROUP, String.class));
    }

    /** Sets the value of the radio_group(radiogroup). */
    @NotNull public WidgetShowcase setRadiogroup(@NotNull Options radiogroup) {
        f.set(Field.RADIOGROUP, radiogroup);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the radio_group(radiogroup). */
    public void setRadiogroupOptions(@NotNull Iterable<Options> items) { f.opts(Field.RADIOGROUP, items); }

    /** Sets the options of the radio_group(radiogroup) with the given KeyMap. */
    public void setRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.RADIOGROUP, items); }

    /** Returns the value of the list_box(listbox). */
    @NotNull public Options getListbox() { return Options.valueOf(f.get(Field.LISTBOX, String.class)); }

    /** Sets the value of the list_box(listbox). */
    @NotNull public WidgetShowcase setListbox(@NotNull Options listbox) {
        f.set(Field.LISTBOX, listbox);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the list_box(listbox). */
    public void setListboxOptions(@NotNull Iterable<Options> items) { f.opts(Field.LISTBOX, items); }

    /** Sets the options of the list_box(listbox) with the given KeyMap. */
    public void setListboxOptions(@NotNull KeyMap items) { f.opts(Field.LISTBOX, items); }

    /** Invoked when pick_list(pick) value ui changes */
    @NotNull public abstract Action changed();

    /** Returns the value of the pick_list(pick). */
    @NotNull public Seq<Options> getPick() { return f.getArray(Field.PICK, Options.class); }

    /** Sets the value of the pick_list(pick). */
    @NotNull public WidgetShowcase setPick(@NotNull Iterable<Options> pick) {
        f.setArray(Field.PICK, pick);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the pick_list(pick). */
    public void setPickOptions(@NotNull Iterable<Options> items) { f.opts(Field.PICK, items); }

    /** Sets the options of the pick_list(pick) with the given KeyMap. */
    public void setPickOptions(@NotNull KeyMap items) { f.opts(Field.PICK, items); }

    /** Returns the value of the tags(tags). */
    @NotNull public Seq<String> getTags() { return f.getArray(Field.TAGS, String.class); }

    /** Sets the value of the tags(tags). */
    @NotNull public WidgetShowcase setTags(@NotNull Iterable<String> tags) {
        f.setArray(Field.TAGS, tags);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the tags(intTags). */
    @NotNull public Seq<Integer> getIntTags() { return f.getArray(Field.INT_TAGS, Integer.class); }

    /** Sets the value of the tags(intTags). */
    @NotNull public WidgetShowcase setIntTags(@NotNull Iterable<Integer> intTags) {
        f.setArray(Field.INT_TAGS, intTags);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the tags(realTags). */
    @NotNull public Seq<Double> getRealTags() { return f.getArray(Field.REAL_TAGS, Double.class); }

    /** Sets the value of the tags(realTags). */
    @NotNull public WidgetShowcase setRealTags(@NotNull Iterable<Double> realTags) {
        f.setArray(Field.REAL_TAGS, realTags);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the tags(decimalTags). */
    @NotNull public Seq<BigDecimal> getDecimalTags() { return f.getArray(Field.DECIMAL_TAGS, BigDecimal.class); }

    /** Sets the value of the tags(decimalTags). */
    @NotNull public WidgetShowcase setDecimalTags(@NotNull Iterable<BigDecimal> decimalTags) {
        f.setArray(Field.DECIMAL_TAGS, decimalTags);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the tags(mailTags). */
    @NotNull public Seq<String> getMailTags() { return f.getArray(Field.MAIL_TAGS, String.class); }

    /** Sets the value of the tags(mailTags). */
    @NotNull public WidgetShowcase setMailTags(@NotNull Iterable<String> mailTags) {
        f.setArray(Field.MAIL_TAGS, mailTags);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the gallery(gallery). */
    @NotNull public Seq<Resource> getGallery() { return f.getArray(Field.GALLERY, Resource.class); }

    /** Sets the value of the gallery(gallery). */
    @NotNull public WidgetShowcase setGallery(@NotNull Iterable<Resource> gallery) {
        f.setArray(Field.GALLERY, gallery);
        return (WidgetShowcase) this;
    }

    /** Invoked when tree_view(treeView) value changes */
    @NotNull public abstract Action nodeSelected();

    /** Returns the value of the tree_view(treeView). */
    @NotNull public Node getTreeView() {
        return Predefined.ensureNotNull(Node.find(getTreeViewKey()), "'treeView' not found");
    }

    /** Returns the key value of the tree_view(treeView). */
    @NotNull public String getTreeViewKey() { return f.get(Field.TREE_VIEW, String.class); }

    /** Sets the value of the tree_view(treeView). */
    @NotNull public WidgetShowcase setTreeView(@NotNull Node treeView) {
        f.set(Field.TREE_VIEW, treeView);
        return (WidgetShowcase) this;
    }

    /** Sets the options of the tree_view(treeView). */
    public void setTreeViewOptions(@NotNull Iterable<? extends Node> items) { f.optsTree(Field.TREE_VIEW, items); }

    /** Sets the options of the tree_view(treeView). */
    public void setTreeViewOptions(@NotNull Iterable<? extends Node> items, @NotNull Comparator<? super Node> comparator) { f.optsTree(Field.TREE_VIEW, items, comparator); }

    /** Returns the value of the text_field(progressNumber). */
    @Nullable public Double getProgressNumber() { return f.get(Field.PROGRESS_NUMBER, Double.class); }

    /** Sets the value of the text_field(progressNumber). */
    @NotNull public WidgetShowcase setProgressNumber(@Nullable Double progressNumber) {
        f.set(Field.PROGRESS_NUMBER, progressNumber);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the text_field(fromNum). */
    @Nullable public Double getFromNum() { return f.get(Field.FROM_NUM, Double.class); }

    /** Sets the value of the text_field(fromNum). */
    @NotNull public WidgetShowcase setFromNum(@Nullable Double fromNum) {
        f.set(Field.FROM_NUM, fromNum);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the progress(progressBar). */
    public double getProgressBar() { return f.get(Field.PROGRESS_BAR, Double.class); }

    /** 
     * Invoked when button($B7) is clicked
     * Invoked when button($B33) is clicked
     */
    @NotNull public abstract Action doStuff();

    /** Invoked when button(synBtn) is clicked */
    @NotNull public abstract Action doWait();

    /** Invoked when button(longBtn) is clicked */
    @NotNull public abstract Action doLongExecution(@NotNull ExecutionFeedback feedback);

    /** Invoked when button(btn) is clicked */
    @NotNull public abstract Action randomProgress();

    /** Returns the value of the text_field(words). */
    @NotNull public String getWords() { return f.get(Field.WORDS, String.class); }

    /** Sets the value of the text_field(words). */
    @NotNull public WidgetShowcase setWords(@NotNull String words) {
        f.set(Field.WORDS, words);
        return (WidgetShowcase) this;
    }

    /** Invoked when button(download) is clicked */
    @NotNull public abstract Action download();

    /** Returns the value of the toggle_button(togglebutton). */
    public boolean isTogglebutton() { return f.get(Field.TOGGLEBUTTON, Boolean.class); }

    /** Sets the value of the toggle_button(togglebutton). */
    @NotNull public WidgetShowcase setTogglebutton(boolean togglebutton) {
        f.set(Field.TOGGLEBUTTON, togglebutton);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the check_box(checkbox). */
    public boolean isCheckbox() { return f.get(Field.CHECKBOX, Boolean.class); }

    /** Sets the value of the check_box(checkbox). */
    @NotNull public WidgetShowcase setCheckbox(boolean checkbox) {
        f.set(Field.CHECKBOX, checkbox);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the date_box(datebox). */
    @NotNull public DateOnly getDatebox() {
        return DateOnly.fromMilliseconds(f.get(Field.DATEBOX, Long.class));
    }

    /** Sets the value of the date_box(datebox). */
    @NotNull public WidgetShowcase setDatebox(@NotNull DateOnly datebox) {
        f.set(Field.DATEBOX, datebox);
        return (WidgetShowcase) this;
    }

    /** Invoked when date_box(datebox2) value changes */
    @NotNull public abstract Action viewDate();

    /** Returns the value of the date_box(datebox2). */
    @NotNull public DateOnly getDatebox2() {
        return DateOnly.fromMilliseconds(f.get(Field.DATEBOX2, Long.class));
    }

    /** Sets the value of the date_box(datebox2). */
    @NotNull public WidgetShowcase setDatebox2(@NotNull DateOnly datebox2) {
        f.set(Field.DATEBOX2, datebox2);
        return (WidgetShowcase) this;
    }

    /** Invoked when date_time_box(datetimebox) value changes */
    @NotNull public abstract Action viewDateTimeBox();

    /** Returns the value of the date_time_box(datetimebox). */
    @NotNull public DateTime getDatetimebox() {
        return DateTime.fromMilliseconds(f.get(Field.DATETIMEBOX, Long.class));
    }

    /** Sets the value of the date_time_box(datetimebox). */
    @NotNull public WidgetShowcase setDatetimebox(@NotNull DateTime datetimebox) {
        f.set(Field.DATETIMEBOX, datetimebox);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the time_picker(timepicker). */
    public int getTimepicker() { return f.get(Field.TIMEPICKER, Integer.class); }

    /** Sets the value of the time_picker(timepicker). */
    @NotNull public WidgetShowcase setTimepicker(int timepicker) {
        f.set(Field.TIMEPICKER, timepicker);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the date_picker(datepicker). */
    @NotNull public DateOnly getDatepicker() {
        return DateOnly.fromMilliseconds(f.get(Field.DATEPICKER, Long.class));
    }

    /** Sets the value of the date_picker(datepicker). */
    @NotNull public WidgetShowcase setDatepicker(@NotNull DateOnly datepicker) {
        f.set(Field.DATEPICKER, datepicker);
        return (WidgetShowcase) this;
    }

    /** Returns the value of the dialog(some). */
    public boolean isSome() { return f.get(Field.SOME, Boolean.class); }

    /** Sets the value of the dialog(some). */
    @NotNull public WidgetShowcase setSome(boolean some) {
        f.set(Field.SOME, some);
        return (WidgetShowcase) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<WidgetShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<WidgetShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<WidgetShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(WidgetShowcase.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        TEXT_WIDGETS("textWidgets"),
        TEXT("text"),
        TEXTFIELD("textfield"),
        MAIL_FILED("mailFiled"),
        ICON_FIELD("iconField"),
        NO_PASTE_TEXTFIELD("noPasteTextfield"),
        PASSWORD("password"),
        TEXTAREA("textarea"),
        RICH_TEXT("richText"),
        COLORPICKER("colorpicker"),
        BREADCRUMB("breadcrumb"),
        SUGGEST("suggest"),
        SUGGEST_INFO("suggestInfo"),
        $M3("$M3"),
        $M4("$M4"),
        $M5("$M5"),
        OPTION("option"),
        COMBO("combo"),
        TAGS_COMBO("tagsCombo"),
        CHECK_BOX_GROUP("checkBoxGroup"),
        RADIOGROUP("radiogroup"),
        LISTBOX("listbox"),
        PICK("pick"),
        TAGS("tags"),
        INT_TAGS("intTags"),
        REAL_TAGS("realTags"),
        DECIMAL_TAGS("decimalTags"),
        MAIL_TAGS("mailTags"),
        GALLERY("gallery"),
        TREE_VIEW("treeView"),
        $V6("$V6"),
        PROGRESS_NUMBER("progressNumber"),
        FROM_NUM("fromNum"),
        PROGRESS_BAR("progressBar"),
        $B7("$B7"),
        BUTTONS("buttons"),
        BUTTON("button"),
        SYN_BTN("synBtn"),
        LONG_BTN("longBtn"),
        BTN("btn"),
        $L8("$L8"),
        $L9("$L9"),
        $H10("$H10"),
        WORDS("words"),
        DOWNLOAD("download"),
        TOGGLEBUTTON("togglebutton"),
        CHECKBOX("checkbox"),
        DATE("date"),
        $V11("$V11"),
        DATEBOX("datebox"),
        DATEBOX2("datebox2"),
        DATETIMEBOX("datetimebox"),
        TIMEPICKER("timepicker"),
        DATEPICKER("datepicker"),
        SOME("some"),
        $T12("$T12"),
        $C13("$C13"),
        $H14("$H14"),
        $T15("$T15"),
        $T16("$T16"),
        $P17("$P17"),
        $H18("$H18"),
        $L19("$L19"),
        $T20("$T20"),
        $T21("$T21"),
        $T22("$T22"),
        $H23("$H23"),
        $T24("$T24"),
        $T25("$T25"),
        $V26("$V26"),
        $T27("$T27"),
        $T28("$T28"),
        $T29("$T29"),
        $T30("$T30"),
        $T31("$T31"),
        $F32("$F32"),
        $B33("$B33"),
        $B34("$B34"),
        $B35("$B35"),
        $F36("$F36"),
        $B37("$B37");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
