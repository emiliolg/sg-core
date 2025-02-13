
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.richtextarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RichTextArea.FontSize;

import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

/**
 * Font picker.
 */
public class FontPicker implements ClickHandler, HasValueChangeHandlers<FontPicker> {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<FontPicker> changeHandler = null;

    private String font = "";

    private final List<Anchor> fontList;

    private final FontPickerType pickerType;

    //~ Constructors .................................................................................................................................

    /** Returns a FontPicker with its type. */
    public FontPicker(FontPickerType type) {
        Collections.addAll(fontSizes, fontSizesArray);

        pickerType = type;

        fontList = new ArrayList<>();

        final String[] fonts = type == FontPickerType.FONT_SIZE ? fontSizesArray : fontFamilies;

        for (final String currentFont : fonts) {
            final Anchor fontLink = HtmlWidgetFactory.anchor(currentFont);

            final Element element = fontLink.getElement();
            if (type == FontPickerType.FONT_SIZE) element.getStyle().setProperty("fontSize", currentFont);
            else element.getStyle().setProperty("fontFamily", currentFont);
            fontLink.addClickHandler(this);

            fontList.add(fontLink);
        }
    }

    //~ Methods ......................................................................................................................................

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<FontPicker> handler) {
        assert changeHandler == null : "Change handler defined";
        changeHandler = handler;
        return () -> changeHandler = null;
    }

    @Override public void fireEvent(GwtEvent<?> event) {
        // todo something
    }

    public void onClick(ClickEvent event) {
        font = ((Anchor) event.getSource()).getText();
        if (changeHandler != null) changeHandler.onValueChange(new ValueChangeEvent<FontPicker>(this) {});
    }

    /** Returns fontList. */
    public List<Anchor> getFontList() {
        return fontList;
    }

    /** Returns Font. */
    public String getFontName() {
        return font;
    }

    /** Returns FontSize . */
    public FontSize getFontSize() {
        switch (fontSizes.indexOf(font)) {
        case 0:
            return FontSize.XX_SMALL;
        case 1:
            return FontSize.X_SMALL;
        case 3:
            return FontSize.LARGE;
        case 4:
            return FontSize.X_LARGE;
        case 5:
            return FontSize.XX_LARGE;
        default:
            return FontSize.MEDIUM;
        }
    }

    /** Returns Picker Type. */
    public FontPickerType getPickerType() {
        return pickerType;
    }

    //~ Static Fields ................................................................................................................................

    private static final String[] fontFamilies = {
        "Times New Roman", "Arial", "Courier New", "Georgia", "Trebuchet", "Verdana", "Comic Sans MS", "Calibri"
    };

    private static final String[] fontSizesArray = { "x-small", "small", "medium", "large", "x-large", "xx-large" };

    private static final List<String> fontSizes = new ArrayList<>();

    //~ Enums ........................................................................................................................................

    public enum FontPickerType { FONT_FAMILY, FONT_SIZE }
}  // end class FontPicker
