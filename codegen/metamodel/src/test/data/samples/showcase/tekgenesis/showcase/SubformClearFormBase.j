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
import tekgenesis.showcase.SubformClearForm.SomeSectionRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SubformClearForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SubformClearFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SubformClearForm");
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

    /** Returns a {@link FormTable<SomeSectionRow>} instance to handle SomeSection manipulation */
    @NotNull public final FormTable<SomeSectionRow> getSomeSection() { return table(Field.SOME_SECTION, SomeSectionRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Invoked when button(clearAndAdd) is clicked */
    @NotNull public abstract Action clearAndAdd();

    /** Invoked when button(clear) is clicked */
    @NotNull public abstract Action clear();

    /** Invoked when button(justAdd) is clicked */
    @NotNull public abstract Action justAdd();

    /** Invoked when button(changeFirst) is clicked */
    @NotNull public abstract Action changeFirst();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SubformClearForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SubformClearForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SubformClearForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SubformClearForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SomeSectionRowBase
        implements FormRowInstance<SomeSectionRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Create and set a new SomeNumberForm instance */
        @NotNull public SomeNumberForm createMysub() { return f.init(Field.MYSUB, SomeNumberForm.class); }

        /** Create and populates set a new SomeNumberForm instance with a pk */
        @NotNull public SomeNumberForm createMysub(@NotNull String key) { return f.init(Field.MYSUB, SomeNumberForm.class, key); }

        /** 
         * Get the SomeNumberForm if defined, or null otherwise.
         * @see #createMysub
         */
        @Nullable public SomeNumberForm getMysub() { return f.subform(Field.MYSUB, SomeNumberForm.class); }

        /** Returns the value of the display(myText). */
        @NotNull public String getMyText() { return f.get(Field.MY_TEXT, String.class); }

        /** Sets the value of the display(myText). */
        @NotNull public SomeSectionRow setMyText(@NotNull String myText) {
            f.set(Field.MY_TEXT, myText);
            return (SomeSectionRow) this;
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
        @NotNull public final FormTable<SomeSectionRow> table() { return getSomeSection(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SomeSectionRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SOME_SECTION("someSection"),
        MYSUB("mysub"),
        MY_TEXT("myText"),
        CLEAR_AND_ADD("clearAndAdd"),
        CLEAR("clear"),
        JUST_ADD("justAdd"),
        CHANGE_FIRST("changeFirst");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
