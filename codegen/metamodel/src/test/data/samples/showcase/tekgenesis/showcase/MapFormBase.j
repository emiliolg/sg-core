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
import tekgenesis.showcase.MapForm.Map2Row;
import tekgenesis.showcase.MapForm.MapRow;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.MapForm.PlacesRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: MapForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class MapFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.MapForm");
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

    /** Returns a {@link FormTable<PlacesRow>} instance to handle Places manipulation */
    @NotNull public final FormTable<PlacesRow> getPlaces() { return table(Field.PLACES, PlacesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<MapRow>} instance to handle Map manipulation */
    @NotNull public final FormTable<MapRow> getMap() { return table(Field.MAP, MapRow.class); }

    /** Returns a {@link FormTable<Map2Row>} instance to handle Map2 manipulation */
    @NotNull public final FormTable<Map2Row> getMap2() { return table(Field.MAP2, Map2Row.class); }

    /** Returns the value of the message(clicked). */
    @NotNull public String getClicked() { return f.get(Field.CLICKED, String.class); }

    /** Sets the value of the message(clicked). */
    @NotNull public MapForm setClicked(@NotNull String clicked) {
        f.set(Field.CLICKED, clicked);
        return (MapForm) this;
    }

    /** Invoked when button(bt) is clicked */
    @NotNull public abstract Action doStuff();

    /** Create and set a new MapSubSubForm instance */
    @NotNull public MapSubSubForm createLocate() { return f.init(Field.LOCATE, MapSubSubForm.class); }

    /** Create and populates set a new MapSubSubForm instance with a pk */
    @NotNull public MapSubSubForm createLocate(@NotNull String key) { return f.init(Field.LOCATE, MapSubSubForm.class, key); }

    /** 
     * Get the MapSubSubForm if defined, or null otherwise.
     * @see #createLocate
     */
    @Nullable public MapSubSubForm getLocate() { return f.subform(Field.LOCATE, MapSubSubForm.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MapForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MapForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<MapForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(MapForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PlacesRowBase
        implements FormRowInstance<PlacesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(latitud). */
        public double getLatitud() { return f.get(Field.LATITUD, Double.class); }

        /** Sets the value of the display(latitud). */
        @NotNull public PlacesRow setLatitud(double latitud) {
            f.set(Field.LATITUD, latitud);
            return (PlacesRow) this;
        }

        /** Returns the value of the display(longitud). */
        public double getLongitud() { return f.get(Field.LONGITUD, Double.class); }

        /** Sets the value of the display(longitud). */
        @NotNull public PlacesRow setLongitud(double longitud) {
            f.set(Field.LONGITUD, longitud);
            return (PlacesRow) this;
        }

        /** Returns the value of the display(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the display(name). */
        @NotNull public PlacesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (PlacesRow) this;
        }

        /** Invoked when button($B3) is clicked */
        @NotNull public abstract Action go();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<PlacesRow> table() { return getPlaces(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PlacesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class MapRowBase
        implements FormRowInstance<MapRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(lat). */
        public double getLat() { return f.get(Field.LAT, Double.class); }

        /** Sets the value of the text_field(lat). */
        @NotNull public MapRow setLat(double lat) {
            f.set(Field.LAT, lat);
            return (MapRow) this;
        }

        /** Returns the value of the text_field(lng). */
        public double getLng() { return f.get(Field.LNG, Double.class); }

        /** Sets the value of the text_field(lng). */
        @NotNull public MapRow setLng(double lng) {
            f.set(Field.LNG, lng);
            return (MapRow) this;
        }

        /** Returns the value of the display(title). */
        @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

        /** Sets the value of the display(title). */
        @NotNull public MapRow setTitle(@NotNull String title) {
            f.set(Field.TITLE, title);
            return (MapRow) this;
        }

        /** Returns the value of the image(img). */
        @NotNull public String getImg() { return f.get(Field.IMG, String.class); }

        /** Sets the value of the image(img). */
        @NotNull public MapRow setImg(@NotNull String img) {
            f.set(Field.IMG, img);
            return (MapRow) this;
        }

        /** 
         * Invoked when button(button) is clicked
         * Invoked when button(button1) is clicked
         * Invoked when button(button3) is clicked
         * Invoked when button(button4) is clicked
         * Invoked when button(button5) is clicked
         * Invoked when button(button6) is clicked
         */
        @NotNull public abstract Action show();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<MapRow> table() { return getMap(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((MapRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class Map2RowBase
        implements FormRowInstance<Map2Row>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(lat2). */
        public double getLat2() { return f.get(Field.LAT2, Double.class); }

        /** Sets the value of the text_field(lat2). */
        @NotNull public Map2Row setLat2(double lat2) {
            f.set(Field.LAT2, lat2);
            return (Map2Row) this;
        }

        /** Returns the value of the text_field(lng2). */
        public double getLng2() { return f.get(Field.LNG2, Double.class); }

        /** Sets the value of the text_field(lng2). */
        @NotNull public Map2Row setLng2(double lng2) {
            f.set(Field.LNG2, lng2);
            return (Map2Row) this;
        }

        /** Returns the value of the display(title2). */
        @NotNull public String getTitle2() { return f.get(Field.TITLE2, String.class); }

        /** Sets the value of the display(title2). */
        @NotNull public Map2Row setTitle2(@NotNull String title2) {
            f.set(Field.TITLE2, title2);
            return (Map2Row) this;
        }

        /** Returns the value of the image(img2). */
        @NotNull public String getImg2() { return f.get(Field.IMG2, String.class); }

        /** Sets the value of the image(img2). */
        @NotNull public Map2Row setImg2(@NotNull String img2) {
            f.set(Field.IMG2, img2);
            return (Map2Row) this;
        }

        /** Invoked when button(button2) is clicked */
        @NotNull public abstract Action show();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<Map2Row> table() { return getMap2(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((Map2Row) this);
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
        PLACES("places"),
        LATITUD("latitud"),
        LONGITUD("longitud"),
        NAME("name"),
        $B3("$B3"),
        MAP("map"),
        LAT("lat"),
        LNG("lng"),
        $V4("$V4"),
        TITLE("title"),
        IMG("img"),
        $H5("$H5"),
        BUTTON("button"),
        BUTTON1("button1"),
        BUTTON3("button3"),
        BUTTON4("button4"),
        BUTTON5("button5"),
        BUTTON6("button6"),
        $V6("$V6"),
        MAP2("map2"),
        LAT2("lat2"),
        LNG2("lng2"),
        $V7("$V7"),
        TITLE2("title2"),
        IMG2("img2"),
        BUTTON2("button2"),
        CLICKED("clicked"),
        BT("bt"),
        LOCATE("locate");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
