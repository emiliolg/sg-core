
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * CheckBoxGroup UI widget.
 */
public class CheckBoxGroupUI extends HasOptionsUI {

    //~ Constructors .................................................................................................................................

    /** Creates RadioGroup UI widget. */
    public CheckBoxGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(new CheckBoxGroupComponent(!model.isRequired()));
    }

    //~ Methods ......................................................................................................................................

    @Override public void addMessage(@NotNull CheckMsg msg) {
        setCurrentMsg(msg);
        setInlineMessage(msg.getType(), msg.getText());
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(FormConstants.RADIO_GROUP);
    }

    /** Widget doesn't support icon. */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Inner Classes ................................................................................................................................

    private class CheckBoxGroupComponent extends FlowPanel implements HasOptionsComponent, ClickHandler, ValueChangeHandler<Boolean> {
        private final Anchor                clearAnchor;
        private final boolean               optional;
        private ValueChangeHandler<Boolean> valueChangeHandler = null;

        private CheckBoxGroupComponent(final boolean optional) {
            this.optional = optional;
            clearAnchor   = HtmlWidgetFactory.anchor(MSGS.clear());
            clearAnchor.addClickHandler(this);
            clearAnchor.setVisible(false);
            add(clearAnchor);
        }

        @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
            valueChangeHandler = cast(changeHandler);
        }

        @Override public void addOption(@NotNull String id, @NotNull String label) {
            final CheckBox checkBox = HtmlWidgetFactory.checkBox();
            checkBox.addStyleName(FormConstants.RADIO_OPTION);
            checkBox.addStyleName(FormConstants.FLOATING_MODAL);  // hack for checkbox not closing popups
            checkBox.setText(label);
            checkBox.addValueChangeHandler(this);
            add(checkBox);
            checkBox.setValue(uiSelectionContains(id));
            checkBox.getElement().setAttribute("data-key", id);
        }

        @Override public void clearOptions() {
            while (getWidgetCount() > 1)
                remove(1);
        }

        @Override public void clearSelection() {
            uiSelectionClear();
            setSelectedOptions(Colls.emptyList(), false);
        }

        /** Clear selected items. */
        @Override public void onClick(final ClickEvent event) {
            clearSelection();
            valueChangeHandler.onValueChange(null);
        }

        /** Checkbox selected. */
        @Override public void onValueChange(final ValueChangeEvent<Boolean> event) {
            final String id = getKey(((CheckBox) event.getSource()));
            uiSelectionUpdate(id, event.getValue());
            updateClearAnchor();
            valueChangeHandler.onValueChange(event);
        }

        @Override public void setFocus(boolean focus) {
            Widgets.setFocus(this, focus);
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean e) {
            for (final com.google.gwt.user.client.ui.Widget widget : this) {
                if (widget instanceof CheckBox) ((CheckBox) widget).setValue(ids.contains(getKey(widget)));
            }
            updateClearAnchor();
        }

        private void updateClearAnchor() {
            if (optional) clearAnchor.setVisible(!uiSelectionIsEmpty());
        }

        private String getKey(com.google.gwt.user.client.ui.Widget widget) {
            return widget.getElement().getAttribute("data-key");
        }
    }  // end class CheckBoxGroupComponent
}  // end class CheckBoxGroupUI
