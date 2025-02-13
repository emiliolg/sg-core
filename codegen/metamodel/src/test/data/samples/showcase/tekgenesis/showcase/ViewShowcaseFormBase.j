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
import tekgenesis.showcase.ViewShowcaseForm.PropsRow;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ViewShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ViewShowcaseFormBase
    extends FormInstance<TextShowcase>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final TextShowcase textShowcase = TextShowcase.create(getId());
        copyTo(textShowcase);
        createOrUpdateProps(textShowcase);
        textShowcase.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final TextShowcase textShowcase = find();
        copyTo(textShowcase);
        createOrUpdateProps(textShowcase);
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

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public TextShowcase populate() {
        final TextShowcase textShowcase = find();

        setTxt(textShowcase.getTxt())
        	.setDate(textShowcase.getDate())
        	.setBool(textShowcase.isBool())
        	.setOption(textShowcase.getOption())
        	.setEntity(textShowcase.getEntity());

        getProps().populate(textShowcase.getProp(), PropsRow::populate);

        return textShowcase;
    }

    /** Invoked when the form is loaded */
    public abstract void onLoad();

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
    public void createOrUpdateProps(@NotNull TextShowcase textShowcase) {
        textShowcase.getProp().merge(getProps(), (myProp, row) -> row.copyTo(myProp));
    }

    /** Returns the value of the display(id). */
    public int getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the display(id). */
    @NotNull public ViewShowcaseForm setId(int id) {
        f.set(Field.ID, id);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(txt). */
    @NotNull public String getTxt() { return f.get(Field.TXT, String.class); }

    /** Sets the value of the display(txt). */
    @NotNull public ViewShowcaseForm setTxt(@NotNull String txt) {
        f.set(Field.TXT, txt);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the display(date). */
    @NotNull public ViewShowcaseForm setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(bool). */
    public boolean isBool() { return f.get(Field.BOOL, Boolean.class); }

    /** Sets the value of the display(bool). */
    @NotNull public ViewShowcaseForm setBool(boolean bool) {
        f.set(Field.BOOL, bool);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(lon). */
    @NotNull public String getLon() { return f.get(Field.LON, String.class); }

    /** Sets the value of the display(lon). */
    @NotNull public ViewShowcaseForm setLon(@NotNull String lon) {
        f.set(Field.LON, lon);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(option). */
    @NotNull public Options getOption() { return Options.valueOf(f.get(Field.OPTION, String.class)); }

    /** Sets the value of the display(option). */
    @NotNull public ViewShowcaseForm setOption(@NotNull Options option) {
        f.set(Field.OPTION, option);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(disp1). */
    @NotNull public String getDisp1() { return f.get(Field.DISP1, String.class); }

    /** Sets the value of the display(disp1). */
    @NotNull public ViewShowcaseForm setDisp1(@NotNull String disp1) {
        f.set(Field.DISP1, disp1);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(disp2). */
    @NotNull public String getDisp2() { return f.get(Field.DISP2, String.class); }

    /** Sets the value of the display(disp2). */
    @NotNull public ViewShowcaseForm setDisp2(@NotNull String disp2) {
        f.set(Field.DISP2, disp2);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(disp3). */
    @NotNull public String getDisp3() { return f.get(Field.DISP3, String.class); }

    /** Returns the value of the display(disp4). */
    @NotNull public String getDisp4() { return f.get(Field.DISP4, String.class); }

    /** Returns the value of the display(entity). */
    @NotNull public SimpleEntity getEntity() {
        return Predefined.ensureNotNull(SimpleEntity.find(getEntityKey()), "'entity' not found");
    }

    /** Returns the key value of the display(entity). */
    @NotNull public String getEntityKey() { return f.get(Field.ENTITY, String.class); }

    /** Sets the value of the display(entity). */
    @NotNull public ViewShowcaseForm setEntity(@NotNull SimpleEntity entity) {
        f.set(Field.ENTITY, entity);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(entityWithImageDisplay). */
    @NotNull public SimpleEntity getEntityWithImageDisplay() {
        return Predefined.ensureNotNull(SimpleEntity.find(getEntityWithImageDisplayKey()), "'entityWithImageDisplay' not found");
    }

    /** Returns the key value of the display(entityWithImageDisplay). */
    @NotNull public String getEntityWithImageDisplayKey() { return f.get(Field.ENTITY_WITH_IMAGE_DISPLAY, String.class); }

    /** Sets the value of the display(entityWithImageDisplay). */
    @NotNull public ViewShowcaseForm setEntityWithImageDisplay(@NotNull SimpleEntity entityWithImageDisplay) {
        f.set(Field.ENTITY_WITH_IMAGE_DISPLAY, entityWithImageDisplay);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(multipleReal). */
    @NotNull public Seq<Double> getMultipleReal() { return f.getArray(Field.MULTIPLE_REAL, Double.class); }

    /** Sets the value of the display(multipleReal). */
    @NotNull public ViewShowcaseForm setMultipleReal(@NotNull Iterable<Double> multipleReal) {
        f.setArray(Field.MULTIPLE_REAL, multipleReal);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(multipleLon). */
    @NotNull public Seq<String> getMultipleLon() { return f.getArray(Field.MULTIPLE_LON, String.class); }

    /** Sets the value of the display(multipleLon). */
    @NotNull public ViewShowcaseForm setMultipleLon(@NotNull Iterable<String> multipleLon) {
        f.setArray(Field.MULTIPLE_LON, multipleLon);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(multipleEntity). */
    @NotNull public Seq<SimpleEntity> getMultipleEntity() {
        return f.getArray(Field.MULTIPLE_ENTITY, SimpleEntity.class);
    }

    /** Sets the value of the display(multipleEntity). */
    @NotNull public ViewShowcaseForm setMultipleEntity(@NotNull Iterable<SimpleEntity> multipleEntity) {
        f.setArray(Field.MULTIPLE_ENTITY, multipleEntity);
        return (ViewShowcaseForm) this;
    }

    /** Returns the value of the display(multipleEntityWithImage). */
    @NotNull public Seq<SimpleEntity> getMultipleEntityWithImage() {
        return f.getArray(Field.MULTIPLE_ENTITY_WITH_IMAGE, SimpleEntity.class);
    }

    /** Sets the value of the display(multipleEntityWithImage). */
    @NotNull public ViewShowcaseForm setMultipleEntityWithImage(@NotNull Iterable<SimpleEntity> multipleEntityWithImage) {
        f.setArray(Field.MULTIPLE_ENTITY_WITH_IMAGE, multipleEntityWithImage);
        return (ViewShowcaseForm) this;
    }

    /** Returns a {@link FormTable<PropsRow>} instance to handle Props manipulation */
    @NotNull public final FormTable<PropsRow> getProps() { return table(Field.PROPS, PropsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(type). */
    public void setTypeOptions(@NotNull Iterable<PropertyType> items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the combo_box(type) with the given KeyMap. */
    public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the dynamic(value) with the given KeyMap. */
    public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ViewShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ViewShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ViewShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ViewShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PropsRowBase
        implements FormRowInstance<PropsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when combo_box(type) value changes */
        @NotNull public abstract Action render();

        /** Returns the value of the combo_box(type). */
        @NotNull public PropertyType getType() {
            return PropertyType.valueOf(f.get(Field.TYPE, String.class));
        }

        /** Sets the value of the combo_box(type). */
        @NotNull public PropsRow setType(@NotNull PropertyType type) {
            f.set(Field.TYPE, type);
            return (PropsRow) this;
        }

        /** Sets the options of the combo_box(type). */
        public void setTypeOptions(@NotNull Iterable<PropertyType> items) { f.opts(Field.TYPE, items); }

        /** Sets the options of the combo_box(type) with the given KeyMap. */
        public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

        /** Returns the value of the dynamic(value). */
        @NotNull public Object getValue() { return f.get(Field.VALUE, Object.class); }

        /** Sets the value of the dynamic(value). */
        @NotNull public PropsRow setValue(@NotNull Object value) {
            f.set(Field.VALUE, value);
            return (PropsRow) this;
        }

        /** Sets the options of the dynamic(value) with the given KeyMap. */
        public void setValueOptions(@NotNull KeyMap items) { f.opts(Field.VALUE, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<PropsRow> table() { return getProps(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PropsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given myProp instance. */
        public void populate(@NotNull MyProp myProp) { setType(myProp.getType()); }

        /** Copies field values to given myProp instance. */
        public void copyTo(@NotNull MyProp myProp) { myProp.setType(getType()); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        $L3("$L3"),
        ID("id"),
        TXT("txt"),
        DATE("date"),
        BOOL("bool"),
        LON("lon"),
        OPTION("option"),
        DISP1("disp1"),
        DISP2("disp2"),
        DISP3("disp3"),
        DISP4("disp4"),
        ENTITY("entity"),
        ENTITY_WITH_IMAGE_DISPLAY("entityWithImageDisplay"),
        MULTIPLE_REAL("multipleReal"),
        MULTIPLE_LON("multipleLon"),
        MULTIPLE_ENTITY("multipleEntity"),
        MULTIPLE_ENTITY_WITH_IMAGE("multipleEntityWithImage"),
        PROPS("props"),
        TYPE("type"),
        VALUE("value"),
        $H4("$H4"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
