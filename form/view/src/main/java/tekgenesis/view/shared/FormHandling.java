
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.view.client.ClientUiModelRetriever;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.widget.WidgetType.SUBFORM;
import static tekgenesis.metadata.form.widget.WidgetType.WIDGET;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;
import static tekgenesis.view.client.ClientUiModelContext.getRetriever;

/**
 * Form handling. Cache, bind, subforms, etc.
 */
public class FormHandling {

    //~ Constructors .................................................................................................................................

    private FormHandling() {}

    //~ Methods ......................................................................................................................................

    /** Clear entries that should be deleted once the model is un-loaded. */
    public static void clearTransientCache() {
        getRetriever().clearTransient();
    }

    /** Inits FormModel. */
    public static <M extends UiModel, T extends UiModelBase<M>> T init(T model, String fqn, @NotNull UiModelRetriever retriever) {
        retriever.getUiModel(createQName(fqn)).ifPresent(m -> model.init(cast(m)));
        return model;
    }

    /** Checks FormModel's subforms. */
    public static void initMetadata(@NotNull FormModel model, @NotNull UiModelRetriever retriever) {
        initMetadata(model, model.metadata(), retriever);
    }

    /** Updates model model's cache. */
    public static void updateCache(@NotNull Form form, @Nullable Iterable<UiModel> references) {
        final ClientUiModelRetriever retriever = getRetriever();
        if (references != null) retriever.putAll(references);
        retriever.put(form);
    }

    /** Recursively initialize {@link UiModelBase<?> model definition} metadata. */
    private static void initMetadata(@NotNull UiModelBase<? extends UiModel> model, @NotNull UiModelRetriever retriever) {
        final UiModel metadata = model.metadata();
        initMetadata(model, metadata, retriever);
    }

    /** Init inner models. */
    private static void initMetadata(@NotNull final Model model, Iterable<Widget> container, @NotNull UiModelRetriever retriever) {
        for (final Widget child : container) {
            if (child.getWidgetType() == SUBFORM) {
                // init subform
                final FormModel subform = model.getSubform(child);
                if (subform != null) {
                    init(subform, child.getSubformFqn(), retriever);
                    initMetadata(subform, retriever);
                }
            }
            else if (child.getWidgetType() == WIDGET) {
                // init widget definition
                final WidgetDefModel widget = model.getWidgetDef(child);
                if (widget != null) {
                    init(widget, child.getWidgetDefinitionFqn(), retriever);
                    widget.parent().ifPresent(p -> p.init(child));
                    initMetadata(widget, retriever);
                }
            }
            else if (isMultiple(child.getWidgetType())) {
                // change model context to row
                final MultipleModel multiple = model.getMultiple((MultipleWidget) child);
                if (multiple != null) {
                    for (int j = 0; j < multiple.size(); j++) {
                        final RowModel row = multiple.getRow(j);
                        initMetadata(row, child, retriever);
                    }
                }
            }
            else
            // recursive through groups
            initMetadata(model, child, retriever);
        }
    }
}  // end class FormHandling
