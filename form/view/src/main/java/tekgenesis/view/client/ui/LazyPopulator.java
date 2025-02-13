
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;

import tekgenesis.view.client.ui.multiple.Rangeable;

import static com.google.gwt.user.client.Window.addWindowScrollHandler;

import static tekgenesis.view.client.ui.multiple.LensEvents.REFRESH;

/**
 * Lazy rows populate.
 */
public class LazyPopulator extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    private final BaseMultipleUI display;
    private int                  lazyCount;
    private final int            lazySize;
    private final Rangeable      rangeable;
    private HandlerRegistration  registration;

    //~ Constructors .................................................................................................................................

    public LazyPopulator(final BaseMultipleUI display, Rangeable rangeable, int size) {
        this.display   = display;
        this.rangeable = rangeable;
        lazySize       = size > DEFAULT_LAZY_SIZE ? size : DEFAULT_LAZY_SIZE;
        lazyCount      = lazySize;
        registerScrollHandler();
    }

    //~ Methods ......................................................................................................................................

    void unregister() {
        registration.removeHandler();
    }

    private void expand(Window.ScrollEvent ignored) {
        final int count = Math.min(lazyCount + lazySize, rangeable.getItemsCount());

        if (lazyCount != count) {
            final int bottom = Window.getClientHeight() + Window.getScrollTop();
            final int top    = getAbsoluteTop();
            if (top < bottom) {
                lazyCount = count;
                rangeable.setVisibleRange(new ItemsRange(0, lazyCount));
                display.react(REFRESH);
            }
        }
        else setVisible(false);
    }

    private void registerScrollHandler() {
        registration = addWindowScrollHandler(this::expand);

        // Set range for initial refresh
        rangeable.setVisibleRange(new ItemsRange(0, lazySize, false));
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_LAZY_SIZE = 30;
}  // end class LazyPopulator
