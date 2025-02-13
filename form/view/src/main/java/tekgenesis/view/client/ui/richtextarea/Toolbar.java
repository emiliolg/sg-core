
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.richtextarea;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.ui.base.DropdownButton;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalSubscription;

import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * RichTextArea Toolbar.
 */
@SuppressWarnings("ClassWithTooManyFields")
public class Toolbar extends Composite {

    //~ Instance Fields ..............................................................................................................................

    private ExtButton alignleft;
    private ExtButton alignmiddle;
    private ExtButton alignright;
    private ExtButton bold;
    private ExtButton breaklink;

    // We use an internal class of the ClickHandler and the KeyUpHandler to be private to others with these events
    private final EventHandler evHandler;

    private ExtButton        fontColor;
    private final FontPicker fontFamilyPicker;
    private final FontPicker fontSizePicker;

    private final ColorPicker foreColorsPicker;
    private ExtButton         generatelink;
    private ExtButton         indentleft;
    private ExtButton         indentright;
    private ExtButton         insertimage;
    private boolean           isHtml;
    private ExtButton         italic;
    private ExtButton         orderlist;

    private ExtButton removeformatting;
    private ExtButton stroke;

    // The RichTextArea this Toolbar referes to and the Interfaces to access the RichTextArea
    private final RichTextArea styleText;
    private final Formatter    styleTextFormatter;

    private final ValueChangeHandler<ColorPicker> colorHandler = new ValueChangeHandler<ColorPicker>() {
            public void onValueChange(ValueChangeEvent<ColorPicker> event) {
                final ColorPicker sender = event.getValue();
                styleTextFormatter.setForeColor(sender.getColor());
                sender.hide();
            }
        };

    private final ValueChangeHandler<FontPicker> fontHandler = new ValueChangeHandler<FontPicker>() {
            public void onValueChange(ValueChangeEvent<FontPicker> event) {
                final FontPicker sender = event.getValue();
                if (sender.getPickerType() == FontPicker.FontPickerType.FONT_FAMILY) styleTextFormatter.setFontName(sender.getFontName());
                else styleTextFormatter.setFontSize(sender.getFontSize());
            }
        };

    private String            modalString;
    private ModalSubscription subscription;
    private ExtButton         texthtml;
    private final FlowPanel   topPanel;
    private ExtButton         underline;
    private ExtButton         unorderlist;

    //~ Constructors .................................................................................................................................

    /** Constructor of the Toolbar.* */
    public Toolbar(RichTextArea richtext) {
        // Initialize the main-panel
        /* Private Variables.* */
        final FlowPanel outer = new FlowPanel();
        // Initialize modal for input
        // Initialize pickers
        fontFamilyPicker = new FontPicker(FontPicker.FontPickerType.FONT_FAMILY);
        fontSizePicker   = new FontPicker(FontPicker.FontPickerType.FONT_SIZE);
        foreColorsPicker = new ColorPicker();
        modalString      = null;
        subscription     = null;

        // Initialize the two inner panels
        topPanel = new FlowPanel();
        topPanel.setStyleName(CSS_ROOT_NAME);

        // Save the reference to the RichText area we refer to and get the interfaces to the stylings
        styleText          = richtext;
        styleTextFormatter = styleText.getFormatter();

        // Set some graphical options, so this toolbar looks how we like it.

        // Add the two inner panels to the main panel
        outer.add(topPanel);

        // Some graphical stuff to the main panel and the initialisation of the new widget
        outer.setWidth("100%");
        // outer.setStyleName(CSS_ROOT_NAME);
        initWidget(outer);

        evHandler = new EventHandler();
        isHtml    = false;

        // Add KeyUp and Click-Handler to the RichText, so that we can actualize the toolbar if neccessary
        styleText.addKeyUpHandler(evHandler);
        styleText.addClickHandler(evHandler);

        // Fill toolbar with buttons
        buildTools();
    }  // end ctor Toolbar

    //~ Methods ......................................................................................................................................

    /** Closes all popup buttons .* */
    public void closeAllButtons() {
        if (foreColorsPicker.isVisible()) foreColorsPicker.hide();
    }

    /** Public method with a more understandable name to get if HTML mode is on or not.* */
    public Boolean isHTMLMode() {
        return isHtml;
    }

