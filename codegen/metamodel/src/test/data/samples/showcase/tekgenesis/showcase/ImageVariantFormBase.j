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
 * Generated base class for form: ImageVariantForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ImageVariantFormBase
    extends FormInstance<Image>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Image image = Image.create();
        copyTo(image);
        image.insert();
        setId(image.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Image image = find();
        copyTo(image);
        image.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Image find() {
        final Image value = Image.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Image populate() {
        final Image image = find();

        setName(image.getName())
        	.setResource(image.getResource());

        return image;
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

    /** Copies field values to given image instance. */
    public void copyTo(@NotNull Image image) {
        image.setName(getName())
        	.setResource(getResource());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public ImageVariantForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (ImageVariantForm) this;
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public ImageVariantForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (ImageVariantForm) this;
    }

    /** Invoked when upload(resource) value ui changes */
    @NotNull public abstract Action copyImage();

    /** Returns the value of the upload(resource). */
    @NotNull public Resource getResource() { return f.get(Field.RESOURCE, Resource.class); }

    /** Sets the value of the upload(resource). */
    @NotNull public ImageVariantForm setResource(@NotNull Resource resource) {
        f.set(Field.RESOURCE, resource);
        return (ImageVariantForm) this;
    }

    /** Invoked when button(fillButt) is clicked */
    @NotNull public abstract Action fillGallery();

    /** Returns the value of the showcase(showcase). */
    @NotNull public Seq<Resource> getShowcase() { return f.getArray(Field.SHOWCASE, Resource.class); }

    /** Sets the value of the showcase(showcase). */
    @NotNull public ImageVariantForm setShowcase(@NotNull Iterable<Resource> showcase) {
        f.setArray(Field.SHOWCASE, showcase);
        return (ImageVariantForm) this;
    }

    /** Returns the value of the showcase(showcase1). */
    @NotNull public Seq<Resource> getShowcase1() { return f.getArray(Field.SHOWCASE1, Resource.class); }

    /** Sets the value of the showcase(showcase1). */
    @NotNull public ImageVariantForm setShowcase1(@NotNull Iterable<Resource> showcase1) {
        f.setArray(Field.SHOWCASE1, showcase1);
        return (ImageVariantForm) this;
    }

    /** Returns the value of the gallery(gal). */
    @NotNull public Seq<Resource> getGal() { return f.getArray(Field.GAL, Resource.class); }

    /** Sets the value of the gallery(gal). */
    @NotNull public ImageVariantForm setGal(@NotNull Iterable<Resource> gal) {
        f.setArray(Field.GAL, gal);
        return (ImageVariantForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ImageVariantForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ImageVariantForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ImageVariantForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ImageVariantForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SEARCH("search"),
        ID("id"),
        NAME("name"),
        RESOURCE("resource"),
        $V2("$V2"),
        FILL_BUTT("fillButt"),
        SHOWCASE("showcase"),
        SHOWCASE1("showcase1"),
        GAL("gal"),
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
