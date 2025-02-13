package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: LinkShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class LinkShowcaseFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.LinkShowcaseForm");
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

    /** Returns the value of the display(displayLink). */
    @NotNull public String getDisplayLink() { return f.get(Field.DISPLAY_LINK, String.class); }

    /** Sets the value of the display(displayLink). */
    @NotNull public LinkShowcaseForm setDisplayLink(@NotNull String displayLink) {
        f.set(Field.DISPLAY_LINK, displayLink);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the display(displayWithBlank). */
    @NotNull public String getDisplayWithBlank() { return f.get(Field.DISPLAY_WITH_BLANK, String.class); }

    /** Sets the value of the display(displayWithBlank). */
    @NotNull public LinkShowcaseForm setDisplayWithBlank(@NotNull String displayWithBlank) {
        f.set(Field.DISPLAY_WITH_BLANK, displayWithBlank);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the display(displayWithLinkForm). */
    @NotNull public String getDisplayWithLinkForm() { return f.get(Field.DISPLAY_WITH_LINK_FORM, String.class); }

    /** Sets the value of the display(displayWithLinkForm). */
    @NotNull public LinkShowcaseForm setDisplayWithLinkForm(@NotNull String displayWithLinkForm) {
        f.set(Field.DISPLAY_WITH_LINK_FORM, displayWithLinkForm);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the display(displayWithLinkFormPk). */
    @NotNull public String getDisplayWithLinkFormPk() { return f.get(Field.DISPLAY_WITH_LINK_FORM_PK, String.class); }

    /** Sets the value of the display(displayWithLinkFormPk). */
    @NotNull public LinkShowcaseForm setDisplayWithLinkFormPk(@NotNull String displayWithLinkFormPk) {
        f.set(Field.DISPLAY_WITH_LINK_FORM_PK, displayWithLinkFormPk);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the suggest_box(sugguestLink). */
    @NotNull public SimpleEntity getSugguestLink() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSugguestLinkKey()), "'sugguestLink' not found");
    }

    /** Returns the key value of the suggest_box(sugguestLink). */
    @NotNull public String getSugguestLinkKey() { return f.get(Field.SUGGUEST_LINK, String.class); }

    /** Sets the value of the suggest_box(sugguestLink). */
    @NotNull public LinkShowcaseForm setSugguestLink(@NotNull SimpleEntity sugguestLink) {
        f.set(Field.SUGGUEST_LINK, sugguestLink);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the suggest_box(sugguestWithBlank). */
    @NotNull public SimpleEntity getSugguestWithBlank() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSugguestWithBlankKey()), "'sugguestWithBlank' not found");
    }

    /** Returns the key value of the suggest_box(sugguestWithBlank). */
    @NotNull public String getSugguestWithBlankKey() { return f.get(Field.SUGGUEST_WITH_BLANK, String.class); }

    /** Sets the value of the suggest_box(sugguestWithBlank). */
    @NotNull public LinkShowcaseForm setSugguestWithBlank(@NotNull SimpleEntity sugguestWithBlank) {
        f.set(Field.SUGGUEST_WITH_BLANK, sugguestWithBlank);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the suggest_box(sugguestWithLinkForm). */
    @NotNull public SimpleEntity getSugguestWithLinkForm() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSugguestWithLinkFormKey()), "'sugguestWithLinkForm' not found");
    }

    /** Returns the key value of the suggest_box(sugguestWithLinkForm). */
    @NotNull public String getSugguestWithLinkFormKey() { return f.get(Field.SUGGUEST_WITH_LINK_FORM, String.class); }

    /** Sets the value of the suggest_box(sugguestWithLinkForm). */
    @NotNull public LinkShowcaseForm setSugguestWithLinkForm(@NotNull SimpleEntity sugguestWithLinkForm) {
        f.set(Field.SUGGUEST_WITH_LINK_FORM, sugguestWithLinkForm);
        return (LinkShowcaseForm) this;
    }

    /** Returns the value of the suggest_box(sugguestWithLinkFormPk). */
    @NotNull public SimpleEntity getSugguestWithLinkFormPk() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSugguestWithLinkFormPkKey()), "'sugguestWithLinkFormPk' not found");
    }

    /** Returns the key value of the suggest_box(sugguestWithLinkFormPk). */
    @NotNull public String getSugguestWithLinkFormPkKey() {
        return f.get(Field.SUGGUEST_WITH_LINK_FORM_PK, String.class);
    }

    /** Sets the value of the suggest_box(sugguestWithLinkFormPk). */
    @NotNull public LinkShowcaseForm setSugguestWithLinkFormPk(@NotNull SimpleEntity sugguestWithLinkFormPk) {
        f.set(Field.SUGGUEST_WITH_LINK_FORM_PK, sugguestWithLinkFormPk);
        return (LinkShowcaseForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<LinkShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<LinkShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<LinkShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(LinkShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        LINK_OPTION("linkOption"),
        LABEL_LINK("labelLink"),
        LABEL_WITH_BLANK("labelWithBlank"),
        LABEL_WITH_LINK_FORM("labelWithLinkForm"),
        LABEL_WITH_LINK_FORM_PK("labelWithLinkFormPk"),
        DISPLAY_OPTION("displayOption"),
        DISPLAY_LINK("displayLink"),
        DISPLAY_WITH_BLANK("displayWithBlank"),
        DISPLAY_WITH_LINK_FORM("displayWithLinkForm"),
        DISPLAY_WITH_LINK_FORM_PK("displayWithLinkFormPk"),
        SUGGUEST_OPTION("sugguestOption"),
        SUGGUEST_LINK("sugguestLink"),
        SUGGUEST_WITH_BLANK("sugguestWithBlank"),
        SUGGUEST_WITH_LINK_FORM("sugguestWithLinkForm"),
        SUGGUEST_WITH_LINK_FORM_PK("sugguestWithLinkFormPk");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
