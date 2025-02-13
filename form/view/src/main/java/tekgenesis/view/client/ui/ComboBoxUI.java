
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

import static tekgenesis.metadata.form.model.FormConstants.BIG_COMBO;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;

/**
 * Combo UI widget.
 */
public class ComboBoxUI extends HasOptionsUI implements HasWidthUI {

    //~ Constructors .................................................................................................................................

    /** Creates Combo UI widget. */
    public ComboBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        final HasOptionsListBox element = model.getDefaultValueExpression() == Expression.NULL || !model.isRequired()
                                          ? new NullableHasOptionsListBox(false)
                                          : new HasOptionsListBox(false);
        init(element);
        element.addStyleName(FORM_CONTROL);
        ((HasOptionsListBox) component).setVisibleItemCount(1);
        ((HasOptionsListBox) component).addStyleName(BIG_COMBO);
    }

    //~ Methods ......................................................................................................................................

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("comboBox");
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}
