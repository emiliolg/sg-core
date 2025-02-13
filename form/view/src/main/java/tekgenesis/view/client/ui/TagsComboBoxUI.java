
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
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.user.client.Event;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.base.Icon;

/**
 * A TagsComboBox UI widget.
 */
public class TagsComboBoxUI extends HasOptionsUI implements HasWidthUI, TagsComboBoxListener {

    //~ Instance Fields ..............................................................................................................................

    private final TagsComboBoxComponent element;

    //~ Constructors .................................................................................................................................

    /** Creates a TagsComboBox UI widget. */
    public TagsComboBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        element = new TagsComboBoxComponent(model.getName());
        element.setListener(this);
        init(element);
        element.addStyleName(FormConstants.FORM_CONTROL);
    }

    //~ Methods ......................................................................................................................................

    @Override public void focus(FocusEvent event) {
        if (getContext().getMultiple().isPresent()) {
            final MultipleUI multipleUI = getContext().getMultiple().get();
            if (multipleUI instanceof TableUI) {
                final TableUI table = (TableUI) multipleUI;
                RootInputHandler.getInstance().handleTableEvent(table, Event.as(event.getNativeEvent()));
            }
        }
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        element.setDisabled(disabled);
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.of(element.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(element, iconStyle, getModel().isExpand());
    }
}  // end class TagsComboBoxUI
