
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.SubformBox;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

/**
 * A anchor UI widget. Just a ´div´.
 */
public class AnchorUI extends WidgetUI {

    //~ Instance Fields ..............................................................................................................................

    private SubformBox current;

    private final FlowPanel placeholder = HtmlWidgetFactory.div();

    //~ Constructors .................................................................................................................................

    /** Creates a Label UI widget. */
    public AnchorUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        current = null;
        initWidget(placeholder);
    }

    //~ Methods ......................................................................................................................................

    /** Attach box to placeholder. */
    public void attach(SubformBox box) {
        detach();
        box.attach(placeholder);
        box.onShow();
        current = box;
    }

    /** Detach box from placeholder. */
    public void detach() {
        if (current != null) {
            current.detach();
            current.onHide();
            current = null;
        }
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("formAnchor");
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}  // end class AnchorUI