    /** Initialize the options on the toolbar.* */
    private void buildTools() {
        // Init the TOP Panel first
        topPanel.add(bold = createButton(false, MSGS.bold(), IconType.BOLD));
        topPanel.add(italic = createButton(true, MSGS.italic(), IconType.ITALIC));
        topPanel.add(underline = createButton(true, MSGS.underline(), IconType.UNDERLINE));
        topPanel.add(createDropdown(IconType.FONT, MSGS.fonts(), fontFamilyPicker));
        topPanel.add(createDropdown(IconType.TEXT_HEIGHT, MSGS.colors(), fontSizePicker));
        topPanel.add(fontColor = createColorButton(MSGS.colors(), IconType.ADJUST));
        topPanel.add(stroke = createButton(true, MSGS.stroke(), IconType.STRIKETHROUGH, "toolbar-border-right"));
        topPanel.add(new HTML("&nbsp;"));
        topPanel.add(alignleft = createButton(false, MSGS.alignLeft(), IconType.ALIGN_LEFT, "toolbar-border-left"));
        topPanel.add(alignmiddle = createButton(false, MSGS.alignCenter(), IconType.ALIGN_CENTER));
        topPanel.add(alignright = createButton(false, MSGS.alignRight(), IconType.ALIGN_RIGHT, "toolbar-border-right"));
        topPanel.add(new HTML("&nbsp;"));
        topPanel.add(orderlist = createButton(false, MSGS.orderedList(), IconType.LIST_OL, "toolbar-border-left"));
        topPanel.add(unorderlist = createButton(false, MSGS.unorderedList(), IconType.LIST_UL, "toolbar-border-right"));
        topPanel.add(new HTML("&nbsp;"));
        topPanel.add(indentright = createButton(false, MSGS.indentRight(), IconType.INDENT, "toolbar-border-left"));
        topPanel.add(indentleft = createButton(false, MSGS.indentLeft(), IconType.OUTDENT, "toolbar-border-right"));
        topPanel.add(new HTML("&nbsp;"));
        topPanel.add(generatelink = createButton(false, MSGS.generateLink(), IconType.LINK, "toolbar-border-left"));
        topPanel.add(breaklink = createButton(false, MSGS.breakLink(), IconType.CHAIN_BROKEN));
        topPanel.add(insertimage = createButton(false, MSGS.insertImage(), IconType.PICTURE_O, "toolbar-border-right"));
        topPanel.add(new HTML("&nbsp;"));
        topPanel.add(removeformatting = createButton(false, MSGS.removeFormatting(), IconType.TIMES, "toolbar-border-left"));
        topPanel.add(texthtml = createButton(true, MSGS.changeView(), IconType.PENCIL_SQUARE_O));

        foreColorsPicker.addValueChangeHandler(colorHandler);
        fontFamilyPicker.addValueChangeHandler(fontHandler);
        fontSizePicker.addValueChangeHandler(fontHandler);
    }

    /** Method called to toggle the style in HTML-Mode.* */
    private void changeHtmlStyle(String startTag, String stopTag) {
        final JsArrayString tx           = getSelection(styleText.getElement());
        final String        txbuffer     = styleText.getText();
        final Integer       startpos     = Integer.parseInt(tx.get(1));
        final String        selectedText = tx.get(0);
        styleText.setText(txbuffer.substring(0, startpos) + startTag + selectedText + stopTag + txbuffer.substring(startpos + selectedText.length()));
    }

    /** Method to create a Button for the toolbar.* */
    private ExtButton createButton(Boolean isToggle, String tip, IconType iconType) {
        return createButton(isToggle, tip, iconType, null);
    }

    private ExtButton createButton(Boolean isToggle, String tip, IconType iconType, String className) {
        final ExtButton tb = new ExtButton();
        if (className != null) tb.addStyleName(className);
        tb.setIcon(iconType);
        tb.addClickHandler(evHandler);
        if (tip != null) tb.setTitle(tip);
        tb.getElement().setTabIndex(-2);
        return tb;
    }

    /** Method to create a ColorButton for the toolbar.* */
    private ExtButton createColorButton(String tip, IconType iconType) {
        final ExtButton tb = new ExtButton();
        tb.setIcon(iconType);
        tb.setCaret();
        tb.addClickHandler(evHandler);
        if (tip != null) tb.setTitle(tip);
        tb.getElement().setTabIndex(-2);
        return tb;
    }

    private DropdownButton createDropdown(IconType iconType, String tip, FontPicker fonts) {
        final DropdownButton button = new DropdownButton();
        button.setIcon(iconType);
        button.setCaret();
        for (final Anchor font : fonts.getFontList())
            button.add(font);
        if (tip != null) button.setTitle(tip);
        button.getFocusTarget().setTabIndex(-2);
        return button;
    }

