
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.DynamicFormBox.BodyOnlyFormBox;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.AnchoredSubformUI;

/**
 * Box for Subforms.
 */
public class SubformBox extends BodyOnlyFormBox {

    //~ Instance Fields ..............................................................................................................................

    private final AnchoredSubformUI anchored;

    //~ Constructors .................................................................................................................................

    SubformBox(@NotNull AnchoredSubformUI anchored, @NotNull final Model container, @NotNull final Widget metadata, @NotNull FormController current) {
        super(container, metadata, current);
        this.anchored = anchored;
    }

    //~ Methods ......................................................................................................................................

    /** Called when Subform is hiding. */
    public void onHide() {
        anchored.toggle();
        unRegister();
    }

    /** Called when Subform is showing. */
    public void onShow() {
        anchored.toggle();
        register();
        applyConfiguration();
    }
}
