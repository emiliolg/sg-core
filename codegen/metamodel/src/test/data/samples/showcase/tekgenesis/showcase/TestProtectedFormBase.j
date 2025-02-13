package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.TestProtectedForm.InnersRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TestProtectedForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TestProtectedFormBase
    extends FormInstance<TestProtected>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final TestProtected testProtected = TestProtected.create();
        copyTo(testProtected);
        createOrUpdateInners(testProtected);
        testProtected.insert();
        setId(testProtected.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final TestProtected testProtected = find();
        copyTo(testProtected);
        createOrUpdateInners(testProtected);
        testProtected.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public TestProtected find() {
        final TestProtected value = TestProtected.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public TestProtected populate() {
        final TestProtected testProtected = find();

        setName(testProtected.getName());

        getInners().populate(testProtected.getInners(), InnersRow::populate);

        populateProtectedFields(testProtected);

        return testProtected;
    }

    /** Invoked from {@link #populate(TestProtected) method} to handle protected fields. */
    public abstract void populateProtectedFields(@NotNull TestProtected testProtected);

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

    /** Copies field values to given testProtected instance. */
    public void copyTo(@NotNull TestProtected testProtected) {
        testProtected.setName(getName());

        copyToProtectedFields(testProtected);
    }

    /** Invoked from {@link #copyTo(TestProtected) method} to handle protected fields. */
    public abstract void copyToProtectedFields(@NotNull TestProtected testProtected);

    /** Updates external references to inners. */
    public void createOrUpdateInners(@NotNull TestProtected testProtected) {
        testProtected.getInners().merge(getInners(), (innerTestProtected, row) -> row.copyTo(innerTestProtected));
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public TestProtectedForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (TestProtectedForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public TestProtectedForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (TestProtectedForm) this;
    }

    /** Returns the value of the text_field(bla). */
    @NotNull public String getBla() { return f.get(Field.BLA, String.class); }

    /** Sets the value of the text_field(bla). */
    @NotNull public TestProtectedForm setBla(@NotNull String bla) {
        f.set(Field.BLA, bla);
        return (TestProtectedForm) this;
    }

    /** Returns a {@link FormTable<InnersRow>} instance to handle Inners manipulation */
    @NotNull public final FormTable<InnersRow> getInners() { return table(Field.INNERS, InnersRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TestProtectedForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TestProtectedForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TestProtectedForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TestProtectedForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class InnersRowBase
        implements FormRowInstance<InnersRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(desc). */
        @NotNull public String getDesc() { return f.get(Field.DESC, String.class); }

        /** Sets the value of the text_field(desc). */
        @NotNull public InnersRow setDesc(@NotNull String desc) {
            f.set(Field.DESC, desc);
            return (InnersRow) this;
        }

        /** Returns the value of the text_field(length). */
        public int getLength() { return f.get(Field.LENGTH, Integer.class); }

        /** Sets the value of the text_field(length). */
        @NotNull public InnersRow setLength(int length) {
            f.set(Field.LENGTH, length);
            return (InnersRow) this;
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
        @NotNull public final FormTable<InnersRow> table() { return getInners(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((InnersRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given innerTestProtected instance. */
        public void populate(@NotNull InnerTestProtected innerTestProtected) {
            setDesc(innerTestProtected.getDesc());

            populateProtectedFields(innerTestProtected);
        }

        /** Invoked from {@link #populate(InnerTestProtected) method} to handle protected fields. */
        public abstract void populateProtectedFields(@NotNull InnerTestProtected innerTestProtected);

        /** Copies field values to given innerTestProtected instance. */
        public void copyTo(@NotNull InnerTestProtected innerTestProtected) {
            innerTestProtected.setDesc(getDesc());

            copyToProtectedFields(innerTestProtected);
        }

        /** Invoked from {@link #copyTo(InnerTestProtected) method} to handle protected fields. */
        public abstract void copyToProtectedFields(@NotNull InnerTestProtected innerTestProtected);

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        NAME("name"),
        BLA("bla"),
        INNERS("inners"),
        DESC("desc"),
        LENGTH("length"),
        $H2("$H2"),
        $B3("$B3"),
        $B4("$B4"),
        $F5("$F5"),
        $B6("$B6"),
        $B7("$B7"),
        $B8("$B8");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
