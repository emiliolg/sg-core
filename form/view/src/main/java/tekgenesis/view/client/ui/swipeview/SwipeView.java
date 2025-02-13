
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.swipeview;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;

import tekgenesis.view.client.ui.swipeview.events.FlipEventHandler;
import tekgenesis.view.client.ui.swipeview.events.MoveInHandler;
import tekgenesis.view.client.ui.swipeview.events.MoveOutHandler;
import tekgenesis.view.client.ui.swipeview.events.SwipeTouchStartHandler;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.metadata.form.model.FormConstants.STYLE_ATTR;
import static tekgenesis.view.client.ui.swipeview.BrowserCapabilitiesUtil.getBrowserCapabilities;

/**
 * Swipe View.
 */
@SuppressWarnings(
        { "BooleanVariableAlwaysNegated", "ClassWithTooManyFields", "WeakerAccess" }
                 )  // (WeakerAccess) It's a
                    // component that will be used
                    // in the near future.
public class SwipeView {

    //~ Instance Fields ..............................................................................................................................

    private FlowPanel container = null;

    private int     containerWidth;
    private int     currentMasterPage = 1;
    private boolean directionLocked;
    private int     directionX;
    private int     dist;

    private final List<FlipEventHandler> flipHandlers    = new ArrayList<>();
    private final SwipeEventHandler      handler;
    private boolean                      initiated;
    private int                          maxX;
    private boolean                      moved;
    private final List<MoveInHandler>    moveInHandlers  = new ArrayList<>();
    private final List<MoveOutHandler>   moveOutHandlers = new ArrayList<>();
    private int                          newX;
    private int                          numberOfPages;

    private SwipeViewOptions      options   = null;
    private int                   page      = 0;
    private int                   pageIndex;
    private final List<FlowPanel> pages     = new ArrayList<>();
    private int                   pageWidth;
    private int                   pointX;
    private int                   pointY;
    private FlowPanel             slider    = null;

    private double                             snapThreshold;
    private int                                startX;
    private int                                stepsX;
    private int                                stepsY;
    private final List<SwipeTouchStartHandler> swipeTouchStartHandlers = new ArrayList<>();
    private boolean                            thresholdExceeded;
    private int                                x                       = 0;

    //~ Constructors .................................................................................................................................

    /** Build a Swipe View in the given container with the DEFAULT options. */
    public SwipeView(final FlowPanel container) {
        this(container, SwipeViewOptions.DEFAULT);
    }

    /** Build a Swipe View in the given container with the given options. */
    public SwipeView(final FlowPanel container, final SwipeViewOptions options) {
        this.container = container;
        this.options   = options;
        numberOfPages  = options.getNumberOfPages();

        container.setStyleName("swiperContainer");

        slider = new FlowPanel();
        slider.getElement().setId("swipeview-slider");
        slider.getElement()
            .setAttribute(STYLE_ATTR,
                "position:relative; " +
                "top:0;height:100%; " +
                "width:100%; " + getBrowserCapabilities().getCssVendor() + "transition-duration:0s; " + getBrowserCapabilities()
                    .getCssVendor() + "transform:translateZ(0); " + getBrowserCapabilities().getCssVendor() + "transition-timing-function:ease-out;");

        container.add(slider);

        refreshSize();

        handler = new SwipeEventHandler();

        buildPages();
    }

    //~ Methods ......................................................................................................................................

    /** Register a flip handler. */
    public void addFlipEventHandler(FlipEventHandler f) {
        flipHandlers.add(f);
    }

    /** Register a move in handler. */
    public void addMoveInHandler(MoveInHandler mi) {
        moveInHandlers.add(mi);
    }

    /** Register a move out handler. */
    public void addMoveOutHandler(MoveOutHandler mo) {
        moveOutHandlers.add(mo);
    }

    /** Register a touch start handler. */
    public void addTouchStartHandler(SwipeTouchStartHandler t) {
        swipeTouchStartHandlers.add(t);
    }

