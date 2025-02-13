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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.Predefined;
import tekgenesis.showcase.TextShowcaseForm.PropRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TextShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TextShowcaseFormBase
    extends FormInstance<TextShowcase>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final TextShowcase textShowcase = TextShowcase.create(getIdKey());
        copyTo(textShowcase);
        createOrUpdateProp(textShowcase);
        textShowcase.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final TextShowcase textShowcase = find();
        copyTo(textShowcase);
        createOrUpdateProp(textShowcase);
        textShowcase.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public TextShowcase find() {
        final TextShowcase value = TextShowcase.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID_KEY, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getIdKey(); }

    /** Invoked when populating a form instance */
    @NotNull public TextShowcase populate() {
        final TextShowcase textShowcase = find();

        setTxt(textShowcase.getTxt())
        	.setDate(textShowcase.getDate())
        	.setBool(textShowcase.isBool())
        	.setOption(textShowcase.getOption())
        	.setEntity(textShowcase.getEntity());

        getProp().populate(textShowcase.getProp(), PropRow::populate);

        return textShowcase;
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

    /** Copies field values to given textShowcase instance. */
    public void copyTo(@NotNull TextShowcase textShowcase) {
        textShowcase.setTxt(getTxt())
        	.setDate(getDate())
        	.setBool(isBool())
        	.setOption(getOption())
        	.setEntity(getEntity());
    }

    /** Updates external references to prop. */
    public void createOrUpdateProp(@NotNull TextShowcase textShowcase) {
        textShowcase.getProp().merge(getProp(), (myProp, row) -> row.copyTo(myProp));
    }

    /** Returns the value of the text_field(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the text_field(idKey). */
    @NotNull public TextShowcaseForm setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (TextShowcaseForm) this;
    }

    /** Returns the value of the text_field(txt). */
    @NotNull public String getTxt() { return f.get(Field.TXT, String.class); }

    /** Sets the value of the text_field(txt). */
    @NotNull public TextShowcaseForm setTxt(@NotNull String txt) {
        f.set(Field.TXT, txt);
        return (TextShowcaseForm) this;
    }

    /** Returns the value of the date_box(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the date_box(date). */
    @NotNull public TextShowcaseForm setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (TextShowcaseForm) this;
    }

    /** Returns the value of the check_box(bool). */
    public boolean isBool() { return f.get(Field.BOOL, Boolean.class); }

    /** Sets the value of the check_box(bool). */
    @NotNull public TextShowcaseForm setBool(boolean bool) {
        f.set(Field.BOOL, bool);
        return (TextShowcaseForm) this;
    }

    /** Returns the value of the combo_box(option). */
    @NotNull public Options getOption() { return Options.valueOf(f.get(Field.OPTION, String.class)); }

    /** Sets the value of the combo_box(option). */
    @NotNull public TextShowcaseForm setOption(@NotNull Options option) {
        f.set(Field.OPTION, option);
        return (TextShowcaseForm) this;
    }

    /** Sets the options of the combo_box(option). */
    public void setOptionOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTION, items); }

    /** Sets the options of the combo_box(option) with the given KeyMap. */
    public void setOptionOptions(@NotNull KeyMap items) { f.opts(Field.OPTION, items); }

    /** Returns the value of the suggest_box(entity). */
    @NotNull public SimpleEntity getEntity() {
        return Predefined.ensureNotNull(SimpleEntity.find(getEntityKey()), "'entity' not found");
    }

    /** Returns the key value of the suggest_box(entity). */
    @NotNull public String getEntityKey() { return f.get(Field.ENTITY, String.class); }

    /** Sets the value of the suggest_box(entity). */
    @NotNull public TextShowcaseForm setEntity(@NotNull SimpleEntity entity) {
        f.set(Field.ENTITY, entity);
        return (TextShowcaseForm) this;
    }

    /** Returns a {@link FormTable<PropRow>} instance to handle Prop manipulation */
    @NotNull public final FormTable<PropRow> getProp() { return table(Field.PROP, PropRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(type). */
    public void setTypeOptions(@NotNull Iterable<PropertyType> items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the combo_box(type) with the given KeyMap. */
    public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TextShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TextShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TextShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TextShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PropRowBase
        implements FormRowInstance<PropRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the combo_box(type). */
        @NotNull public PropertyType getType() {
            return PropertyType.valueOf(f.get(Field.TYPE, String.class));
        }

        /** Sets the value of the combo_box(type). */
        @NotNull public PropRow setType(@NotNull PropertyType type) {
            f.set(Field.TYPE, type);
            return (PropRow) this;
        }

        /** Sets the options of the combo_box(type). */
        public void setTypeOptions(@NotNull Iterable<PropertyType> items) { f.opts(Field.TYPE, items); }

        /** Sets the options of the combo_box(type) with the given KeyMap. */
        public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

        /** Returns the value of the text_field(value). */
        @NotNull public String getValue() { return f.get(Field.VALUE, String.class); }

        /** Sets the value of the text_field(value). */
        @NotNull public PropRow setValue(@NotNull String value) {
            f.set(Field.VALUE, value);
            return (PropRow) this;
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
        @NotNull public final FormTable<PropRow> table() { return getProp(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PropRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given myProp instance. */
        public void populate(@NotNull MyProp myProp) {
            setType(myProp.getType())
            	.setValue(myProp.getValue());
        }

        /** Copies field values to given myProp instance. */
        public void copyTo(@NotNull MyProp myProp) {
            myProp.setType(getType())
            	.setValue(getValue());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID_KEY("idKey"),
        TXT("txt"),
        DATE("date"),
        BOOL("bool"),
        OPTION("option"),
        ENTITY("entity"),
        PROP("prop"),
        TYPE("type"),
        VALUE("value"),
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
