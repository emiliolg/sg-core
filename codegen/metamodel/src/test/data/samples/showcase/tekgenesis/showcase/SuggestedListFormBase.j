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
import tekgenesis.common.Predefined;
import tekgenesis.showcase.SuggestedListForm.SuggestedPersonsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SuggestedListForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SuggestedListFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SuggestedListForm");
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

    /** Returns the value of the suggest_box(persons). */
    @NotNull public SuggestedPerson getPersons() {
        return Predefined.ensureNotNull(SuggestedPerson.find(getPersonsKey()), "'persons' not found");
    }

    /** Returns the key value of the suggest_box(persons). */
    @NotNull public String getPersonsKey() { return f.get(Field.PERSONS, String.class); }

    /** Sets the value of the suggest_box(persons). */
    @NotNull public SuggestedListForm setPersons(@NotNull SuggestedPerson persons) {
        f.set(Field.PERSONS, persons);
        return (SuggestedListForm) this;
    }

    /** Returns a {@link FormTable<SuggestedPersonsRow>} instance to handle SuggestedPersons manipulation */
    @NotNull public final FormTable<SuggestedPersonsRow> getSuggestedPersons() {
        return table(Field.SUGGESTED_PERSONS, SuggestedPersonsRow.class);
    }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SuggestedListForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SuggestedListForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SuggestedListForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SuggestedListForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SuggestedPersonsRowBase
        implements FormRowInstance<SuggestedPersonsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(person). */
        @NotNull public SuggestedPerson getPerson() {
            return Predefined.ensureNotNull(SuggestedPerson.find(getPersonKey()), "'person' not found");
        }

        /** Returns the key value of the display(person). */
        @NotNull public String getPersonKey() { return f.get(Field.PERSON, String.class); }

        /** Sets the value of the display(person). */
        @NotNull public SuggestedPersonsRow setPerson(@NotNull SuggestedPerson person) {
            f.set(Field.PERSON, person);
            return (SuggestedPersonsRow) this;
        }

        /** Returns the value of the suggest_box(suggest). */
        @NotNull public SuggestedPerson getSuggest() {
            return Predefined.ensureNotNull(SuggestedPerson.find(getSuggestKey()), "'suggest' not found");
        }

        /** Returns the key value of the suggest_box(suggest). */
        @NotNull public String getSuggestKey() { return f.get(Field.SUGGEST, String.class); }

        /** Sets the value of the suggest_box(suggest). */
        @NotNull public SuggestedPersonsRow setSuggest(@NotNull SuggestedPerson suggest) {
            f.set(Field.SUGGEST, suggest);
            return (SuggestedPersonsRow) this;
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
        @NotNull public final FormTable<SuggestedPersonsRow> table() { return getSuggestedPersons(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SuggestedPersonsRow) this);
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
        PERSONS("persons"),
        SUGGESTED_PERSONS("suggestedPersons"),
        PERSON("person"),
        SUGGEST("suggest");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
