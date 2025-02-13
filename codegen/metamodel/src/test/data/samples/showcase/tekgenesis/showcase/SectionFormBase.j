package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.SectionForm.CellsRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
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
import tekgenesis.showcase.SectionForm.PathRow;
import tekgenesis.showcase.SectionForm.RoomsRow;
import tekgenesis.showcase.SectionForm.ScrollRow;
import tekgenesis.showcase.SectionForm.SecRow;
import tekgenesis.showcase.SectionForm.SomeRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SectionForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SectionFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SectionForm");
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

    /** Returns a {@link FormTable<RoomsRow>} instance to handle Rooms manipulation */
    @NotNull public final FormTable<RoomsRow> getRooms() { return table(Field.ROOMS, RoomsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(amenities). */
    public void setAmenitiesOptions(@NotNull Iterable<SectionCategory> items) { f.opts(Field.AMENITIES, items); }

    /** Sets the options of the combo_box(amenities) with the given KeyMap. */
    public void setAmenitiesOptions(@NotNull KeyMap items) { f.opts(Field.AMENITIES, items); }

    /** Invoked when button(more) is clicked */
    @NotNull public abstract Action more();

    /** Invoked when button(clear) is clicked */
    @NotNull public abstract Action clear();

    /** Invoked when button(remove) is clicked */
    @NotNull public abstract Action removeFirstRoom();

    /** Returns a {@link FormTable<PathRow>} instance to handle Path manipulation */
    @NotNull public final FormTable<PathRow> getPath() { return table(Field.PATH, PathRow.class); }

    /** Returns a {@link FormTable<SecRow>} instance to handle Sec manipulation */
    @NotNull public final FormTable<SecRow> getSec() { return table(Field.SEC, SecRow.class); }

    /** Returns a {@link FormTable<CellsRow>} instance to handle Cells manipulation */
    @NotNull public final FormTable<CellsRow> getCells() { return table(Field.CELLS, CellsRow.class); }

    /** Returns a {@link FormTable<SomeRow>} instance to handle Some manipulation */
    @NotNull public final FormTable<SomeRow> getSome() { return table(Field.SOME, SomeRow.class); }

    /** Returns a {@link FormTable<ScrollRow>} instance to handle Scroll manipulation */
    @NotNull public final FormTable<ScrollRow> getScroll() { return table(Field.SCROLL, ScrollRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SectionForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SectionForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SectionForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SectionForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class RoomsRowBase
        implements FormRowInstance<RoomsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** 
         * Invoked when text_field(adults) value changes
         * Invoked when text_field(children) value changes
         */
        @NotNull public abstract Action changed();

        /** Returns the value of the text_field(adults). */
        public int getAdults() { return f.get(Field.ADULTS, Integer.class); }

        /** Sets the value of the text_field(adults). */
        @NotNull public RoomsRow setAdults(int adults) {
            f.set(Field.ADULTS, adults);
            return (RoomsRow) this;
        }

        /** Returns the value of the text_field(children). */
        public int getChildren() { return f.get(Field.CHILDREN, Integer.class); }

        /** Sets the value of the text_field(children). */
        @NotNull public RoomsRow setChildren(int children) {
            f.set(Field.CHILDREN, children);
            return (RoomsRow) this;
        }

        /** Returns the value of the combo_box(amenities). */
        @NotNull public SectionCategory getAmenities() {
            return SectionCategory.valueOf(f.get(Field.AMENITIES, String.class));
        }

        /** Sets the value of the combo_box(amenities). */
        @NotNull public RoomsRow setAmenities(@NotNull SectionCategory amenities) {
            f.set(Field.AMENITIES, amenities);
            return (RoomsRow) this;
        }

        /** Sets the options of the combo_box(amenities). */
        public void setAmenitiesOptions(@NotNull Iterable<SectionCategory> items) { f.opts(Field.AMENITIES, items); }

        /** Sets the options of the combo_box(amenities) with the given KeyMap. */
        public void setAmenitiesOptions(@NotNull KeyMap items) { f.opts(Field.AMENITIES, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<RoomsRow> table() { return getRooms(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((RoomsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class PathRowBase
        implements FormRowInstance<PathRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(fqn). */
        @NotNull public String getFqn() { return f.get(Field.FQN, String.class); }

        /** Sets the value of the internal(fqn). */
        @NotNull public PathRow setFqn(@NotNull String fqn) {
            f.set(Field.FQN, fqn);
            return (PathRow) this;
        }

        /** Invoked when display(display) is clicked */
        @NotNull public abstract Action link();

        /** Returns the value of the display(display). */
        @NotNull public String getDisplay() { return f.get(Field.DISPLAY, String.class); }

        /** Sets the value of the display(display). */
        @NotNull public PathRow setDisplay(@NotNull String display) {
            f.set(Field.DISPLAY, display);
            return (PathRow) this;
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
        @NotNull public final FormTable<PathRow> table() { return getPath(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PathRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class SecRowBase
        implements FormRowInstance<SecRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(width). */
        public int getWidth() { return f.get(Field.WIDTH, Integer.class); }

        /** Sets the value of the internal(width). */
        @NotNull public SecRow setWidth(int width) {
            f.set(Field.WIDTH, width);
            return (SecRow) this;
        }

        /** Returns the value of the display(lab). */
        @NotNull public String getLab() { return f.get(Field.LAB, String.class); }

        /** Sets the value of the display(lab). */
        @NotNull public SecRow setLab(@NotNull String lab) {
            f.set(Field.LAB, lab);
            return (SecRow) this;
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
        @NotNull public final FormTable<SecRow> table() { return getSec(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SecRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class CellsRowBase
        implements FormRowInstance<CellsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(value). */
        @NotNull public String getValue() { return f.get(Field.VALUE, String.class); }

        /** Sets the value of the display(value). */
        @NotNull public CellsRow setValue(@NotNull String value) {
            f.set(Field.VALUE, value);
            return (CellsRow) this;
        }

        /** Returns the value of the internal(style). */
        @NotNull public String getStyle() { return f.get(Field.STYLE, String.class); }

        /** Sets the value of the internal(style). */
        @NotNull public CellsRow setStyle(@NotNull String style) {
            f.set(Field.STYLE, style);
            return (CellsRow) this;
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
        @NotNull public final FormTable<CellsRow> table() { return getCells(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((CellsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class SomeRowBase
        implements FormRowInstance<SomeRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the image(colImg). */
        @NotNull public String getColImg() { return f.get(Field.COL_IMG, String.class); }

        /** Sets the value of the image(colImg). */
        @NotNull public SomeRow setColImg(@NotNull String colImg) {
            f.set(Field.COL_IMG, colImg);
            return (SomeRow) this;
        }

        /** Returns the value of the display(displayText). */
        @NotNull public String getDisplayText() { return f.get(Field.DISPLAY_TEXT, String.class); }

        /** Sets the value of the display(displayText). */
        @NotNull public SomeRow setDisplayText(@NotNull String displayText) {
            f.set(Field.DISPLAY_TEXT, displayText);
            return (SomeRow) this;
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
        @NotNull public final FormTable<SomeRow> table() { return getSome(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SomeRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class ScrollRowBase
        implements FormRowInstance<ScrollRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the image(img). */
        @NotNull public String getImg() { return f.get(Field.IMG, String.class); }

        /** Sets the value of the image(img). */
        @NotNull public ScrollRow setImg(@NotNull String img) {
            f.set(Field.IMG, img);
            return (ScrollRow) this;
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
        @NotNull public final FormTable<ScrollRow> table() { return getScroll(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ScrollRow) this);
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
        $L2("$L2"),
        ROOMS("rooms"),
        $H3("$H3"),
        ADULTS("adults"),
        CHILDREN("children"),
        AMENITIES("amenities"),
        $H4("$H4"),
        MORE("more"),
        CLEAR("clear"),
        REMOVE("remove"),
        PATH("path"),
        $H5("$H5"),
        FQN("fqn"),
        DISPLAY("display"),
        $L6("$L6"),
        SEC("sec"),
        WIDTH("width"),
        LAB("lab"),
        $H7("$H7"),
        CELLS("cells"),
        VALUE("value"),
        STYLE("style"),
        SOME("some"),
        $V8("$V8"),
        COL_IMG("colImg"),
        DISPLAY_TEXT("displayText"),
        SCROLL("scroll"),
        IMG("img"),
        NAME("name");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
