
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
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.model.FormConstants.GRID;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * An inline subform UI widget.
 */
public class InlineSubformUI extends FieldWidgetUI implements SubformUI {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel inlineAnchor = div();
    private FormModel       subformModel = null;

    //~ Constructors .................................................................................................................................

    /** UI Constructor. */
    public InlineSubformUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model, true);
        initWidget(inlineAnchor);
    }

    //~ Methods ......................................................................................................................................

    /** Get where to anchor subform if inline option is on. */
    public FlowPanel getInlineAnchor() {
        return inlineAnchor;
    }

    @Override public void setInputWidth(int col) {
        super.setInputWidth(GRID);
    }

    /** Get subform path. */
    public String getPath() {
        return subformModel != null ? subformModel.getPath() : "";
    }

    /** Get form model corresponding to subform. */
    public FormModel getSubformModel() {
        return subformModel;
    }

    /** Set form model corresponding to subform. */
    public void setSubformModel(FormModel subformModel) {
        this.subformModel = subformModel;
    }

    @NotNull @Override Option<Element> createIcon() {
        return empty();
    }
}  // end class InlineSubformUI
