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
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.DialogsForm.SectionRow;
import tekgenesis.showcase.DialogsForm.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DialogsForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DialogsFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DialogsForm");
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

    /** Invoked when button(open) is clicked */
    @NotNull public abstract Action openDialog();

    /** Returns the value of the dialog(diag). */
    public boolean isDiag() { return f.get(Field.DIAG, Boolean.class); }

    /** Sets the value of the dialog(diag). */
    @NotNull public DialogsForm setDiag(boolean diag) {
        f.set(Field.DIAG, diag);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(text1). */
    @Nullable public String getText1() { return f.get(Field.TEXT1, String.class); }

    /** Sets the value of the text_field(text1). */
    @NotNull public DialogsForm setText1(@Nullable String text1) {
        f.set(Field.TEXT1, text1);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(text2). */
    @Nullable public String getText2() { return f.get(Field.TEXT2, String.class); }

    /** Sets the value of the text_field(text2). */
    @NotNull public DialogsForm setText2(@Nullable String text2) {
        f.set(Field.TEXT2, text2);
        return (DialogsForm) this;
    }

    /** Invoked when button(foc) is clicked */
    @NotNull public abstract Action focusText();

    /** Invoked when text_field(foc2) value changes */
    @NotNull public abstract Action putText4();

    /** Returns the value of the text_field(foc2). */
    @NotNull public String getFoc2() { return f.get(Field.FOC2, String.class); }

    /** Sets the value of the text_field(foc2). */
    @NotNull public DialogsForm setFoc2(@NotNull String foc2) {
        f.set(Field.FOC2, foc2);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(text3). */
    @NotNull public String getText3() { return f.get(Field.TEXT3, String.class); }

    /** Sets the value of the text_field(text3). */
    @NotNull public DialogsForm setText3(@NotNull String text3) {
        f.set(Field.TEXT3, text3);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(text4). */
    @NotNull public String getText4() { return f.get(Field.TEXT4, String.class); }

    /** Sets the value of the text_field(text4). */
    @NotNull public DialogsForm setText4(@NotNull String text4) {
        f.set(Field.TEXT4, text4);
        return (DialogsForm) this;
    }

    /** Returns the value of the dialog(some). */
    public boolean isSome() { return f.get(Field.SOME, Boolean.class); }

    /** Sets the value of the dialog(some). */
    @NotNull public DialogsForm setSome(boolean some) {
        f.set(Field.SOME, some);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(text). */
    @Nullable public String getText() { return f.get(Field.TEXT, String.class); }

    /** Sets the value of the text_field(text). */
    @NotNull public DialogsForm setText(@Nullable String text) {
        f.set(Field.TEXT, text);
        return (DialogsForm) this;
    }

    /** Invoked when button(confirm) is clicked */
    @NotNull public abstract Action confirm();

    /** Invoked when button($B1) is clicked */
    @NotNull public abstract Action showOtherDialog();

    /** Invoked when button($B2) is clicked */
    @NotNull public abstract Action hideDialog();

    /** 
     * Invoked when dialog(other) value ui changes
     * Invoked when button($B8) is clicked
     */
    @NotNull public abstract Action goBackToPrevious();

    /** Returns the value of the dialog(other). */
    public boolean isOther() { return f.get(Field.OTHER, Boolean.class); }

    /** Sets the value of the dialog(other). */
    @NotNull public DialogsForm setOther(boolean other) {
        f.set(Field.OTHER, other);
        return (DialogsForm) this;
    }

    /** Returns the value of the text_field(otherText). */
    @Nullable public String getOtherText() { return f.get(Field.OTHER_TEXT, String.class); }

    /** Sets the value of the text_field(otherText). */
    @NotNull public DialogsForm setOtherText(@Nullable String otherText) {
        f.set(Field.OTHER_TEXT, otherText);
        return (DialogsForm) this;
    }

    /** Invoked when button($B4) is clicked */
    @NotNull public abstract Action openTableZero();

    /** Invoked when button($B5) is clicked */
    @NotNull public abstract Action openTableOne();

    /** Invoked when button($B6) is clicked */
    @NotNull public abstract Action openSectionZero();

    /** Invoked when button($B7) is clicked */
    @NotNull public abstract Action openSectionOne();

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<SectionRow>} instance to handle Section manipulation */
    @NotNull public final FormTable<SectionRow> getSection() { return table(Field.SECTION, SectionRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DialogsForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DialogsForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DialogsForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DialogsForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the dialog(tableDialog). */
        public boolean isTableDialog() { return f.get(Field.TABLE_DIALOG, Boolean.class); }

        /** Sets the value of the dialog(tableDialog). */
        @NotNull public TableRow setTableDialog(boolean tableDialog) {
            f.set(Field.TABLE_DIALOG, tableDialog);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(tableText). */
        @Nullable public String getTableText() { return f.get(Field.TABLE_TEXT, String.class); }

        /** Sets the value of the text_field(tableText). */
        @NotNull public TableRow setTableText(@Nullable String tableText) {
            f.set(Field.TABLE_TEXT, tableText);
            return (TableRow) this;
        }

        /** Invoked when button($B9) is clicked */
        @NotNull public abstract Action goBackToOther();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<TableRow> table() { return getTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((TableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class SectionRowBase
        implements FormRowInstance<SectionRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the dialog(sectionDialog). */
        public boolean isSectionDialog() { return f.get(Field.SECTION_DIALOG, Boolean.class); }

        /** Sets the value of the dialog(sectionDialog). */
        @NotNull public SectionRow setSectionDialog(boolean sectionDialog) {
            f.set(Field.SECTION_DIALOG, sectionDialog);
            return (SectionRow) this;
        }

        /** Returns the value of the text_field(sectionText). */
        @Nullable public String getSectionText() { return f.get(Field.SECTION_TEXT, String.class); }

        /** Sets the value of the text_field(sectionText). */
        @NotNull public SectionRow setSectionText(@Nullable String sectionText) {
            f.set(Field.SECTION_TEXT, sectionText);
            return (SectionRow) this;
        }

        /** Invoked when button($B10) is clicked */
        @NotNull public abstract Action goBackToOther();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<SectionRow> table() { return getSection(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SectionRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        OPEN("open"),
        DIAG("diag"),
        TEXT1("text1"),
        TEXT2("text2"),
        FOC("foc"),
        FOC2("foc2"),
        TEXT3("text3"),
        TEXT4("text4"),
        SOME("some"),
        TEXT("text"),
        $F0("$F0"),
        CONFIRM("confirm"),
        $B1("$B1"),
        $B2("$B2"),
        OTHER("other"),
        OTHER_TEXT("otherText"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6"),
        $B7("$B7"),
        $B8("$B8"),
        TABLE("table"),
        TABLE_DIALOG("tableDialog"),
        TABLE_TEXT("tableText"),
        $B9("$B9"),
        SECTION("section"),
        SECTION_DIALOG("sectionDialog"),
        SECTION_TEXT("sectionText"),
        $B10("$B10");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
