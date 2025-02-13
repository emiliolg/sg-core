
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;

import static tekgenesis.metadata.form.model.ParentModel.createParent;

/**
 * Model values for a {@link WidgetDef widget definition}.
 */
public class WidgetDefModel extends UiModelBase<WidgetDef> {

    //~ Constructors .................................................................................................................................

    private WidgetDefModel(@NotNull final WidgetDef metamodel, @NotNull final ParentModel parent) {
        this(metamodel, parent.model, parent.anchor, parent.section);
    }

    /** Widget definition constructor. */
    public WidgetDefModel(@NotNull final WidgetDef metamodel, @NotNull final UiModelBase<?> parent, @Nullable final Widget anchor,
                          @NotNull final Option<Integer> section) {
        this(metamodel,
            createParent(parent, anchor, section),
            metamodel.getFieldDimension(),
            metamodel.getMultipleDimension(),
            metamodel.getSubformDimension(),
            metamodel.getWidgetDefDimension(),
            metamodel.getOptionsDimension(),
            metamodel.getConfigureDimension());
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    private WidgetDefModel(@Nullable WidgetDef metamodel, @NotNull ParentModel parent, int fieldDimension, int multipleDimension,
                           int subformDimension, int widgetDefDimension, int optionsDimension, int configureDimension) {
        super(metamodel, parent, fieldDimension, multipleDimension, optionsDimension, subformDimension, widgetDefDimension, configureDimension);
    }

    //~ Methods ......................................................................................................................................

    @Override public WidgetDefModel init(@NotNull WidgetDef m) {
        return (WidgetDefModel) super.init(m);
    }

    @NotNull @Override public FormModel root() {
        return parent().getOrFail(MANDATORY_PARENT).root();
    }

    @Override public Widget widgetByEnumOrdinal(int ordinal) {
        return metadata().getWidgetByOrdinal(ordinal);
    }

    @Override public Widget widgetByName(String name) {
        return metadata().getElement(name);
    }

    @Override public boolean isDeprecated() {
        // todo pcolunga review
        return false;
    }

    @Override public boolean isUpdate() {
        return root().isUpdate();
    }

    @Override public boolean isReadOnly() {
        return root().isReadOnly();
    }

    protected WidgetDefModel copy() {
        final WidgetDefModel copy = new WidgetDefModel(metadata(), parent().getOrFail(MANDATORY_PARENT));
        copyTo(copy);
        return copy;
    }

    void reparent(@NotNull final UiModelBase<?> actual) {
        parent().get().reparent(actual);
    }

    @Override void serialize(StreamWriter w, boolean full) {
        w.writeInt(multiples.length);
        w.writeInt(values.length);
        w.writeInt(subforms.length);
        w.writeInt(widgets.length);
        w.writeInt(options.length);
        w.writeInt(configurations.length);

        super.serialize(w, full);
    }

    //~ Methods ......................................................................................................................................

    /** Initiate a {@link WidgetDefModel} from a StreamReader. */
    public static WidgetDefModel instantiate(@NotNull final StreamReader r, @NotNull UiModelBase<?> parent, @NotNull Option<Integer> section) {
        final int multipleDimension  = r.readInt();
        final int fieldDimension     = r.readInt();
        final int subformDimension   = r.readInt();
        final int widgetDefDimension = r.readInt();
        final int optionsDimension   = r.readInt();
        final int configureDimension = r.readInt();
        return new WidgetDefModel(null,
            createParent(parent, null, section),
            fieldDimension,
            multipleDimension,
            subformDimension,
            widgetDefDimension,
            optionsDimension,
            configureDimension);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String MANDATORY_PARENT = "Widget definitions should always have parent";
}  // end class WidgetDefModel
