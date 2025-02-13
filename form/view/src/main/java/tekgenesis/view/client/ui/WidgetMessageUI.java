
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.check.CheckType.PLAIN;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.model.FormConstants.ELLIPSIS;
import static tekgenesis.view.client.ui.ExtendedHtmlSanitizer.sanitizeHtml;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.html;

/**
 * A TextArea UI widgets.
 */
public class WidgetMessageUI extends MessageUI implements HasTextLengthUI {

    //~ Instance Fields ..............................................................................................................................

    private final Anchor closeLink = anchor(FormConstants.CLOSE_ICON);

    private final HTML displayedMsg = html();
    private String     value        = "";

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public WidgetMessageUI(ModelUI container, final Widget model) {
        super(container, model);

        initWidget(displayedMsg);
        hideIfEmpty();

        setup(model);
    }

    //~ Methods ......................................................................................................................................

    @Override public void setTextLength(int length) {
        setValue(length < value.length() ? value.substring(0, length) + ELLIPSIS : value);
    }

    @Override public Object getValue() {
        return value;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        final String  text       = notNull((String) modelValue, "");
        final Element msgElement = displayedMsg.getElement();
        value = getModel().isFullText() ? text : coverText(text);
        removeLastNode(msgElement);
        final DivElement textDiv = Document.get().createDivElement();
        textDiv.addClassName("message-text");
        if (getModel().getMsgType() == PLAIN) textDiv.setInnerHTML(value);
        else textDiv.setInnerSafeHtml(sanitizeHtml(value));
        msgElement.appendChild(textDiv);
        hideIfEmpty();
    }  // end method setValue

    @NotNull @Override Option<Element> createIcon() {
        if (getModel().getMsgType() != PLAIN) return some(getElement().getFirstChildElement().getNextSiblingElement());
        else return some(getElement().getFirstChildElement());
    }

    @NotNull @Override Option<Element> createLabel() {
        final HeadingElement titleElem = Document.get().createHElement(4);
        final Element        element   = getElement();

        titleElem.setClassName(ALERT_HEADING);
        element.insertBefore(titleElem, element.getChild(element.getChildCount() - 1));
        return some(titleElem);
    }

    /** We control the hide, hide when not visible. */
    private void hideIfEmpty() {
        if (getModel().getHideExpression() == Expression.FALSE) setVisible(isNotEmpty(displayedMsg.getText()));
    }

    /** Setup message close and style names. */
    private void setup(Widget model) {
        final CheckType msgType         = model.getMsgType();
        final String    decorationClass = msgType.getDecorationClass(false);

        if (msgType != PLAIN) {
            closeLink.setStyleName(FormConstants.CLOSE_BUTTON);
            closeLink.addClickHandler(event -> setVisible(false));
            ((FlowPanel) div).insert(closeLink, 0);

            addStyleName(ALERT_STYLE_CLASS_NAME);
            setStylePrimaryName(ALERT_STYLE_CLASS_NAME);
            setStyleDependentName(decorationClass, true);
        }
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String ALERT_HEADING = "alert-heading";

    @NonNls public static final String ALERT_STYLE_CLASS_NAME = "alert";
}  // end class WidgetMessageUI
