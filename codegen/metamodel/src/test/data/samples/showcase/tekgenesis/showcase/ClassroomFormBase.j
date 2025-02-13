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
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.ClassroomForm.StudentsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ClassroomForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ClassroomFormBase
    extends FormInstance<Classroom>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Classroom classroom = Classroom.create(getIdKey());
        copyTo(classroom);
        createOrUpdateStudents(classroom);
        classroom.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Classroom classroom = find();
        copyTo(classroom);
        createOrUpdateStudents(classroom);
        classroom.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Classroom find() {
        final Classroom value = Classroom.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID_KEY, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getIdKey(); }

    /** Invoked when populating a form instance */
    @NotNull public Classroom populate() {
        final Classroom classroom = find();

        setRoom(classroom.getRoom());

        getStudents().populate(classroom.getStudents(), StudentsRow::populate);

        return classroom;
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

    /** Copies field values to given classroom instance. */
    public void copyTo(@NotNull Classroom classroom) { classroom.setRoom(getRoom()); }

    /** Updates external references to students. */
    public void createOrUpdateStudents(@NotNull Classroom classroom) {
        classroom.getStudents().merge(getStudents(), (student, row) -> row.copyTo(student));
    }

    /** Returns the value of the text_field(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the text_field(idKey). */
    @NotNull public ClassroomForm setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (ClassroomForm) this;
    }

    /** Returns the value of the text_field(room). */
    @NotNull public String getRoom() { return f.get(Field.ROOM, String.class); }

    /** Sets the value of the text_field(room). */
    @NotNull public ClassroomForm setRoom(@NotNull String room) {
        f.set(Field.ROOM, room);
        return (ClassroomForm) this;
    }

    /** Invoked when text_field(fill) value changes */
    @NotNull public abstract Action fill();

    /** Returns the value of the text_field(fill). */
    @NotNull public String getFill() { return f.get(Field.FILL, String.class); }

    /** Returns the value of the check_box(showDni). */
    public boolean isShowDni() { return f.get(Field.SHOW_DNI, Boolean.class); }

    /** Sets the value of the check_box(showDni). */
    @NotNull public ClassroomForm setShowDni(boolean showDni) {
        f.set(Field.SHOW_DNI, showDni);
        return (ClassroomForm) this;
    }

    /** Returns the value of the check_box(showFirstName). */
    public boolean isShowFirstName() { return f.get(Field.SHOW_FIRST_NAME, Boolean.class); }

    /** Sets the value of the check_box(showFirstName). */
    @NotNull public ClassroomForm setShowFirstName(boolean showFirstName) {
        f.set(Field.SHOW_FIRST_NAME, showFirstName);
        return (ClassroomForm) this;
    }

    /** Returns the value of the check_box(showLastName). */
    public boolean isShowLastName() { return f.get(Field.SHOW_LAST_NAME, Boolean.class); }

    /** Sets the value of the check_box(showLastName). */
    @NotNull public ClassroomForm setShowLastName(boolean showLastName) {
        f.set(Field.SHOW_LAST_NAME, showLastName);
        return (ClassroomForm) this;
    }

    /** Returns the value of the check_box(showAge). */
    public boolean isShowAge() { return f.get(Field.SHOW_AGE, Boolean.class); }

    /** Sets the value of the check_box(showAge). */
    @NotNull public ClassroomForm setShowAge(boolean showAge) {
        f.set(Field.SHOW_AGE, showAge);
        return (ClassroomForm) this;
    }

    /** Returns the value of the check_box(showGender). */
    public boolean isShowGender() { return f.get(Field.SHOW_GENDER, Boolean.class); }

    /** Sets the value of the check_box(showGender). */
    @NotNull public ClassroomForm setShowGender(boolean showGender) {
        f.set(Field.SHOW_GENDER, showGender);
        return (ClassroomForm) this;
    }

    /** Invoked when button(clear) is clicked */
    @NotNull public abstract Action clear();

    /** Invoked when button(clearFill) is clicked */
    @NotNull public abstract Action clearAndFill();

    /** Invoked when button(random) is clicked */
    @NotNull public abstract Action random();

    /** Invoked when button(first) is clicked */
    @NotNull public abstract Action first();

    /** Invoked when button(second) is clicked */
    @NotNull public abstract Action second();

    /** Returns the value of the display(rows). */
    public int getRows() { return f.get(Field.ROWS, Integer.class); }

    /** Sets the value of the display(rows). */
    @NotNull public ClassroomForm setRows(int rows) {
        f.set(Field.ROWS, rows);
        return (ClassroomForm) this;
    }

    /** Returns the value of the check_box(swipe). */
    public boolean isSwipe() { return f.get(Field.SWIPE, Boolean.class); }

    /** Sets the value of the check_box(swipe). */
    @NotNull public ClassroomForm setSwipe(boolean swipe) {
        f.set(Field.SWIPE, swipe);
        return (ClassroomForm) this;
    }

    /** Invoked when button(navigate) is clicked */
    @NotNull public abstract Action navigateToWidgetShowcase();

    /** Invoked when table(students) is clicked */
    @NotNull public abstract Action rowClicked();

    /** Returns a {@link FormTable<StudentsRow>} instance to handle Students manipulation */
    @NotNull public final FormTable<StudentsRow> getStudents() { return table(Field.STUDENTS, StudentsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(gender). */
    public void setGenderOptions(@NotNull Iterable<Gender> items) { f.opts(Field.GENDER, items); }

    /** Sets the options of the combo_box(gender) with the given KeyMap. */
    public void setGenderOptions(@NotNull KeyMap items) { f.opts(Field.GENDER, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClassroomForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClassroomForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ClassroomForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ClassroomForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class StudentsRowBase
        implements FormRowInstance<StudentsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(dni). */
        public int getDni() { return f.get(Field.DNI, Integer.class); }

        /** Sets the value of the text_field(dni). */
        @NotNull public StudentsRow setDni(int dni) {
            f.set(Field.DNI, dni);
            return (StudentsRow) this;
        }

        /** Returns the value of the text_field(firstName). */
        @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

        /** Sets the value of the text_field(firstName). */
        @NotNull public StudentsRow setFirstName(@NotNull String firstName) {
            f.set(Field.FIRST_NAME, firstName);
            return (StudentsRow) this;
        }

        /** Returns the value of the text_field(lastName). */
        @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

        /** Sets the value of the text_field(lastName). */
        @NotNull public StudentsRow setLastName(@NotNull String lastName) {
            f.set(Field.LAST_NAME, lastName);
            return (StudentsRow) this;
        }

        /** Returns the value of the text_field(age). */
        public int getAge() { return f.get(Field.AGE, Integer.class); }

        /** Sets the value of the text_field(age). */
        @NotNull public StudentsRow setAge(int age) {
            f.set(Field.AGE, age);
            return (StudentsRow) this;
        }

        /** Returns the value of the combo_box(gender). */
        @NotNull public Gender getGender() { return Gender.valueOf(f.get(Field.GENDER, String.class)); }

        /** Sets the value of the combo_box(gender). */
        @NotNull public StudentsRow setGender(@NotNull Gender gender) {
            f.set(Field.GENDER, gender);
            return (StudentsRow) this;
        }

        /** Sets the options of the combo_box(gender). */
        public void setGenderOptions(@NotNull Iterable<Gender> items) { f.opts(Field.GENDER, items); }

        /** Sets the options of the combo_box(gender) with the given KeyMap. */
        public void setGenderOptions(@NotNull KeyMap items) { f.opts(Field.GENDER, items); }

        /** Invoked when button(view) is clicked */
        @NotNull public abstract Action swipe();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<StudentsRow> table() { return getStudents(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((StudentsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given student instance. */
        public void populate(@NotNull Student student) {
            setDni(student.getDni())
            	.setFirstName(student.getFirstName())
            	.setLastName(student.getLastName())
            	.setAge(student.getAge())
            	.setGender(student.getGender());
        }

        /** Copies field values to given student instance. */
        public void copyTo(@NotNull Student student) {
            student.setDni(getDni())
            	.setFirstName(getFirstName())
            	.setLastName(getLastName())
            	.setAge(getAge())
            	.setGender(getGender());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        $L3("$L3"),
        ID_KEY("idKey"),
        ROOM("room"),
        FILL("fill"),
        $H4("$H4"),
        SHOW_DNI("showDni"),
        SHOW_FIRST_NAME("showFirstName"),
        SHOW_LAST_NAME("showLastName"),
        SHOW_AGE("showAge"),
        SHOW_GENDER("showGender"),
        $H5("$H5"),
        CLEAR("clear"),
        CLEAR_FILL("clearFill"),
        RANDOM("random"),
        FIRST("first"),
        SECOND("second"),
        ROWS("rows"),
        SWIPE("swipe"),
        NAVIGATE("navigate"),
        STUDENTS("students"),
        DNI("dni"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        AGE("age"),
        GENDER("gender"),
        VIEW("view"),
        DDOWN("ddown"),
        $L6("$L6"),
        $L7("$L7"),
        $L8("$L8"),
        $H9("$H9"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
        $F10("$F10"),
        $B11("$B11"),
        $B12("$B12"),
        $B13("$B13");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