    //~ Methods ......................................................................................................................................

    /** Native JavaScript that returns the selected text and position of the start.* */
    public static native JsArrayString getSelection(Element elem)  /*-{
                                                                var txt = "";
                                                                var pos = 0;
                                                                var range;
                                                                var parentElement;
                                                                var container;
    
                                                                if (elem.contentWindow.getSelection) {
                                                                txt = elem.contentWindow.getSelection();
                                                                pos = elem.contentWindow.getSelection().getRangeAt(0).startOffset;
                                                                } else if (elem.contentWindow.document.getSelection) {
                                                                txt = elem.contentWindow.document.getSelection();
                                                                pos = elem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
                                                                } else if (elem.contentWindow.document.selection) {
                                                                range = elem.contentWindow.document.selection.createRange();
                                                                txt = range.text;
                                                                parentElement = range.parentElement();
                                                                container = range.duplicate();
                                                                container.moveToElementText(parentElement);
                                                                container.setEndPoint('EndToEnd', range);
                                                                pos = container.text.length - range.text.length;
                                                                }
                                                                return [""+txt,""+pos];
    }-*/;

    //~ Static Fields ................................................................................................................................

    /** Local CONSTANTS.* */
    // ImageMap and CSS related
    private static final String CSS_ROOT_NAME = "richText-toolbar";

    // HTML Related (styles made by SPAN and DIV)
    private static final String HTML_STYLE_CLOSE_SPAN       = "</span>";
    private static final String HTML_STYLE_CLOSE_DIV        = "</div>";
    private static final String HTML_STYLE_OPEN_BOLD        = "<span style=\"font-weight: bold;\">";
    private static final String HTML_STYLE_OPEN_ITALIC      = "<span style=\"font-weight: italic;\">";
    private static final String HTML_STYLE_OPEN_UNDERLINE   = "<span style=\"font-weight: underline;\">";
    private static final String HTML_STYLE_OPEN_LINETHROUGH = "<span style=\"font-weight: line-through;\">";
    private static final String HTML_STYLE_OPEN_ALIGNLEFT   = "<div style=\"text-align: left;\">";
    private static final String HTML_STYLE_OPEN_ALIGNCENTER = "<div style=\"text-align: center;\">";
    private static final String HTML_STYLE_OPEN_ALIGNRIGHT  = "<div style=\"text-align: right;\">";
    private static final String HTML_STYLE_OPEN_INDENTRIGHT = "<div style=\"margin-left: 40px;\">";

    // HTML Related (styles made by custom HTML-Tags)
    private static final String HTML_STYLE_OPEN_ORDERLIST    = "<ol><li>";
    private static final String HTML_STYLE_CLOSE_ORDERLIST   = "</ol></li>";
    private static final String HTML_STYLE_OPEN_UNORDERLIST  = "<ul><li>";
    private static final String HTML_STYLE_CLOSE_UNORDERLIST = "</ul></li>";

    //~ Enums ........................................................................................................................................

    /**
     * Modal Types.*
     */
    enum ModalType { LINK, IMAGE }

    //~ Inner Classes ................................................................................................................................

