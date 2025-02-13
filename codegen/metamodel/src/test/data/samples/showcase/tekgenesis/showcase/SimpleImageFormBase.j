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
import tekgenesis.common.core.Resource;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SimpleImageForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SimpleImageFormBase
    extends FormInstance<ImageResource>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final ImageResource imageResource = ImageResource.create(getId());
        copyTo(imageResource);
        imageResource.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final ImageResource imageResource = find();
        copyTo(imageResource);
        imageResource.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public ImageResource find() {
        final ImageResource value = ImageResource.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public ImageResource populate() {
        final ImageResource imageResource = find();

        setImgBound(imageResource.getImg());

        return imageResource;
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

    /** Copies field values to given imageResource instance. */
    public void copyTo(@NotNull ImageResource imageResource) { imageResource.setImg(getImgBound()); }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public SimpleImageForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the upload(imgBound). */
    @Nullable public Resource getImgBound() { return f.get(Field.IMG_BOUND, Resource.class); }

    /** Sets the value of the upload(imgBound). */
    @NotNull public SimpleImageForm setImgBound(@Nullable Resource imgBound) {
        f.set(Field.IMG_BOUND, imgBound);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the image(imgEnum). */
    @NotNull public Bands getImgEnum() { return Bands.valueOf(f.get(Field.IMG_ENUM, String.class)); }

    /** Sets the value of the image(imgEnum). */
    @NotNull public SimpleImageForm setImgEnum(@NotNull Bands imgEnum) {
        f.set(Field.IMG_ENUM, imgEnum);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the image(imgString). */
    @NotNull public String getImgString() { return f.get(Field.IMG_STRING, String.class); }

    /** Sets the value of the image(imgString). */
    @NotNull public SimpleImageForm setImgString(@NotNull String imgString) {
        f.set(Field.IMG_STRING, imgString);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the image(imgMultiple). */
    @NotNull public Seq<String> getImgMultiple() { return f.getArray(Field.IMG_MULTIPLE, String.class); }

    /** Sets the value of the image(imgMultiple). */
    @NotNull public SimpleImageForm setImgMultiple(@NotNull Iterable<String> imgMultiple) {
        f.setArray(Field.IMG_MULTIPLE, imgMultiple);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the image(imgResource). */
    @Nullable public Resource getImgResource() { return f.get(Field.IMG_RESOURCE, Resource.class); }

    /** Sets the value of the image(imgResource). */
    @NotNull public SimpleImageForm setImgResource(@Nullable Resource imgResource) {
        f.set(Field.IMG_RESOURCE, imgResource);
        return (SimpleImageForm) this;
    }

    /** Returns the value of the gallery(gallery). */
    @NotNull public Seq<String> getGallery() { return f.getArray(Field.GALLERY, String.class); }

    /** Sets the value of the gallery(gallery). */
    @NotNull public SimpleImageForm setGallery(@NotNull Iterable<String> gallery) {
        f.setArray(Field.GALLERY, gallery);
        return (SimpleImageForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleImageForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleImageForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SimpleImageForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SimpleImageForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        IMG_BOUND("imgBound"),
        IMG_ENUM("imgEnum"),
        IMG_STRING("imgString"),
        IMG_MULTIPLE("imgMultiple"),
        IMG_RESOURCE("imgResource"),
        GALLERY("gallery"),
        $F2("$F2"),
        $B3("$B3"),
        $B4("$B4"),
        $B5("$B5");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