    /** Performs a previous page. */
    public void back() {
        if (!options.isLoop() && x == 0) return;

        directionX =  1;
        x          += 1;
        checkPosition();
    }

    /** Go to specific page. */
    public void goToPage(final int p, final boolean fireEvent) {
        pages.get(currentMasterPage).removeStyleName(SWIPEVIEW_ACTIVE_CSS_CLASS);

        for (int i = 0; i < 3; i++) {
            if (pages.get(i).getStyleName().contains(SWIPEVIEW_LOADING_CSS_CLASS)) pages.get(i).addStyleName(SWIPEVIEW_LOADING_CSS_CLASS);
        }

        page      = p < 0 ? 0 : p > numberOfPages - 1 ? numberOfPages - 1 : p;
        pageIndex = page;
        setStyle(slider, getBrowserCapabilities().getTransitionDuration(), "0s");
        pos(-p * pageWidth);

        currentMasterPage = (int) ((page + 1) - Math.floor(((double) (page + 1)) / 3) * 3);

        pages.get(currentMasterPage).addStyleName(SWIPEVIEW_ACTIVE_CSS_CLASS);
        pages.get(currentMasterPage).getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);

        if (currentMasterPage == 0) {
            pages.get(2).getElement().getStyle().setLeft(page * 100 - 100, Style.Unit.PCT);
            pages.get(0).getElement().getStyle().setLeft(page * 100, Style.Unit.PCT);
            pages.get(1).getElement().getStyle().setLeft(page * 100 + 100, Style.Unit.PCT);

            pages.get(2).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, (page == 0 ? numberOfPages : page) - 1);
            pages.get(0).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page);
            pages.get(1).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page == numberOfPages - 1 ? 0 : page + 1);
        }
        else if (currentMasterPage == 1) {
            pages.get(0).getElement().getStyle().setLeft(page * 100 - 100, Style.Unit.PCT);
            pages.get(1).getElement().getStyle().setLeft(page * 100, Style.Unit.PCT);
            pages.get(2).getElement().getStyle().setLeft(page * 100 + 100, Style.Unit.PCT);

            pages.get(0).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, (page == 0 ? numberOfPages : page) - 1);
            pages.get(1).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page);
            pages.get(2).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page == numberOfPages - 1 ? 0 : page + 1);
        }
        else {
            pages.get(1).getElement().getStyle().setLeft(page * 100 - 100, Style.Unit.PCT);
            pages.get(2).getElement().getStyle().setLeft(page * 100, Style.Unit.PCT);
            pages.get(0).getElement().getStyle().setLeft(page * 100 + 100, Style.Unit.PCT);

            pages.get(1).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, (page == 0 ? numberOfPages : page) - 1);
            pages.get(2).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page);
            pages.get(0).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, page == numberOfPages - 1 ? 0 : page + 1);
        }

        flip(true, fireEvent);
    }  // end method goToPage

    /** Performs a next page. */
    public void next() {
        if (!options.isLoop() && x == maxX) return;

        directionX =  -1;
        x          -= 1;
        checkPosition();
    }

    /** Updates page count. */
    public void updatePageCount(int n) {
        numberOfPages = n;
        maxX          = -numberOfPages * pageWidth + containerWidth;
    }

    /** Returns the active page index. */
    public int getActivePageIndex() {
        for (int i = 0; i < 3; i++)
            if (pages.get(i).getStyleName().contains(SWIPEVIEW_ACTIVE_CSS_CLASS)) return getPageIndex(i);
        throw unreachable();
    }

    /** Returns the i page div's panel. */
    public FlowPanel getPage(int i) {
        return pages.get(i);
    }

    /** Returns the upcoming page index of the i page. */
    public int getPageIndex(int i) {
        return getPage(i).getElement().getPropertyInt(PAGE_INDEX_ATTR);
    }

    /** Returns the upcoming page index of the i page. */
    public int getUpcomingPageIndex(int i) {
        return getPage(i).getElement().getPropertyInt(UPCOMING_PAGE_INDEX_ATTR);
    }

    private void buildPages() {
        for (int i = -1; i < 2; i++) {
            final FlowPanel pagePanel = new FlowPanel();
            pagePanel.getElement().setId("swipeview-masterpage-" + (i + 1));

            pagePanel.getElement()
                .setAttribute(STYLE_ATTR,
                    "position:absolute; top:0; height:100%; width:100%;" + getBrowserCapabilities()
                        .getCssVendor() + "transform:translateZ(0); left:" + i * 100 + "%");

            final int pIndex = i == -1 ? numberOfPages - 1 : i;

            pagePanel.getElement().setPropertyInt(PAGE_INDEX_ATTR, pIndex);
            pagePanel.getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, pIndex);

            if (!options.isLoop() && i == -1) pagePanel.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);

            slider.add(pagePanel);
            pages.add(pagePanel);
        }

        final String className = pages.get(1).getStyleName();
        pages.get(1).setStyleName("".equals(className) ? SWIPEVIEW_ACTIVE_CSS_CLASS : className + " swipeview-active");

        Window.addResizeHandler(handler);
        container.addAttachHandler(handler);

        if (getBrowserCapabilities().hasTouch()) {
            container.addDomHandler(handler, TouchStartEvent.getType());
            container.addDomHandler(handler, TouchMoveEvent.getType());
            container.addDomHandler(handler, TouchCancelEvent.getType());
            container.addDomHandler(handler, TouchEndEvent.getType());
        }
        else {
            container.addDomHandler(handler, MouseDownEvent.getType());
            container.addDomHandler(handler, MouseMoveEvent.getType());
            container.addDomHandler(handler, MouseUpEvent.getType());
        }

        // slider.addDomHandler(handler, TransitionEndEvent.getType());
    }  // end method buildPages

    private void checkPosition() {
        pages.get(currentMasterPage).removeStyleName(SWIPEVIEW_ACTIVE_CSS_CLASS);

        int pageFlip;
        int pageFlipIndex;

        if (directionX > 0) {
            page              = (int) -Math.ceil(((double) x) / ((double) pageWidth));
            currentMasterPage = (int) ((page + 1) - Math.floor(((double) (page + 1)) / 3) * 3);
            pageIndex         = (pageIndex == 0 ? numberOfPages : pageIndex) - 1;

            pageFlip = currentMasterPage - 1;
            pageFlip = pageFlip < 0 ? 2 : pageFlip;
            pages.get(pageFlip).getElement().getStyle().setLeft(page * 100 - 100, Style.Unit.PCT);

            pageFlipIndex = page - 1;
        }
        else {
            page              = (int) -Math.floor(((double) x) / ((double) pageWidth));
            currentMasterPage = (int) ((page + 1) - Math.floor(((double) (page + 1)) / 3) * 3);
            pageIndex         = pageIndex == numberOfPages - 1 ? 0 : pageIndex + 1;

            pageFlip = currentMasterPage + 1;
            pageFlip = pageFlip > 2 ? 0 : pageFlip;
            pages.get(pageFlip).getElement().getStyle().setLeft(page * 100 + 100, Style.Unit.PCT);

            pageFlipIndex = page + 1;
        }

        // Add active class to current page
        pages.get(currentMasterPage).addStyleName(SWIPEVIEW_ACTIVE_CSS_CLASS);
        pages.get(currentMasterPage).getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);

        // Add loading class to flipped page
        pages.get(pageFlip).addStyleName(SWIPEVIEW_LOADING_CSS_CLASS);

        pageFlipIndex = (int) (pageFlipIndex - Math.floor(((double) pageFlipIndex) / ((double) numberOfPages)) * numberOfPages);
        pages.get(pageFlip).getElement().setPropertyInt(UPCOMING_PAGE_INDEX_ATTR, pageFlipIndex);

        newX = -page * pageWidth;

        setStyle(slider, getBrowserCapabilities().getTransitionDuration(), Math.floor(TRANSITION_DURATION_2 * Math.abs(x - newX) / pageWidth) + "ms");

        // Hide the next page if we decided to disable looping
        if (!options.isLoop())
            pages.get(pageFlip).getElement().getStyle().setVisibility(newX == 0 || newX == maxX ? Style.Visibility.HIDDEN : Style.Visibility.VISIBLE);

        if (x == newX) flip(directionX <= 0, true);  // If we swiped all the way long to the next page (extremely rare but still)
        else {
            pos(newX);
            if (options.isHastyPageFlip()) flip(directionX <= 0, true);
        }
    }                                                // end method checkPosition

    private void end(final int pageX) {
        if (!initiated) return;

        dist = Math.abs(pageX - startX);

        initiated = false;

        if (!moved) return;

        if (!options.isLoop() && (x > 0 || x < maxX)) {
            dist = 0;
            fireMoveInEvent();
        }

        // Check if we exceeded the snap threshold
        if (dist < snapThreshold) {
            setStyle(slider, getBrowserCapabilities().getTransitionDuration(), Math.floor(TRANSITION_DURATION_1 * dist / snapThreshold) + "ms");
            pos(-page * pageWidth);
            return;
        }

        checkPosition();
    }

    private void fireFlipEvent(boolean rightDirection) {
        for (final FlipEventHandler h : flipHandlers)
            h.onFlip(rightDirection);
    }

    private void fireMoveInEvent() {
        for (final MoveInHandler h : moveInHandlers)
            h.onMoveIn();
    }

    private void fireMoveOutEvent() {
        for (final MoveOutHandler h : moveOutHandlers)
            h.onMoveOut();
    }

    private void fireTouchStartEvent() {
        for (final SwipeTouchStartHandler h : swipeTouchStartHandlers)
            h.onTouchStart();
    }

    private void flip(boolean rightDirection, boolean fireEvent) {
        if (fireEvent) fireFlipEvent(rightDirection);

        for (int i = 0; i < 3; i++) {
            pages.get(i).removeStyleName(SWIPEVIEW_LOADING_CSS_CLASS);
            pages.get(i).getElement().setPropertyInt(PAGE_INDEX_ATTR, pages.get(i).getElement().getPropertyInt(UPCOMING_PAGE_INDEX_ATTR));
        }
    }

    private void move(final int pageX, final int pageY) {
        if (!initiated) return;

        final int deltaX = pageX - pointX;
        final int deltaY = pageY - pointY;
        newX = x + deltaX;
        dist = Math.abs(pageX - startX);

        moved      =  true;
        pointX     =  pageX;
        pointY     =  pageY;
        directionX =  deltaX > 0 ? 1 : deltaX < 0 ? -1 : 0;
        stepsX     += Math.abs(deltaX);
        stepsY     += Math.abs(deltaY);

        // We take a 10px buffer to figure out the direction of the swipe
        if (stepsX < 10 && stepsY < 10) return;

        // We are scrolling vertically, so skip SwipeView and give the control back to the browser
        if (!directionLocked && stepsY > stepsX) {
            initiated = false;
            return;
        }

        directionLocked = true;

        if (!options.isLoop() && (newX > 0 || newX < maxX)) newX = x + (deltaX / 2);

        if (!thresholdExceeded && dist >= snapThreshold) {
            thresholdExceeded = true;
            fireMoveOutEvent();
        }
        else if (thresholdExceeded && dist < snapThreshold) {
            thresholdExceeded = false;
            fireMoveInEvent();
        }

        pos(newX);
    }

    private void pos(int xpos) {
        x = xpos;
        final String property = getBrowserCapabilities().getTransform();
        final String value    = "translate(" + xpos + "px, 0)" + getBrowserCapabilities().getTranslateZ();
        setStyle(slider, property, value);
    }

    private void refreshSize() {
        containerWidth = options.getWidth();
        pageWidth      = containerWidth;
        maxX           = -numberOfPages * pageWidth + containerWidth;
        snapThreshold  = options.getSnapThreshold() == 0D ? Math.round(pageWidth * DEFAULT_SNAP_THRESHOLD) : options.getSnapThreshold();
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private void resize() {
        refreshSize();
        setStyle(slider, getBrowserCapabilities().getTransitionDuration(), "0s");
        pos(-page * pageWidth);
    }

    private void start(final int pageX, final int pageY) {
        if (initiated) return;

        initiated         = true;
        moved             = false;
        thresholdExceeded = false;
        startX            = pageX;
        // int startY = pageY;
        pointX          = pageX;
        pointY          = pageY;
        stepsX          = 0;
        stepsY          = 0;
        directionX      = 0;
        directionLocked = false;

        setStyle(slider, getBrowserCapabilities().getTransitionDuration(), "0s");

        fireTouchStartEvent();
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")  // indexOf its supported
    private void setStyle(final FlowPanel panel, String property, String value) {
        final String style        = panel.getElement().getAttribute(STYLE_ATTR);
        final String propAndValue = property + " : " + value + ";";

        if (style.isEmpty()) panel.getElement().setAttribute(STYLE_ATTR, propAndValue);
        else if (!style.contains(property)) panel.getElement().setAttribute(STYLE_ATTR, style + "; " + propAndValue);
        else {
            final int start = style.indexOf(property);
            final int i     = style.indexOf(";", start);
            panel.getElement().setAttribute(STYLE_ATTR, style.substring(0, start) + propAndValue + style.substring(i + 1, style.length()));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String UPCOMING_PAGE_INDEX_ATTR    = "upcomingPageIndex";
    private static final String PAGE_INDEX_ATTR             = "pageIndex";
    private static final String SWIPEVIEW_LOADING_CSS_CLASS = "swipeview-loading";
    private static final String SWIPEVIEW_ACTIVE_CSS_CLASS  = "swipeview-active";
    private static final int    TRANSITION_DURATION_1       = 300;
    private static final int    TRANSITION_DURATION_2       = 500;
    private static final double DEFAULT_SNAP_THRESHOLD      = .15;

    //~ Inner Classes ................................................................................................................................

    private class SwipeEventHandler
        implements ResizeHandler, TouchStartHandler, TouchMoveHandler, TouchEndHandler, TouchCancelHandler, MouseDownHandler, MouseMoveHandler,
                   MouseUpHandler, AttachEvent.Handler
    {
        @Override public void onAttachOrDetach(AttachEvent event) {
            refreshSize();
        }
        @Override public void onMouseDown(MouseDownEvent e) {
            start(e.getClientX(), e.getClientY());
        }

        @Override public void onMouseMove(MouseMoveEvent e) {
            move(e.getClientX(), e.getClientY());
            e.preventDefault();
        }

        @Override public void onMouseUp(MouseUpEvent e) {
            end(e.getClientX());
        }

        @Override public void onResize(ResizeEvent event) {
            resize();
        }

        @Override public void onTouchCancel(TouchCancelEvent e) {
            end(e.getChangedTouches().get(0).getPageX());
        }

        @Override public void onTouchEnd(TouchEndEvent e) {
            end(e.getChangedTouches().get(0).getPageX());
        }

        @Override public void onTouchMove(TouchMoveEvent e) {
            final Touch touch = e.getTouches().get(0);
            move(touch.getPageX(), touch.getPageY());
            e.preventDefault();
        }

        @Override public void onTouchStart(TouchStartEvent e) {
            final Touch touch = e.getTouches().get(0);
            start(touch.getPageX(), touch.getPageY());
        }
    }  // end class SwipeEventHandler
}