    /**
     * Click Handler of the Toolbar.*
     */
    private class EventHandler implements ClickHandler, KeyUpHandler {
        @SuppressWarnings(
                {
                    "StatementWithEmptyBody", "IfStatementWithTooManyBranches", "OverlyLongMethod", "OverlyComplexMethod",
                    "MagicNumber"
                }
                         )
        public void onClick(ClickEvent event) {
            event.stopPropagation();
            final ExtButton source = (ExtButton) event.getSource();
            source.toggle();
            if (source.equals(bold)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_BOLD, HTML_STYLE_CLOSE_SPAN);
                else styleTextFormatter.toggleBold();
            }
            else if (source.equals(italic)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_ITALIC, HTML_STYLE_CLOSE_SPAN);
                else styleTextFormatter.toggleItalic();
            }
            else if (source.equals(underline)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_UNDERLINE, HTML_STYLE_CLOSE_SPAN);
                else styleTextFormatter.toggleUnderline();
            }
            else if (source.equals(stroke)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_LINETHROUGH, HTML_STYLE_CLOSE_SPAN);
                else styleTextFormatter.toggleStrikethrough();
            }
            else if (source.equals(alignleft)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_ALIGNLEFT, HTML_STYLE_CLOSE_DIV);
                else styleTextFormatter.setJustification(RichTextArea.Justification.LEFT);
            }
            else if (source.equals(alignmiddle)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_ALIGNCENTER, HTML_STYLE_CLOSE_DIV);
                else styleTextFormatter.setJustification(RichTextArea.Justification.CENTER);
            }
            else if (source.equals(alignright)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_ALIGNRIGHT, HTML_STYLE_CLOSE_DIV);
                else styleTextFormatter.setJustification(RichTextArea.Justification.RIGHT);
            }
            else if (source.equals(orderlist)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_ORDERLIST, HTML_STYLE_CLOSE_ORDERLIST);
                else styleTextFormatter.insertOrderedList();
            }
            else if (source.equals(unorderlist)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_UNORDERLIST, HTML_STYLE_CLOSE_UNORDERLIST);
                else styleTextFormatter.insertUnorderedList();
            }
            else if (source.equals(indentright)) {
                if (isHTMLMode()) changeHtmlStyle(HTML_STYLE_OPEN_INDENTRIGHT, HTML_STYLE_CLOSE_DIV);
                else styleTextFormatter.rightIndent();
            }
            else if (source.equals(indentleft)) {
                if (isHTMLMode()) {
                    // TODO nothing can be done here at the moment
                }
                else styleTextFormatter.leftIndent();
            }
            else if (source.equals(generatelink)) {
                event.stopPropagation();
                subscription = Application.modal(new ToolbarModal(ModalType.LINK));
            }
            else if (source.equals(breaklink)) {
                if (isHTMLMode()) {
                    // TODO nothing can be done here at the moment
                }
                else styleTextFormatter.removeLink();
            }
            else if (source.equals(insertimage)) {
                event.stopPropagation();
                subscription = Application.modal(new ToolbarModal(ModalType.IMAGE));
            }
            else if (source.equals(removeformatting)) {
                if (isHTMLMode()) {
                    // TODO nothing can be done here at the moment
                }
                else styleTextFormatter.removeFormat();
            }
            else if (source.equals(texthtml)) {
                if (!isHtml) {
                    isHtml = true;
                    styleText.setText(styleText.getHTML());
                }
                else {
                    isHtml = false;
                    styleText.setHTML(styleText.getText());
                }
            }
            else if (source.equals(fontColor)) {
                foreColorsPicker.setPopupPosition(fontColor.getAbsoluteLeft(), fontColor.getAbsoluteTop() + 30);
                foreColorsPicker.show();
            }
            updateStatus();
        }

        public void onKeyUp(KeyUpEvent event) {
            updateStatus();
        }

        /**
         * Private method to set the toggle buttons and disable/enable buttons which do not work in
         * html-mode.*
         */
        private void updateStatus() {
            if (isHTMLMode()) removeformatting.setEnabled(false);
            else removeformatting.setEnabled(true);
        }
    }  // end class EventHandler

    private class ToolbarModal extends ModalContent {
        public ToolbarModal(ModalType type) {
            final TextBox   urlBox   = new TextBox();
            final ExtButton addButon = new ExtButton("Add");
            addButon.addStyleName(FormConstants.BTN_PRIMARY);

            switch (type) {
            case LINK:
                setTitle("Insert Link Url");
                addButon.setIcon(IconType.LINK);
                addButon.addClickHandler(event -> {
                    modalString = urlBox.getValue();
                    if (modalString != null) {
                        if (isHTMLMode()) changeHtmlStyle("<a href=\"" + modalString + "\">", "</a>");
                        else styleTextFormatter.createLink(modalString);
                        modalString = null;
                        subscription.hide();
                    }
                });

                break;
            case IMAGE:
                setTitle("Insert Image Url");
                addButon.setIcon(IconType.PICTURE_O);
                addButon.addClickHandler(event -> {
                    modalString = urlBox.getValue();
                    if (modalString != null) {
                        if (isHTMLMode()) changeHtmlStyle("<img src=\"" + modalString + "\">", "");
                        else styleTextFormatter.insertImage(modalString);
                        modalString = null;
                        subscription.hide();
                    }
                });

                break;
            }
            urlBox.setText("http://");
            setBody(urlBox);
            setFooter(addButon);
            urlBox.setFocus(true);
        }  // end ctor ToolbarModal
    }  // end class ToolbarModal
}  // end class Toolbar
