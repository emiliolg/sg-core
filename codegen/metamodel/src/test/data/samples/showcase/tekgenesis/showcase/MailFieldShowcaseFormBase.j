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
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.showcase.MailFieldShowcaseForm.MailTableRow;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.showcase.MailFieldShowcaseForm.UiTableRow;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: MailFieldShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class MailFieldShowcaseFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.MailFieldShowcaseForm");
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

    /** Returns the value of the mail_field(mail). */
    @NotNull public String getMail() { return f.get(Field.MAIL, String.class); }

    /** Sets the value of the mail_field(mail). */
    @NotNull public MailFieldShowcaseForm setMail(@NotNull String mail) {
        f.set(Field.MAIL, mail);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(mail). */
    public void setMailOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL, items); }

    /** Returns the value of the mail_field(blurChange). */
    @Nullable public String getBlurChange() { return f.get(Field.BLUR_CHANGE, String.class); }

    /** Sets the value of the mail_field(blurChange). */
    @NotNull public MailFieldShowcaseForm setBlurChange(@Nullable String blurChange) {
        f.set(Field.BLUR_CHANGE, blurChange);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(blurChange). */
    public void setBlurChangeOptions(@NotNull Iterable<String> items) { f.opts(Field.BLUR_CHANGE, items); }

    /** Invoked when mail_field(mailChange) value ui changes */
    @NotNull public abstract Action changedEmail();

    /** Returns the value of the mail_field(mailChange). */
    @NotNull public String getMailChange() { return f.get(Field.MAIL_CHANGE, String.class); }

    /** Sets the value of the mail_field(mailChange). */
    @NotNull public MailFieldShowcaseForm setMailChange(@NotNull String mailChange) {
        f.set(Field.MAIL_CHANGE, mailChange);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(mailChange). */
    public void setMailChangeOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_CHANGE, items); }

    /** Returns the value of the mail_field(mailSuggest). */
    @NotNull public String getMailSuggest() { return f.get(Field.MAIL_SUGGEST, String.class); }

    /** Sets the value of the mail_field(mailSuggest). */
    @NotNull public MailFieldShowcaseForm setMailSuggest(@NotNull String mailSuggest) {
        f.set(Field.MAIL_SUGGEST, mailSuggest);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(mailSuggest). */
    public void setMailSuggestOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_SUGGEST, items); }

    /** Returns the value of the mail_field(mailSuggestSync). */
    @NotNull public String getMailSuggestSync() { return f.get(Field.MAIL_SUGGEST_SYNC, String.class); }

    /** Sets the value of the mail_field(mailSuggestSync). */
    @NotNull public MailFieldShowcaseForm setMailSuggestSync(@NotNull String mailSuggestSync) {
        f.set(Field.MAIL_SUGGEST_SYNC, mailSuggestSync);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(mailSuggestSync). */
    public void setMailSuggestSyncOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_SUGGEST_SYNC, items); }

    /** Returns the value of the mail_field(mailOptions). */
    @NotNull public String getMailOptions() { return f.get(Field.MAIL_OPTIONS, String.class); }

    /** Sets the value of the mail_field(mailOptions). */
    @NotNull public MailFieldShowcaseForm setMailOptions(@NotNull String mailOptions) {
        f.set(Field.MAIL_OPTIONS, mailOptions);
        return (MailFieldShowcaseForm) this;
    }

    /** Sets the options of the mail_field(mailOptions). */
    public void setMailOptionsOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_OPTIONS, items); }

    /** Returns a {@link FormTable<MailTableRow>} instance to handle MailTable manipulation */
    @NotNull public final FormTable<MailTableRow> getMailTable() { return table(Field.MAIL_TABLE, MailTableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the mail_field(tableChange). */
    public void setTableChangeOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_CHANGE, items); }

    /** Sets the options of the mail_field(tableSuggest). */
    public void setTableSuggestOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_SUGGEST, items); }

    /** Sets the options of the mail_field(tableSuggestSync). */
    public void setTableSuggestSyncOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_SUGGEST_SYNC, items); }

    /** Returns a {@link FormTable<UiTableRow>} instance to handle UiTable manipulation */
    @NotNull public final FormTable<UiTableRow> getUiTable() { return table(Field.UI_TABLE, UiTableRow.class); }

    /** Sets the options of the mail_field(mailUi). */
    public void setMailUiOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_UI, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MailFieldShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MailFieldShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<MailFieldShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(MailFieldShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class MailTableRowBase
        implements FormRowInstance<MailTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when mail_field(tableChange) value changes */
        @NotNull public abstract Action tableMailChange();

        /** Returns the value of the mail_field(tableChange). */
        @NotNull public String getTableChange() { return f.get(Field.TABLE_CHANGE, String.class); }

        /** Sets the value of the mail_field(tableChange). */
        @NotNull public MailTableRow setTableChange(@NotNull String tableChange) {
            f.set(Field.TABLE_CHANGE, tableChange);
            return (MailTableRow) this;
        }

        /** Sets the options of the mail_field(tableChange). */
        public void setTableChangeOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_CHANGE, items); }

        /** Returns the value of the mail_field(tableSuggest). */
        @NotNull public String getTableSuggest() { return f.get(Field.TABLE_SUGGEST, String.class); }

        /** Sets the value of the mail_field(tableSuggest). */
        @NotNull public MailTableRow setTableSuggest(@NotNull String tableSuggest) {
            f.set(Field.TABLE_SUGGEST, tableSuggest);
            return (MailTableRow) this;
        }

        /** Sets the options of the mail_field(tableSuggest). */
        public void setTableSuggestOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_SUGGEST, items); }

        /** Returns the value of the mail_field(tableSuggestSync). */
        @NotNull public String getTableSuggestSync() { return f.get(Field.TABLE_SUGGEST_SYNC, String.class); }

        /** Sets the value of the mail_field(tableSuggestSync). */
        @NotNull public MailTableRow setTableSuggestSync(@NotNull String tableSuggestSync) {
            f.set(Field.TABLE_SUGGEST_SYNC, tableSuggestSync);
            return (MailTableRow) this;
        }

        /** Sets the options of the mail_field(tableSuggestSync). */
        public void setTableSuggestSyncOptions(@NotNull Iterable<String> items) { f.opts(Field.TABLE_SUGGEST_SYNC, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<MailTableRow> table() { return getMailTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((MailTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class UiTableRowBase
        implements FormRowInstance<UiTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(name) value ui changes */
        @NotNull public abstract Action changedNameUpdateNick();

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public UiTableRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (UiTableRow) this;
        }

        /** Returns the value of the text_field(nick). */
        @NotNull public String getNick() { return f.get(Field.NICK, String.class); }

        /** Sets the value of the text_field(nick). */
        @NotNull public UiTableRow setNick(@NotNull String nick) {
            f.set(Field.NICK, nick);
            return (UiTableRow) this;
        }

        /** Returns the value of the mail_field(mailUi). */
        @NotNull public String getMailUi() { return f.get(Field.MAIL_UI, String.class); }

        /** Sets the value of the mail_field(mailUi). */
        @NotNull public UiTableRow setMailUi(@NotNull String mailUi) {
            f.set(Field.MAIL_UI, mailUi);
            return (UiTableRow) this;
        }

        /** Sets the options of the mail_field(mailUi). */
        public void setMailUiOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_UI, items); }

        /** Returns the value of the text_field(number). */
        @NotNull public String getNumber() { return f.get(Field.NUMBER, String.class); }

        /** Sets the value of the text_field(number). */
        @NotNull public UiTableRow setNumber(@NotNull String number) {
            f.set(Field.NUMBER, number);
            return (UiTableRow) this;
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
        @NotNull public final FormTable<UiTableRow> table() { return getUiTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((UiTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        MAIL("mail"),
        BLUR_CHANGE("blurChange"),
        MAIL_CHANGE("mailChange"),
        MAIL_SUGGEST("mailSuggest"),
        MAIL_SUGGEST_SYNC("mailSuggestSync"),
        MAIL_OPTIONS("mailOptions"),
        MAIL_TABLE("mailTable"),
        TABLE_CHANGE("tableChange"),
        TABLE_SUGGEST("tableSuggest"),
        TABLE_SUGGEST_SYNC("tableSuggestSync"),
        UI_TABLE("uiTable"),
        NAME("name"),
        NICK("nick"),
        MAIL_UI("mailUi"),
        NUMBER("number"),
        SUBMIT("submit");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
