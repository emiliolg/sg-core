
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

import tekgenesis.check.CheckType;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.ui.base.AutoAligner;
import tekgenesis.view.client.ui.base.Tooltip;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.html;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * Widget messages content to be attached to any widget.
 */
public class WidgetMessages extends Composite {

    //~ Instance Fields ..............................................................................................................................

    private final InlineHTML arrowContainer;
    private DisplayHandler   displayHandler = null;

    private String          lastMsgClass;
    private HTML            message          = html();
    private final FlowPanel panel;
    private final Widget    parent;
    private final Element   tooltipActivator;

    //~ Constructors .................................................................................................................................

    private WidgetMessages(FlowPanel parent, boolean isTooltip) {
        panel          = div();
        arrowContainer = span();
        arrowContainer.setStyleName(Tooltip.TOOLTIP_ARROW);
        panel.add(arrowContainer);

        lastMsgClass = null;
        final FlowPanel msgSpan = div();
        message = html();
        msgSpan.setStyleName("tooltip-inner");
        msgSpan.add(message);
        panel.add(msgSpan);

        displayHandler = new DisplayHandler();
        panel.addStyleName(TOOLTIP + " top");
        if (isTooltip) panel.addStyleName(INFO_TOOLTIP);
        else panel.addStyleName("check-tooltip");

        initWidget(panel);
        parent.insert(this, parent.getWidgetCount() > 0 ? 1 : 0);
        setVisible(!isTooltip);
        this.parent      = parent.getParent();
        tooltipActivator = parent.getElement().getFirstChildElement();
    }

    //~ Methods ......................................................................................................................................

    /** Clear all messages in content. */
    public void clear() {
        // hide msg panel
        setVisible(false);

        // remove parent messages styles
        parent.removeStyleName("has-" + lastMsgClass);
        message.setText(null);
    }

    /** Add error messageText to the content. */
    public void error(final String messageText) {
        addMessage(CheckType.ERROR, messageText);
    }

    /** Hides message. */
    public void hide() {
        if (isVisible()) setVisible(false);
    }

    /** Add debug message to the content. */
    public void msg(final CheckType type, final String text) {
        addMessage(type, text);
    }

    /** Shows message. */
    public void show() {
        setVisible(true);
        panel.getElement().getStyle().clearLeft();  // Clear left so resizing the window won't make tooltip look broken
        displayHandler.setPosition();
        Application.getInstance().setCurrentTooltip(this);
    }

    /** Shows message in the given position. */
    public void show(final int x, final int y) {
        if (!isVisible()) {
            setVisible(true);
            arrowContainer.getElement().getStyle().clearLeft();  // Clear left on arrow to correct rendering when moving to side
            final Style style = panel.getElement().getStyle();
            style.setPosition(Style.Position.FIXED);
            style.setLeft(x - LEFT_OFFSET, Style.Unit.PX);
            style.setTop(y - panel.getOffsetHeight(), Style.Unit.PX);
            style.setMarginLeft(LEFT_MARGIN, Style.Unit.PX);
            Application.getInstance().setCurrentTooltip(this);
            displayHandler.leaveInsideScreen(displayHandler::checkLeft, intIntTuple -> displayHandler.checkMovingRight(intIntTuple, x, y));
        }
    }

    /** Add a msg. */
    public void tooltipmsg(final String text) {
        message.setHTML(ExtendedHtmlSanitizer.sanitizeHtml(notNull(text, "")));
    }

    private void addMessage(final CheckType type, final String text) {
        message.setHTML(ExtendedHtmlSanitizer.sanitizeHtml(notNull(text, "")));
        lastMsgClass = type.getDecorationClass(true);
        parent.addStyleName("has-" + lastMsgClass);
    }

    //~ Methods ......................................................................................................................................

    /** Creates an empty messages content. */
    public static WidgetMessages create(FlowPanel parent, boolean isTooltip) {
        return new WidgetMessages(parent, isTooltip);
    }

    //~ Static Fields ................................................................................................................................

    private static final int    MARGIN       = -20;
    private static final int    LEFT_MARGIN  = -3;
    private static final int    LEFT_OFFSET  = 10;  // Used to match the mouse arrow with the tooltip small spike
    private static final String INFO_TOOLTIP = "info-tooltip";
    private static final String TOOLTIP      = "tooltip";

    //~ Inner Classes ................................................................................................................................

    private class DisplayHandler extends AutoAligner {
        /** Tooltip will never overlap with the left side of the screen. */
        @Override public void checkLeft(int mainLeft) {}

        @Override public void checkRight(IntIntTuple main) {
            final int absoluteLeft = panel.getAbsoluteLeft();
            final int offsetWidth  = panel.getOffsetWidth();
            if (absoluteLeft + offsetWidth > main.second() + main.first()) {
                final int iconOffset  = tooltipActivator.getOffsetWidth();
                final int arrow       = arrowContainer.getAbsoluteLeft() - absoluteLeft + arrowContainer.getOffsetWidth();
                final int arrowLength = arrowContainer.getAbsoluteLeft() - absoluteLeft;
                getElement().getStyle().setLeft(-(offsetWidth - iconOffset - arrow - ((arrowLength + 1) / 2)), Style.Unit.PX);     // moves tooltip to the left
                arrowContainer.getElement().getStyle().setLeft(offsetWidth - iconOffset, Style.Unit.PX);                           // moves arrow to the right
            }
        }

        @Override public void setPosition() {
            final Style style = panel.getElement().getStyle();
            style.setMarginTop(MARGIN - panel.getOffsetHeight(), Style.Unit.PX);
            arrowContainer.getElement().getStyle().clearLeft();
            leaveInsideScreen(this::checkLeft, this::checkRight);
        }

        void checkMovingRight(IntIntTuple main, final int x, final int y) {
            final int offset       = panel.getOffsetWidth();
            final int absoluteLeft = panel.getAbsoluteLeft();
            if (absoluteLeft + offset > main.second() + main.first()) {
                getElement().getStyle().setLeft(x - offset + (LEFT_OFFSET * 2), Style.Unit.PX);
                arrowContainer.getElement().getStyle().setLeft(offset - (x - absoluteLeft) + (LEFT_OFFSET / 2), Style.Unit.PX);
            }
        }
    }
}  // end class WidgetMessages
