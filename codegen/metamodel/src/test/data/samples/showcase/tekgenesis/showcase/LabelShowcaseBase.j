package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: LabelShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class LabelShowcaseBase
    extends FormInstance<Label>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Label label = Label.create();
        copyTo(label);
        label.insert();
        setId(label.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Label label = find();
        copyTo(label);
        label.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Label find() {
        final Label value = Label.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Label populate() {
        final Label label = find();

        setWithLabel1(label.getWithLabel1())
        	.setWithLabel2(label.getWithLabel2())
        	.setNoLabel1(label.getNoLabel1())
        	.setNoLabel2(label.getNoLabel2())
        	.setNoLabel3(label.getNoLabel3())
        	.setNoLabel4(label.getNoLabel4());

        return label;
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

    /** Copies field values to given label instance. */
    public void copyTo(@NotNull Label label) {
        label.setWithLabel1(getWithLabel1())
        	.setWithLabel2(getWithLabel2())
        	.setNoLabel1(getNoLabel1())
        	.setNoLabel2(getNoLabel2())
        	.setNoLabel3(getNoLabel3())
        	.setNoLabel4(getNoLabel4());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public LabelShowcase setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(withLabel1). */
    @NotNull public String getWithLabel1() { return f.get(Field.WITH_LABEL1, String.class); }

    /** Sets the value of the text_field(withLabel1). */
    @NotNull public LabelShowcase setWithLabel1(@NotNull String withLabel1) {
        f.set(Field.WITH_LABEL1, withLabel1);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(noLabel1). */
    @NotNull public String getNoLabel1() { return f.get(Field.NO_LABEL1, String.class); }

    /** Sets the value of the text_field(noLabel1). */
    @NotNull public LabelShowcase setNoLabel1(@NotNull String noLabel1) {
        f.set(Field.NO_LABEL1, noLabel1);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(noLabel2). */
    @NotNull public String getNoLabel2() { return f.get(Field.NO_LABEL2, String.class); }

    /** Sets the value of the text_field(noLabel2). */
    @NotNull public LabelShowcase setNoLabel2(@NotNull String noLabel2) {
        f.set(Field.NO_LABEL2, noLabel2);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(withLabel2). */
    @NotNull public String getWithLabel2() { return f.get(Field.WITH_LABEL2, String.class); }

    /** Sets the value of the text_field(withLabel2). */
    @NotNull public LabelShowcase setWithLabel2(@NotNull String withLabel2) {
        f.set(Field.WITH_LABEL2, withLabel2);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(noLabel3). */
    @NotNull public String getNoLabel3() { return f.get(Field.NO_LABEL3, String.class); }

    /** Sets the value of the text_field(noLabel3). */
    @NotNull public LabelShowcase setNoLabel3(@NotNull String noLabel3) {
        f.set(Field.NO_LABEL3, noLabel3);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(noLabel4). */
    @NotNull public String getNoLabel4() { return f.get(Field.NO_LABEL4, String.class); }

    /** Sets the value of the text_field(noLabel4). */
    @NotNull public LabelShowcase setNoLabel4(@NotNull String noLabel4) {
        f.set(Field.NO_LABEL4, noLabel4);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel1). */
    @NotNull public String getFormLabel1() { return f.get(Field.FORM_LABEL1, String.class); }

    /** Sets the value of the text_field(formLabel1). */
    @NotNull public LabelShowcase setFormLabel1(@NotNull String formLabel1) {
        f.set(Field.FORM_LABEL1, formLabel1);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel3). */
    @NotNull public String getFormLabel3() { return f.get(Field.FORM_LABEL3, String.class); }

    /** Sets the value of the text_field(formLabel3). */
    @NotNull public LabelShowcase setFormLabel3(@NotNull String formLabel3) {
        f.set(Field.FORM_LABEL3, formLabel3);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel5). */
    @NotNull public String getFormLabel5() { return f.get(Field.FORM_LABEL5, String.class); }

    /** Sets the value of the text_field(formLabel5). */
    @NotNull public LabelShowcase setFormLabel5(@NotNull String formLabel5) {
        f.set(Field.FORM_LABEL5, formLabel5);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel2). */
    @NotNull public String getFormLabel2() { return f.get(Field.FORM_LABEL2, String.class); }

    /** Sets the value of the text_field(formLabel2). */
    @NotNull public LabelShowcase setFormLabel2(@NotNull String formLabel2) {
        f.set(Field.FORM_LABEL2, formLabel2);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel4). */
    @NotNull public String getFormLabel4() { return f.get(Field.FORM_LABEL4, String.class); }

    /** Sets the value of the text_field(formLabel4). */
    @NotNull public LabelShowcase setFormLabel4(@NotNull String formLabel4) {
        f.set(Field.FORM_LABEL4, formLabel4);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel6). */
    @NotNull public String getFormLabel6() { return f.get(Field.FORM_LABEL6, String.class); }

    /** Sets the value of the text_field(formLabel6). */
    @NotNull public LabelShowcase setFormLabel6(@NotNull String formLabel6) {
        f.set(Field.FORM_LABEL6, formLabel6);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel12). */
    @NotNull public String getFormLabel12() { return f.get(Field.FORM_LABEL12, String.class); }

    /** Sets the value of the text_field(formLabel12). */
    @NotNull public LabelShowcase setFormLabel12(@NotNull String formLabel12) {
        f.set(Field.FORM_LABEL12, formLabel12);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel32). */
    @NotNull public String getFormLabel32() { return f.get(Field.FORM_LABEL32, String.class); }

    /** Sets the value of the text_field(formLabel32). */
    @NotNull public LabelShowcase setFormLabel32(@NotNull String formLabel32) {
        f.set(Field.FORM_LABEL32, formLabel32);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel52). */
    @NotNull public String getFormLabel52() { return f.get(Field.FORM_LABEL52, String.class); }

    /** Sets the value of the text_field(formLabel52). */
    @NotNull public LabelShowcase setFormLabel52(@NotNull String formLabel52) {
        f.set(Field.FORM_LABEL52, formLabel52);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel22). */
    @NotNull public String getFormLabel22() { return f.get(Field.FORM_LABEL22, String.class); }

    /** Sets the value of the text_field(formLabel22). */
    @NotNull public LabelShowcase setFormLabel22(@NotNull String formLabel22) {
        f.set(Field.FORM_LABEL22, formLabel22);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel42). */
    @NotNull public String getFormLabel42() { return f.get(Field.FORM_LABEL42, String.class); }

    /** Sets the value of the text_field(formLabel42). */
    @NotNull public LabelShowcase setFormLabel42(@NotNull String formLabel42) {
        f.set(Field.FORM_LABEL42, formLabel42);
        return (LabelShowcase) this;
    }

    /** Returns the value of the text_field(formLabel62). */
    @NotNull public String getFormLabel62() { return f.get(Field.FORM_LABEL62, String.class); }

    /** Sets the value of the text_field(formLabel62). */
    @NotNull public LabelShowcase setFormLabel62(@NotNull String formLabel62) {
        f.set(Field.FORM_LABEL62, formLabel62);
        return (LabelShowcase) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<LabelShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<LabelShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<LabelShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(LabelShowcase.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        ID("id"),
        BINDINGS_WITH_LABELS("bindingsWithLabels"),
        WITH_LABEL1("withLabel1"),
        NO_LABEL1("noLabel1"),
        NO_LABEL2("noLabel2"),
        BINDINGS_NO_LABELS("bindingsNoLabels"),
        WITH_LABEL2("withLabel2"),
        NO_LABEL3("noLabel3"),
        NO_LABEL4("noLabel4"),
        WITH_LABELS("withLabels"),
        FORM_LABEL1("formLabel1"),
        FORM_LABEL3("formLabel3"),
        FORM_LABEL5("formLabel5"),
        NO_LABELS("noLabels"),
        FORM_LABEL2("formLabel2"),
        FORM_LABEL4("formLabel4"),
        FORM_LABEL6("formLabel6"),
        INPUT_GROUP_WITH_LABELS("inputGroupWithLabels"),
        FORM_LABEL12("formLabel12"),
        FORM_LABEL32("formLabel32"),
        FORM_LABEL52("formLabel52"),
        INPUT_GROUP_NO_LABELS("inputGroupNoLabels"),
        FORM_LABEL22("formLabel22"),
        FORM_LABEL42("formLabel42"),
        FORM_LABEL62("formLabel62"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
