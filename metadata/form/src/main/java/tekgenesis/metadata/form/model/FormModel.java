
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.permission.Permission;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.type.permission.Permission.Custom.valueOf;

/**
 * Model values for a {@link Form form}.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class FormModel extends UiModelBase<Form> {

    //~ Instance Fields ..............................................................................................................................

    private boolean deprecated = false;
    private String  describeBy = "";

    private boolean dirtyByUser = false;

    private SourceWidget focus     = null;
    private SourceWidget lastFocus = null;

    private Integer miid;  // Model Instance ID

    private String parameters = "";

    @NotNull private Set<Permission> permissions     = new HashSet<>();
    private String                   pk              = "";
    private boolean                  readOnly        = false;
    private boolean                  readOnlyChanged = false;

    private boolean           update          = false;
    private boolean           userInteraction = false;
    private InstanceReference workItem        = null;  // Work Item reference

    //~ Constructors .................................................................................................................................

    /** Creates an empty model for a Form. */
    public FormModel(@NotNull final Form metamodel) {
        this(metamodel,
            metamodel.getFieldDimension(),
            metamodel.getMultipleDimension(),
            metamodel.getSubformDimension(),
            metamodel.getWidgetDefDimension(),
            metamodel.getOptionsDimension(),
            metamodel.getConfigureDimension());
        miid = createModelInstanceId();
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    private FormModel(@Nullable Form metamodel, int fieldDimension, int multipleDimension, int subformDimension, int widgetDefDimension,
                      int optionsDimension, int configureDimension) {
        super(metamodel, null, fieldDimension, multipleDimension, optionsDimension, subformDimension, widgetDefDimension, configureDimension);
        miid = null;
    }

    //~ Methods ......................................................................................................................................

    /** Model accepts other model if internal Model Instance ID matches. */
    public boolean accepts(@NotNull final FormModel model) {
        return model.miid.equals(miid);
    }

    /** Sets this FormModel as an update model. */
    public void asUpdate() {
        update = true;
    }

    @Override public void deserialize(final StreamReader r, boolean full) {
        update     = r.readBoolean();
        readOnly   = r.readBoolean();
        deprecated = r.readBoolean();
        focus      = SourceWidget.deserialize(r);
        lastFocus  = SourceWidget.deserialize(r);
        workItem   = InstanceReference.deserialize(r);
        miid       = r.readInt();
        pk         = r.readString();
        parameters = r.readString();
        describeBy = r.readString();

        super.deserialize(r, full);

        permissions = cast(r.readObject());
    }

    /** Queries the given permission name over the permissions. */
    @Override public boolean hasPermission(String name) {
        return permissions.contains(valueOf(name));
    }

    /** Returns true if read-only has changed. */
    public boolean hasReadOnlyChanged() {
        return readOnlyChanged;
    }

    /** Return true if model has associated work item reference. */
    public boolean hasWorkItem() {
        return workItem != null;
    }

    @Override public FormModel init(@NotNull Form m) {
        return (FormModel) super.init(m);
    }

    /** Populate a scalar or column field with a value. */
    public void populateField(final int fieldOrdinal, final int rowIndex, @NotNull final Object value) {
        final Widget field = widgetByEnumOrdinal(fieldOrdinal);
        switch (field.getElemType()) {
        case SCALAR:
            if (rowIndex == 0)  // only set a scalar the first time
                set(field, value);
            break;
        case COLUMN:
            final MultipleModel multiple = getMultiple(field.getMultiple().get());
            final RowModel      r        = multiple.getOrAddRow(rowIndex);
            r.set(field, value);
            break;
        default:
            throw new IllegalArgumentException("Type " + field.getElemType() + " doesn't support a model scalar value");
        }
    }

    @NotNull @Override public FormModel root()
    {
        return this;
    }

    @Override public void serialize(StreamWriter w, boolean full) {
        w.writeInt(multiples.length);
        w.writeInt(values.length);
        w.writeInt(subforms.length);
        w.writeInt(widgets.length);
        w.writeInt(options.length);
        w.writeInt(configurations.length);
        w.writeBoolean(update);
        w.writeBoolean(readOnly);
        w.writeBoolean(deprecated);
        SourceWidget.serialize(w, focus);
        SourceWidget.serialize(w, lastFocus);
        InstanceReference.serialize(w, workItem);
        w.writeInt(miid);
        w.writeString(pk);
        w.writeString(parameters);
        w.writeString(describeBy);

        super.serialize(w, full);

        w.writeObject(permissions);
    }

    /** Starts listening to user interactions. */
    public void startUserInteraction() {
        userInteraction = true;
    }

    /** Sync the dirty values of the passed model. Return true if any value has changed. */
    @Override public boolean sync(final Model model) {
        deprecated = model.isDeprecated();

        final FormModel f = (FormModel) model;
        focus = f.focus != null && equal(f.lastFocus, lastFocus) ? f.focus : lastFocus;

        if (readOnly != f.readOnly) {
            readOnly        = f.readOnly;
            readOnlyChanged = true;
        }

        return super.sync(model);
    }

    /** Update focus on model (from application). */
    public void updateFocus(SourceWidget f) {
        lastFocus = f;
        focus     = null;
    }

    @Override public Widget widgetByEnumOrdinal(int ordinal) {
        return metadata().getWidgetByOrdinal(ordinal);
    }

    @Override public Widget widgetByName(String name) {
        return metadata().getElement(name);
    }

    /** Returns true if this model represents a deprecated instance. */
    public boolean isDeprecated() {
        return deprecated;
    }

    /** Sets this FormModel as a model of a deprecated instance. */
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    /** Get describe by reference on model. Returns null if there is no entity bind or a String */
    public String getDescribeBy() {
        return describeBy;
    }

    /** Set describe by reference on model. */
    public void setDescribeBy(String describeBy) {
        this.describeBy = describeBy;
    }

    /** Returns true for an update, false for a create. */
    public boolean isUpdate() {
        return update;
    }

    /** Get widget on focus. */
    public SourceWidget getFocus() {
        return focus;
    }

    /** Returns the form meta model full name. */
    public String getFormFullName() {
        return metadata().getFullName();
    }

    /** Get last widget on focus. */
    public SourceWidget getLastFocus() {
        return lastFocus;
    }

    /** Returns the parameters under with the form was created. */
    public String getParameters() {
        return parameters;
    }

    /** Sets the parameters under with the form was created. */
    public void setParameters(final String parameters) {
        this.parameters = notNull(parameters);
    }

    /** Turns on/off a permission. */
    public void setPermission(@NotNull final String permission, boolean status) {
        final Permission p = Permission.Custom.valueOf(permission);
        if (status) permissions.add(p);
        else permissions.remove(p);
    }

    /** Sets the permissions that the user has over the form represented by this form model. */
    public void setPermissions(@NotNull Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /** Returns the primary key under with the form was created. */
    public String getPk() {
        return pk;
    }

    /** Sets the primary key under with the form was created. */
    public void setPrimaryKey(final String primaryKey) {
        pk = notNull(primaryKey);
    }

    /** Is model dirty by user? */
    public boolean isDirtyByUser() {
        if (dirtyByUser) return true;

        for (final MultipleModel multiple : multiples) {
            if (multiple.hasAnyRowDirty()) return true;
        }

        return false;
    }

    /** Sets this FormModel as a read-only model. */
    public void setReadOnly(boolean ro) {
        readOnly = ro;
    }

    /** Get work item reference on model. */
    public InstanceReference getWorkItem() {
        return workItem;
    }

    /** Set work item reference on model. */
    public void setWorkItem(InstanceReference workItem) {
        this.workItem = workItem;
    }

    /** Returns true is model is read-only. */
    public boolean isReadOnly() {
        return readOnly;
    }

    protected FormModel copy() {
        final FormModel copy = new FormModel(metadata());
        copyTo(copy);
        return copy;
    }

    /** Copy actual Model values to given Model copy. */
    @Override void copyTo(Model model) {
        final FormModel copy = (FormModel) model;
        copy.update     = update;
        copy.readOnly   = readOnly;
        copy.deprecated = deprecated;
        copy.focus      = focus;
        copy.lastFocus  = lastFocus;
        copy.miid       = miid;
        copy.workItem   = workItem;
        copy.pk         = pk;
        copy.parameters = parameters;

        super.copyTo(copy);

        copy.permissions = permissions;
    }

    /** Returns true if user interaction has started. */
    boolean isUserInteractionStarted() {
        return userInteraction;
    }

    void setDirtyByUser() {
        dirtyByUser = true;
    }

    /** Set intended new focus (from user code). */
    void setIntendedFocus(SourceWidget f) {
        focus = f;
    }

    /** Create Model Instance ID, used to verify model match prior to sync. */
    private Integer createModelInstanceId() {
        return new Random().nextInt();
    }

    //~ Methods ......................................................................................................................................

    /** Initiate a {@link FormModel} from a StreamReader. */
    public static FormModel instantiate(final StreamReader r) {
        final int multipleDimension  = r.readInt();
        final int fieldDimension     = r.readInt();
        final int subformDimension   = r.readInt();
        final int widgetDefDimension = r.readInt();
        final int optionsDimension   = r.readInt();
        final int configureDimension = r.readInt();
        return new FormModel(null, fieldDimension, multipleDimension, subformDimension, widgetDefDimension, optionsDimension, configureDimension);
    }
}  // end class FormModel
