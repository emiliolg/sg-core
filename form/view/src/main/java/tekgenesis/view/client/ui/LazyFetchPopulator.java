
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

/**
 * Lazy rows populate.
 */
public class LazyFetchPopulator extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    private final LazyHandler handler;
    private final int         limit;

    private int             offset;
    private final Rangeable rangeable;

    private HandlerRegistration scrollRegistration;

    //~ Constructors .................................................................................................................................

    public LazyFetchPopulator(int initialOffset, int size, Rangeable rangeable, LazyHandler handler) {
        limit          = size > DEFAULT_LAZY_SIZE ? size : DEFAULT_LAZY_SIZE;
        this.rangeable = rangeable;
        this.handler   = handler;
        offset         = initialOffset;
        registerScrollHandler();
    }

    //~ Methods ......................................................................................................................................

    void unregister() {
        scrollRegistration.removeHandler();
    }

    private void registerScrollHandler() {
        scrollRegistration = addWindowScrollHandler(this::scroll);

        // Set range for initial refresh
        rangeable.setVisibleRange(new ItemsRange(0, limit, false));
    }

    /** React to scroll event. */
    private void scroll(Window.ScrollEvent ignored) {
        final int bottom = Window.getClientHeight() + Window.getScrollTop();
        final int top    = getAbsoluteTop();
        if (top < bottom) {
            if (requestStatus.isRequest()) {
                handler.request(offset, limit);
                requestStatus.setRequest(false);
                offset += limit;
            }
        }
    }

    //~ Methods ......................................................................................................................................

    public static LazyFetchStatus getRequestStatus() {
        return requestStatus;
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_LAZY_SIZE = 15;

    private static final LazyFetchStatus requestStatus = new LazyFetchStatus();

    //~ Inner Interfaces .............................................................................................................................

    public interface LazyHandler {
        void request(int offset, int limit);
    }

    //~ Inner Classes ................................................................................................................................

    public static class LazyFetchStatus {
        private boolean request = true;

        LazyFetchStatus() {}

        public void setRequest(boolean request) {
            this.request = request;
        }

        public boolean isRequest() {
            return request;
        }
    }
}  // end class LazyFetchPopulator
