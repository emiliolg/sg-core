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
import tekgenesis.showcase.TableImageForm.ImgsRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Resource;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableImageForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableImageFormBase
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
        createOrUpdateImgs(imageResource);
        imageResource.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final ImageResource imageResource = find();
        copyTo(imageResource);
        createOrUpdateImgs(imageResource);
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

        setImg(imageResource.getImg());

        getImgs().populate(imageResource.getImgs(), ImgsRow::populate);

        return imageResource;
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

    /** Copies field values to given imageResource instance. */
    public void copyTo(@NotNull ImageResource imageResource) { imageResource.setImg(getImg()); }

    /** Updates external references to imgs. */
    public void createOrUpdateImgs(@NotNull ImageResource imageResource) {
        imageResource.getImgs().merge(getImgs(), (imageResources, row) -> row.copyTo(imageResources));
    }

    /** Returns the value of the text_field(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the text_field(id). */
    @NotNull public TableImageForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (TableImageForm) this;
    }

    /** Returns the value of the upload(img). */
    @Nullable public Resource getImg() { return f.get(Field.IMG, Resource.class); }

    /** Sets the value of the upload(img). */
    @NotNull public TableImageForm setImg(@Nullable Resource img) {
        f.set(Field.IMG, img);
        return (TableImageForm) this;
    }

    /** Invoked when button(clear) is clicked */
    @NotNull public abstract Action clear();

    /** Returns a {@link FormTable<ImgsRow>} instance to handle Imgs manipulation */
    @NotNull public final FormTable<ImgsRow> getImgs() { return table(Field.IMGS, ImgsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableImageForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableImageForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TableImageForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TableImageForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ImgsRowBase
        implements FormRowInstance<ImgsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(imgsName). */
        @NotNull public String getImgsName() { return f.get(Field.IMGS_NAME, String.class); }

        /** Sets the value of the text_field(imgsName). */
        @NotNull public ImgsRow setImgsName(@NotNull String imgsName) {
            f.set(Field.IMGS_NAME, imgsName);
            return (ImgsRow) this;
        }

        /** Returns the value of the upload(imgsImg). */
        @Nullable public Resource getImgsImg() { return f.get(Field.IMGS_IMG, Resource.class); }

        /** Sets the value of the upload(imgsImg). */
        @NotNull public ImgsRow setImgsImg(@Nullable Resource imgsImg) {
            f.set(Field.IMGS_IMG, imgsImg);
            return (ImgsRow) this;
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
        @NotNull public final FormTable<ImgsRow> table() { return getImgs(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ImgsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given imageResources instance. */
        public void populate(@NotNull ImageResources imageResources) {
            setImgsName(imageResources.getName())
            	.setImgsImg(imageResources.getImg());
        }

        /** Copies field values to given imageResources instance. */
        public void copyTo(@NotNull ImageResources imageResources) {
            imageResources.setName(getImgsName())
            	.setImg(getImgsImg());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        IMG("img"),
        CLEAR("clear"),
        IMGS("imgs"),
        IMGS_NAME("imgsName"),
        IMGS_IMG("imgsImg"),
        $H2("$H2"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
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
