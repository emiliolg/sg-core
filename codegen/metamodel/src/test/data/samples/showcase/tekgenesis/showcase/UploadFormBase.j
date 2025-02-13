package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Resource;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: UploadForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class UploadFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.UploadForm");
    }

    /** Invoked when the form is loaded */
    public abstract void cropConfig();

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

    /** Returns the value of the upload(camera). */
    @NotNull public Resource getCamera() { return f.get(Field.CAMERA, Resource.class); }

    /** Sets the value of the upload(camera). */
    @NotNull public UploadForm setCamera(@NotNull Resource camera) {
        f.set(Field.CAMERA, camera);
        return (UploadForm) this;
    }

    /** Returns the value of the upload(up). */
    @NotNull public Seq<Resource> getUp() { return f.getArray(Field.UP, Resource.class); }

    /** Sets the value of the upload(up). */
    @NotNull public UploadForm setUp(@NotNull Iterable<Resource> up) {
        f.setArray(Field.UP, up);
        return (UploadForm) this;
    }

    /** Returns the value of the showcase(showcase). */
    @NotNull public Seq<Resource> getShowcase() { return f.getArray(Field.SHOWCASE, Resource.class); }

    /** Sets the value of the showcase(showcase). */
    @NotNull public UploadForm setShowcase(@NotNull Iterable<Resource> showcase) {
        f.setArray(Field.SHOWCASE, showcase);
        return (UploadForm) this;
    }

    /** Invoked when button(butt) is clicked */
    @NotNull public abstract Action resetMe();

    /** Invoked when button(fillButt) is clicked */
    @NotNull public abstract Action fillGallery();

    /** Returns the value of the gallery(gal). */
    @NotNull public Seq<Resource> getGal() { return f.getArray(Field.GAL, Resource.class); }

    /** Sets the value of the gallery(gal). */
    @NotNull public UploadForm setGal(@NotNull Iterable<Resource> gal) {
        f.setArray(Field.GAL, gal);
        return (UploadForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<UploadForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<UploadForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<UploadForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(UploadForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        CAMERA("camera"),
        UP("up"),
        SHOWCASE("showcase"),
        BUTT("butt"),
        FILL_BUTT("fillButt"),
        GAL("gal");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
