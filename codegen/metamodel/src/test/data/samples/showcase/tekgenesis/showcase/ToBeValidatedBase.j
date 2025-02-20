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
import tekgenesis.showcase.ToBeValidated.SectionRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ToBeValidated.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ToBeValidatedBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ToBeValidated");
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

    /** Returns the value of the text_field(textRequired). */
    @NotNull public String getTextRequired() { return f.get(Field.TEXT_REQUIRED, String.class); }

    /** Sets the value of the text_field(textRequired). */
    @NotNull public ToBeValidated setTextRequired(@NotNull String textRequired) {
        f.set(Field.TEXT_REQUIRED, textRequired);
        return (ToBeValidated) this;
    }

    /** Returns the value of the text_field(textOptional). */
    @Nullable public String getTextOptional() { return f.get(Field.TEXT_OPTIONAL, String.class); }

    /** Sets the value of the text_field(textOptional). */
    @NotNull public ToBeValidated setTextOptional(@Nullable String textOptional) {
        f.set(Field.TEXT_OPTIONAL, textOptional);
        return (ToBeValidated) this;
    }

    /** Create and set a new ToBeValidated1 instance */
    @NotNull public ToBeValidated1 createInner() { return f.init(Field.INNER, ToBeValidated1.class); }

    /** Create and populates set a new ToBeValidated1 instance with a pk */
    @NotNull public ToBeValidated1 createInner(@NotNull String key) { return f.init(Field.INNER, ToBeValidated1.class, key); }

    /** 
     * Get the ToBeValidated1 if defined, or null otherwise.
     * @see #createInner
     */
    @Nullable public ToBeValidated1 getInner() { return f.subform(Field.INNER, ToBeValidated1.class); }

    /** Returns a {@link FormTable<SectionRow>} instance to handle Section manipulation */
    @NotNull public final FormTable<SectionRow> getSection() { return table(Field.SECTION, SectionRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ToBeValidated> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ToBeValidated> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ToBeValidated> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ToBeValidated.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SectionRowBase
        implements FormRowInstance<SectionRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Create and set a new ToBeValidated1 instance */
        @NotNull public ToBeValidated1 createNested() { return f.init(Field.NESTED, ToBeValidated1.class); }

        /** Create and populates set a new ToBeValidated1 instance with a pk */
        @NotNull public ToBeValidated1 createNested(@NotNull String key) { return f.init(Field.NESTED, ToBeValidated1.class, key); }

        /** 
         * Get the ToBeValidated1 if defined, or null otherwise.
         * @see #createNested
         */
        @Nullable public ToBeValidated1 getNested() { return f.subform(Field.NESTED, ToBeValidated1.class); }

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
        TEXT_REQUIRED("textRequired"),
        TEXT_OPTIONAL("textOptional"),
        INNER("inner"),
        SECTION("section"),
        NESTED("nested");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
