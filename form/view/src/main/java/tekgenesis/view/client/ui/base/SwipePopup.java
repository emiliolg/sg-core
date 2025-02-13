
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NonNls;

import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.SwipeFormBox;
import tekgenesis.view.client.ui.swipeview.SwipeView;
import tekgenesis.view.client.ui.swipeview.SwipeViewOptions;
import tekgenesis.view.client.ui.swipeview.events.FlipEventHandler;
import tekgenesis.view.shared.response.SwipeResponse;

import static tekgenesis.view.client.ui.base.DetailPopup.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.clickableDiv;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * Swipper popup panel used to swipe things.
 */
public class SwipePopup extends PopupPanel implements FlipEventHandler {

    //~ Instance Fields ..............................................................................................................................

    private ClickablePanel backArrow = null;

    private final SwipeView carousel;

    private FlowPanel               content       = null;
    private Consumer<List<Integer>> fetchFunction = null;
    private int                     fetchSize;

    private final SwipeFormBox[] forms;
    private HandlerRegistration  handlerRegistration = null;

    private final List<Integer> indexesMap;

    private final boolean   loop;
    private ClickablePanel  nextArrow      = null;
    private final FlowPanel swipeContainer;

    //~ Constructors .................................................................................................................................

    /** Creates a Swipper. */
    public SwipePopup(final SwipeResponse response) {
        super(true);
        indexesMap = response.getIndexesMap();
        forms      = new SwipeFormBox[getSwipperSize()];
        loop       = response.isLoop();

        setStyleName(SWIPE_POPUP_FULL_SCREEN);

        swipeContainer = div();
        carousel       = new SwipeView(swipeContainer,
                new SwipeViewOptions().width(response.getWidth())
                                      .loop(getSwipperSize() > 2 && loop)
                                      .numberOfPages(getSwipperSize())
                                      .hastyPageFlip(true));
        carousel.addFlipEventHandler(this);

        if (response.isFullscreen()) {
            buildFullScreen();
            add(swipeContainer);
        }
        else {
            setGlassStyleName(SWIPE_POPUP_GLASS);
            setGlassEnabled(true);

            content = div();  // for arrows.
            content.add(swipeContainer);
            add(content);
            resize(response.getMarginTop(), response.getWidth(), response.getHeight());
        }

        attachArrows(response.isFullscreen());
    }

    //~ Methods ......................................................................................................................................

    /** Called when a fetch is finished to load possible not present forms. */
    public void fetchFinish() {
        System.out.println("FETCH FINISH!");
        // Lets see if I need the new form...
        for (int page = 0; page < 3; page++) {
            final FlowPanel holder = carousel.getPage(page);
            if (holder != null && holder.getWidgetCount() == 0) {
                final SwipeFormBox box = forms[carousel.getPageIndex(page)];
                if (box != null) box.attach(holder);
            }
        }
    }

    @Override public void hide(boolean autoClosed) {
        Document.get().getBody().getStyle().clearOverflow();

        if (handlerRegistration != null) handlerRegistration.removeHandler();
        for (final SwipeFormBox box : forms)
            if (box != null) box.detach();
        super.hide(autoClosed);
    }

    @Override public void onFlip(boolean rightDirection) {
        if (getSwipperSize() > 2) {
            for (int i = 0; i < 3; i++) {
                final int upcoming  = carousel.getUpcomingPageIndex(i);
                final int pageIndex = carousel.getPageIndex(i);

                if (upcoming != pageIndex) {
                    final FlowPanel holder = carousel.getPage(i);
                    holder.clear();
                    final SwipeFormBox box = forms[upcoming];
                    if (box == null) fetchFunction.accept(resolveIndexesToLoad(upcoming, rightDirection));
                    else box.attach(holder);
                }
            }
        }

        updateArrowsVisibility();
    }

    /** Called to put more models on this Swipper. */
    public void put(final int i, final SwipeFormBox box) {
        forms[indexesMap.indexOf(i)] = box;
    }

    /** Show from a given index. */
    public void show(final int startIndex) {
        Document.get().getBody().getStyle().setOverflow(Style.Overflow.HIDDEN);  // prevent body scroll
        show();

        carousel.goToPage(startIndex, false);

        final int size = getSwipperSize();
        final int from = size > 2 ? 0 : 1;
        final int to   = size < 2 ? 2 : 3;
        for (int i = from; i < to; i++) {
            final int       page   = carousel.getPageIndex(i);
            final FlowPanel holder = carousel.getPage(i);
            holder.clear();
            final SwipeFormBox box = forms[page];
            if (box != null) box.attach(holder);
        }

        if (size > 1) updateArrowsVisibility();
    }

