
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
import com.google.gwt.user.client.ui.CheckBox;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

/**
 * Checkbox UI widget.
 */
public class CheckBoxUI extends BaseHasScalarValueUI implements HasClickUI {

    //~ Instance Fields ..............................................................................................................................

    private final CheckBox checkBox;

    //~ Constructors .................................................................................................................................

    /** Create a Checkbox UI widget. */
    public CheckBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(checkBox = HtmlWidgetFactory.checkBox(), false);
    }

    //~ Methods ......................................................................................................................................

    @Override public void click() {
        setValue(!checkBox.getValue(), true);
    }

    public void setPlaceholder(String placeholder) {
        checkBox.setText(placeholder);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("checkBox");
    }

    // ** Widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}
