
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.richtextarea;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.TextField;

import static tekgenesis.common.core.Strings.verifyHexColor;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.textField;

/**
 * Color picker.
 */
@SuppressWarnings("MagicNumber")
public class ColorPicker extends PopupPanel implements ClickHandler, HasValueChangeHandlers<ColorPicker> {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<ColorPicker> changeHandler = null;
    private final ColorCell                 colorSample;

    private String          colorSelected;
    private String          colorShown                    = "";
    private final Label     hashTag;
    private final TextField hexText;
    private final FlowPanel hexValue;
    private final FlowPanel parent;

    //~ Constructors .................................................................................................................................

    /** Creates a ColorPickerUI. */
    public ColorPicker() {
        super(true);
        parent = HtmlWidgetFactory.div();
        parent.addStyleName(INPUT_PREPEND);
        getElement().getStyle().setBackgroundColor(ColorCell.WHITE);
        final FlexTable colorPallet = new FlexTable();
        colorSelected = colorShown;

        // Elements for the hexadecimal Sample
        hashTag = new Label();
        hashTag.addStyleName(INPUT_GROUP_ADDON);
        hashTag.setText("#");
        colorSample = new ColorCell("");
        colorSample.addStyleName(INPUT_GROUP_ADDON);
        hexValue = HtmlWidgetFactory.div();
        hexValue.addStyleName(INPUT_GROUP + " color-input");

        // Pallet table
        colorPallet.setCellPadding(0);
        colorPallet.setCellSpacing(0);
        // colorPallet.getElement().getStyle().setProperty(FormConstants.BORDER, "1px solid #cccccc");

        int i = 0;
        for (int r = 0; i < COLORS.length; r++) {
            for (int c = 0; c < 7 && i < COLORS.length; c++, i++) {
                final ColorCell cell = new ColorCell(COLORS[i]);
                cell.addClickHandler(this);
                colorPallet.setWidget(r, c, cell);
            }
        }

        // TextField containing the hexadecimal value of the color
        hexText = textField();
        hexText.addStyleName(FORM_CONTROL);
        hexText.addChangeHandler(event -> onKeyPress());

        hexText.addKeyUpHandler(event -> {
            if (verifyHexColor("#" + hexText.getText())) onKeyPress();
        });

        // Add the both of the tables to the parent one
        parent.add(colorPallet);
        setHexSample(colorSelected);
        add(parent);
    }  // end ctor ColorPicker

    //~ Methods ......................................................................................................................................

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ColorPicker> handler) {
        assert changeHandler == null : "Change handler is already defined";
        changeHandler = handler;
        return () -> changeHandler = null;
    }

    public void onClick(ClickEvent event) {
        final ColorCell cell = (ColorCell) event.getSource();
        colorSelected = cell.getColor();
        setHexSample(colorSelected);
        if (changeHandler != null) changeHandler.onValueChange(new ValueChangeEvent<ColorPicker>(this) {});
    }

    /** Returns color. */
    public String getColor() {
        return colorSelected;
    }

    /** To handle when the hexadecimal text is changed. */
    private void onKeyPress() {
        final String textInserted = "#" + hexText.getText();
        if (verifyHexColor(textInserted)) {
            colorSelected = textInserted;
            colorShown    = colorSelected;
            setHexSample(colorSelected);
        }
        if (changeHandler != null) changeHandler.onValueChange(new ValueChangeEvent<ColorPicker>(this) {});
    }

    /** Receives a String representing an hexadecimal color and sets that value to the sample. */
    private void setHexSample(String color) {
        final String simpleValues = color.contains("#") ? color.substring(1, color.length()) : color;
        hexText.setText(simpleValues);

        colorSample.setColor(simpleValues);

        hexValue.add(hashTag);
        hexValue.add(hexText);
        hexValue.add(colorSample);

        parent.add(hexValue);
        parent.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
    }

    //~ Static Fields ................................................................................................................................

    private static final long[] COLORS = {
        0xffffff, 0xcccccc, 0xc0c0c0, 0x999999, 0x666666, 0x333333, 0x000000,
        0xffcccc, 0xff6666, 0xff0000, 0xcc0000, 0x990000, 0x660000, 0x330000,
        0xffcc99, 0xff9966, 0xff9900, 0xfd6500, 0xcb6500, 0x983200, 0x653200,
        0xffff99, 0xffff66, 0xffcc66, 0xfdcb32, 0xcb9832, 0x986532, 0x653232,
        0xffffcc, 0xffff33, 0xffff00, 0xfdcb00, 0x989800, 0x656500, 0x323200,
        0x99ff99, 0x66ff99, 0x33ff33, 0x32cb00, 0x009800, 0x006500, 0x003200,
        0x99ffff, 0x33ffff, 0x66cccc, 0x00cbcb, 0x329898, 0x326565, 0x003232,
        0xccffff, 0x66ffff, 0x33ccff, 0x3265fd, 0x3232fd, 0x000098, 0x000065,
        0xccccff, 0x9999ff, 0x6666cc, 0x6532fd, 0x6500cb, 0x323298, 0x320098,
        0xffccff, 0xff99ff, 0xcc66cc, 0xcb32cb, 0x983298, 0x653265, 0x320032,
    };

    private static final String borderColor = "#cccccc";

    //~ Inner Classes ................................................................................................................................

    public class ColorCell extends Label {
        String rgbColor;

        /** ColorCell constructor from long color value. */
        public ColorCell(long color) {
            this(Long.toHexString(color));
        }

        /** ColorCell constructor from String color value. */
        public ColorCell(String color) {
            super();
            setColor(color);
            setTitle(rgbColor);
            setSize("14px", "14px");
            getElement().getStyle().setBackgroundColor(rgbColor);
            setBorderColor(borderColor);
            addMouseOverHandler(event -> {
                setBorderColor(WHITE);
                colorShown = getColor();
                setHexSample(colorShown);
            });
            addMouseOutHandler(event -> {
                setBorderColor(borderColor);
                setHexSample(colorSelected);
            });
        }

        /** Sets the border color with the given parameter. */
        public void setBorderColor(String color) {
            getElement().getStyle().setProperty(FormConstants.BORDER, "1px solid " + color);
        }

        /** Returns the color selected from the cells. */
        public String getColor() {
            return rgbColor;
        }

        private void setColor(final String s) {
            String result = s;
            if (result.length() == 3) {
                String aux = "";
                for (int i = 0; i < 3; i++)
                    aux = aux + result.charAt(i) + result.charAt(i);
                result = aux;
            }
            while (result.length() < 6)
                result = "0" + result;
            rgbColor = "#" + result;
            getElement().getStyle().setBackgroundColor(rgbColor);
        }

        @SuppressWarnings("DuplicateStringLiteralInspection")
        public static final String WHITE = "#ffffff";
    }  // end class ColorCell
}  // end class ColorPicker
