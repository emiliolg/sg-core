package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import tekgenesis.showcase.MakeForm.ModelsRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: MakeForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class MakeFormBase
    extends FormInstance<Make>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Make make = Make.create();
        copyTo(make);
        createOrUpdateModels(make);
        make.insert();
        setId(make.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Make make = find();
        copyTo(make);
        createOrUpdateModels(make);
        make.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Make find() {
        final Make value = Make.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Make populate() {
        final Make make = find();

        setName(make.getName())
        	.setOrigin(make.getOrigin());

        getModels().populate(make.getModels(), ModelsRow::populate);

        return make;
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

    /** Copies field values to given make instance. */
    public void copyTo(@NotNull Make make) {
        make.setName(getName())
        	.setOrigin(getOrigin());
    }

    /** Updates external references to models. */
    public void createOrUpdateModels(@NotNull Make make) {
        make.getModels().merge(getModels(), (model, row) -> row.copyTo(model));
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public MakeForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (MakeForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public MakeForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (MakeForm) this;
    }

    /** Returns the value of the combo_box(origin). */
    @NotNull public Country getOrigin() { return Country.valueOf(f.get(Field.ORIGIN, String.class)); }

    /** Sets the value of the combo_box(origin). */
    @NotNull public MakeForm setOrigin(@NotNull Country origin) {
        f.set(Field.ORIGIN, origin);
        return (MakeForm) this;
    }

    /** Sets the options of the combo_box(origin). */
    public void setOriginOptions(@NotNull Iterable<Country> items) { f.opts(Field.ORIGIN, items); }

    /** Sets the options of the combo_box(origin) with the given KeyMap. */
    public void setOriginOptions(@NotNull KeyMap items) { f.opts(Field.ORIGIN, items); }

    /** Returns a {@link FormTable<ModelsRow>} instance to handle Models manipulation */
    @NotNull public final FormTable<ModelsRow> getModels() { return table(Field.MODELS, ModelsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MakeForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MakeForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<MakeForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(MakeForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ModelsRowBase
        implements FormRowInstance<ModelsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(model). */
        @NotNull public String getModel() { return f.get(Field.MODEL, String.class); }

        /** Sets the value of the text_field(model). */
        @NotNull public ModelsRow setModel(@NotNull String model) {
            f.set(Field.MODEL, model);
            return (ModelsRow) this;
        }

        /** Returns the value of the date_box(released). */
        @NotNull public DateOnly getReleased() {
            return DateOnly.fromMilliseconds(f.get(Field.RELEASED, Long.class));
        }

        /** Sets the value of the date_box(released). */
        @NotNull public ModelsRow setReleased(@NotNull DateOnly released) {
            f.set(Field.RELEASED, released);
            return (ModelsRow) this;
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
        @NotNull public final FormTable<ModelsRow> table() { return getModels(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ModelsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given model instance. */
        public void populate(@NotNull Model model) {
            setModel(model.getModel())
            	.setReleased(model.getReleased());
        }

        /** Copies field values to given model instance. */
        public void copyTo(@NotNull Model model) {
            model.setModel(getModel())
            	.setReleased(getReleased());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        NAME("name"),
        ORIGIN("origin"),
        MODELS("models"),
        MODEL("model"),
        RELEASED("released"),
        $H3("$H3"),
        $B4("$B4"),
        $B5("$B5"),
        $F6("$F6"),
        $B7("$B7"),
        $B8("$B8"),
        $B9("$B9");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
