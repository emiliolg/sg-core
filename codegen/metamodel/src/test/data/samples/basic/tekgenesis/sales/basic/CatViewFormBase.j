package tekgenesis.sales.basic;

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
 * Generated base class for form: CatViewForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CatViewFormBase
    extends FormInstance<CategoryView>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() { throw new UnsupportedOperationException(); }

    /** Invoked when updating a form instance */
    @NotNull public Action update() { throw new UnsupportedOperationException(); }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() { throw new UnsupportedOperationException(); }

    /** Invoked to find an entity instance */
    @NotNull public CategoryView find() {
        final CategoryView value = CategoryView.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.VID, Conversions.toLong(key)); }

    @NotNull public String keyAsString() { return "" + getVid(); }

    /** Invoked when populating a form instance */
    @NotNull public CategoryView populate() {
        final CategoryView categoryView = find();

        setVname(categoryView.getVname())
        	.setVdescr(categoryView.getVdescr());

        return categoryView;
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

    /** Returns the value of the internal(vid). */
    @Nullable public Long getVid() { return f.get(Field.VID, Long.class); }

    /** Sets the value of the internal(vid). */
    @NotNull public CatViewForm setVid(@Nullable Long vid) {
        f.set(Field.VID, vid);
        return (CatViewForm) this;
    }

    /** Returns the value of the text_field(vname). */
    @NotNull public String getVname() { return f.get(Field.VNAME, String.class); }

    /** Sets the value of the text_field(vname). */
    @NotNull public CatViewForm setVname(@NotNull String vname) {
        f.set(Field.VNAME, vname);
        return (CatViewForm) this;
    }

    /** Returns the value of the text_field(vdescr). */
    @NotNull public String getVdescr() { return f.get(Field.VDESCR, String.class); }

    /** Sets the value of the text_field(vdescr). */
    @NotNull public CatViewForm setVdescr(@NotNull String vdescr) {
        f.set(Field.VDESCR, vdescr);
        return (CatViewForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CatViewForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CatViewForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CatViewForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CatViewForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        VID("vid"),
        VNAME("vname"),
        VDESCR("vdescr"),
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
