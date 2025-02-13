
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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.IntIntTuple;
import tekgenesis.metadata.form.widget.PopoverType;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.metadata.form.model.FormConstants.DROPDOWN_OPEN_LEFT;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clickElement;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * Popover Widget.
 */
public class Popover extends FlowPanel implements ClickHandler {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel      arrow;
    private final FlowPanel      contentDiv;
    private final DisplayHandler displayHandler;
    private final Anchor         link;

    private ModalListener     listener;
    private boolean           opened;
    private final FlowPanel   popover;
    private final PopoverType popoverType;

    //~ Constructors .................................................................................................................................

    /** Popover constructor. */
    public Popover(@NotNull PopoverType popoverType) {
        popover        = div();
        link           = anchor();
        contentDiv     = div();
        arrow          = div();
        listener       = null;
        displayHandler = new DisplayHandler();

        this.popoverType = popoverType;

        addStyleName("popover-container");
        popover.addStyleName("popover fade in");
        contentDiv.addStyleName("popover-content");
        arrow.addStyleName("arrow");

        popover.getElement().insertFirst(arrow.getElement());
        popover.add(contentDiv);
        add(link);
        add(popover);

        popover.setVisible(false);
        link.addClickHandler(this);
    }

    //~ Methods ......................................................................................................................................

    /** Add to content div. */
    public void addContent(@NotNull Widget w) {
        contentDiv.add(w);
    }

    /** Set content style. */
    public void addContentStyleName(String styleName) {
        popover.addStyleName(styleName);
    }

    /** addFooter. */
    public void addFooter(@NotNull Widget w) {
        popover.add(w);
    }

    /** addHeader. */
    public void addHeader(@NotNull Widget w) {
        popover.insert(w, 0);
    }

    /** Adds hover trigger. */
    public void addHoverTrigger() {
        final Timer openTimer = new Timer() {
                @Override public void run() {
                    showContent();
                }
            };

        link.addMouseOverHandler(event -> openTimer.schedule(100));

        link.addMouseOutHandler(event -> openTimer.cancel());
        popover.addDomHandler(event -> hideContent(), MouseOutEvent.getType());
    }

    /** Set Link StyleName. */
    public void addLinkStyleName(String style) {
        link.addStyleName(style);
    }

    /** Clicks Link element. */
    public void clickLink() {
        clickElement(link.getElement());
    }

    /** Hide Popover content. */
    public void hideContent() {
        opened = false;
        link.removeStyleName(ACTIVE_STYLE);
        popover.setVisible(false);
        ModalGlass.hide();
        if (listener != null) listener.onHide(null);
    }

    @Override public void onClick(ClickEvent event) {
        if (opened) hideContent();
        else showContent();
    }

    /** Show Popover content. */
    public void showContent() {
        opened = true;
        link.addStyleName(ACTIVE_STYLE);
        popover.setVisible(true);
        displayHandler.setPosition();
        final ModalGlass glass = ModalGlass.show(true);
        glass.setClickHandler(event -> hideContent());
        glass.setKeyHandler(event -> hideContent());
        if (listener != null) listener.onShow();
    }

    /** Returns if popup is visible. */
    public boolean isShowing() {
        return opened;
    }

    /** Returns the link part of the popover. */
    public Anchor getLink() {
        return link;
    }

    /** Set Link icon. */
    public void setLinkIcon(String iconStyle) {
        if (iconStyle != null) Icon.replaceInWidget(link, iconStyle);
    }

    /** Set link text. */
    public void setLinkTextSafe(String linkText) {
        final Element firstChildElement = link.getElement().getFirstChildElement();
        link.setText(linkText);
        if (firstChildElement != null) link.getElement().insertFirst(firstChildElement);
    }

    /** Set Listener. */
    public void setListener(ModalListener listener) {
        this.listener = listener;
    }

    /** Set Popover title. */
    public void setTitle(@Nullable String label) {
        if (!isEmpty(label)) {
            final HeadingElement title = Document.get().createHElement(3);
            title.setClassName(POPOVER_TITLE);
            title.setInnerText(label);
            popover.getElement().insertFirst(title);
            setLinkTextSafe(label);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String  POPOVER_TITLE = "popover-title";
    private static final String TO_RIGHT      = "to-right";

    //~ Inner Classes ................................................................................................................................

    private class DisplayHandler extends AutoAligner {
        private boolean notPositioned = true;

        @Override public void checkLeft(int mainLeft) {
            if (popover.getAbsoluteLeft() < mainLeft) {
                popover.getElement().getStyle().clearLeft();
                arrow.addStyleName(TO_RIGHT);
            }
            else arrow.removeStyleName(TO_RIGHT);
        }

        @Override public void checkRight(IntIntTuple main) {
            if (popover.getAbsoluteLeft() + popover.getOffsetWidth() > main.second() + main.first()) popover.getElement().getStyle().clearRight();
        }

        /** Position popover. */
        @Override public void setPosition() {
            if (notPositioned) {
                final Style style = popover.getElement().getStyle();
                popover.addStyleName(popoverType.toString());

                switch (popoverType) {
                case TOP:
                    style.setLeft(-(popover.getOffsetWidth() / 2 - link.getOffsetWidth() / 2), Style.Unit.PX);
                    style.setTop(-(popover.getOffsetHeight()), Style.Unit.PX);
                    break;
                case LEFT:
                    style.setLeft(-popover.getOffsetWidth(), Style.Unit.PX);
                    style.setTop(-(popover.getOffsetHeight() / 2 - link.getOffsetHeight() / 2), Style.Unit.PX);
                    break;
                case BOTTOM:
                    if (popover.getStyleName().contains(DROPDOWN_OPEN_LEFT))
                        style.setLeft(-(popover.getOffsetWidth() - link.getOffsetWidth()), Style.Unit.PX);
                    else style.setLeft(-(popover.getOffsetWidth() / 2 - link.getOffsetWidth() / 2), Style.Unit.PX);
                    style.setTop((link.getOffsetHeight()), Style.Unit.PX);
                    break;
                case RIGHT:
                    style.setLeft(link.getOffsetWidth(), Style.Unit.PX);
                    style.setTop(-(popover.getOffsetHeight() / 2 - link.getOffsetHeight() / 2), Style.Unit.PX);
                    break;
                }
                notPositioned = false;
                leaveInsideScreen(this::checkLeft, this::checkRight);
            }
        }
    }  // end class DisplayHandler
}  // end class Popover