    /** Sets the fetch function to be used to fetch more models. */
    public void setFetchFunction(Consumer<List<Integer>> fetchFunction) {
        this.fetchFunction = fetchFunction;
    }

    /** Sets the fetch size of this swipper. */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    private void attachArrows(boolean fullScreen) {
        if (!fullScreen && getSwipperSize() > 1) {
            backArrow = clickableDiv();
            // noinspection GWTStyleCheck
            backArrow.setStyleName("swipeBackArrow clickable");
            backArrow.addClickHandler(event -> carousel.back());
            content.add(backArrow);

            nextArrow = clickableDiv();
            // noinspection GWTStyleCheck
            nextArrow.setStyleName("swipeNextArrow clickable");
            nextArrow.addClickHandler(event -> carousel.next());
            content.add(nextArrow);

            final ClickablePanel close = clickableDiv();
            close.setStyleName(SWIPE_CLOSE_CONTAINER_CLICKABLE);
            close.addClickHandler(event -> hide());

            final Icon closeIcon = new Icon(IconType.TIMES_CIRCLE);
            closeIcon.addStyleName(SWIPE_CLOSE_ICON_3X);
            close.add(closeIcon);
            content.add(close);
        }

        handlerRegistration = RootPanel.get().addDomHandler(event -> {
                    final int keyCode = event.getNativeEvent().getKeyCode();
                    if (keyCode == KeyCodes.KEY_LEFT) carousel.back();
                    else if (keyCode == KeyCodes.KEY_RIGHT) carousel.next();
                    else if (keyCode == KeyCodes.KEY_ESCAPE) hide();
                },
                KeyDownEvent.getType());
    }

    /**  */
    private void buildFullScreen() {
        getElement().getStyle().setWidth(100, Style.Unit.PCT);
        getElement().getStyle().setHeight(100, Style.Unit.PCT);
        swipeContainer.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        swipeContainer.getElement().getStyle().setHeight(100, Style.Unit.PCT);
    }

    /** Resize swipper based on size properties. */
    private void resize(final int marginTop, final int width, final int height) {
        addStyleName(SWIPE_POPUP);
        final int w = width != 0 ? width : DEFAULT_WIDTH;
        getElement().getStyle().setMarginLeft(-w / 2, Style.Unit.PX);
        getElement().getStyle().setMarginTop(marginTop, Style.Unit.PX);
        swipeContainer.getElement().getStyle().setWidth(w, Style.Unit.PX);
        swipeContainer.getElement().getStyle().setHeight(height != 0 ? height : DEFAULT_HEIGHT, Style.Unit.PX);
    }

    private List<Integer> resolveIndexesToLoad(final int startIndex, final boolean rightDirection) {
        final int           fetch  = Math.min(fetchSize, getSwipperSize());
        final List<Integer> result = new ArrayList<>(fetch);

        // add the next indexes
        int i = startIndex;
        if (rightDirection) {
            while (result.size() < fetch) {
                if (i >= getSwipperSize()) {
                    if (loop) i = 0;  // start over
                    else break;       // end
                }

                if (forms[i] == null) result.add(indexesMap.get(i));  // add index if we don't have it yet
                i++;
            }
        }
        else {
            while (result.size() < fetch) {
                if (i < 0) {
                    if (loop) i = getSwipperSize() - 1;               // start over
                    else break;                                       // end
                }

                if (forms[i] == null) result.add(indexesMap.get(i));  // add index if we don't have it yet
                i--;
            }
        }

        return result;
    }

    private void updateArrowsVisibility() {
        final int current = carousel.getActivePageIndex();
        final int size    = getSwipperSize();

        backArrow.setVisible(current != 0 || loop);
        nextArrow.setVisible(current != size - 1 || loop);
    }

    private int getSwipperSize() {
        return indexesMap.size();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String SWIPE_CLOSE_CONTAINER_CLICKABLE = "swipeCloseContainer clickable";
    @NonNls public static final String SWIPE_CLOSE_ICON_3X             = "swipeClose fa-3x";

    static final int DEFAULT_WIDTH  = 800;
    static final int DEFAULT_HEIGHT = 500;
}  // end class SwipePopup
