
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.GRID;

/**
 * InputGroup UI widget.
 */
public class InputGroupUI extends ContainerUI {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel panel;

    //~ Constructors .................................................................................................................................

    /** Creates a Group UI widget. */
    public InputGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        panel = HtmlWidgetFactory.div();
        panel.addStyleName("input-group-container");
        if (model.isFlow()) panel.addStyleName("col-sm-12");
        div.addStyleName(FieldWidgetUI.FORM_GROUP);
        div.add(panel);
    }

    //~ Methods ......................................................................................................................................

    @Override void addChildPanel(WidgetUI w) {
        // w.addStyleName(FormConstants.NO_LABEL);
        if (w instanceof FieldWidgetUI) {
            final FieldWidgetUI ui = (FieldWidgetUI) w;
            ui.setInputWidth(0);
            ui.removeStyleName(FieldWidgetUI.FORM_GROUP);
            // ui.setLabelWidth(0); hide label?
        }
        panel.add(w);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        if (getModel().isFlow()) div.addStyleName("flow-group");
        if (getModel().isTopLabel()) addStyleName(FormConstants.VERTICAL_LABEL_FIELD);
    }

    @NotNull @Override Option<Element> createLabel() {
        final Widget       model = getModel();
        final LabelElement label = Document.get().createLabelElement();
        label.setInnerText(model.getLabel());
        div.getElement().insertFirst(label);
        final int labelCol = model.getLabelCol();
        final int col      = labelCol > 0 ? labelCol : 2;
        setLabelWidth(label, col);
        panel.removeStyleName("col-sm-12");
        panel.addStyleName("padding-0 col-sm-" + (GRID - col));
        return some(label);
    }
}  // end class InputGroupUI
