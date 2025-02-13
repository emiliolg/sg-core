
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.*;

import static tekgenesis.metadata.form.model.FormConstants.BTN_DEFAULT;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clearStyleName;

/**
 * A factory to centralize the creation of GWT widgets that replaces the default class name.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class HtmlWidgetFactory {

    //~ Constructors .................................................................................................................................

    private HtmlWidgetFactory() {}

    //~ Methods ......................................................................................................................................

    /** Creates a GWT anchor widget replacing its default class name. */
    public static Anchor anchor() {
        final Anchor a = new Anchor(true);
        clearStyleName(a);
        return a;
    }

    /** Creates a GWT anchor widget replacing its default class name. */
    public static Anchor anchor(final String text) {
        final Anchor a = new Anchor(text);
        clearStyleName(a);
        return a;
    }

    /** Creates a GWT fieldSet widget. */
    public static CustomPanel anchorPanel() {
        return new CustomPanel(Document.get().createAnchorElement());
    }

    /** Creates a GWT button widget replacing its default class name. */
    public static Button button() {
        final Button btn = new Button();
        btn.setStyleName(BUTTON_STYLE_NAME);
        btn.addStyleName(BTN_DEFAULT);
        return btn;
    }

    /** Creates a GWT button widget replacing its default class name. */
    public static Button button(final String text) {
        final Button btn = new Button(text);
        btn.setStyleName(BUTTON_STYLE_NAME);
        btn.addStyleName(BTN_DEFAULT);
        return btn;
    }

    /** Creates a GWT checkBox widget replacing its default class name. */
    public static CheckBox checkBox() {
        final CheckBox checkBox = new CheckBox();
        clearStyleName(checkBox);
        return checkBox;
    }

    /** Creates a clickable GWT Div widget replacing its default class name. */
    public static ClickablePanel clickableDiv() {
        final ClickablePanel div = new ClickablePanel();
        clearStyleName(div);
        return div;
    }

    /** Creates a GWT Div widget replacing its default class name. */
    public static FlowPanel div() {
        final FlowPanel div = new FlowPanel();
        clearStyleName(div);
        return div;
    }

    /** Creates a GWT fieldSet widget. */
    public static CustomPanel fieldSet() {
        return new CustomPanel(Document.get().createFieldSetElement());
    }

    /** Creates a frame widget. */
    public static Frame frame(final String url) {
        final Frame frame = new Frame(url);
        clearStyleName(frame);
        return frame;
    }

    /** Creates a GWT label widget replacing its default class name, which supports html. */
    public static HTML html() {
        final HTML html = new HTML();
        clearStyleName(html);
        return html;
    }

    /** Creates a GWT anchor widget replacing its default class name. */
    public static ImageAnchor imageAnchor() {
        final ImageAnchor a = new ImageAnchor();
        clearStyleName(a);
        return a;
    }

    /** Creates a img widget. */
    public static Image img() {
        final Image img = new Image();
        clearStyleName(img);
        return img;
    }

    /** Creates a img widget with an img source. */
    public static Image img(String src) {
        final Image img = new Image(src);
        clearStyleName(img);
        return img;
    }

    /** Creates a GWT Div widget with a span inside replacing its default class name. */
    public static InlineLabel inlineLabel() {
        final InlineLabel inlineLabel = new InlineLabel();
        clearStyleName(inlineLabel);
        return inlineLabel;
    }

    /** Creates a GWT label widget replacing its default class name. */
    public static Label label() {
        final Label label = new Label();
        clearStyleName(label);
        return label;
    }

    /** Creates a GWT label widget replacing its default class name. */
    public static Label label(final String text) {
        final Label label = new Label(text);
        clearStyleName(label);
        return label;
    }

    /** Creates an html label element replacing its default class name. */
    public static LabelElement labelElement() {
        final LabelElement label = Document.get().createLabelElement();
        clearStyleName(label);
        return label;
    }

    /** Creates a li widget. */
    public static HtmlList.Item li() {
        return new HtmlList.Item();
    }

    /** Creates a li widget. */
    public static HtmlList.Item li(final String text) {
        return new HtmlList.Item(text);
    }

    /** Creates a GWT list box widget replacing its default class name. */
    public static ListBox list(boolean multiple, int rows) {
        final ListBox list = new ListBox();
        list.setMultipleSelect(multiple);
        list.setVisibleItemCount(rows);
        clearStyleName(list);
        return list;
    }

    /** Creates a ol widget. */
    public static HtmlList.Ordered ol() {
        return new HtmlList.Ordered();
    }

    /** Creates a GWT PasswordTextBox widget replacing its default class name. */
    public static PasswordTextBox passwordTextBox() {
        final PasswordTextBox textBox = new PasswordTextBox();
        clearStyleName(textBox);
        return textBox;
    }

    /** Creates a GWT popupPanel widget replacing its default class name. */
    public static PopupPanel popup() {
        final PopupPanel popup = new PopupPanel();
        clearStyleName(popup);
        return popup;
    }

    /** Creates a GWT radio widget replacing its default class name. */
    public static RadioButton radio(final String radioName, final String text) {
        final RadioButton checkBox = new RadioButton(radioName, text);
        clearStyleName(checkBox);
        return checkBox;
    }

    /** Creates a richTextArea widget. */
    public static RichTextArea richTextArea() {
        final RichTextArea richText = new RichTextArea();
        clearStyleName(richText);

        return richText;
    }
    /** Creates a GWT Span widget replacing its default class name. */
    public static InlineHTML span() {
        final InlineHTML inlineHTML = new InlineHTML();
        clearStyleName(inlineHTML);
        return inlineHTML;
    }

    /** Creates a GWT Span widget replacing its default class name. */
    public static InlineHTML span(final String text) {
        final InlineHTML inlineHTML = new InlineHTML(text);
        clearStyleName(inlineHTML);
        return inlineHTML;
    }

    /** Creates a textArea widget. */
    public static TextArea textArea() {
        final TextArea textArea = new TextArea();
        clearStyleName(textArea);
        return textArea;
    }

    /** Creates a GWT textBox widget replacing its default class name. */
    public static TextField textField() {
        final TextField textBox = new TextField();
        clearStyleName(textBox);
        return textBox;
    }

    /** Creates a ul widget. */
    public static HtmlList.Unordered ul() {
        return new HtmlList.Unordered();
    }

    //~ Static Fields ................................................................................................................................

    private static final String BUTTON_STYLE_NAME = "btn";
}  // end class HtmlWidgetFactory
