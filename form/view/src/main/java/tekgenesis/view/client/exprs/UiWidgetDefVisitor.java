
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.ModelUiVisitor;
import tekgenesis.view.client.ui.MultipleUI;
import tekgenesis.view.client.ui.WidgetDefUI;
import tekgenesis.view.client.ui.WidgetUIFinder;

import static tekgenesis.common.Predefined.min;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.view.client.controller.ViewCreator.createView;

/**
 * View visitor to create optional widget definitions view.
 */
public class UiWidgetDefVisitor implements ModelUiVisitor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final FormController controller;

    @NotNull private final Model local;

    //~ Constructors .................................................................................................................................

    /** Default {@link UiWidgetDefVisitor ui widget definition visitor} constructor. */
    UiWidgetDefVisitor(@NotNull final FormController controller, @NotNull final WidgetUIFinder finder, @NotNull final Model local) {
        this.controller = controller;
        this.local      = local;
    }

    //~ Methods ......................................................................................................................................

    @Override public void visit(MultipleUI ui) {
        final MultipleModel multiple = local.getMultiple(ui.getMultipleModel());

        for (int section = 0; section < min(multiple.size(), ui.getSectionsCount()); section++)
            traverse(ui.getSection(section));
    }

    @Override public void visit(WidgetDefUI widget) {
        final Widget         w = widget.getModel();
        final WidgetDefModel m = resolveModel(local, w, widget.getContext().getItem()).getWidgetDef(w);
        if (m != null) {
            if (widget.isEmpty()) {
                createView(m.metadata(), widget, local.isUpdate());  // Create view (for optional widgets)
                widget.initialized();
                new UiLoadVisitor(widget.finder(), m).traverse(widget);
            }
            new UiWidgetDefVisitor(controller, widget.container().finder(), m).traverse(widget);
        }
    }
}
