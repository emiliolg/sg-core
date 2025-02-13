
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
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.ClickablePanel;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.TextField;
import tekgenesis.view.client.ui.richtextarea.ColorPicker;

import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Strings.verifyHexColor;
import static tekgenesis.metadata.form.model.FormConstants.INPUT_GROUP;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.clickableDiv;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.textField;

/**
 * A ColorPicker UI widget.
 */
public class ColorPickerUI extends BaseHasScalarValueUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final ColorPickerBox box;

    //~ Constructors .................................................................................................................................

    /** Creates a ColorPicker UI widget. */
    public ColorPickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        box = new ColorPickerBox();
        init(box, false);
        setFocusTarget(ofNullable(box.getTextField()));
        box.addStyleName(INPUT_GROUP);
    }

    //~ Methods ......................................................................................................................................

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        box.setDisabled(disabled);
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Inner Classes ................................................................................................................................

    private class ColorPickerBox extends FlowPanel implements HasValue<String> {
        private final ClickablePanel colorBox;
        private final ColorPicker    colorPicker;
        private final Style          style;
        private final TextField      textField;

        private String value = "";

        private ColorPickerBox() {
            colorBox = clickableDiv();
            colorBox.addStyleName(FormConstants.INPUT_GROUP_ADDON);
            Icon.inWidget(colorBox.getElement(), IconType.CARET_DOWN.getClassName());

            textField = textField();
            textField.addStyleName(FormConstants.FORM_CONTROL);
            textField.setReadOnly(true);

            style = textField.getElement().getStyle();

            colorPicker = new ColorPicker();
            // clearStyleName(colorPicker);

            colorBox.addClickHandler(event -> colorPicker.showRelativeTo(colorBox));

            colorPicker.addValueChangeHandler(event -> setValue(colorPicker.getColor(), true));

            textField.addValueChangeHandler(event -> setValue(textField.getText(), true));

            add(textField);
            add(colorBox);
        }

        @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
            return addHandler(handler, ValueChangeEvent.getType());
        }

        public void setDisabled(boolean disabled) {
            if (disabled) addStyleName(COLOR_DISABLE);
            else removeStyleName(COLOR_DISABLE);
        }

        /** Methods. */

        @Override public String getValue() {
            return value;
        }

        @Override public void setValue(String v) {
            setValue(v, false);
        }

        @Override public void setValue(String v, boolean fireEvents) {
            if (verifyHexColor(v)) {
                style.setBackgroundColor(v);
                value = v;
            }
            else value = null;

            if (fireEvents) ValueChangeEvent.fire(this, value);
        }

        TextField getTextField() {
            return textField;
        }

        static final String COLOR_DISABLE = "color-disable";
    }  // end class ColorPickerBox
}  // end class ColorPickerUI
