
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.UUID;
import tekgenesis.metadata.form.configuration.RadioGroupConfig;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.configuration.RadioGroupConfig.DEFAULT;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * RadioGroup UI widget.
 */
public class RadioGroupUI extends HasOptionsUI {

    //~ Constructors .................................................................................................................................

    /** Creates RadioGroup UI widget. */
    public RadioGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(new RadioGroupComponent(model.getName(), !model.isRequired()));
    }

    //~ Methods ......................................................................................................................................

    public void applyConfig(@Nullable RadioGroupConfig c) {
        ((RadioGroupComponent) component).applyConfig(notNull(c, DEFAULT));
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(FormConstants.RADIO_GROUP);
    }

    /** This widget doesn't support icon. */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Inner Classes ................................................................................................................................

    private class RadioGroupComponent extends FlowPanel implements HasOptionsComponent, ClickHandler, ValueChangeHandler<Boolean> {
        private final Anchor                clearAnchor;
        private final boolean               optional;
        private String                      radioName;
        private final List<RadioButton>     radios             = new ArrayList<>();
        private ValueChangeHandler<Boolean> valueChangeHandler = null;

        RadioGroupComponent(String radioName, final boolean optional) {
            this.radioName = UUID.generateUUIDString();
            this.optional  = optional;
            clearAnchor    = HtmlWidgetFactory.anchor(MSGS.clear());
            clearAnchor.addClickHandler(this);
            clearAnchor.setVisible(false);
            add(clearAnchor);
        }

        @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
            valueChangeHandler = cast(changeHandler);
        }

        @Override public void addOption(@NotNull String id, @NotNull String label) {
            final RadioButton radioButton = HtmlWidgetFactory.radio(radioName, label);
            radioButton.addStyleName(FormConstants.RADIO_OPTION);
            radioButton.addStyleName(FormConstants.FLOATING_MODAL);  // hack for radios not closing popups
            radioButton.addValueChangeHandler(this);
            radioButton.setValue(uiSelectionContains(id));
            radioButton.getElement().setAttribute("data-key", id);
            radios.add(radioButton);
            add(radioButton);
        }

        public void applyConfig(RadioGroupConfig c) {
            final List<String> cs = c.getStyleClasses();
            if (!cs.isEmpty()) {
                for (int i = 0; i < radios.size(); i++)
                    radios.get(i).addStyleName(cs.get(i < cs.size() ? i : 0));
            }
            radios.clear();
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

        @Override public void onValueChange(ValueChangeEvent<Boolean> event) {
            if (event.getValue()) {
                uiSelectionClear();
                final String id = getKey((RadioButton) event.getSource());
                uiSelectionUpdate(id, true);
                valueChangeHandler.onValueChange(event);
                updateClearAnchor();
            }
        }

        @Override public void setFocus(boolean focus) {
            Widgets.setFocus(this, focus);
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean e) {
            for (final com.google.gwt.user.client.ui.Widget widget : this) {
                if (widget instanceof CheckBox) ((CheckBox) widget).setValue(ids.contains(getKey(widget)));
            }
            updateClearAnchor();

            if (e) DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
        }

        void setRadioName(final String radioName) {
            if (!Predefined.equal(radioName, this.radioName)) {
                this.radioName = radioName;
                for (final com.google.gwt.user.client.ui.Widget widget : this)
                    if (widget instanceof RadioButton) ((RadioButton) widget).setName(radioName);
            }
        }

        private void updateClearAnchor() {
            if (optional) clearAnchor.setVisible(!uiSelectionIsEmpty());
        }

        private String getKey(com.google.gwt.user.client.ui.Widget widget) {
            return widget.getElement().getAttribute("data-key");
        }
    }  // end class RadioGroupComponent
}  // end class RadioGroupUI
