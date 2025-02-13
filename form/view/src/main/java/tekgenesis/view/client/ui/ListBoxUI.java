
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;

/**
 * A ListBox UI widget.
 */
public class ListBoxUI extends HasOptionsUI {

    //~ Constructors .................................................................................................................................

    /** Creates a ListBox UI widget. */
    public ListBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(
            model.getDefaultValueExpression() == Expression.NULL || !model.isRequired() ? new NullableHasOptionsListBox(model.isMultiple())
                                                                                        : new HasOptionsListBox(model.isMultiple()));
    }

    //~ Methods ......................................................................................................................................

    /** Sets the number of visible rows. */
    public void setVisibleItemCount(int rows) {
        ((HasOptionsListBox) component).setVisibleItemCount(rows);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("listBox");
        ((HasOptionsListBox) component).addStyleName(FORM_CONTROL);
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}
